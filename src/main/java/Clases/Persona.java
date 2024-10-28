package Clases;

public class Persona {
    private String nombre;
    private String apellido;
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
    private int edad;
    private String fondoPension;
    private int semanasCotizadas;
    private boolean fondoExtranjero;

    public Persona(String nombre, String apellido, String celular, String id, String correo,
                   boolean listaNegra, boolean prePensionado, boolean institucionPublica,
                   String nombreInstitucion, boolean observacionesDisciplinarias, boolean tieneFamiliaPolicia,
                   boolean tieneHijosInpec, boolean condecoracion, String paisNacimiento,
                   String ciudadNacimiento, int edad, String fondoPension, int semanasCotizadas,
                   boolean fondoExtranjero) {
        this.nombre = nombre;
        this.apellido = apellido;
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
        this.edad = edad;
        this.fondoPension = fondoPension;
        this.semanasCotizadas = semanasCotizadas;
        this.fondoExtranjero = fondoExtranjero;
    }

    // Método para evaluar los criterios de aceptación, rechazo, etc.
    public String evaluarCriterios() {
        // 1. Verificar si ha estado en lista negra
        if (listaNegra) {
            return "Rechazar";
        }

        // 2. Verificar si es pre-pensionado
        if (prePensionado) {
            return "Rechazar";
        }

        // 3. Evaluar institución pública
        if (institucionPublica) {
            switch (nombreInstitucion) {
                case "Mininterior":
                case "Minsalud":
                    if (observacionesDisciplinarias) {
                        listaNegra = true;
                        return "Rechazar y agregar a la lista negra";
                    }
                    return "Aprobar";

                case "Policía":
                    if (tieneFamiliaPolicia) {
                        if (edad < 18) {
                            return "Procesar como civil";
                        } else {
                            return "Aprobar";
                        }
                    }
                    return "Aprobar";

                case "Inpec":
                    if (tieneHijosInpec || condecoracion) {
                        return "Aprobar";
                    } else {
                        return "Procesar como civil";
                    }

                case "Armada":
                    if (condecoracion) {
                        return "Aprobar";
                    } else {
                        return "Procesar como civil";
                    }
            }
        }

        // Si no pertenece a una institución pública, es civil
        boolean ciudadEspecial = ciudadNacimiento.equalsIgnoreCase("Bogotá") ||
                ciudadNacimiento.equalsIgnoreCase("Medellín") ||
                ciudadNacimiento.equalsIgnoreCase("Cali");
        boolean paisTerminaEnTan = paisNacimiento.toLowerCase().endsWith("tan");

        // 4. Verificar lugar de nacimiento
        if (ciudadEspecial || paisTerminaEnTan) {
            return "Rechazar";
        }

        // 5. Verificar edad
        if (edad > 55) {
            return "Rechazar";
        }

        // 6. Verificar fondo de pensión y semanas cotizadas
        switch (fondoPension) {
            case "Provenir":
                if (semanasCotizadas > 800) return "Rechazar";
                break;
            case "Protección":
                if (semanasCotizadas > 590) return "Rechazar";
                break;
            case "Colfondos":
                if (semanasCotizadas > 300) return "Rechazar";
                break;
            case "Old Mutual":
                if (semanasCotizadas > 100) return "Rechazar";
                break;
        }

        // 7. Verificar fondo extranjero
        if (fondoExtranjero) {
            return "Aprobar";
        }

        // Si no se cumplen las condiciones anteriores, aprobar
        return "Aprobar";
    }

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
}
