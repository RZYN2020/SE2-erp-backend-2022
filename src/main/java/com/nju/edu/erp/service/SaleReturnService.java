package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.vo.SaleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SaleReturnService {

  /**
   * 制定销售退货单
   * @param userVO
   * @param saleReturnSheetVO 销售退货单
   */
  void makeSaleReturnSheet(UserVO userVO, SaleReturnSheetVO saleReturnSheetVO);

  /**
   * 根据状态获取销售退货单(state == null 则获取所有销售退货单)
   * @param state
   * @return
   */
  List<SaleReturnSheetVO> getSaleReturnSheetByState(SaleReturnSheetState state);

  /**
   * 审批
   * @param saleReturnsSheetId
   * @param state
   */
  void approval(String saleReturnsSheetId, SaleReturnSheetState state);

}
