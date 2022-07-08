package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.YearEndAwardsPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface YearEndAwardsDao {

    YearEndAwardsPO findById(Integer employeeId);

    void create(YearEndAwardsPO yearEndAwardsPO);

    List<YearEndAwardsPO> findAll();

    void addAwards(Integer employeeId, BigDecimal awards);
}
