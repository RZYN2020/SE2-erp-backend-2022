package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.service.SalaryGrantService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/grantSheet")
public class SalaryGrantController {
    private final SalaryGrantService salaryGrantService;

    @Autowired
    public SalaryGrantController(SalaryGrantService salaryGrantService) {
        this.salaryGrantService = salaryGrantService;
    }

    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SalaryGrantSheetState salaryGrantSheetState) {
        return Response.buildSuccess(salaryGrantService.getSheetByState(salaryGrantSheetState));
    }
}
