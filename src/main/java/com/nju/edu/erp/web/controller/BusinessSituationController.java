package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.model.vo.businessSituation.BusinessSituationVO;
import com.nju.edu.erp.service.BusinessSituationService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(path = "bsSheet")
public class BusinessSituationController {
    private final BusinessSituationService businessSituationService;

    @Autowired
    public BusinessSituationController(BusinessSituationService businessSituationService) {
        this.businessSituationService =businessSituationService;
    }

    /**
     * 查看经营情况表：一个时间段内的 折让后总收入 总支出 利润
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     * @throws ParseException
     */
    @GetMapping("/sheet-show")
    public Response getBusinessSituationByTime(@RequestParam String beginDateStr, @RequestParam String endDateStr) throws ParseException {
        // 如果开始时间比结束时间晚，businessSituationVO是null
        BusinessSituationVO businessSituationVO = businessSituationService.getBusinessSituationByTime(beginDateStr, endDateStr);
        return Response.buildSuccess(businessSituationVO);
    }
}
