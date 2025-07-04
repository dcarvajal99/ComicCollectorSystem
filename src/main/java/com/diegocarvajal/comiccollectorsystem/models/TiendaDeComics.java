package com.diegocarvajal.comiccollectorsystem.models;

import com.diegocarvajal.comiccollectorsystem.services.ComicsServicio;
import com.diegocarvajal.comiccollectorsystem.services.UsuariosServicio;
import com.diegocarvajal.comiccollectorsystem.exceptions.ComicNoEncontradoException;
import com.diegocarvajal.comiccollectorsystem.exceptions.ComicYaPrestadoException;

import java.util.*;

/**
 * Clase principal que gestiona la lógica de la tienda de cómics.
 * Aquí se almacenan y manipulan los cómics y usuarios, y se realizan operaciones como registrar, buscar, prestar y guardar datos.
 */
public class TiendaDeComics {
    // Lista de cómics disponibles en la tienda
    private ArrayList<Comic> comics = new ArrayList<>();
    // Mapa de usuarios registrados, usando el email como clave para evitar duplicados
    private HashMap<String, Usuario> usuarios = new HashMap<>();
    // Servicios para leer y escribir archivos CSV de cómics y usuarios
    private ComicsServicio comicsServicio = new ComicsServicio();
    private UsuariosServicio usuariosServicio = new UsuariosServicio();

    /**
     * Constructor: carga los datos de cómics y usuarios desde archivos CSV al iniciar la aplicación.
     */
    public TiendaDeComics() {
        cargarComicsDesdeCSV("src/main/java/com/diegocarvajal/comiccollectorsystem/data/comics.csv");
        cargarUsuariosDesdeCSV("src/main/java/com/diegocarvajal/comiccollectorsystem/data/usuarios.csv");
    }

    /**
     * Lee los cómics desde un archivo CSV y los agrega a la lista.
     */
    private void cargarComicsDesdeCSV(String rutaArchivo) {
        List<String[]> datos = comicsServicio.leerComicCSV(rutaArchivo);
        boolean esPrimera = true;
        for (String[] fila : datos) {
            if (esPrimera) { esPrimera = false; continue; } // Saltar cabecera
            // Asegurarse de que siempre haya 5 columnas
            String[] normalizado = new String[5];
            for (int i = 0; i < 5; i++) {
                normalizado[i] = (i < fila.length) ? fila[i] : "";
            }
            String id = normalizado[0];
            String titulo = normalizado[1];
            String autor = normalizado[2];
            Boolean estado = Boolean.parseBoolean(normalizado[3]);
            String asignadoA = normalizado[4];
            comics.add(new Comic(id, titulo, autor, estado, asignadoA));
        }
    }

    /**
     * Lee los usuarios desde un archivo CSV y los agrega al mapa de usuarios.
     */
    private void cargarUsuariosDesdeCSV(String rutaArchivo) {
        List<String[]> datos = usuariosServicio.leerUsuariosCSV(rutaArchivo);
        boolean esPrimera = true;
        for (String[] fila : datos) {
            if (esPrimera) { esPrimera = false; continue; } // Saltar cabecera
            if (fila.length == 5) {
                // fila[0]=id, fila[1]=nombre, fila[2]=apellido, fila[3]=email, fila[4]=telefono
                Usuario usuario = new Usuario(fila[0], fila[1], fila[2], fila[3], fila[4]);
                usuarios.put(usuario.getEmail(), usuario);
            }
        }
    }

    /**
     * Agrega un cómic a la lista (no asigna id automáticamente).
     */
    public void agregarComic(Comic comic) {
        comics.add(comic);
    }

    /**
     * Registra un nuevo cómic, asignando un id incremental y estado disponible o prestado según si está asignado.
     */
    public void registrarComic(String titulo, String autor, String asignadoA) {
        // Buscar el id más alto actual
        int maxId = 0;
        for (Comic comic : comics) {
            try {
                int idComic = Integer.parseInt(comic.getId());
                if (idComic > maxId) maxId = idComic;
            } catch (NumberFormatException e) {
                // Ignorar ids no numéricos
            }
        }
        String nuevoId = String.valueOf(maxId + 1);
        Boolean estadoBool = true; // Siempre disponible al registrar
        String asignadoFinal = (asignadoA != null && !asignadoA.isEmpty()) ? asignadoA : "";
        if (!asignadoFinal.isEmpty()) {
            estadoBool = false;
        }
        Comic comic = new Comic(nuevoId, titulo, autor, estadoBool, asignadoFinal);
        comics.add(comic);
    }

