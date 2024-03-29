package com.ponyz.stellar.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "deck_idx", nullable = false)
    private Long deckIdx;

    @Column(name = "amount_cards", nullable = false)
    private Integer amountCards;

    @Column(name = "selected_cards", nullable = false)
    private Integer selectedCards;

    @Column(name = "way_to_array", nullable = false)
    private Integer wayToArray;

    @Column(name = "reservation_at", nullable = false)
    private LocalDateTime reservationAt;

    @Column(name = "setcards_at")
    private LocalDateTime setcardsAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "enc_key")
    private String encKey;

    public void setSetcardsAt (LocalDateTime setcardsAt) { this.setcardsAt = setcardsAt; }

    public void addSelectedCards (Integer selectedCards) { this.selectedCards += selectedCards; }
}
