package com.example.mapper;



import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface DataExchangeMapper {
    /**
     * 获取所有学生信息
     */
    List<Map<String,Object>> getStu();
}
