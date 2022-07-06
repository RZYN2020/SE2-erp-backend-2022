package com.nju.edu.erp.utils.promotion;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionInfo {

  /**
   * 折扣
   */
  private BigDecimal discount;

  /**
   *  折让的价格(特价包才会用到)
   */
  private BigDecimal voucher_amount;

  /**
   * 赠送的代金券
   */
  private BigDecimal coupon;

  /**
   * 赠送的商品对应的id
   */
  private String pid;

  /**
   * 赠品数量
   */
  private Integer amount;
}
