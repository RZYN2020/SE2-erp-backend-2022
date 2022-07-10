package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.SalaryGrantSheetDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.SalaryGrantSheetPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SalaryTests {
  @Autowired
  SalaryService salaryService;

  @Autowired
  EmployeeService employeeService;

  @Autowired
  SalarySheetDao salarySheetDao;

  @Autowired
  SaleService saleService;

  @Autowired
  SaleSheetDao saleSheetDao;

  @Autowired
  SalaryGrantSheetDao salaryGrantSheetDao;

  private final String DELL = "0000000000400000";
  private final String Xiaomi = "0000000000400001";


  /**
   * 集成测试: 制定工资单(综合各模块) ->  审批 -> 生成工资发放单
   */
  @Test
  @Transactional
  @Rollback(value = true)
  public void createTest() {
    UserVO userVO = UserVO.builder()
        .name("hale zhao")
        .role(Role.HR)
        .build();


    List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
    saleSheetContentVOS.add(SaleSheetContentVO.builder()
        .pid(DELL)
        .quantity(5)
        .remark("Test1-product1")
        .unitPrice(BigDecimal.valueOf(3200))
        .build());
    saleSheetContentVOS.add(SaleSheetContentVO.builder()
        .pid(Xiaomi)
        .quantity(60)
        .remark("Test1-product2")
        .unitPrice(BigDecimal.valueOf(4200))
        .build());
    SaleSheetVO saleSheetVO = SaleSheetVO.builder()
        .saleSheetContent(saleSheetContentVOS)
        .supplier(1)
        .salesman("一级销售经理E")
        .voucherAmount(BigDecimal.valueOf(300))
        .remark("Test1")
        .build();

    saleService.makeSaleSheet(userVO, saleSheetVO);

    SaleSheetPO latest_saleSheet = saleSheetDao.getLatestSheet();
    Assertions.assertEquals(latest_saleSheet.getState(), SaleSheetState.PENDING_LEVEL_1);
    Assertions.assertEquals(latest_saleSheet.getSalesman(), "一级销售经理E");
    saleService.approval(latest_saleSheet.getId(), SaleSheetState.PENDING_LEVEL_2);
    saleService.approval(latest_saleSheet.getId(), SaleSheetState.SUCCESS);

    SalarySheetVO salarySheetVO = SalarySheetVO.builder()
        .employee_name("一级销售经理E")
        .operator("HR")
        .employee_id(6)
        .build();
    salaryService.makeSalarySheet(userVO, salarySheetVO);
    SalarySheetPO salarySheet = salarySheetDao.getLatest();
    Assertions.assertEquals(salarySheet.getState(), SalarySheetState.PENDING_LEVEL_1);

    salaryService.approval(salarySheet.getId(), SalarySheetState.PENDING_LEVEL_2);
    salaryService.approval(salarySheet.getId(), SalarySheetState.SUCCESS);

    //检查工资单 有提成
    salarySheet = salarySheetDao.getSheetById(salarySheet.getId());
    Assertions.assertEquals(0, salarySheet.getBasic_salary().compareTo(BigDecimal.valueOf(6000)));
    Assertions.assertEquals(0, salarySheet.getJob_salary().compareTo(BigDecimal.valueOf(6000)));
    assert salarySheet.getCommission() != null;
    Assertions.assertEquals(0, salarySheet.getIncome_tax().compareTo(BigDecimal.valueOf(300)));

    //检查生成工资发放单，金额对得上
    SalaryGrantSheetPO salaryGrantSheetPO = salaryGrantSheetDao.findAll().get(0);
    assert salaryGrantSheetPO.getRealSalary() != null;
  }


}
