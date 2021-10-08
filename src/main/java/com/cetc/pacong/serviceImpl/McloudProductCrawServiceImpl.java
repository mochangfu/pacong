package com.cetc.pacong.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cetc.pacong.dao.ProductDao;
import com.cetc.pacong.downloader.CustomHttpClientDownloader;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.ProductPipeline;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.spider.McloudProductResolver;
import com.cetc.pacong.spider.McloudProductUlrListCrawler;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.io.File;
import java.util.*;


/**
 * created on 20/8/1.
 * 爬取指定百科词条
 */
@Component
public class McloudProductCrawServiceImpl implements ICrawService {

    private Map<String, Object> context = Maps.newHashMap();

    private Scheduler scheduler0 = new QueueScheduler();
    private Scheduler scheduler1 = new QueueScheduler();
    private Spider spiderResolver;//html子链接解析
    private Spider spiderCrawler;//爬取html内容


    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行这个main方法-爬取 指定主题的的浅论天下 论文
    //但是cookie参数可能会变，需要自己注册浅论天下的会员，然后登录之后，就能在浏览器开发模式下查看cookie（这句话听不明白可以找个软件开发人员问一下）
    //param1是要爬取的论文主题



    @Override
    public void crawl(String site, String savePath) {

    }

    McloudProductResolver mcloudProductResolver=new McloudProductResolver();
    public void init(String savePath) {
        spiderResolver = Spider.create(new McloudProductUlrListCrawler())
                .setDownloader(new CustomHttpClientDownloader())
                .setScheduler(scheduler0)
                .addPipeline((resultItems, task) -> {
                    List<Request> toCrawRequests = resultItems.get("requests");
                    toCrawRequests.forEach(r -> {
                        scheduler1.push(r, spiderCrawler);
                    });
                })
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .thread(1);

        spiderCrawler = Spider.create(mcloudProductResolver)
                .setDownloader(new CustomHttpClientDownloader())
                .addPipeline(new ProductPipeline(savePath, "crawled_data.txt"))
                .setScheduler(scheduler1)
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .thread(1);

    }

    public static void main(String[] args) throws Exception {
        new McloudProductCrawServiceImpl().crawl1();
    }
    public String crawl1()  {

        Map<String,String>headers = HeadersParm.getHeaders();
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
        collect2Resovle2Save1(headers);
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
    public void collect2Resovle2Save1(Map<String,String>headers) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initUrls(headers);
                }
            }).start();
            Thread.currentThread().sleep(10000);
            spiderResolver.runAsync();
            Thread.currentThread().sleep(10000);
            spiderCrawler.runAsync();
            Thread.currentThread().sleep(10000);
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
        /*while (!Spider.Status.Stopped.equals(spiderCrawler.getStatus()) || !Spider.Status.Stopped.equals(spiderResolver.getStatus())) {
            try {
                Thread.currentThread().sleep(10000);
                i++;
                System.out.println("正在爬取;循环次数i=" + i);
            } catch (Exception e) {
                System.out.println("异常");
            }
        }*/
        System.out.println("结束");
        KeySetSingleton.getInstance().setUrlKeyMapNew(new HashMap<>());
    }


    @Override
    public void collect2Resovle2Save() {
    }

    String url0 = "http://xiazai.lunwenfw.com/search?q=";


    @Autowired
    private ProductDao productDao;
    public void initUrls(Map<String,String> headers) {
        // 控制页数
        Integer page_size = 10;
        Integer start_page = 1;
        List<String> provinces=Arrays.asList(   "北京市");
    /*    List<String> provinces=Arrays.asList(   "北京市","辽宁省", "河北省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省",
                "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "四川省", "贵州省", "云南省", "西藏自治区", "陕西省",
                "甘肃省", "山西省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区", "台湾", "内蒙古自治区");*/
        String base_url = "https://mdcloud.joinchain.cn/api/search/product/getProductList";
        Integer count =0;
        for (int i = 2020; i < 2021; i++) {
            for (String province : provinces) {
                count++;
                logger.info(count+"页-开始"+i+"年"+province);
                Map<String ,String > map=new HashMap<>();
                map.put("year",String.valueOf(i));
                map.put("province",province);
                map.put("pageSize",page_size.toString());
                map.put("pageIndex","1");
                // map.put("city",)
                String url_0 = base_url +"?"+ urlencode(map);
                try{
                    Connection connection= Jsoup.connect(url_0).ignoreContentType(true);
                    System.out.println(url_0);
                    connection.headers(map);
                    Document rootdocument =connection.ignoreContentType(true).get();
                    JSONObject jsonObject = JSON.parseObject(rootdocument.body().text());
                    Integer pages =(Integer)jsonObject.get("pages");
                    for (int j = start_page; j <pages; j++) {
                        map.put("pageIndex",j+"");
                        url_0 =base_url + "?"+urlencode(map);
                        Request request = new Request(url_0);
                        request.getHeaders().putAll(headers);
                        scheduler0.push(request,spiderResolver);
                    }
                }catch (Exception e){
                    logger.info(url_0,e.getMessage(),e);
                }
                try {
                    Thread.sleep(20000);
                } catch (Exception e) {
                  logger.info("异常",e.getMessage(),e);
                }
            }
        }
        logger.info("等待------");
    }

    public void insertProducts() {

        while (true){
            synchronized (mcloudProductResolver.list){
                if(mcloudProductResolver.list.size()<3){
                    try {
                        mcloudProductResolver.list.wait();
                    }catch (Exception e){
                        logger.info("异常();");
                    }
                }
                productDao.addItems(mcloudProductResolver.list);
                mcloudProductResolver.list.clear();
                logger.info("完成插入();");
                mcloudProductResolver.list.notifyAll();
            }


        }
}


    @Override
    public Object getContext(String key) {
        return context.get(key);
    }
}

