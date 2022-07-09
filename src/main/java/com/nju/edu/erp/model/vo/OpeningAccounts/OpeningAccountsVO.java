package com.nju.edu.erp.model.vo.OpeningAccounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningAccountsVO {
    private List<OpeningAccountsBankAccountVO> bankAccountVOList;
    private List<OpeningAccountsCustomerVO> customerVOList;
    private List<OpeningAccountsProductVO> productVOList;
}
