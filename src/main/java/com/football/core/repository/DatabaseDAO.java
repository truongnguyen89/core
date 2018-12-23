package com.football.core.repository;

import com.football.common.constant.Constant;
import com.football.common.database.ConnectionCommon;
import com.football.common.model.user.User;
import com.football.common.util.JsonCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : truongnq
 * @Date time: 2018-12-22 09:51
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DatabaseDAO implements IDatabaseDAO {

    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.DB);

    @Autowired
    DataSource dataSource;

    /**
     * Funtion trong mysql khong the return ra cursor nen bo function nay, se edit code de dung viec khac
     * @param matchId
     * @return
     * @throws Exception
     */
    @Override
    public List<User> getListManagerFromMatch(long matchId) throws Exception {
        Connection connection = null;
        long id = System.currentTimeMillis();
        LOGGER.info("[B][" + matchId + "] DatabaseDAO.getListManagerFromMatch " + Constant.PROCEDURES.GET_PC_CODE_AND_REGION_ID);
        List<User> userList = new ArrayList<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            cs = connection.prepareCall(Constant.PROCEDURES.GET_PC_CODE_AND_REGION_ID);
            cs.registerOutParameter(1, Types.REF_CURSOR);
            ConnectionCommon.doSetLongParams(cs, 2, matchId);
            cs.execute();
            rs = (ResultSet) cs.getObject(1);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("n_id"));
                user.setName(rs.getString("s_name"));
                user.setAddress(rs.getString("s_address"));

                userList.add(user);
            }
        } catch (Exception e) {
            LOGGER.error("Exception when DatabaseDAO.getListManagerFromMatch ", e);
        } finally {
            ConnectionCommon.close(rs, cs);
            ConnectionCommon.close(connection);
            LOGGER.info("[E][" + matchId + "][Duration = " + (System.currentTimeMillis() - id) + "] DatabaseDAO.getListManagerFromMatch " + JsonCommon.objectToJsonNotNull(userList));
        }
        return userList;
    }
}
