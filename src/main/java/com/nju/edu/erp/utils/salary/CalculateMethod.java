package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.model.po.EmployeePO;
import java.math.BigDecimal;

public interface CalculateMethod {

  BigDecimal doCalculate(EmployeePO employeePO);

  String display();

}