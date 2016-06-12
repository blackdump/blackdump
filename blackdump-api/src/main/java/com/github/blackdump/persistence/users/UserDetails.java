package com.github.blackdump.persistence.users;

import lombok.Data;

import java.io.Serializable;

/**
 * Dettagli dell'utente
 */
@Data
public class UserDetails implements Serializable {

    private double currentMoney = 100;
    private int rank;

}
