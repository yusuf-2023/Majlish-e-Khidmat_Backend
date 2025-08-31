package com.majlishekhidmat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // <- Important
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
}
