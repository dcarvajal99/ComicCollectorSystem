package com.diegocarvajal.comiccollectorsystem.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

/**
 * Servicio para leer y escribir archivos CSV de usuarios.
 * Permite guardar y cargar la información de los usuarios en formato texto.
 */
public class UsuariosServicio {

    // Encabezado estándar para el archivo CSV de usuarios
    private static final String ENCABEZADO = "id|nombre|apellido|email|telefono";

    /**
     * Lee la lista de usuarios desde un archivo CSV, devolviendo cada fila como un arreglo de Strings.
     */
    public List<String[]> leerUsuariosCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
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
            System.out.println("Usuarios leídos correctamente desde el archivo CSV " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return datos;
    }

    /**
     * Escribe la lista de usuarios en un archivo CSV, agregando el encabezado y separando los campos por '|'.
     */
    public void escribirUsuariosCSV(String rutaArchivo, List<String[]> datos) {
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
            System.out.println("Usuarios escritos correctamente en formato CSV en " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }

}
