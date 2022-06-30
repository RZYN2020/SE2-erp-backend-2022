package com.nju.edu.erp.enums;

public enum PaymentMethod implements BaseEnum<CustomerType, Integer>{
  Yearly(1),
  Monthly(2);

  private final Integer value;

  PaymentMethod(Integer value) {
    this.value = value;
  }

  @Override
  public Integer getValue() {
    return value;
  }
}
