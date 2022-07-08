package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.SalaryGrantSheetDao;
import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.service.SalaryGrantService;
import com.nju.edu.erp.utils.sheet.SalaryGrantSheet;
import com.nju.edu.erp.utils.sheet.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryGrantServiceImpl implements SalaryGrantService {

    private final SalaryGrantSheetDao salaryGrantSheetDao;

    @Autowired
    public SalaryGrantServiceImpl(SalaryGrantSheetDao salaryGrantSheetDao) {
        this.salaryGrantSheetDao = salaryGrantSheetDao;
    }

    /**
     * 根据审批状态获取批量工资发放单
     * @param salaryGrantSheetState
     * @return List<SalaryGrantSheet>
     */
    @Override
    public List<SalaryGrantSheet> getSheetByState(SalaryGrantSheetState salaryGrantSheetState) {
        Sheet sheet = new SalaryGrantSheet(salaryGrantSheetDao);
        List<SheetVO> temp_list = sheet.findSheetByState(salaryGrantSheetState);
        return new ArrayList(temp_list);
    }

    /**
     * 审批工资发放单
     * @param id, state
     */
    @Override
    public void approval(String id, SalaryGrantSheetState state) {
        Sheet sheet = new SalaryGrantSheet(salaryGrantSheetDao);
        sheet.approval(id, state);
    }
}
