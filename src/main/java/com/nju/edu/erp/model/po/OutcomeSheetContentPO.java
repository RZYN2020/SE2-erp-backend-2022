package com.nju.edu.erp.model.po;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutcomeSheetContentPO {
  private Integer id;
  private String outcome_sheet_id;
  private String account;
  private BigDecimal amount;
  private String remark;
}
