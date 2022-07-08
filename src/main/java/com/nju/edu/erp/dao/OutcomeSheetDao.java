package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
import com.nju.edu.erp.model.po.OutcomeSheetContentPO;
import com.nju.edu.erp.model.po.OutcomeSheetPO;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OutcomeSheetDao {
  OutcomeSheetPO getLatest();

  int saveSheet(OutcomeSheetPO incomeSheetPO);

  void saveBatchSheetContent(List<OutcomeSheetContentPO> saveList);

  List<OutcomeSheetPO> findAll();

  List<OutcomeSheetPO> findAllByState(OutcomeSheetState incomeSheetState);

  List<OutcomeSheetContentPO> findContentBySheetId(String incomeSheetId);

  OutcomeSheetPO getSheetById(String id);

  int updateState(String id, OutcomeSheetState incomeSheetState);

  int updateStateV2(String id, OutcomeSheetState state, OutcomeSheetState prevState);

  int updateDate(String id, Date create_time);
}
