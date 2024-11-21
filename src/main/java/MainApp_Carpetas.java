import com.unquindisoft.colpensionex.model.Persona;
import com.unquindisoft.colpensionex.util.ArchivoUtil;
import com.unquindisoft.colpensionex.util.CSVReader;
import com.unquindisoft.colpensionex.util.VerificarCiudad;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainApp_Carpetas {
    public static void main(String[] args) throws IOException {
        String carpetaCSV = "src/main/resources/SolicitudesEnProcesamiento/Prueba peque/";  // Ruta de la carpeta
        String rutaCiudades = "src/main/resources/BaseDeDatos/paises/colombia/municipio.csv";
        String separador = ",";

        // Obtener todos los archivos CSV de la carpeta
        File folder = new File(carpetaCSV);
        File[] archivosCSV = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (archivosCSV == null || archivosCSV.length == 0) {
            System.out.println("No se encontraron archivos CSV en la carpeta.");
            return;
        }

        // Crear el verificador de ciudad
        VerificarCiudad verificador = new VerificarCiudad(rutaCiudades);

        // Crear un pool de hilos para procesar los archivos
        ExecutorService executor = Executors.newFixedThreadPool(4); // Ajusta el tamaño del pool según tu CPU
        AtomicInteger contadorProcesados = new AtomicInteger(0);

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
                    CSVReader lectorCSV = new CSVReader(archivoCSV.getAbsolutePath(), separador);
                    List<Persona> personas = lectorCSV.leerArchivo();

                    // Verificar el formato de las fechas en el archivo CSV
                    boolean fechasCorrectas = true;
                    for (Persona persona : personas) {
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

                    // Procesar cada persona
                    for (Persona persona : personas) {
                        System.out.println("Persona: " + persona.getNombre() + " " + persona.getApellido());
                        System.out.println("Lista Negra: " + persona.getListaNegra());
                        System.out.println("Caracterización inicial: " + persona.getCaracterizacion());

                        verificador.verificarCiudad(persona);
                        persona.evaluarCaracterizacion();

                        System.out.println("Caracterización evaluada: " + persona.getCaracterizacion());
                        actualizarCaracterizacionCSV(archivoCSV.getAbsolutePath(), persona);
                    }

                    // Guardar las personas aprobadas en un archivo CSV
                    String rutaAprobados = "src/main/resources/SolicitudesEnProcesamiento/aprobados.csv";
                    ArchivoUtil.guardarAprobados(personas, rutaAprobados);

                    // Incrementar el contador de archivos procesados
                    int procesados = contadorProcesados.incrementAndGet();
                    System.out.println("Archivos procesados hasta el momento: " + procesados);

                } catch (IOException e) {
                    System.err.println("Error al procesar el archivo: " + archivoCSV.getName() + " - " + e.getMessage());
                }
            });
        }

        // Apagar el executor cuando termine todo el procesamiento
        executor.shutdown();
    }

    public static void actualizarCaracterizacionCSV(String rutaCSV, Persona persona) throws IOException {
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
}
