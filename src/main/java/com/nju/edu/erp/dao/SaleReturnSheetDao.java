package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.po.PurchaseReturnsSheetContentPO;
import com.nju.edu.erp.model.po.PurchaseReturnsSheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SaleReturnSheetDao {
  SaleReturnSheetPO getLatestSheet();

  int saveSheet(SaleReturnSheetPO toSave);

  void saveBatchSheetContent(List<SaleReturnSheetContentPO> saveList);

  List<SaleReturnSheetPO> findAllSheet();

  List<SaleReturnSheetPO> findAllByState(SaleReturnSheetState state);

  int updateState(String saleReturnSheetId, SaleReturnSheetState state);

  int updateStateV2(String saleReturnSheetId, SaleReturnSheetState prevState, SaleReturnSheetState state);

  SaleReturnSheetPO findSheetById(String saleReturnSheetId);

  List<SaleReturnSheetContentPO> findContentBySaleReturnSheetId(String saleReturnsSheetId);

  Integer findBatchId(String saleReturnSheetId);
}
