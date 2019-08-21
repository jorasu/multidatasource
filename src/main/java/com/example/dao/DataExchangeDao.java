package com.example.dao;

import com.example.mapper.DataExchangeMapper;

import com.example.mutidatasource.TargetDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public class DataExchangeDao {

    @Autowired
   private DataExchangeMapper deMapper;
    @TargetDataSource("EXAM")
   public List<Map<String,Object>> getStu(){
        return deMapper.getStu();
    };

}
