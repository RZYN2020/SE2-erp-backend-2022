# ERP系统测试文档

## 1. 引言

> 最后一次修改: 杨峥
>
> 最后一次修改日期: 2022/7/9

| 修改人员 | 日期       | 修改原因                      | 版本号 |
| -------- | ---------- | ----------------------------- | ------ |
| 杨峥     | 2022/07/08 | 后端全部单元测试              | 1.0    |
| 何浩达   | 2022/07/09 | 集成测试-api测试-财务模块     | 1.1    |
| 赵勇臻   | 2022/07/09 | 集成测试-api测试-促销模块     | 1.3    |
| 杨峥     | 2022/07/09 | 集成测试-api测试-人力资源模块 | 1.4    |
| 赵勇臻   | 2022/07/09 | 系统测试                      | 2.0    |

## 2. 单元测试

> 最后一次修改: 杨峥
>
> 最后一次修改日期: 2022/7/8

**所有后端单元测试均已通过测试**

![后端单元测试](https://raw.githubusercontent.com/heiyan-2020/SE2/master/%E5%90%8E%E7%AB%AF%E5%8D%95%E5%85%83%E6%B5%8B%E8%AF%95.png)

### 2.1 财务模块

#### 2.1.1 账户管理

```java
package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class BankAccountTest {

    @Autowired
    BankAccountService bankAccountService;

    @Test
    @Transactional
    @Rollback
    public void AllTest() {
        BankAccountVO bankAccountVO1 = BankAccountVO.builder()
                .accountName("Horizon")
                .amount(7)
                .build();
        BankAccountVO bankAccountVO2 = BankAccountVO.builder()
                .accountName("Seer")
                .amount(10)
                .build();
        bankAccountService.createBankAccount(bankAccountVO1);
        bankAccountService.createBankAccount(bankAccountVO2);
        List<BankAccountVO> list = bankAccountService.findAll();
        Assertions.assertEquals("Horizon", list.get(list.size() - 2).getAccountName());
        Assertions.assertEquals(7, list.get(list.size() - 2).getAmount());
        Assertions.assertEquals("Seer", list.get(list.size() - 1).getAccountName());
        Assertions.assertEquals(10, list.get(list.size() - 1).getAmount());
        bankAccountService.delete("Seer");
        list = bankAccountService.findAll();
        Assertions.assertEquals("Horizon", list.get(list.size() - 1).getAccountName());
        Assertions.assertEquals(7, list.get(list.size() - 1).getAmount());
        System.out.println("Horizon");
    }


}
```

#### 2.1.2 制定收款单

```java
package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.IncomeSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.IncomeSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.IncomeSheetContentPO;
import com.nju.edu.erp.model.po.IncomeSheetPO;
import com.nju.edu.erp.model.po.SaleSheetContentPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetContentVO;
import com.nju.edu.erp.model.vo.income.IncomeSheetVO;
import com.nju.edu.erp.utils.IdGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class IncomeServiceTest {

  @Autowired
  IncomeService incomeService;

  @Autowired
  IncomeSheetDao incomeSheetDao;

  @Test
  @Transactional
  @Rollback
  public void createTest() {
    UserVO userVO = UserVO.builder()
        .name("赵勇臻")
        .role(Role.FINANCIAL_STAFF)
        .build();

    List<IncomeSheetContentVO> vos = new ArrayList<>();
    vos.add(IncomeSheetContentVO.builder()
    .account("bank-001-000")
    .amount(BigDecimal.valueOf(1000)).remark("item 1")
    .build());

    vos.add(IncomeSheetContentVO.builder()
        .account("bank-001-001")
        .amount(BigDecimal.valueOf(10000)).remark("item 2")
        .build());
    IncomeSheetVO incomeSheetVO = IncomeSheetVO.builder()
        .income_sheet_content(vos)
        .customer_id(2)
        .build();
    IncomeSheetPO prevSheet = incomeSheetDao.getLatest();
    String realSheetId = IdGenerator
        .generateSheetId(prevSheet == null ? null : prevSheet.getId(), "SKD");
    incomeService.makeIncomeSheet(userVO, incomeSheetVO);


    IncomeSheetPO latestSheet = incomeSheetDao.getLatest();
    Assertions.assertNotNull(latestSheet);
    Assertions.assertEquals(realSheetId, latestSheet.getId());
    Assertions.assertEquals(0, latestSheet.getTotal_amount().compareTo(BigDecimal.valueOf(11000.00)));
    Assertions.assertEquals(IncomeSheetState.PENDING, latestSheet.getState());

    String sheetId = latestSheet.getId();
    Assertions.assertNotNull(sheetId);
    List<IncomeSheetContentPO> content = incomeSheetDao.findContentBySheetId(sheetId);
    Assertions.assertEquals(2, content.size());
  }

}
```

#### 2.1.3 查看经营情况表

```java
package com.nju.edu.erp.service;


import com.nju.edu.erp.model.vo.businessSituation.BusinessSituationVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
public class BusinessSituationServiceTest {

    @Autowired
    BusinessSituationService businessSituationService;

    @Test
    @Transactional
    @Rollback
    public void AllTest() {
        BusinessSituationVO vo = businessSituationService.getBusinessSituationByTime("2022-01-12 11:38:30", "2022-12-12 11:38:30");
        Assertions.assertEquals(0, new BigDecimal(5610400).compareTo(vo.getProfit()));
    }
}
```

#### 2.1.4 期初建账

```java
package com.nju.edu.erp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class OpeningAccountsTest {

    @Autowired
    OpeningAccountsService openingAccountsService;

    @Test
    @Transactional
    @Rollback
    public void Test1() {
        openingAccountsService.find("2022-07");
    }
}
```

### 2.2 人力资源模块

#### 2.2.1 员工管理

```java
package com.nju.edu.erp.service;


import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    @Test
    @Transactional
    @Rollback
    public void createTest() {
        EmployeeVO employeeVO = EmployeeVO.builder()
                .name("哈队")
                .gender("男")
                .birthDate(new Date(1647258440))
                .phoneNumber("123456")
                .job("SALE_MANAGER")
                .jobLevel(2)
                .account("1248616394")
                .build();
        employeeService.createEmployee(employeeVO);
        List<EmployeeVO> list = employeeService.findAll();
        EmployeeVO target = list.get(list.size() - 1);
        Assertions.assertEquals(employeeVO.getName(), target.getName());
        Assertions.assertEquals(employeeVO.getGender(), target.getGender());
        Assertions.assertEquals(employeeVO.getBirthDate().toString().compareTo(target.getBirthDate().toString()), 0);
        Assertions.assertEquals(employeeVO.getPhoneNumber(), target.getPhoneNumber());
        Assertions.assertEquals(employeeVO.getJob(), target.getJob());
        Assertions.assertEquals(employeeVO.getJobLevel(), target.getJobLevel());
        Assertions.assertEquals(employeeVO.getAccount(), target.getAccount());

        UserVO userVO = employeeService.findUserByEmployeeId(10);
        Assertions.assertEquals("哈队", userVO.getName());
        Assertions.assertEquals(Role.SALE_MANAGER, userVO.getRole());
        Assertions.assertEquals("123456", userVO.getPassword());
    }

    @Test
    @Transactional
    @Rollback
    public void signInTest() {
        employeeService.signIn("lock");
        EmployeePO lockson = employeeService.findOneById(1);
        Assertions.assertEquals(2, lockson.getSignTimes());
    }

    @Test
    @Transactional
    @Rollback
    public void findAbsenceTest() {
        Date now = new Date();
        Assertions.assertEquals(Integer.parseInt(now.toString().split(" ")[2]) - 1, employeeService.findAbsence("lock"));
    }

}
```

#### 2.2.2 岗位管理

```java
package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.PaymentMethod;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.JobPO;
import com.nju.edu.erp.model.vo.JobVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class JobServiceTest {

    @Autowired
    JobService jobService;

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        List<JobVO> list = jobService.findAll();
        JobVO random = list.get(4);
        Assertions.assertEquals(Role.HR.name(), random.getName());
        Assertions.assertEquals(0, new BigDecimal(2000).compareTo(random.getBasicSalary()));
        Assertions.assertEquals(0, new BigDecimal(3000).compareTo(random.getJobSalary()));
        Assertions.assertEquals(2, random.getJobLevel());
        Assertions.assertEquals(1, random.getCalculateMethod());
        Assertions.assertEquals(PaymentMethod.Monthly, random.getPaymentMethod());
    }

    @Test
    @Transactional
    @Rollback
    public void updateJobTest() {
        JobVO jobVO = jobService.findAll().get(0);
        JobPO jobPO = new JobPO();
        BeanUtils.copyProperties(jobVO, jobPO);
        jobPO.setJobSalary(new BigDecimal(20000));
        jobPO.setPaymentMethod(PaymentMethod.Yearly);
        jobService.updateJob(jobPO);
        jobVO = jobService.findAll().get(0);
        Assertions.assertEquals(0, jobVO.getJobSalary().compareTo(new BigDecimal(20000)));
        Assertions.assertEquals(PaymentMethod.Yearly, jobVO.getPaymentMethod());
    }

    @Test
    @Transactional
    @Rollback
    public void findAllCalculateMethodTest() {
        List<String> list = jobService.findAllCalculateMethod();
        Assertions.assertEquals("基本工资 + 岗位工资 - 税款", list.get(0));
        Assertions.assertEquals("基本工资 + 提成 + 岗位工资 - 税款", list.get(1));
    }

    @Test
    @Transactional
    @Rollback
    public void findAllPaymentMethodTest() {
        List<PaymentMethod> list = jobService.findAllPaymentMethod();
        Assertions.assertEquals(PaymentMethod.Monthly, list.get(0));
        Assertions.assertEquals(PaymentMethod.Yearly, list.get(1));
    }
}
```

#### 2.2.3 制定工资单

```java
package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.Salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.utils.sheet.SalarySheet;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SalaryTests {
  @Autowired
  SalaryService salaryService;

  @Autowired
  EmployeeService employeeService;

  @Autowired
  SalarySheetDao salarySheetDao;

  @Test
  @Transactional
  @Rollback(value = true)
  public void createTest() {
    UserVO userVO = UserVO.builder()
        .name("hale zhao")
        .role(Role.HR)
        .build();

    SalarySheetVO salarySheetVO = SalarySheetVO.builder()
        .employee_name("DTA")
        .employee_id(1)
        .operator("simimasai")
        .build();
    salaryService.makeSalarySheet(userVO, salarySheetVO);
    SalarySheetPO salarySheet = salarySheetDao.getLatest();
  }

}
```

#### 2.2.4 发放年终奖

```java
package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.YearEndAwardsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class YearEndAwardsTest {
    @Autowired
    YearEndAwardsService yearEndAwardsService;

    @Test
    @Transactional
    @Rollback
    public void AllTest() {
        List<YearEndAwardsVO> list = yearEndAwardsService.findAllYearEndSalary();
        yearEndAwardsService.establishYearEndAwards(1,new BigDecimal(1500));
        list = yearEndAwardsService.findAllYearEndSalary();
        Assertions.assertEquals("一级库存管理人员A", list.get(0).getEmployeeName());
        Assertions.assertEquals(0, new BigDecimal(1500).compareTo(list.get(0).getYearEndAwards()));
    }
}
```

## 3. 集成测试

### 3.1 API测试

数据库此时已经过配置填充。

#### 3.1.1 API测试/总经理模块

> 最后一次修改: 赵勇臻
>
> 最后一次修改日期: 2022/07/09

##### GET 获取所有用户促销策略

GET /promotion/user/show-all

获取所有针对不同用户的促销策略

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "effect_level": 50,
            "product_id": "0000000000400001",
            "product_amount": 2,
            "coupon": 500,
            "discount": 0.3,
            "begin_date": "2022-07-25T16:00:00.000+00:00",
            "end_date": "2022-07-27T16:00:00.000+00:00"
        },
        {
            "effect_level": 3,
            "product_id": "0000000000400001",
            "product_amount": 2,
            "coupon": 500,
            "discount": 0.3,
            "begin_date": "2022-07-25T16:00:00.000+00:00",
            "end_date": "2022-07-27T16:00:00.000+00:00"
        }
    ]
}
```



###### 预期结果

同上

##### GET 获取所有总价促销策略

GET /promotion/price/show-all

获取所有针对总价的促销策略

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "effect_price": 50,
            "product_id": "0000000000500002",
            "product_amount": null,
            "coupon": 50,
            "begin_date": "2022-07-04T16:00:00.000+00:00",
            "end_date": "2022-07-21T16:00:00.000+00:00"
        },
        {
            "effect_price": 500,
            "product_id": "0000000000400001",
            "product_amount": 60,
            "coupon": 500,
            "begin_date": "2022-06-30T16:00:00.000+00:00",
            "end_date": "2022-07-02T16:00:00.000+00:00"
        }
    ]
}
```



