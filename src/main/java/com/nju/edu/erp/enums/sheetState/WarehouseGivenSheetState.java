package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum WarehouseGivenSheetState implements BaseEnum<WarehouseGivenSheetState, String>, SheetState {
  PENDING("待审批"),
  SUCCESS("审批完成"),
  FAILURE("审批失败");

  private final String value;

  WarehouseGivenSheetState(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
