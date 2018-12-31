package com.football.core.service.match;

import com.football.common.model.match.Match;
import com.football.common.repository.MatchRepository;
import com.football.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MatchServiceImpl extends BaseService implements MatchService {
    @Autowired
    MatchRepository matchRepository;

    @Override
    public Match create(Match match) throws Exception {
        return matchRepository.save(match);
    }

    @Override
    public Match findById(Long id) throws Exception {
        return matchRepository.findOne(id);
    }

    @Override
    public List<Match> findByStatus(int status) throws Exception {
        return matchRepository.findByStatus(status);
    }

    @Override
    public Iterable<Match> findAll() throws Exception {
        return matchRepository.findAll();
    }

    @Override
    public Match update(Match match) throws Exception {
        return matchRepository.save(match);
    }
}
