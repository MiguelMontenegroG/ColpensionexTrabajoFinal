package com.unquindisoft.colpensionex.repository;

import com.unquindisoft.colpensionex.model.Caracterizacion;
import com.unquindisoft.colpensionex.model.Cotizante;

import java.util.ArrayList;
import java.util.List;

public class DataCache {
    private List<Cotizante> cotizantes = new ArrayList<>();
    private List<SolicitudTraslado> solicitudesTraslado = new ArrayList<>();
    private List<Caracterizacion> caracterizaciones = new ArrayList<>();

    public List<Cotizante> obtenerCotizantes() {
        return cotizantes;
    }

    public void agregarCotizante(Cotizante cotizante) {
        cotizantes.add(cotizante);
    }

    public List<SolicitudTraslado> obtenerSolicitudesTraslado() {
        return solicitudesTraslado;
    }

    public void agregarSolicitudTraslado(SolicitudTraslado solicitud) {
        solicitudesTraslado.add(solicitud);
    }

    public List<Caracterizacion> obtenerCaracterizaciones() {
        return caracterizaciones;
    }

    public void agregarCaracterizacion(Caracterizacion caracterizacion) {
        caracterizaciones.add(caracterizacion);
    }
}
