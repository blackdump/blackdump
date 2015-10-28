package com.github.blackdump.data.network;

import com.github.blackdump.serializer.JsonSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * Messaggio di comunicazione serializzabile verso il server
 */
@Data
public class BlackdumpMessage implements Serializable {

    private int actionId;
    private Serializable message;


    public BlackdumpMessage()
    {

    }

    public BlackdumpMessage(int actionId, Serializable message)
    {
        this.actionId = actionId;
        this.message = message;

    }


    /**
     * Function helper per la costruzione dei messaggi e la serializazione automatica
     * @param actionId
     * @param message
     * @return
     */
    public static String build(int actionId, Serializable message)
    {
        BlackdumpMessage msg = new BlackdumpMessage();
        msg.setActionId(actionId);
        msg.setMessage(message);

        return JsonSerializer.Serialize(msg);
    }


    /**
     * Parse del messaggio e deserializzazione automatica
     * @param message
     * @return
     */
    public static BlackdumpMessage parse(String message)
    {

        return JsonSerializer.Deserialize(message, BlackdumpMessage.class);
    }

}
