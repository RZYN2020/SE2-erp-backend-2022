package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeVO {
    private Integer id; // 员工id
    private String name; // 姓名
    private String gender; // 性别
    private Date birthDate; // 出生日期
    private String phoneNumber; // 手机
    private String job; // 工作岗位
    private Integer jobLevel; // 岗位级别
    private String account; // 工资卡账户
}
