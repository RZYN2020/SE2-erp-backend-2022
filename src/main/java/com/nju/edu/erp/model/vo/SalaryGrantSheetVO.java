package com.nju.edu.erp.model.vo;

import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryGrantSheetVO implements SheetVO {
    // 包含单据编号、员工编号、姓名、银行账户信息、应发工资、扣除税款（个人所得税、失业保险、住房公积金）、实发金额
    private String id; // 单据编号
    private Integer employeeId; // 员工编号
    private String employeeName; // 员工姓名
    private String employeeAccount; // 银行账户信息, 对应employee中的工资卡账户
    private BigDecimal salaryBeforeTax; // 应发工资
    private BigDecimal commission;//提成
    private BigDecimal incomeTax;
    private BigDecimal insurance;
    private BigDecimal fund;
    private BigDecimal realSalary; // 实发金额

    private SalaryGrantSheetState state; // 审批状态
    private Date createTime;
}
