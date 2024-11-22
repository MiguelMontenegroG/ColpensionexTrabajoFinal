package com.unquindisoft.colpensionex.util;

import com.unquindisoft.colpensionex.model.Cotizante;
import com.unquindisoft.colpensionex.model.Caracterizacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private String rutaArchivo;
    private String separador;

    // Constructor
    public CSVReader(String rutaArchivo, String separador) {
        this.rutaArchivo = rutaArchivo;
        this.separador = separador;
    }

    // Método para leer el archivo CSV y convertirlo en una lista de objetos Persona
    public List<Cotizante> leerArchivo() {
        List<Cotizante> personas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            // Leer línea por línea
            while ((linea = br.readLine()) != null) {
                // Saltar la cabecera
                if (linea.startsWith("Nombre")) {
                    continue;
                }

                // Separar los valores de la línea por el separador
                String[] valores = linea.split(separador);

                // Asignar cada valor a las variables correspondientes
                String nombre = valores[0];
                String apellido = valores[1];
                String fecha = valores[2];
                String celular = valores[3];
                String id = valores[4];
                String correo = valores[5];
                // Ahora listaNegra es un String, no un booleano
                String listaNegra = valores[6];  // Asignar directamente el valor del CSV
                boolean prePensionado = valores[7].equalsIgnoreCase("Sí");
                boolean institucionPublica = valores[8].equalsIgnoreCase("Sí");
                String nombreInstitucion = valores[9];
                boolean observacionesDisciplinarias = valores[10].equalsIgnoreCase("Sí");
                boolean tieneFamiliaPolicia = valores[11].equalsIgnoreCase("Sí");
                boolean tieneHijosInpec = valores[12].equalsIgnoreCase("Sí");
                boolean condecoracion = valores[13].equalsIgnoreCase("Sí");
                String paisNacimiento = valores[14];
                String ciudadNacimiento = valores[15];
                String ciudadResidencia = valores[16];
                int edad = Integer.parseInt(valores[17]);
                String fechaNacimiento = valores[18];
                String fondoPension = valores[19];
                int semanasCotizadas = Integer.parseInt(valores[20]);
                boolean fondoExtranjero = valores[21].equalsIgnoreCase("Sí");

                // Obtener la caracterización desde el archivo CSV
                Caracterizacion caracterizacion = Caracterizacion.valueOf(valores[22].toUpperCase());

                // Crear el objeto Persona y añadirlo a la lista
                Cotizante persona = new Cotizante(nombre, apellido, fecha, celular, id, correo, listaNegra, prePensionado, institucionPublica,
                        nombreInstitucion, observacionesDisciplinarias, tieneFamiliaPolicia, tieneHijosInpec, condecoracion,
                        paisNacimiento, ciudadNacimiento, ciudadResidencia, edad, fechaNacimiento, fondoPension, semanasCotizadas,
                        fondoExtranjero, caracterizacion);
                personas.add(persona);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personas;
    }
}
