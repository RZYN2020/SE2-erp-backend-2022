package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;
import com.nju.edu.erp.service.JobService;
import com.nju.edu.erp.utils.salary.CM1;
import com.nju.edu.erp.utils.salary.CM2;
import com.nju.edu.erp.utils.salary.CalculateMethod;
import com.nju.edu.erp.utils.salary.SignIn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobDao jobDao;
    private SignIn signIn;

    private CalculateMethod[] calMethods;

    @Autowired
    public JobServiceImpl(JobDao jobDao, EmployeeDao employeeDao) {
        this.jobDao = jobDao;
        signIn = new SignIn(employeeDao);
        initCalMethods();
    }

    @Override
    public List<JobVO> findAll() {
        List<JobPO> jobPOList = jobDao.findAll();
        List<JobVO> ans = new ArrayList<>();
        for (JobPO jobPO : jobPOList) {
            JobVO jobVO = new JobVO();
            BeanUtils.copyProperties(jobPO, jobVO);
            ans.add(jobVO);
        }
        return ans;
    }

    @Override
    public void updateJob(JobPO jobPO) {
        jobDao.update(jobPO);
    }

    private void initCalMethods() {
        calMethods = new CalculateMethod[16];
        calMethods[0] = new CM1(jobDao, signIn);
        calMethods[1] = new CM2(jobDao, signIn);
    }
}
