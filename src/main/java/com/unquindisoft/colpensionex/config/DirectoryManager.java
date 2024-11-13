package com.unquindisoft.colpensionex.config;
import java.io.File;

public class DirectoryManager {
    private static final String SOLICITUDES_ENTRANTES = "SolicitudesEntrantes";
    private static final String SOLICITUDES_EN_PROCESAMIENTO = "SolicitudesEnProcesamiento";

    public static File obtenerDirectorioEntrantes() {
        return new File(SOLICITUDES_ENTRANTES);
    }

    public static File obtenerDirectorioProcesamiento() {
        return new File(SOLICITUDES_EN_PROCESAMIENTO);
    }

    public static void crearDirectorios() {
        File entrantes = obtenerDirectorioEntrantes();
        File procesamiento = obtenerDirectorioProcesamiento();

        if (!entrantes.exists()) {
            entrantes.mkdir();
        }
        if (!procesamiento.exists()) {
            procesamiento.mkdir();
        }
    }
}