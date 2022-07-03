package com.nju.edu.erp.model.vo.Salary;

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
  String id;
  int employee_id;
  String employee_name;
  BigDecimal basic_salary;//基本工资
  BigDecimal job_salary;//岗位工资
  BigDecimal commission;//提成
  TaxVO tax;
}
