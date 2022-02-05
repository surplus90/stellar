package com.ponyz.stellar.domain.reservation.repository;

import com.ponyz.stellar.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByOrderByCreatedAtDesc();
}
