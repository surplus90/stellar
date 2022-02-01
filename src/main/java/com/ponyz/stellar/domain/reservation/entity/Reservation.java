package com.ponyz.stellar.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Integer amount_cards;

    @Column(name = "selected_cards", nullable = false)
    private Integer selected_cards;

    @Column(insertable = false, updatable = false)
    private Timestamp created_at;
}
