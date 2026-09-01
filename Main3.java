
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class Main3 {

    public static void main(String[] args) {
        String archivoOrigen = "data2.txt";
        String archivoDestino = "output2.txt";
        String vocales = "aeiouAEIOUáéíóúÁÉÍÓÚüÜ"; // Incluye tildes por seguridad

        try(BufferedReader br = new BufferedReader(new FileReader(archivoOrigen));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archivoDestino))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    char primerCaracter = linea.charAt(0);
                    if (vocales.indexOf(primerCaracter) != -1) {
                        bw.write(linea);
                        bw.newLine();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("no se encontro el archivo ");
            System.out.println("detalle: " + e.getMessage());

        } catch (AccessDeniedException e) {
            System.out.println("No tienes permisos para acceder a este archivo.");

        } catch (IOException e) {
            System.out.println("error de entrada/salida al procesar los archivos.");
            System.out.println("detalle: " + e.getMessage());

        } catch (SecurityException e) {
            System.out.println("violacion de permisos.");

        } finally {
            System.out.println("ejecucion del programa finalizada.");
        }
    }
}