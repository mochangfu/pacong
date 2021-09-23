package com.cetc.pacong.domain;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * created on 20/8/1.
 */
public class News {
    public News() {

    }
    public String docId="";
    private String pageUrl = "";
    public String title = "";
    //来源
    public String from = "";
    public String date = "";
    public String author = "";
    public String editor = "";
    public String url = "";
    public String cat = "";
    public List<String> contentList = Lists.newArrayList();

    public List<String> picsList = Lists.newArrayList();
    public List<String> videoList = Lists.newArrayList();



    public List<String> addUrl(String url) {
        picsList.add(url);
        return picsList;
    }

    public List<String> addDesc(String desc) {
        contentList.add(desc);
        return contentList;
    }

    public List<String> addVideo(String url) {
        videoList.add(url);
        return videoList;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public List<String> getPicsList() {
        return picsList;
    }

    public void setPicsList(List<String> picsList) {
        this.picsList = picsList;
    }


    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
