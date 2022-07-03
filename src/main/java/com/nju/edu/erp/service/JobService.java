package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.PaymentMethod;
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

    /**
     * 获取所有薪资计算方式
     * @return 薪资计算方式列表(如"基本工资 + 岗位工资 - 税款")
     */
    List<String> findAllCalculateMethod();

    /**
     * 获取所有薪资发放方式
     * @return 薪资发放方式列表(如"月薪制")
     */
    List<PaymentMethod> findAllPaymentMethod();
}
