package com.cetc.pacong.dao;

import com.cetc.pacong.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDao {
    void addItems(@Param(value = "items")List<Product> items);
    void addItem(Product item);
    List<String> getIdList();
}
