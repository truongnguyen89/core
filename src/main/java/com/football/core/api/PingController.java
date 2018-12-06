package com.football.core.api;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 28-Nov-18
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("api/core/ping")
@RefreshScope
public class PingController {

    @RequestMapping(method = GET)
    public ResponseEntity<?> ping() throws Exception {
        return new ResponseEntity<Integer>(1, HttpStatus.OK);
    }
}
