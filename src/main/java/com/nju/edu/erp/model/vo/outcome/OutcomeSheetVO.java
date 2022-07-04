package com.nju.edu.erp.model.vo.outcome;

import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
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
public class OutcomeSheetVO implements SheetVO {
  private String id;//null
  private Integer customer_id;
  private String operator;//null
  private List<OutcomeSheetContentVO> outcome_sheet_content;
  private BigDecimal total_amount;//null
  private OutcomeSheetState state;//null
  private Date createTime;//null
}