###### 预期结果

同上

##### GET 获取所有特价包

GET /promotion/package/show-all

获取所有特价包

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "product_id": [
                "0000000000400000",
                "0000000000400001"
            ],
            "product_amount": [
                1,
                1
            ],
            "voucher_amount": 1000,
            "begin_date": "2022-06-30T16:00:00.000+00:00",
            "end_date": "2022-07-07T16:00:00.000+00:00"
        },
        {
            "product_id": [
                "0000000000400001"
            ],
            "product_amount": [
                10
            ],
            "voucher_amount": 20,
            "begin_date": "2022-07-05T16:00:00.000+00:00",
            "end_date": "2022-07-21T16:00:00.000+00:00"
        },
        {
            "product_id": [
                "0000000000400000"
            ],
            "product_amount": [
                550
            ],
            "voucher_amount": 50,
            "begin_date": "2022-07-05T16:00:00.000+00:00",
            "end_date": "2022-07-22T16:00:00.000+00:00"
        },
        {
            "product_id": [
                "0000000000500001"
            ],
            "product_amount": [
                40404
            ],
            "voucher_amount": 50000,
            "begin_date": "2022-07-26T16:00:00.000+00:00",
            "end_date": "2022-07-29T16:00:00.000+00:00"
        },
        {
            "product_id": [
                "0000000000500001"
            ],
            "product_amount": [
                40404
            ],
            "voucher_amount": 50000,
            "begin_date": "2022-07-26T16:00:00.000+00:00",
            "end_date": "2022-07-29T16:00:00.000+00:00"
        }
    ]
}
```



###### 预期结果

同上

##### GET 显示所有代金券

GET /promotion/coupon/show

显示所有代金券

###### 请求参数

| 名称          | 位置   | 类型    | 必选 | 说明 |
| ------------- | ------ | ------- | ---- | ---- |
| customer_id   | query  | integer | 是   | none |
| Authorization | header | string  | 是   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "id": 2,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 3,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 4,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 8,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 9,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 10,
            "customer_id": 3,
            "amount": 10
        },
        {
            "id": 11,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 12,
            "customer_id": 3,
            "amount": 10
        },
        {
            "id": 13,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 14,
            "customer_id": 3,
            "amount": 10
        },
        {
            "id": 15,
            "customer_id": 3,
            "amount": 1000
        },
        {
            "id": 16,
            "customer_id": 3,
            "amount": 10
        }
    ]
}
```



