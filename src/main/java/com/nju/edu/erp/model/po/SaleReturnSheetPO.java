package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
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
public class SaleReturnSheetPO {
  /**
   * 销售单单据编号（格式为：XSTHD-yyyyMMdd-xxxxx
   */
  private String id;

  /**
   * 关联的销售退货单id
   */
  private String saleSheetID;
  /**
   * 操作员
   */
  private String operator;
  /**
   * 备注
   */
  private String remark;
  /**
   * 退货商品总额
   */
  private BigDecimal totalAmount;
  /**
   * 单据状态
   */
  private SaleReturnSheetState state;

}
