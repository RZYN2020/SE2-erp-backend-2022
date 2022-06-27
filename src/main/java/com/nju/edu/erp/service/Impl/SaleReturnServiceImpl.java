package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleReturnSheetDao;
import com.nju.edu.erp.dao.SaleReturnSheetDao;
import com.nju.edu.erp.dao.SaleReturnSheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.dao.WarehouseOutputSheetDao;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.model.po.PurchaseSheetContentPO;
import com.nju.edu.erp.model.po.PurchaseSheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetContentPO;
//import com.nju.edu.erp.model.vo.Sale.SaleReturnSheetContentVO;
//import com.nju.edu.erp.model.vo.Sale.SaleReturnSheetVO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.po.WarehouseInputSheetContentPO;
import com.nju.edu.erp.model.po.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.po.WarehousePO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetContentVO;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseInputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseInputFormVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleReturnService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleReturnServiceImpl implements SaleReturnService {

  private final SaleReturnSheetDao saleReturnSheetDao;

  private final SaleSheetDao saleSheetDao;

  private final WarehouseOutputSheetDao warehouseOutputSheetDao;

  private final WarehouseDao warehouseDao;

  private final ProductDao productDao;

  private final CustomerDao customerDao;

  private final ProductService productService;

  private final CustomerService customerService;

  private final WarehouseService warehouseService;

  @Autowired
  public SaleReturnServiceImpl(SaleReturnSheetDao saleReturnSheetDao, SaleSheetDao saleSheetDao,
      WarehouseOutputSheetDao warehouseOutputSheetDao, ProductDao productDao,
      CustomerDao customerDao, ProductService productService,
      CustomerService customerService, WarehouseService warehouseService, WarehouseDao warehouseDao) {
    this.saleReturnSheetDao = saleReturnSheetDao;
    this.saleSheetDao = saleSheetDao;
    this.warehouseOutputSheetDao = warehouseOutputSheetDao;
    this.productDao = productDao;
    this.customerDao = customerDao;
    this.productService = productService;
    this.customerService = customerService;
    this.warehouseService = warehouseService;
    this.warehouseDao = warehouseDao;
  }

  @Override
  @Transactional
  public void makeSaleReturnSheet(UserVO userVO, SaleReturnSheetVO saleReturnSheetVO) {
    SaleReturnSheetPO saleReturnSheetPO = new SaleReturnSheetPO();
    BeanUtils.copyProperties(saleReturnSheetVO, saleReturnSheetPO);
    // 此处根据制定单据人员确定操作员
    saleReturnSheetPO.setOperator(userVO.getName());
    SaleReturnSheetPO latest = saleReturnSheetDao.getLatestSheet();
    String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSTHD");
    saleReturnSheetPO.setId(id);
    saleReturnSheetPO.setState(SaleReturnSheetState.PENDING_LEVEL_1);
    BigDecimal totalAmount = BigDecimal.ZERO;
    List<SaleReturnSheetContentPO> saleReturnSheetContent = saleReturnSheetDao.findContentBySaleReturnSheetId(saleReturnSheetPO.getId());
    Map<String, SaleReturnSheetContentPO> map = new HashMap<>();
    for(SaleReturnSheetContentPO item : saleReturnSheetContent) {
      map.put(item.getPid(), item);
    }

    SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnSheetPO.getSaleSheetID());
    BigDecimal discount = saleSheetPO.getDiscount();
    BigDecimal voucher_amount = saleSheetPO.getVoucherAmount();
    BigDecimal total_amount = saleSheetPO.getRawTotalAmount();
    BigDecimal ratio = discount.subtract(voucher_amount.divide(total_amount));

    List<SaleReturnSheetContentPO> pContentPOList = new ArrayList<>();
    for(SaleReturnSheetContentVO content : saleReturnSheetVO.getSaleReturnSheetContent()) {
      SaleReturnSheetContentPO pContentPO = new SaleReturnSheetContentPO();
      BeanUtils.copyProperties(content,pContentPO);
      pContentPO.setSaleReturnSheetId(id);
      BigDecimal returnPrice = pContentPO.getUnitPrice().multiply(ratio);
      pContentPO.setTotalPrice(returnPrice.multiply(BigDecimal.valueOf(pContentPO.getQuantity())));
      pContentPOList.add(pContentPO);
      totalAmount = totalAmount.add(pContentPO.getTotalPrice());
    }
    saleReturnSheetDao.saveBatchSheetContent(pContentPOList);
    saleReturnSheetPO.setTotalAmount(totalAmount);
    saleReturnSheetDao.saveSheet(saleReturnSheetPO);
  }

  @Override
  @Transactional
  public List<SaleReturnSheetVO> getSaleReturnSheetByState(SaleReturnSheetState state) {
    List<SaleReturnSheetVO> res = new ArrayList<>();
    List<SaleReturnSheetPO> all;
    if(state == null) {
      all = saleReturnSheetDao.findAllSheet();
    } else {
      all = saleReturnSheetDao.findAllByState(state);
    }
    for(SaleReturnSheetPO po: all) {
      SaleReturnSheetVO vo = new SaleReturnSheetVO();
      BeanUtils.copyProperties(po, vo);
      List<SaleReturnSheetContentPO> all_saleContent = saleReturnSheetDao.findContentBySaleReturnSheetId(po.getId());
      List<SaleReturnSheetContentVO> vos = new ArrayList<>();
      for (SaleReturnSheetContentPO p : all_saleContent) {
        SaleReturnSheetContentVO v = new SaleReturnSheetContentVO();
        BeanUtils.copyProperties(p, v);
        vos.add(v);
      }
      vo.setSaleReturnSheetContent(vos);
      res.add(vo);
    }
    return res;
  }

  @Override
  @Transactional
  public void approval(String saleReturnSheetId, SaleReturnSheetState state) {

    SaleReturnSheetPO saleReturnSheet = saleReturnSheetDao.findSheetById(saleReturnSheetId);

    if(state.equals(SaleReturnSheetState.FAILURE)) {
      if(saleReturnSheet.getState() == SaleReturnSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
      int effectLines = saleReturnSheetDao.updateState(saleReturnSheetId, state);
      if(effectLines == 0) throw new RuntimeException("状态更新失败");
    } else {
      SaleReturnSheetState prevState;
      if(state.equals(SaleReturnSheetState.SUCCESS)) {
        prevState = SaleReturnSheetState.PENDING_LEVEL_2;
      } else if(state.equals(SaleReturnSheetState.PENDING_LEVEL_2)) {
        prevState = SaleReturnSheetState.PENDING_LEVEL_1;
      } else {
        throw new RuntimeException("状态更新失败");
      }
      int effectLines = saleReturnSheetDao.updateStateV2(saleReturnSheetId, prevState, state);
      if(effectLines == 0) throw new RuntimeException("状态更新失败");
      if(state.equals(SaleReturnSheetState.SUCCESS)) {
        List<SaleReturnSheetContentPO> saleReturnSheetContent =  saleReturnSheetDao.findContentBySaleReturnSheetId(saleReturnSheetId);
        List<WarehouseOutputSheetContentPO> wareOut_list = warehouseOutputSheetDao.getAllContentBySHD(saleReturnSheet.getSaleSheetID());
        Map<String, List<WarehouseOutputSheetContentPO>> map = new HashMap<>();
        for (WarehouseOutputSheetContentPO item : wareOut_list) {
          if (!map.containsKey(item.getPid())) {
            map.put(item.getPid(), new ArrayList<>());
          }
          map.get(item.getPid()).add(item);
        }

        List<WarehouseInputFormContentVO> warehouseInputFormContentVOS = new ArrayList<>();

        for(SaleReturnSheetContentPO content : saleReturnSheetContent) {
          feedProducts(map.get(content.getPid()), content);
        }

        SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleReturnSheetId);
        CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
        customerPO.setPayable(customerPO.getPayable().add(saleReturnSheet.getTotalAmount()));
        customerService.updateCustomer(customerPO);
      }
    }
  }

  private void feedProducts(List<WarehouseOutputSheetContentPO> outProducts, SaleReturnSheetContentPO content) {
    int amount = content.getQuantity();
    List<WarehouseInputFormContentVO> inProducts = new ArrayList<>();
    for (WarehouseOutputSheetContentPO item : outProducts) {
      WarehouseInputFormContentVO wiContentVO = new WarehouseInputFormContentVO();
      int consume_amount = Math.min(amount, item.getQuantity());
      amount -= consume_amount;
      WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(item.getPid(), item.getBatchId());
      warehousePO.setQuantity(warehousePO.getQuantity() + consume_amount);
      warehouseDao.deductQuantity(warehousePO);
      if (amount == 0) return;
    }
  }

}
