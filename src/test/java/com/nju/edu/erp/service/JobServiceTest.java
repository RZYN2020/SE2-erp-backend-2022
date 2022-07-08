package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.PaymentMethod;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        JobVO random = list.get(4);
        Assertions.assertEquals(Role.HR.name(), random.getName());
        Assertions.assertEquals(0, new BigDecimal(2000).compareTo(random.getBasicSalary()));
        Assertions.assertEquals(0, new BigDecimal(3000).compareTo(random.getJobSalary()));
        Assertions.assertEquals(2, random.getJobLevel());
        Assertions.assertEquals(1, random.getCalculateMethod());
        Assertions.assertEquals(PaymentMethod.Monthly, random.getPaymentMethod());
    }

    @Test
    @Transactional
    @Rollback
    public void updateJobTest() {
        JobVO jobVO = jobService.findAll().get(0);
        JobPO jobPO = new JobPO();
        BeanUtils.copyProperties(jobVO, jobPO);
        jobPO.setJobSalary(new BigDecimal(20000));
        jobPO.setPaymentMethod(PaymentMethod.Yearly);
        jobService.updateJob(jobPO);
        jobVO = jobService.findAll().get(0);
        Assertions.assertEquals(0, jobVO.getJobSalary().compareTo(new BigDecimal(20000)));
        Assertions.assertEquals(PaymentMethod.Yearly, jobVO.getPaymentMethod());
    }

    @Test
    @Transactional
    @Rollback
    public void findAllCalculateMethodTest() {
        List<String> list = jobService.findAllCalculateMethod();
        Assertions.assertEquals("基本工资 + 岗位工资 - 税款", list.get(0));
        Assertions.assertEquals("基本工资 + 提成 + 岗位工资 - 税款", list.get(1));
    }

    @Test
    @Transactional
    @Rollback
    public void findAllPaymentMethodTest() {
        List<PaymentMethod> list = jobService.findAllPaymentMethod();
        Assertions.assertEquals(PaymentMethod.Monthly, list.get(0));
        Assertions.assertEquals(PaymentMethod.Yearly, list.get(1));
    }
}
