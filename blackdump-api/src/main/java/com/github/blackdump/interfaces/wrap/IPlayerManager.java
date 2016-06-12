package com.github.blackdump.interfaces.wrap;


import com.github.blackdump.persistence.users.User;

public interface IPlayerManager {

    void setCurrentUser(User user);

    User getCurrentUser();

}
