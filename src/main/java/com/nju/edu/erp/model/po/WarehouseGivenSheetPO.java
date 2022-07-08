package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseGivenSheetPO {

  /**
   * ZSD + 日期 + index
   */
  private String id;

  /**
   * 操作员
   */
  private String operator;

  /**
   * 操作时间
   */
  private Date create_time;

  /**
   * 关联的销售单ID
   */
  private String saleSheetId;

  /**
   * 单据状态
   */
  private WarehouseGivenSheetState state;

}
