package com.football.core.repository;

import com.football.common.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : truongnq
 * @Date time: 2018-12-22 09:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface IDatabaseDAO {
    List<User> getListManagerFromMatch(long matchId) throws Exception;


}
