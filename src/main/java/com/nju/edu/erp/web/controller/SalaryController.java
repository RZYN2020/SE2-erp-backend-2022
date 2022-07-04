package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.SalaryService;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {
  private final SalaryService salaryService;

  @Autowired
  public SalaryController(SalaryService salaryService) {
    this.salaryService = salaryService;
  }

  @Authorized(roles = {Role.HR, Role.GM, Role.ADMIN})
  @PostMapping(value = "/sheet-make")
  public Response makeSaleOrder(UserVO userVO, @RequestBody SalarySheetVO salarySheetVO)  {
    System.out.println("\n\n\n\n\n SHEETMAKE \n\n\n");
    salaryService.makeSalarySheet(userVO, salarySheetVO);
    return Response.buildSuccess();
  }

  @GetMapping(value = "/sheet-show")
  public Response showSheetByState(@RequestParam(value = "state", required = false) SalarySheetState salarySheetState)  {
    return Response.buildSuccess(salaryService.getSheetByState(salarySheetState));
  }

}
