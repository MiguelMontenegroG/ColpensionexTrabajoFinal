package com.unquindisoft.colpensionex.util;


import com.unquindisoft.colpensionex.exception.ArchivoInvalidoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidadorCSV {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Validar la estructura del archivo CSV
    public static void validarEstructura(String[] campos, int numColumnas) throws ArchivoInvalidoException {
        if (campos.length != numColumnas) {
            throw new ArchivoInvalidoException("La cantidad de columnas no coincide.");
        }
    }

    // Validar el formato de fecha
    public static void validarFecha(String fecha) throws ArchivoInvalidoException {
        try {
            LocalDate.parse(fecha, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new ArchivoInvalidoException("Formato de fecha inválido. Se espera YYYY-MM-DD.");
        }
    }
}

