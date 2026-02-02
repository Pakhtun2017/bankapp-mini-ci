package com.bankapp.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AccountApplication {

    @Value("${ENABLE_AUDIT_LOGS:false}")
    private boolean auditEnabled;

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

    @GetMapping("/account")
    public String getAccount() {
    if (auditEnabled) {
        System.out.println("AUDIT ENABLED: /account endpoint accessed");
    } else {
        System.out.println("AUDIT DISABLED");
    }
    return "Account balance: $1000";
    }	
    
}

