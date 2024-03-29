package com.nju.edu.erp.model.vo.income;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeSheetContentVO {
  private Integer id;//null
  private String income_sheetId;//null
  private String account;
  private BigDecimal amount;
  private String remark;
}
