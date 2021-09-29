package com.cetc.pacong.pipeline;

import com.alibaba.fastjson.JSON;
import com.cetc.pacong.domain.QccEnt;
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

/**
 * created on 20/8/1.
 */
public class QccPipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private File txtFile;

    public QccPipeline(String path, String txtFileName) {
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
        if (resultItems.get("qccEnt") == null) {
            return;
        }
        QccEnt news = resultItems.get("qccEnt");

                //TODO download  img
        downLoadImg(news.getImageUrl(), news.getKeyNo(),news);
        write(news);

    }

    private void downLoadImg(String picUrl, String keyNo, QccEnt news) {

        //保存图片到文件
        String fileName=keyNo +".jpg";
        try {
            DownloadFileUtil.downLoadFromUrl(picUrl, fileName, path);
        } catch (IOException ioException) {
            logger.error(String.format("download img failed ： %s", ioException));
        }
    }

    public void write(QccEnt news) {
        //TODO  write jsonDOC
        String jsonDoc =  JSON.toJSONString(news);
        FileUtil.list2txt(Lists.newArrayList(jsonDoc), txtFile.getAbsolutePath());
    }
}
