package com.cetc.pacong.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface DeviceCommonNameDao {
    List<String> findNames();

}
