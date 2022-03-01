package com.ponyz.stellar.web.vo;

import com.ponyz.stellar.domain.card.entity.TarotCards;
import com.ponyz.stellar.domain.reservation.entity.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReservationDetailVo {
    private Reservation reservation;
    private List<Integer> cards;
    private List<TarotCards> cardsInfo;
}
