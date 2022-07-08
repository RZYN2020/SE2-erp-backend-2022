package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGivenSheetVO;
import java.util.List;

public interface WarehouseGivenService {

  void makeSheet(UserVO userVO, WarehouseGivenSheetVO sheetVO);

  List<WarehouseGivenSheetVO> getSheetByState(WarehouseGivenSheetState state);

  void approval(String sheetId, WarehouseGivenSheetState state);
}
