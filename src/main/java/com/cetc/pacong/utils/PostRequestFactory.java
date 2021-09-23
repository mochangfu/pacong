package com.cetc.pacong.utils;

import com.google.common.collect.Maps;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.Map;

public class PostRequestFactory {
    public static Request getTargetImgRequest(String cookie, String par, int pageSize, int pageNo) {
        Request request = new Request("http://21.43.16.92:8008/dm/cn.ac.nci.htzc.targetimg/queryTargetImage.action");
        request.setMethod(HttpConstant.Method.POST);
        request.addHeader("Accept", "*/*");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.addHeader("Connection", "keep-alive");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.addHeader("Cookie", cookie);
        request.addHeader("Host", "21.43.16.92:8008");
        request.addHeader("Origin", "http://21.43.16.92:8008");
        request.addHeader("Referer", "http://21.43.16.92:8008/dm/cn.ac.nci.htzc.targetimg/queryTargetImage.action?pageNum=0");
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        Map<String, Object> formMap = Maps.newHashMap();
        formMap.put("par", par);
        formMap.put("pageSize", pageSize);
        formMap.put("pageNum", pageNo);
        formMap.put("direction", "desc");
        formMap.put("sort", "receivetime");
        request.setRequestBody(HttpRequestBody.form(formMap, "utf-8"));
        request.putExtra("pageNo", pageNo);
        return request;
    }
//
//    public static Request getTargetImgNextPageRequest(Request request) {
//        Integer pageNo = (Integer) request.getExtra("pageNo");
//        if (pageNo == null) {
//            return null;
//        }
//    }


    public static Request getOceanpostureRequest(String cookie, String par, int pageSize, int pageNo) {
        Request request = new Request("http://21.43.16.92:8008/dm/cn.ac.nci.htzc.oceanposture/queryOceanposture.action");
        request.setMethod(HttpConstant.Method.POST);
        request.addHeader("Accept", "*/*");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.addHeader("Connection", "keep-alive");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.addHeader("Cookie", cookie);
        request.addHeader("Host", "21.43.16.92:8008");
        request.addHeader("Origin", "http://21.43.16.92:8008");
        request.addHeader("Referer", "http://21.43.16.92:8008/dm/cn.ac.nci.htzc.targetimg/queryTargetImage.action?pageNum=0");
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        Map<String, Object> formMap = Maps.newHashMap();
        formMap.put("par", par);
        formMap.put("pageSize", pageSize);
        formMap.put("pageNum", pageNo);
        formMap.put("direction", "desc");
        formMap.put("sort", "receivetime");
        request.setRequestBody(HttpRequestBody.form(formMap, "utf-8"));
        request.putExtra("pageNo", pageNo);
        return request;
    }


    public static Request getInformationGetRequest(String cookie, String type, int pageNum) {
        Request request = new Request("http://21.43.16.92:8008/dm/cn.ac.nci.htzc.oceanposture/queryOceanposture.action");
        request.setMethod(HttpConstant.Method.GET);
        request.addHeader("Accept", "*/*");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.addHeader("Connection", "keep-alive");
//        request.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        request.addHeader("Cookie", cookie);
        request.addHeader("Host", "21.43.16.92:8008");
//        request.addHeader("Origin","http://21.43.16.92:8008");
        request.addHeader("Referer", "http://21.43.16.92:8008/dm/layout/left.jsp");
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
//        request.addHeader("X-Requested-With","XMLHttpRequest");
        Map<String, Object> formMap = Maps.newHashMap();
        formMap.put("type", type);

        formMap.put("pageNum", pageNum);

        request.setRequestBody(HttpRequestBody.form(formMap, "utf-8"));
        request.putExtra("pageNo", pageNum);

        return request;
    }


    /**
     * 目标图像： referer :http://21.43.16.92:8008/dm/cn.ac.nci.htzc.targetimg/queryTargetImage.action?pageNum=0
     *
     * @param cookie
     * @param referer
     * @param IMAGEPRODUCTID
     * @param PDTTYPE
     * @return
     */
    public static Request getDownLoadRequest(String cookie, String referer, String IMAGEPRODUCTID, String PDTTYPE) {
        Request downloadReq = new Request("http://21.43.16.92:8008/dm/cn.ac.nci.htzc.download/download.action");
        downloadReq.setMethod(HttpConstant.Method.POST);
        downloadReq.addHeader("Accept", "*/*");
        downloadReq.addHeader("Accept-Encoding", "gzip, deflate");
        downloadReq.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        downloadReq.addHeader("Connection", "keep-alive");
        downloadReq.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        downloadReq.addHeader("Cookie", cookie);
        downloadReq.addHeader("Host", "21.43.16.92:8008");
        downloadReq.addHeader("Origin", "http://21.43.16.92:8008");
        downloadReq.addHeader("Referer", referer);
        downloadReq.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
        downloadReq.addHeader("X-Requested-With", "XMLHttpRequest");

        Map<String, Object> formMap = Maps.newHashMap();
        formMap.put("downloadEntity.IMAGEPRODUCTID", IMAGEPRODUCTID);
        formMap.put("downloadEntity.PDTTYPE", PDTTYPE);
        downloadReq.setRequestBody(HttpRequestBody.form(formMap, "utf-8"));
        downloadReq.putExtra("downloadReq", "true");
        return downloadReq;
    }
}
