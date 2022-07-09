package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.service.OpeningAccountsService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/openingAccounts")
public class OpeningAccountsController {

    private final OpeningAccountsService openingAccountsService;

    @Autowired
    public OpeningAccountsController(OpeningAccountsService openingAccountsService) {
        this.openingAccountsService = openingAccountsService;
    }

    @GetMapping("open")
    public Response open() {
        openingAccountsService.open();
        return Response.buildSuccess();
    }

    @GetMapping("find")
    public Response find(@RequestParam String time) {
        return Response.buildSuccess(openingAccountsService.find(time));
    }
}
