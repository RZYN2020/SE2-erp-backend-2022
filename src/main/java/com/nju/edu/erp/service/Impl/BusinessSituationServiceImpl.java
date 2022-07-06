package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.sheetState.SalaryGrantSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.po.SalaryGrantSheetPO;
import com.nju.edu.erp.model.po.SaleReturnSheetPO;
import com.nju.edu.erp.model.vo.businessSituation.BusinessIncomeVO;
import com.nju.edu.erp.model.vo.businessSituation.BusinessOutcomeVO;
import com.nju.edu.erp.model.vo.businessSituation.BusinessSituationVO;
import com.nju.edu.erp.service.BusinessSituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BusinessSituationServiceImpl implements BusinessSituationService {

    private final PurchaseSheetDao purchaseSheetDao;
    private final PurchaseReturnsSheetDao purchaseReturnsSheetDao;
    private final SaleSheetDao saleSheetDao;
    private final SaleReturnSheetDao saleReturnSheetDao;
    private final SalaryGrantSheetDao salaryGrantSheetDao;

    @Autowired
    public BusinessSituationServiceImpl(PurchaseSheetDao purchaseSheetDao, PurchaseReturnsSheetDao purchaseReturnsSheetDao, SaleSheetDao saleSheetDao, SaleReturnSheetDao saleReturnSheetDao, SalaryGrantSheetDao salaryGrantSheetDao) {
        this.purchaseSheetDao = purchaseSheetDao;
        this.purchaseReturnsSheetDao = purchaseReturnsSheetDao;
        this.saleSheetDao = saleSheetDao;
        this.saleReturnSheetDao = saleReturnSheetDao;
        this.salaryGrantSheetDao = salaryGrantSheetDao;
    }

    /**
     * 查看经营情况表：一个时间段内的 折让后总收入 总支出 利润
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return 经营情况 BusinessSituationVO
     */
    @Override
    public BusinessSituationVO getBusinessSituationByTime(String beginDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginDate = sdf.parse(beginDateStr);
            Date endDate = sdf.parse(endDateStr);
            if (beginDate.compareTo(endDate) < 0) {
                BusinessIncomeVO businessIncomeVO = new BusinessIncomeVO();
                // 收入信息：折让后总收入，折扣（0.84 0.93这样），销售收入，进货退货收入

                BigDecimal rawSaleIncome = saleSheetDao.getTotalRawAmountByTime(beginDate, endDate);
                if (rawSaleIncome == null) rawSaleIncome = new BigDecimal(0);
                BigDecimal finalSaleIncome = saleSheetDao.getTotalFinalAmountByTime(beginDate, endDate); //销售收入
                if (finalSaleIncome == null) finalSaleIncome = new BigDecimal(0);
                // 考虑除零异常, 回来做
                double discount;
                if (finalSaleIncome.intValue() == 0) discount = 0;
                else discount = finalSaleIncome.divide(rawSaleIncome, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue(); // 折扣
                BigDecimal purchaseReturnsIncome = purchaseReturnsSheetDao.getTotalAmountByTime(beginDate, endDate); //进货退货收入
                if (purchaseReturnsIncome == null) purchaseReturnsIncome = new BigDecimal(0);
                BigDecimal incomeAfterDiscount = finalSaleIncome.add(purchaseReturnsIncome);

                businessIncomeVO.setIncomeAfterDiscount(incomeAfterDiscount);
                businessIncomeVO.setDiscount(discount);
                businessIncomeVO.setSaleIncome(finalSaleIncome);
                businessIncomeVO.setPurchaseReturnsIncome(purchaseReturnsIncome);


                BusinessOutcomeVO businessOutcomeVO = new BusinessOutcomeVO();
                // 支出信息：总支出，销售退货支出，进货支出，人力成本（工资）

                String[] beginArray = beginDateStr.split("-");
                String begin = beginArray[0] + beginArray[1] + beginArray[2].substring(0, 2);
                String[] endArray = endDateStr.split("-");
                String end = endArray[0] + endArray[1] + endArray[2].substring(0, 2);

                // 销售退货支出
                BigDecimal saleReturnOutcome = new BigDecimal(0);
                List<SaleReturnSheetPO> saleReturnSheetPOList = saleReturnSheetDao.findAllByState(SaleReturnSheetState.SUCCESS);
                for (SaleReturnSheetPO saleReturnSheetPO : saleReturnSheetPOList) {
                    String createDate = saleReturnSheetPO.getId().substring(6, 14);
                    if (begin.compareTo(createDate) <= 0 && end.compareTo(createDate) >= 0) {
                        saleReturnOutcome = saleReturnOutcome.add(saleReturnSheetPO.getTotalAmount());
                    }
                }

                BigDecimal purchaseOutcome = purchaseSheetDao.getTotalAmountByTime(beginDate, endDate);
                if (purchaseOutcome == null) purchaseOutcome = new BigDecimal(0);

                // 人力成本（工资）
                BigDecimal salary = new BigDecimal(0);
                List<SalaryGrantSheetPO> salaryGrantSheetPOList = salaryGrantSheetDao.findAllByState(SalaryGrantSheetState.SUCCESS);
                for (SalaryGrantSheetPO salaryGrantSheetPO : salaryGrantSheetPOList) {
                    String createDate = salaryGrantSheetPO.getId().substring(6, 14);
                    if (begin.compareTo(createDate) <= 0 && end.compareTo(createDate) >= 0) {
                        salary = salary.add(salaryGrantSheetPO.getRealSalary());
                    }
                }

                BigDecimal outcome = saleReturnOutcome.add(purchaseOutcome).add(salary); // 总支出

                businessOutcomeVO.setSaleReturnOutcome(saleReturnOutcome);
                businessOutcomeVO.setPurchaseOutcome(purchaseOutcome);
                businessOutcomeVO.setSalary(salary);
                businessOutcomeVO.setOutcome(outcome);

                BusinessSituationVO ans = BusinessSituationVO.builder()
                        .businessIncomeVO(businessIncomeVO)
                        .businessOutcomeVO(businessOutcomeVO)
                        .profit(incomeAfterDiscount.subtract(outcome))
                        .build();
                return ans;
            }
            else return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
