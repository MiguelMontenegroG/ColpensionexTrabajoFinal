package com.unquindisoft.colpensionex.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logger {

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    static {
        try {
            // Configuración del archivo de log
            FileHandler fileHandler = new FileHandler("logs/util.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.INFO); // Puedes cambiar el nivel aquí (e.g., Level.SEVERE, Level.WARNING)
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al configurar el logger de util", e);
        }
    }

    // Método para obtener el logger
    public static Logger getLogger() {
        return LOGGER;
    }
}
