package com.cetc.pacong.domain;

import lombok.Data;

import java.util.Date;

@Data
public class BatchRecord {
    private Integer id;
    private String batch_lot_num;
    private Integer data_source_id;
    private Date data_get_time;
    private Integer amount;
    private String step;
    private String last_batch;
    private String status_cd;

}
