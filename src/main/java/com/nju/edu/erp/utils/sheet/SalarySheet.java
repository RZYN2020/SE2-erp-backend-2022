package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import java.util.List;

public class SalarySheet implements Sheet {

  @Override
  public void makeSheet(UserVO userVO, SheetVO sheetVO) {

  }

  @Override
  public List<SheetVO> findSheetByState(SheetState state) {
    return null;
  }

  @Override
  public void approval(String sheetId, SheetState state) {

  }
}
