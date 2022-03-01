package com.ponyz.stellar.domain.card.repository;

import com.ponyz.stellar.domain.card.entity.TarotCards;
import com.ponyz.stellar.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarotCardsRepository extends JpaRepository<TarotCards, Long> {
}
