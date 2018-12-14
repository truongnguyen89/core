package com.football.core.api;

import com.football.core.service.init.InitService;
import com.football.common.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 19-Jun-18
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "init")
public class InitController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CATEGORY);
    @Autowired
    InitService initService;


    @RequestMapping(method = GET)
    public ResponseEntity<?> init() throws Exception {
        initService.initCache();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
