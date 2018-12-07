package com.football.core.service.stadium;

import com.football.common.model.stadium.StadiumManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 04-Dec-18
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface StadiumManagerService {
    StadiumManager create(StadiumManager stadiumManager) throws Exception;

    StadiumManager findById(Long id) throws Exception;

    List<StadiumManager> findByStatus(int status) throws Exception;

    Iterable<StadiumManager> findAll() throws Exception;

    StadiumManager update(StadiumManager stadiumManager) throws Exception;
}
