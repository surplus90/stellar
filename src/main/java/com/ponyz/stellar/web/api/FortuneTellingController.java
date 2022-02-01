package com.ponyz.stellar.web.api;

import com.ponyz.stellar.domain.reservation.entity.Reservation;
import com.ponyz.stellar.domain.reservation.repository.ReservationRepository;
import com.ponyz.stellar.web.dto.SelectedCardsDto;
import com.ponyz.stellar.web.dto.SettingToSpreadingDto;
import com.ponyz.stellar.web.vo.ResrvationListVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fortune-telling")
@RequiredArgsConstructor
public class FortuneTellingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReservationRepository reservationRepository;

    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        List<Reservation> data = reservationRepository.findAll();
        return data;
    }

    @GetMapping("/reservations/{idx}")
    public Optional<Reservation> getReservationByIdx(@PathVariable Long idx) {
        Optional<Reservation> data = reservationRepository.findById(idx);
        return data;
    }

    @PostMapping("/setting")
    public String addSetting(@RequestBody SettingToSpreadingDto settingToSpreadingDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime reservationAt = LocalDateTime.parse(settingToSpreadingDto.getReservationAt(), formatter);

        reservationRepository.save(Reservation.builder()
                .title(settingToSpreadingDto.getTitle())
                .amountCards(settingToSpreadingDto.getAmountOfCards())
                .selectedCards(settingToSpreadingDto.getSelectedCards())
                .reservationAt(reservationAt)
                .build());
        return settingToSpreadingDto.getTitle();
    }

    @PostMapping("/pick-cards")
    public String addPickCards(@RequestBody SelectedCardsDto selectedCardsDto) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (Integer card : selectedCardsDto.getCards()) {
            listOperations.rightPushAll(selectedCardsDto.getKey(), card.toString());
        }

        return selectedCardsDto.getKey();
    }
}
