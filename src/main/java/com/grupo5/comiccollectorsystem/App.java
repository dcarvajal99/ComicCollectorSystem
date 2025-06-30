package com.grupo5.comiccollectorsystem;

import com.grupo5.comiccollectorsystem.models.Comic;
import com.grupo5.comiccollectorsystem.models.Usuario;
import com.grupo5.comiccollectorsystem.models.TiendaDeComics;
import com.grupo5.comiccollectorsystem.exceptions.ComicNoEncontradoException;
import com.grupo5.comiccollectorsystem.exceptions.ComicYaPrestadoException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    private static final String DATA_DIR = "src/main/java/com/grupo5/comiccollectorsystem/data/";

    public static void main(String[] args) {
        TiendaDeComics tiendaDeComics = new TiendaDeComics();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menú Biblioteca DUOC UC ---");
            System.out.println("1. Mostrar comics");
            System.out.println("2. Mostrar usuarios");
            System.out.println("3. Buscar comic");
            System.out.println("4. Prestar comic");
            System.out.println("5. Registrar usuario");
            System.out.println("7. Registrar comic");
            System.out.println("8. Guardar y salir");
            System.out.print("Seleccione una opción: ");
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                switch (opcion) {
                    case 1:
                        int orden = pedirOpcionOrdenamiento(scanner);
                        String criterio = switch (orden) {
                            case 2 -> "autor";
                            case 3 -> "id";
                            default -> "titulo";
                        };
                        tiendaDeComics.mostrarComics(criterio);
                        break;
                    case 2:
                        tiendaDeComics.mostrarUsuarios();
                        break;
                    case 3:
                        System.out.print("Ingrese el título del comic: ");
                        String titulo = scanner.nextLine();
                        try {
                            Comic comic = tiendaDeComics.buscarComic(titulo);
                            System.out.println("Encontrado: " + comic);
                        } catch (ComicNoEncontradoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Ingrese el título del comic a prestar: ");
                        String tituloPrestar = scanner.nextLine();
                        System.out.print("Ingrese el email del usuario que lo retira: ");
                        String emailUsuario = scanner.nextLine();
                        if (tiendaDeComics.getUsuarioPorEmail(emailUsuario) == null) {
                            System.out.println("Error: El email ingresado no corresponde a un usuario registrado.");
                            break;
                        }
                        try {
                            tiendaDeComics.prestarComic(tituloPrestar, emailUsuario);
                        } catch (ComicNoEncontradoException | ComicYaPrestadoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        String nombre, apellido, email, telefono;
                        nombre = pedirCampo(scanner, "Nombre");
                        apellido = pedirCampo(scanner, "Apellido");
                        email = pedirCampo(scanner, "Email");
                        telefono = pedirCampo(scanner, "Teléfono");
                        Usuario usuario = new Usuario(nombre, apellido, email, telefono);
                        tiendaDeComics.registrarUsuario(usuario);
                        System.out.println("Usuario registrado correctamente.");
                        break;
                    case 7:
                        String tituloComic, autorComic, asignadoA;
                        tituloComic = pedirCampo(scanner, "Título");
                        autorComic = pedirCampo(scanner, "Autor");
                        System.out.print("Asignado a (email, dejar vacío si disponible): ");
                        asignadoA = scanner.nextLine().trim();
                        if (!asignadoA.isEmpty() && tiendaDeComics.getUsuarioPorEmail(asignadoA) == null) {
                            System.out.println("Error: El email ingresado no corresponde a un usuario registrado. El comic se registrará como disponible.");
                            asignadoA = "";
                        }
                        tiendaDeComics.registrarComic(tituloComic, autorComic, asignadoA);
                        System.out.println("Comic registrado correctamente.");
                        break;
                    case 8:
                        tiendaDeComics.guardarComicsEnCSV(DATA_DIR+"comics.csv");
                        tiendaDeComics.guardarUsuariosEnCSV(DATA_DIR+"usuarios.csv");
                        System.out.println("Datos guardados. Saliendo...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static String pedirCampo(Scanner scanner, String campo) {
        String valor;
        do {
            System.out.print(campo + ": ");
            valor = scanner.nextLine();
            if (valor.contains("|")) {
                System.out.println("Error: El " + campo.toLowerCase() + " no puede contener el carácter '|'.");
            }
        } while (valor.contains("|"));
        return valor;
    }

    private static int pedirOpcionOrdenamiento(Scanner scanner) {
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