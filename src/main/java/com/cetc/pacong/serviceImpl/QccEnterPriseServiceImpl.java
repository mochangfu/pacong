package com.cetc.pacong.serviceImpl;

import com.cetc.pacong.downloader.CustomHttpClientDownloader;

import com.cetc.pacong.pipeline.QccPipeline;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.spider.QccResolver;
import com.cetc.pacong.spider.QianlunUrlListCrawler;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.io.File;
import java.util.*;

/**
 * created on 20/8/1.
 * 企查查企业信息
 */
@Component
public class QccEnterPriseServiceImpl implements ICrawService {

    private Map<String, Object> context = Maps.newHashMap();

    private Spider spiderResolver;//html子链接解析
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    String userAgent ="";
    String cookies ="";
    String base_url = "https://www.qcc.com/web/search?key=";
    public void getUr() {
        List<String> nameList=Arrays.asList(   "三诺生物传感股份有限公司");
        List<Request> list = new ArrayList<>();
        for (String name : nameList) {
            String url=base_url+name;
            Request request = new Request(url);
            request.setMethod("get");
            request.addHeader("User-Agent", userAgent);
            request.addHeader("Cookie", cookies);
            scheduler1.push(request,spiderResolver);
        }

        return ;
    }

    public static void main(String[] args) throws Exception {
        String savePath = "D:/yiliaoPacongData/qcc/";
        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        savePath = savePath + "qcc_" + time + "/";
        File fileTxt = new File(savePath + "txt/");
        File fileimage = new File(savePath + "image/");
        //如果文件夹不存在则创建
        if (!fileTxt.exists() && !fileTxt.isDirectory()) {
            fileTxt.mkdirs();
            fileimage.mkdirs();
        }
        new QccEnterPriseServiceImpl().crawl1(savePath);
    }


    @Override
    public void crawl(String site, String savePath) {

    }

    private Scheduler scheduler1 = new QueueScheduler();
    public void crawl1(String savePath) throws Exception {
        spiderResolver = Spider.create(new QccResolver())
             /*   .addRequest(getUr().toArray(new Request[1]))*/
                .setScheduler(scheduler1)
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(true)
                //.setEmptySleepTime(2000)
                .addPipeline (new QccPipeline(savePath, "crawled_data.txt"))
                .thread(1);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getUr();
                }
            }).start();
            Thread.currentThread().sleep(5000);
            spiderResolver.runAsync();
            Thread.currentThread().sleep(1000);
        } catch (Exception e) {
            System.out.println("异常");
        }
        return;
    }

    @Override
    public void collect2Resovle2Save() { }

    @Override
    public Object getContext(String key) { return context.get(key);
    }
}
