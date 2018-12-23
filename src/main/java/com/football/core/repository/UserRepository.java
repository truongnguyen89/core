package com.football.core.repository;

import com.football.common.model.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * UserRepository: Truong Nguyen
 * Date: 26-Nov-18
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(
            value = "SELECT \n" +
                    "    u.*\n" +
                    "FROM\n" +
                    "    football.user u,\n" +
                    "    football.matchs m,\n" +
                    "    football.sub_stadium ss,\n" +
                    "    football.stadium_manager sm\n" +
                    "WHERE\n" +
                    "    m.n_sub_stadium_id = ss.n_id\n" +
                    "        AND ss.n_stadium_id = sm.n_stadium_id\n" +
                    "        AND sm.n_user_id = u.n_id\n" +
                    "        AND m.n_id = :matchId",
            nativeQuery = true)
    List<User> findManagerByMatch(@Param("matchId") long matchId);
}
