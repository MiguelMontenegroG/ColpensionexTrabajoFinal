import Utilidades.CSVReader;
import Clases.Persona;
import Utilidades.VerificarCiudad;

import java.io.IOException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        String rutaCSV = "ruta/al/archivo.csv";
        String rutaCiudades = "src/main/resources/BaseDeDatos/paises/colombia/municipio.csv";
        String separador = ",";

        try {
            // Leer personas desde el archivo CSV
            CSVReader lectorCSV = new CSVReader(rutaCSV, separador);
            List<Persona> personas = lectorCSV.leerArchivo();

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
        // Implementar lógica para sobrescribir la fila correspondiente
        // en el archivo CSV con la caracterización actualizada de la persona.
        // (Aquí podría abrir el archivo en modo escritura y reemplazar la línea)
    }
}
