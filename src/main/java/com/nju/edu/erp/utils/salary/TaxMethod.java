package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.TaxVO;
import java.math.BigDecimal;

public interface TaxMethod {

  TaxVO doCalculate(BigDecimal payable);

}
