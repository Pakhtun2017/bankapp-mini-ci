package com.bankapp.transaction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TransactionApplication {

    @Value("${DB_TIMEOUT:100}")
    private int timeout;

    @Value("${ENABLE_TX_AUDIT:false}")
    private boolean txAuditEnabled;

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @GetMapping("/transactions")
    public String getTransactions() throws InterruptedException {
        Thread.sleep(timeout); 
        return "Transactions returned after " + timeout + "ms";
    }
}

