package com.football.core.service.booking;

import com.football.common.model.stadium.Booking;
import com.football.common.response.Response;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface BookingService {
    Booking create(Booking booking) throws Exception;

    Booking findById(long id) throws Exception;

    List<Booking> findByStatus(int status) throws Exception;

    Iterable<Booking> findAll() throws Exception;

    Booking update(long id, Booking booking) throws Exception;

    Response update(long id, int status, long userId, String reason) throws Exception;

    Response booking(long playerId, long matchId, Date matchDay, Integer type, long createdUserId, String comment) throws Exception;
}
