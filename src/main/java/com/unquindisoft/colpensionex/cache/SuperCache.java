package com.unquindisoft.colpensionex.cache;

import com.unquindisoft.colpensionex.model.Cotizante;
import com.unquindisoft.colpensionex.model.Solicitud;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SuperCache {
    private Map<String, Cotizante> cotizantesCache = new HashMap<>();
    private Map<String, Solicitud> solicitudesCache = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(SuperCache.class.getName());

    // Agregar cotizante al cache
    public void agregarCotizante(Cotizante cotizante) {
        cotizantesCache.put(cotizante.getNumeroDocumento(), cotizante);
        LOGGER.info("Cotizante agregado al cache: " + cotizante.getNumeroDocumento());
    }

    // Obtener cotizante desde el cache
    public Cotizante obtenerCotizante(String numeroDocumento) {
        return cotizantesCache.get(numeroDocumento);
    }

    // Agregar solicitud al cache
    public void agregarSolicitud(Solicitud solicitud) {
        solicitudesCache.put(solicitud.getId(), solicitud);
        LOGGER.info("Solicitud agregada al cache: " + solicitud.getId());
    }

    // Obtener solicitud desde el cache
    public Solicitud obtenerSolicitud(String id) {
        return solicitudesCache.get(id);
    }

    // Método para limpiar caché si se requiere
    public void limpiarCache() {
        cotizantesCache.clear();
        solicitudesCache.clear();
        LOGGER.info("Cache limpiado.");
    }
}
