package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.model.po.EmployeePO;
import java.math.BigDecimal;

public interface CalculateMethod {

  /**
   * 计算employeePO的实发工资
   * @param employeePO
   * @return
   */
  BigDecimal doCalculate(EmployeePO employeePO);

  /**
   * 返回这种薪资计算方式对应的字符串标识，用于返回给前端. 比如"基本工资 + 岗位工资 - 税款"
   * @return
   */
  String display();

}