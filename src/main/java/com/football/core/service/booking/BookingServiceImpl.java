package com.football.core.service.booking;

import com.football.common.cache.Cache;
import com.football.common.constant.Constant;
import com.football.common.constant.TextConstant;
import com.football.common.database.ConnectionCommon;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.model.stadium.Booking;
import com.football.common.model.stadium.BookingLog;
import com.football.common.model.user.User;
import com.football.common.response.Response;
import com.football.core.repository.BookingLogRepository;
import com.football.core.repository.BookingRepository;
import com.football.core.repository.MatchRepository;
import com.football.core.repository.UserRepository;
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
    BookingLogRepository bookingLogRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    DataSource dataSource;

    @Override
    public Booking create(Booking booking) throws Exception {
        //<editor-fold defaultstate="collapsed" desc="Validate inut">
        if (booking.getMatchDay().before(new Date()))
            throw new CommonException(Response.BAD_REQUEST,
                    MessageCommon.getMessage(
                            TextConstant.MESSAGE.INVALID,
                            Constant.TABLE.BOOKING
                    )
            );
        else if (booking.getStatus() != Cache.getIntParamFromCache(Constant.PARAMS.TYPE.BOOKING, Constant.PARAMS.CODE.STATUS_NEW, 1))
            throw new CommonException(Response.BAD_REQUEST,
                    MessageCommon.getMessage(
                            TextConstant.MESSAGE.INVALID_FIELD_OF_OBJECT,
                            Constant.PARAMS.CODE.STATUS,
                            Constant.TABLE.BOOKING
                    )
            );
        //Validate player, create user, match
        User player = userRepository.findOne(booking.getPlayerId());
        if (player == null || player.getStatus() != Constant.STATUS_OBJECT.ACTIVE_INT)
            throw new CommonException(Response.NOT_FOUND,
                    MessageCommon.getMessage(
                            TextConstant.MESSAGE.NOT_FOUND_FIELD_OF_OBJECT,
                            booking.getPlayerId() + "",
                            Constant.TABLE.USER
                    )
            );
        User creater = userRepository.findOne(booking.getCreatedUserId());
        if (creater == null || creater.getStatus() != Constant.STATUS_OBJECT.ACTIVE_INT)
            throw new CommonException(Response.NOT_FOUND,
                    MessageCommon.getMessage(
                            TextConstant.MESSAGE.NOT_FOUND_FIELD_OF_OBJECT,
                            booking.getCreatedUserId() + "",
                            Constant.TABLE.USER
                    )
            );


        //</editor-fold>
        Booking bookingNew = bookingRepository.save(booking);
        BookingLog bookingLog = new BookingLog();
        bookingLog.setBookingId(bookingNew.getId());
        bookingLog.setStatusNew(0);
        bookingLog.setStatusNew(bookingNew.getStatus());
        bookingLog.setReason("booking");
        bookingLog.setUserId(bookingNew.getCreatedUserId());
        bookingLogRepository.save(bookingLog);
        return bookingNew;
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
    public Booking update(long id, Booking booking) throws Exception {
        return bookingRepository.save(booking);
    }

    @Override
    public Response update(long id, int status, long userId, String reason) throws Exception {
        //<editor-fold defaultstate="collapsed" desc="Validate inut">
        Booking booking = bookingRepository.findOne(id);
        if (booking == null)
            throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(TextConstant.MESSAGE.NOT_FOUND, Constant.TABLE.BOOKING));
        else {
            int oldStatus = booking.getStatus();
            if (status == oldStatus)
                throw new CommonException(Response.BAD_REQUEST,
                        MessageCommon.getMessage(
                                TextConstant.MESSAGE.INVALID_FIELD_OF_OBJECT,
                                Constant.PARAMS.CODE.STATUS,
                                Constant.TABLE.BOOKING
                        )
                );
        }
        //Validate match day
        if (booking.getMatchDay().before(new Date()))
            throw new CommonException(Response.BAD_REQUEST,
                    MessageCommon.getMessage(
                            TextConstant.MESSAGE.INVALID,
                            Constant.TABLE.BOOKING
                    )
            );
        //Validate
        //</editor-fold>
        return updateBooking(id, status, userId, reason);
    }

    public Response updateBooking(long id, int status, long userId, String reason) throws Exception {
        Connection connection = dataSource.getConnection();
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall("{call pro_update_booking (?, ?, ?, ?, ?)}");
            int i = 1;
            ConnectionCommon.doSetLongParams(cs, i++, id);
            ConnectionCommon.doSetIntParams(cs, i++, status);
            ConnectionCommon.doSetLongParams(cs, i++, userId);
            ConnectionCommon.doSetStringParams(cs, i++, reason);
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
