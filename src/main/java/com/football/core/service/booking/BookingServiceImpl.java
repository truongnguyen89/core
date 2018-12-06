package com.football.core.service.booking;

import com.football.common.database.ConnectionCommon;
import com.football.common.model.stadium.Booking;
import com.football.common.response.Response;
import com.football.core.repository.BookingRepository;
import com.football.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Date;
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

    @Autowired
    DataSource dataSource;

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

    @Override
    public Response booking(long playerId, long matchId, Date matchDay, Integer type, long createdUserId, String comment) throws Exception {
        Connection connection = dataSource.getConnection();
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall("{call pro_booking (?, ?, ?, ?, ?, ?, ?)}");
            int i = 1;
            ConnectionCommon.doSetLongParams(cs, i++, playerId);
            ConnectionCommon.doSetLongParams(cs, i++, matchId);
            ConnectionCommon.doSetDateParams(cs, i++, new java.sql.Date(matchDay.getTime()));
            ConnectionCommon.doSetIntParams(cs, i++, type);
            ConnectionCommon.doSetLongParams(cs, i++, createdUserId);
            ConnectionCommon.doSetStringParams(cs, i++, comment);
            cs.registerOutParameter(i, Types.VARCHAR);
            cs.execute();
            return Response.valueOf(cs.getString(i));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            ConnectionCommon.close(cs);
            ConnectionCommon.close(connection);
        }
    }
}
