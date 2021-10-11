package com.cetc.pacong.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cetc.pacong.dao.ProductDao;
import com.cetc.pacong.dao.ProductProcessDao;
import com.cetc.pacong.domain.LocalParms;
import com.cetc.pacong.domain.Product;
import com.cetc.pacong.downloader.CustomHttpClientDownloader;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.ProductPipeline;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.spider.McloudProductResolver;
import com.cetc.pacong.spider.McloudProductUlrListCrawler;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.io.File;
import java.util.*;

import static com.cetc.pacong.domain.LocalParms.LISTSIZE;


/**
 * created on 20/8/1.
 * 爬取指定百科词条
 */
@Component
public class McloudProductCrawServiceImpl implements ICrawService {

    private Map<String, Object> context = Maps.newHashMap();

    private QueueScheduler schedulerUrls = new QueueScheduler();
    private QueueScheduler scheduleHhtmls = new QueueScheduler();
    private Spider spiderUrlListResolver;//html子链接解析
    private Spider spiderHtmlCrawler;//爬取html内容

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductProcessDao productProcessDao;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行这个main方法-爬取 指定主题的的浅论天下 论文
    //但是cookie参数可能会变，需要自己注册浅论天下的会员，然后登录之后，就能在浏览器开发模式下查看cookie（这句话听不明白可以找个软件开发人员问一下）
    //param1是要爬取的论文主题



    @Override
    public void crawl(String site, String savePath) {

    }
    McloudProductUlrListCrawler mcloudProductUlrListCrawler=new McloudProductUlrListCrawler();

