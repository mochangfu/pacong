package com.cetc.pacong.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cetc.pacong.downloader.CustomHttpClientDownloader;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.BussEnterprisePipeline;
import com.cetc.pacong.pipeline.EnterprisePipeline;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.spider.McloudBussEnterpriseResolver;
import com.cetc.pacong.spider.McloudBussEnterpriseUlrListCrawler;
import com.cetc.pacong.spider.McloudEnterpriseResolver;
import com.cetc.pacong.spider.McloudEnterpriseUlrListCrawler;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.File;
import java.util.*;


/**
 * created on 20/8/1.
 * 爬取指定百科词条
 */
@Component
public class McloudBussEnterpriseCrawServiceImpl implements ICrawService {

    private Map<String, Object> context = Maps.newHashMap();

    private Scheduler scheduler = new QueueScheduler();
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

    public void init(String savePath) {
        Map<String,String>headers =new HashMap<>();

        String Cookie = "deviceId=4c1932cf; mh_access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOiJpbWFnZV9jb2RlIiwidXNlcl9uYW1lIjoieHgxNDMzNjY1NjI4NDM3MzE1NTg1Iiwic2NvcGUiOlsiMTAwIl0sImlkIjoiMTQzMzY2NTYyODg0ODM1NzM3OCIsImV4cCI6MTYzMjc2Njc5NSwibG9naW5ObyI6IjVmZGRmMmZlODk0ZTRjODk4YjIzYzFiNDc1MWNmYTVmIiwiYXV0aG9yaXRpZXMiOlsiMTAwMTAwMSJdLCJqdGkiOiJhMWIwMjQ3OS1kNGEzLTQ4ZGMtYThmNi1mYTQzZmFjZGRkN2QiLCJjbGllbnRfaWQiOiJtaFdlYiJ9.qbSNNgXwZXDJmHb3lGJEffDDUSjV83DS-iM3gy9T_RI; mh_refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOiJpbWFnZV9jb2RlIiwidXNlcl9uYW1lIjoieHgxNDMzNjY1NjI4NDM3MzE1NTg1Iiwic2NvcGUiOlsiMTAwIl0sImF0aSI6ImExYjAyNDc5LWQ0YTMtNDhkYy1hOGY2LWZhNDNmYWNkZGQ3ZCIsImlkIjoiMTQzMzY2NTYyODg0ODM1NzM3OCIsImV4cCI6MTYzMjg0NTk5NSwibG9naW5ObyI6IjVmZGRmMmZlODk0ZTRjODk4YjIzYzFiNDc1MWNmYTVmIiwiYXV0aG9yaXRpZXMiOlsiMTAwMTAwMSJdLCJqdGkiOiI0YTRlMTA2OC1hNGMyLTRkMWItODczYS1kMzlkNTgwYmM5MDkiLCJjbGllbnRfaWQiOiJtaFdlYiJ9.hIS28MTrpydwVZpJrep20pKe_q-iznOEJuTqtYfVRuY; mh_expires_in=1632766795.351";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
        String Authorization="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzI4ODY2MjcsImxvZ2luTm8iOiI5OWE5NmUxZTVhOTg0YjBkYWQzYTJkYTUwNzE2N2Y2YSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiYjc5ODRlMGUtMGZhMC00ZjgzLThiM2UtOWRkNjNhY2JkMjhjIiwiY2xpZW50X2lkIjoibWhXZWIifQ.Fp893Upc-ByXCZZSZNWjeTsIJTqT_nfQF4Gnak1YeFM";
        headers.put("User-Agent",userAgent);
        headers.put("Cookie",Cookie);
        headers.put("Authorization",Authorization);
        spiderResolver = Spider.create(new McloudBussEnterpriseUlrListCrawler())
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(true)
                .setScheduler(scheduler1)
                .addPipeline((resultItems, task) -> {
                    List<Request> toCrawRequests = resultItems.get("requests");
                    toCrawRequests.forEach(r -> {
                        scheduler.push(r, spiderCrawler);
                    });
                })
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .setExitWhenComplete(true)
                .thread(1);
        spiderCrawler = Spider.create(new McloudBussEnterpriseResolver())
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(true)
                .addPipeline(new BussEnterprisePipeline(savePath, "crawled_data.txt"))
                .setScheduler(scheduler)
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .setExitWhenComplete(true)
                .thread(1);

    }

