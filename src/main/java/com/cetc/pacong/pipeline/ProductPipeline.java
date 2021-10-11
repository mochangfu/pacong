
package com.cetc.pacong.pipeline;
import com.alibaba.fastjson.JSON;

import com.cetc.pacong.domain.BaiduBaikeDoc;
import com.cetc.pacong.domain.Product;
import com.cetc.pacong.utils.DownloadFileUtil;
import com.cetc.pacong.utils.FileUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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

public class ProductPipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
    }

}
