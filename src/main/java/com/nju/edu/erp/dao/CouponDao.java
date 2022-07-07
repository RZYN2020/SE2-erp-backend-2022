package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.promotion.CouponPO;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CouponDao {
  List<CouponPO> findByCustomer(Integer customer_id);

  int deleteOne(Integer customer_id, BigDecimal amount);

  int addOne(Integer customer_id, BigDecimal amount);
}
