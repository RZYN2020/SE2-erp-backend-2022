package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
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

    /**
     * 总经理审批
     * @param id 工资单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/approval")
    public Response secondApproval(@RequestParam("id") String id,
        @RequestParam("state") SalaryGrantSheetState state)  {
        if(state.equals(SalaryGrantSheetState.FAILURE) || state.equals(SalaryGrantSheetState.SUCCESS)) {
            salaryGrantService.approval(id, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }
}
