package com.bankapp.transaction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionApplication {

    @Value("${DB_TIMEOUT:100}")
    private int timeout;

    @GetMapping("/transactions")
    public String getTransactions() throws InterruptedException {
        Thread.sleep(timeout); // 👈 intentional problem
        return "Transactions returned after " + timeout + "ms";
    }
}

