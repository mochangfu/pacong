package com.cetc.pacong.serviceImpl.service;

import java.util.Map;

/**
 * created on 20/8/1.
 */
public interface ICrawService {
    void crawl(String site, String savePath);

    //void crawl1(String site, String savePath, Map<String,String> headers,String method);

    void collect2Resovle2Save();

    Object getContext(String key);
}
