package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.EmployeeDao;
import java.util.Date;

public class SignIn {
  private EmployeeDao employeeDao;

  public SignIn(EmployeeDao employeeDao) {
    this.employeeDao = employeeDao;
  }

  public int findAbsence(String username) {
    int signInTimes = employeeDao.findSignInTimes(username);
    return getToday() - signInTimes;
  }

   public int getToday() {
    // 返回今天是一个月第几天
    Date now = new Date();
    return Integer.parseInt(now.toString().split(" ")[2]);
  }
}
