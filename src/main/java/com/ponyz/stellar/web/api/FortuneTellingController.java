package com.ponyz.stellar.web.api;

import com.ponyz.stellar.domain.card.entity.TarotCards;
import com.ponyz.stellar.domain.card.repository.TarotCardsQueryRepository;
import com.ponyz.stellar.domain.reservation.entity.Reservation;
import com.ponyz.stellar.domain.reservation.repository.ReservationRepository;
import com.ponyz.stellar.web.dto.SelectedCardsDto;
import com.ponyz.stellar.web.dto.SettingToSpreadingDto;
import com.ponyz.stellar.web.vo.ReservationDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fortune-telling")
@RequiredArgsConstructor
public class FortuneTellingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReservationRepository reservationRepository;
    private final TarotCardsQueryRepository tarotCardsQueryRepository;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> data = reservationRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/reservations/{idx}")
    public ResponseEntity<ReservationDetailVo> getReservationByIdx(@PathVariable Long idx) throws Exception {
        Reservation data = reservationRepository.findById(idx).orElseThrow(() -> new Exception("예약이 존재하지 않습니다."));

        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        List<Object> cards = listOperations.range(idx.toString(), 0, -1);
        List<Integer> convertCards = cards.stream().map(obj -> Integer.parseInt(obj.toString())).collect(Collectors.toList());
        List<TarotCards> cardsInfo = tarotCardsQueryRepository.findBySeqs(convertCards);

        ReservationDetailVo result = ReservationDetailVo.builder()
                .reservation(data)
                .cards(convertCards)
                .cardsInfo(cardsInfo)
                .build();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/setting")
    public ResponseEntity addSetting(@RequestBody SettingToSpreadingDto settingToSpreadingDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime reservationAt = LocalDateTime.parse(settingToSpreadingDto.getReservationAt(), formatter);

        reservationRepository.save(Reservation.builder()
                .userName(settingToSpreadingDto.getUserName())
                .amountCards(settingToSpreadingDto.getAmountOfCards())
                .selectedCards(settingToSpreadingDto.getSelectedCards())
                .reservationAt(reservationAt)
                .build());
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/setting/{idx}/cards/{amount}")
    public ResponseEntity putSettingMoreCards(@PathVariable Long idx, @PathVariable Integer amount) throws Exception {
        Reservation data = reservationRepository.findById(idx).orElseThrow(() -> new Exception("예약이 존재하지 않습니다."));
        data.addSelectedCards(amount);
        reservationRepository.save(data);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/pick-cards")
    public ResponseEntity<Long> addPickCards(@RequestBody SelectedCardsDto selectedCardsDto) throws Exception {
        Reservation data = reservationRepository.findById(selectedCardsDto.getReservationIdx()).orElseThrow(() -> new Exception("예약이 존재하지 않습니다."));

        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (Integer card : selectedCardsDto.getCards()) {
            listOperations.rightPushAll(data.getIdx().toString(), card.toString());
        }

        data.setSetcardsAt(LocalDateTime.now());
        reservationRepository.save(data);

        return ResponseEntity.ok().body(data.getIdx());
    }

    @GetMapping("/show-cards/{idx}")
    public ResponseEntity<List<Object>> showPickCards(@PathVariable Long idx) throws Exception {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(idx.toString());
        if (size.equals(0L)) throw new Exception("선택 된 카드가 없습니다.");
        List<Object> cards = listOperations.range(idx.toString(), 0, -1);

        return ResponseEntity.ok().body(cards);
    }
}
