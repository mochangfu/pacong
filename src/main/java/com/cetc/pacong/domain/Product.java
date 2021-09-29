package com.cetc.pacong.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Product {

    public Product() {

    }
    public String docId="";
    public String title = "";
    public String key="";
    public String url = "";
    public String registrationNumber="";
    public String name;


}
