package com.nju.edu.erp.service;


import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    @Test
    @Transactional
    @Rollback
    public void createTest() {
        EmployeeVO employeeVO = EmployeeVO.builder()
                .name("哈队")
                .gender("男")
                .birthDate(new Date(1647258440))
                .phoneNumber("123456")
                .job("SALE_MANAGER")
                .jobLevel(2)
                .account("1248616394")
                .build();
        employeeService.createEmployee(employeeVO);
        List<EmployeeVO> list = employeeService.findAll();
        EmployeeVO target = list.get(list.size() - 1);
        Assertions.assertEquals(employeeVO.getName(), target.getName());
        Assertions.assertEquals(employeeVO.getGender(), target.getGender());
        Assertions.assertEquals(employeeVO.getBirthDate().toString().compareTo(target.getBirthDate().toString()), 0);
        Assertions.assertEquals(employeeVO.getPhoneNumber(), target.getPhoneNumber());
        Assertions.assertEquals(employeeVO.getJob(), target.getJob());
        Assertions.assertEquals(employeeVO.getJobLevel(), target.getJobLevel());
        Assertions.assertEquals(employeeVO.getAccount(), target.getAccount());

        UserVO userVO = employeeService.findUserByEmployeeId(2);
        System.out.println("赵咣");
    }

    @Test
    @Transactional
    @Rollback
    public void signInTest() {
        employeeService.signIn("lock");
        EmployeePO lockson = employeeService.findOneById(1);
        Assertions.assertEquals(2, lockson.getSignTimes());
    }

    @Test
    @Transactional
    @Rollback
    public void findAbsenceTest() {
        Date now = new Date();
        Assertions.assertEquals(Integer.parseInt(now.toString().split(" ")[2]) - 1, employeeService.findAbsence("lock"));
    }

}
