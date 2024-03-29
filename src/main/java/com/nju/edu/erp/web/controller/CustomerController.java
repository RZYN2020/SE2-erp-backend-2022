package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CreateCustomerVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/findByType")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @PostMapping("/create")
    public Response createCustomer(@RequestBody CreateCustomerVO inputVO) {
        return Response.buildSuccess(customerService.createCustomer(inputVO));
    }

    @PostMapping("/update")
    public Response updateCustomer(@RequestBody CustomerVO customerVO) {
        CustomerPO customerPO = new CustomerPO();
        BeanUtils.copyProperties(customerVO, customerPO);
        customerService.updateCustomer(customerPO);
        return Response.buildSuccess();
    }

    @GetMapping("/delete")
    public Response deleteCustomerById(@RequestParam(value = "id") int id) {
        customerService.deleteCustomerById(id);
        return Response.buildSuccess();
    }

}
