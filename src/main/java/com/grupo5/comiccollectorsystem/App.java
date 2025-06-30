package com.grupo5.comiccollectorsystem;

import com.grupo5.comiccollectorsystem.models.Comic;
import com.grupo5.comiccollectorsystem.models.Usuario;
import com.grupo5.comiccollectorsystem.models.TiendaDeComics;
import com.grupo5.comiccollectorsystem.exceptions.ComicNoEncontradoException;
import com.grupo5.comiccollectorsystem.exceptions.ComicYaPrestadoException;
import com.grupo5.comiccollectorsystem.utils.InputUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String DATA_DIR = "src/main/java/com/grupo5/comiccollectorsystem/data/";

    public static void main(String[] args) {
        TiendaDeComics tiendaDeComics = new TiendaDeComics();
        Scanner scanner = new Scanner(System.in);
        mostrarMenuPrincipal(tiendaDeComics, scanner);
        scanner.close();
    }

    /**
     * Muestra el menú principal y gestiona la interacción con el usuario.
     */
    private static void mostrarMenuPrincipal(TiendaDeComics tiendaDeComics, Scanner scanner) {
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
                    case 1 -> mostrarComics(tiendaDeComics, scanner);
                    case 2 -> tiendaDeComics.mostrarUsuarios();
                    case 3 -> buscarComic(tiendaDeComics, scanner);
                    case 4 -> prestarComic(tiendaDeComics, scanner);
                    case 5 -> registrarUsuario(tiendaDeComics, scanner);
                    case 7 -> registrarComic(tiendaDeComics, scanner);
                    case 8 -> {
                        tiendaDeComics.guardarComicsEnCSV(DATA_DIR+"comics.csv");
                        tiendaDeComics.guardarUsuariosEnCSV(DATA_DIR+"usuarios.csv");
                        System.out.println("Datos guardados. Saliendo...");
                        salir = true;
                    }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Muestra los cómics ordenados según el criterio elegido.
     */
    private static void mostrarComics(TiendaDeComics tiendaDeComics, Scanner scanner) {
        int orden = InputUtils.pedirOpcionOrdenamiento(scanner);
        String criterio = switch (orden) {
            case 2 -> "autor";
            case 3 -> "id";
            default -> "titulo";
        };
        tiendaDeComics.mostrarComics(criterio);
    }

    /**
     * Permite buscar cómics por id, título o autor según elección del usuario, mostrando todos los resultados posibles.
     * Refactorizado para mayor legibilidad y modularidad.
     */
    private static void buscarComic(TiendaDeComics tiendaDeComics, Scanner scanner) {
        int opcion = pedirCriterioBusqueda(scanner);
        List<Comic> resultados = switch (opcion) {
            case 1 -> buscarPorId(tiendaDeComics, scanner);
            case 2 -> buscarPorTitulo(tiendaDeComics, scanner);
            case 3 -> buscarPorAutor(tiendaDeComics, scanner);
            default -> new ArrayList<>();
        };
        mostrarResultadosBusqueda(resultados, opcion);
    }

    private static int pedirCriterioBusqueda(Scanner scanner) {
        System.out.println("¿Cómo desea buscar el cómic?");
        System.out.println("1. Por ID");
        System.out.println("2. Por título");
        System.out.println("3. Por autor");
        System.out.print("Seleccione una opción: ");
        int opcion = 0;
        try {
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número.");
            scanner.nextLine();
        }
        return opcion;
    }

    private static List<Comic> buscarPorId(TiendaDeComics tiendaDeComics, Scanner scanner) {
        System.out.print("Ingrese el ID del comic: ");
        String id = scanner.nextLine();
        List<Comic> resultados = new ArrayList<>();
        try {
            Comic comic = tiendaDeComics.buscarComicPorId(id);
            resultados.add(comic);
        } catch (ComicNoEncontradoException e) {
            // No encontrado, lista vacía
        }
        return resultados;
    }

    private static List<Comic> buscarPorTitulo(TiendaDeComics tiendaDeComics, Scanner scanner) {
        System.out.print("Ingrese el título del comic: ");
        String titulo = scanner.nextLine();
        List<Comic> resultados = new ArrayList<>();
        for (Comic c : tiendaDeComics.getComics()) {
            if (c.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados.add(c);
            }
        }
        return resultados;
    }

    private static List<Comic> buscarPorAutor(TiendaDeComics tiendaDeComics, Scanner scanner) {
        System.out.print("Ingrese el autor del comic: ");
        String autor = scanner.nextLine();
        List<Comic> resultados = new ArrayList<>();
        for (Comic c : tiendaDeComics.getComics()) {
            if (c.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                resultados.add(c);
            }
        }
        return resultados;
    }

    private static void mostrarResultadosBusqueda(List<Comic> resultados, int criterio) {
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron cómics con el criterio ingresado.");
        } else {
            System.out.println("Cómics encontrados:");
            Comparator<Comic> comparador = switch (criterio) {
                case 1 -> Comparator.comparingInt(c -> Integer.parseInt(c.getId()));
                case 2 -> Comparator.comparing(Comic::getTitulo);
                case 3 -> Comparator.comparing(Comic::getAutor).thenComparing(Comic::getTitulo);
                default -> Comparator.comparing(Comic::getTitulo);
            };
            resultados.stream().sorted(comparador).forEach(System.out::println);
        }
    }

    /**
     * Permite prestar un cómic a un usuario registrado.
     */
    private static void prestarComic(TiendaDeComics tiendaDeComics, Scanner scanner) {
        System.out.print("Ingrese el título del comic a prestar: ");
        String tituloPrestar = scanner.nextLine();
        System.out.print("Ingrese el email del usuario que lo retira: ");
        String emailUsuario = scanner.nextLine();
        if (tiendaDeComics.getUsuarioPorEmail(emailUsuario) == null) {
            System.out.println("Error: El email ingresado no corresponde a un usuario registrado.");
            return;
        }
        try {
            tiendaDeComics.prestarComic(tituloPrestar, emailUsuario);
        } catch (ComicNoEncontradoException | ComicYaPrestadoException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Permite registrar un nuevo usuario validando los datos ingresados.
     */
    private static void registrarUsuario(TiendaDeComics tiendaDeComics, Scanner scanner) {
        String nombre = InputUtils.pedirCampoNoVacio(scanner, "Nombre");
        String apellido = InputUtils.pedirCampoNoVacio(scanner, "Apellido");
        String email = InputUtils.pedirEmail(scanner);
        String telefono = InputUtils.pedirTelefono(scanner);
        Usuario usuario = new Usuario(nombre, apellido, email, telefono);
        tiendaDeComics.registrarUsuario(usuario);
        System.out.println("Usuario registrado correctamente.");
    }

    /**
     * Permite registrar un nuevo cómic validando los datos ingresados.
     */
    private static void registrarComic(TiendaDeComics tiendaDeComics, Scanner scanner) {
        String tituloComic = InputUtils.pedirCampoNoVacio(scanner, "Título");
        String autorComic = InputUtils.pedirCampoNoVacio(scanner, "Autor");
        System.out.print("Asignado a (email, dejar vacío si disponible): ");
        String asignadoA = scanner.nextLine().trim();
        if (!asignadoA.isEmpty() && tiendaDeComics.getUsuarioPorEmail(asignadoA) == null) {
            System.out.println("Error: El email ingresado no corresponde a un usuario registrado. El comic se registrará como disponible.");
            asignadoA = "";
        }
        tiendaDeComics.registrarComic(tituloComic, autorComic, asignadoA);
        System.out.println("Comic registrado correctamente.");
    }
}