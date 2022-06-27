package com.nju.edu.erp.model.vo.SaleReturns;

import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnSheetVO {
  /**
   * 进货单单据编号（格式为：XSTHD-yyyyMMdd-xxxxx), 新建单据时前端传null
   */
  private String id;

  /**
   * 关联的销售单ID
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
   * 折让前总额
   */
  private BigDecimal totalAmount;
  /**
   * 单据状态, 新建单据时前端传null
   */
  private SaleSheetState state;
  /**
   * 退货单具体内容
   */
  List<SaleReturnSheetContentVO> saleReturnSheetContent;

}
