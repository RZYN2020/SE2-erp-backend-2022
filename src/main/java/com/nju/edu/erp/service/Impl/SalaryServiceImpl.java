package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.dao.JobDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.SalaryService;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import com.nju.edu.erp.utils.sheet.Sheet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SalaryServiceImpl implements SalaryService {

  private SalarySheetDao salarySheetDao;
  private EmployeeDao employeeDao;
  private JobDao jobDao;

  public SalaryServiceImpl(SalarySheetDao salarySheetDao, EmployeeDao employeeDao, JobDao jobDao) {
    this.salarySheetDao = salarySheetDao;
    this.employeeDao = employeeDao;
    this.jobDao = jobDao;
  }

  @Override
  public void makeSaleReturnSheet(UserVO userVO, SalarySheetVO salarySheetVO) {
    Sheet sheet = new SalarySheet(salarySheetDao, employeeDao, jobDao);
    sheet.makeSheet(userVO, salarySheetVO);
  }

  @Override
  public List<SalarySheet> getSheetByState(SalarySheetState state) {
    Sheet sheet = new SalarySheet(salarySheetDao, employeeDao, jobDao);
    List<SheetVO> temp_list = sheet.findSheetByState(state);
    return new ArrayList(temp_list);
  }

  @Override
  public void approval(String id, SalarySheetState state) {
    Sheet sheet = new SalarySheet(salarySheetDao, employeeDao, jobDao);
    sheet.approval(id, state);
  }
}