###### 预期结果

同上

##### POST 创建用户促销策略

POST /promotion/user/create

创建针对不同用户的促销策略

> Body 请求参数

```json
{
    "effect_level": "3",
    "product_id": "0000000000400001",
    "product_amount": "2",
    "discount": "0.3",
    "coupon": "500",
    "begin_date": "2022-07-25T16:00:00.000Z",
    "end_date": "2022-07-27T16:00:00.000Z"
}
```

###### 请求参数

| 名称             | 位置   | 类型   | 必选 | 说明 |
| ---------------- | ------ | ------ | ---- | ---- |
| Authorization    | header | string | 否   | none |
| body             | body   | object | 否   | none |
| » effect_level   | body   | string | 是   | none |
| » product_id     | body   | string | 是   | none |
| » product_amount | body   | string | 是   | none |
| » discount       | body   | string | 是   | none |
| » coupon         | body   | string | 是   | none |
| » begin_date     | body   | string | 是   | none |
| » end_date       | body   | string | 是   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

###### 预期结果

同上

#### 3.1.2 API测试/财务模块

> 最后一次修改: 何浩达
>
> 最后一次修改日期: 2022/07/09

##### GET 显示所有账户

GET /account/findAll

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "accountName": "a652",
            "amount": 326471
        },
        {
            "accountName": "HELLO",
            "amount": 600
        },
        {
            "accountName": "hhd",
            "amount": 100000
        }
    ]
}
```

###### 预期结果

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "accountName": "a652",
            "amount": 326471
        },
        {
            "accountName": "HELLO",
            "amount": 600
        },
        {
            "accountName": "hhd",
            "amount": 100000
        }
    ]
}
```

