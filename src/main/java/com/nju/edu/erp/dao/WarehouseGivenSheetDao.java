package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import com.nju.edu.erp.model.po.SaleSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseGivenSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseGivenSheetPO;
import com.nju.edu.erp.utils.sheet.WarehouseGivenSheet;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface WarehouseGivenSheetDao {

  WarehouseGivenSheetPO getLatest();

  int saveSheet(WarehouseGivenSheetPO warehouseGivenSheetPO);

  int saveBatch(List<WarehouseGivenSheetContentPO> contents);

  List<WarehouseGivenSheetPO> findAll();

  List<WarehouseGivenSheetPO> findAllByState(WarehouseGivenSheetState state);

  List<WarehouseGivenSheetContentPO> findContentById(String id);

  WarehouseGivenSheetPO getSheetById(String id);

  int update(String id, WarehouseGivenSheetState state);

  int updateV2(String id, WarehouseGivenSheetState prevState, WarehouseGivenSheetState state);

  int updateDate(String id, Date create_time);
}
