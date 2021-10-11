package com.cetc.pacong.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DataSource {
    public Integer data_source_id;
    public String data_source_name;
    public String data_source_table;
    public String data_source_url;
    public Integer data_source_step;
    public Date data_source_time;
    public Date data_get_time;
    public Integer data_amount;
    public Integer lot_amount;
    public String lot_num;
    public Integer last_lot;
    public String operate_type;
    public String status_cd;

}
