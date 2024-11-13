package Utilidades;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR
//NO EJECUTAR

public class GeneradorCSV {

    private static final String[] NOMBRES = {"Juan", "Maria", "Carlos", "Ana", "Luis", "Carolina", "José", "Lucía", "Pedro", "Andrea"};
    private static final String[] APELLIDOS = {"Pérez", "Gomez", "Sánchez", "López", "Martínez", "Díaz", "Rodríguez", "Mejía", "García", "Ramírez"};
    private static final String[] INSTITUCIONES = {"Mininterior", "Minsalud", "Policía", "Inpec", "Armada"};
    private static final String[] PAISES = {"Colombia", "Argentina", "Chile", "Perú", "Kazajistán", "Tayikistán"};
    private static final String[] CIUDADES = {"Bogotá", "Medellín", "Cali", "Buenos Aires", "Lima", "Dushanbe"};
    private static final String[] FONDOS_PENSION = {"Provenir", "Protección", "Colfondos", "Old Mutual"};
    private static final String[] CARACTERIZACIONES = {"APROBADO", "RECHAZADO", "INHABILITADO", "EMBARGADO"};
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Random random = new Random();

    public static void main(String[] args) {
        String carpetaSalida = "carpeta_csv";
        crearCarpeta(carpetaSalida);

        for (int i = 1; i <= 100000; i++) {
            String archivoNombre = carpetaSalida + "/archivo_" + i + ".csv";
            generarCSV(archivoNombre);
        }

        System.out.println("100,000 archivos CSV generados exitosamente en la carpeta: " + carpetaSalida);
    }

    private static void crearCarpeta(String carpeta) {
        try {
            Files.createDirectories(Paths.get(carpeta));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generarCSV(String archivoNombre) {
        try (FileWriter writer = new FileWriter(archivoNombre)) {
            // Escribir encabezado
            writer.write("Nombre,Apellido,Fecha,Celular,ID,Correo,ListaNegra,PrePensionado,InstitucionPublica,NombreInstitucion,ObservacionesDisciplinarias,TieneFamiliaPolicia,TieneHijosInpec,Condecoracion,PaisNacimiento,CiudadNacimiento,CiudadResidencia,Edad,FechaNacimiento,FondoPension,SemanasCotizadas,FondoExtranjero,Caracterizacion\n");

            // Generar datos aleatorios para una fila
            String fila = generarFilaAleatoria();
            writer.write(fila);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generarFilaAleatoria() {
        String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
        String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
        String fecha = LocalDate.now().format(DATE_FORMAT);
        String celular = "3" + (random.nextInt(1000000000) + 100000000);
        String id = String.valueOf(100000000 + random.nextInt(900000000));
        String correo = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@example.com";
        String listaNegra = randomBoolean();
        String prePensionado = randomBoolean();
        String institucionPublica = randomBoolean();
        String nombreInstitucion = institucionPublica.equals("Sí") ? INSTITUCIONES[random.nextInt(INSTITUCIONES.length)] : "";
        String observacionesDisciplinarias = randomBoolean();
        String tieneFamiliaPolicia = randomBoolean();
        String tieneHijosInpec = randomBoolean();
        String condecoracion = randomBoolean();
        String paisNacimiento = PAISES[random.nextInt(PAISES.length)];
        String ciudadNacimiento = CIUDADES[random.nextInt(CIUDADES.length)];
        String ciudadResidencia = CIUDADES[random.nextInt(CIUDADES.length)];
        int edad = 18 + random.nextInt(50); // Edad entre 18 y 67
        String fechaNacimiento = LocalDate.now().minusYears(edad).format(DATE_FORMAT);
        String fondoPension = FONDOS_PENSION[random.nextInt(FONDOS_PENSION.length)];
        int semanasCotizadas = random.nextInt(1000); // Semanas entre 0 y 999
        String fondoExtranjero = randomBoolean();
        String caracterizacion = CARACTERIZACIONES[random.nextInt(CARACTERIZACIONES.length)];

        return String.join(",", nombre, apellido, fecha, celular, id, correo, listaNegra, prePensionado, institucionPublica,
                nombreInstitucion, observacionesDisciplinarias, tieneFamiliaPolicia, tieneHijosInpec, condecoracion,
                paisNacimiento, ciudadNacimiento, ciudadResidencia, String.valueOf(edad), fechaNacimiento, fondoPension,
                String.valueOf(semanasCotizadas), fondoExtranjero, caracterizacion) + "\n";
    }

    private static String randomBoolean() {
        return random.nextBoolean() ? "Sí" : "No";
    }
}