    public static void main(String[] args) throws Exception {

        new McloudBussEnterpriseCrawServiceImpl().crawl1();

    }
    public String crawl1()  {

        Map<String,String>headers =new HashMap<>();

        String Cookie = "deviceId=4c1932cf; mh_access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzI4OTM3MDksImxvZ2luTm8iOiI5OWE5NmUxZTVhOTg0YjBkYWQzYTJkYTUwNzE2N2Y2YSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiYzhlNTRlYjAtZTlhMy00NDQ0LTkxZTgtMDIwZTAyNTBjN2QwIiwiY2xpZW50X2lkIjoibWhXZWIifQ.nApZDrPAJOHy8-0N2i1i7DA0XccGygEzgNkzVH6OwqE; mh_refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJhdGkiOiJjOGU1NGViMC1lOWEzLTQ0NDQtOTFlOC0wMjBlMDI1MGM3ZDAiLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzI4OTUzOTEsImxvZ2luTm8iOiI5OWE5NmUxZTVhOTg0YjBkYWQzYTJkYTUwNzE2N2Y2YSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiMmExY2I2YjAtNWRmNS00NzkwLTkwODAtZTY0ZWZkMDZjZDFlIiwiY2xpZW50X2lkIjoibWhXZWIifQ.gj_MGqKtvNsLaXYilZHMaPfMLMFhYiQd58V0I5Fvxhk; mh_expires_in=1632893709.295";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
        String Authorization="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzeXN0ZW1JZCI6IjEwMCIsImdyYW50X3R5cGUiOm51bGwsInVzZXJfbmFtZSI6Inh4MTQzMzY2NTYyODQzNzMxNTU4NSIsInNjb3BlIjpbIjEwMCJdLCJpZCI6IjE0MzM2NjU2Mjg4NDgzNTczNzgiLCJleHAiOjE2MzI4OTM3MDksImxvZ2luTm8iOiI5OWE5NmUxZTVhOTg0YjBkYWQzYTJkYTUwNzE2N2Y2YSIsImF1dGhvcml0aWVzIjpbIjEwMDEwMDEiXSwianRpIjoiYzhlNTRlYjAtZTlhMy00NDQ0LTkxZTgtMDIwZTAyNTBjN2QwIiwiY2xpZW50X2lkIjoibWhXZWIifQ.nApZDrPAJOHy8-0N2i1i7DA0XccGygEzgNkzVH6OwqE";
        headers.put("User-Agent",userAgent);
        headers.put("Cookie",Cookie);
        headers.put("Authorization",Authorization);
        String savePath = "D:/yiliaoPacongData/BussEnterprise/";
        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        savePath = savePath + "BussEnterprise_" + time + "/";
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
            Thread.currentThread().sleep(5000);
            spiderResolver.runAsync();
            Thread.currentThread().sleep(12000);
            spiderCrawler.runAsync();
            Thread.currentThread().sleep(3000);
        } catch (Exception e) {
            System.out.println("异常");
        }
        int i = 0;
        while (!Spider.Status.Stopped.equals(spiderCrawler.getStatus()) || !Spider.Status.Stopped.equals(spiderResolver.getStatus())) {

            try {
                Thread.currentThread().sleep(10000);
                i++;
                System.out.println("正在爬取;循环次数i=" + i);
            } catch (Exception e) {
                System.out.println("异常");
            }
        }
        System.out.println("结束");
        KeySetSingleton.getInstance().setUrlKeyMapNew(new HashMap<>());
    }


    @Override
    public void collect2Resovle2Save() {
    }

    String url0 = "http://xiazai.lunwenfw.com/search?q=";


    public void initUrls(Map<String,String> headers) {
        // 控制页数
        Integer page_size = 10;
        Integer start_page = 1;
        List<String> provinces=Arrays.asList(   "北京市");
    /*    List<String> provinces=Arrays.asList(   "北京市","辽宁省", "河北省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省",
                "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "四川省", "贵州省", "云南省", "西藏自治区", "陕西省",
                "甘肃省", "山西省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区", "台湾", "内蒙古自治区");*/
        String base_url = "https://mdcloud.joinchain.cn/api/database/intermediate/getEntBusinessList?resourceId=702175b94d1e45f09e11593727b79718&pageIndex=1&pageSize=10";
        Integer count =0;

            for (String province : provinces) {
                count++;
                logger.info(count+"页-开始"+province+"-"+province);
                Map<String ,String > map=new HashMap<>();
                //map.put("year",String.valueOf(i));
                map.put("province",province);
                map.put("pageSize",page_size.toString());
                map.put("pageIndex","1");

                // map.put("city",)
                String url_0 = base_url +"&"+ urlencode(map);
                try{
                    Connection connection= Jsoup.connect(url_0).ignoreContentType(true);
                    System.out.println(url_0);
                    connection.headers(headers);
                    Document rootdocument =connection.ignoreContentType(true).get();
                    JSONObject jsonObject = JSON.parseObject(rootdocument.body().text());
                    Integer pages =(Integer)jsonObject.get("total");
                    for (int j = start_page; j <pages; j++) {
                        map.put("pageIndex",j+"");
                        url_0 =base_url + "?"+urlencode(map);
                        Request request = new Request(url_0);
                        request.getHeaders().putAll(headers);
                        request.setMethod(HttpConstant.Method.POST);

                        scheduler1.push(request,spiderResolver);
                    }
                }catch (Exception e){
                    logger.info(url_0,e.getMessage(),e);
                }
                try {
                    Thread.currentThread().sleep(3000);
                } catch (Exception e) {
                    System.out.println("异常");
                }
            }

        logger.info("等待------");
    }

    @Override
    public Object getContext(String key) {
        return context.get(key);
    }
}

