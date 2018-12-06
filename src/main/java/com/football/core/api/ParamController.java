package com.football.core.api;

import com.football.core.service.param.ParamService;
import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.model.param.Param;
import com.football.common.model.param.ParamKey;
import com.football.common.util.StringCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 17-May-18
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "/api/core/param")
public class ParamController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CATEGORY);
    @Autowired
    ParamService paramService;

    @RequestMapping(method = POST)
    @ResponseBody
    public ResponseEntity<?> create(@Valid @RequestBody Param param) throws Exception {
        try {
            return new ResponseEntity<Param>(paramService.create(param), HttpStatus.CREATED);
        } catch (CommonException e) {
            return new ResponseEntity<>(e.toString(), e.getResponse().getStatus());
        }
    }

    @RequestMapping(path = "/type/{type}/code/{code}", method = GET)
    public ResponseEntity<?> findById(@PathVariable String type, @PathVariable String code) throws Exception {
        return new ResponseEntity<Param>(paramService.findById(type, code), HttpStatus.OK);
    }

    @RequestMapping(path = "/status/{status}", method = GET)
    public ResponseEntity<?> findByStatus(@PathVariable int status) throws Exception {
        return new ResponseEntity<List<Param>>(paramService.findByStatus(status), HttpStatus.OK);
    }

    @RequestMapping(method = GET)
    public ResponseEntity<?> findAll() throws Exception {
        return new ResponseEntity<Iterable<Param>>(paramService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/type/{type}/code/{code}", method = PUT)
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable String type, @PathVariable String code, @RequestBody Param param) throws Exception {
        param.setParamKey(new ParamKey(
                !StringCommon.isNullOrBlank(type) ? type.trim().toUpperCase() : "",
                !StringCommon.isNullOrBlank(code) ? code.trim().toUpperCase() : ""));
        try {
            return new ResponseEntity<Param>(paramService.update(param), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(e.toString(), e.getResponse().getStatus());
        }
    }
}