##### POST 创建新的账户

POST /account/create

> Body 请求参数

```json
{
  "accountName": "hhd",
  "amount": "100000"
}
```

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |
| body          | body   | object | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": {
        "accountName": "hhd",
        "amount": 100000
    }
}
```

###### 预期结果

```json
{
    "code": "00000",
    "msg": "Success",
    "result": {
        "accountName": "hhd",
        "amount": 100000
    }
}
```

##### GET 收款单查询测试

GET /incomeSheet/sheet-show

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "id": "SKD-20220704-00000",
            "customer_id": 2,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 38,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 39,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 40,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 41,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 42,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 43,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 44,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                },
                {
                    "id": 45,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T14:37:13.000+00:00"
        },
        {
            "id": "SKD-20220704-00001",
            "customer_id": 1,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 46,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T14:40:38.000+00:00"
        },
        {
            "id": "SKD-20220704-00002",
            "customer_id": 2,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 47,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 11,
                    "remark": "1"
                }
            ],
            "total_amount": 11,
            "state": "待审批",
            "create_time": "2022-07-04T14:47:47.000+00:00"
        },
        {
            "id": "SKD-20220704-00003",
            "customer_id": 2,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 48,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T14:48:09.000+00:00"
        },
        {
            "id": "SKD-20220704-00004",
            "customer_id": 1,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 49,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T14:53:32.000+00:00"
        },
        {
            "id": "SKD-20220704-00005",
            "customer_id": 2,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 50,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T14:57:36.000+00:00"
        },
        {
            "id": "SKD-20220704-00006",
            "customer_id": 1,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 51,
                    "income_sheetId": null,
                    "account": "2",
                    "amount": 2,
                    "remark": "1"
                }
            ],
            "total_amount": 2,
            "state": "待审批",
            "create_time": "2022-07-04T14:58:27.000+00:00"
        },
        {
            "id": "SKD-20220704-00007",
            "customer_id": 1,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 52,
                    "income_sheetId": null,
                    "account": "1",
                    "amount": 1,
                    "remark": "1"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T15:07:28.000+00:00"
        },
        {
            "id": "SKD-20220704-00008",
            "customer_id": 1,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 53,
                    "income_sheetId": null,
                    "account": null,
                    "amount": 10000,
                    "remark": "第一条"
                },
                {
                    "id": 54,
                    "income_sheetId": null,
                    "account": null,
                    "amount": 1000000,
                    "remark": "第二条"
                }
            ],
            "total_amount": 1010000,
            "state": "待审批",
            "create_time": "2022-07-04T15:08:57.000+00:00"
        },
        {
            "id": "SKD-20220704-00009",
            "customer_id": 2,
            "operator": "sky",
            "income_sheet_content": [
                {
                    "id": 55,
                    "income_sheetId": null,
                    "account": "bank",
                    "amount": 1,
                    "remark": "字符串"
                }
            ],
            "total_amount": 1,
            "state": "待审批",
            "create_time": "2022-07-04T15:11:11.000+00:00"
        }
    ]
}
```

###### 预期结果

* 同上

##### GET 付款单查询测试

