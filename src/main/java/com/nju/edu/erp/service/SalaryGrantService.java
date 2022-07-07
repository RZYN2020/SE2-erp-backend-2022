package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.utils.sheet.SalaryGrantSheet;

import java.util.List;

public interface SalaryGrantService {

    List<SalaryGrantSheet> getSheetByState(SalaryGrantSheetState salaryGrantSheetState);

    void approval(String id, SalaryGrantSheetState state);
}
