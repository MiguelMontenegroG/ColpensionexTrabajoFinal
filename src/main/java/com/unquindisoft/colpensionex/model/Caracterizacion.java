package com.unquindisoft.colpensionex.model;

public class Caracterizacion {
    private String tipoDocumento;
    private String documento;
    private String caracterizacion; // Puede ser "INHABILITAR" o "EMBARGAR"

    public Caracterizacion(String tipoDocumento, String documento, String caracterizacion) {
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.caracterizacion = caracterizacion;
    }

    public String getDocumento() {
        return documento;
    }

    public String getCaracterizacion() {
        return caracterizacion;
    }
}
