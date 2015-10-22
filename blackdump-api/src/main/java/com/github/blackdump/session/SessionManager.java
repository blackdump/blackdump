package com.github.blackdump.session;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.persistence.users.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe creata per permettere ai componenti JavaFX di accedere in
 * modo statico all'engine, in quanto non e' possibile creare una classe
 * base per le finestre
 */

public class SessionManager {
    @Getter @Setter
    private static IBlackdumpEngine engine;

    @Getter
    @Setter
    private static User currentUser;



}
