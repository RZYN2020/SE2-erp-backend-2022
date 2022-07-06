package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.promotion.PackageStrategyVO;
import com.nju.edu.erp.model.vo.promotion.PriceStrategyVO;
import com.nju.edu.erp.model.vo.promotion.UserStrategyVO;
import com.nju.edu.erp.utils.promotion.PackageStrategy;
import java.util.List;

public interface PromotionService {

  void createUserStrategy(UserStrategyVO userStrategyVO);

  void createPriceStrategy(PriceStrategyVO priceStrategyVO);

  void createPackageStrategy(PackageStrategyVO packageStrategyVO);

  List<UserStrategyVO> getAllUserStrategy();

  List<PriceStrategyVO> getAllPriceStrategy();

  List<PackageStrategyVO> getAllPackageStrategy();

}
