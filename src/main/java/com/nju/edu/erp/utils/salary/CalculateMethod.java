package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.TaxVO;
import java.math.BigDecimal;

public interface CalculateMethod {

  /**
   * 计算employeePO的实发工资, 涉及应发工资、扣税和缺勤扣除等
   * @param employeePO
   * @return
   */
  BigDecimal doCalculate(EmployeePO employeePO); // 税后

  /**
   * 计算税款
   * @param employeePO
   * @return
   */
  TaxVO calculate_tax(EmployeePO employeePO);

  /**
   * 计算应发工资
   * @param employeePO
   * @return
   */
  BigDecimal calculate_payable(EmployeePO employeePO);  // 税前

  /**
   * 返回这种薪资计算方式对应的字符串标识，用于返回给前端. 比如"基本工资 + 岗位工资 - 税款"
   * @return
   */
  String display();

}