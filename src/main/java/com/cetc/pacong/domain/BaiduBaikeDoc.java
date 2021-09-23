package com.cetc.pacong.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class BaiduBaikeDoc{

    public BaiduBaikeDoc() {

    }
    public String docId="";
    public String title = "";
    public String url = "";
    public Map<String, Map<String, List<String>>> paraMaps = Maps.newLinkedHashMap();
    public List<String> referenceList = Lists.newArrayList();
    public List<String> catalogList = Lists.newArrayList();
    public Map<String, String> basicInfoMap = Maps.newLinkedHashMap();
    public List<String> summary = Lists.newArrayList();
    public Map<String, String> picLinks = Maps.newConcurrentMap();
    public Map<String, List<String>> relationShip = Maps.newHashMap();
    public String firstCat = "";
    public String secCat = "";
    public String thirdCat = "";
    public String fourthCat = "";
    public String fileName="";
    public String from="";
//    public String country = "";


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

    public Map<String, Map<String, List<String>>> getParaMaps() {
        return paraMaps;
    }

    public void setParaMaps(Map<String, Map<String, List<String>>> paraMaps) {
        this.paraMaps = paraMaps;
    }

    public List<String> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<String> referenceList) {
        this.referenceList = referenceList;
    }

    public List<String> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<String> catalogList) {
        this.catalogList = catalogList;
    }

    public Map<String, String> getBasicInfoMap() {
        return basicInfoMap;
    }

    public void setBasicInfoMap(Map<String, String> basicInfoMap) {
        this.basicInfoMap = basicInfoMap;
    }

    public List<String> getSummary() {
        return summary;
    }

    public void setSummary(List<String> summary) {
        this.summary = summary;
    }

    public Map<String, String> getPicLinks() {
        return picLinks;
    }

    public void setPicLinks(Map<String, String> picLinks) {
        this.picLinks = picLinks;
    }

    public Map<String, List<String>> getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(Map<String, List<String>> relationShip) {
        this.relationShip = relationShip;
    }

    public String getFirstCat() {
        return firstCat;
    }

    public void setFirstCat(String firstCat) {
        this.firstCat = firstCat;
    }

    public String getSecCat() {
        return secCat;
    }

    public void setSecCat(String secCat) {
        this.secCat = secCat;
    }

    public String getThirdCat() {
        return thirdCat;
    }

    public void setThirdCat(String thirdCat) {
        this.thirdCat = thirdCat;
    }

    public String getFourthCat() {
        return fourthCat;
    }

    public void setFourthCat(String fourthCat) {
        this.fourthCat = fourthCat;
    }

//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
