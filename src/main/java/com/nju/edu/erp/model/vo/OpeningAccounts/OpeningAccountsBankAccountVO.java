package com.nju.edu.erp.model.vo.OpeningAccounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningAccountsBankAccountVO {
    private String name;
    private int amount;
    private String date;
}
