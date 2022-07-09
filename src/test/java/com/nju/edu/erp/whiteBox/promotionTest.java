package com.nju.edu.erp.whiteBox;

import com.nju.edu.erp.model.vo.promotion.UserStrategyVO;
import com.nju.edu.erp.service.PromotionService;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class promotionTest {
  @Autowired
  PromotionService promotionService;

  private final String DELL = "0000000000400000";
  private final Integer MONTH = 30 * 24 * 60 * 60 * 1000;
  private final Integer WEEK = 7 * 24 * 60 * 60 * 1000


  @Test
  @Transactional
  @Rollback(value = true)
  public void createUserStrategy() {
    //第一个用户促销策略，已过期
    UserStrategyVO userStrategyVO1 = UserStrategyVO.builder()
        .effect_level(2)
        .discount(BigDecimal.valueOf(0.5))
        .coupon(BigDecimal.valueOf(100))
        .product_id(DELL)
        .product_amount(1)
        .begin_date(new Date(System.currentTimeMillis() - MONTH))
        .end_date(new Date(System.currentTimeMillis() - WEEK)).build();
    //第二个用户策略，未过期
    UserStrategyVO userStrategyVO2 = UserStrategyVO.builder()
        .effect_level(3)
        .discount(BigDecimal.valueOf(0.5))
        .coupon(BigDecimal.valueOf(100))
        .product_id(DELL)
        .product_amount(1)
        .begin_date(new Date(System.currentTimeMillis() - MONTH))
        .end_date(new Date(System.currentTimeMillis() - WEEK)).build();
    promotionService.createUserStrategy(userStrategyVO1);
    promotionService.createUserStrategy(userStrategyVO2);
    
  }
}
