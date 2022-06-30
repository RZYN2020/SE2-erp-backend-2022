package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.SaleReturnService;
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
@RequestMapping(path = "/saleReturn")
public class SaleReturnController {

  private final SaleReturnService saleReturnService;

  @Autowired
  public SaleReturnController(SaleReturnService saleReturnService) {
    this.saleReturnService = saleReturnService;
  }

  /** 销售人员制定销售退货单 */
  @Authorized(roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
  @PostMapping(value = "/sheet-make")
  public Response makeSaleReturnOrder(
      UserVO userVO, @RequestBody SaleReturnSheetVO saleReturnSheetVO) {
    saleReturnService.makeSaleReturnSheet(userVO, saleReturnSheetVO);
    return Response.buildSuccess();
  }

  /** 根据状态查看销售退货单 */
  @GetMapping(value = "/sheet-show")
  public Response showSheetByState(
      @RequestParam(value = "state", required = false) SaleReturnSheetState state) {
    return Response.buildSuccess(saleReturnService.getSaleReturnSheetByState(state));
  }

  /**
   * 销售经理审批
   *
   * @param saleSheetId 进货单id
   * @param state 修改后的状态("审批失败"/"待二级审批")
   */
  @GetMapping(value = "/first-approval")
  @Authorized(roles = {Role.SALE_MANAGER, Role.ADMIN})
  public Response firstApproval(
      @RequestParam("id") String id,
      @RequestParam("state") SaleReturnSheetState state) {
    if (state.equals(SaleReturnSheetState.FAILURE)
        || state.equals(SaleReturnSheetState.PENDING_LEVEL_2)) {
      saleReturnService.approval(id, state);
      return Response.buildSuccess();
    } else {
      return Response.buildFailed("000000", "操作失败");
    }
  }

  /**
   * 总经理审批
   * @param saleSheetId 进货单id
   * @param state 修改后的状态("审批失败"/"审批完成")
   */
  @Authorized (roles = {Role.GM, Role.ADMIN})
  @GetMapping(value = "/second-approval")
  public Response secondApproval(@RequestParam("id") String id,
      @RequestParam("state") SaleReturnSheetState state)  {
    if(state.equals(SaleSheetState.FAILURE) || state.equals(SaleSheetState.SUCCESS)) {
      saleReturnService.approval(id, state);
      return Response.buildSuccess();
    } else {
      return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
    }
  }
}