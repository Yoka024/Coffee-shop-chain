package com.offee.Coffee.shop.chain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/log-test")
    public String logTest() {
        logger.info("Це тестовий лог повідомлення для ELK");
        return "OK";
    }
}
