package com.cetc.pacong.listener;

import com.cetc.pacong.utils.FileUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;

public class ResolverListener extends FilePersistentBase implements SpiderListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private File failedFile;
    private File successedFile;

    public ResolverListener(String path, String failFileName, String successFileName) {
        setPath(path);
        failedFile = getFile(path + failFileName);
        successedFile = getFile(path + successFileName);
    }
    @Override
    public void onSuccess(Request request) {
        //todo 写入爬取成功文件
        String url = request.getUrl();
        write(url, successedFile);

    }

    @Override
    public void onError(Request request) {
        //todo 写入爬取失败文件
        String url = request.getUrl();
        write(url, failedFile);
    }

    public void write(String url, File file) {

        FileUtil.list2txt(Lists.newArrayList(url), file.getAbsolutePath());
    }
}
