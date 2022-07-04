package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import java.util.Date;
import java.util.List;
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

  @Test
  @Transactional
  @Rollback(value = true)
  public void createTest() {
    UserVO userVO = UserVO.builder()
        .name("hale zhao")
        .role(Role.HR)
        .build();

    SalarySheetVO salarySheetVO = SalarySheetVO.builder()
        .employee_name("DTA")
        .employee_id(1)
        .operator("simimasai")
        .build();
    salaryService.makeSalarySheet(userVO, salarySheetVO);
    SalarySheetPO salarySheet = salarySheetDao.getLatest();
  }

}
