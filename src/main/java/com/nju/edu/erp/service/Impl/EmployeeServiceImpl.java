package com.nju.edu.erp.service.Impl;


import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public EmployeeVO createEmployee(EmployeeVO inputVO) {
        // 仿照CustomerServiceImpl.createProduct

        // 生成员工id
        int employeeId;
        try {
            employeeId = employeeDao.findMaxEmployeeId() + 1;
        } catch (Exception ignored) {
            employeeId = 1;
        }

        EmployeePO savePO = new EmployeePO();
        BeanUtils.copyProperties(inputVO, savePO);
        savePO.setId(employeeId);
        savePO.setUsername(inputVO.getName());
        savePO.setSignTimes(getToday());

        employeeDao.createEmployee(savePO);

        EmployeePO responsePO = employeeDao.findOneById(employeeId);
        EmployeeVO ans = new EmployeeVO();
        BeanUtils.copyProperties(responsePO, ans);

        return ans;
    }

    @Override
    public List<EmployeeVO> findAll() {
        List<EmployeePO> employeePOList = employeeDao.findAll();
        List<EmployeeVO> ans = new ArrayList<>();
        for (EmployeePO employeePO : employeePOList) {
            EmployeeVO employeeVO = new EmployeeVO();
            BeanUtils.copyProperties(employeePO, employeeVO);
            ans.add(employeeVO);
        }
        return ans;
    }

    @Override
    public UserVO findUserByEmployeeId(Integer id) {
        User userPO = employeeDao.findUserByEmployeeId(id);
        UserVO ans = new UserVO();
        BeanUtils.copyProperties(userPO, ans);
        return ans;
    }

    @Override
    public void signIn(String username) {
        employeeDao.signIn(username);
    }

    @Override
    public int findAbsence(String username) {
        int signInTimes = employeeDao.findSignInTimes(username);
        return getToday() - signInTimes;
    }

    @Override
    public EmployeePO findOneById(int id) {
        return employeeDao.findOneById(id);
    }

    private int getToday() {
        // 返回今天是一个月第几天
        Date now = new Date();
        return Integer.parseInt(now.toString().split(" ")[2]);
    }
}
