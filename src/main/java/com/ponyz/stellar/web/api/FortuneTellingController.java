package com.ponyz.stellar.web.api;

import com.ponyz.stellar.domain.reservation.entity.Reservation;
import com.ponyz.stellar.domain.reservation.repository.ReservationRepository;
import com.ponyz.stellar.web.dto.SelectedCardsDto;
import com.ponyz.stellar.web.dto.SettingToSpreadingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fortune-telling")
@RequiredArgsConstructor
public class FortuneTellingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReservationRepository reservationRepository;

    @PostMapping("/setting")
    public String setting(@RequestBody SettingToSpreadingDto settingToSpreadingDto) {
        reservationRepository.save(Reservation.builder()
                .title(settingToSpreadingDto.getTitle())
                .amount_cards(settingToSpreadingDto.getAmountOfCards())
                .selected_cards(settingToSpreadingDto.getSelectedCards())
                .build());
        return settingToSpreadingDto.getTitle();
    }

    @PostMapping("/pick-cards")
    public String pickCards(@RequestBody SelectedCardsDto selectedCardsDto) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (Integer card : selectedCardsDto.getCards()) {
            listOperations.rightPushAll(selectedCardsDto.getKey(), card.toString());
        }

        return selectedCardsDto.getKey();
    }
}
