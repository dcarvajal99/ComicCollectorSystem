package com.grupo5.comiccollectorsystem.controllers;

import com.grupo5.comiccollectorsystem.models.TiendaDeComics;
import com.grupo5.comiccollectorsystem.models.Comic;
import com.grupo5.comiccollectorsystem.models.Usuario;
import com.grupo5.comiccollectorsystem.exceptions.ComicNoEncontradoException;
import com.grupo5.comiccollectorsystem.exceptions.ComicYaPrestadoException;
import com.grupo5.comiccollectorsystem.utils.InputUtils;
import java.util.*;

/**
 * Controlador principal del menú y las acciones de la aplicación.
 */
public class MenuController {
    private final TiendaDeComics tiendaDeComics;
    private final Scanner scanner;
    private final String DATA_DIR = "src/main/java/com/grupo5/comiccollectorsystem/data/";

    public MenuController(TiendaDeComics tiendaDeComics, Scanner scanner) {
        this.tiendaDeComics = tiendaDeComics;
        this.scanner = scanner;
    }

    public void mostrarMenuPrincipal() {
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
                    case 1 -> mostrarComics();
                    case 2 -> tiendaDeComics.mostrarUsuarios();
                    case 3 -> buscarComic();
                    case 4 -> prestarComic();
                    case 5 -> registrarUsuario();
                    case 7 -> registrarComic();
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

    public void mostrarComics() {
        int orden = InputUtils.pedirOpcionOrdenamiento(scanner);
        String criterio = switch (orden) {
            case 2 -> "autor";
            case 3 -> "id";
            default -> "titulo";
        };
        tiendaDeComics.mostrarComics(criterio);
    }

    public void buscarComic() {
        int opcion = pedirCriterioBusqueda();
        List<Comic> resultados = switch (opcion) {
            case 1 -> buscarPorId();
            case 2 -> buscarPorTitulo();
            case 3 -> buscarPorAutor();
            default -> new ArrayList<>();
        };
        mostrarResultadosBusqueda(resultados, opcion);
    }

    private int pedirCriterioBusqueda() {
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

    private List<Comic> buscarPorId() {
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

    private List<Comic> buscarPorTitulo() {
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

    private List<Comic> buscarPorAutor() {
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

    private void mostrarResultadosBusqueda(List<Comic> resultados, int criterio) {
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

    public void prestarComic() {
        System.out.println("\n--- Préstamo de cómic ---");
        if (preguntarSiMostrarComics()) {
            mostrarComics();
        }
        Comic comic = seleccionarComicParaPrestar();
        if (comic == null) return;
        if (!comic.getEstado()) {
            System.out.println("El comic ya está prestado: " + comic.getTitulo());
            return;
        }
        String emailUsuario = pedirEmailUsuario();
        if (tiendaDeComics.getUsuarioPorEmail(emailUsuario) == null) {
            System.out.println("Error: El email ingresado no corresponde a un usuario registrado.");
            return;
        }
        try {
            tiendaDeComics.prestarComic(comic.getTitulo(), emailUsuario);
        } catch (ComicNoEncontradoException | ComicYaPrestadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean preguntarSiMostrarComics() {
        System.out.println("¿Desea ver la lista de cómics antes de prestar?");
        System.out.println("1. Sí");
        System.out.println("2. No");
        System.out.print("Seleccione una opción: ");
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion == 1;
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número.");
            scanner.nextLine();
            return false;
        }
    }

    private Comic seleccionarComicParaPrestar() {
        System.out.println("¿Cómo desea buscar el cómic a prestar?");
        System.out.println("1. Por ID");
        System.out.println("2. Por título");
        System.out.print("Seleccione una opción: ");
        int criterio = 0;
        try {
            criterio = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número.");
            scanner.nextLine();
            return null;
        }
        try {
            if (criterio == 1) {
                System.out.print("Ingrese el ID del comic a prestar: ");
                String id = scanner.nextLine();
                return tiendaDeComics.buscarComicPorId(id);
            } else if (criterio == 2) {
                System.out.print("Ingrese el título del comic a prestar: ");
                String titulo = scanner.nextLine();
                return tiendaDeComics.buscarComic(titulo);
            } else {
                System.out.println("Opción no válida.");
                return null;
            }
        } catch (ComicNoEncontradoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String pedirEmailUsuario() {
        System.out.print("Ingrese el email del usuario que lo retira: ");
        return scanner.nextLine();
    }

    public void registrarUsuario() {
        String nombre = InputUtils.pedirCampoNoVacio(scanner, "Nombre");
        String apellido = InputUtils.pedirCampoNoVacio(scanner, "Apellido");
        String email = InputUtils.pedirEmail(scanner);
        String telefono = InputUtils.pedirTelefono(scanner);
        Usuario usuario = new Usuario("", nombre, apellido, email, telefono);
        tiendaDeComics.registrarUsuario(usuario);
        System.out.println("Usuario registrado correctamente.");
    }

    public void registrarComic() {
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

