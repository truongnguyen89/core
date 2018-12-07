package com.football.core.service.user;

import com.football.common.model.user.User;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * UserRepository: Truong Nguyen
 * Date: 26-Nov-18
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface UserService {
    User create(User user) throws Exception;

    Iterable<User> findAll() throws Exception;
}
