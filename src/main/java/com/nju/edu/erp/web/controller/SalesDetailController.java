package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.SaleDetailService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/salesDetail")
public class SalesDetailController {

  private final SaleDetailService saleDetailService;

  @Autowired
  public SalesDetailController(SaleDetailService saleDetailService) {
    this.saleDetailService = saleDetailService;
  }

  @Authorized(roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
  @GetMapping(value = "/show-all")
  public Response makeSaleReturnOrder() {
    return Response.buildSuccess(saleDetailService.findAllRecords());
  }
}
