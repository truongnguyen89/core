package com.football.core.service.match;

import com.football.common.model.match.Match;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MatchService {
    Match create(Match match) throws Exception;

    Match findById(Long id) throws Exception;

    List<Match> findByStatus(int status) throws Exception;

    Iterable<Match> findAll() throws Exception;

    Match update(Match match) throws Exception;
}
