package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import java.util.List;
import org.springframework.stereotype.Service;

public interface SalaryService {

  void makeSalarySheet(UserVO userVO, SalarySheetVO salarySheetVO);

  List<SalarySheet> getSheetByState(SalarySheetState state);

  void approval(String id, SalarySheetState state);


}
