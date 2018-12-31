package com.football.core.service.booking;

import com.football.common.cache.Cache;
import com.football.common.constant.Constant;
import com.football.common.database.ConnectionCommon;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.model.match.Match;
import com.football.common.model.stadium.Booking;
import com.football.common.model.stadium.BookingLog;
import com.football.common.model.user.User;
import com.football.common.repository.BookingLogRepository;
import com.football.common.repository.BookingRepository;
import com.football.common.repository.MatchRepository;
import com.football.common.repository.UserRepository;
import com.football.common.response.Response;
import com.football.common.util.Resource;
import com.football.core.component.NotificationAccess;
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

    @Autowired
    NotificationAccess notificationAccess;

    @Override
    public Booking create(Booking booking) throws Exception {
        //<editor-fold defaultstate="collapsed" desc="Validate inut">
        if (booking.getMatchDay().before(new Date()))
            throw new CommonException(Response.BAD_REQUEST,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND),
                            Constant.TABLE.BOOKING
                    )
            );
        else if (booking.getStatus() != Cache.getIntParamFromCache(Constant.PARAMS.TYPE.BOOKING, Constant.PARAMS.CODE.STATUS_NEW, 1))
            throw new CommonException(Response.BAD_REQUEST,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_AVAILABLE_FIELD_OF_OBJECT),
                            Constant.PARAMS.CODE.STATUS,
                            Constant.TABLE.BOOKING
                    )
            );
        //Validate player, create user, match
        User player = userRepository.findOne(booking.getPlayerId());
        if (player == null || player.getStatus() != Constant.STATUS_OBJECT.ACTIVE)
            throw new CommonException(Response.NOT_FOUND,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND_FIELD_OF_OBJECT),
                            booking.getPlayerId() + "",
                            Constant.TABLE.USER
                    )
            );
        User creater = userRepository.findOne(booking.getCreatedUserId());
        if (creater == null || creater.getStatus() != Constant.STATUS_OBJECT.ACTIVE)
            throw new CommonException(Response.NOT_FOUND,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND_FIELD_OF_OBJECT),
                            booking.getCreatedUserId() + "",
                            Constant.TABLE.USER
                    )
            );
        //Validate match
        Match match = matchRepository.findOne(booking.getMatchId());
        if (match == null)
            throw new CommonException(Response.NOT_FOUND,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND_FIELD_OF_OBJECT),
                            booking.getCreatedUserId() + "",
                            Constant.TABLE.USER
                    )
            );
        else if (match.getStatus() != Constant.MATCH.STATUS.FREE
                && match.getStatus() != Constant.MATCH.STATUS.WAITING_CONFIRM)
            throw new CommonException(Response.NOT_FOUND,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_AVAILABLE),
                            Constant.TABLE.MATCH
                    )
            );
        //</editor-fold>
        Booking bookingNew = bookingRepository.save(booking);
        bookingNew.setPlayer(player);
        bookingNew.setCreater(creater);
        bookingNew.setMatch(match);
        BookingLog bookingLog = new BookingLog();
        bookingLog.setBookingId(bookingNew.getId());
        bookingLog.setStatusOld(Constant.BOOKING.STATUS.NEW);
        bookingLog.setStatusNew(bookingNew.getStatus());
        bookingLog.setReason(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.BOOKING));
        bookingLog.setUserId(bookingNew.getCreatedUserId());
        bookingLogRepository.save(bookingLog);

        //Gui noti den cac quan ly san bong
        notificationAccess.sendNotificationToManagerWhenBooking(bookingNew);

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
        //validate user and permission
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.USER));
        else if (user.getStatus() != Constant.STATUS_OBJECT.ACTIVE)
            throw new CommonException(Response.INVALID_PERMISSION, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_AVAILABLE), Constant.TABLE.USER));
        else if (user.getType() == Constant.USER.TYPE.PLAYER
                && (status == Constant.BOOKING.STATUS.BOOKED
                || status == Constant.BOOKING.STATUS.REFUSE
                || status == Constant.BOOKING.STATUS.REJECT)
        )
            throw new CommonException(Response.INVALID_PERMISSION, "Người chơi không thể từ chối lịch đặt sân");
        else if (user.getType() == Constant.USER.TYPE.MANAGER
                && (status == Constant.BOOKING.STATUS.CANCEL)
        )
            throw new CommonException(Response.INVALID_PERMISSION, "Quản lý phải từ chối lịch đặt sân");
        //validate booking
        Booking booking = bookingRepository.findOne(id);
        if (booking == null)
            throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.BOOKING));
        else {
            int oldStatus = booking.getStatus();
            if (status == oldStatus)
                throw new CommonException(Response.BAD_REQUEST,
                        MessageCommon.getMessage(
                                Resource.getMessageResoudrce(Constant.RESOURCE.KEY.INVALID_FIELD_OF_OBJECT),
                                Constant.PARAMS.CODE.STATUS,
                                Constant.TABLE.BOOKING
                        )
                );
        }
        //Validate match day
        if (booking.getMatchDay().before(new Date()))
            throw new CommonException(Response.BAD_REQUEST,
                    MessageCommon.getMessage(
                            Resource.getMessageResoudrce(Constant.RESOURCE.KEY.INVALID),
                            Constant.TABLE.BOOKING
                    )
            );
        //Validate match
        Match match = matchRepository.findOne(booking.getMatchId());
        if (match == null)
            throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.MATCH));
        //</editor-fold>
        return updateBooking(booking, match, status, user, reason);
    }

    public Response updateBooking(Booking booking, Match match, int newStatus, User user, String reason) throws Exception {
        long userId = user.getId();
        if (newStatus == Constant.BOOKING.STATUS.BOOKED) {
            //get all booking other
            List<Booking> bookingList = bookingRepository.findByMatchId(booking.getMatchId());
            for (Booking bookingOther : bookingList) {
                if (bookingOther.getId() == booking.getId()) {
                    updateBooking(booking, newStatus, userId, reason);
                } else {
                    updateBooking(bookingOther, Constant.BOOKING.STATUS.REJECT, userId, "Lịch đặt sân bị từ chối do " + user.getName() + "(" + user.getEmail() + ") đã đồng ý cho người chơi khác");
                }
            }
        } else
            updateBooking(booking, newStatus, userId, reason);
        return Response.OK;
    }

    public void updateBooking(Booking booking, int newStatus, long userId, String reason) throws Exception {
        int oldStatus = booking.getStatus();
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        BookingLog bookingLog = new BookingLog();
        bookingLog.setBookingId(booking.getId());
        bookingLog.setStatusOld(oldStatus);
        bookingLog.setStatusNew(newStatus);
        bookingLog.setUserId(userId);
        bookingLog.setReason(reason);
        bookingLogRepository.save(bookingLog);
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
