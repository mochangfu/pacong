package com.cetc.pacong.controller;

import com.cetc.pacong.beans.Body;
import com.cetc.pacong.beans.CrawlerRequest;
import com.cetc.pacong.beans.Response;
import com.cetc.pacong.serviceImpl.BaikeItemCrawServiceImpl;
import com.cetc.pacong.serviceImpl.BaikeItemYiliaoCrawServiceImpl;
import com.cetc.pacong.serviceImpl.McloudProductCrawServiceImpl;
import com.cetc.pacong.serviceImpl.QianlunTianXiaServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spider")
public class SpiderController {
    @Autowired
    private QianlunTianXiaServiceImpl qianlunTianXiaServiceImpl;
    @Autowired
    private BaikeItemCrawServiceImpl baikeItemCrawService;
    @Autowired
    private BaikeItemYiliaoCrawServiceImpl baikeItemYiliaoCrawService;
    @Autowired
    private McloudProductCrawServiceImpl mcloudProductCrawService;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @PostMapping("/qianlun/down")
    public String qianlun(@RequestBody CrawlerRequest crawlerRequest) throws Exception {
        logger.info("qianlun controller");

        qianlunTianXiaServiceImpl.crawl1(crawlerRequest.getSavePath(), crawlerRequest.getCookie(), crawlerRequest.getParam1());

        Response response = new Response();
        response.success();
        return "请等待";
    }

    @RequestMapping(value = "/baikeItem/collect")
    public String getItem1(@RequestBody(required = false) Body crawlerRequest) throws Exception {
        logger.info("baikeItem controller");
        return baikeItemCrawService.crawl1(crawlerRequest.getItem(), crawlerRequest.getSavePath(), crawlerRequest.getWithChild());
    }


    @RequestMapping(value = "/baikeItem")
    public String baikeItem() throws Exception {
        logger.info("baikeItem controller");
        return baikeItemYiliaoCrawService.crawl1();

    }

    @RequestMapping(value = "/start")
    public String product(@RequestParam("code") String code) throws Exception {
        switch (code){
            case "medical_product":
                logger.info("product controller");
                mcloudProductCrawService.crawl1();
                break;
            case "":
                break;
            default:
                break;
        }
        return "请等待";

    }

}
