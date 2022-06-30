package com.nju.edu.erp.model.vo.SaleReturns;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnSheetContentVO {
  /**
   * 商品id
   */
  private String pid;
  /**
   * 数量
   */
  private Integer quantity;
  /**
   * 退还单价，前端传null
   */
  private BigDecimal unitPrice;
  /**
   * 金额
   */
  private BigDecimal totalPrice;
  /**
   * 备注
   */
  private String remark;

}
