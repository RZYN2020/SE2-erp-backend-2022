package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.utils.sheet.SalaryGrantSheet;

import java.util.List;

public interface SalaryGrantService {

    /**
     * 根据审批状态获取批量工资发放单
     * @param salaryGrantSheetState
     * @return List<SalaryGrantSheet>
     */
    List<SalaryGrantSheet> getSheetByState(SalaryGrantSheetState salaryGrantSheetState);

    /**
     * 审批工资发放单
     * @param id, state
     */
    void approval(String id, SalaryGrantSheetState state);
}
