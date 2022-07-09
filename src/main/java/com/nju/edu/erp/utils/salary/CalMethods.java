package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.TaxVO;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CalMethods {
  private static ArrayList<CalculateMethod> calculateMethods;
  private static JobDao jobDao;
  private static SaleSheetDao saleSheetDao;


  public static void init(JobDao jobDao, SaleSheetDao saleSheetDao) {
    assert calculateMethods == null;
    calculateMethods = new ArrayList<>();
    CalMethods.jobDao = jobDao;
    CalMethods.saleSheetDao = saleSheetDao;


    calculateMethods.add(new CM1(jobDao));
    calculateMethods.add(new CM2(jobDao, saleSheetDao));
  }

  public static void add(CalculateMethod cm) {
    calculateMethods.add(cm);
  }

  public static BigDecimal doCalculate(EmployeePO employeePO, Integer idx) {
    return calculateMethods.get(idx - 1).doCalculate(employeePO);
  }

  public static TaxVO calculate_tax(EmployeePO employeePO, Integer idx) {
    return calculateMethods.get(idx - 1).calculate_tax(employeePO);
  }

  public static BigDecimal calculate_payable(EmployeePO employeePO, Integer idx) {
    return calculateMethods.get(idx - 1).calculate_payable(employeePO);
  }

  public static String display(Integer idx) {
    return calculateMethods.get(idx - 1).display();
  }

  public static int getSize() {
    return calculateMethods.size();
  }
}
