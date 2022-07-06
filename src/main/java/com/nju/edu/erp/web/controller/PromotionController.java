package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.promotion.PackageStrategyVO;
import com.nju.edu.erp.model.vo.promotion.PriceStrategyVO;
import com.nju.edu.erp.model.vo.promotion.UserStrategyVO;
import com.nju.edu.erp.service.PromotionService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/promotion")
public class PromotionController {

  private final PromotionService promotionService;

  @Autowired
  public PromotionController(PromotionService promotionService) {
    this.promotionService = promotionService;
  }

  @Authorized(roles = {Role.GM, Role.ADMIN})
  @PostMapping(value = "/user/create")
  public Response makeUserStrategy(@RequestBody UserStrategyVO userStrategyVO) {
    promotionService.createUserStrategy(userStrategyVO);
    return Response.buildSuccess();
  }

  @Authorized(roles = {Role.GM, Role.ADMIN})
  @PostMapping(value = "/package/create")
  public Response makePackageStrategy(@RequestBody PackageStrategyVO packageStrategyVO) {
    promotionService.createPackageStrategy(packageStrategyVO);
    return Response.buildSuccess();
  }

  @Authorized(roles = {Role.GM, Role.ADMIN})
  @PostMapping(value = "/price/create")
  public Response makePriceStrategy(@RequestBody PriceStrategyVO priceStrategyVO) {
    promotionService.createPriceStrategy(priceStrategyVO);
    return Response.buildSuccess();
  }

  @Authorized(roles = {Role.GM, Role.ADMIN})
  @GetMapping(value = "/user/show-all")
  public Response showUserStrategy() {
    return Response.buildSuccess(promotionService.getAllUserStrategy());
  }

  @Authorized(roles = {Role.GM, Role.ADMIN})
  @GetMapping(value = "/package/show-all")
  public Response showPackageStrategy() {
    return Response.buildSuccess(promotionService.getAllPackageStrategy());
  }

  @Authorized(roles = {Role.GM, Role.ADMIN})
  @GetMapping(value = "/price/show-all")
  public Response showPriceStrategy() {
    return Response.buildSuccess(promotionService.getAllPriceStrategy());
  }
}
