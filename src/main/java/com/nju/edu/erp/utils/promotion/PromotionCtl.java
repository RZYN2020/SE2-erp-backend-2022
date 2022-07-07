package com.nju.edu.erp.utils.promotion;

import com.nju.edu.erp.dao.PromotionDao;
import com.nju.edu.erp.model.po.promotion.PackageStrategyContentPO;
import com.nju.edu.erp.model.po.promotion.PackageStrategyPO;
import com.nju.edu.erp.model.po.promotion.PriceStrategyPO;
import com.nju.edu.erp.model.po.promotion.UserStrategyPO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromotionCtl {

  public static List<PromotionStrategy> strategyList;

  private static PromotionDao promotionDao;

  public static void init(PromotionDao promotionDao) {
    PromotionCtl.promotionDao = promotionDao;
    strategyList = new ArrayList<>();
    init_userStrategy();
    init_packageStrategy();
    init_priceStrategy();
  }

  private static void init_userStrategy() {
    List<UserStrategyPO> userStrategyPOS = promotionDao.findAllUserStrategy();
    for (UserStrategyPO po : userStrategyPOS) {
      UserStrategy userStrategy = new UserStrategy(po.getEffect_level(), po.getProduct_id(), po.getProduct_amount(),
          po.getDiscount(), po.getCoupon(), po.getBegin_date(), po.getEnd_date());
      strategyList.add(userStrategy);
    }
  }

  private static void init_priceStrategy() {
    List<PriceStrategyPO> priceStrategyPOS = promotionDao.findAllPriceStrategy();
    for (PriceStrategyPO po : priceStrategyPOS) {
      PriceStrategy priceStrategy = new PriceStrategy(po.getEffect_price(), po.getProduct_id()
      , po.getProduct_amount(), po.getCoupon(), po.getBegin_date(), po.getEnd_date());
      strategyList.add(priceStrategy);
    }
  }

  private static void init_packageStrategy() {
    List<PackageStrategyPO> packageStrategyPOS = promotionDao.findAllPackageStrategy();
    for (PackageStrategyPO po : packageStrategyPOS) {
      List<PackageStrategyContentPO> packageStrategyContentPOS = promotionDao.findPackageContentsById(po.getId());
      List<String> pid = new ArrayList<>();
      List<Integer> amount = new ArrayList<>();
      for (PackageStrategyContentPO contentPO : packageStrategyContentPOS) {
        pid.add(contentPO.getProduct_id());
        amount.add(contentPO.getProduct_amount());
      }
      PackageStrategy packageStrategy = new PackageStrategy(pid, amount, po.getVoucher_amount(), po.getBegin_date(), po.getEnd_date());
      strategyList.add(packageStrategy);
    }
  }

}
