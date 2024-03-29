package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.model.vo.TaxVO;
import java.math.BigDecimal;

public class TaxMethod {

  private final static BigDecimal[] payable_step = {new BigDecimal(2000),
      new BigDecimal(5000), new BigDecimal(10000)};

  private final static BigDecimal[] income_tax_step = {new BigDecimal(100), new BigDecimal(200), new BigDecimal(300)};
  private final static BigDecimal[] insurance_step = {new BigDecimal(100), new BigDecimal(200), new BigDecimal(300)};
  private final static BigDecimal[] fund_step = {new BigDecimal(100), new BigDecimal(200), new BigDecimal(300)};

  /**
   * 表驱动方式计算扣税信息，按应发工资梯度进行扣税
   * @param payable
   * @return
   */
  public static TaxVO calculateTax(BigDecimal payable) {
    TaxVO ret = new TaxVO();
    int maxLevel = payable_step.length - 1;
    for (int level = 0; level <= maxLevel; level++) {
      if (payable.compareTo(payable_step[level]) < 0) {
        ret.setIncome_tax(income_tax_step[level]);
        ret.setInsurance(insurance_step[level]);
        ret.setFund(fund_step[level]);
        return ret;
      }
    }
    ret.setIncome_tax(income_tax_step[maxLevel]);
    ret.setInsurance(insurance_step[maxLevel]);
    ret.setFund(fund_step[maxLevel]);
    return ret;
  }

}
