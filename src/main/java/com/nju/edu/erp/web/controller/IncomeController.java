package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import com.nju.edu.erp.service.IncomeService;
import com.nju.edu.erp.service.SalaryService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping(path = "/incomeSheet")
public class IncomeController {
  private final IncomeService incomeService;

  @Autowired
  public IncomeController(IncomeService incomeService) {
    this.incomeService = incomeService;
  }

  @Authorized(roles = {Role.HR, Role.GM, Role.ADMIN})
  @PostMapping(value = "/sheet-make")
  public Response makeIncomeSheet(UserVO userVO, @RequestBody IncomeSheetVO incomeSheetVO)  {
    incomeService.makeIncomeSheet(userVO, incomeSheetVO);
    return Response.buildSuccess();
  }

  @GetMapping(value = "/sheet-show")
  public Response showSheetByState(@RequestParam(value = "state", required = false) IncomeSheetState incomeSheetState)  {
    return Response.buildSuccess(incomeService.getSheetByState(incomeSheetState));
  }

  @GetMapping(value = "/approval")
  @Authorized (roles = {Role.SALE_MANAGER, Role.ADMIN})
  public Response approval(@RequestParam("saleSheetId") String id,
      @RequestParam("state") IncomeSheetState state)  {
    if(state.equals(IncomeSheetState.FAILURE) || state.equals(IncomeSheetState.SUCCESS)) {
      incomeService.approval(id, state);
      return Response.buildSuccess();
    } else {
      return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
    }
  }
}
