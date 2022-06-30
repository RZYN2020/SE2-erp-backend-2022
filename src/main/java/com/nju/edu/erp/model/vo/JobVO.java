package com.nju.edu.erp.model.vo;

import java.math.BigDecimal;

public class JobVO {
    String name; // 员工姓名
    BigDecimal basic_salary; // 基本工资
    BigDecimal job_salary; // 岗位工资
    Integer level; // 岗位级别, 同EmployeeVO里的job_level
    int calculate_method; // 薪资计算方式
    int payment_method; // 薪资发放方式
    int tax; // 税务信息(表驱动)
}
