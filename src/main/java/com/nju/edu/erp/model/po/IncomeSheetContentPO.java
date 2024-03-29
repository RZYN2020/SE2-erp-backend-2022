package com.nju.edu.erp.model.po;

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
public class IncomeSheetContentPO {
  private Integer id;
  private String income_sheet_id;
  private String account;
  private BigDecimal amount;
  private String remark;
}

