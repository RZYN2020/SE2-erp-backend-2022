package com.nju.edu.erp.model.po.promotion;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponPO {

  /**
   * 代金券id
   */
  private Integer id;

  /**
   * 客户id
   */
  private Integer customer_id;

  /**
   * 代金券金额
   */
  private BigDecimal amount;
}