GET /outcomeSheet/sheet-show

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "id": "SKD-20220705-00000",
            "customer_id": 2,
            "operator": "sky",
            "outcome_sheet_content": [
                {
                    "id": 38,
                    "outcome_sheetId": null,
                    "account": "bank",
                    "amount": 10000,
                    "remark": "付款01"
                }
            ],
            "total_amount": 10000,
            "state": "待审批",
            "create_time": "2022-07-05T01:38:33.000+00:00"
        },
        {
            "id": "SKD-20220705-00001",
            "customer_id": 2,
            "operator": "sky",
            "outcome_sheet_content": [
                {
                    "id": 39,
                    "outcome_sheetId": null,
                    "account": "bank1",
                    "amount": 10000,
                    "remark": "付款1"
                }
            ],
            "total_amount": 10000,
            "state": "待审批",
            "create_time": "2022-07-05T01:39:41.000+00:00"
        },
        {
            "id": "SKD-20220705-00002",
            "customer_id": 2,
            "operator": "sky",
            "outcome_sheet_content": [
                {
                    "id": 40,
                    "outcome_sheetId": null,
                    "account": "bank01",
                    "amount": 10000,
                    "remark": "第一笔付款"
                },
                {
                    "id": 41,
                    "outcome_sheetId": null,
                    "account": "bank02",
                    "amount": 150000,
                    "remark": "第二笔付款"
                }
            ],
            "total_amount": 160000,
            "state": "待审批",
            "create_time": "2022-07-05T01:43:15.000+00:00"
        },
        {
            "id": "SKD-20220705-00003",
            "customer_id": 2,
            "operator": "sky",
            "outcome_sheet_content": [
                {
                    "id": 42,
                    "outcome_sheetId": null,
                    "account": "bank01",
                    "amount": 1500,
                    "remark": "第一笔付款"
                },
                {
                    "id": 43,
                    "outcome_sheetId": null,
                    "account": "bank02",
                    "amount": 165230,
                    "remark": "第二笔付款"
                }
            ],
            "total_amount": 166730,
            "state": "待审批",
            "create_time": "2022-07-05T01:55:03.000+00:00"
        }
    ]
}
```

###### 预期结果

* 同上

##### GET 查看销售明细表

GET /salesDetail/show-all

查看销售明细表

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "sale_time": "2022-05-23T16:04:37.000+00:00",
            "record_type": "销售",
            "product_name": "戴尔电脑",
            "product_type": "戴尔(DELL)Vostro笔记本电脑5410 123色 戴尔成就3500Vostro1625D",
            "customer_id": 2,
            "operator": "xiaoshoujingli",
            "amount": 600,
            "unit_price": 3500.00,
            "total_price": 2100000.00
        },
        {
            "sale_time": "2022-05-23T16:04:37.000+00:00",
            "record_type": "销售",
            "product_name": "小米手机",
            "product_type": "lalalalala",
            "customer_id": 2,
            "operator": "xiaoshoujingli",
            "amount": 600,
            "unit_price": 3500.00,
            "total_price": 2100000.00
        },
        {
            "sale_time": "2022-05-23T16:32:41.000+00:00",
            "record_type": "销售",
            "product_name": "戴尔电脑",
            "product_type": "戴尔(DELL)Vostro笔记本电脑5410 123色 戴尔成就3500Vostro1625D",
            "customer_id": 2,
            "operator": "xiaoshoujingli",
            "amount": 100,
            "unit_price": 2200.00,
            "total_price": 220000.00
        },
        {
            "sale_time": "2022-05-23T16:32:41.000+00:00",
            "record_type": "销售",
            "product_name": "小米手机",
            "product_type": "lalalalala",
            "customer_id": 2,
            "operator": "xiaoshoujingli",
            "amount": 100,
            "unit_price": 4000.00,
            "total_price": 400000.00
        },
        {
            "sale_time": "2022-05-23T16:45:25.000+00:00",
            "record_type": "销售",
            "product_name": "戴尔电脑",
            "product_type": "戴尔(DELL)Vostro笔记本电脑5410 123色 戴尔成就3500Vostro1625D",
            "customer_id": 2,
            "operator": "xiaoshoujingli",
            "amount": 100,
            "unit_price": 3000.00,
            "total_price": 300000.00
        },
        {
            "sale_time": "2022-05-23T16:45:25.000+00:00",
            "record_type": "销售",
            "product_name": "小米手机",
            "product_type": "lalalalala",
            "customer_id": 2,
            "operator": "xiaoshoujingli",
            "amount": 100,
            "unit_price": 4200.00,
            "total_price": 420000.00
        }
    ]
}
```



###### 预期结果

同上

##### GET 查看经营情况表

GET /bsSheet/sheet-show

查看经营情况表

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| beginDateStr  | query  | string | 否   | none |
| endDateStr    | query  | string | 否   | none |
| Authorization | header | string | 否   | none |

