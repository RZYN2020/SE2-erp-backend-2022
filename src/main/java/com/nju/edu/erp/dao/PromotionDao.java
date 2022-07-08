package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.promotion.PackageStrategyContentPO;
import com.nju.edu.erp.model.po.promotion.PackageStrategyPO;
import com.nju.edu.erp.model.po.promotion.PriceStrategyPO;
import com.nju.edu.erp.model.po.promotion.UserStrategyPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
@Repository
@Mapper
public interface PromotionDao {

  int saveUserStrategy(UserStrategyPO userStrategyPO);

  int savePriceStrategy(PriceStrategyPO priceStrategyPO);

  int savePackageStrategy(PackageStrategyPO packageStrategyPO);

  int savePackageContents(List<PackageStrategyContentPO> contentPOS);

  List<UserStrategyPO> findAllUserStrategy();

  List<PriceStrategyPO> findAllPriceStrategy();

  List<PackageStrategyPO> findAllPackageStrategy();

  List<PackageStrategyContentPO> findPackageContentsById(Integer id);

  PackageStrategyPO findLatest();

}
