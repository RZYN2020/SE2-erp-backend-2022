package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.service.EmployeeService;
import com.nju.edu.erp.web.Response;
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
    return Response.buildSuccess(employeeService.createEmployee(inputVO));
  }

  @GetMapping("/findAllEmployee")
  public Response findAll() {
    return Response.buildSuccess(employeeService.findAll());
  }

  @GetMapping("/findUser")
  public Response findUser(@RequestParam Integer id) {
    return Response.buildSuccess(employeeService.findUserByEmployeeId(id));
  }

  @GetMapping("/signIn")
  public Response signIn(@RequestParam String username) {
    employeeService.signIn(username);
    return Response.buildSuccess();
  }

  @GetMapping("/findAbsence")
  public Response findAbsence(@RequestParam String username) {
    return Response.buildSuccess(employeeService.findAbsence(username));
  }

}
