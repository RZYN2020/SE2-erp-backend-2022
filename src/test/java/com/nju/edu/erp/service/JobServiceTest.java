package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.PaymentMethod;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class JobServiceTest {

    @Autowired
    JobService jobService;

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        List<JobVO> list = jobService.findAll();
        System.out.println("赵锁子");
    }

    @Test
    @Transactional
    @Rollback
    public void updateJobTest() {
        JobVO jobVO = jobService.findAll().get(0);
        JobPO jobPO = new JobPO();
        BeanUtils.copyProperties(jobVO, jobPO);
        jobPO.setJobLevel(20);
        jobPO.setPaymentMethod(PaymentMethod.Monthly);
        jobService.updateJob(jobPO);
        jobVO = jobService.findAll().get(0);

        System.out.println("赵锁子");
    }

    @Test
    @Transactional
    @Rollback
    public void findAllCalculateMethodTest() {
        List<String> list = jobService.findAllCalculateMethod();
        System.out.println("赵锁子");
    }

    @Test
    @Transactional
    @Rollback
    public void findAllPaymentMethodTest() {
        List<PaymentMethod> list = jobService.findAllPaymentMethod();
        System.out.println("赵锁子");
    }
}
