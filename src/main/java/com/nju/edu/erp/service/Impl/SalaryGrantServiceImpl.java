package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.service.SalaryGrantService;
import com.nju.edu.erp.utils.sheet.SalaryGrantSheet;
import com.nju.edu.erp.utils.sheet.Sheet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryGrantServiceImpl implements SalaryGrantService {

    @Override
    public List<SalaryGrantSheet> getSheetByState(SalaryGrantSheetState salaryGrantSheetState) {
        Sheet sheet = new SalaryGrantSheet();
        List<SheetVO> temp_list = sheet.findSheetByState(salaryGrantSheetState);
        return new ArrayList(temp_list);
    }

    @Override
    public void approval(String id, SalaryGrantSheetState state) {
        Sheet sheet = new SalaryGrantSheet();
        sheet.approval(id, state);
    }
}
