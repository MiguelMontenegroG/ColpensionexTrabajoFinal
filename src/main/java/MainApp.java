import Utilidades.CSVReader;
import Clases.Persona;
import Utilidades.VerificarCiudad;
import Utilidades.ArchivoUtil;

import java.io.IOException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        String rutaCSV = "src/main/resources/SolicitudesEnProcesamiento/CSVEjemplo2.csv";
        String rutaCiudades = "src/main/resources/BaseDeDatos/paises/colombia/municipio.csv";
        String separador = ",";

        // Verificar el número de columnas en el archivo CSV
        int cantidadColumnas = ArchivoUtil.verificarColumnasCSV(rutaCSV);

        // Comprobar si el número de columnas es el correcto (por ejemplo, 20)
        if (cantidadColumnas != 23) {
            System.out.println("Error: El archivo CSV tiene un número incorrecto de columnas.");
            return;  // Salir si el número de columnas no es correcto
        }

        try {
            // Leer personas desde el archivo CSV
            CSVReader lectorCSV = new CSVReader(rutaCSV, separador);
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
                System.out.println("Error: Alguna de las fechas tiene un formato incorrecto.");
                return;  // Salir si alguna fecha tiene formato incorrecto
            }

            // Crear el verificador de ciudad
            VerificarCiudad verificador = new VerificarCiudad(rutaCiudades);

            // Procesar cada persona
            for (Persona persona : personas) {
                // Imprimir la caracterización inicial
                System.out.println("Persona: " + persona.getNombre() + " " + persona.getApellido());
                System.out.println("Caracterización inicial: " + persona.getCaracterizacion());

                // Verificar la ciudad
                verificador.verificarCiudad(persona);

                // Evaluar la caracterización
                persona.evaluarCaracterizacion();

                // Imprimir la caracterización final después de la evaluación
                System.out.println("Caracterización evaluada: " + persona.getCaracterizacion());

                // Actualizar el archivo CSV con la nueva caracterización
                actualizarCaracterizacionCSV(rutaCSV, persona);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarCaracterizacionCSV(String rutaCSV, Persona persona) {
        try {
            // Leer el archivo CSV
            List<String> lineas = ArchivoUtil.leerArchivoBufferedReader(rutaCSV);

            // Encontrar y actualizar la línea de la persona correspondiente
            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] datos = linea.split(",");

                if (datos[4].equals(persona.getId())) {  // Compara el ID de la persona
                    datos[22] = persona.getCaracterizacion().toString();  // Suponiendo que la caracterización es la columna 19

                    lineas.set(i, String.join(",", datos));
                    break;
                }
            }

            // Escribir las líneas actualizadas de nuevo en el archivo
            ArchivoUtil.escribirArchivoBufferedWriter(rutaCSV, lineas, false);  // false para sobrescribir el archivo

        } catch (IOException e) {
            System.err.println("Error al actualizar el archivo CSV: " + e.getMessage());
        }
    }
    /**
     *      // Configuración inicial
     *         LoggerConfig.getLogger();  // Inicializa el logger
     *         DirectoryManager.crearDirectorios();  // Crea los directorios necesarios
     *
     *         // Inicia el proceso programado
     *         SchedulerConfig scheduler = new SchedulerConfig();
     *         scheduler.startScheduler();
     *
     *         System.out.println("Proceso iniciado correctamente.");
     *
     *         // Probar mover archivos (esto debe llamarse automáticamente cada hora)
     *         SolicitudService solicitudService = new SolicitudService();
     *         solicitudService.trasladarSolicitudes();
     */
}