> 返回示例

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": {
        "businessIncomeVO": {
            "incomeAfterDiscount": 6231400,
            "discount": 0.8,
            "saleIncome": 4431400,
            "purchaseReturnsIncome": 1800000
        },
        "businessOutcomeVO": {
            "outcome": 8308100,
            "saleReturnOutcome": 0,
            "purchaseOutcome": 8300000,
            "salary": 8100
        },
        "profit": -2076700
    }
}
```



###### 预期结果

同上



#### 3.1.3 API测试/人力资源模块

> 最后一次修改: 杨峥
>
> 最后一次修改日期: 2022/07/09

##### GET 查询所有员工信息

GET /employee/findAllEmployee

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "id": 1,
            "name": "DTA",
            "gender": "男",
            "birthDate": "2022-06-30T15:17:42.000+00:00",
            "phoneNumber": "18168100075",
            "job": "INVENTORY_MANAGER",
            "jobLevel": 1,
            "account": "doomgrahamismyfather"
        },
        {
            "id": 2,
            "name": "刘钦老师",
            "gender": "男",
            "birthDate": "1986-06-30T14:17:42.000+00:00",
            "phoneNumber": "110",
            "job": "GM",
            "jobLevel": 1,
            "account": "seec67"
        },
        {
            "id": 3,
            "name": "赵锁子",
            "gender": "男",
            "birthDate": "1800-04-01T15:17:42.000+00:00",
            "phoneNumber": "201250178",
            "job": "INVENTORY_MANAGER",
            "jobLevel": 1,
            "account": "lockson"
        },
        {
            "id": 4,
            "name": "袜子socket",
            "gender": "男",
            "birthDate": "2008-06-30T15:17:42.000+00:00",
            "phoneNumber": "12306",
            "job": "INVENTORY_MANAGER",
            "jobLevel": 2,
            "account": "hahaVO"
        },
        {
            "id": 5,
            "name": "seecoder平台",
            "gender": "男",
            "birthDate": "2015-06-30T15:17:42.000+00:00",
            "phoneNumber": "123456789",
            "job": "INVENTORY_MANAGER",
            "jobLevel": 3,
            "account": "seecoder"
        },
        {
            "id": 6,
            "name": "兆星锐",
            "gender": "男",
            "birthDate": "1998-06-30T15:17:42.000+00:00",
            "phoneNumber": "4008823823",
            "job": "SALE_MANAGER",
            "jobLevel": 1,
            "account": "zxrhandsomeboy"
        },
        {
            "id": 7,
            "name": "赵如雷",
            "gender": "女",
            "birthDate": "2020-12-30T15:17:42.000+00:00",
            "phoneNumber": "20220708",
            "job": "SALE_STAFF",
            "jobLevel": 3,
            "account": "Thunder"
        },
        {
            "id": 8,
            "name": "大洞王爷",
            "gender": "男",
            "birthDate": "2022-02-24T15:17:42.000+00:00",
            "phoneNumber": "114514",
            "job": "GM",
            "jobLevel": 1,
            "account": "BigHoleShoes"
        },
        {
            "id": 9,
            "name": "勇哥大三加油",
            "gender": "男",
            "birthDate": "2022-07-08T15:17:42.000+00:00",
            "phoneNumber": "666",
            "job": "SALE_MANAGER",
            "jobLevel": 1,
            "account": "bravebrothergogogo"
        }
    ]
}
```

###### 预期结果

同上

##### POST 创建新员工

POST /employee/createEmployee

> Body 请求参数

```json
{
  "name": "Test_man",
  "gender": "男",
  "birthDate": "2022-07-08T15:17:42.000+00:00",
  "phoneNumber": "666",
  "job": "SALE_MANAGER",
  "jobLevel": 1,
  "account": "test"
}
```

###### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| body | body | object | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": {
        "id": 10,
        "name": "Test_man",
        "gender": "男",
        "birthDate": "2022-07-08T15:17:42.000+00:00",
        "phoneNumber": "666",
        "job": "SALE_MANAGER",
        "jobLevel": 1,
        "account": "test"
    }
}
```

###### 预期结果

同上

##### GET 根据员工id查询其账户

GET /employee/findUser

###### 请求参数

| 名称          | 位置   | 类型    | 必选 | 说明 |
| ------------- | ------ | ------- | ---- | ---- |
| id            | query  | integer | 否   | none |
| Authorization | header | string  | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": {
        "name": "lock",
        "role": "INVENTORY_MANAGER",
        "password": "123456"
    }
}
```

###### 预期结果

同上

##### GET 根据员工id查询缺勤天数

GET /employee/findAbsence

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| username      | query  | string | 否   | none |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": 8
}
```

###### 预期结果

同上

##### POST 更新岗位信息

POST /job/updateJob

> Body 请求参数

```json
{
  "name": "SALE_STAFF",
  "basicSalary": 5000,
  "jobSalary": 6000,
  "jobLevel": 3,
  "calculateMethod": 2,
  "paymentMethod": "月薪制"
}
```

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |
| body          | body   | object | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": "操作成功"
}
```

###### 预期结果

同上

##### GET 获取全部薪资计算方式

