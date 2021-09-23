package com.cetc.pacong.domain;

import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.Map;

/**
 * created on 20/8/2.
 */
public class BaseVideoDoc {

    private String docId;
    private String pageUrl;
    private String title;
    private String date;
    private String from;
    private String author;
    private String editor;

    private List<String> descriptions = Lists.newArrayList();
    private Map<String, String> videoUrls = Maps.newHashMap();

    public Map<String, String> addVideoUrl(String url, String type) {
        videoUrls.put(url, type);
        return videoUrls;
    }

    public List<String> addDesc(String desc) {
        descriptions.add(desc);
        return descriptions;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public Map<String, String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(Map<String, String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
