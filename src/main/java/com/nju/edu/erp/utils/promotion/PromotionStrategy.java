package com.nju.edu.erp.utils.promotion;

import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import java.util.Date;
import java.util.List;

public abstract class PromotionStrategy {

  protected Date begin_date;

  protected Date end_date;
  /**
   * 判断该促销策略能否应用在这次销售中
   * @param customerVO 客户信息
   * @param contentVOS 销售信息
   * @return
   */
  abstract boolean checkEffect(CustomerVO customerVO, List<SaleSheetContentVO> contentVOS);

  /**
   * 返回该促销策略生效后的产物
   * @return
   */
  abstract PromotionInfo taskEffect();

}
