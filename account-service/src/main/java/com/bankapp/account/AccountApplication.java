package com.bankapp.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountApplication {

    @Value("${ENABLE_AUDIT_LOGS:false}")
    private boolean auditEnabled;

    @GetMapping("/account")
    public String getAccount() {
        if (auditEnabled) {
            System.out.println("AUDIT: Account endpoint accessed");
        }
        return "Account balance: $1000";
    }
}

