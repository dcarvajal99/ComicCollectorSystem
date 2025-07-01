package com.diegocarvajal.comiccollectorsystem.services;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * Servicio para leer y escribir archivos CSV de cómics.
 * Permite guardar y cargar la información de los cómics en formato texto.
 */
public class ComicsServicio {

    // Encabezado estándar para el archivo CSV de cómics
    private static final String ENCABEZADO = "id|titulo|autor|estado|asignadoA";

    /**
     * Escribe la lista de cómics en un archivo CSV, agregando el encabezado y separando los campos por '|'.
     */
    public void escribirComicsCSV(String rutaArchivo, java.util.List<String[]> datos) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
                writer.write(ENCABEZADO);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo CSV: " + e.getMessage());
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(ENCABEZADO);
            writer.newLine();
            for (String[] fila : datos) {
                writer.write(String.join("|", fila));
                writer.newLine();
            }
            System.out.println("Comics escritos correctamente en formato CSV en " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Lee la lista de cómics desde un archivo CSV, devolviendo cada fila como un arreglo de Strings.
     */
    public java.util.List<String[]> leerComicCSV(String rutaArchivo) {
        java.util.List<String[]> datos = new java.util.ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
                writer.write(ENCABEZADO);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo CSV: " + e.getMessage());
            }
            return datos;
        }
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] fila = linea.split("\\|");
                datos.add(fila);
            }
            System.out.println("Comics leídos correctamente desde el archivo CSV " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return datos;
    }

}
