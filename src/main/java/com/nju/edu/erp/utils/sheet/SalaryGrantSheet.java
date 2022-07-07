package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.SalaryGrantSheetDao;
import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.SalaryGrantSheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetContentVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class SalaryGrantSheet implements Sheet{

    private final SalaryGrantSheetDao salaryGrantSheetDao;

    public SalaryGrantSheet(SalaryGrantSheetDao salaryGrantSheetDao) {
        this.salaryGrantSheetDao = salaryGrantSheetDao;
    }

    @Override
    public void makeSheet(UserVO userVO, SheetVO sheetVO) {

    }

    @Override
    public List<SheetVO> findSheetByState(SheetState state) {
        List<SheetVO> res = new ArrayList<>();
        List<SalaryGrantSheetPO> all;
        if (state == null) {
            all = salaryGrantSheetDao.findAll();
        }
        else {
            all = salaryGrantSheetDao.findAllByState((SalaryGrantSheetState) state);
        }
        for (SalaryGrantSheetPO po : all) {
            SalaryGrantSheetVO vo = new SalaryGrantSheetVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }
        return res;
    }

    @Override
    public void approval(String sheetId, SheetState state) {
        SalaryGrantSheetPO salaryGrantSheetPO = salaryGrantSheetDao.getSheetById(sheetId);
        SalaryGrantSheetState salaryGrantSheetState = (SalaryGrantSheetState) state;
        if (salaryGrantSheetState.equals(SalaryGrantSheetState.FAILURE)) {
            if (salaryGrantSheetPO.getState().equals(SalaryGrantSheetState.SUCCESS)) throw new RuntimeException("状态更新失败");
            int effectLines = salaryGrantSheetDao.updateState(sheetId, salaryGrantSheetState);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        }
        else {
            //建立正确的状态迁移
            SalaryGrantSheetState prevState;
            if (salaryGrantSheetState.equals(SalaryGrantSheetState.SUCCESS)) {
                prevState = SalaryGrantSheetState.PENDING;
            }
            else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = salaryGrantSheetDao.updateStateV2(sheetId, salaryGrantSheetState, prevState);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        }
    }
}
