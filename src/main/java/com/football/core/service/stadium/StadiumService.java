package com.football.core.service.stadium;

import com.football.common.model.stadium.Stadium;
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
public interface StadiumService {
    Stadium create(Stadium stadium) throws Exception;

    Stadium findById(Long id) throws Exception;

    List<Stadium> findByStatus(int status) throws Exception;

    Iterable<Stadium> findAll() throws Exception;

    Stadium update(Stadium stadium) throws Exception;
}
