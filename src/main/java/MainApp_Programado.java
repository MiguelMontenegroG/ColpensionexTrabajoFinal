import com.unquindisoft.colpensionex.model.Cotizante;
import com.unquindisoft.colpensionex.model.CotizanteManager;
import com.unquindisoft.colpensionex.util.ArchivoUtil;
import com.unquindisoft.colpensionex.util.CSVReader;
import com.unquindisoft.colpensionex.util.VerificarCiudad;
import com.unquindisoft.colpensionex.util.ProcesadorCotizantes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainApp_Programado {
    private static final String CARPETA_CSV = "src/main/resources/SolicitudesEnProcesamiento/ArchivosPorProcesar";
    private static final String RUTA_CIUDADES = "src/main/resources/BaseDeDatos/paises/colombia/municipio.csv";
    private static final String SEPARADOR = ",";

    public static void main(String[] args) throws IOException {
        // Crear un verificador de ciudad
        VerificarCiudad verificador = new VerificarCiudad(RUTA_CIUDADES);

        // Crear un pool de hilos para procesar los archivos
        ExecutorService executor = Executors.newFixedThreadPool(4); // Ajusta el tamaño del pool según tu CPU
        AtomicInteger contadorProcesados = new AtomicInteger(0);

        // Crear un scheduler para ejecutar el procesamiento a la 1 a. m.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Calcular retraso inicial hasta la 1 a. m.
        long retrasoInicial = calcularRetrasoInicial(1, 35); // Hora específica: 1 a. m.

        // Programar ejecución diaria a la misma hora
        scheduler.scheduleAtFixedRate(
                () -> procesarArchivos(executor, verificador, contadorProcesados),
                retrasoInicial,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS
        );
    }

    private static void procesarArchivos(ExecutorService executor, VerificarCiudad verificador, AtomicInteger contadorProcesados) {
        // Obtener todos los archivos CSV de la carpeta
        File folder = new File(CARPETA_CSV);
        File[] archivosCSV = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (archivosCSV == null || archivosCSV.length == 0) {
            System.out.println("No se encontraron archivos CSV en la carpeta.");
            return;
        }

        for (File archivoCSV : archivosCSV) {
            executor.execute(() -> {
                try {
                    System.out.println("Procesando archivo: " + archivoCSV.getName());

                    // Verificar el número de columnas en el archivo CSV
                    int cantidadColumnas = ArchivoUtil.verificarColumnasCSV(archivoCSV.getAbsolutePath());
                    if (cantidadColumnas != 23) {
                        System.out.println("Error: El archivo " + archivoCSV.getName() + " tiene un número incorrecto de columnas.");
                        return;
                    }

                    // Leer personas desde el archivo CSV
                    CSVReader lectorCSV = new CSVReader(archivoCSV.getAbsolutePath(), SEPARADOR);
                    List<Cotizante> personas = lectorCSV.leerArchivo();

                    // Verificar el formato de las fechas en el archivo CSV
                    boolean fechasCorrectas = true;
                    for (Cotizante persona : personas) {
                        String fecha = persona.getFecha();
                        if (!ArchivoUtil.verificarFecha(fecha)) {
                            fechasCorrectas = false;
                            break;
                        }
                    }

                    if (!fechasCorrectas) {
                        System.out.println("Error: Alguna de las fechas en " + archivoCSV.getName() + " tiene un formato incorrecto.");
                        return;
                    }

                    CotizanteManager.procesarCotizantes(personas, archivoCSV.getAbsolutePath());

                    // Procesar cada persona
                    for (Cotizante persona : personas) {
                        System.out.println("Persona: " + persona.getNombre() + " " + persona.getApellido());
                        System.out.println("Lista Negra: " + persona.getListaNegra());
                        System.out.println("Caracterización inicial: " + persona.getCaracterizacion());

                        verificador.verificarCiudad(persona);
                        persona.evaluarCaracterizacion();

                        System.out.println("Caracterización evaluada: " + persona.getCaracterizacion());
                        actualizarCaracterizacionCSV(archivoCSV.getAbsolutePath(), persona);
                    }

                    // Guardar las personas aprobadas en una PriorityQueue con la función de ProcesadorCotizantes
                    String rutaAprobados = "src/main/resources/SolicitudesEnProcesamiento/aprobados.csv";
                    PriorityQueue<Cotizante> colaAprobados = ProcesadorCotizantes.guardarAprobadosConPrioridad(personas, rutaAprobados);

                    // Guardar la PriorityQueue en un archivo CSV
                    guardarPriorityQueueEnCSV(colaAprobados, rutaAprobados);

                    // Incrementar el contador de archivos procesados
                    int procesados = contadorProcesados.incrementAndGet();
                    System.out.println("Archivos procesados hasta el momento: " + procesados);

                    // Mostrar los cotizantes aprobados ordenados
                    System.out.println("Cotizantes aprobados y ordenados:");
                    while (!colaAprobados.isEmpty()) {
                        Cotizante cotizante = colaAprobados.poll();
                        System.out.println(cotizante.getNombre() + " " + cotizante.getApellido() + ", Edad: " + cotizante.getEdad());
                    }

                } catch (IOException e) {
                    System.err.println("Error al procesar el archivo: " + archivoCSV.getName() + " - " + e.getMessage());
                }
            });
        }
    }

    private static long calcularRetrasoInicial(int horaEjecutar, int minutosEjecutar) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime siguienteEjecucion = ahora.withHour(horaEjecutar).withMinute(minutosEjecutar).withSecond(0);

        // Si la hora actual es posterior a la hora de ejecución, programar para el día siguiente
        if (ahora.isAfter(siguienteEjecucion)) {
            siguienteEjecucion = siguienteEjecucion.plusDays(1);
        }

        return ChronoUnit.SECONDS.between(ahora, siguienteEjecucion);
    }

    public static void actualizarCaracterizacionCSV(String rutaCSV, Cotizante persona) throws IOException {
        // Leer el archivo CSV
        List<String> lineas = ArchivoUtil.leerArchivoBufferedReader(rutaCSV);

        // Encontrar y actualizar la línea de la persona correspondiente
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] datos = linea.split(",");

            if (datos[4].equals(persona.getId())) {  // Compara el ID de la persona
                datos[22] = persona.getCaracterizacion().toString();  // Suponiendo que la caracterización es la columna 22

                lineas.set(i, String.join(",", datos));
                break;
            }
        }

        // Escribir las líneas actualizadas de nuevo en el archivo
        ArchivoUtil.escribirArchivoBufferedWriter(rutaCSV, lineas, false);  // false para sobrescribir el archivo
    }

    public static void guardarPriorityQueueEnCSV(PriorityQueue<Cotizante> colaAprobados, String rutaCSV) throws IOException {
        // Guardar los cotizantes en el archivo CSV
        StringBuilder sb = new StringBuilder();
        while (!colaAprobados.isEmpty()) {
            Cotizante cotizante = colaAprobados.poll();
            // Formato de los datos que deseas guardar
            sb.append(cotizante.getId()).append(",")
                    .append(cotizante.getNombre()).append(",")
                    .append(cotizante.getApellido()).append(",")
                    .append(cotizante.getEdad()).append(",")
                    .append(cotizante.getCaracterizacion()).append("\n");
        }
        List<String> lineas = Arrays.asList(sb.toString().split("\n"));
        // Escribir los datos en el archivo CSV
        ArchivoUtil.escribirArchivoBufferedWriter(rutaCSV, lineas, false);  // false para sobrescribir el archivo
    }
}
