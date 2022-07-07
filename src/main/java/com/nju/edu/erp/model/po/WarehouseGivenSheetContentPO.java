package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseGivenSheetContentPO {

  /**
   * 自增Id
   */
  private Integer id;

  /**
   * 关联的库存赠送单ID
   */
  private String warehouse_given_sheet_id;

  /**
   * 赠送的商品id
   */
  private String pid;

  /**
   * 赠送数量
   */
  private Integer amount;
}
