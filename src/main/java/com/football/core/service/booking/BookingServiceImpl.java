package com.football.core.service.booking;

import com.football.common.model.stadium.Booking;
import com.football.core.repository.BookingRepository;
import com.football.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class BookingServiceImpl extends BaseService implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Override
    public Booking create(Booking booking) throws Exception {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findById(long id) throws Exception {
        return bookingRepository.findOne(id);
    }

    @Override
    public List<Booking> findByStatus(int status) throws Exception {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public Iterable<Booking> findAll() throws Exception {
        return bookingRepository.findAll();
    }

    @Override
    public Booking update(Booking booking) throws Exception {
        return bookingRepository.save(booking);
    }
}
