package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import java.util.List;

public interface IncomeService {
  void makeIncomeSheet(UserVO userVO, IncomeSheetVO incomeSheetVO);

  List<IncomeSheetVO> getSheetByState(IncomeSheetState incomeSheetState);

  void approval(String id, IncomeSheetState state);

}
