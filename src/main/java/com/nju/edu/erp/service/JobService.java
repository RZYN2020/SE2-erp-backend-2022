package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;

import java.util.List;

public interface JobService {
    /**
     * 查询所有岗位信息
     * @return 岗位信息列表
     */
    List<JobVO> findAll();

    /**
     * 更新岗位信息
     * @param jobPO 岗位信息
     */
    void updateJob(JobPO jobPO);

}
