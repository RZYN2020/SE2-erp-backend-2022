package com.nju.edu.erp.utils.sheet;

import com.nju.edu.erp.enums.sheetState.SheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import java.util.List;

public interface Sheet {

  /**
   * 制作表单并持久化
   * @param userVO
   * @param sheetVO
   */
  void makeSheet(UserVO userVO, SheetVO sheetVO);

  /**
   * 寻找某一状态的全部表单
   * @param state
   * @return SheetVO与SheetState表单类型相同
   */
  List<SheetVO> findSheetByState(SheetState state);

  /**
   * 将sheetId对应的表单审批为状态state, 如果不符合状态迁移规则则抛出异常
   * @param sheetId
   * @param state
   */
  void approval(String sheetId, SheetState state);

}
