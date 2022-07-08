package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SalarySheetDao {

  SalarySheetPO getLatest();

  int saveSheet(SalarySheetPO toSave);

  int updateDate(String id, Date create_time);

  List<SalarySheetPO> findAll();

  List<SalarySheetPO> findAllByState(SalarySheetState state);

  SalarySheetPO getSheetById(String id);

  int updateState(String id, SalarySheetState state);

  int updateStateV2(String id, SalarySheetState state, SalarySheetState prevState);
}
