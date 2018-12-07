package com.football.core.service.stadium;

import com.football.common.model.stadium.SubStadium;
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
public interface SubStadiumService {
    SubStadium create(SubStadium subStadium) throws Exception;

    SubStadium findById(Long id) throws Exception;

    List<SubStadium> findByStatus(int status) throws Exception;

    Iterable<SubStadium> findAll() throws Exception;

    SubStadium update(SubStadium subStadium) throws Exception;
}
