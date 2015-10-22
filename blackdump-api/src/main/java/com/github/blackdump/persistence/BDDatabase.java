package com.github.blackdump.persistence;

import com.github.blackdump.persistence.users.User;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * La classe che unisce tutti i tipi di oggetti
 */
@Data
public class BDDatabase implements Serializable {

    private Date creationDate = new Date();
    private Date lastModifiedDate = new Date();


    private List<User> users = new ArrayList<>();


}
