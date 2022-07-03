package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.JobPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface JobDao {

    List<JobPO> findAll();

    void update(JobPO jobPO);
}
