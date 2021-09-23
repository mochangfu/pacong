package com.cetc.pacong.beans;

import lombok.Data;

/**
 * created on 20/8/6.
 */
@Data
public class HangtianZhenchaRequest extends CrawlerRequest {
    public int fromPageNum;
    public int toPageNum;
    public int docSize;
    public int crawlInterval; //爬取时间间隔
    public String cookie;
    public int sleepTime;
    public String infoType;

    public int getFromPageNum() {
        return fromPageNum;
    }

    public void setFromPageNum(int fromPageNum) {
        this.fromPageNum = fromPageNum;
    }

    public int getToPageNum() {
        return toPageNum;
    }

    public void setToPageNum(int toPageNum) {
        this.toPageNum = toPageNum;
    }

    public int getDocSize() {
        return docSize;
    }

    public void setDocSize(int docSize) {
        this.docSize = docSize;
    }

    public int getCrawlInterval() {
        return crawlInterval;
    }

    public void setCrawlInterval(int crawlInterval) {
        this.crawlInterval = crawlInterval;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }
}
