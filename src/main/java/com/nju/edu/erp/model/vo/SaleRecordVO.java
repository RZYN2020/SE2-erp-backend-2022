package com.nju.edu.erp.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRecordVO {
  private Date sale_time;
  private String record_type;
  private String product_name;
  private String product_type;
  private Integer customer_id;
  private String operator;
  private Integer amount;
  private BigDecimal unit_price;
  private BigDecimal total_price;
}
