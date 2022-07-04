package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
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
public class OutcomeSheetPO {
  private String id;
  private Integer customer_id;
  private String operator;
  private BigDecimal total_amount;
  private OutcomeSheetState state;
  private Date create_time;
}
