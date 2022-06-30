package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CreateCustomerVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nju.edu.erp.exception.MyServiceException;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * 根据id更新客户信息
     *
     * @param customerPO 客户信息
     */
    @Override
    public void updateCustomer(CustomerPO customerPO) {
        customerDao.updateOne(customerPO);
    }

    /**
     * 根据type查找对应类型的客户
     *
     * @param type 客户类型
     * @return 客户列表
     */
    @Override
    public List<CustomerPO> getCustomersByType(CustomerType type) {

        return customerDao.findAllByType(type);
    }

    @Override
    public CustomerPO findCustomerById(Integer supplier) {
        return customerDao.findOneById(supplier);
    }

    // TODO: impl two new method

    @Override
    public void deleteCustomerById(Integer id) {
        CustomerPO customerPOToDelete = customerDao.findOneById(id);
        if (customerPOToDelete == null) {
            throw new MyServiceException("C0000", "不存在该客户 删除失败！");
        }
        else {
            customerDao.deleteOneById(id);
        }
    }

    @Override
    public CustomerVO createCustomer(CreateCustomerVO inputVO) {
        // 仿照ProductServiceImpl.createProduct
        System.out.println("赵锁子");

        // 生成客户id
        int customerId = customerDao.findMaxCustomerId() + 1;

        CustomerPO savePO = new CustomerPO();
        BeanUtils.copyProperties(inputVO, savePO);
        savePO.setId(customerId);
        customerDao.createCustomer(savePO);

        CustomerPO responsePO = customerDao.findOneById(customerId);
        CustomerVO ans = new CustomerVO();
        BeanUtils.copyProperties(responsePO, ans);

        return ans;
    }

}
