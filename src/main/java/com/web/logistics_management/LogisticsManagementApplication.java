package com.web.logistics_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
public class LogisticsManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsManagementApplication.class, args);
    }

}
