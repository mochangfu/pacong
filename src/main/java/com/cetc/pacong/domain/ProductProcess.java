package com.cetc.pacong.domain;

import lombok.Data;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.util.Date;

@Data
public class ProductProcess {

    public ProductProcess() {

    }

    public Integer id;//
    public String type;
    public String batch;
    public String item;
    public String  ip;
    public Integer sourceId;
    public String status;
}
