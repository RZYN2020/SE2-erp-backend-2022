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
    salaryService.makeSalarySheet(userVO, salarySheetVO);
    return Response.buildSuccess();
  }

  @GetMapping(value = "/sheet-show")
  public Response showSheetByState(@RequestParam(value = "state", required = false) SalarySheetState salarySheetState)  {
    return Response.buildSuccess(salaryService.getSheetByState(salarySheetState));
  }

  /**
   * 人力资源人员审批
   * @param id 工资单id
   * @param state 修改后的状态("审批失败"/"待二级审批")
   */
  @GetMapping(value = "/first-approval")
  @Authorized (roles = {Role.HR, Role.ADMIN})
  public Response firstApproval(@RequestParam("id") String id,
      @RequestParam("state") SalarySheetState state)  {
    if(state.equals(SalarySheetState.FAILURE) || state.equals(SalarySheetState.PENDING_LEVEL_2)) {
      salaryService.approval(id, state);
      return Response.buildSuccess();
    } else {
      return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
    }
  }

  /**
   * 总经理审批
   * @param id 工资单id
   * @param state 修改后的状态("审批失败"/"审批完成")
   */
  @Authorized (roles = {Role.GM, Role.ADMIN})
  @GetMapping(value = "/second-approval")
  public Response secondApproval(@RequestParam("id") String id,
      @RequestParam("state") SalarySheetState state)  {
    if(state.equals(SalarySheetState.FAILURE) || state.equals(SalarySheetState.SUCCESS)) {
      salaryService.approval(id, state);
      return Response.buildSuccess();
    } else {
      return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
    }
  }





}
