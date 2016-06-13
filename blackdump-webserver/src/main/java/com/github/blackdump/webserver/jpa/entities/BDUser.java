package com.github.blackdump.webserver.jpa.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class BDUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(min = 1, max = 50)
    private String username;

    @Length(min = 1, max=100)
    private String password;

    private Date creationDate;

    private Date modifiedDate;

    private Date lastLoginDate;

    private boolean online;

    private boolean invisible;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private BDUserDetail userDetail;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private BDSession session;
}
