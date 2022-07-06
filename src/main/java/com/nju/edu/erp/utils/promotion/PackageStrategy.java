package com.nju.edu.erp.utils.promotion;

import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageStrategy implements PromotionStrategy {

  private List<String> pid;
  private List<Integer> amount;
  private BigDecimal voucher_amount; //减少的价格

  public PackageStrategy(List<String> pid, List<Integer> amount, BigDecimal voucher_amount) {
    assert pid.size() == amount.size();
    this.pid = pid;
    this.amount = amount;
    this.voucher_amount = voucher_amount;
  }

  @Override
  public boolean checkEffect(CustomerVO customerVO, List<SaleSheetContentVO> contentVOS) {
    Map<String, Integer> pid2Amount = new HashMap<>();

    for (SaleSheetContentVO vo : contentVOS) {
      assert !pid2Amount.containsKey(vo.getPid());
      pid2Amount.put(vo.getPid(), vo.getQuantity());
    }

    for (int i = 0; i < pid.size(); i++) {
      if (pid2Amount.get(pid.get(i)) < amount.get(i)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public PromotionInfo taskEffect() {
    PromotionInfo promotionInfo = new PromotionInfo();
    promotionInfo.setVoucher_amount(voucher_amount);
    return promotionInfo;
  }

}
