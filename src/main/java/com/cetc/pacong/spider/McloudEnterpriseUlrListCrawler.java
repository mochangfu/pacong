package com.cetc.pacong.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 *
 */
public class McloudEnterpriseUlrListCrawler implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setTimeOut(10000);
    String detail_url = "https://mdcloud.joinchain.cn/api/database/enterprise/getBaseEnterpriseDetails";//  # 工商信息
   // String detail_url = "https://mdcloud.joinchain.cn/api/database/enterprise/getBaseEnterpriseDetails";
    @Override
    public void process(Page page) {
        List<Request>  reqList = Lists.newArrayList();
       /* Html html =  page.getHtml();
        Document document = html.getDocument();*/
        String text=page.getRawText();
        Request request = page.getRequest();

        JSONObject jsonObject = JSON.parseObject(text);
        JSONArray records=jsonObject.getJSONArray("records");

        for (Object record : records) {
           String id= ((JSONObject)record).getString("id");
            String url_0=detail_url+"?id="+id;
            Request req=new Request(url_0);
            req.getHeaders().putAll(request.getHeaders());
            reqList.add(req);
        }
        page.putField("requests", reqList);

        try {
            Thread.currentThread().sleep(3000);
        }catch (Exception e){
        }
    }


    @Override
    public Site getSite() {
        return site;
    }


}
