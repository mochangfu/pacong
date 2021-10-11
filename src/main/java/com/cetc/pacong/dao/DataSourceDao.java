package com.cetc.pacong.dao;

import com.cetc.pacong.domain.DataSource;
import com.cetc.pacong.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataSourceDao {
    DataSource findById(Integer data_source_id);
    DataSource findByName(String data_source_name);
    void update(DataSource dataSource);
}
