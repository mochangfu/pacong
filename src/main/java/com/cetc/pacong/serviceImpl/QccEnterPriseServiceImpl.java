package com.cetc.pacong.serviceImpl;

import com.cetc.pacong.downloader.CustomHttpClientDownloader;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.News2TxtQianlunPipeline;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.spider.QianlunCrawler;
import com.cetc.pacong.spider.QianlunResolver;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
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
    public List<Request> getUr() {
        List<Request> list = new ArrayList<>();
        String url=base_url+"三诺生物传感股份有限公司";
            Request request = new Request(url);
            request.setMethod("get");
            request.addHeader("User-Agent", userAgent);
            request.addHeader("Cookie", cookies);
            list.add(request);
        return list;
    }

    public static void main(String[] args) throws Exception {
        new QccEnterPriseServiceImpl().crawl1("D:/yiliaoPacongData/qcc/");
    }


    @Override
    public void crawl(String site, String savePath) {

    }


    public void crawl1(String savePath) throws Exception {

        spiderResolver = Spider.create(new QianlunCrawler())
                .addRequest(getUr().toArray(new Request[1]))
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(true)
                .addPipeline (new News2TxtQianlunPipeline(savePath, "crawled_data.txt"))
                .thread(1);
        try {
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
