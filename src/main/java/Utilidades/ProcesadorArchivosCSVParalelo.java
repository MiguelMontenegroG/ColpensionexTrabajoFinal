package Utilidades;

import Utilidades.CSVReader;
import Clases.Persona;
import Utilidades.VerificarCiudad;
import Utilidades.ArchivoUtil;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//VERISON MAIN; PERO CON HILOS

public class ProcesadorArchivosCSVParalelo {

    private static final String CARPETA_ENTRADA = "src/main/resources/SolicitudesEnProcesamiento";
    private static final String RUTA_CIUDADES = "src/main/resources/BaseDeDatos/paises/colombia/municipio.csv";
    private static final int NUMERO_HILOS = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMERO_HILOS);

        try (DirectoryStream<Path> archivosCSV = Files.newDirectoryStream(Paths.get(CARPETA_ENTRADA), "*.csv")) {
            for (Path archivoCSV : archivosCSV) {
                executorService.execute(() -> procesarArchivo(archivoCSV));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    public static void procesarArchivo(Path archivoCSV) {
        String rutaCSV = archivoCSV.toString();
        String separador = ",";

        // Verificar el número de columnas
        int cantidadColumnas = ArchivoUtil.verificarColumnasCSV(rutaCSV);
        if (cantidadColumnas != 23) {
            System.out.println("Error: El archivo " + archivoCSV.getFileName() + " tiene un número incorrecto de columnas.");
            return;
        }

        try {
            // Leer personas desde el archivo CSV
            CSVReader lectorCSV = new CSVReader(rutaCSV, separador);
            List<Persona> personas = lectorCSV.leerArchivo();

            // Verificar formato de fechas
            boolean fechasCorrectas = personas.stream()
                    .allMatch(persona -> ArchivoUtil.verificarFecha(persona.getFecha()));
            if (!fechasCorrectas) {
                System.out.println("Error: Alguna fecha tiene un formato incorrecto en el archivo: " + archivoCSV.getFileName());
                return;
            }

            VerificarCiudad verificador = new VerificarCiudad(RUTA_CIUDADES);

            for (Persona persona : personas) {
                System.out.println("Procesando: " + persona.getNombre() + " " + persona.getApellido());

                // Verificar la ciudad
                verificador.verificarCiudad(persona);

                // Evaluar la caracterización
                persona.evaluarCaracterizacion();

                // Actualizar la caracterización en el archivo CSV
                actualizarCaracterizacionCSV(rutaCSV, persona);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarCaracterizacionCSV(String rutaCSV, Persona persona) {
        try {
            List<String> lineas = ArchivoUtil.leerArchivoBufferedReader(rutaCSV);

            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] datos = linea.split(",");

                if (datos[4].equals(persona.getId())) {  // Comparar el ID de la persona
                    datos[22] = persona.getCaracterizacion().toString();  // Asignar la caracterización actualizada

                    lineas.set(i, String.join(",", datos));
                    break;
                }
            }

            ArchivoUtil.escribirArchivoBufferedWriter(rutaCSV, lineas, false);  // Sobreescribir el archivo CSV

        } catch (IOException e) {
            System.err.println("Error al actualizar el archivo CSV: " + e.getMessage());
        }
    }
}
