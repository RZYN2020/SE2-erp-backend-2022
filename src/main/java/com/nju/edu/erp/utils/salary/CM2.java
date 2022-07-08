package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.PaymentMethod;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.TaxVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CM2 implements CalculateMethod{
  private final String identifier = "基本工资 + 提成 + 岗位工资 - 税款";
  private final BigDecimal ratio = new BigDecimal(0.01);

  private JobDao jobDao;
  private SaleSheetDao saleSheetDao;


  public CM2(JobDao jobDao, SaleSheetDao saleSheetDao) {
    this.jobDao = jobDao;
    this.saleSheetDao = saleSheetDao;
  }

  public BigDecimal doCalculate(EmployeePO employeePO) {
    JobPO jobPO = jobDao.findJobByKey(employeePO.getJob(), employeePO.getJobLevel());
    BigDecimal payable = jobPO.getBasicSalary().add(jobPO.getJobSalary());
    TaxVO taxVO = TaxMethod.calculateTax(payable);
    BigDecimal total_tax = taxVO.getIncome_tax().add(taxVO.getInsurance()).add(taxVO.getFund());
    BigDecimal actually_paid = payable.subtract(total_tax);
    BigDecimal absence = new BigDecimal(SignIn.findAbsence(employeePO.getUsername()));

    //计算提成, 规则:该销售员在过去30天内参与的所有销售单金额 * 0.01
    List<SaleSheetPO> saleSheetPOList = saleSheetDao.findAllByState(SaleSheetState.SUCCESS);
    BigDecimal totalSaleAmount = BigDecimal.ZERO;
    Date monthBefore = new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000);
    for (SaleSheetPO saleSheetPO : saleSheetPOList) {
      assert saleSheetPO.getCreate_time() != null; //已经审批完成，应该具有日期
      if (saleSheetPO.getCreate_time().after(monthBefore)) {
        totalSaleAmount = totalSaleAmount.add(saleSheetPO.getFinalAmount());
      }
    }
    actually_paid = actually_paid.add(totalSaleAmount.multiply(ratio));

    //计算打卡扣除的款项,总经理不参与扣款
    if (jobPO.getName().equals("GM")) {
      return actually_paid;
    }
    BigDecimal radix;
    if (jobPO.getPaymentMethod().equals(PaymentMethod.Yearly)) {
      radix = new BigDecimal(365);
    } else {
      radix = new BigDecimal(30);
    }
    BigDecimal deduction = jobPO.getBasicSalary().multiply(absence).divide(radix);
    return actually_paid.subtract(deduction);
  }

  public TaxVO calculate_tax(EmployeePO employeePO) {
    return TaxMethod.calculateTax(calculate_payable(employeePO));
  }

  public BigDecimal calculate_payable(EmployeePO employeePO) {
    JobPO jobPO = jobDao.findJobByKey(employeePO.getJob(), employeePO.getJobLevel());
    return jobPO.getBasicSalary().add(jobPO.getJobSalary());
  }

  public String display() {
    return identifier;
  }
}
