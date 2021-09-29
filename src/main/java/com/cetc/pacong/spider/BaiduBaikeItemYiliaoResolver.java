package com.cetc.pacong.spider;

import com.cetc.pacong.domain.BaiduBaikeDoc;
import com.cetc.pacong.domain.BaikeItem;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.utils.KeySetSingleton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaiduBaikeItemYiliaoResolver implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(5000);




    @Override
    public void process(Page page) {
        Set<String> keyUrlSet = KeySetSingleton.getInstance().getUrlDateMapOld().keySet();
        Set<String> keyUrlSetNew = KeySetSingleton.getInstance().getUrlKeyMapNew().keySet();

        Html html =  page.getHtml();
        Request request = page.getRequest();
        String url=request.getUrl();
        if(keyUrlSetNew.contains(url))return;
        BaikeItem doc = new BaikeItem();
        Document document = html.getDocument();

        //无法精确匹配，查找推荐
        if ("true".equals(page.getRequest().getExtra("secondPage"))){
            String key=(String) request.getExtra("key");
            Elements searchList = document.getElementsByClass("search-list");
            if(searchList.size()>0&&searchList.get(0).getElementsByTag("a").size()>0){ //无法精确匹配，有推荐
                Elements items = searchList.get(0).getElementsByTag("a");
                Element item =items.get(0);
                if (item.attr("href") != null && item.attr("target") != null)
                    if (item.attr("target").equals("_blank") && item.attr("href").contains("/item/")) {
                        String href = item.attr("href");
                        if (href.startsWith("/item")) href = "https://baike.baidu.com" + href;
                        page.addTargetRequest(new Request(href).putExtra("key", key));
                    }
            }else {//无推荐
                logger.info("失败："+key);
                File file=new File("D:/BaiKeCrawler/baikeItemyiliao"+"/keyMapFail.txt");
                try{
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                    bw.append(key+"\r\n");
                    bw.close();
                }catch (Exception e){
                    logger.info("写文件keyMap失败");
                }
                return;
            }
            return;
        }
        String key=request.getUrl();
        key=key.replace("https://baike.baidu.com/item/","");
        try { key= URLDecoder.decode("%E4%BD%A0%E5%A5%BD", "utf-8");
        }catch (Exception e){
        }

        Elements mainElement=document.getElementsByClass("main-content");
        if(mainElement.size()>0){  //直接精确匹配
            Element mainContentElement = mainElement.get(0);
            //获取标题
            Elements titleEles = mainContentElement.getElementsByClass("lemmaWgt-lemmaTitle-title");
            if (!titleEles.isEmpty()) {
                doc.title = titleEles.first().getElementsByTag("h1").first().text();

            }else if(!document.getElementsByClass("lemmaWgt-lemmaTitle-title").isEmpty()){
                doc.title = document.getElementsByClass("lemmaWgt-lemmaTitle-title").first().getElementsByTag("h1").first().text();
            }
            doc.url=request.getUrl();
            if(request.getExtra("key")!=null){
                doc.key=(String) request.getExtra("key");
                logger.info("替换："+key+"-为-"+doc.title);
            }else {
                doc.key=doc.title;
            }

            KeySetSingleton.getInstance().getUrlKeyMapNew().put(page.getRequest().getUrl(),key);

            page.putField("baikeDoc", doc);
            logger.info("成功数量"+KeySetSingleton.getInstance().getUrlKeyMapNew().keySet().size());
            return;
        }else {//不能直接精确匹配，然后查询相关词条
            if (page.getRequest().getUrl().contains("search")) return;
            {
                Elements searchList = document.getElementsByClass("search-list");
                if (searchList.size() == 0) {
                    if(page.getRequest().getUrl().contains("search"))return;
                    String url1= "https://baike.baidu.com/search/none?pn=0&rn=10&enc=utf8&word="+page.getRequest().getUrl().replace("https://baike.baidu.com/item/","");
                    if(keyUrlSet.contains(url1)) return;

                    page.addTargetRequest( new Request(url1).putExtra("secondPage", "true").putExtra("key", key));
                    return;

                }
            }
            return;
        }


    }



    @Override
    public Site getSite() {
        return site;
    }


}
