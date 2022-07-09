package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.OpeningAccounts.OpeningAccountsVO;

public interface OpeningAccountsService {

    void open();

    OpeningAccountsVO find(String time);
}
