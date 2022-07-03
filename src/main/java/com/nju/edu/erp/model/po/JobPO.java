package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPO {
    private String name; // 员工姓名
    private BigDecimal basicSalary; // 基本工资
    private BigDecimal jobSalary; // 岗位工资
    private Integer level; // 岗位级别
    private int calculateMethod; // 薪资计算方式
    private PaymentMethod paymentMethod; // 薪资发放方式
}
