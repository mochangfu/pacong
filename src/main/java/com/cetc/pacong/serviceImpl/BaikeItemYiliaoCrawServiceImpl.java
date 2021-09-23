package com.cetc.pacong.serviceImpl;

import com.alibaba.druid.support.json.JSONUtils;
import com.cetc.pacong.dao.DeviceCommonNameDao;
import com.cetc.pacong.downloader.CustomHttpClientDownloader;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.BaiduBaikeItem2TxtPipeline;

import com.cetc.pacong.spider.BaiduBaikeItemByKeyYiliaoDetailResolver;
import com.cetc.pacong.utils.FileUtil;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.io.*;
import java.util.*;


/**
 * created on 20/8/1.
 * 爬取指定百科词条
 */
@Component
public class BaikeItemYiliaoCrawServiceImpl  {

    @Autowired
    private DeviceCommonNameDao deviceCommonNameDao;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Scheduler scheduler = new QueueScheduler();
    private Spider spider;

    //执行这个main方法-爬取指定百科词条
    public static void main(String[] args) throws Exception {
        new BaikeItemYiliaoCrawServiceImpl().crawl1();

    }

    public void baikeItemByKeyInit(List<String> items, String savePath) {
        spider = Spider.create(new BaiduBaikeItemByKeyYiliaoDetailResolver())
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(false)
                .addPipeline(new BaiduBaikeItem2TxtPipeline(savePath, "crawled_data.txt"))
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .addUrl(getUrls(items))
                .setExitWhenComplete(true)
                .thread(1);
    }

    private String[] getUrls(List<String> items) {
        if (items == null || items.size() == 0) return null;
        List<String> urls = Lists.newArrayList();
        for (String item : items) {
            if (item.startsWith("/")) item.substring(1);
            urls.add("https://baike.baidu.com/item/" + item);
        }
        return urls.toArray(new String[0]);
    }

    /**
     * @return
     * @throws Exception
     */
    public String crawl1() throws Exception {
        String savePath = "D:/yiliaoPacongData/baikeItem/";
        if (!savePath.endsWith("/")) {
            savePath = savePath + "/";
        }
        deviceCommonNameDao.getOne();
        List<String> items = deviceCommonNameDao.findNames();

        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        String basePath = savePath;
        savePath = basePath + "baikeItem_" + time + "/";
        File fileTxt = new File(savePath + "txt/");
        File fileimage = new File(savePath + "image/");
        //如果文件夹不存在则创建
        if (!fileTxt.exists() && !fileTxt.isDirectory()) {
            fileTxt.mkdirs();
            fileimage.mkdirs();
        }
        File fileKeyMap = new File(basePath + "/keyMap.txt");
        if (!fileKeyMap.exists() || fileKeyMap.isDirectory()) {
            fileKeyMap.createNewFile();
        }
        File fileKeyTip = new File(basePath + "/别删除,keyMap文件,记录历爬取词条信息-避免重复爬取-修改保存路径后-keyMap文件手动转移.txt");
        if (!fileKeyTip.exists() || fileKeyTip.isDirectory()) {
            fileKeyTip.createNewFile();
        }



        baikeItemByKeyInit(items, savePath);
        collectItemResovle2Save();


        Integer newItem = KeySetSingleton.getInstance().getUrlKeyMapNew().size();

        saveUlrKey(basePath, KeySetSingleton.getInstance().getUrlKeyMapNew(), KeySetSingleton.getInstance().getUrlDateMapNew());
        KeySetSingleton.getInstance().clearBaikeItemKey();

        logger.info("更新所有词条" + newItem + "条");
        return "更新所有词条" + newItem + "条";
         }

    public void saveUlrKey(String savePath, Map<String, String> urlKeymap, Map<String, String> urlDatemap) {

        File file = new File(savePath + "/keyMap.txt");
        try {
            BufferedWriter bw =
                    new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file, true)));
            for (String url : urlKeymap.keySet()) {
                bw.append(urlKeymap.getOrDefault(url, "111") + "____" + url + "____" + urlDatemap.getOrDefault(url, "111") + "\r\n");
            }
            bw.close();
        } catch (Exception e) {
            logger.info("写文件keyMap失败");
        }
    }


    public void collectItemResovle2Save() {
        spider.runAsync();
        while (!Spider.Status.Stopped.equals(spider.getStatus())) {
            try {
                Thread.currentThread().sleep(10000);
            } catch (Exception e) {
                System.out.println("异常");
            }
        }
        System.out.println("结束");
        logger.info("size=" + KeySetSingleton.getInstance().getUrlKeyMapNew().size());
        logger.info(JSONUtils.toJSONString(KeySetSingleton.getInstance().getUrlKeyMapNew().values()));
    }


}
