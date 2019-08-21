package com.example.mapper;

import com.example.vo.Emp;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectKeyMapper {
     int insert(Emp emp);
}
