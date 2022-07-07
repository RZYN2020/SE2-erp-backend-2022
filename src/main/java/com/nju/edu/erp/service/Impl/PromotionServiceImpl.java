package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PromotionDao;
import com.nju.edu.erp.model.po.promotion.PackageStrategyContentPO;
import com.nju.edu.erp.model.po.promotion.PackageStrategyPO;
import com.nju.edu.erp.model.po.promotion.PriceStrategyPO;
import com.nju.edu.erp.model.po.promotion.UserStrategyPO;
import com.nju.edu.erp.model.vo.promotion.PackageStrategyVO;
import com.nju.edu.erp.model.vo.promotion.PriceStrategyVO;
import com.nju.edu.erp.model.vo.promotion.UserStrategyVO;
import com.nju.edu.erp.service.PromotionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl implements PromotionService {

  private PromotionDao promotionDao;

  @Autowired
  public PromotionServiceImpl(PromotionDao promotionDao) {
    this.promotionDao = promotionDao;
  }

  @Override
  public void createUserStrategy(UserStrategyVO userStrategyVO) {
    UserStrategyPO userStrategyPO = new UserStrategyPO();
    BeanUtils.copyProperties(userStrategyVO, userStrategyPO);
    promotionDao.saveUserStrategy(userStrategyPO);
  }

  @Override
  public void createPriceStrategy(PriceStrategyVO priceStrategyVO) {
    PriceStrategyPO priceStrategyPO = new PriceStrategyPO();
    BeanUtils.copyProperties(priceStrategyVO, priceStrategyPO);
    promotionDao.savePriceStrategy(priceStrategyPO);
  }

  @Override
  public void createPackageStrategy(PackageStrategyVO packageStrategyVO) {
    PackageStrategyPO packageStrategyPO = new PackageStrategyPO();
    BeanUtils.copyProperties(packageStrategyVO, packageStrategyPO);
    PackageStrategyPO latest = promotionDao.findLatest();
    Integer id;
    if (latest == null) {
      id = 1;
    } else {
      id = latest.getId() + 1;
    }
    List<PackageStrategyContentPO> pos = new ArrayList<>();
    for (int i = 0; i < packageStrategyVO.getProduct_id().size(); i++) {
      PackageStrategyContentPO po = new PackageStrategyContentPO();
      po.setPackage_strategy_id(id);
      po.setProduct_id(packageStrategyVO.getProduct_id().get(i));
      po.setProduct_amount(packageStrategyVO.getProduct_amount().get(i));
      pos.add(po);
    }
    promotionDao.savePackageContents(pos);
    promotionDao.savePackageStrategy(packageStrategyPO);
  }

  @Override
  public List<UserStrategyVO> getAllUserStrategy()  {
    List<UserStrategyPO> userStrategyPOS = promotionDao.findAllUserStrategy();
    List<UserStrategyVO> userStrategyVOS = new ArrayList<>();
    for (UserStrategyPO po : userStrategyPOS) {
      UserStrategyVO userStrategyVO = new UserStrategyVO();
      BeanUtils.copyProperties(po, userStrategyVO);
      userStrategyVOS.add(userStrategyVO);
    }
    return userStrategyVOS;
  }

  @Override
  public List<PriceStrategyVO> getAllPriceStrategy() {
    List<PriceStrategyPO> priceStrategyPOS = promotionDao.findAllPriceStrategy();
    List<PriceStrategyVO> priceStrategyVOS = new ArrayList<>();
    for (PriceStrategyPO po : priceStrategyPOS) {
      PriceStrategyVO priceStrategyVO = new PriceStrategyVO();
      BeanUtils.copyProperties(po, priceStrategyVO);
      priceStrategyVOS.add(priceStrategyVO);
    }
    return priceStrategyVOS;
  }

  @Override
  public List<PackageStrategyVO> getAllPackageStrategy() {
    List<PackageStrategyPO> packageStrategyPOS = promotionDao.findAllPackageStrategy();
    List<PackageStrategyVO> packageStrategyVOS = new ArrayList<>();
    for (PackageStrategyPO po : packageStrategyPOS) {
      PackageStrategyVO packageStrategyVO = new PackageStrategyVO();
      BeanUtils.copyProperties(po, packageStrategyVO);
      List<PackageStrategyContentPO> contentPOS = promotionDao.findPackageContentsById(po.getId());
      List<String> pid = new ArrayList<>();
      List<Integer> amount = new ArrayList<>();
      for (PackageStrategyContentPO contentPO : contentPOS) {
        pid.add(contentPO.getProduct_id());
        amount.add(contentPO.getProduct_amount());
      }
      packageStrategyVO.setProduct_id(pid);
      packageStrategyVO.setProduct_amount(amount);
      packageStrategyVOS.add(packageStrategyVO);
    }
    return packageStrategyVOS;
  }

}
