package com.football.core.repository;

import com.football.common.model.stadium.SubStadium;
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
public interface SubStadiumRepository extends CrudRepository<SubStadium, Long> {
    List<SubStadium> findByStatus(int status);

}
