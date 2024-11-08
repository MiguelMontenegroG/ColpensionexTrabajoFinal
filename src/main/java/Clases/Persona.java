package Clases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;

public class Persona {

    private String nombre;
    private String apellido;
    private String fecha;
    private String celular;
    private String id;
    private String correo;
    private boolean listaNegra;
    private boolean prePensionado;
    private boolean institucionPublica;
    private String nombreInstitucion;
    private boolean observacionesDisciplinarias;
    private boolean tieneFamiliaPolicia;
    private boolean tieneHijosInpec;
    private boolean condecoracion;
    private String paisNacimiento;
    private String ciudadNacimiento;
    private String ciudadResidencia;
    private int edad;
    private String fechaNacimiento;
    private String fondoPension;
    private int semanasCotizadas;
    private boolean fondoExtranjero;
    private Caracterizacion caracterizacion; // Este es un enum, no un booleano

    // Constructor
    public Persona(String nombre, String apellido, String fecha,String celular, String id, String correo,
                   boolean listaNegra, boolean prePensionado, boolean institucionPublica, String nombreInstitucion,
                   boolean observacionesDisciplinarias, boolean tieneFamiliaPolicia, boolean tieneHijosInpec,
                   boolean condecoracion, String paisNacimiento, String ciudadNacimiento, String ciudadResidencia,
                   int edad, String fechaNacimiento, String fondoPension, int semanasCotizadas, boolean fondoExtranjero, Caracterizacion caracterizacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecha = fecha;
        this.celular = celular;
        this.id = id;
        this.correo = correo;
        this.listaNegra = listaNegra;
        this.prePensionado = prePensionado;
        this.institucionPublica = institucionPublica;
        this.nombreInstitucion = nombreInstitucion;
        this.observacionesDisciplinarias = observacionesDisciplinarias;
        this.tieneFamiliaPolicia = tieneFamiliaPolicia;
        this.tieneHijosInpec = tieneHijosInpec;
        this.condecoracion = condecoracion;
        this.paisNacimiento = paisNacimiento;
        this.ciudadNacimiento = ciudadNacimiento;
        this.ciudadResidencia = ciudadResidencia;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.fondoPension = fondoPension;
        this.semanasCotizadas = semanasCotizadas;
        this.fondoExtranjero = fondoExtranjero;
        this.caracterizacion = caracterizacion; // Asignamos el valor del enum
    }

    public void evaluarCaracterizacion() {
        // 1. Verificar si ha estado en lista negra
        if (listaNegra) {
            this.caracterizacion = Caracterizacion.RECHAZADO;
            return;
        }

        // 2. Verificar si es pre-pensionado
        if (prePensionado) {
            this.caracterizacion = Caracterizacion.RECHAZADO;
            return;
        }

        // 3. Evaluar institución pública
        if (institucionPublica) {
            switch (nombreInstitucion) {
                case "Mininterior":
                case "Minsalud":
                    if (observacionesDisciplinarias) {
                        listaNegra = true;
                        this.caracterizacion = Caracterizacion.RECHAZADO;
                    } else {
                        this.caracterizacion = Caracterizacion.APROBADO;
                    }
                    return;

                case "Policía":
                    if (tieneFamiliaPolicia) {
                        if (edad < 18) {
                            this.caracterizacion = Caracterizacion.INHABILITADO;
                        } else {
                            this.caracterizacion = Caracterizacion.APROBADO;
                        }
                    } else {
                        this.caracterizacion = Caracterizacion.APROBADO;
                    }
                    return;

                case "Inpec":
                    if (tieneHijosInpec || condecoracion) {
                        this.caracterizacion = Caracterizacion.APROBADO;
                    } else {
                        this.caracterizacion = Caracterizacion.INHABILITADO;
                    }
                    return;

                case "Armada":
                    if (condecoracion) {
                        this.caracterizacion = Caracterizacion.APROBADO;
                    } else {
                        this.caracterizacion = Caracterizacion.INHABILITADO;
                    }
                    return;
            }
        }

        // Si no pertenece a una institución pública, es civil
        boolean ciudadEspecial = ciudadNacimiento.equalsIgnoreCase("Bogotá") ||
                ciudadNacimiento.equalsIgnoreCase("Medellín") ||
                ciudadNacimiento.equalsIgnoreCase("Cali");
        boolean paisTerminaEnTan = paisNacimiento.toLowerCase().endsWith("tan");

        // 4. Verificar lugar de nacimiento
        if (ciudadEspecial || paisTerminaEnTan) {
            this.caracterizacion = Caracterizacion.RECHAZADO;
            return;
        }

        // 5. Verificar edad
        if (edad > 55) {
            this.caracterizacion = Caracterizacion.RECHAZADO;
            return;
        }

        // 6. Verificar fondo de pensión y semanas cotizadas
        switch (fondoPension) {
            case "Provenir":
                if (semanasCotizadas > 800) {
                    this.caracterizacion = Caracterizacion.EMBARGADO;
                    return;
                }
                break;
            case "Protección":
                if (semanasCotizadas > 590) {
                    this.caracterizacion = Caracterizacion.EMBARGADO;
                    return;
                }
                break;
            case "Colfondos":
                if (semanasCotizadas > 300) {
                    this.caracterizacion = Caracterizacion.EMBARGADO;
                    return;
                }
                break;
            case "Old Mutual":
                if (semanasCotizadas > 100) {
                    this.caracterizacion = Caracterizacion.EMBARGADO;
                    return;
                }
                break;
        }

        // 7. Verificar fondo extranjero
        if (fondoExtranjero) {
            this.caracterizacion = Caracterizacion.APROBADO;
            return;
        }

        // Si no se cumplen las condiciones anteriores, aprobar
        this.caracterizacion = Caracterizacion.APROBADO;

        int edadCalculada = calcularEdad();
        if (edadCalculada != this.edad) {
            this.caracterizacion = Caracterizacion.RECHAZADO;  // Si la edad no coincide, rechazamos la persona
        } else {
            this.caracterizacion = Caracterizacion.APROBADO;
        }
    }

