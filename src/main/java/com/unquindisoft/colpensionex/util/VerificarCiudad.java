package com.unquindisoft.colpensionex.util;

import com.unquindisoft.colpensionex.model.Persona;

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
                // Normalizar la ciudad a minúsculas y eliminar espacios extra
                ciudadesColombia.add(linea.trim().toLowerCase());
            }
        }
    }

    public void verificarCiudad(Persona persona) {
        // Validar ciudad de nacimiento para personas de Colombia
        if ("Colombia".equalsIgnoreCase(persona.getPaisNacimiento())) {
            // Normalizar la ciudad de nacimiento
            String ciudadNacimiento = persona.getCiudadNacimiento().trim().toLowerCase();

            // Verificar si la ciudad de nacimiento no está en la lista de ciudades válidas
            if (!ciudadesColombia.contains(ciudadNacimiento)) {
                // Solo imprimir si la ciudad no es válida
                // System.out.println("Ciudad de nacimiento no válida para personas de Colombia.");
                persona.setCiudadNacimiento(null);
            }
        }

        // Validar ciudad de residencia
        String ciudadResidencia = persona.getCiudadResidencia().trim().toLowerCase();
        if (ciudadResidencia != null && !ciudadesColombia.contains(ciudadResidencia)) {
            // Solo imprimir si la ciudad no es válida
            // System.out.println("La ciudad de residencia no es válida.");
            persona.setCiudadResidencia(null);
        }

        // Rechazar si la ciudad de residencia es Bogotá, Medellín, Cali
        if ("bogotá".equalsIgnoreCase(ciudadResidencia) ||
                "medellin".equalsIgnoreCase(ciudadResidencia) ||
                "cali".equalsIgnoreCase(ciudadResidencia)) {
            System.out.println("Ciudad de residencia rechazada: " + ciudadResidencia);
            persona.setCiudadResidencia(null);
        }

        // Rechazar si el país de nacimiento termina en "tan"
        if (persona.getPaisNacimiento() != null &&
                persona.getPaisNacimiento().toLowerCase().endsWith("tan")) {
            System.out.println("País de nacimiento terminado en 'tan', persona rechazada.");
            persona.setCiudadNacimiento(null);
        }
    }
}

