package com.ponyz.stellar.web.api;

import com.ponyz.stellar.domain.card.entity.TarotCards;
import com.ponyz.stellar.domain.card.entity.TarotDecks;
import com.ponyz.stellar.domain.card.repository.TarotCardsQueryRepository;
import com.ponyz.stellar.domain.card.repository.TarotDecksRepository;
import com.ponyz.stellar.domain.reservation.entity.Reservation;
import com.ponyz.stellar.domain.reservation.repository.ReservationQueryRepository;
import com.ponyz.stellar.domain.reservation.repository.ReservationRepository;
import com.ponyz.stellar.web.dto.SelectedCardsDto;
import com.ponyz.stellar.web.dto.SettingToSpreadingDto;
import com.ponyz.stellar.web.vo.ReservationDetailVo;
import com.ponyz.stellar.web.vo.ReservationVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Base64;
import java.util.Base64.Encoder;

@RestController
@RequestMapping("/api/fortune-telling")
@RequiredArgsConstructor
public class FortuneTellingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReservationRepository reservationRepository;
    private final TarotDecksRepository tarotDecksRepository;
    private final TarotCardsQueryRepository tarotCardsQueryRepository;
    private final ReservationQueryRepository reservationQueryRepository;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> data = reservationRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/reservations/{idx}")
    public ResponseEntity<ReservationDetailVo> getReservationByIdx(@PathVariable Long idx, @RequestParam(value = "encKey", required = false) String encKey) throws Exception {
        ReservationVo data = (idx.equals(0L))? reservationQueryRepository.getByEncKey(URLEncoder.encode(encKey, "UTF-8")) : reservationQueryRepository.getByIdx(idx);

        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        List<Object> cards = listOperations.range(data.getIdx().toString(), 0, -1);
        List<Integer> convertCards = cards.stream().map(obj -> Integer.parseInt(obj.toString())).collect(Collectors.toList());
        List<TarotCards> cardsInfo = tarotCardsQueryRepository.findBySeqs(data.getDeckIdx(), convertCards);

        ReservationDetailVo result = ReservationDetailVo.builder()
                .reservation(data)
                .cards(convertCards)
                .cardsInfo(cardsInfo)
                .build();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/decks")
    public ResponseEntity<List<TarotDecks>> getTarotDecks() {
        List<TarotDecks> data = tarotDecksRepository.findAll();
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/setting")
    public ResponseEntity addSetting(@RequestBody SettingToSpreadingDto settingToSpreadingDto) throws UnsupportedEncodingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime reservationAt = LocalDateTime.parse(settingToSpreadingDto.getReservationAt(), formatter);
        String encodedStr = URLEncoder.encode(Base64.getEncoder().encodeToString(reservationAt.toString().getBytes()), "UTF-8");

        reservationRepository.save(Reservation.builder()
                .userName(settingToSpreadingDto.getUserName())
                .deckIdx(settingToSpreadingDto.getDeckIdx())
                .amountCards(settingToSpreadingDto.getAmountOfCards())
                .selectedCards(settingToSpreadingDto.getSelectedCards())
                .wayToArray(settingToSpreadingDto.getWayToArray())
                .reservationAt(reservationAt)
                .encKey(encodedStr)
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
