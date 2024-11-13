package com.unquindisoft.colpensionex.model;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Cotizante {
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private boolean debeDeclararRenta;
    private String estado = "GENERADA"; // Estado inicial para cada solicitud

    public Cotizante(String tipoDocumento, String numeroDocumento, String nombreCompleto, String fechaNacimientoStr, boolean debeDeclararRenta) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreCompleto = nombreCompleto;

        // Convertir la cadena de fecha a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);

        this.debeDeclararRenta = debeDeclararRenta;
    }

    // Getters y setters
    public String getTipoDocumento() { return tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public boolean isDebeDeclararRenta() { return debeDeclararRenta; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para calcular la edad
    public int calcularEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
