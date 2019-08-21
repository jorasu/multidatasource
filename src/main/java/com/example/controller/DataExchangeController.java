package com.example.controller;

import com.example.service.DataExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RestController
public class DataExchangeController {
    @Autowired
    private DataExchangeService deService;
    @RequestMapping(value="/getStu" ,method = RequestMethod.GET)
    public List<Map<String,Object>> getStu(){
       return deService.getStu();
    };
}
