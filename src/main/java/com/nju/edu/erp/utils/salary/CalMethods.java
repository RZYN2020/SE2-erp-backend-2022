package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.JobDao;
import java.util.ArrayList;

public class CalMethods {
  private static ArrayList<CalculateMethod> calculateMethods;
  private static JobDao jobDao;
  private static SignIn signIn;

  public static void init(JobDao jobDao_, SignIn signIn_) {
    assert calculateMethods == null;
    calculateMethods = new ArrayList<>();
    jobDao = jobDao_;
    signIn = signIn_;

    calculateMethods.add(new CM1(jobDao, signIn));
    calculateMethods.add(new CM2(jobDao, signIn));
  }

  public static void add(CalculateMethod cm) {
    calculateMethods.add(cm);
  }

  public static CalculateMethod get(Integer idx) {
    return calculateMethods.get(idx);
  }


}
