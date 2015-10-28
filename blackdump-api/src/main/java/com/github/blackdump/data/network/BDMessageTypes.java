package com.github.blackdump.data.network;

import com.github.blackdump.persistence.users.User;

/**
 * Classe contenitore per i tipi di messaggi
 */
public class BDMessageTypes {


    public static int ERROR = -1;

    public static int LOGIN_REQUEST = 0;
    public static int LOGIN_RESPONSE = 1;


    /**
     * Funzione helper la construzione del login
     * @param user
     * @return
     */
    public static String buildLoginRequestMessage(User user)
    {
        return BlackdumpMessage.build(LOGIN_REQUEST, user);
    }


    /**
     * Costruisce il messaggio di eccezione
     * @param ex
     * @return
     */
    public static String buildErrorMessage(Exception ex)
    {
        return buildErrorMessage(ex.getMessage());
    }

    /**
     * Costruisce il messaggio di errore
     * @param text
     * @return
     */
    public static String buildErrorMessage(String text)
    {
        return BlackdumpMessage.build(ERROR, text);
    }
}
