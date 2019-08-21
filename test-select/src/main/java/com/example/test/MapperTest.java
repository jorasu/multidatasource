package com.example.test;

import com.example.mapper.SelectKeyMapper;
import com.example.vo.Emp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Resource
    private SelectKeyMapper selectKeyMapper;
    @Test
    public void  mapperTest(){
        Emp emp=new Emp();
        emp.setDeptno(1);
        emp.setEname("aa");
        emp.setComm(1);
        emp.setJob("bb");
        emp.setMgr(1);
        emp.setSal(100);
        int result=selectKeyMapper.insert(emp);
        System.out.println(result);
        System.out.println(emp.getEmpno());
    }
}
