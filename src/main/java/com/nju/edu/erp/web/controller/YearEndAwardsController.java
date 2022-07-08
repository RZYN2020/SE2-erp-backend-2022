package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.service.YearEndAwardsService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/yearEndAwards")
public class YearEndAwardsController {

    private final YearEndAwardsService yearEndAwardsService;

    @Autowired
    public YearEndAwardsController(YearEndAwardsService yearEndAwardsService) {
        this.yearEndAwardsService = yearEndAwardsService;
    }

    @GetMapping("/getTotalSalaryExceptDecember")
    public Response getTotalSalaryExceptDecember(@RequestParam Integer employeeId) {
        return Response.buildSuccess(yearEndAwardsService.getTotalSalaryExceptDecember(employeeId));
    }

    @GetMapping("/establishYearEndAwards")
    public Response establishYearEndAwards(@RequestParam Integer employeeId, @RequestParam BigDecimal awards) {
        yearEndAwardsService.establishYearEndAwards(employeeId, awards);
        return Response.buildSuccess();
    }

}
