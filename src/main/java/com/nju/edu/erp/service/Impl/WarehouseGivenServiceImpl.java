package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.dao.WarehouseGivenSheetDao;
import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGivenSheetVO;
import com.nju.edu.erp.service.WarehouseGivenService;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import com.nju.edu.erp.utils.sheet.Sheet;
import com.nju.edu.erp.utils.sheet.WarehouseGivenSheet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseGivenServiceImpl implements WarehouseGivenService {

  private WarehouseGivenSheetDao warehouseGivenSheetDao;
  private WarehouseDao warehouseDao;

  @Autowired
  public WarehouseGivenServiceImpl(WarehouseGivenSheetDao warehouseGivenSheetDao, WarehouseDao warehouseDao) {
    this.warehouseDao = warehouseDao;
    this.warehouseGivenSheetDao = warehouseGivenSheetDao;
  }

  @Override
  public void makeSheet(UserVO userVO, WarehouseGivenSheetVO sheetVO) {
    Sheet sheet = new WarehouseGivenSheet(warehouseGivenSheetDao, warehouseDao);
    sheet.makeSheet(userVO, sheetVO);
  }

  @Override
  public List<WarehouseGivenSheetVO> getSheetByState(WarehouseGivenSheetState state) {
    Sheet sheet = new WarehouseGivenSheet(warehouseGivenSheetDao, warehouseDao);
    List<SheetVO> temp_list = sheet.findSheetByState(state);
    return new ArrayList(temp_list);
  }

  @Override
  public void approval(String sheetId, WarehouseGivenSheetState state) {
    Sheet sheet = new WarehouseGivenSheet(warehouseGivenSheetDao, warehouseDao);
    sheet.approval(sheetId, state);
  }

}
