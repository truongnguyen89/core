package com.football.core.service.booking;

import com.football.common.model.stadium.BookingLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingLogService {
    List<BookingLog> findByStatus(int status) throws Exception;

    List<BookingLog> findByBookingId(long bookingId) throws Exception;
}
