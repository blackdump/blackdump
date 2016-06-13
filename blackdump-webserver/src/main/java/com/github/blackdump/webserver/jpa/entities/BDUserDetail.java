package com.github.blackdump.webserver.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class BDUserDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int rank;

    private int currentCredits;

    @OneToOne
    private BDUser user;


}
