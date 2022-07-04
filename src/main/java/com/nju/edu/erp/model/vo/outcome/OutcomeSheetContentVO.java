package com.nju.edu.erp.model.vo.outcome;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutcomeSheetContentVO {
  private Integer id;//null
  private String outcome_sheetId;//null
  private String account;
  private BigDecimal amount;
  private String remark;
}
