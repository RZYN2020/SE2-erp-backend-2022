package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.TaxVO;
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
public class SalarySheetPO {
  private String id;
  private String operator;
  private Integer employee_id;
  private String remark;
  private SalarySheetState state;
  private BigDecimal basic_salary;//基本工资
  private BigDecimal job_salary;//岗位工资
  private BigDecimal commission;//提成
  private BigDecimal income_tax;
  private BigDecimal insurance;
  private Date create_time;
  private BigDecimal fund;
}
