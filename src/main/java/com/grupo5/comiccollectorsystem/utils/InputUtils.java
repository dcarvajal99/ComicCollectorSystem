package com.grupo5.comiccollectorsystem.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utilidades para validación y entrada de datos por consola.
 */
public class InputUtils {
    /**
     * Solicita un campo al usuario y valida que no esté vacío ni contenga '|'.
     */
    public static String pedirCampoNoVacio(Scanner scanner, String campo) {
        String valor;
        do {
            System.out.print(campo + ": ");
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Error: El " + campo.toLowerCase() + " no puede estar vacío.");
            } else if (valor.contains("|")) {
                System.out.println("Error: El " + campo.toLowerCase() + " no puede contener el carácter '|'.");
            }
        } while (valor.isEmpty() || valor.contains("|"));
        return valor;
    }

    /**
     * Solicita y valida un email.
     */
    public static String pedirEmail(Scanner scanner) {
        String email;
        do {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                System.out.println("Error: Email no válido.");
            } else if (email.contains("|")) {
                System.out.println("Error: El email no puede contener el carácter '|'.");
            }
        } while (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || email.contains("|"));
        return email;
    }

    /**
     * Solicita y valida un teléfono (solo dígitos, mínimo 7).
     */
    public static String pedirTelefono(Scanner scanner) {
        String telefono;
        do {
            System.out.print("Teléfono: ");
            telefono = scanner.nextLine().trim();
            if (!telefono.matches("\\d{7,}")) {
                System.out.println("Error: El teléfono debe contener al menos 7 dígitos y solo números.");
            } else if (telefono.contains("|")) {
                System.out.println("Error: El teléfono no puede contener el carácter '|'.");
            }
        } while (!telefono.matches("\\d{7,}") || telefono.contains("|"));
        return telefono;
    }

    /**
     * Solicita al usuario el criterio de ordenamiento de cómics.
     */
    public static int pedirOpcionOrdenamiento(Scanner scanner) {
        int opcion = 0;
        boolean valido = false;
        do {
            System.out.println("¿Cómo desea ordenar los cómics?");
            System.out.println("1. Por título");
            System.out.println("2. Por autor");
            System.out.println("3. Por id");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                if (opcion >= 1 && opcion <= 3) {
                    valido = true;
                } else {
                    System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                scanner.nextLine();
            }
        } while (!valido);
        return opcion;
    }
}

