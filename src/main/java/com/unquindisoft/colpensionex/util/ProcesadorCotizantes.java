package com.unquindisoft.colpensionex.util;
import com.unquindisoft.colpensionex.model.Caracterizacion;
import com.unquindisoft.colpensionex.model.Cotizante;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ProcesadorCotizantes {

    public static PriorityQueue<Cotizante> guardarAprobadosConPrioridad(List<Cotizante> Cotizantes, String rutaArchivo) {
        // Comparator para la prioridad: menores de 35 años primero, luego por edad
        Comparator<Cotizante> comparator = (p1, p2) -> {
            boolean p1Menor35 = p1.getEdad() < 35;
            boolean p2Menor35 = p2.getEdad() < 35;
            if (p1Menor35 && !p2Menor35) {
                return -1;
            } else if (!p1Menor35 && p2Menor35) {
                return 1;
            }
            return Integer.compare(p1.getEdad(), p2.getEdad());
        };

        // Crear PriorityQueue con el comparador
        PriorityQueue<Cotizante> aprobadosQueue = new PriorityQueue<>(comparator);

        try {
            // Leer contenido existente si el archivo ya existe
            List<String> lineasExistentes = new ArrayList<>();
            File archivo = new File(rutaArchivo);

            if (archivo.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        lineasExistentes.add(linea);
                    }
                }
            }

            // Crear una lista para las nuevas líneas a escribir
            List<String> nuevasLineas = new ArrayList<>();

            // Agregar encabezado si el archivo está vacío
            if (lineasExistentes.isEmpty()) {
                nuevasLineas.add("Nombre,Apellido,Edad,Caracterizacion");
            }

            // Filtrar Cotizantes aprobadas y evitar duplicados
            for (Cotizante Cotizante : Cotizantes) {
                Cotizante.evaluarCaracterizacion(); // Evaluar la caracterización
                if (Cotizante.getCaracterizacion() == Caracterizacion.APROBADO) {
                    String lineaCotizante = Cotizante.getNombre() + "," + Cotizante.getApellido() + ","
                            + Cotizante.getEdad() + "," + Cotizante.getCaracterizacion();

                    // Solo agregar si no está ya en las líneas existentes
                    if (!lineasExistentes.contains(lineaCotizante)) {
                        nuevasLineas.add(lineaCotizante);
                        aprobadosQueue.add(Cotizante); // Agregar a la PriorityQueue
                    }
                }
            }

            // Escribir las líneas existentes y las nuevas
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
                for (String nuevaLinea : nuevasLineas) {
                    writer.write(nuevaLinea);
                    writer.newLine();
                }
            }

            System.out.println("Archivo actualizado exitosamente: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar los aprobados: " + e.getMessage());
        }

        // Devolver la PriorityQueue con los aprobados
        return aprobadosQueue;
    }
}
