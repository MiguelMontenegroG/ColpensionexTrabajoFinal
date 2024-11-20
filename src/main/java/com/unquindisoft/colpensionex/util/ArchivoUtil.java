package com.unquindisoft.colpensionex.util;

import com.unquindisoft.colpensionex.model.Caracterizacion;
import com.unquindisoft.colpensionex.model.Persona;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class  ArchivoUtil {

    /**
     * Permite leer un archivo desde una ruta específica mediante Scanner
     * @param ruta Ruta a leer
     * @return Lista de String por cada línea del archivo
     * @throws IOException
     */
    public static ArrayList<String> leerArchivoScanner(String ruta) throws IOException{

        ArrayList<String> lista = new ArrayList<>();
        Scanner sc = new Scanner(new File(ruta));

        while(sc.hasNextLine()) {
            lista.add(sc.nextLine());
        }

        sc.close();

        return lista;
    }

    /**
     * Permite leer un archivo desde una ruta específica mediante BufferedReader
     * @param ruta Ruta a leer
     * @return Lista de String por cada línea del archivo
     * @throws IOException
     */
    public static ArrayList<String> leerArchivoBufferedReader(String ruta) throws IOException{

        ArrayList<String> lista = new ArrayList<>();
        FileReader fr = new FileReader(ruta);
        BufferedReader br = new BufferedReader(fr);
        String linea;

        while( ( linea = br.readLine() )!=null ) {
            lista.add(linea);
        }

        br.close();
        fr.close();

        return lista;
    }

    /**
     * Escribe datos en un archivo de texo
     * @param ruta Ruta donde se va a crear el archivo
     * @param lista Datos que se escriben en el archivo
     * @throws IOException
     */
    public static void escribirArchivoFormatter(String ruta, List<String> lista) throws IOException{
        Formatter ft = new Formatter(ruta);
        for(String s : lista){
            ft.format(s+"%n");
        }
        ft.close();
    }

    /**
     * Escribe datos en un archivo de texo
     * @param ruta ruta Ruta donde se va a crear el archivo
     * @param lista Información a guardar en el archivo
     * @param concat True si se concatena los nuevos datos sin sobreescibir todo el archivo
     * @throws IOException
     */
    public static void escribirArchivoBufferedWriter(String ruta, List<String> lista, boolean concat) throws IOException{

        FileWriter fw = new FileWriter(ruta, concat);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String string : lista) {
            bw.write(string);
            bw.newLine();
        }

        bw.close();
        fw.close();
    }

    /**
     * Serializa un objeto en disco
     * @param ruta Ruta del archivo donde se va a serializar el objeto
     * @param objeto Objeto a serializar
     * @throws IOException
     */
    public static void serializarObjeto(String ruta, Object objeto) throws IOException{
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(ruta));
        os.writeObject(objeto);
        os.close();
    }

    /**
     * Deserializa un objeto que está guardado en disco
     * @param ruta Ruta del archivo a deserializar
     * @return Objeto deserializado
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Object deserializarObjeto(String ruta) throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(ruta));
        Object objeto = is.readObject();
        is.close();

        return objeto;
    }

    /**
     * Serializa un objeto en un archivo en formato XML
     * @param ruta Ruta del archivo donde se va a serializar el objeto
     * @param objeto Objeto a serializar
     * @throws FileNotFoundException
     */
    public static void serializarObjetoXML(String ruta, Object objeto) throws FileNotFoundException {
        XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ruta));
        encoder.writeObject(objeto);
        encoder.close();
    }

    /**
     * Deserializa un objeto desde un archivo XML
     * @param ruta Ruta del archivo a deserializar
     * @return Objeto deserializado
     * @throws IOException
     */
    public static Object deserializarObjetoXML(String ruta) throws IOException{
        XMLDecoder decoder = new XMLDecoder(new FileInputStream(ruta));
        Object objeto = decoder.readObject();
        decoder.close();

        return objeto;
    }

    public static String crearCapetaDia (String ruta) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String folderName = "SolicitudesProcesadas_" + today.format(formatter);

        File folder = new File(ruta, folderName);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Carpeta creada: " + folder.getAbsolutePath());
            } else {
                System.err.println("No se pudo crear la carpeta.");
            }
        } else {
            System.out.println("La carpeta ya existe: " + folder.getAbsolutePath());
        }

        return folder.getAbsolutePath();
    }

    public static void moveCsvFiles(String sourceDir, String targetDir) {
        File sourceFolder = new File(sourceDir);
        File targetFolder = new File(targetDir);

        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        File[] csvFiles = sourceFolder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No hay archivos CSV en la carpeta de origen.");
            return;
        }

        for (File csvFile : csvFiles) {
            Path sourcePath = csvFile.toPath();
            Path targetPath = Paths.get(targetFolder.getAbsolutePath(), csvFile.getName());

            try {
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo movido: " + csvFile.getName());
            } catch (IOException e) {
                System.err.println("Error al mover el archivo " + csvFile.getName() + ": " + e.getMessage());
            }
        }
    }

    public static void comprimirCarpeta(String folderPath, String zipFilePath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Path sourcePath = Paths.get(folderPath);

            // Recorrer todos los archivos en la carpeta para agregarlos al zip
            Files.walk(sourcePath).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                try {
                    zos.putNextEntry(zipEntry);
                    Files.copy(path, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    System.err.println("Error al agregar archivo al ZIP: " + e.getMessage());
                }
            });
            System.out.println("Carpeta comprimida en: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Error al crear el archivo ZIP: " + e.getMessage());
        }
    }

    public static void comprimirCarpetaAyer(String baseDir) {
        // Obtener la fecha del día anterior en formato yyyy_MM_dd
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String folderName = "SolicitudesProcesadas_" + yesterday.format(formatter);

        // Definir la ruta de la carpeta y del archivo zip
        String folderPath = Paths.get(baseDir, folderName).toString();
        String zipFilePath = Paths.get(baseDir, folderName + ".zip").toString();

        // Comprimir la carpeta si existe
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            comprimirCarpeta(folderPath, zipFilePath);
        } else {
            System.out.println("No existe la carpeta de ayer para comprimir.");
        }
    }

    public static void moverSolicitudes(String carpetaOrigen, String carpetaDestino) {
        Path carpetaEntrantes = Paths.get(carpetaOrigen);
        Path carpetaProcesamiento = Paths.get(carpetaDestino);

        try {
            // Crear la carpeta de procesamiento si no existe
            if (Files.notExists(carpetaProcesamiento)) {
                Files.createDirectories(carpetaProcesamiento);
            }

            // Listar archivos en "SolicitudesEntrantes" y moverlos
            DirectoryStream<Path> stream = Files.newDirectoryStream(carpetaEntrantes);

            for (Path archivo : stream) {
                Path destino = carpetaProcesamiento.resolve(archivo.getFileName());
                Files.move(archivo, destino, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo movido: " + archivo.getFileName());
            }
        } catch (IOException e) {
            System.err.println("Error al mover los archivos: " + e.getMessage());
        }
    }

    public static int verificarColumnasCSV(String archivoCSV) {
        int cantidadColumnas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea = br.readLine(); // Leer la primera línea (encabezados)
            if (linea != null) {
                // Contar las columnas separadas por coma (asumiendo formato CSV estándar)
                String[] columnas = linea.split(",");
                cantidadColumnas = columnas.length;
                System.out.println("Cantidad de columnas: " + cantidadColumnas);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }

        return cantidadColumnas;
    }

    public static boolean verificarFecha(String fecha) {
        // Definir el formato de fecha esperado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Intentar parsear la fecha con el formato especificado
            LocalDate.parse(fecha, formatter);
            System.out.println("La fecha es válida: " + fecha);
            return true;  // Si no lanza excepción, es una fecha válida
        } catch (DateTimeParseException e) {
            System.out.println("Fecha no válida: " + fecha);
            return false;  // Si ocurre una excepción, la fecha es inválida
        }
    }

    public static void guardarAprobados(List<Persona> personas, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Escribir encabezado
            writer.append("Nombre,Apellido,Edad,Caracterizacion\n");

            // Filtrar y escribir personas aprobadas
            for (Persona persona : personas) {
                persona.evaluarCaracterizacion(); // Evaluar la caracterización
                if (persona.getCaracterizacion() == Caracterizacion.APROBADO) {
                    writer.append(persona.getNombre()).append(",")
                            .append(persona.getApellido()).append(",")
                            .append(String.valueOf(persona.getEdad())).append(",")
                            .append(persona.getCaracterizacion().toString()).append("\n");
                }
            }
            System.out.println("Archivo generado exitosamente: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar los aprobados: " + e.getMessage());
        }
    }

}