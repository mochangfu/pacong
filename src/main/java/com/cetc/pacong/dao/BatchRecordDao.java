package com.cetc.pacong.dao;

import com.cetc.pacong.domain.BatchRecord;
import com.cetc.pacong.domain.DataSource;
import com.cetc.pacong.domain.ProductProcess;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchRecordDao {
    BatchRecord findByNum(String batch_lot_num);
    void addItem(BatchRecord batchRecord);
    void update(BatchRecord batchRecord);
}
