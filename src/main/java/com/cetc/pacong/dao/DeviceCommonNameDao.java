package com.cetc.pacong.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceCommonNameDao {
    List<String> findNames();
    void update1();
    void getOne();
}
