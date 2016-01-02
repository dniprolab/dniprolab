package com.dniprolab.user.repo;

import com.dniprolab.user.entity.User;
import org.springframework.data.repository.Repository;

/**
 * Created by Overlord on 03.01.2016.
 */
public interface UserRepository extends Repository<User, Long> {

    User findUserByLogin(String login);

}
