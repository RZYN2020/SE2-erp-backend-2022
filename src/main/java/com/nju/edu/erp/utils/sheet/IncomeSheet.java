package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.dao.IncomeSheetDao;
import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.IncomeSheetContentPO;
import com.nju.edu.erp.model.po.IncomeSheetPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetContentVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.utils.IdGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class IncomeSheet implements Sheet {

  private IncomeSheetDao incomeSheetDao;
  private CustomerService customerService;

  public IncomeSheet(IncomeSheetDao incomeSheetDao, CustomerService customerService) {
    this.incomeSheetDao = incomeSheetDao;
    this.customerService = customerService;
  }

  public void makeSheet(UserVO userVO, SheetVO sheetVO) {
    assert sheetVO instanceof IncomeSheetVO;
    IncomeSheetVO incomeSheetVO = (IncomeSheetVO) sheetVO;
    IncomeSheetPO incomeSheetPO = new IncomeSheetPO();
    BeanUtils.copyProperties(incomeSheetVO, incomeSheetPO);

    incomeSheetPO.setOperator(userVO.getName());
    IncomeSheetPO latest = incomeSheetDao.getLatest();
    String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "SKD");
    incomeSheetPO.setId(id);
    incomeSheetPO.setCreate_time(new Date());
    incomeSheetPO.setState(IncomeSheetState.PENDING);
    BigDecimal totalAmount = BigDecimal.ZERO;
    List<IncomeSheetContentPO> contentBatch = new ArrayList<>();
    for (IncomeSheetContentVO vo : incomeSheetVO.getIncome_sheet_content()) {
      IncomeSheetContentPO po = new IncomeSheetContentPO();
      BeanUtils.copyProperties(vo, po);
      po.setIncome_sheet_id(id);
      totalAmount = totalAmount.add(po.getAmount());
      contentBatch.add(po);
    }

    incomeSheetDao.saveBatchSheetContent(contentBatch);
    incomeSheetPO.setTotal_amount(totalAmount);
    incomeSheetDao.saveSheet(incomeSheetPO);
  }

  public List<SheetVO> findSheetByState(SheetState state) {
    List<SheetVO> res = new ArrayList<>();
    List<IncomeSheetPO> all;
    if(state == null) {
      all = incomeSheetDao.findAll();
    } else {
      all = incomeSheetDao.findAllByState((IncomeSheetState) state);
    }


    for (IncomeSheetPO po : all) {
      IncomeSheetVO vo = new IncomeSheetVO();
      BeanUtils.copyProperties(po, vo);
      List<IncomeSheetContentPO> pos = incomeSheetDao.findContentBySheetId(po.getId());
      List<IncomeSheetContentVO> vos = new ArrayList<>();
      for (IncomeSheetContentPO contentPO : pos) {
        IncomeSheetContentVO contentVO = new IncomeSheetContentVO();
        BeanUtils.copyProperties(contentPO, contentVO);
        vos.add(contentVO);
      }
      vo.setIncome_sheet_content(vos);
      res.add(vo);
    }

    return res;
  }

  public void approval(String sheetId, SheetState state) {
    IncomeSheetPO incomeSheetPO = incomeSheetDao.getSheetById(sheetId);
    IncomeSheetState incomeSheetState = (IncomeSheetState) state;
    if (incomeSheetState.equals(IncomeSheetState.FAILURE)) {
      if (incomeSheetPO.getState().equals(IncomeSheetState.SUCCESS)) throw new RuntimeException("状态更新失败");
      int effectLines = incomeSheetDao.updateState(sheetId, incomeSheetState);
      if(effectLines == 0) throw new RuntimeException("状态更新失败");
    } else {
      IncomeSheetState prevState;
      if (incomeSheetState.equals(SalarySheetState.SUCCESS)) {
        prevState = IncomeSheetState.PENDING;
      } else {
        throw new RuntimeException("状态更新失败");
      }
      int effectLines = incomeSheetDao.updateStateV2(sheetId, incomeSheetState, prevState);
      if (effectLines == 0) throw new RuntimeException("状态更新失败");

      //更改销售商应付数据
      CustomerPO customerPO = customerService.findCustomerById(incomeSheetPO.getCustomer_id());
      customerPO.setPayable(customerPO.getPayable().add(incomeSheetPO.getTotal_amount()));
      customerService.updateCustomer(customerPO);

      //设置时间
      incomeSheetDao.updateDate(incomeSheetPO.getId(), new Date());
    }
  }

}
