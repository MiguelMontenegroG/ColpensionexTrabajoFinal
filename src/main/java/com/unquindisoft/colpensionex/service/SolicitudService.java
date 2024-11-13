package com.unquindisoft.colpensionex.service;

import com.unquindisoft.colpensionex.config.DirectoryManager;
import com.unquindisoft.colpensionex.model.Solicitud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolicitudService {
    private static final Logger LOGGER = Logger.getLogger(SolicitudService.class.getName());

    public void trasladarSolicitudes() {
        Path sourceDir = DirectoryManager.obtenerDirectorioEntrantes().toPath();
        Path targetDir = DirectoryManager.obtenerDirectorioProcesamiento().toPath();

        try {
            Files.list(sourceDir).forEach(file -> {
                try {
                    Files.move(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("Archivo movido a SolicitudesEnProcesamiento: " + file.getFileName());
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error al mover archivo: " + file.getFileName(), e);
                }
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al listar archivos en SolicitudesEntrantes", e);
        }
    }
}
