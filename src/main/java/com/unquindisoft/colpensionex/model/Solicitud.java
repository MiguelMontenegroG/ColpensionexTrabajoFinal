package com.unquindisoft.colpensionex.model;

import java.time.LocalDate;

public class Solicitud {
    private String id;
    private Cotizante cotizante;
    private LocalDate fechaSolicitud;
    private int prioridad; // 1 para menores de 35, 2 para no obligados a declarar

    // Constructor
    public Solicitud(String id, Cotizante cotizante, LocalDate fechaSolicitud) {
        this.id = id;
        this.cotizante = cotizante;
        this.fechaSolicitud = fechaSolicitud;
        calcularPrioridad(); // Calcula prioridad al momento de crear la solicitud
    }

    // Getters y setters
    public String getId() { return id; }
    public Cotizante getCotizante() { return cotizante; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public int getPrioridad() { return prioridad; }

    // Calcula la prioridad basándose en los criterios definidos
    public void calcularPrioridad() {
        int edad = cotizante.calcularEdad();
        if (edad < 35) {
            this.prioridad = 1;
        } else if (!cotizante.isDebeDeclararRenta()) {
            this.prioridad = 2;
        } else {
            this.prioridad = 3;
        }
    }
}

