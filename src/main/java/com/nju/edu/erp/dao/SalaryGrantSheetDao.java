package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.model.po.SalaryGrantSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalaryGrantSheetDao {

    SalaryGrantSheetPO getSheetById(String id);

    List<SalaryGrantSheetPO> findAll();

    List<SalaryGrantSheetPO> findAllByState(SalaryGrantSheetState state);

    int updateState(String id, SalaryGrantSheetState state);

    int updateStateV2(String id, SalaryGrantSheetState state, SalaryGrantSheetState prevState);

    SalaryGrantSheetPO getLatest();

    int saveSheet(SalaryGrantSheetPO toSave);
}
