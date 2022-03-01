package com.ponyz.stellar.domain.card.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tarot_cards")
public class TarotCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "deck_idx", nullable = false)
    private Long deckIdx;

    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "is_major", nullable = false)
    private Boolean isMajor;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