GET /job/findAllCalculateMethod

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        "基本工资 + 岗位工资 - 税款",
        "基本工资 + 提成 + 岗位工资 - 税款"
    ]
}
```

###### 预期结果

同上

##### GET 查询所有岗位信息

GET /job/findAllJob

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "name": "FINANCIAL_STAFF",
            "basicSalary": 1500,
            "jobSalary": 2500,
            "jobLevel": 1,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "FINANCIAL_STAFF",
            "basicSalary": 2000,
            "jobSalary": 3000,
            "jobLevel": 2,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "GM",
            "basicSalary": 100000,
            "jobSalary": 100000,
            "jobLevel": 1,
            "calculateMethod": 2,
            "paymentMethod": "年薪制"
        },
        {
            "name": "HR",
            "basicSalary": 1500,
            "jobSalary": 2500,
            "jobLevel": 1,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "HR",
            "basicSalary": 2000,
            "jobSalary": 3000,
            "jobLevel": 2,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "HR",
            "basicSalary": 3000,
            "jobSalary": 4000,
            "jobLevel": 3,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "INVENTORY_MANAGER",
            "basicSalary": 1000,
            "jobSalary": 2000,
            "jobLevel": 1,
            "calculateMethod": 1,
            "paymentMethod": "年薪制"
        },
        {
            "name": "INVENTORY_MANAGER",
            "basicSalary": 1500,
            "jobSalary": 2500,
            "jobLevel": 2,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "INVENTORY_MANAGER",
            "basicSalary": 2000,
            "jobSalary": 3000,
            "jobLevel": 3,
            "calculateMethod": 1,
            "paymentMethod": "月薪制"
        },
        {
            "name": "SALE_MANAGER",
            "basicSalary": 6000,
            "jobSalary": 6000,
            "jobLevel": 1,
            "calculateMethod": 2,
            "paymentMethod": "月薪制"
        },
        {
            "name": "SALE_STAFF",
            "basicSalary": 2000,
            "jobSalary": 3000,
            "jobLevel": 1,
            "calculateMethod": 2,
            "paymentMethod": "月薪制"
        },
        {
            "name": "SALE_STAFF",
            "basicSalary": 3000,
            "jobSalary": 4000,
            "jobLevel": 2,
            "calculateMethod": 2,
            "paymentMethod": "月薪制"
        },
        {
            "name": "SALE_STAFF",
            "basicSalary": 5000,
            "jobSalary": 6000,
            "jobLevel": 3,
            "calculateMethod": 2,
            "paymentMethod": "月薪制"
        }
    ]
}
```

###### 预期结果

同上

##### GET 获取全部薪资发放方式

GET /job/findAllPaymentMethod

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        "月薪制",
        "年薪制"
    ]
}
```

###### 预期结果

同上

##### GET 制定年终奖

GET /yearEndAwards/establishYearEndAwards

###### 请求参数

| 名称       | 位置  | 类型    | 必选 | 说明 |
| ---------- | ----- | ------- | ---- | ---- |
| employeeId | query | integer | 否   | none |
| awards     | query | integer | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": "操作成功"
}
```

###### 预期结果

同上

##### GET 查看全部员工年终奖情况

GET /yearEndAwards/findAllYearEndSalary

###### 请求参数

| 名称          | 位置   | 类型   | 必选 | 说明 |
| ------------- | ------ | ------ | ---- | ---- |
| Authorization | header | string | 否   | none |

###### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功 | Inline   |

```json
{
    "code": "00000",
    "msg": "Success",
    "result": [
        {
            "employeeId": 1,
            "employeeName": "DTA",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 550
        },
        {
            "employeeId": 2,
            "employeeName": "刘钦老师",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 3,
            "employeeName": "赵锁子",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 4,
            "employeeName": "袜子socket",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 5,
            "employeeName": "seecoder平台",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 6,
            "employeeName": "兆星锐",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 7,
            "employeeName": "赵如雷",
            "totalSalaryExceptDecember": 8100,
            "yearEndAwards": 0
        },
        {
            "employeeId": 8,
            "employeeName": "大洞王爷",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 9,
            "employeeName": "勇哥大三加油",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        },
        {
            "employeeId": 10,
            "employeeName": "Test_man",
            "totalSalaryExceptDecember": 0,
            "yearEndAwards": 0
        }
    ]
}
```

###### 预期结果

同上

## 4. 系统测试

### 销售人员

#### 1.  创建客户

+ 登录销售人员账户，进入客户管理页面，点击新增客户，并输入客户信息

![image-20220709145138835](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709145138835-16573679661781.png)

![image-20220709145214621](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709145214621-16573679661792.png)

+ 点击确定后，发现已经创建好客户

![image-20220709145301751](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709145301751-16573679661793.png)

#### 2.  制定销售单以及审批

+ 进入销售管理页面点击创建销售单

![image-20220709152434435](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709152434435-16573679661794.png)

+ 创建完成后点击对勾，审批通过，销售单状态变为待总经理进行二级审批

![image-20220709154709825](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709154709825-16573679661795.png)

![image-20220709154727963](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709154727963-16573679661796.png)

+ 否则，点击红色×，则状态变为审批失败

![image-20220709154803037](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709154803037-16573679661797.png)

#### 3.  制定销售退货单以及审批

+ 进入销售退货页面，点击创建销售退货，选择相关联的

![image-20220709163640639](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709163640639.png)

+ 创建完成后可以进行审批，过程与销售单类似

![image-20220709164123296](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709164123296.png)

### 总经理

#### 1.审批单据

+ 总经理可以对各种单据进行二级审批或者审批，如进货单的二级审批

![image-20220709164242249](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709164242249.png)

![image-20220709173902395](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709173902395.png)

+ 点击√后单据状态变为审批完成，否则变为审批失败

![image-20220709164519093](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709164519093.png)

#### 2. 制定年终奖

