package com.example.service.impl;

import com.example.dao.DataExchangeDao;
import com.example.mutidatasource.TargetDataSource;
import com.example.service.DataExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class DataExchangeServiceImpl implements DataExchangeService {

    @Resource
    private DataExchangeDao deDao;

    @Override
    public List<Map<String, Object>> getStu() {
        return deDao.getStu();
    }
}
