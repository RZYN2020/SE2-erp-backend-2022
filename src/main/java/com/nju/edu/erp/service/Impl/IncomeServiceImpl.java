package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.IncomeSheetDao;
import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.IncomeService;
import com.nju.edu.erp.utils.sheet.IncomeSheet;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import com.nju.edu.erp.utils.sheet.Sheet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IncomeServiceImpl implements IncomeService {

  private IncomeSheetDao incomeSheetDao;

  private CustomerService customerService;

  public IncomeServiceImpl(IncomeSheetDao incomeSheetDao, CustomerService customerService) {
    this.incomeSheetDao = incomeSheetDao;
    this.customerService = customerService;
  }

  @Override
  public void makeIncomeSheet(UserVO userVO, IncomeSheetVO incomeSheetVO) {
    Sheet sheet = new IncomeSheet(incomeSheetDao, customerService);
    sheet.makeSheet(userVO, incomeSheetVO);
  }

  @Override
  public List<IncomeSheetVO> getSheetByState(IncomeSheetState incomeSheetState) {
    Sheet sheet = new IncomeSheet(incomeSheetDao, customerService);
    List<SheetVO> temp_list = sheet.findSheetByState(incomeSheetState);
    return new ArrayList(temp_list);
  }

  @Override
  public void approval(String id, IncomeSheetState state) {
    Sheet sheet = new IncomeSheet(incomeSheetDao, customerService);
    sheet.approval(id, state);
  }

}
