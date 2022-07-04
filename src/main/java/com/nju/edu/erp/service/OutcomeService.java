package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetVO;
import java.util.List;

public interface OutcomeService {
  void makeOutcomeSheet(UserVO userVO, OutcomeSheetVO OutcomeSheetVO);

  List<OutcomeSheetVO> getSheetByState(OutcomeSheetState OutcomeSheetState);

  void approval(String id, OutcomeSheetState state);

}
