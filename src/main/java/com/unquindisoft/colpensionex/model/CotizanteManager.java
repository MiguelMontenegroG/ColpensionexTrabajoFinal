package com.unquindisoft.colpensionex.model;

import com.unquindisoft.colpensionex.util.ArchivoUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class CotizanteManager {

    public static void procesarCotizantes(List<Cotizante> cotizantes, String rutaCSV) {
        // Crear lista enlazada para inhabilitados y embargados
        LinkedList<Cotizante> inhabilitados = new LinkedList<>();
        LinkedList<Cotizante> embargados = new LinkedList<>();

        // Evaluar la caracterización de cada cotizante
        for (Cotizante cotizante : cotizantes) {
            cotizante.evaluarCaracterizacion(); // Evaluamos la caracterización

            // Clasificar cotizantes según su caracterización
            switch (cotizante.getCaracterizacion()) {
                case INHABILITADO:
                    inhabilitados.add(cotizante);  // Agregar a la lista de inhabilitados
                    break;
                case EMBARGADO:
                    embargados.add(cotizante);  // Agregar a la lista de embargados
                    break;
                default:
                    // No se hace nada con los otros estados
                    break;
            }
        }

        // Extraer solo el nombre del archivo sin la ruta completa
        String archivoNombre = new File(rutaCSV).getName();

        // Obtener la fecha actual
        String fechaHoy = LocalDate.now().toString(); // Ejemplo: 2024-11-22

        // Crear carpeta con la fecha de hoy antes de guardar los archivos
        String rutaContraloriaBase = "src/main/resources/data/contraloria/" + fechaHoy;
        String rutaFiscaliaBase = "src/main/resources/data/fiscalia/" + fechaHoy;
        String rutaProcuraduriaBase = "src/main/resources/data/procuraduria/" + fechaHoy;

        // Crear las carpetas si no existen
        new File(rutaContraloriaBase).mkdirs();
        new File(rutaFiscaliaBase).mkdirs();
        new File(rutaProcuraduriaBase).mkdirs();

        // Guardar los cotizantes inhabilitados en la ruta de Contraloría
        String rutaContraloria = rutaContraloriaBase + "/Inhabilitados_" + archivoNombre;
        guardarCSV(inhabilitados, "Inhabilitados.csv", rutaContraloria);

        // Guardar los cotizantes embargados en la ruta de Fiscalía
        String rutaFiscalia = rutaFiscaliaBase + "/Embargados_" + archivoNombre;
        guardarCSV(embargados, "Embargados.csv", rutaFiscalia);

        // Guardar los cotizantes embargados en la ruta de Procuraduría
        String rutaProcuraduria = rutaProcuraduriaBase + "/Embargados_" + archivoNombre;
        guardarCSV(embargados, "Embargados.csv", rutaProcuraduria);

        // Imprimir los resultados para verificar las listas
        System.out.println("Cotizantes Inhabilitados: " + inhabilitados.size());
        for (Cotizante cotizante : inhabilitados) {
            System.out.println(cotizante.getId() + " - " + cotizante.getNombre() + " " + cotizante.getApellido());
        }

        System.out.println("Cotizantes Embargados: " + embargados.size());
        for (Cotizante cotizante : embargados) {
            System.out.println(cotizante.getId() + " - " + cotizante.getNombre() + " " + cotizante.getApellido());
        }
    }

    // Método para guardar una lista de cotizantes en un archivo CSV
    private static void guardarCSV(LinkedList<Cotizante> listaCotizantes, String nombreArchivo, String rutaDestino) {
        // Definir la carpeta destino según la ruta proporcionada
        File carpeta = new File(rutaDestino);

        // Verificar si la carpeta existe, si no, crearla
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (FileWriter writer = new FileWriter(new File(carpeta, nombreArchivo))) {
            // Escribir el encabezado del CSV
            writer.append("ID,Nombre,Apellido,Caracterizacion\n");

            // Escribir los datos de los cotizantes
            for (Cotizante cotizante : listaCotizantes) {
                writer.append(cotizante.getId()).append(",");
                writer.append(cotizante.getNombre()).append(",");
                writer.append(cotizante.getApellido()).append(",");
                writer.append(cotizante.getCaracterizacion().toString()).append("\n");
            }

            System.out.println("Archivo " + nombreArchivo + " generado exitosamente en " + rutaDestino);
        } catch (IOException e) {
            System.out.println("Error al generar el archivo CSV: " + e.getMessage());
        }
    }

}