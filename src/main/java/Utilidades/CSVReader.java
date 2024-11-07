package Utilidades;

import Clases.Persona;
import Clases.Caracterizacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private String rutaArchivo;
    private String separador;

    public CSVReader(String rutaArchivo, String separador) {
        this.rutaArchivo = rutaArchivo;
        this.separador = separador;
    }

    public List<Persona> leerArchivo() {
        List<Persona> personas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            br.readLine(); // Saltar la primera línea (cabecera)
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(separador);

                // Parsear y asignar los valores correctamente
                Persona persona = new Persona(
                        valores[0],                                       // Nombre
                        valores[1],                                       // Apellido
                        valores[2],                                       // Celular
                        valores[3],                                       // ID o Cédula
                        valores[4],                                       // Correo
                        valores[5].equalsIgnoreCase("Sí"),                // ListaNegra
                        valores[6].equalsIgnoreCase("Sí"),                // PrePensionado
                        valores[7].equalsIgnoreCase("Sí"),                // InstitucionPublica
                        valores[8],                                       // NombreInstitucion
                        valores[9].equalsIgnoreCase("Sí"),                // ObservacionesDisciplinarias
                        valores[10].equalsIgnoreCase("Sí"),               // TieneFamiliaPolicia
                        valores[11].equalsIgnoreCase("Sí"),               // TieneHijosInpec
                        valores[12].equalsIgnoreCase("Sí"),               // Condecoracion
                        valores[13],                                      // PaisNacimiento
                        valores[14],                                      // CiudadNacimiento
                        valores[15],                                      // CiudadResidencia
                        Integer.parseInt(valores[16]),                    // Edad
                        valores[17],                                      // FondoPension
                        Integer.parseInt(valores[18]),                    // SemanasCotizadas
                        valores[19].equalsIgnoreCase("Sí"),               // FondoExtranjero
                        // Asignar la caracterización basada en el último campo
                        // Ahora usando el método valueOf para convertir a Caracterizacion
                        parseCaracterizacion(valores[20]) // Caracterizacion
                );

                personas.add(persona);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return personas;
    }

    // Método para manejar la conversión de String a Caracterizacion
    private Caracterizacion parseCaracterizacion(String valor) {
        try {
            return Caracterizacion.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Caracterizacion.NULL;  // Retorna NULL si no es un valor válido
        }
    }
}
