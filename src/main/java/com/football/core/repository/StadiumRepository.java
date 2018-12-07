package com.football.core.repository;

import com.football.common.model.stadium.Stadium;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 04-Dec-18
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface StadiumRepository extends CrudRepository<Stadium, Long> {
    List<Stadium> findByStatus(int status);

}
