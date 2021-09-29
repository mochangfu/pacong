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
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class McloudBussEnterpriseUlrListCrawler implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setTimeOut(10000);
    String detail_url = "https://mdcloud.joinchain.cn/api/database/intermediate/getEntBusinessDetails";//  # 工商信息
   // String detail_url = "https://mdcloud.joinchain.cn/api/database/enterprise/getBaseEnterpriseDetails";
    @Override
    public void process(Page page) {
        List<Request>  reqList = Lists.newArrayList();
        String text=page.getRawText();
        Request request = page.getRequest();

        JSONObject jsonObject = JSON.parseObject(text);
        JSONArray records=jsonObject.getJSONArray("records");

        for (Object record : records) {
            String id= ((JSONObject)record).getString("entId");
            String enterpriseName= ((JSONObject)record).getString("enterpriseName");
            String url_0=detail_url;
            Request req=new Request(url_0);
            Map<String ,Object > mapBody=new HashMap<>();
            mapBody.put("entId",id);
            mapBody.put("enterpriseName",enterpriseName);
            request.setRequestBody(HttpRequestBody.form(mapBody,"utf-8"));
            request.setMethod(HttpConstant.Method.POST);
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
