package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.EmployeeDao;
import java.util.Date;

public class SignIn {
  private static EmployeeDao employeeDao;

  public static void init(EmployeeDao employeeDao_) {
    employeeDao = employeeDao_;
  }

  /**
   * 查询用户的缺勤天数
   * @param username
   * @return
   */
  public static int findAbsence(String username) {
    int signInTimes = employeeDao.findSignInTimes(username);
    return getToday() - signInTimes;
  }

  /**
   * 返回今天是本月的第几天
   * @return
   */
   public static int getToday() {
    Date now = new Date();
    return Integer.parseInt(now.toString().split(" ")[2]);
  }

    /**
     * 返回今天是本月的第几天
     * @return
     */
    public static boolean canSignIn(Date lastSignDate) {
        Date now = new Date();
        String[] nowArray = now.toString().split(" ");
        String[] tarArray = lastSignDate.toString().split(" ");
        return !(nowArray[1].equals(tarArray[1]) && nowArray[2].equals(tarArray[2]));
    }
}
