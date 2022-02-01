package com.ponyz.stellar.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "amount_cards", nullable = false)
    private Integer amountCards;

    @Column(name = "selected_cards", nullable = false)
    private Integer selectedCards;

    @Column(name = "reservation_at", nullable = false)
    private LocalDateTime reservationAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
