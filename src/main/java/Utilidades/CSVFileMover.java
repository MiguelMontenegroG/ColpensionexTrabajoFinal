package Utilidades;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class CSVFileMover {

    public static void moveCsvFiles(String sourceDir, String targetDir) {
        File sourceFolder = new File(sourceDir);
        File targetFolder = new File(targetDir);

        // Verificar que las carpetas existan, si no, crearlas
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        // Filtrar y mover los archivos con extensión .csv
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
}