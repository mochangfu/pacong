package com.cetc.pacong.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class BaiduBaikeDoc{

    public BaiduBaikeDoc() {

    }
    public String docId="";
    public String title = "";
    public String key="";
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

}
