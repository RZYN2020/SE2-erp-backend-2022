package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.IncomeSheetContentPO;
import com.nju.edu.erp.model.po.IncomeSheetPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetContentPO;
import com.nju.edu.erp.service.IncomeService;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IncomeSheetDao {
  IncomeSheetPO getLatest();

  int saveSheet(IncomeSheetPO incomeSheetPO);

  void saveBatchSheetContent(List<IncomeSheetContentPO> saveList);

  List<IncomeSheetPO> findAll();

  List<IncomeSheetPO> findAllByState(IncomeSheetState incomeSheetState);

  List<IncomeSheetContentPO> findContentBySheetId(String incomeSheetId);

  IncomeSheetPO getSheetById(String id);

  int updateState(String id, IncomeSheetState incomeSheetState);

  int updateStateV2(String id, IncomeSheetState state, IncomeSheetState prevState);
}
