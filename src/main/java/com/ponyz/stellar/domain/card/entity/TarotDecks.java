package com.ponyz.stellar.domain.card.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tarot_decks")
public class TarotDecks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "deck_name", nullable = false)
    private String deckName;

    @Column(name = "amount_cards", nullable = false)
    private Integer amountCards;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
