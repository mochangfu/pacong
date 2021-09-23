package com.cetc.pacong.pipeline;

import com.alibaba.fastjson.JSON;

import com.cetc.pacong.domain.BaiduBaikeDoc;
import com.cetc.pacong.utils.DownloadFileUtil;
import com.cetc.pacong.utils.FileUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * created on 20/8/1.
 */
public class News2TxtQianlunPipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private File txtFile;

    public News2TxtQianlunPipeline(String path, String txtFileName) {
        setPath(path+"image/");
        txtFile = getFile(path+"txt/"+ txtFileName);
    }
    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("81HotNews") == null) {
            return;
        }
        BaiduBaikeDoc news = resultItems.get("81HotNews");

                //TODO download  img
        downLoadImg(news.picLinks.keySet(), news.getDocId(),news);
        write(news);

    }

    private void downLoadImg(Set<String> imgUrls, String docId, BaiduBaikeDoc news) {
        if (imgUrls.isEmpty()) {
            return;
        }


        //保存图片到文件
        String fileName=new Date().getTime() +".pdf";
        try {
            for (String picUrl : imgUrls) {
                DownloadFileUtil.downLoadFromUrl(picUrl, fileName, path + docId);
            }
            //news.setTitle(news.title);
            news.setFileName(fileName);
        } catch (IOException ioException) {
            logger.error(String.format("download img failed ： %s", ioException));
        }
    }

    public void write(BaiduBaikeDoc news) {
        //TODO  write jsonDOC
        String jsonDoc =  JSON.toJSONString(news);
        FileUtil.list2txt(Lists.newArrayList(jsonDoc), txtFile.getAbsolutePath());
    }
}
