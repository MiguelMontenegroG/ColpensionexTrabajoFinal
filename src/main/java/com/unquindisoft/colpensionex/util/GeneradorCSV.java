package com.unquindisoft.colpensionex.util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeneradorCSV {
    public static void main(String[] args) {
        String inputPath = "src/main/resources/SolicitudesEnProcesamiento/original.csv";
        String outputPath = "src/main/resources/SolicitudesEnProcesamiento/randomized.csv";

        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath));
            if (lines.isEmpty()) {
                System.out.println("El archivo está vacío.");
                return;
            }

            // Leer encabezados
            String header = lines.get(0);
            List<String> randomizedLines = new ArrayList<>();
            randomizedLines.add(header);

            // Generar datos aleatorios
            Random random = new Random();
            for (int i = 1; i < lines.size(); i++) {
                String[] fields = lines.get(i).split(",");

                // Generar datos aleatorios
                String nombre = getRandomNombre();
                String apellido = getRandomApellido();
                String fecha = getRandomDate(1950, 2024);
                String celular = "300" + (random.nextInt(9000000) + 1000000);
                String id = String.valueOf(100000000 + random.nextInt(900000000));
                String correo = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@example.com";
                String listaNegra = random.nextBoolean() ? "Sí" : "No";
                String prePensionado = random.nextBoolean() ? "Sí" : "No";
                String institucionPublica = random.nextBoolean() ? "Sí" : "No";
                String nombreInstitucion = getRandomInstitucion();
                String observaciones = random.nextBoolean() ? "Sí" : "No";
                String tieneFamiliaPolicia = random.nextBoolean() ? "Sí" : "No";
                String tieneHijosInpec = random.nextBoolean() ? "Sí" : "No";
                String condecoracion = random.nextBoolean() ? "Sí" : "No";
                String paisNacimiento = "Colombia";
                String ciudadNacimiento = getRandomCiudad();
                String ciudadResidencia = getRandomCiudad();
                String edad = String.valueOf(random.nextInt(60) + 18);
                String fechaNacimiento = getFechaNacimiento(Integer.parseInt(edad));
                String fondoPension = getRandomFondoPension();
                String semanasCotizadas = String.valueOf(random.nextInt(800) + 1);
                String fondoExtranjero = random.nextBoolean() ? "Sí" : "No";
                String caracterizacion = getRandomCaracterizacion();

                String randomizedLine = String.join(",",
                        nombre, apellido, fecha, celular, id, correo, listaNegra, prePensionado,
                        institucionPublica, nombreInstitucion, observaciones, tieneFamiliaPolicia,
                        tieneHijosInpec, condecoracion, paisNacimiento, ciudadNacimiento,
                        ciudadResidencia, edad, fechaNacimiento, fondoPension, semanasCotizadas,
                        fondoExtranjero, caracterizacion);
                randomizedLines.add(randomizedLine);
            }

            Files.write(Paths.get(outputPath), randomizedLines);
            System.out.println("Archivo aleatorizado guardado en: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomNombre() {
        String[] nombres = {"Juan", "María", "Carlos", "Ana", "Luis", "Sofía", "Miguel", "Camila"};
        return nombres[new Random().nextInt(nombres.length)];
    }

    private static String getRandomApellido() {
        String[] apellidos = {"Pérez", "Gómez", "Rodríguez", "Martínez", "López", "García", "Hernández", "Ramírez"};
        return apellidos[new Random().nextInt(apellidos.length)];
    }

    private static String getRandomDate(int startYear, int endYear) {
        Random random = new Random();
        int year = random.nextInt(endYear - startYear + 1) + startYear;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return LocalDate.of(year, month, day).toString();
    }

    private static String getRandomInstitucion() {
        String[] instituciones = {"Mininterior", "Ministerio de Educación", "Secretaría de Salud", "ICBF"};
        return instituciones[new Random().nextInt(instituciones.length)];
    }

    private static String getRandomCiudad() {
        String[] ciudades = {"Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Bucaramanga"};
        return ciudades[new Random().nextInt(ciudades.length)];
    }

    private static String getFechaNacimiento(int edad) {
        int year = LocalDate.now().getYear() - edad;
        return getRandomDate(year, year);
    }

    private static String getRandomFondoPension() {
        String[] fondos = {"Provenir", "Colpensiones", "FondoPrivado"};
        return fondos[new Random().nextInt(fondos.length)];
    }

    private static String getRandomCaracterizacion() {
        String[] caracterizaciones = {"APROBADO", "RECHAZADO", "INHABILITADO", "EMBARGADO", "NULL"};
        return caracterizaciones[new Random().nextInt(caracterizaciones.length)];
    }
}
