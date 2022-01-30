package com.ponyz.stellar.web.api;

import com.ponyz.stellar.web.dto.SelectedCardsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fortune-telling")
public class FortuneTellingController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/pick-cards")
    public String pickCards(@RequestBody SelectedCardsDto selectedCardsDto) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (Integer card : selectedCardsDto.getCards()) {
            listOperations.rightPushAll(selectedCardsDto.getKey(), card.toString());
        }

        return selectedCardsDto.getKey();
    }
}
