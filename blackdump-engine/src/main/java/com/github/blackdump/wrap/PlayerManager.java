package com.github.blackdump.wrap;

import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.base.BaseManager;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.persistence.IBDDatabaseManager;
import com.github.blackdump.interfaces.wrap.IPlayerManager;
import com.github.blackdump.persistence.users.User;
import com.github.blackdump.session.SessionManager;
import lombok.Getter;
import lombok.Setter;

@ABDManager
public class PlayerManager extends BaseManager implements IPlayerManager {


    private IBDDatabaseManager databaseManager;

    @Getter @Setter
    private User currentUser;


    @Override
    public void start(IBlackdumpEngine engine) {
        super.start(engine);
        databaseManager = engine.getDatabaseManager();

        engine.subscribeEvent(EventBusMessages.CURRENT_USER_OBJECT_REQUEST, o -> {engine.broadcastEvent(EventBusMessages.CURRENT_USER_OBJECT_SEND, currentUser);});

    }
}
