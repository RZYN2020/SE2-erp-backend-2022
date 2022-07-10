package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.dao.WarehouseGivenSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseOutputSheetState;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.po.WarehouseGivenSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseGivenSheetPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.promotion.UserStrategyVO;
import com.nju.edu.erp.service.PromotionService;
import com.nju.edu.erp.utils.promotion.PromotionStrategy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class promotionTest {
  @Autowired
  PromotionService promotionService;

  @Autowired
  SaleService saleService;

  @Autowired
  SaleSheetDao saleSheetDao;

  @Autowired
  WarehouseGivenSheetDao warehouseGivenSheetDao;

  private final String DELL = "0000000000400000";
  private final String Xiaomi = "0000000000400001";
  private final Long MONTH = 30 * 24 * 60 * 60 * 1000L;
  private final long WEEK = 7 * 24 * 60 * 60 * 1000;

  /**
   * 集成测试: 制定促销策略 -> 销售 -> 审批 -> 生成库存赠送单
   */
  @Test
  @Transactional
  @Rollback(value = true)
  public void createUserStrategy() {
    //第一个用户促销策略，已过期
    UserStrategyVO userStrategyVO1 = UserStrategyVO.builder()
        .effect_level(1)
        .discount(BigDecimal.valueOf(0.5))
        .coupon(BigDecimal.valueOf(100))
        .product_id(DELL)
        .product_amount(1)
        .begin_date(new Date(System.currentTimeMillis() - MONTH))
        .end_date(new Date(System.currentTimeMillis() - WEEK)).build();
    //第二个用户策略，未过期
    UserStrategyVO userStrategyVO2 = UserStrategyVO.builder()
        .effect_level(3)
        .discount(BigDecimal.valueOf(0.6))
        .coupon(BigDecimal.valueOf(100))
        .product_id(DELL)
        .product_amount(1)
        .begin_date(new Date(System.currentTimeMillis() - MONTH))
        .end_date(new Date(System.currentTimeMillis() + WEEK)).build();
    promotionService.createUserStrategy(userStrategyVO1);
    promotionService.createUserStrategy(userStrategyVO2);
    List<UserStrategyVO> list = promotionService.getAllUserStrategy();
    Assertions.assertEquals(list.size(), 2);

    CustomerVO customerVO =  CustomerVO.builder()
        .name("lxs")
        .id(1)
        .level(1).build();

    List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
    saleSheetContentVOS.add(SaleSheetContentVO.builder()
        .pid(DELL)
        .quantity(5)
        .remark("Test1-product1")
        .unitPrice(BigDecimal.valueOf(3200))
        .build());
    saleSheetContentVOS.add(SaleSheetContentVO.builder()
        .pid(Xiaomi)
        .quantity(60)
        .remark("Test1-product2")
        .unitPrice(BigDecimal.valueOf(4200))
        .build());
    SaleSheetVO saleSheetVO = SaleSheetVO.builder()
        .saleSheetContent(saleSheetContentVOS)
        .supplier(1)
        .salesman("一级销售经理E")
        .voucherAmount(BigDecimal.valueOf(300))
        .remark("Test1")
        .build();

    UserVO userVO = UserVO.builder()
        .name("xiaoshoujingli")
        .role(Role.SALE_MANAGER)
        .build();

    saleService.makeSaleSheet(userVO, saleSheetVO);
    SaleSheetPO latest = saleSheetDao.getLatestSheet();
    saleService.approval(latest.getId(), SaleSheetState.PENDING_LEVEL_2);
    saleService.approval(latest.getId(), SaleSheetState.SUCCESS);

    latest = saleSheetDao.findSheetById(latest.getId());

    //因为不在时间范围内, 促销策略不生效
    Assertions.assertEquals(latest.getState(), SaleSheetState.SUCCESS);
    Assertions.assertEquals(0, latest.getVoucherAmount().compareTo(BigDecimal.valueOf(300)));

    UserStrategyVO userStrategyVO3 = UserStrategyVO.builder()
        .effect_level(1)
        .discount(BigDecimal.valueOf(0.8))
        .coupon(BigDecimal.valueOf(100))
        .product_id(DELL)
        .product_amount(1)
        .begin_date(new Date(System.currentTimeMillis() - MONTH))
        .end_date(new Date(System.currentTimeMillis() + MONTH)).build();
    promotionService.createUserStrategy(userStrategyVO3);
    saleService.makeSaleSheet(userVO, saleSheetVO);
    latest = saleSheetDao.getLatestSheet();
    saleService.approval(latest.getId(), SaleSheetState.PENDING_LEVEL_2);
    saleService.approval(latest.getId(), SaleSheetState.SUCCESS);

    //促销策略生效
    latest = saleSheetDao.findSheetById(latest.getId());
    Assertions.assertEquals(latest.getState(), SaleSheetState.SUCCESS);
    Assertions.assertEquals(0, latest.getVoucherAmount().compareTo(BigDecimal.valueOf(300)));
    Assertions.assertEquals(0, latest.getDiscount().compareTo(BigDecimal.valueOf(0.8)));

    //生成库存赠送单
    List<WarehouseGivenSheetPO> warehouseGivenSheetPOS = warehouseGivenSheetDao.findAll();
    Assertions.assertEquals(warehouseGivenSheetPOS.size(), 1);
    WarehouseGivenSheetPO po = warehouseGivenSheetPOS.get(0);
    Assertions.assertEquals(po.getState(), WarehouseGivenSheetState.PENDING);
  }
}