    public Caracterizacion getCaracterizacion() {
        return caracterizacion;
    }

    // Otros getters y setters omitidos para simplicidad

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isListaNegra() {
        return listaNegra;
    }

    public void setListaNegra(boolean listaNegra) {
        this.listaNegra = listaNegra;
    }

    public boolean isPrePensionado() {
        return prePensionado;
    }

    public void setPrePensionado(boolean prePensionado) {
        this.prePensionado = prePensionado;
    }

    public boolean isInstitucionPublica() {
        return institucionPublica;
    }

    public void setInstitucionPublica(boolean institucionPublica) {
        this.institucionPublica = institucionPublica;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public boolean isObservacionesDisciplinarias() {
        return observacionesDisciplinarias;
    }

    public void setObservacionesDisciplinarias(boolean observacionesDisciplinarias) {
        this.observacionesDisciplinarias = observacionesDisciplinarias;
    }

    public boolean isTieneFamiliaPolicia() {
        return tieneFamiliaPolicia;
    }

    public void setTieneFamiliaPolicia(boolean tieneFamiliaPolicia) {
        this.tieneFamiliaPolicia = tieneFamiliaPolicia;
    }

    public boolean isTieneHijosInpec() {
        return tieneHijosInpec;
    }

    public void setTieneHijosInpec(boolean tieneHijosInpec) {
        this.tieneHijosInpec = tieneHijosInpec;
    }

    public boolean isCondecoracion() {
        return condecoracion;
    }

    public void setCondecoracion(boolean condecoracion) {
        this.condecoracion = condecoracion;
    }

    public String getPaisNacimiento() {
        return paisNacimiento;
    }

    public void setPaisNacimiento(String paisNacimiento) {
        this.paisNacimiento = paisNacimiento;
    }

    public String getCiudadNacimiento() {
        return ciudadNacimiento;
    }

    public void setCiudadNacimiento(String ciudadNacimiento) {
        this.ciudadNacimiento = ciudadNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFondoPension() {
        return fondoPension;
    }

    public void setFondoPension(String fondoPension) {
        this.fondoPension = fondoPension;
    }

    public int getSemanasCotizadas() {
        return semanasCotizadas;
    }

    public void setSemanasCotizadas(int semanasCotizadas) {
        this.semanasCotizadas = semanasCotizadas;
    }

    public boolean isFondoExtranjero() {
        return fondoExtranjero;
    }

    public void setFondoExtranjero(boolean fondoExtranjero) {
        this.fondoExtranjero = fondoExtranjero;
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public void setCaracterizacion(Caracterizacion caracterizacion) {
        this.caracterizacion = caracterizacion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Método para calcular la edad desde la fecha de nacimiento
    public int calcularEdad() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNacimientoLocal = LocalDate.parse(this.fechaNacimiento, formatter);
        LocalDate fechaHoy = LocalDate.now();
        Period periodo = Period.between(fechaNacimientoLocal, fechaHoy);
        return periodo.getYears();
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

