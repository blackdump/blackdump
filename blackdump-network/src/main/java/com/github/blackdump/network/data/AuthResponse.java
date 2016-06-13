package com.github.blackdump.network.data;

import lombok.Data;

import java.io.Serializable;

@Data

public class AuthResponse implements Serializable {
    public enum AuthResponseType {
        OK,
        ERROR
    }

    private AuthResponseType type;

    private String sessionId;
    private String error;


}
