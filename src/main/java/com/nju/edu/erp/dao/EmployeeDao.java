package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface EmployeeDao {

    int findMaxEmployeeId();

    void createEmployee(EmployeePO employeePO);

    EmployeePO findOneById(int employeeId);

    List<EmployeePO> findAll();

    User findUserByEmployeeId(Integer id);

    void signIn(String username);

    int findSignInTimes(String username);

    void createUser(User user);

    int findMaxUserId();

    Date getLastSignTimeByUsername(String username);
}
