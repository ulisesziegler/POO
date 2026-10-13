import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Persistencia {

    private static final Path ARCHIVO =
            Paths.get("misas.txt").toAbsolutePath();

    public static void guardar(Archivos archivos)
            throws IOException {

        Path temporal = ARCHIVO.resolveSibling("misas.tmp");

        try (BufferedWriter writer = Files.newBufferedWriter(
                temporal, StandardCharsets.UTF_8)) {

            for (misas misa : archivos.getmisass()) {
                writer.write(codificar(misa.getbasilica()));
                writer.write(';');
                writer.write(codificar(misa.getprovincia()));
                writer.write(';');
                writer.write(codificar(misa.getciudad()));
                writer.write(';');
                writer.write(codificar(misa.getfecha()));
                writer.newLine();
            }
        }

        try {
            Files.move(
                    temporal,
                    ARCHIVO,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        } catch (IOException e) {
            // Intentar el reemplazo normal si no se admite
            // el movimiento atómico.
            Files.move(
                    temporal,
                    ARCHIVO,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    public static void cargar(Archivos archivos)
            throws IOException {

        if (!Files.exists(ARCHIVO)) {
            return;
        }

        List<misas> cargadas = new ArrayList<>();
        int numeroLinea = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                ARCHIVO, StandardCharsets.UTF_8)) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] datos = decodificarLinea(linea);

                    if (datos.length != 4) {
                        throw new IllegalArgumentException(
                                "Se esperaban cuatro campos."
                        );
                    }

                    misas misa = new misas(
                            datos[0], datos[1], datos[2], datos[3]
                    );

                    for (misas existente : cargadas) {
                        if (existente.getfecha().equals(
                                misa.getfecha())) {
                            throw new IllegalArgumentException(
                                    "Fecha duplicada: " + datos[3]
                            );
                        }
                    }

                    cargadas.add(misa);

                } catch (IllegalArgumentException e) {
                    throw new IOException(
                            "Error en la línea " + numeroLinea +
                                    " de " + ARCHIVO + ": " + e.getMessage(),
                            e
                    );
                }
            }
        }

        // Incorporar los datos únicamente después de leer y
        // validar correctamente todo el archivo.
        for (misas misa : cargadas) {
            if (!archivos.agregarmisas(misa)) {
                throw new IOException(
                        "Ya existe una misa con fecha " +
                                misa.getfecha() + " en memoria."
                );
            }
        }
    }

    private static String codificar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private static String[] decodificarLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean escape = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (escape) {
                switch (c) {
                    case '\\':
                        campo.append('\\');
                        break;
                    case ';':
                        campo.append(';');
                        break;
                    case 'n':
                        campo.append('\n');
                        break;
                    case 'r':
                        campo.append('\r');
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Secuencia de escape inválida."
                        );
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == ';') {
                campos.add(campo.toString());
                campo.setLength(0);
            } else {
                campo.append(c);
            }
        }

        if (escape) {
            throw new IllegalArgumentException(
                    "La línea termina con una barra invertida."
            );
        }

        campos.add(campo.toString());
        return campos.toArray(new String[0]);
    }
}