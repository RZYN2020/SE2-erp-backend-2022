package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleReturnSheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.model.po.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import com.nju.edu.erp.model.po.SaleSheetContentPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.SaleRecordVO;
import com.nju.edu.erp.service.SaleDetailService;
import com.nju.edu.erp.service.SaleService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SaleDetailServiceImpl implements SaleDetailService {

  private SaleSheetDao saleSheetDao;
  private ProductDao productDao;
  private SaleReturnSheetDao saleReturnSheetDao;

  public SaleDetailServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao
  , SaleReturnSheetDao saleReturnSheetDao) {
    this.saleSheetDao = saleSheetDao;
    this.productDao = productDao;
    this.saleReturnSheetDao = saleReturnSheetDao;
  }

  @Override
  public List<SaleRecordVO> findAllRecords() {
    List<SaleRecordVO> all = new ArrayList<>();
    all.addAll(findSaleRecords());
    all.addAll(findSaleReturnRecords());

    return all;
  }

  private List<SaleRecordVO> findSaleRecords() {
    List<SaleRecordVO> all = new ArrayList<>();
    List<SaleSheetPO> all_sale_sheets = saleSheetDao.findAllSheet();

    for (SaleSheetPO sheetPO : all_sale_sheets) {
      List<SaleSheetContentPO> contentPOS = saleSheetDao.findContentBySheetId(sheetPO.getId());
      for (SaleSheetContentPO contentPO : contentPOS) {
        ProductPO productPO = productDao.findById(contentPO.getPid());
        SaleRecordVO recordVO = new SaleRecordVO();
        recordVO.setRecord_type("销售");
        recordVO.setProduct_name(productPO.getName());
        recordVO.setProduct_type(productPO.getType());
        recordVO.setUnit_price(contentPO.getUnitPrice());
        recordVO.setTotal_price(contentPO.getTotalPrice());
        recordVO.setSale_time(sheetPO.getCreateTime());
        all.add(recordVO);
      }
    }

    return all;
  }

  private List<SaleRecordVO> findSaleReturnRecords() {
    List<SaleRecordVO> all = new ArrayList<>();
    List<SaleReturnSheetPO> all_saleReturn_sheets = saleReturnSheetDao.findAllSheet();

    for (SaleReturnSheetPO sheetPO : all_saleReturn_sheets) {
      List<SaleReturnSheetContentPO> contentPOS = saleReturnSheetDao.findContentBySaleReturnSheetId(sheetPO.getSaleSheetID());
      for (SaleReturnSheetContentPO contentPO : contentPOS) {
        ProductPO productPO = productDao.findById(contentPO.getPid());
        SaleRecordVO recordVO = new SaleRecordVO();
        recordVO.setRecord_type("销售退货");
        recordVO.setProduct_name(productPO.getName());
        recordVO.setProduct_type(productPO.getType());
        recordVO.setUnit_price(contentPO.getUnitPrice());
        recordVO.setTotal_price(contentPO.getTotalPrice());
        recordVO.setSale_time(sheetPO.getCreate_time());
        all.add(recordVO);
      }
    }

    return all;
  }
}
