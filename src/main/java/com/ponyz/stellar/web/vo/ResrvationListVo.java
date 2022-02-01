package com.ponyz.stellar.web.vo;

import com.ponyz.stellar.domain.reservation.entity.Reservation;
import lombok.Builder;

import java.util.List;

@Builder
public class ResrvationListVo {
    private Integer totalPages;
    private List<Reservation> rows;
}
