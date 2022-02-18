package com.ponyz.stellar.web.api;

import com.ponyz.stellar.domain.reservation.entity.Reservation;
import com.ponyz.stellar.domain.reservation.repository.ReservationRepository;
import com.ponyz.stellar.web.dto.SelectedCardsDto;
import com.ponyz.stellar.web.dto.SettingToSpreadingDto;
import com.ponyz.stellar.web.vo.ReservationDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<Reservation> data = reservationRepository.findAllByOrderByCreatedAtDesc();
        return data;
    }

    @GetMapping("/reservations/{idx}")
    public ResponseEntity<ReservationDetailVo> getReservationByIdx(@PathVariable Long idx) throws Exception {
        Reservation data = reservationRepository.findById(idx).orElseThrow(() -> new Exception("예약이 존재하지 않습니다."));

        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(idx.toString());
        List<Object> cards = listOperations.range(idx.toString(), 0, -1);

        ReservationDetailVo result = ReservationDetailVo.builder()
                .reservation(data)
                .cards(cards)
                .build();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/setting")
    public String addSetting(@RequestBody SettingToSpreadingDto settingToSpreadingDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime reservationAt = LocalDateTime.parse(settingToSpreadingDto.getReservationAt(), formatter);

        reservationRepository.save(Reservation.builder()
                .userName(settingToSpreadingDto.getUserName())
                .amountCards(settingToSpreadingDto.getAmountOfCards())
                .selectedCards(settingToSpreadingDto.getSelectedCards())
                .reservationAt(reservationAt)
                .build());
        return settingToSpreadingDto.getUserName();
    }

    @PostMapping("/pick-cards")
    public Long addPickCards(@RequestBody SelectedCardsDto selectedCardsDto) throws Exception {
        Reservation data = reservationRepository.findById(selectedCardsDto.getReservationIdx()).orElseThrow(() -> new Exception("예약이 존재하지 않습니다."));

        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (Integer card : selectedCardsDto.getCards()) {
            listOperations.rightPushAll(data.getIdx().toString(), card.toString());
        }

        data.setSetcardsAt(LocalDateTime.now());
        reservationRepository.save(data);

        return data.getIdx();
    }

    @GetMapping("/show-cards/{idx}")
    public List<Object> showPickCards(@PathVariable Long idx) throws Exception {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(idx.toString());
        if (size.equals(0L)) throw new Exception("선택 된 카드가 없습니다.");
        List<Object> cards = listOperations.range(idx.toString(), 0, -1);

        return cards;
    }
}
