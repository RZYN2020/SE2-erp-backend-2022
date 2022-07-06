package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.SaleRecordVO;
import java.util.List;

public interface SaleDetailService {

    List<SaleRecordVO> findAllRecords();
}
