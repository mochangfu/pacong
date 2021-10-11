package com.cetc.pacong.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cetc.pacong.dao.ProductDao;
import com.cetc.pacong.dao.ProductProcessDao;
import com.cetc.pacong.domain.LocalParms;
import com.google.common.collect.Lists;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;

/**
 *
 */
public class McloudProductUlrListCrawler implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Site site = Site.me().setRetryTimes(3).setSleepTime(3000).setTimeOut(10000).setCharset("UTF-8");

    String detail_url = "https://mdcloud.joinchain.cn/api/database/product/getProductDetails";


    @Override
    public void process(Page page) {
        List<Request> reqList = Lists.newArrayList();

        String text = page.getRawText();
        Request request = page.getRequest();

        JSONObject jsonObject = JSON.parseObject(text);
        JSONArray records = jsonObject.getJSONArray("records");

        for (Object record : records) {
            String proId = ((JSONObject) record).getString("proId");
            if (LocalParms.idSet.contains(proId)) continue;
            String url_0 = detail_url + "?proId=" + proId;
            Request req = new Request(url_0);
            req.getHeaders().putAll(request.getHeaders());
            req.setMethod(HttpConstant.Method.GET);
            req.putExtra("currentItem", request.getExtra("currentItem"));
            reqList.add(req);
        }
        page.putField("requests", reqList);

        synchronized (this) {
            try {
                this.notify();
                this.wait();
            } catch (Exception e) {
                logger.info("", e.getMessage(), e);
            }
        }

        logger.info(" -------------------------");

    }


    @Override
    public Site getSite() {
        return site;
    }


}
