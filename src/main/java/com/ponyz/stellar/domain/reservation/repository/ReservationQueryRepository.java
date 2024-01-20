package com.ponyz.stellar.domain.reservation.repository;

import com.ponyz.stellar.domain.card.entity.QTarotCards;
import com.ponyz.stellar.domain.card.entity.QTarotDecks;
import com.ponyz.stellar.domain.card.entity.TarotCards;
import com.ponyz.stellar.domain.reservation.entity.QReservation;
import com.ponyz.stellar.web.vo.ReservationDetailVo;
import com.ponyz.stellar.web.vo.ReservationVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ReservationVo getByIdx(Long idx) {
        QReservation reservation = QReservation.reservation;
        QTarotDecks tarotDecks = QTarotDecks.tarotDecks;
        return jpaQueryFactory.select(Projections.fields(ReservationVo.class,
                reservation.idx,
                reservation.userName,
                reservation.deckIdx,
                tarotDecks.deckName,
                reservation.amountCards,
                reservation.selectedCards,
                reservation.wayToArray,
                reservation.reservationAt,
                reservation.setcardsAt,
                reservation.createdAt
                ))
                .from(reservation)
                .innerJoin(tarotDecks).on(tarotDecks.idx.eq(reservation.deckIdx))
                .where(reservation.idx.eq(idx))
                .fetchOne();
    }

    public ReservationVo getByEncKey(String encKey) {
        QReservation reservation = QReservation.reservation;
        QTarotDecks tarotDecks = QTarotDecks.tarotDecks;
        return jpaQueryFactory.select(Projections.fields(ReservationVo.class,
                reservation.idx,
                reservation.userName,
                reservation.deckIdx,
                tarotDecks.deckName,
                reservation.amountCards,
                reservation.selectedCards,
                reservation.wayToArray,
                reservation.reservationAt,
                reservation.setcardsAt,
                reservation.createdAt
        ))
                .from(reservation)
                .innerJoin(tarotDecks).on(tarotDecks.idx.eq(reservation.deckIdx))
                .where(reservation.encKey.eq(encKey))
                .fetchOne();
    }
}
