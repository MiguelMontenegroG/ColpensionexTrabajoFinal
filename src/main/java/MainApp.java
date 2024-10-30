import Utilidades.CSVReader;
import Clases.Persona;

import java.util.List;

public class MainApp {

    public void procesarCSV() {
        CSVReader lectorCSV = new CSVReader("ruta/del/archivo.csv", ",");
        List<Persona> personas = lectorCSV.leerArchivo();

        for (Persona persona : personas) {
            String resultado = persona.evaluarCriterios();
            System.out.println("Evaluación para " + persona.getNombre() + " " + persona.getApellido() + ": " + resultado);
        }
    }
}
