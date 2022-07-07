package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.dao.WarehouseGivenSheetDao;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.WarehouseGivenSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseGivenSheetPO;
import com.nju.edu.erp.model.po.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.po.WarehousePO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGivenSheetContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGivenSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.utils.IdGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

public class WarehouseGivenSheet implements Sheet{

  private WarehouseGivenSheetDao warehouseGivenSheetDao;
  private WarehouseDao warehouseDao;

  public WarehouseGivenSheet(WarehouseGivenSheetDao warehouseGivenSheetDao, WarehouseDao warehouseDao) {
    this.warehouseGivenSheetDao = warehouseGivenSheetDao;
    this.warehouseDao = warehouseDao;
  }

  public void makeSheet(UserVO userVO, SheetVO sheetVO) {
    assert sheetVO instanceof WarehouseGivenSheetVO;
    WarehouseGivenSheetVO warehouseGivenSheetVO = (WarehouseGivenSheetVO) sheetVO;
    WarehouseGivenSheetPO warehouseGivenSheetPO = new WarehouseGivenSheetPO();
    BeanUtils.copyProperties(warehouseGivenSheetVO, warehouseGivenSheetPO);

    warehouseGivenSheetPO.setOperator(userVO.getName());
    WarehouseGivenSheetPO latest = warehouseGivenSheetDao.getLatest();
    String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "ZSD");
    warehouseGivenSheetPO.setId(id);
    warehouseGivenSheetPO.setState(WarehouseGivenSheetState.PENDING);

    List<WarehouseGivenSheetContentPO> pos = new ArrayList<>();
    for (WarehouseGivenSheetContentVO vo : warehouseGivenSheetVO.getProducts()) {
      WarehouseGivenSheetContentPO po = new WarehouseGivenSheetContentPO();
      BeanUtils.copyProperties(vo, po);
      po.setWarehouse_given_sheet_id(warehouseGivenSheetPO.getId());
      pos.add(po);
    }

    warehouseGivenSheetDao.saveSheet(warehouseGivenSheetPO);
  }

  public List<SheetVO> findSheetByState(SheetState state) {
    List<SheetVO> res = new ArrayList<>();
    List<WarehouseGivenSheetPO> all;
    if(state == null) {
      all = warehouseGivenSheetDao.findAll();
    } else {
      all = warehouseGivenSheetDao.findAllByState((WarehouseGivenSheetState) state);
    }

    for (WarehouseGivenSheetPO po : all) {
      WarehouseGivenSheetVO vo = new WarehouseGivenSheetVO();
      BeanUtils.copyProperties(po, vo);
      List<WarehouseGivenSheetContentPO> contents = warehouseGivenSheetDao.findContentById(po.getId());
      List<WarehouseGivenSheetContentVO> vos = new ArrayList<>();
      for (WarehouseGivenSheetContentPO contentPO : contents) {
        WarehouseGivenSheetContentVO contentVO = new WarehouseGivenSheetContentVO();
        BeanUtils.copyProperties(contentPO, contentVO);
        vos.add(contentVO);
      }
      res.add(vo);
    }
    return res;
  }

  public void approval(String sheetId, SheetState state) {
    WarehouseGivenSheetPO warehouseGivenSheetPO = warehouseGivenSheetDao.getSheetById(sheetId);
    WarehouseGivenSheetState warehouseGivenSheetState = (WarehouseGivenSheetState) state;
    if (warehouseGivenSheetState.equals(WarehouseGivenSheetState.FAILURE)) {
      if (warehouseGivenSheetPO.getState().equals(WarehouseGivenSheetState.SUCCESS)) throw new RuntimeException("状态更新失败");
      int effectLines = warehouseGivenSheetDao.update(sheetId, warehouseGivenSheetState);
      if (effectLines == 0) throw new RuntimeException("状态更新失败");
    } else {
      WarehouseGivenSheetState prevState;
      if (warehouseGivenSheetState.equals(WarehouseGivenSheetState.SUCCESS)) {
        prevState = WarehouseGivenSheetState.PENDING;
      } else {
        throw new RuntimeException("状态更新失败");
      }
      int effectLines = warehouseGivenSheetDao.updateV2(sheetId, prevState, warehouseGivenSheetState);
      if (effectLines == 0) throw new RuntimeException("状态更新失败");
      //修改时间
      warehouseGivenSheetPO.setCreate_time(new Date());
      warehouseGivenSheetDao.saveSheet(warehouseGivenSheetPO);
      //减去库存
      for (WarehouseGivenSheetContentPO po : warehouseGivenSheetDao.findContentById(warehouseGivenSheetPO.getId())) {
        List<WarehousePO> warehousePOS = warehouseDao.findByPidOrderByPurchasePricePos(po.getPid());
        if (warehousePOS.size() == 0) throw new RuntimeException("库存不足");
        WarehousePO warehousePO = warehousePOS.get(0);
        warehousePO.setQuantity(warehousePO.getQuantity() - po.getAmount());
        warehouseDao.deductQuantity(warehousePO);
      }
    }
  }
}