    McloudProductResolver mcloudProductResolver=new McloudProductResolver();
    public void init(String savePath) {
        spiderUrlListResolver = Spider.create(mcloudProductUlrListCrawler)
                .setDownloader(new CustomHttpClientDownloader())
                .setScheduler(schedulerUrls)
                .addPipeline((resultItems, task) -> {
                    List<Request> toCrawRequests = resultItems.get("requests");
                    toCrawRequests.forEach(r -> {
                        scheduleHhtmls.push(r, spiderHtmlCrawler);
                    });
                }).setExitWhenComplete(false)
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .thread(1);
        mcloudProductResolver.productProcessDao=productProcessDao;
        spiderHtmlCrawler = Spider.create(mcloudProductResolver)
                .setDownloader(new CustomHttpClientDownloader())
                .addPipeline(new ProductPipeline())
                .setScheduler(scheduleHhtmls)
                .setExitWhenComplete(false)
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .thread(1);

    }
    public static void main(String[] args) throws Exception {
        new McloudProductCrawServiceImpl().crawl1();
    }
    public String crawl1()  {
        if(spiderUrlListResolver!=null)return "";
        Date date=new Date();
        //LocalParms.productBatch;
        LocalParms.update_time= date;
        Map<String,String>headers =new HashMap<>();;
        headers.putAll(HeadersParm.getHeaders());
        headers.remove("Authorization");
        String savePath = "D:/yiliaoPacongData/product/";
        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        savePath = savePath + "product_" + time + "/";
        File fileTxt = new File(savePath + "txt/");
        File fileimage = new File(savePath + "image/");
        //如果文件夹不存在则创建
        if (!fileTxt.exists() && !fileTxt.isDirectory()) {
            fileTxt.mkdirs();
            fileimage.mkdirs();
        }

        init( savePath);
        collect2Resovle2Save1(headers,  LocalParms.productBatch);
        return "";
    }
    String urlencode(Map<String,String> paramap){
        String url="";
        for (String s : paramap.keySet()) {
            if(url.length()==0) url+=s+"="+paramap.get(s);
            else url+="&"+s+"="+paramap.get(s);
        };
        return url;
    }
    public synchronized void collect2Resovle2Save1(Map<String,String>headers,String batch) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initUrls(headers,batch);
                }
            }).start();

            wait(5000);
            spiderUrlListResolver.runAsync();
            wait(1000);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notifySpiderUrlListResolver();
                }
            }).start();
            wait(4000);
            spiderHtmlCrawler.runAsync();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    insertProducts();
                }
            }).start();
        } catch (Exception e) {
            logger.info("异常",e,e.getMessage());
        }

        int i = 0;
        System.out.println("结束");
        KeySetSingleton.getInstance().setUrlKeyMapNew(new HashMap<>());
    }


    @Override
    public void collect2Resovle2Save() {
    }


    /*  @Value("${pacong.product.provinces}")
      private  String filePath;*/
    public synchronized void initUrls(Map<String,String> headers,String batch) {
        LocalParms.idSet=new HashSet<>(productDao.getIdList());
        List<String> currentItems=productProcessDao.getItems("product",batch);
        String newItem=null;

        String lastItem =currentItems.size()>0?currentItems.get(currentItems.size()-1):null;
        // 控制页数
        Integer page_size = 10;
        Integer start_page = 1;
      /*  List<String> provinces=Arrays.asList(   "北京市","辽宁省", "河北省", "吉林省", "黑龙江省", "江苏省","浙江省",  "安徽省", "福建省", "江西省", "山东省",
                "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "四川省", "贵州省", "云南省", "西藏自治区", "陕西省",
                "甘肃省", "山西省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区", "台湾", "内蒙古自治区");*/
        List<String> provinces=Arrays.asList(   "北京市","辽宁省", "河北省", "吉林省", "黑龙江省", "江苏省","浙江省");
        String base_url = "https://mdcloud.joinchain.cn/api/search/product/getProductList";
        Integer count =0;
        for (int i = 2021; i> 2000; i--) {
            for (String province : provinces) {
                if(lastItem!=null&&!lastItem.contains(i + "-" + province))continue;
                count++;
                //   logger.info(count+"开始"+i+"年"+province);
                Map<String ,String > map=new HashMap<>();
                map.put("year",String.valueOf(i));
                map.put("province",province);
                map.put("pageSize",page_size.toString());
                map.put("pageIndex","1");
                String url_0 = base_url +"?"+ urlencode(map);
                try{
                    Connection connection= Jsoup.connect(url_0).ignoreContentType(true);
                    System.out.println(url_0);
                    connection.headers(map);
                    Document rootdocument =connection.ignoreContentType(true).get();
                    JSONObject jsonObject = JSON.parseObject(rootdocument.body().text());
                    Integer pages =(Integer)jsonObject.get("pages");
                    if(pages==null)pages=1;
                    for (int j = start_page; j <=pages; j++) {

                        map.put("pageIndex",j+"");
                        url_0 =base_url + "?"+urlencode(map);
                        Request request = new Request(url_0);
                        newItem = i + "-" + province + "-" + j + "";
                        request.getHeaders().putAll(headers);
                        if(!currentItems.contains(newItem)){
                            if(McloudProductResolver.lastItem==null)McloudProductResolver.lastItem=newItem;
                            request.putExtra("currentItem",newItem);
                            schedulerUrls.push(request, spiderUrlListResolver);
                        }

                    }
                }catch (Exception e){
                    logger.info(url_0,e.getMessage(),e);
                }
                try {
                    wait(5000);
                } catch (Exception e) {
                    logger.info("异常",e.getMessage(),e);
                }
            }
        }
        logger.info("等待------");
    }

    public void insertProducts() {
        while (true){
            synchronized (mcloudProductResolver){
                if(mcloudProductResolver.list.size()<LISTSIZE){
                    try {
                        mcloudProductResolver.wait();
                    }catch (Exception e){
                        logger.info("",e.getMessage(),e);
                    }
                }
                try{
                    List<Product> list= new ArrayList<>();list.addAll(mcloudProductResolver.list);
                    productDao.addItems(list);
                }catch (Exception e){
                    logger.info(e.getMessage(),e);
                }

                mcloudProductResolver.list.clear();
                logger.info("完成插入();");
                mcloudProductResolver.notifyAll();
            }


        }
    }
    public void notifySpiderUrlListResolver(){
        while (true){
            synchronized (mcloudProductUlrListCrawler){
                try {
                    mcloudProductUlrListCrawler.wait(1000);
                    if(scheduleHhtmls.getLeftRequestsCount(spiderHtmlCrawler)<2){
                        mcloudProductUlrListCrawler.notify();
                    }
                }catch (Exception e){
                    logger.info(" lock.wait();",e.getMessage(),e);
                }finally {
                }
            }
        }
    }

    @Override
    public Object getContext(String key) {
        return context.get(key);
    }
}

