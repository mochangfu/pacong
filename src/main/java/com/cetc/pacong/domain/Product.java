package com.cetc.pacong.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Product {

    public Product() {

    }

    public String docId = "";//url生成编码
    public String key = "";//网站的唯一id
    public String url = "";//网页url
    public String source="";
    public Date loadTime;
    public String attachment;
    public Date update_time;
    public Integer status_cd;//
    public String batch;

    public String registrationNumber = "";////注册证编码
    public String entName;//注册人名称
    public String addressOfRegistrant;//
    public String entId;//企业id
    public String productionAddress;//生产地址
    public String category;//管理类别
    public String modelAndSpecification;//型号规格
    public String sampleName;//品名举例
    public String structureAndComposition;//结构及组成 / 主要组成部分
    public String scopeOfApplication;//适用范围 / 预期用途
    public String approvalDate;//批准日期
    public String approvalDepartment;//审批部门
    public String remarks;//备注
    public String validity;//有效期至
    public String status;//状态
    public String name;//器械名称
    public String productType;//是否进口
    public String province;//注册地区
    public String year;//注册年份
    public String twoProdCategory;//二级目录
    public String zeroProductCategory;
    public String oneProdCategory;
    public String registType;
}
