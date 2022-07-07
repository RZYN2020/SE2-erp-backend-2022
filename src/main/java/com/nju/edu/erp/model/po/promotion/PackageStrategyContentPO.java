package com.nju.edu.erp.model.po.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageStrategyContentPO {
  private Integer id;
  private Integer package_strategy_id;
  private String product_id;
  private Integer product_amount;
}
