package com.cetc.pacong.dao;

import com.cetc.pacong.domain.Product;
import com.cetc.pacong.domain.ProductProcess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductProcessDao {
    void updateItem(String type,String currentItem);
    void addItem(ProductProcess item);
    String getItem(String type);
    List<String> getItems(String type,String batch);
}
