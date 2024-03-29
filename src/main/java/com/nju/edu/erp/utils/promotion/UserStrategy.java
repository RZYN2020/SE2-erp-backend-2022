package com.nju.edu.erp.utils.promotion;

import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class UserStrategy extends PromotionStrategy{

  private Integer effect_level; //生效的客户等级
  private String product_id; //赠品id
  private Integer product_amount; //赠品数量
  private BigDecimal discount; //折扣
  private BigDecimal coupon; //赠送的代金券金额

  public UserStrategy(Integer effect_level, String product_id, Integer product_amount,
      BigDecimal discount, BigDecimal coupon, Date begin, Date end) {
    this.effect_level = effect_level;
    this.product_id = product_id;
    this.product_amount = product_amount;
    this.discount = discount;
    this.coupon = coupon;
    this.begin_date = begin;
    this.end_date = end;
  }

  @Override
  public boolean checkEffect(CustomerPO customerPO, List<SaleSheetContentVO> contentVOS, Date date) {
    assert customerPO.getLevel() != null;
    return this.effect_level == customerPO.getLevel() && date.before(this.end_date) && date.after(this.begin_date);
  }

  @Override
  public PromotionInfo taskEffect() {
    PromotionInfo promotionInfo = new PromotionInfo();
    promotionInfo.setPid(this.product_id);
    promotionInfo.setAmount(this.product_amount);
    promotionInfo.setDiscount(this.discount);
    promotionInfo.setCoupon(this.coupon);
    return promotionInfo;
  }
}
