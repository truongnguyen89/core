package com.football.core.api;

import com.football.common.constant.Constant;
import com.football.common.util.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 28-Nov-18
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/api/core/user")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CATEGORY);

    @RequestMapping(path = "/demobus", method = GET)
    public ResponseEntity<?> getAbc(
            @RequestParam(value = "value", required = false, defaultValue = "value = ") String value) throws Exception {
        return new ResponseEntity<String>(value + Common.getAuditNumber(), HttpStatus.OK);
    }
}
