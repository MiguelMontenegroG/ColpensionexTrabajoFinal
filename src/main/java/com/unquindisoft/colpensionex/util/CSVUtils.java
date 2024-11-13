package com.unquindisoft.colpensionex.util;

import com.unquindisoft.colpensionex.exception.CSVFormatException;
import com.unquindisoft.colpensionex.model.Caracterizacion;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CSVUtils {
    public static List<Cotizante> leerCotizantes(String filePath) throws IOException, CSVFormatException {
        List<Cotizante> cotizantes = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 5) throw new CSVFormatException("Formato incorrecto en archivo de cotizantes");
                Cotizante cotizante = new Cotizante(values[0], values[1], values[2], values[3], Boolean.parseBoolean(values[4]));
                cotizantes.add(cotizante);
            }
        }
        return cotizantes;
    }

    public static List<Caracterizacion> leerCaracterizaciones(String filePath) throws IOException, CSVFormatException {
        List<Caracterizacion> caracterizaciones = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3) throw new CSVFormatException("Formato incorrecto en archivo de caracterizaciones");
                Caracterizacion caracterizacion = new Caracterizacion(values[0], values[1], values[2]);
                caracterizaciones.add(caracterizacion);
            }
        }
        return caracterizaciones;
    }
}
