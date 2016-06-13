package com.github.blackdump.webserver.jpa.entities;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class BDSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String uuid;

    @OneToOne
    private BDUser user;

    private Date creationDate;
}
