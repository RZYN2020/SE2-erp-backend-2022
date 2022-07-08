package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.service.YearEndAwardsService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/yearEndAwards")
public class YearEndAwardsController {

    private final YearEndAwardsService yearEndAwardsService;

    @Autowired
    public YearEndAwardsController(YearEndAwardsService yearEndAwardsService) {
        this.yearEndAwardsService = yearEndAwardsService;
    }

    @GetMapping("/findAllYearEndSalary")
    public Response findAllYearEndSalary() {
        return Response.buildSuccess(yearEndAwardsService.findAllYearEndSalary());
    }

    @GetMapping("/establishYearEndAwards")
    public Response establishYearEndAwards(@RequestParam Integer employeeId, @RequestParam BigDecimal awards) {
        yearEndAwardsService.establishYearEndAwards(employeeId, awards);
        return Response.buildSuccess();
    }

}
