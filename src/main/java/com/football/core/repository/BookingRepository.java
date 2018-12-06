package com.football.core.repository;

import com.football.common.model.stadium.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findByStatus(int status);
}
