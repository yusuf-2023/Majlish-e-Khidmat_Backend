package com.majlishekhidmat.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class pingController {

    @GetMapping("/ping")
public String ping() {
    return "ok";
}


}
