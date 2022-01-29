package com.ponyz.stellar.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FortuneTellingController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/pick/cards")
    public String index() {
        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "test";

        // when
        valueOperations.set(key, "cndnj");

        // then
        String value = valueOperations.get(key);

        return value;
    }
}
