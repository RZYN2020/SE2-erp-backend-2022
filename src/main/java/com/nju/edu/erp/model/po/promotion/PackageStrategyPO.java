package com.nju.edu.erp.model.po.promotion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageStrategyPO {

  private Integer id;

  /**
   * 该特价包折让的价格
   */
  private BigDecimal voucher_amount;

  /**
   * 开始时间
   */
  private Date begin_date;

  /**
   * 结束时间
   */
  private Date end_date;

}
