package com.nju.edu.erp.model.vo.promotion;

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
public class PromotionPackageVO {

  /**
   * 特价包内含的商品
   */
  private List<String> product_id;

  /**
   * 特价包商品数量，下标与上述列表一一对应
   */
  private List<Integer> product_amount;

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
