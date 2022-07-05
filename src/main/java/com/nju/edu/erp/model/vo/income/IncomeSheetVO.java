package com.nju.edu.erp.model.vo.income;

import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
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
public class IncomeSheetVO implements SheetVO {
  private String id;//null
  private Integer customer_id;
  private String operator;//null
  private List<IncomeSheetContentVO> income_sheet_content;
  private BigDecimal total_amount;//null
  private IncomeSheetState state;//null
  private Date create_time;//null
}
