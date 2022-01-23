package com.ponyz.stellar.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String index() {
        String res = "Hello, World!";
        return res;
    }
}
