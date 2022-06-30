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
    Integer id; // 员工id
    String name; // 姓名
    String gender; // 性别
    Date birth_date; // 出生日期
    String phone_number; // 手机
    String job; // 工作岗位
    Integer job_level; // 岗位级别
    String account; // 工资卡账户
}
