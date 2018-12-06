package com.football.core.service.param;

import com.football.common.model.param.Param;
import com.football.common.model.param.ParamKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface ParamRepository extends CrudRepository<Param, ParamKey> {
    List<Param> findByStatus(int status);
}
