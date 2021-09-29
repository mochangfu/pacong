package com.cetc.pacong.pipeline;

import com.alibaba.fastjson.JSON;
import com.cetc.pacong.domain.BaiduBaikeDoc;
import com.cetc.pacong.domain.BaikeItem;
import com.cetc.pacong.utils.Base64Util;
import com.cetc.pacong.utils.DownloadFileUtil;
import com.cetc.pacong.utils.FileUtil;
import com.cetc.pacong.utils.UrlUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * created on 20/8/1.
 */
public class BaiduYiliaoItem2TxtPipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private File txtFile;

    public BaiduYiliaoItem2TxtPipeline(String path, String txtFileName) {
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

        if (resultItems.get("baikeDoc") == null) {
            return;
        }
        BaikeItem baikeDoc = resultItems.get("baikeDoc");
        write(baikeDoc);

        //TODO download  img
        downLoadImg(new HashSet<>(),baikeDoc.getDocId());

    }

    private synchronized void downLoadImg(Set<String> imgUrls, String docId) {
        if (imgUrls.isEmpty()) {
            return;
        }

        //保存图片到文件
        try {
            for (String picUrl : imgUrls) {
                DownloadFileUtil.downLoadFromUrl(picUrl, Base64Util.encode(UrlUtil.simpleFileName(picUrl))+".jpg", path + docId);
            }

        } catch (IOException ioException) {
            logger.error(String.format("download img failed ： %s", ioException));
//            failedUrls.add(picUrl);
        }
    }

    public synchronized void write(BaikeItem baikeDoc ) {
        //TODO  write jsonDOC
        String jsonDoc =  JSON.toJSONString(baikeDoc);
        FileUtil.list2txt(Lists.newArrayList(jsonDoc), txtFile.getAbsolutePath());
    }
}
