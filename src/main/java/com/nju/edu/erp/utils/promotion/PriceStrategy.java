package com.nju.edu.erp.utils.promotion;

import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import java.math.BigDecimal;
import java.util.List;

public class PriceStrategy implements PromotionStrategy{

  private BigDecimal effect_price; //生效的总价
  private String product_id; //赠品id
  private Integer product_amount; //赠品数量
  private BigDecimal coupon; //赠送的代金券金额

  public PriceStrategy(BigDecimal effect_price, String product_id,
      Integer product_amount, BigDecimal coupon) {
    this.effect_price = effect_price;
    this.product_id = product_id;
    this.product_amount = product_amount;
    this.coupon = coupon;
  }

  @Override
  public boolean checkEffect(CustomerVO customerVO, List<SaleSheetContentVO> contentVOS) {
    BigDecimal rawAmount = BigDecimal.ZERO;
    for (SaleSheetContentVO vo : contentVOS) {
      rawAmount.add(vo.getTotalPrice());
    }
    return rawAmount.compareTo(this.effect_price) >= 0;
  }

  @Override
  public PromotionInfo taskEffect() {
    PromotionInfo promotionInfo = new PromotionInfo();
    promotionInfo.setPid(this.product_id);
    promotionInfo.setAmount(this.product_amount);
    promotionInfo.setCoupon(this.coupon);
    return promotionInfo;
  }
}
