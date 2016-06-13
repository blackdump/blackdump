package com.github.blackdump.webserver.jpa.repositories;

import com.github.blackdump.webserver.jpa.entities.BDUser;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<BDUser, Long> {

    BDUser findBDUserByUsernameAndPassword(String username, String password);
}
