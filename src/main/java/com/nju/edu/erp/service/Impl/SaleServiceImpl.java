package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CouponDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetContentVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGivenSheetContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGivenSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseInputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseInputFormVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.WarehouseGivenService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import com.nju.edu.erp.utils.promotion.PromotionCtl;
import com.nju.edu.erp.utils.promotion.PromotionInfo;
import com.nju.edu.erp.utils.promotion.PromotionStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final ProductService productService;

    private final WarehouseDao warehouseDao;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final CouponDao couponDao;

    private final WarehouseGivenService warehouseGivenService;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, CustomerDao customerDao,
        ProductService productService, CustomerService customerService,
        WarehouseDao warehouseDao, WarehouseService warehouseService,
        CouponDao couponDao, WarehouseGivenService warehouseGivenService) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.warehouseDao = warehouseDao;
        this.couponDao = couponDao;
        this.warehouseGivenService = warehouseGivenService;
    }

    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO) {
        // 需要持久化销售单（SaleSheet）和销售单content（SaleSheetContent），其中总价或者折后价格的计算需要在后端进行
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO, saleSheetPO);
        // 此处根据制定单据人员确定操作员
        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreate_time(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> pContentPOList = new ArrayList<>();
        for(SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()) {
            SaleSheetContentPO pContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content,pContentPO);
            pContentPO.setSaleSheetId(id);
            BigDecimal unitPrice = pContentPO.getUnitPrice();
            if(unitPrice == null) {
                ProductPO product = productDao.findById(content.getPid());
                unitPrice = product.getPurchasePrice();
                pContentPO.setUnitPrice(unitPrice);
            }
            pContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(pContentPO.getQuantity())));
            pContentPOList.add(pContentPO);
            totalAmount = totalAmount.add(pContentPO.getTotalPrice());
        }
        saleSheetDao.saveBatchSheetContent(pContentPOList);
        saleSheetPO.setRawTotalAmount(totalAmount);

        CustomerPO customerPO = customerDao.findOneById(saleSheetPO.getSupplier());
        BigDecimal discount = BigDecimal.ONE;
        BigDecimal voucher_amount = BigDecimal.ZERO;
        if (saleSheetVO.getVoucherAmount() != null) voucher_amount = voucher_amount.add(saleSheetVO.getVoucherAmount());
        WarehouseGivenSheetVO warehouseGivenSheetVO = new WarehouseGivenSheetVO();
        warehouseGivenSheetVO.setSaleSheetId(saleSheetPO.getId());
        List<WarehouseGivenSheetContentVO> contentVOS = new ArrayList<>();
        for (PromotionStrategy strategy: PromotionCtl.strategyList) {
            if (strategy.checkEffect(customerPO, saleSheetVO.getSaleSheetContent(), new Date())) {
                PromotionInfo info = strategy.taskEffect();
                if (info.getDiscount() != null) discount = info.getDiscount();
                if (info.getVoucher_amount() != null) voucher_amount = voucher_amount.add(info.getVoucher_amount());
                if (info.getCoupon() != null) couponDao.addOne(customerPO.getId(), info.getCoupon());
                if (info.getPid() != null) {
                    WarehouseGivenSheetContentVO vo = new WarehouseGivenSheetContentVO();
                    vo.setPid(info.getPid());
                    vo.setAmount(info.getAmount());
                    contentVOS.add(vo);
                }
            }
        }
        warehouseGivenSheetVO.setProducts(contentVOS);
        warehouseGivenService.makeSheet(userVO, warehouseGivenSheetVO);

        BigDecimal finalAmount = totalAmount.multiply(discount).subtract(voucher_amount);
        saleSheetPO.setDiscount(discount);
        saleSheetPO.setVoucherAmount(voucher_amount);
        saleSheetPO.setFinalAmount(finalAmount);
        saleSheetDao.saveSheet(saleSheetPO);
    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        // TODO
        // 根据单据状态获取销售单（注意：VO包含SaleSheetContent）
        // 依赖的dao层部分方法未提供，需要自己实现
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if(state == null) {
            all = saleSheetDao.findAllSheet();
        } else {
            all = saleSheetDao.findAllByState(state);
        }
        for(SaleSheetPO po: all) {
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleSheetContentPO> all_saleContent = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for (SaleSheetContentPO p : all_saleContent) {
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * 根据销售单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleSheetId 销售单id
     * @param state       销售单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        /* 一些注意点：
            1. 二级审批成功之后需要进行
                 1. 修改单据状态
                 2. 更新商品表
                 3. 更新客户表
                 4. 新建出库草稿
            2. 一级审批状态不能直接到审批完成状态； 二级审批状态不能回到一级审批状态
         */
        if(state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if(saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleSheetState prevState;
            if(state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if(state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            if(state.equals(SaleSheetState.SUCCESS)) {
                List<SaleSheetContentPO> saleSheetContent =  saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> wareOut_list = new ArrayList<>();

                for(SaleSheetContentPO content : saleSheetContent) {
                    //修改商品数量
                    List<WarehouseOutputFormContentVO> temp_list = feedSales(content.getPid(), content.getUnitPrice(), content.getQuantity());
                    if (temp_list.isEmpty()) {
                        saleSheetDao.updateSheetState(saleSheetId, SaleSheetState.FAILURE);
                        throw new RuntimeException("库存数量不足！审批失败！");
                    }
                    wareOut_list.addAll(temp_list);
                }
                // 更新客户表(更新应收字段)
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                customerService.updateCustomer(customerPO);

                // 制定出库单草稿(在这里关联进货单)
                // 调用创建出库单的方法
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null); // 暂时不填操作人(确认草稿单的时候填写)
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(wareOut_list);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);

                //修改时间
                saleSheetDao.updateDate(saleSheetId, new Date());

                //赠品、赠送代金券生效
                WarehouseGivenSheetVO warehouseGivenSheetVO = new WarehouseGivenSheetVO();
                warehouseGivenSheetVO.setSaleSheetId(saleSheet.getId());
                List<WarehouseGivenSheetContentVO> contentVOS = new ArrayList<>();
                for (PromotionStrategy strategy: PromotionCtl.strategyList) {
                    List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
                    List<SaleSheetContentPO> saleSheetContentPOS = saleSheetDao.findContentBySheetId(saleSheetId);
                    for (SaleSheetContentPO contentPO : saleSheetContentPOS) {
                        SaleSheetContentVO contentVO = new SaleSheetContentVO();
                        BeanUtils.copyProperties(contentPO, contentVO);
                        saleSheetContentVOList.add(contentVO);
                    }
                    if (strategy.checkEffect(customerPO, saleSheetContentVOList, new Date())) {
                        PromotionInfo info = strategy.taskEffect();
                        if (info.getCoupon() != null) couponDao.addOne(customerPO.getId(), info.getCoupon());
                        if (info.getPid() != null) {
                            WarehouseGivenSheetContentVO vo = new WarehouseGivenSheetContentVO();
                            vo.setPid(info.getPid());
                            vo.setAmount(info.getAmount());
                            contentVOS.add(vo);
                        }
                    }
                }
                warehouseGivenSheetVO.setProducts(contentVOS);
                UserVO userVO = new UserVO();
                userVO.setName(saleSheet.getOperator());
                warehouseGivenService.makeSheet(userVO, warehouseGivenSheetVO);
            }
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0) {
                return null;
            } else {
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman,beginTime,endTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据销售单Id搜索销售单信息
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if(saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content:
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }

    /**
     * 满足针对一个商品的销售请求
     * @param pid
     * @param unitPrice
     * @param amount
     * @return
     */
    private List<WarehouseOutputFormContentVO> feedSales(String pid, BigDecimal unitPrice, Integer amount) {
        //当前的策略是进价低的优先出库
        List<WarehouseOutputFormContentVO> outForm_list = new ArrayList<>();

        List<WarehousePO> stocks = warehouseDao.findByPidOrderByPurchasePricePos(pid);
        int capacity = 0;
        for (WarehousePO stock : stocks) {
            capacity += stock.getQuantity();
        }
        if (capacity < amount) return outForm_list;

        for (WarehousePO stock : stocks) {
            int consume_amount = Math.min(amount, stock.getQuantity());
            stock.setQuantity(stock.getQuantity() - consume_amount);
            warehouseDao.deductQuantity(stock);
            amount -= consume_amount;

            WarehouseOutputFormContentVO woContentVO = new WarehouseOutputFormContentVO();
            woContentVO.setSalePrice(unitPrice);
            woContentVO.setQuantity(consume_amount);
            woContentVO.setPid(pid);
            woContentVO.setBatchId(stock.getBatchId());
            outForm_list.add(woContentVO);

            assert amount >= 0;
            if (amount == 0) break;
        }

        ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
        productInfoVO.setQuantity(productInfoVO.getQuantity() - amount);
        productInfoVO.setRecentRp(unitPrice);
        productService.updateProduct(productInfoVO);

        return outForm_list;
    }
}
