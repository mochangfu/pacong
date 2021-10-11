package com.cetc.pacong.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cetc.pacong.dao.ProductProcessDao;
import com.cetc.pacong.domain.LocalParms;
import com.cetc.pacong.domain.News;
import com.cetc.pacong.domain.Product;
import com.cetc.pacong.domain.ProductProcess;
import com.cetc.pacong.utils.Base64Util;
import com.cetc.pacong.utils.KeySetSingleton;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class McloudProductResolver implements PageProcessor {


    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Site site = Site.me().setRetryTimes(3).setSleepTime(4000).setTimeOut(10000).setCharset("UTF-8");

    public List<Product> list=new ArrayList<>();
    public ProductProcessDao productProcessDao;
    public static String lastItem=null;
    private static String currentItem="";
    @Override
    public void process(Page page) {

        Request request = page.getRequest();
        Product product = new Product();

        String text=page.getRawText();
        JSONObject jsonObject = JSON.parseObject(text);
        System.out.println(jsonObject);
        product.docId = Base64Util.encodeURLSafeString(request.getUrl());//url生成编码
        product.key = jsonObject.getString("proId");//网站的唯一id
        product.url = request.getUrl();//网页url
        product.source="joinchain众诚医械";
        product.loadTime=new Date();
        product.attachment=null;
        product.update_time=LocalParms.update_time;
        product.status_cd=1;
        product.batch=LocalParms.productBatch;
        product.registrationNumber = jsonObject.getString("registNum");////注册证编码
        product.entName=jsonObject.getString("enterpriseName");//注册人名称
        product.addressOfRegistrant=jsonObject.getString("registrarAddress");;//
        product.entId=jsonObject.getString("proId");;//企业id
        product.productionAddress=jsonObject.getString("productionAddress");;//生产地址
        product.category=jsonObject.getString("category");;//管理类别
        product.modelAndSpecification=jsonObject.getString("modelSpeci");;//型号规格
        if( product.modelAndSpecification!=null&&product.modelAndSpecification.length()>1900) {
            product.modelAndSpecification = product.modelAndSpecification.substring(0,1900);
        }
        product.sampleName=jsonObject.getString("sampleName");;//品名举例
        product.structureAndComposition=jsonObject.getString("structure");;//结构及组成 / 主要组成部分
        product.scopeOfApplication=jsonObject.getString("applyRange");;//适用范围 / 预期用途
        product.approvalDate=jsonObject.getString("approvalDate");;//批准日期
        product.approvalDepartment=jsonObject.getString("approvalDepart");;//审批部门
        product.remarks=jsonObject.getString("");;//备注
        product.validity=jsonObject.getString("validDate");;//有效期至
        product.status=jsonObject.getString("status");;//状态
        product.name=jsonObject.getString("productName");;//器械名称
        product.productType=jsonObject.getString("productType");;//是否进口
        product.province=jsonObject.getString("province");;//注册地区
        product.year=jsonObject.getString("year");//注册年份
        product.twoProdCategory=jsonObject.getString("twoProdCategory");
        product.zeroProductCategory=jsonObject.getString("name");
        product.oneProdCategory=jsonObject.getString("oneProdCategory");
        product.registType=jsonObject.getString("registType");

        Map<String,String> map = KeySetSingleton.getInstance().getUrlKeyMapNew();
        map.put(request.getUrl(),request.getUrl());
        page.putField("productItems", product);
        if(LocalParms.idSet.contains(product.getEntId()))return;
        list.add(product);
        synchronized (this){
            if(list.size()== LocalParms.LISTSIZE){
                try {
                    this.notifyAll();
                    //logger.info("wait(0);");
                    this.wait();
                }catch (Exception e){
                    logger.info("wait(1);");
                }
            }
        }
        currentItem=(String) request.getExtra("currentItem");
        if(lastItem==null){
            lastItem=currentItem;
        }else if(!lastItem.equals(currentItem)){
            ProductProcess productProcess=new ProductProcess();
            productProcess.type="product";
            productProcess.item =lastItem;
            productProcess.batch="";
            productProcessDao.addItem(productProcess);
            lastItem=(String) request.getExtra("currentItem");
        }

        logger.info("下载数量："+map.keySet().size());

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

