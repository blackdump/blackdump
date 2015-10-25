package com.github.blackdump.persistence.users;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Classe utente
 */
@Data
public class User implements Serializable {

    private String uuid;

    private String nickname;

    private String password;

    private String email;

    private Date registrationDate;

    private Date lastLoginDate;

    private UserDetails userDetails = new UserDetails();




    /**
     * Restituisce un nuovo utente
     * @param nickname
     * @param email
     * @param password
     * @return
     */
    public static User newUser(String nickname, String email, String password)
    {
        User user = new User();

        user.setUuid(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setLastLoginDate(new Date());
        user.setRegistrationDate(new Date());

        return user;

    }
}
