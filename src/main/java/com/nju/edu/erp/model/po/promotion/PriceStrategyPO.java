package com.nju.edu.erp.model.po.promotion;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceStrategyPO {

  private Integer id;

  /**
   * 生效的总价(如果是用户策略, 则该值为NULL)
   */
  private BigDecimal effect_price;

  /**
   * 赠品的pid
   */
  private String product_id;

  /**
   * 赠品数量
   */
  private Integer product_amount;

  /**
   * 赠送的代金券金额
   */
  private BigDecimal coupon;

  /**
   * 开始时间
   */
  private Date begin_date;

  /**
   * 结束时间
   */
  private Date end_date;

}
