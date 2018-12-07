package com.football.core.service.stadium;


import com.football.core.repository.SubStadiumRepository;
import com.football.core.service.BaseService;
import com.football.common.model.stadium.SubStadium;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SubStadiumServiceImpl extends BaseService implements SubStadiumService {
    @Autowired
    SubStadiumRepository subStadiumRepository;

    @Override
    public SubStadium create(SubStadium subStadium) throws Exception {
        return subStadiumRepository.save(subStadium);
    }

    @Override
    public SubStadium findById(Long id) throws Exception {
        return subStadiumRepository.findOne(id);
    }

    @Override
    public List<SubStadium> findByStatus(int status) throws Exception {
        return subStadiumRepository.findByStatus(status);
    }

    @Override
    public Iterable<SubStadium> findAll() throws Exception {
        return subStadiumRepository.findAll();
    }

    @Override
    public SubStadium update(SubStadium subStadium) throws Exception {
        return subStadiumRepository.save(subStadium);
    }
}

