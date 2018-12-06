package com.football.core.api;

import com.football.common.constant.Constant;
import com.football.common.model.stadium.Booking;
import com.football.common.model.stadium.Stadium;
import com.football.common.response.Response;
import com.football.common.util.DateCommon;
import com.football.core.service.booking.BookingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/api/core/booking")
public class BookingController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CATEGORY);
    @Autowired
    BookingService bookingService;

    @RequestMapping(method = POST)
    @ResponseBody
    public ResponseEntity<?> booking(
            @RequestParam(value = "playerId", required = true) long playerId,
            @RequestParam(value = "matchId", required = true) long matchId,
            @RequestParam(value = "matchDay", required = true, defaultValue = "") @DateTimeFormat(pattern = Constant.DATE.FORMAT.SHORT_DATE) Date matchDay,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "createdUserId", required = true) long createdUserId,
            @RequestParam(value = "comment", required = false) String comment
    ) throws Exception {
        return new ResponseEntity<Response>(bookingService.booking(playerId, matchId, matchDay, type, createdUserId, comment), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = GET)
    public ResponseEntity<?> findById(
            @PathVariable long id) throws Exception {
        return new ResponseEntity<Booking>(bookingService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(path = "/status/{status}", method = GET)
    public ResponseEntity<?> findByStatus(@PathVariable int status) throws Exception {
        return new ResponseEntity<List<Booking>>(bookingService.findByStatus(status), HttpStatus.OK);
    }

    @RequestMapping(method = GET)
    public ResponseEntity<?> findAll() throws Exception {
        return new ResponseEntity<Iterable<Booking>>(bookingService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}", method = PUT)
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable long id,
                                    @Valid @RequestBody Booking booking) throws Exception {
        booking.setId(id);
        return new ResponseEntity<Booking>(bookingService.update(booking), HttpStatus.OK);
    }

}
