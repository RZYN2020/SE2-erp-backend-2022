package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;
import com.nju.edu.erp.service.JobService;
import com.nju.edu.erp.utils.salary.CM1;
import com.nju.edu.erp.utils.salary.CM2;
import com.nju.edu.erp.utils.salary.CalculateMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobDao jobDao;

    private CalculateMethod[] calMethods;

    @Autowired
    public JobServiceImpl(JobDao jobDao) {
        this.jobDao = jobDao;
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
        calMethods[0] = new CM1(jobDao);
        calMethods[1] = new CM2(jobDao);
    }
}
