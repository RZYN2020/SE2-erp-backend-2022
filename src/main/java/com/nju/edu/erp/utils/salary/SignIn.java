package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.EmployeeDao;
import java.util.Date;

public class SignIn {
  private static EmployeeDao employeeDao;

  public static void init(EmployeeDao employeeDao_) {
    employeeDao = employeeDao_;
  }

  public static int findAbsence(String username) {
    int signInTimes = employeeDao.findSignInTimes(username);
    return getToday() - signInTimes;
  }

   public static int getToday() {
    // 返回今天是一个月第几天
    Date now = new Date();
    return Integer.parseInt(now.toString().split(" ")[2]);
  }
}
