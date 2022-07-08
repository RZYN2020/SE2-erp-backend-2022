package com.nju.edu.erp.utils;

import java.lang.reflect.Field;

public class ObjectUtils {

  /**
   * 判断对象o是否含有为空的字段
   * @param o
   * @return true if has empty field;
   */
  public static boolean hasEmptyFields(Object o) {
    try {
      for (Field field : o.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        Object object = field.get(o);
        if (object == null) return true;
      }
      return false;
    } catch (Exception e) {
      System.out.println("判断对象字段异常");
      return true;
    }
  }
}
