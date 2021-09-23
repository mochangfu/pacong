package com.cetc.pacong.spider;

import com.google.common.collect.Lists;
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

import java.util.List;

/**
 *
 */
public class QianlunCrawler implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setTimeOut(10000);


    @Override
    public void process(Page page) {

        List<Request>  reqList = Lists.newArrayList();
        Html html =  page.getHtml();
        Document document = html.getDocument();
        Request req = page.getRequest();

        Elements searchListEles = document.getElementsByTag("tbody").first().getElementsByTag("tr");

            if (!searchListEles.isEmpty()) {
                    int i=0;
                    for (Element tr : searchListEles) {
                        try{
                            String url = tr.getElementsByTag("td").get(1).getElementsByTag("a").first().attr("href");
                            reqList.add(new Request(url).addHeader("Cookie",req.getHeaders().get("Cookie")));
                        }catch (Exception e){
                            logger.info(String.format("解析出错", e.getMessage(),e));
                        }
                    }
                    }
                    logger.info(String.format("add success reqList size: %s", reqList.size()));
            page.putField("requests", reqList);

    }


    @Override
    public Site getSite() {
        return site;
    }


}
