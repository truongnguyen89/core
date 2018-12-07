package com.football.core.service.match;

import com.football.core.repository.MatchScheduleRepository;
import com.football.core.service.BaseService;
import com.football.common.model.match.MatchSchedule;
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
public class MatchScheduleServiceImpl extends BaseService implements MatchScheduleService {
    @Autowired
    MatchScheduleRepository matchScheduleRepository;

    @Override
    public MatchSchedule create(MatchSchedule matchSchedule) throws Exception {
        return matchScheduleRepository.save(matchSchedule);
    }

    @Override
    public MatchSchedule findById(Long id) throws Exception {
        return matchScheduleRepository.findOne(id);
    }

    @Override
    public List<MatchSchedule> findByStatus(int status) throws Exception {
        return matchScheduleRepository.findByStatus(status);
    }

    @Override
    public Iterable<MatchSchedule> findAll() throws Exception {
        return matchScheduleRepository.findAll();
    }

    @Override
    public MatchSchedule update(MatchSchedule matchSchedule) throws Exception {
        return matchScheduleRepository.save(matchSchedule);
    }
}
