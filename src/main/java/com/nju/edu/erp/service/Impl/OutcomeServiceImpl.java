package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.OutcomeSheetDao;
import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.OutcomeService;
import com.nju.edu.erp.utils.sheet.OutcomeSheet;
import com.nju.edu.erp.utils.sheet.Sheet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  private OutcomeSheetDao OutcomeSheetDao;

  private CustomerService customerService;

  public OutcomeServiceImpl(OutcomeSheetDao OutcomeSheetDao, CustomerService customerService) {
    this.OutcomeSheetDao = OutcomeSheetDao;
    this.customerService = customerService;
  }

  @Override
  public void makeOutcomeSheet(UserVO userVO, OutcomeSheetVO OutcomeSheetVO) {
    Sheet sheet = new OutcomeSheet(OutcomeSheetDao, customerService);
    sheet.makeSheet(userVO, OutcomeSheetVO);
  }

  @Override
  public List<OutcomeSheetVO> getSheetByState(OutcomeSheetState OutcomeSheetState) {
    Sheet sheet = new OutcomeSheet(OutcomeSheetDao, customerService);
    List<SheetVO> temp_list = sheet.findSheetByState(OutcomeSheetState);
    return new ArrayList(temp_list);
  }

  @Override
  public void approval(String id, OutcomeSheetState state) {
    Sheet sheet = new OutcomeSheet(OutcomeSheetDao, customerService);
    sheet.approval(id, state);
  }

}
