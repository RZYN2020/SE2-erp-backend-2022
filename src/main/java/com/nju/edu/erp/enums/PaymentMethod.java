package com.nju.edu.erp.enums;

public enum PaymentMethod implements BaseEnum<CustomerType, String>{
  Yearly("年薪制"),
  Monthly("月薪制");

  private final String value;

  PaymentMethod(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
