package ProcesadorArchivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class Main {

    public static void main(String[] args) {

        String archivoEntrada = "data.txt";
        String archivoSalida  = "output.txt";

        try (
                BufferedReader lector  = new BufferedReader(new FileReader(archivoEntrada));
                BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoSalida))
        ) {
            String linea;
            int contadorLineas = 0;

            while ((linea = lector.readLine()) != null) {
                contadorLineas++;
                String lineaProcesada = contadorLineas + ". " + linea.toUpperCase();
                escritor.write(lineaProcesada);
                escritor.newLine();
            }

            System.out.println("proceso finalizado con exito.");
            System.out.println("lineas procesadas: " + contadorLineas);

        } catch (FileNotFoundException e) {
            System.out.println("no se encontro el archivo '" + archivoEntrada + "'.");
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
