package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.UserVO;

public interface EmployeeService {

  /**
   * 新增一个员工, 并创建相应的账号
   * @param inputVO
   * @return
   */
  EmployeeVO createEmployee(EmployeeVO inputVO);

  /**
   * 查询所有员工信息
   * @return
   */
  List<EmploeeVO> findAll();

  /**
   * 查找EmployeeId对应的User
   * @param id
   * @return
   */
  UserVO findUserByEmployeeId(Integer id);
}
