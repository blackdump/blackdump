package com.github.blackdump.data;

import lombok.Data;

import java.io.Serializable;

/**
 * Classe serializzabile per salvare la configurazione
 */
@Data
public class BDConfig implements Serializable {

    private String modsDirectory;

    private String defaultTheme = "default.css";

    private String defaultBackground = "background2.jpg";
}
