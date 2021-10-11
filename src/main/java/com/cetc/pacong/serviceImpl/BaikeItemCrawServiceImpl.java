package com.cetc.pacong.serviceImpl;

import com.alibaba.druid.support.json.JSONUtils;
import com.cetc.pacong.listener.ResolverListener;
import com.cetc.pacong.pipeline.BaiduBaikeItem2TxtPipeline;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.spider.BaiduBaikeItemByKeyDetailResolver;
import com.cetc.pacong.spider.BaiduBaikeItemDetailResolver;
import com.cetc.pacong.downloader.CustomHttpClientDownloader;
import com.cetc.pacong.utils.FileUtil;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BaikeItemCrawServiceImpl implements ICrawService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, Object> context = Maps.newHashMap();

    private Scheduler scheduler = new QueueScheduler();
    private Spider spider;

    //执行这个main方法-爬取指定百科词条
    public static void main(String[] args) throws Exception {
        new BaikeItemCrawServiceImpl().crawl1("蔡英文,美国总统,神州五号", "D:/BaiKeCrawler/baikeItem/", false);

    }


    public void baikeItemInit(List<String> items, String savePath) {
        spider = Spider.create(new BaiduBaikeItemDetailResolver(this))
                .setDownloader(new CustomHttpClientDownloader())
                .setExitWhenComplete(false)
                .addPipeline(new BaiduBaikeItem2TxtPipeline(savePath, "crawled_data.txt"))
                .setSpiderListeners(Lists.newArrayList(new ResolverListener(savePath, "crawl_failed_url.txt", "crawl_succ_url.txt")))
                .addUrl(getUrls(items))
                .setExitWhenComplete(true)
                .thread(1);
    }

    public void baikeItemByKeyInit(List<String> items, String savePath) {
        spider = Spider.create(new BaiduBaikeItemByKeyDetailResolver(this))
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
     * @param itemKeys  词条列表 "蔡英文,美国总统,神州五号"
     * @param savePath  "D:/BaiKeCrawler/baikeItem/"
     * @param withChild
     * @return
     * @throws Exception
     */
    public String crawl1(String itemKeys, String savePath,Boolean withChild) throws Exception {
        if (savePath == null) savePath = "D:/BaiKeCrawler/baikeItem/";
        if (!savePath.endsWith("/")) {
            savePath = savePath + "/";
        }

        if (itemKeys == null) return "请添加词条";
        List<String> items = new ArrayList<>();
        String keys = "";
        for (String s : itemKeys.split(",")) {
            items.add(s);
            keys += s + ",";
            if (withChild && items.size() > 9) break;
        }

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
        KeySetSingletonInit(basePath + "/keyMap.txt");

        if (withChild == null || withChild) baikeItemInit(items, savePath);
        else baikeItemByKeyInit(items, savePath);
        collectItemResovle2Save();


        Integer newItem = KeySetSingleton.getInstance().getUrlKeyMapNew().size();

        if (newItem < 100) addDemoTxt(savePath + "txt/");
        saveUlrDate(basePath, KeySetSingleton.getInstance().getUrlKeyMapNew(), KeySetSingleton.getInstance().getUrlDateMapNew());
        KeySetSingleton.getInstance().clearBaikeItemKey();


        if (withChild != null && !withChild) {
            logger.info("更新所有词条" + newItem + "条");
            return "更新所有词条" + newItem + "条";
        }
        logger.info("更新类别 " + keys + "共" + newItem + "条,（withChild=true时词条数量大于10只取前10个）");
        return "更新类别 " + keys + "共" + newItem + "条,（withChild=true时词条数量大于10只取前10个）";
    }


    public void KeySetSingletonInit(String keyMapFile) {
        KeySetSingleton keySetSingleton = KeySetSingleton.getInstance();
        Map<String, String> map = keySetSingleton.getUrlDateMapOld();
        if (map.size() > 0) return;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(keyMapFile),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String[] names = lineTxt.split("____");
                if (names.length > 2) map.put(names[1], names[2]);
                else if (names.length > 1) map.put(names[1], names[0]);
            }
            br.close();
        } catch (Exception e) {
            logger.info("读取文件keyMap失败");
        }

    }

    public void saveUlrDate(String savePath, Map<String, String> urlKeymap, Map<String, String> urlDatemap) {

        //File file0 =new File(savePath);
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

    @Override
    public Object getContext(String key) {
        return context.get(key);
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
                        filePath.substring(0, filePath.lastIndexOf("\\")) + "\\" + name +
                                ".jpg";
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

    public void addDemoTxt(String target) {
        //File demofile=new File(target+"/demodata.txt");
        try {
            FileUtil.copyFile(".\\src\\main\\resources\\demodata.txt", target);
        } catch (Exception e) {

        }
        // demofile.renameTo( new File(target+"/demodata.txt"));
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
}
