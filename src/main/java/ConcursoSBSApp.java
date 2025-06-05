import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Cuenta;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class ConcursoSBSApp {

    private static final double SALDO_MINIMO_CONCURSO = 5000.00;
    private static final double PREMIO_CONCURSO = 10000.00;
    private static final String DIRECTORIO_SALIDA = "resultados_sbs";
    private static final String JSON_INPUT_FILE = "cuentas.json";

    public static void main(String[] args) {
        List<Cuenta> cuentas = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Lee el archivo JSON y lo mapea a una lista de objetos Cuenta
            cuentas = objectMapper.readValue(
                    new File(JSON_INPUT_FILE),
                    new TypeReference<List<Cuenta>>() {} // Indica a Jackson que espere una lista de Cuenta
            );
            System.out.println("✅ Registros leídos desde '" + JSON_INPUT_FILE + "' con éxito.");
            System.out.println("Total de registros cargados: " + cuentas.size());

        } catch (IOException e) {
            System.err.println("❌ ERROR: No se pudo leer o parsear el archivo JSON: " + JSON_INPUT_FILE);
            System.err.println("Mensaje de error: " + e.getMessage());
            System.err.println("Asegúrate de que el archivo existe en la raíz del proyecto y tiene el formato JSON correcto.");
            e.printStackTrace();
            return;
        }

        Path outputDirPath = Paths.get(DIRECTORIO_SALIDA);
        try {
            Files.createDirectories(outputDirPath);
            System.out.println("✅ Directorio de salida creado o ya existente: " + outputDirPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ ERROR: No se pudo crear el directorio de salida: " + DIRECTORIO_SALIDA);
            System.err.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("\n--- Procesando cuentas ---");
        for (Cuenta cuenta : cuentas) {
            // Filtrar solo los registros con estado "true"
            if (cuenta.isEstado()) {
                procesarCuenta(cuenta, outputDirPath);
            } else {
                System.out.println("⏩ Saltando cuenta " + cuenta.getNroCuenta() + " (Estado: " + cuenta.isEstado() + ")");
            }
        }

        System.out.println("\n--- Proceso finalizado ---");
        System.out.println("✔️ Revise los archivos generados en el directorio: " + outputDirPath.toAbsolutePath());
    }


    private static void procesarCuenta(Cuenta cuenta, Path outputDirectory) {
        String fileName = cuenta.getNroCuenta() + ".txt";
        Path filePath = outputDirectory.resolve(fileName);

        String contenidoArchivo;

        boolean esAptoParaConcurso = cuenta.getSaldo() > SALDO_MINIMO_CONCURSO;

        Locale localeParaFormato = Locale.US;

        if (esAptoParaConcurso) {
            contenidoArchivo = String.format(localeParaFormato,
                    "Banco de origen: %s%n" +
                            "La cuenta con el nro de cuenta: %d tiene un saldo de %.4f%n" + // .4f para 4 decimales
                            "Usted es apto a participar en el  concurso de la SBS por %.2f soles.%n" +
                            "%n" +
                            "Suerte!",
                    capitalizeFirstLetter(cuenta.getBanco()),
                    cuenta.getNroCuenta(),
                    cuenta.getSaldo(),
                    PREMIO_CONCURSO
            );
            System.out.println("✨ Procesando cuenta " + cuenta.getNroCuenta() + ": ¡Apto para concurso!");
        } else {
            contenidoArchivo = String.format(localeParaFormato,
                    "Banco de origen: %s%n" +
                            "La cuenta con el nro de cuenta: %d no tiene un saldo superior a %.2f.%n" +
                            "Lamentablemente no podrá acceder al concurso de la SBS por %.2f soles.%n" +
                            "%n" + // Salto de línea extra como en el ejemplo
                            "Gracias",
                    capitalizeFirstLetter(cuenta.getBanco()), // Capitalizamos el banco
                    cuenta.getNroCuenta(),
                    SALDO_MINIMO_CONCURSO,
                    PREMIO_CONCURSO
            );
            System.out.println("➖ Procesando cuenta " + cuenta.getNroCuenta() + ": No apto para concurso.");
        }

        // --- Escribir el contenido al archivo TXT ---
        try {
            // Escribe el contenido en el archivo.
            // StandardCharsets.UTF_8 asegura la codificación correcta.
            // Si el archivo ya existe, será sobrescrito (comportamiento por defecto de Files.write)
            Files.write(filePath, contenidoArchivo.getBytes(StandardCharsets.UTF_8));
            System.out.println("  ✔️ Archivo creado: " + filePath.getFileName());
        } catch (IOException e) {
            System.err.println("  ❌ ERROR al escribir el archivo para la cuenta " + cuenta.getNroCuenta() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para capitalizar la primera letra de una cadena (ej. "scotiabank" -> "Scotiabank").
     * @param str La cadena a capitalizar.
     * @return La cadena con la primera letra en mayúscula, o la cadena original si es nula o vacía.
     */
    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}