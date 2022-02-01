package com.ponyz.stellar.domain.reservation.repository;

import com.ponyz.stellar.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
