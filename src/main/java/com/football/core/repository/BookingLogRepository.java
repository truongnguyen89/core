package com.football.core.repository;

import com.football.common.model.stadium.Booking;
import com.football.common.model.stadium.BookingLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingLogRepository extends CrudRepository<BookingLog, Long> {
    List<BookingLog> findByStatusNew(int status);

    List<BookingLog> findByBookingId(long bookingId);
}
