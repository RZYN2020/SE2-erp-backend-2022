package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.businessSituation.BusinessSituationVO;

import java.text.ParseException;

public interface BusinessSituationService {

    /**
     * 查看经营情况表：一个时间段内的 折让后总收入 总支出 利润
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     * @throws ParseException
     */
    BusinessSituationVO getBusinessSituationByTime(String beginDateStr, String endDateStr);
}
