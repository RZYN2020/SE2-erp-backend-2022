package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;
import com.nju.edu.erp.service.JobService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/job")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/findAllJob")
    public Response findAll() {
        return Response.buildSuccess(jobService.findAll());
    }

    @PostMapping("/updateJob")
    public Response updateJob(@RequestBody JobVO jobVO) {
        JobPO jobPO = new JobPO();
        BeanUtils.copyProperties(jobVO, jobPO);
        jobService.updateJob(jobPO);
        return Response.buildSuccess();
    }

    @GetMapping("/findAllCalculateMethod")
    public Response findAllCalculateMethod() {
        return null;
    }

    @GetMapping("/findAllPaymentMethod")
    public Response findAllPaymentMethod() {
        return null;
    }
}
