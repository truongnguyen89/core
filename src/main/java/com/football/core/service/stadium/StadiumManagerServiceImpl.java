package com.football.core.service.stadium;

import com.football.core.repository.StadiumManagerRepository;
import com.football.core.repository.StadiumRepository;
import com.football.core.repository.UserRepository;
import com.football.core.service.BaseService;
import com.football.common.exception.CommonException;
import com.football.common.model.stadium.Stadium;
import com.football.common.model.stadium.StadiumManager;
import com.football.common.model.user.User;
import com.football.common.response.Response;
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
public class StadiumManagerServiceImpl extends BaseService implements StadiumManagerService {
    @Autowired
    StadiumManagerRepository stadiumManagerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StadiumRepository stadiumRepository;

    @Override
    public StadiumManager create(StadiumManager stadiumManager) throws Exception {
        if (stadiumManager.getUserId() == null) {
            if (stadiumManager.getUser() == null)
                throw new CommonException(Response.BAD_REQUEST, "Khong co thong tin user quan ly");
            else {
                User user = userRepository.save(stadiumManager.getUser());
                stadiumManager.setUserId(user.getId());
            }
        }

        if (stadiumManager.getStadiumId() == null) {
            if (stadiumManager.getStadium() == null)
                throw new CommonException(Response.BAD_REQUEST, "Khong co thong tin Stadium");
            else {
                Stadium stadium = stadiumRepository.save(stadiumManager.getStadium());
                stadiumManager.setStadiumId(stadium.getId());
            }
        }

        return stadiumManagerRepository.save(stadiumManager);
    }

    @Override
    public StadiumManager findById(Long id) throws Exception {
        return stadiumManagerRepository.findOne(id);
    }

    @Override
    public List<StadiumManager> findByStatus(int status) throws Exception {
        return stadiumManagerRepository.findByStatus(status);
    }

    @Override
    public Iterable<StadiumManager> findAll() throws Exception {
        return stadiumManagerRepository.findAll();
    }

    @Override
    public StadiumManager update(StadiumManager stadiumManager) throws Exception {
        return stadiumManagerRepository.save(stadiumManager);
    }
}
