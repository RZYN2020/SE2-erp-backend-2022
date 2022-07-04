package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.model.vo.BankAccountVO;
import com.nju.edu.erp.service.BankAccountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create")
    public Response createAccount(@RequestBody BankAccountVO inputVO) {
        return Response.buildSuccess(bankAccountService.createBankAccount(inputVO));
    }

    @GetMapping("/delete")
    public Response delete(@RequestParam(value = "accountName") String accountName) {
        bankAccountService.delete(accountName);
        return Response.buildSuccess();
    }

    @GetMapping("/findAll")
    public Response findAll() {
        return Response.buildSuccess(bankAccountService.findAll());
    }

}