package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CreateCustomerVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.EmployeeService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

  private final EmployeeService employeeService;
  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping("/createEmployee")
  public Response createEmployee(@RequestBody EmployeeVO inputVO) {

  }

  @GetMapping("/findAllEmployee")
  public Response findAll() {

  }

  @GetMapping("/findUser")
  public Response findUser(@RequestParam Integer id) {

  }

  @GetMapping("/signIn")
  public Response signIn(@RequestParam Integer username) {

  }

  @GetMapping("/findAbsence")
  public Response findAbsence(@RequestParam Integer username) {

  }

}