    /**
     * Registra un nuevo usuario, asignando un id incremental único y evitando duplicados por email.
     */
    public void registrarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getEmail())) {
            System.out.println("Error: Ya existe un usuario registrado con ese email.");
            return;
        }
        // Asignar id incremental automáticamente
        int maxId = 1;
        for (Usuario u : usuarios.values()) {
            try {
                int idU = Integer.parseInt(u.getId());
                if (idU >= maxId) maxId = idU + 1;
            } catch (NumberFormatException e) {
                // Ignorar ids no numéricos
            }
        }
        usuario.setId(String.valueOf(maxId));
        usuarios.put(usuario.getEmail(), usuario);
    }

    /**
     * Busca un cómic por su título (ignorando mayúsculas/minúsculas).
     * Lanza excepción si no lo encuentra.
     */
    public Comic buscarComic(String titulo) throws ComicNoEncontradoException {
        for (Comic comic : comics) {
            if (comic.getTitulo().equalsIgnoreCase(titulo)) {
                return comic;
            }
        }
        throw new ComicNoEncontradoException("Comic no encontrado: " + titulo);
    }

    /**
     * Busca un cómic por su ID.
     * Lanza excepción si no lo encuentra.
     */
    public Comic buscarComicPorId(String id) throws ComicNoEncontradoException {
        for (Comic comic : comics) {
            if (comic.getId().equals(id)) {
                return comic;
            }
        }
        throw new ComicNoEncontradoException("Comic no encontrado con ID: " + id);
    }

    /**
     * Busca todos los cómics de un autor (ignorando mayúsculas/minúsculas).
     * Devuelve una lista de cómics encontrados.
     */
    public List<Comic> buscarComicsPorAutor(String autor) {
        List<Comic> resultado = new ArrayList<>();
        for (Comic comic : comics) {
            if (comic.getAutor().equalsIgnoreCase(autor)) {
                resultado.add(comic);
            }
        }
        return resultado;
    }

    /**
     * Presta un cómic a un usuario registrado, cambiando su estado y asignando el email del usuario.
     * Lanza excepción si el cómic no existe o ya está prestado.
     */
    public void prestarComic(String titulo, String emailUsuario) throws ComicNoEncontradoException, ComicYaPrestadoException {
        Comic comic = buscarComic(titulo);
        if (!comic.getEstado()) {
            throw new ComicYaPrestadoException("El comic ya está prestado: " + titulo);
        }
        comic.setEstado(false);
        comic.setAsignadoA(emailUsuario);
        System.out.println("Comic prestado con éxito: " + titulo + " a " + emailUsuario);
    }

    /**
     * Devuelve el usuario registrado con el email dado, o null si no existe.
     */
    public Usuario getUsuarioPorEmail(String email) {
        return usuarios.get(email);
    }

    /**
     * Guarda la lista de cómics en un archivo CSV usando el servicio correspondiente.
     */
    public void guardarComicsEnCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
        for (Comic comic : comics) {
            datos.add(new String[]{comic.getId(), comic.getTitulo(), comic.getAutor(), comic.getEstado().toString(), comic.getAsignadoA() == null ? "" : comic.getAsignadoA()});
        }
        comicsServicio.escribirComicsCSV(rutaArchivo, datos);
    }

    /**
     * Guarda la lista de usuarios en un archivo CSV usando el servicio correspondiente.
     */
    public void guardarUsuariosEnCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            datos.add(new String[]{usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getTelefono()});
        }
        usuariosServicio.escribirUsuariosCSV(rutaArchivo, datos);
    }

    /**
     * Muestra los cómics ordenados según el criterio (título, autor o id), evitando duplicados.
     */
    public void mostrarComics(String criterio) {
        // TreeSet para evitar duplicados y mostrar cómics ordenados según el criterio
        Comparator<Comic> comparador;
        switch (criterio) {
            case "autor":
                comparador = Comparator.comparing(Comic::getAutor).thenComparing(Comic::getTitulo);
                break;
            case "id":
                comparador = Comparator.comparingInt(c -> Integer.parseInt(c.getId()));
                break;
            default:
                comparador = Comparator.comparing(Comic::getTitulo);
        }
        TreeSet<Comic> comicsOrdenados = new TreeSet<>(comparador);
        comicsOrdenados.addAll(comics);
        for (Comic comic : comicsOrdenados) {
            System.out.println(comic);
        }
    }

    /**
     * Muestra los usuarios ordenados por email, evitando duplicados.
     */
    public void mostrarUsuarios() {
        // TreeSet para evitar duplicados y mostrar usuarios ordenados por email
        TreeSet<Usuario> usuariosOrdenados = new TreeSet<>(Comparator.comparing(Usuario::getEmail));
        usuariosOrdenados.addAll(usuarios.values());
        for (Usuario usuario : usuariosOrdenados) {
            System.out.println(usuario);
        }
    }

    /**
     * Devuelve la lista de cómics (solo para búsquedas y utilidades).
     */
    public List<Comic> getComics() {
        return new ArrayList<>(comics);
    }
}
