package com.football.core.service.match;

import com.football.common.model.match.MatchSchedule;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MatchScheduleService {
    MatchSchedule create(MatchSchedule matchSchedule) throws Exception;

    MatchSchedule findById(Long id) throws Exception;

    List<MatchSchedule> findByStatus(int status) throws Exception;

    Iterable<MatchSchedule> findAll() throws Exception;

    MatchSchedule update(MatchSchedule matchSchedule) throws Exception;
}
