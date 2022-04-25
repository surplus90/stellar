package com.ponyz.stellar.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationVo {
    private Long idx;
    private String userName;
    private Long deckIdx;
    private String deckName;
    private Integer amountCards;
    private Integer selectedCards;
    private LocalDateTime reservationAt;
    private LocalDateTime setcardsAt;
    private LocalDateTime createdAt;
}
