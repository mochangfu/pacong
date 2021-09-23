package com.cetc.pacong.beans;

import lombok.Data;

/**
 * created on 20/8/6.
 */
@Data
public class CrawlerRequest {
    public String siteUrl;
    public String savePath;
    public String cookie;
    public String param1;

}
