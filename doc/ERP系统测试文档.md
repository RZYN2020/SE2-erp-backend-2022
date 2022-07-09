# ERP系统测试文档

## 1. 引言

> 最后一次修改: 赵勇臻
>
> 最后一次修改日期: 2022/7/9

## 2. 单元测试

### 2.1 后端API测试

数据库此时已经过配置填充。

#### 2.1.1 API测试/总经理模块

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

#### 2.1.2 API测试/财务模块

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



## 3. 系统测试

