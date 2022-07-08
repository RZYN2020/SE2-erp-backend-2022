package com.nju.edu.erp.model.vo.warehouse;

import com.nju.edu.erp.enums.sheetState.WarehouseGivenSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseGivenSheetVO implements SheetVO {
  private String id;
  private String operator;
  private String saleSheetId;
  private Date create_time;
  private WarehouseGivenSheetState state;
  private List<WarehouseGivenSheetContentVO> products;
}
