package com.ponyz.stellar.domain.card.repository;

import com.ponyz.stellar.domain.card.entity.QTarotCards;
import com.ponyz.stellar.domain.card.entity.TarotCards;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TarotCardsQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<TarotCards> findBySeqs(List<Integer> seqs) {
        QTarotCards tarotCards = QTarotCards.tarotCards;
        return jpaQueryFactory.selectFrom(tarotCards)
                .where(tarotCards.seq.in(seqs))
                .fetch();
    }
}
