package com.cetc.pacong.dao;

import com.cetc.pacong.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {
    void addItems(List<Product> items);
    void addItem(Product item);
}
