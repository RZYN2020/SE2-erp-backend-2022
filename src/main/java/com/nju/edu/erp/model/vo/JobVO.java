package com.nju.edu.erp.model.vo;

import java.math.BigDecimal;

import com.nju.edu.erp.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobVO {
    private String name; // 员工姓名
    private BigDecimal basicSalary; // 基本工资
    private BigDecimal jobSalary; // 岗位工资
    private Integer level; // 岗位级别, 同EmployeeVO里的job_level
    private int calculateMethod; // 薪资计算方式
    private PaymentMethod paymentMethod; // 薪资发放方式(月薪制 年薪制)
}
