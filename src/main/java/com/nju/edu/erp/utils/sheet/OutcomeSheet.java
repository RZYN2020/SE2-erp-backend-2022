package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.OutcomeSheetDao;
import com.nju.edu.erp.enums.sheetState.OutcomeSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.OutcomeSheetContentPO;
import com.nju.edu.erp.model.po.OutcomeSheetPO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetContentVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetContentVO;
import com.nju.edu.erp.model.vo.outcome.OutcomeSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.utils.IdGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class OutcomeSheet implements Sheet {

  private OutcomeSheetDao outcomeSheetDao;
  private CustomerService customerService;

  public OutcomeSheet(OutcomeSheetDao outcomeSheetDao, CustomerService customerService) {
    this.outcomeSheetDao = outcomeSheetDao;
    this.customerService = customerService;
  }

  public void makeSheet(UserVO userVO, SheetVO sheetVO) {
    if (sheetVO == null) return;
    assert sheetVO instanceof OutcomeSheetVO;
    OutcomeSheetVO outcomeSheetVO = (OutcomeSheetVO) sheetVO;
    OutcomeSheetPO outcomeSheetPO = new OutcomeSheetPO();
    BeanUtils.copyProperties(outcomeSheetVO, outcomeSheetPO);
    if (outcomeSheetVO.getOutcome_sheet_content().size() == 0) {
      return;
    }

    outcomeSheetPO.setOperator(userVO.getName());
    OutcomeSheetPO latest = outcomeSheetDao.getLatest();
    String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "SKD");
    outcomeSheetPO.setId(id);
    outcomeSheetPO.setCreate_time(new Date());
    outcomeSheetPO.setState(OutcomeSheetState.PENDING);
    BigDecimal totalAmount = BigDecimal.ZERO;
    List<OutcomeSheetContentPO> contentBatch = new ArrayList<>();
    for (OutcomeSheetContentVO vo : outcomeSheetVO.getOutcome_sheet_content()) {
      OutcomeSheetContentPO po = new OutcomeSheetContentPO();
      BeanUtils.copyProperties(vo, po);
      po.setOutcome_sheet_id(id);
      totalAmount = totalAmount.add(po.getAmount());
      contentBatch.add(po);
    }

    outcomeSheetDao.saveBatchSheetContent(contentBatch);
    outcomeSheetPO.setTotal_amount(totalAmount);
    outcomeSheetDao.saveSheet(outcomeSheetPO);
  }

  public List<SheetVO> findSheetByState(SheetState state) {
    List<SheetVO> res = new ArrayList<>();
    List<OutcomeSheetPO> all;
    if(state == null) {
      all = outcomeSheetDao.findAll();
    } else {
      all = outcomeSheetDao.findAllByState((OutcomeSheetState) state);
    }


    for (OutcomeSheetPO po : all) {
      OutcomeSheetVO vo = new OutcomeSheetVO();
      BeanUtils.copyProperties(po, vo);
      List<OutcomeSheetContentPO> pos = outcomeSheetDao.findContentBySheetId(po.getId());
      List<OutcomeSheetContentVO> vos = new ArrayList<>();
      for (OutcomeSheetContentPO contentPO : pos) {
        assert contentPO instanceof OutcomeSheetContentPO;
        OutcomeSheetContentVO contentVO = new OutcomeSheetContentVO();
        BeanUtils.copyProperties(contentPO, contentVO);
        vos.add(contentVO);
      }
      vo.setOutcome_sheet_content(vos);
      res.add(vo);
    }

    return res;
  }

  public void approval(String sheetId, SheetState state) {
    OutcomeSheetPO outcomeSheetPO = outcomeSheetDao.getSheetById(sheetId);
    OutcomeSheetState outcomeSheetState = (OutcomeSheetState) state;
    if (outcomeSheetState.equals(OutcomeSheetState.FAILURE)) {
      if (outcomeSheetPO.getState().equals(OutcomeSheetState.SUCCESS)) throw new RuntimeException("状态更新失败");
      int effectLines = outcomeSheetDao.updateState(sheetId, outcomeSheetState);
      if(effectLines == 0) throw new RuntimeException("状态更新失败");
    } else {
      OutcomeSheetState prevState;
      if (outcomeSheetState.equals(OutcomeSheetState.SUCCESS)) {
        prevState = OutcomeSheetState.PENDING;
      } else {
        throw new RuntimeException("状态更新失败");
      }
      int effectLines = outcomeSheetDao.updateStateV2(sheetId, outcomeSheetState, prevState);
      if (effectLines == 0) throw new RuntimeException("状态更新失败");

      //更改应收数据
      CustomerPO customerPO = customerService.findCustomerById(outcomeSheetPO.getCustomer_id());
      customerPO.setReceivable(customerPO.getPayable().add(outcomeSheetPO.getTotal_amount()));
      customerService.updateCustomer(customerPO);

      //设置时间
      outcomeSheetDao.updateDate(outcomeSheetPO.getId(), new Date());
    }
  }

}
