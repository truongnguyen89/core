package com.football.core.component;

import com.football.common.cache.Cache;
import com.football.common.constant.Constant;
import com.football.common.feign.NotificationServiceFeignAPI;
import com.football.common.message.MessageCommon;
import com.football.common.model.stadium.Booking;
import com.football.common.model.user.User;
import com.football.common.repository.UserRepository;
import com.football.common.util.ArrayListCommon;
import com.football.common.util.JsonCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NotificationAccess {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.DB);

    @Autowired
    NotificationServiceFeignAPI notificationServiceFeignAPI;

    @Autowired
    UserRepository userRepository;

    ExecutorService executorService;
    private int numThreads = 10;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void sendNotificationToManagerWhenBooking(Booking booking) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<User> userList = userRepository.findManagerByMatch(booking.getMatchId());
                    if (!ArrayListCommon.isNullOrEmpty(userList))
                        for (User user : userList) {
                            String title = Cache.getValueParamFromCache(Constant.PARAMS.TYPE.NOTIFICATION_TO_MANAGER_WHEN_BOOKING, Constant.PARAMS.CODE.TITLE);
                            title = MessageCommon.getMessage(title, booking.getId() + "");
                            String content = Cache.getValueParamFromCache(Constant.PARAMS.TYPE.NOTIFICATION_TO_MANAGER_WHEN_BOOKING, Constant.PARAMS.CODE.CONTENT);
                            String[] params = {
                                    JsonCommon.objectToJsonLog(booking.getPlayer()),
                                    JsonCommon.objectToJsonLog(booking.getMatch()),
                                    JsonCommon.objectToJsonLog(booking.getCreater())
                            };
                            content = MessageCommon.getMessage(content, params);
                            int type = Constant.NOTIFICATION.TYPE.NORMAL;
                            notificationServiceFeignAPI.create(title, content, type, user.getId());
                        }
                } catch (Exception e) {
                    LOGGER.error("Exception sendNotificationToManagerWhenBooking ", e);
                }
            }
        });
    }
}
