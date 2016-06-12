package com.github.blackdump.eventbus.data;

import lombok.Data;

import java.io.Serializable;


@Data
public class PlayerPropertyData implements Serializable {

    private String valueName;

    private Object value;
}
