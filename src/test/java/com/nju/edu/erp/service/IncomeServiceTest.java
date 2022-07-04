package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.IncomeSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.IncomeSheetContentPO;
import com.nju.edu.erp.model.po.IncomeSheetPO;
import com.nju.edu.erp.model.po.SaleSheetContentPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetContentVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import com.nju.edu.erp.utils.IdGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class IncomeServiceTest {

  @Autowired
  IncomeService incomeService;

  @Autowired
  IncomeSheetDao incomeSheetDao;

  @Test
  @Transactional
  @Rollback
  public void createTest() {
    UserVO userVO = UserVO.builder()
        .name("赵勇臻")
        .role(Role.FINANCIAL_STAFF)
        .build();

    List<IncomeSheetContentVO> vos = new ArrayList<>();
    vos.add(IncomeSheetContentVO.builder()
    .account("bank-001-000")
    .amount(BigDecimal.valueOf(1000)).remark("item 1")
    .build());

    vos.add(IncomeSheetContentVO.builder()
        .account("bank-001-001")
        .amount(BigDecimal.valueOf(10000)).remark("item 2")
        .build());
    IncomeSheetVO incomeSheetVO = IncomeSheetVO.builder()
        .income_sheet_content(vos)
        .customer_id(2)
        .build();
    IncomeSheetPO prevSheet = incomeSheetDao.getLatest();
    String realSheetId = IdGenerator
        .generateSheetId(prevSheet == null ? null : prevSheet.getId(), "SKD");
    incomeService.makeIncomeSheet(userVO, incomeSheetVO);


    IncomeSheetPO latestSheet = incomeSheetDao.getLatest();
    Assertions.assertNotNull(latestSheet);
    Assertions.assertEquals(realSheetId, latestSheet.getId());
    Assertions.assertEquals(0, latestSheet.getTotal_amount().compareTo(BigDecimal.valueOf(11000.00)));
    Assertions.assertEquals(IncomeSheetState.PENDING_LEVEL_1, latestSheet.getState());

    String sheetId = latestSheet.getId();
    Assertions.assertNotNull(sheetId);
    List<IncomeSheetContentPO> content = incomeSheetDao.findContentBySheetId(sheetId);
    Assertions.assertEquals(2, content.size());
  }

}
