package com.cetc.pacong.serviceImpl;

import com.cetc.pacong.serviceImpl.service.ICrawService;

import com.cetc.pacong.spider.QianlunUrlListCrawler;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.News2TxtQianlunPipeline;
import com.cetc.pacong.downloader.CustomHttpClientDownloader;
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
 * 浅论天下 论文
 */
@Component
public class QianlunTianXiaServiceImpl implements ICrawService {

    private Map<String, Object> context = Maps.newHashMap();

    private Scheduler scheduler = new QueueScheduler();
    private Spider spiderResolver;//html子链接解析
    private Spider spiderCrawler;//爬取html内容

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行这个main方法-爬取 指定主题的的浅论天下 论文
    //但是cookie参数可能会变，需要自己注册浅论天下的会员，然后登录之后，就能在浏览器开发模式下查看cookie（这句话听不明白可以找个软件开发人员问一下）
    //param1是要爬取的论文主题
    public static void main(String[] args) throws Exception {
        String cookie = "__yjs_duid=1_43ff3b453cd67366e38142ce9accc9001620627591312; __root_domain_v=.lunwenfw.com; _qddaz=QD.564920627590747; PHPSESSID=3d06de8de92aeb5317f78f1af0c4b277; __cfduid=d7b16e18cbf5e5a0b3626dd66b09c2ad01620696956; userid=360982; token=KESycu8efjbPR8y4FNzp; groupid=1001; email=2541453938%40qq.com; tgw_l7_route=ffff6b93c28da37c4fe254cf7e46cc67";
        String savePath = "I:/BaiKeCrawler/lunwen/";
        String param1 = "作战数据+任务规划";
        new QianlunTianXiaServiceImpl().crawl1(savePath, cookie, param1);

    }


    @Override
    public void crawl(String site, String savePath) {

    }

    public void init(String word, String savePath, String cookie) {


        spiderResolver = Spider.create(new QianlunUrlListCrawler())
                .addRequest(getUr(cookie, word).toArray(new Request[1]))
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(true)
                .addPipeline((resultItems, task) -> {
                    List<Request> toCrawRequests = resultItems.get("requests");
                    toCrawRequests.forEach(r -> {
                        scheduler.push(r, spiderCrawler);
                    });
                })
                .thread(1);
        spiderCrawler = Spider.create(new QianlunResolver())
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(true)
                .addPipeline(new News2TxtQianlunPipeline(savePath, "crawled_data.txt"))
                .setScheduler(scheduler)
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .thread(2);

    }

    public void crawl1(String savePath, String cookie, String word) throws Exception {
        if (savePath == null) savePath = ("D:/BaiKeCrawler/lunwen/");
        if (!savePath.endsWith("/")) {
            savePath = (savePath + "/");
        }

        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        savePath = savePath + "lunwen_" + time + "/";
        File fileTxt = new File(savePath + "txt/");
        File fileimage = new File(savePath + "image/");
        //如果文件夹不存在则创建
        if (!fileTxt.exists() && !fileTxt.isDirectory()) {
            fileTxt.mkdirs();
            fileimage.mkdirs();
        }
        init(word, savePath, cookie);
        collect2Resovle2Save1();

        return;
    }

    public void collect2Resovle2Save1() {
        try {
            spiderResolver.runAsync();
            Thread.currentThread().sleep(4000);
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

    public void renameImage(String saveFilePath) throws Exception {
        File fileFolder = new File(saveFilePath);
        File[] files = fileFolder.listFiles();
        for (File fileChild : files) {
            File[] filesunzi = fileChild.listFiles();
            if (filesunzi == null) continue;
            for (File filelast : filesunzi) {
                String filePath = filelast.getAbsolutePath();
                String name = filelast.getName();
                Random rand = new Random();
                String fileName =
                        filePath.substring(0, filePath.lastIndexOf("\\")) + "\\" + name;
                filelast.renameTo(new File(fileName));
                System.out.println(fileName);
            }
        }

    }

    public void move(String from, String target) throws Exception {

        //想命名的原文件夹的路径
        File file1 = new File(from);
        //将原文件夹更改为A，其中路径是必要的。注意
        fileMove(from, target + "/image");

        new File(target + "/txt").mkdir();
        File crawled_data = new File(from + "crawled_data.txt");
        crawled_data.renameTo(new File(target + "/txt" + "/" + "crawled_data.txt"));
        if (crawled_data.exists()) {
            crawled_data.delete();
        }
        deleteDir(file1);
    }

    public static void deleteDir(File file) {
        //判断是否为文件夹
        if (file.isDirectory()) {
            //获取该文件夹下的子文件夹
            File[] files = file.listFiles();
            //循环子文件夹重复调用delete方法
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        //若为空文件夹或者文件删除，File类的删除方法
        file.delete();
    }

    public static void fileMove(String from, String to) throws Exception {
        try {
            File dir = new File(from);
            File[] files = dir.listFiles();
            if (files == null) return;
            File moveDir = new File(to);
            if (!moveDir.exists()) {
                moveDir.mkdirs();
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    fileMove(files[i].getPath()
                            , to + "\\" + files[i].getName());
                    files[i].delete();
                }
                File moveFile =
                        new File(moveDir.getPath() + "\\"
                                + files[i].getName());
                if (moveFile.exists()) {
                    moveFile.delete();
                }
                files[i].renameTo(moveFile);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void collect2Resovle2Save() {
        spiderResolver.runAsync();
        spiderCrawler.runAsync();

    }

    String url0 = "http://xiazai.lunwenfw.com/search?q=";

    //国际新闻
    public List<Request> getUr(String cookie, String word) {
        List<Request> list = new ArrayList<>();
        if (word == null || word.isEmpty())
            word = "%E4%BD%9C%E6%88%98%E6%95%B0%E6%8D%AE+%E4%BB%BB%E5%8A%A1%E8%A7%84%E5%88%92";
        //for (int y = new Date().getYear()-1; y >=2010 ; y--) {
        //String url=url0+word+"&year="+y+"&page=";
        String url = url0 + word + "&page=";
        for (int i = 1; i <= 15; i++) {

            Request request = new Request(url + i);
            request.setMethod("post");
            request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            request.addHeader("Cookie", cookie);
            list.add(request);
        }
        //}

        return list;
    }


    @Override
    public Object getContext(String key) {
        return context.get(key);
    }
}
