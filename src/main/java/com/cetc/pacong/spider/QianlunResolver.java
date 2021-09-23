package com.cetc.pacong.spider;

import com.alibaba.fastjson.JSON;

import com.cetc.pacong.domain.BaiduBaikeDoc;
import com.cetc.pacong.domain.News;
import com.cetc.pacong.serviceImpl.service.ICrawService;
import com.cetc.pacong.utils.Base64Util;
import com.cetc.pacong.utils.KeySetSingleton;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QianlunResolver implements PageProcessor {


    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setTimeOut(10000);


    @Override
    public void process(Page page) {

        Request request = page.getRequest();
        BaiduBaikeDoc docs = new BaiduBaikeDoc();
        docs.setDocId(Base64Util.encodeURLSafeString(request.getUrl()));

        Html html =  page.getHtml();
        Document document = html.getDocument();

        //解析标题
        Element articleHeaderEle = document.getElementsByClass("article-title").first();
        if (articleHeaderEle!=null) {
            docs.title = articleHeaderEle.text();
        }
        docs.basicInfoMap=new HashMap<>();
        docs.picLinks=new HashMap<>();
        docs.basicInfoMap.put("来源","浅论天下");
        docs.setFirstCat("论文");
        docs.from="浅论天下";

        //解析文章基本信息
        Elements elements = document.getElementsByClass("article-intro layui-clear").first().getElementsByTag("dl");
        for (Element element : elements) {
            String key =element.getElementsByTag("dt").first().text();
            String value = element.getElementsByTag("dd").first().text();
            if(key.equals("论文摘要")){
                docs.summary.add(key+":"+value);
            }else {
                docs.basicInfoMap.put(key,value);
            }

        }
        docs.url="";

        String filename=document.getElementsByClass("layui-btn layui-btn-lg layui-btn-normal layui-btn-radius")
                .first().attr("data-filename");
        String contentUrl="http://xiazai.lunwenfw.com/file/getUrl?type=down&filename="+filename;
        logger.info("contentUrl="+contentUrl);

        Map<String,String> map = KeySetSingleton.getInstance().getUrlKeyMapNew();
        map.put(contentUrl,contentUrl);

        String json =sendPost(contentUrl,"",request.getHeaders().get("Cookie"));


        Map<String,String> maps = (Map)JSON.parse(json);
        String downurl=maps.get("url");


        docs.picLinks.put(downurl,downurl);

        //docs.url=docs.docId+docs.title;

        try {
            if(map.keySet().size()%10==0){
                Thread.currentThread().sleep(10000);
                logger.info("sleep5000");
            }
            logger.info("下载数量："+map.keySet().size());
        }catch (Exception e){
        }

        page.putField("81HotNews", docs);

////////////////////

    }



    @Override
    public Site getSite() {
        return site;
    }

    private void resovleContent1(Element content, News news, Request request) {

        Elements pTagEles = content.getElementsByTag("p");
        pTagEles.forEach( p -> {
            if (p.text() != null && !"".equals(p.text()) && !"&nbsp;".equals(p.text())) {
                news.addDesc(p.text());
            }

        });
        Elements centerTagEles = content.getElementsByTag("div");
        centerTagEles.forEach(c -> {
            Elements imgTags = c.getElementsByTag("img");
            if (!imgTags.isEmpty()) {
                //center 里是图片
                String src = imgTags.first().attr("src");
                String imgUrl = (src.contains("http"))?src:resovleImgUrl1(request.getUrl(), src);
                news.addUrl(imgUrl);

            }
        });
    }
    private String resovleImgUrl1(String baseUrl, String imgSrc) {
        baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));

        return baseUrl+ "/" + imgSrc;
    }



    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String cookie) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Cookie",cookie);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}

