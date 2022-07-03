package com.nju.edu.erp.utils.salary;

import com.nju.edu.erp.dao.JobDao;
import java.util.ArrayList;

public class CalMethods {
  private static ArrayList<CalculateMethod> calculateMethods;
  private static JobDao jobDao;


  public static void init(JobDao jobDao_) {
    assert calculateMethods == null;
    calculateMethods = new ArrayList<>();
    jobDao = jobDao_;


    calculateMethods.add(new CM1(jobDao));
    calculateMethods.add(new CM2(jobDao));
  }

  public static void add(CalculateMethod cm) {
    calculateMethods.add(cm);
  }

  public static CalculateMethod get(Integer idx) {
    return calculateMethods.get(idx);
  }

  public static int getSize() {
    return calculateMethods.size();
  }
}
