package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetVO;
import com.nju.edu.erp.service.OutcomeService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping(path = "/outcomeSheet")
public class OutcomeController {
  private final OutcomeService outcomeService;

  @Autowired
  public OutcomeController(OutcomeService outcomeService) {
    this.outcomeService = outcomeService;
  }

  @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
  @PostMapping(value = "/sheet-make")
  public Response makeOutcomeSheet(UserVO userVO, @RequestBody OutcomeSheetVO outcomeSheetVO)  {
    outcomeService.makeOutcomeSheet(userVO, outcomeSheetVO);
    return Response.buildSuccess();
  }

  @GetMapping(value = "/sheet-show")
  public Response showSheetByState(@RequestParam(value = "state", required = false) OutcomeSheetState outcomeSheetState)  {
    return Response.buildSuccess(outcomeService.getSheetByState(outcomeSheetState));
  }

  @GetMapping(value = "/approval")
  @Authorized (roles = {Role.ADMIN, Role.GM})
  public Response approval(@RequestParam("saleSheetId") String id,
      @RequestParam("state") OutcomeSheetState state)  {
    if(state.equals(OutcomeSheetState.FAILURE) || state.equals(OutcomeSheetState.SUCCESS)) {
      outcomeService.approval(id, state);
      return Response.buildSuccess();
    } else {
      return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
    }
  }
}
