package com.ponyz.stellar.web.vo;

import com.ponyz.stellar.domain.card.entity.TarotCards;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailVo {
    private ReservationVo reservation;
    private List<Integer> cards;
    private List<TarotCards> cardsInfo;
}
