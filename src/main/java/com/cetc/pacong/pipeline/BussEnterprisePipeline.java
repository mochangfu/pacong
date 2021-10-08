
package com.cetc.pacong.pipeline;

import com.alibaba.fastjson.JSON;
import com.cetc.pacong.domain.BussEnterprise;
import com.cetc.pacong.utils.FileUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;

/**
 * created on 20/8/1.
 */
public class BussEnterprisePipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private File txtFile;

    public BussEnterprisePipeline(String path, String txtFileName) {
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
        if (resultItems.get("resultItems") == null) {
            return;
        }
        BussEnterprise news = resultItems.get("resultItems");

        //TODO download  img

        write(news);

    }
    public void write(BussEnterprise news) {
        //TODO  write jsonDOC
        String jsonDoc =  JSON.toJSONString(news);
        FileUtil.list2txt(Lists.newArrayList(jsonDoc), txtFile.getAbsolutePath());
    }
}
