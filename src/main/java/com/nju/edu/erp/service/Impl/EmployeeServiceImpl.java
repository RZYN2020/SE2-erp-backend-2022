package com.nju.edu.erp.service.Impl;


import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.EmployeeService;
import com.nju.edu.erp.utils.salary.SignIn;
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
        SignIn.init(employeeDao);
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
        savePO.setSignTimes(SignIn.getToday());
        savePO.setLastSignTime(new Date());

        employeeDao.createEmployee(savePO);

        EmployeePO responsePO = employeeDao.findOneById(employeeId);
        EmployeeVO ans = new EmployeeVO();
        BeanUtils.copyProperties(responsePO, ans);

        // create User
        User user = new User();
        int userId;
        try {
            userId = employeeDao.findMaxUserId() + 1;
        } catch (Exception ignored) {
            userId = 1;
        }
        user.setId(userId);
        user.setName(responsePO.getName());
        user.setPassword("123456");
        user.setRole(Role.valueOf(responsePO.getJob()));
        employeeDao.createUser(user);

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
        Date lastSignTime = employeeDao.getLastSignTimeByUsername(username);
        if (SignIn.canSignIn(lastSignTime)) {
            employeeDao.signIn(username);
        }
        else throw new MyServiceException("E0000", "今日已打卡，不可重复打卡！");
    }

    @Override
    public EmployeePO findOneById(int id) {
        return employeeDao.findOneById(id);
    }

    @Override
    public int findAbsence(String username) {
        int signInTimes = employeeDao.findSignInTimes(username);
        return SignIn.getToday() - signInTimes;
    }

}
