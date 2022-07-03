package com.nju.edu.erp.model.vo.Salary;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.TaxVO;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetVO implements SheetVO {
  private String id;
  private String operator;
  private int employee_id;
  private String employee_name;
  private BigDecimal basic_salary;//基本工资 null
  private BigDecimal job_salary;//岗位工资 null
  private BigDecimal commission;//提成 null
  private TaxVO tax; // null
  private String remark;
  private SalarySheetState state;
}