+ 总经理进入年终奖制定页面，选择需要制定的员工

![image-20220709180433768](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709180433768.png)

+ 总经理输入金额，点击制定，制定成功

![image-20220709180513674](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709180513674.png)

![image-20220709180532330](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709180532330.png)

#### 3. 制定促销策略

+ 登录总经理账户，进入策略制定页面，默认进入“争对不同级别的用户制定促销策略”子页面。

![image-20220709145340529](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709145340529-16573679661798.png)

+ 点击新增用户策略，针对级别为10的用户制定销售策略，并点击立即创建。

![image-20220709145950764](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709145950764-16573679661799.png)

![image-20220709150038538](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709150038538-165736796617910.png)

+ 策略创建后，当有用户的销售单被审批成功后，就会生成库存赠送单，并且送给该用户代金券

![image-20220709152845476](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709152845476-165736796617911.png)

### 人力资源管理人员

#### 1.  员工打卡

+ 员工可以通过左下角打卡按键打卡，系统还显示本月该员工缺勤天数。

![image-20220709193553843](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709193553843.png)

#### 2. 岗位管理

+ 人力资源管理人员可以进入岗位管理页面编辑岗位信息

![image-20220709193840606](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709193840606.png)

+ 点击编辑操作并保存，即完成岗位信息的修改

![image-20220709193925147](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709193925147.png)

![image-20220709194156600](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709194156600.png)

#### 3.  工资单制定和审批

+ 人力资源管理人员可以在工资管理页面点击“制定工资单”按键制定工资单

![image-20220709194310640](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709194310640.png)

+ 点击“立即创建”制定成功

![image-20220709194807426](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709194807426.png)

+ 人力资源管理人员还可以点击√和×进行审批

![image-20220709195143468](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709195143468.png)

### 财务人员

#### 1. 账户管理

* 登录财务人员账户，进入账户制定界面

  ![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A11.png)

* 点击新增账户，新增一条用户名为"zyz"，余额为15000的账户信息，并点击“确定”

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A12.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A13.png)

* 点击删除账户会弹出提示框，以防止信息误删；点击“确定”后，该条账户信息被删除。

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A14.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A15.png)

#### 2. 收款管理

* 登陆财务人员账户，进入“收款管理”页面

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A16.png)

* 点击每条收款单后面的“展开”，可以显示详细转账列表

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A17.png)

* 点击制定收款单，为客户“赵勇臻”制定如下一条收款单

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A18.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A19.png)

#### 3. 付款管理

* 登陆财务人员账户，进入“付款管理”页面

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A110.png)

* 点击每条付款单后面的“展开”，可以显示详细转账列表

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A111.png)

* 点击制定付款单，为客户“赵勇臻”制定如下一条付款单

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A112.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A113.png)

#### 4. 查看工资发放单

* 登陆财务人员账户，进入“工资发放单管理”页面

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A114.png)

* 点击每条工资发放单后面的“展开”，可以显示工资单详细的明细

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A115.png)

#### 5. 查看经营历程表以及红冲，红冲复制

+ 财务人员可以查看经营历程表

![image-20220709213046783](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709213046783.png)

+ 可以根据条件筛选表单

![image-20220709213233799](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709213233799.png)

![image-20220709213243849](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709213243849.png)

+ 点击“查看单据细节”可以显示所选单据详细信息

![image-20220709213447795](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709213447795.png)

+ 点击“红冲”并确认，即进行红冲操作

![image-20220709213523630](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709213523630.png)

![image-20220709215050905](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709215050905.png)

+ 点击红冲复制，可以进行红冲复制操作。红冲复制时会跳转到对应单据创建页面，以选中单据为模板修改创建。

![image-20220709215556935](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709215556935.png)

+ 点击“导出EXCEL”可以导出EXCEL格式的表格

![image-20220709220712060](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709220712060.png)

![image-20220709220744262](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709220744262.png)

#### 6. 查看销售情况表

* 登陆财务人员账户，进入“销售情况表”页面，初始状态下所有数据均为空值

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A116.png)

* 输入或选择开始日期和结束日期，点击查询后刷新页面数据为此时间段内公司销售情况信息

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A117.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A118.png)

#### 7. 查看销售明细表

* 登陆财务人员账户，进入“销售明细表”页面，默认展示所有销售明细

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A119.png)

* 输入或选择开始日期和结束日期，点击”筛选日期“后页面仅显示在此时间范围内的销售明细

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A120.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A121.png)

* 选择重置日期后，开始日期和结束日期被置空，页面重新展示所有销售明细

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A122.png)

* 点击”销售时间“旁的排序箭头，可以实现对销售明细按时间先后的排序
  ![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A123.png)

* 可以点击”商品名称“、”客户ID“、”操作员“表头旁边的下拉选择箭头，以实现根据对应列信息的筛选，此处以”商品名称“作为演示。

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A124.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A125.png)

* 点击”导出EXCEL“，可以将当前页面内所展示的所有的销售明细以EXCEL格式导出并保存到本地

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A126.png)

![](https://raw.githubusercontent.com/heiyan-2020/SE2/master/image-20220709-%E8%B4%A2%E5%8A%A127.png)
