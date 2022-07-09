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
> 最后一次修改日期: 2022/7/9

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

## 3. 系统测试

