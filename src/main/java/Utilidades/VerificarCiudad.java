package Utilidades;

import Clases.Persona;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VerificarCiudad {
    private Set<String> ciudadesColombia;

    public VerificarCiudad(String pathCsvCiudades) throws IOException {
        ciudadesColombia = new HashSet<>();
        cargarCiudadesDesdeCSV(pathCsvCiudades);
    }

    private void cargarCiudadesDesdeCSV(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                ciudadesColombia.add(linea.trim().toLowerCase());
            }
        }
    }

    public void verificarCiudad(Persona persona) {
        if ("Colombia".equalsIgnoreCase(persona.getPaisNacimiento())) {
            // Verificar ciudad de nacimiento
            if (!ciudadesColombia.contains(persona.getCiudadNacimiento().toLowerCase())) {
                System.out.println("Ciudad de nacimiento no válida para personas de Colombia.");
                persona.setCiudadNacimiento(null); // O marca como no válida según se requiera
            }
        } else {
            // Si no es de Colombia, establecer ciudad de residencia
            persona.setCiudadNacimiento(null); // Campo de nacimiento queda como null
            String ciudadResidencia = persona.getCiudadResidencia();
            if (ciudadResidencia == null || !ciudadesColombia.contains(ciudadResidencia.toLowerCase())) {
                System.out.println("Ciudad de residencia no válida.");
                persona.setCiudadResidencia(null); // O marcar como no válida
            }
        }
    }
}
