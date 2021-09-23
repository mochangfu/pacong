package com.cetc.pacong.spider;

import com.cetc.pacong.domain.BaiduBaikeDoc;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.utils.Base64Util;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaiduBaikeItemByKeyDetailResolver implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(5000);
//    public static ArrayBlockingQueue<BaiduBaikeDetailResolver.Term> blockingQueue = new ArrayBlockingQueue<BaiduBaikeDetailResolver.Term>(10000);

    private ICrawService crawContr;

    public BaiduBaikeItemByKeyDetailResolver(ICrawService crawContr) {
        this.crawContr = crawContr;
    }

    @Override
    public void process(Page page) {
        Set<String> keyUrlSet = KeySetSingleton.getInstance().getUrlDateMapOld().keySet();
        Set<String> keyUrlSetNew = KeySetSingleton.getInstance().getUrlKeyMapNew().keySet();
        if ("false".equals(page.getRequest().getExtra("secondPage")))
            if(keyUrlSet.contains(page.getRequest().getUrl())){
                return;
            }
        System.out.println("第几条-"+ keyUrlSetNew.size());
        BaiduBaikeDoc doc = new BaiduBaikeDoc();
        //List<String> failedUrls = Lists.newArrayList();
        Html html =  page.getHtml();
        Request request = page.getRequest();
        Document document = html.getDocument();
        Elements mainElement=document.getElementsByClass("main-content");

        if(mainElement.size()==0){
            Elements searchList = document.getElementsByClass("search-list");
            if(searchList.size()==0){
                if(page.getRequest().getUrl().contains("search"))return;
                String url1= "https://baike.baidu.com/search/none?pn=0&rn=10&enc=utf8&word="+page.getRequest().getUrl().replace("https://baike.baidu.com/item/","");
                if(keyUrlSet.contains(url1))
                    return;

                page.addTargetRequest(  new Request(url1).putExtra("secondPage", "true"));
                return;
            }else {
                Elements items = searchList.get(0).getElementsByTag("a");
                if ("true".equals(page.getRequest().getExtra("secondPage"))||page.getRequest().getExtra("secondPage")==null) {
                    List<String> itemHrefs = new ArrayList<>();
                    items.forEach(item -> {
                        if (item.attr("href") != null && item.attr("target") != null)
                            if (item.attr("target").equals("_blank") && item.attr("href").contains("/item/")) {
                                String href=item.attr("href");
                                if(href.startsWith("/item"))href="https://baike.baidu.com"+href;
                                itemHrefs.add(href);
                            }
                    });
                    for (String itemHref : itemHrefs) {
                        if(keyUrlSet.contains(itemHref))
                            continue;
                        page.addTargetRequest(  new Request(itemHref).putExtra("secondPage", "false"));
                    }
                    logger.info(doc.title+"-itemHref二级词条数量="+itemHrefs.size());
                }
                return;
            }
        }


        Element mainContentElement = mainElement.get(0);
        doc.setUrl(request.getUrl());

        //获取标题
        Elements titleEles = mainContentElement.getElementsByClass("lemmaWgt-lemmaTitle-title");
        if (!titleEles.isEmpty()) {
            doc.title = titleEles.first().getElementsByTag("h1").first().text();

        }else if(!document.getElementsByClass("lemmaWgt-lemmaTitle-title").isEmpty()){
            doc.title = document.getElementsByClass("lemmaWgt-lemmaTitle-title").first().getElementsByTag("h1").first().text();
        }

        String date ="(2021-01-01)";
        Elements sideContent = document.getElementsByClass("side-box lemma-statistics");
        Elements jmodifiedtime = sideContent.first()!=null?sideContent.first().getElementsByClass("j-modified-time"):null;
        if (jmodifiedtime!=null&&!jmodifiedtime.isEmpty()){
            date = jmodifiedtime.first().text();
        }
        KeySetSingleton.getInstance().getUrlDateMapNew().put(page.getRequest().getUrl(),date);
        KeySetSingleton.getInstance().getUrlDateMapOld().put(page.getRequest().getUrl(),doc.title);
        KeySetSingleton.getInstance().getUrlKeyMapNew().put(page.getRequest().getUrl(),doc.title);
        System.out.println(doc.getTitle());
        //获取 摘要信息
        Elements summaryElements = mainContentElement.getElementsByClass("lemma-summary");
        if (!summaryElements.isEmpty()) {
            Element summaryEle = summaryElements.first();
            Elements paraEles = summaryEle.getElementsByClass("para");
            paraEles.forEach(para -> {
                doc.summary.add(para.text());
            });
        }

        //爬取basic-info 基本信息
        Elements basicInfoEles = mainContentElement.getElementsByClass("basic-info cmn-clearfix");
        if (! basicInfoEles.isEmpty()) {
            Element basicInfoEle = basicInfoEles.first();
            Elements itemNames = basicInfoEle.getElementsByClass("basicInfo-item name");
            Elements itemValues = basicInfoEle.getElementsByClass("basicInfo-item value");
            if (!itemNames.isEmpty() && !itemValues.isEmpty() && itemNames.size() == itemValues.size()) {
                for (int i = 0; i < itemNames.size(); i++) {
                    Element valueElement = itemValues.get(i);
                    String relation = itemNames.get(i).text();
                    doc.basicInfoMap.put(relation, valueElement.text());
                }
            }
        }

        //获取目录
        Elements catalogEles = mainContentElement.getElementsByAttributeValueContaining("class", "catalog-list");
        if (!catalogEles.isEmpty()) {
            Element catalogEle = catalogEles.first();
            Elements level1Eles = catalogEle.getElementsByClass("level1");
            if (!level1Eles.isEmpty()) {
                level1Eles.forEach(l -> {
                    String text = l.getElementsByClass("text").text();
                    doc.catalogList.add(text);
                });
            }

        }

        //获取1级标题及正文内容
        Elements level2TitleEles  = mainContentElement.getElementsByClass("para-title level-2");
        if (!level2TitleEles.isEmpty()) {
            level2TitleEles.forEach(l2te -> {
                Map<String, List<String>> paragraph = processPara(l2te);
                Element l2tEle = l2te.getElementsByClass("title-text").get(0);
                l2tEle.getElementsByClass("title-prefix").remove();
                doc.paraMaps.put(l2tEle.text(), paragraph);
            });
        }
        //获取参考文献
        Elements referenceElements = mainContentElement.getElementsByClass("reference-item reference-item--type1 ");
        if (!referenceElements.isEmpty()) {
            referenceElements.forEach(re -> {
                doc.referenceList.add(re.text());
            });
        }

        //保存概述图

        Elements summaryPicEles = document.getElementsByClass("summary-pic");
        if (!summaryPicEles.isEmpty()) {
            Elements imgEles = summaryPicEles.first().getElementsByTag("img");
            if (!imgEles.isEmpty()) {
                Element imgElement = imgEles.first();
                doc.picLinks.put(imgElement.attr("src"), imgElement.text());
            }
        }

        //图片链接待爬取
        Elements picEles = mainContentElement.getElementsByClass("picture");
        if (!picEles.isEmpty()) {
            picEles.forEach( e -> {
                String picName = e.attr("alt");
                String picUrl = e.attr("src");
                doc.picLinks.put(picUrl, picName);

            });
        }

        picEles = mainContentElement.getElementsByClass("lazy-img");
        if (!picEles.isEmpty()) {
            picEles.forEach( e -> {
                String picName = e.attr("alt");
                String picUrl = e.attr("data-src");
                doc.picLinks.put(picUrl, picName);

            });
        }
        //爬取词条相册内容
        Elements albumListEles = mainContentElement.getElementsByClass("album-list");

        if (!albumListEles.isEmpty()) {
            Element albumEle = albumListEles.first();
            Elements imgEles = albumEle.getElementsByTag("img");
            if (!imgEles.isEmpty()) {
                imgEles.forEach( i -> {
                    String url = i.attr("src");
                    String name = i.attr("alt");
                    if (null != url) {
                        doc.picLinks.put(url, name);
                    }
                });
            }
        }


        doc.docId = Base64Util.encodeURLSafeString(page.getRequest().getUrl());
        if (!"true".equals(page.getRequest().getExtra("secondPage"))) {
            //一级页面才放类目信息了
            doc.firstCat = crawContr.getContext("firstCat") == null ? "" : (String)crawContr.getContext("firstCat");
            doc.secCat = crawContr.getContext("secCat") == null ? "" : (String)crawContr.getContext("secCat");
            doc.thirdCat = crawContr.getContext("thirdCat") == null ? "" : (String)crawContr.getContext("thirdCat");
            doc.fourthCat = request.getExtra("fourthCat") == null ? "" : (String)request.getExtra("fourthCat");
        }

        doc.url = request.getUrl();

        //校验各个字段；
        checkFields(doc);
        page.putField("baikeDoc", doc);

        return;
    }

    private void checkFields(BaiduBaikeDoc doc) {
        if ("".equals(doc.getDocId()) || "".equals(doc.getTitle()) || doc.getParaMaps().isEmpty()) {
            throw new IllegalStateException("some fields are empty ");
        }
    }

    private Map<String, List<String>> processPara(Element l2e) {
        Element nextEle = l2e.nextElementSibling();
        Map<String, List<String>> resMap = Maps.newHashMap();
        while (nextEle != null) {
          if ("para-title level-2".equals(nextEle.attr("class"))
                    && "para-title".equals(nextEle.attr("label-module"))) {
                //已经是下一个段落，直接return
                return resMap;
            } else if (("para-title level-3".equals(nextEle.attr("class"))
                    && "para-title".equals(nextEle.attr("label-module")))
                    ||("para".equals(nextEle.attr("class"))
                    && "para".equals(nextEle.attr("label-module")))) {

                 //处理正文和三级标题
                //三级标题 降为正文处理

                if(!nextEle.getElementsByClass("description").isEmpty()) {
                  //去掉图片下方的描述文字
                  nextEle.getElementsByClass("description").remove();
                }
                String text = nextEle.text();
                List<String> texts = resMap.getOrDefault("texts", Lists.newArrayList());
                resMap.put("texts", texts);
                texts.add(text);

              nextEle = nextEle.nextElementSibling();

          }else if ("table".equals(nextEle.tagName())) {
                //处理表格信息
                String table = processTable(nextEle);
                List<String> tables = resMap.getOrDefault("tables", Lists.newArrayList());
                resMap.put("tables",tables );
                tables.add(table);
                nextEle = nextEle.nextElementSibling();

            }else {
                nextEle = nextEle.nextElementSibling();
            }
        }
        return resMap;

    }

    private String processTable(Element tableEle) {
        tableEle.attr("style", "margin: 0;\n" +
                "    border-collapse: collapse;\n" +
                "    word-wrap: break-word;\n" +
                "    word-break: break-all;\n" +
                "    font-size: 12px;\n" +
                "    line-height: 22px;\n" +
                "    color: #000;\n" +
                "    border: 1px; \n" +
                "    solid: #e6e6e6; \n" +
                "    padding: 2px 10px; \n" +
                "    height: auto; \n" +
                "    text-indent: 0; \n" +
                "    text-shadow: 0 0 black;");
        tableEle.attr("border", "1");
        tableEle.attr("cellpadding", "6");

        Elements aEles = tableEle.getElementsByTag("a");
        if (!aEles.isEmpty()) {
            aEles.forEach( a -> {
                a.tagName("span");
            });
        }
        return tableEle.toString();
    }



    @Override
    public Site getSite() {
        return site;
    }
/*
    public static void main(String[] args) {

        Spider.create(new BaiduBaikeItemDetailResolver(null))
                .test("http://baike.baidu.com/view/281065.htm");
    }*/




}
