package com.nju.edu.erp.model.vo;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxVO {
  BigDecimal income_tax;
  BigDecimal insurance;
  BigDecimal fund;
}
