package com.grupo5.comiccollectorsystem.models;

import com.grupo5.comiccollectorsystem.services.ComicsServicio;
import com.grupo5.comiccollectorsystem.services.UsuariosServicio;
import com.grupo5.comiccollectorsystem.exceptions.ComicNoEncontradoException;
import com.grupo5.comiccollectorsystem.exceptions.ComicYaPrestadoException;

import java.util.*;

public class TiendaDeComics {
    private ArrayList<Comic> comics = new ArrayList<>();
    private HashMap<String, Usuario> usuarios = new HashMap<>();
    private ComicsServicio comicsServicio = new ComicsServicio();
    private UsuariosServicio usuariosServicio = new UsuariosServicio();

    public TiendaDeComics() {
        cargarComicsDesdeCSV("src/main/java/com/grupo5/comiccollectorsystem/data/comics.csv");
        cargarUsuariosDesdeCSV("src/main/java/com/grupo5/comiccollectorsystem/data/usuarios.csv");
    }

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

    private void cargarUsuariosDesdeCSV(String rutaArchivo) {
        List<String[]> datos = usuariosServicio.leerUsuariosCSV(rutaArchivo);
        boolean esPrimera = true;
        for (String[] fila : datos) {
            if (esPrimera) { esPrimera = false; continue; } // Saltar cabecera
            if (fila.length == 5) {
                // fila[0]=id, fila[1]=nombre, fila[2]=apellido, fila[3]=email, fila[4]=telefono
                Usuario usuario = new Usuario(fila[1], fila[2], fila[3], fila[4]);
                usuarios.put(usuario.getEmail(), usuario);
            }
        }
    }

    public void agregarComic(Comic comic) {
        comics.add(comic);
    }

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

    public void registrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    public Comic buscarComic(String titulo) throws ComicNoEncontradoException {
        for (Comic comic : comics) {
            if (comic.getTitulo().equalsIgnoreCase(titulo)) {
                return comic;
            }
        }
        throw new ComicNoEncontradoException("Comic no encontrado: " + titulo);
    }

    public void prestarComic(String titulo, String emailUsuario) throws ComicNoEncontradoException, ComicYaPrestadoException {
        Comic comic = buscarComic(titulo);
        if (!comic.getEstado()) {
            throw new ComicYaPrestadoException("El comic ya está prestado: " + titulo);
        }
        comic.setEstado(false);
        comic.setAsignadoA(emailUsuario);
        System.out.println("Comic prestado con éxito: " + titulo + " a " + emailUsuario);
    }

    public Usuario getUsuarioPorEmail(String email) {
        return usuarios.get(email);
    }

    public void guardarComicsEnCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
        datos.add(new String[]{"id","titulo","autor","estado","asignadoA"});
        for (Comic comic : comics) {
            datos.add(new String[]{comic.getId(), comic.getTitulo(), comic.getAutor(), comic.getEstado().toString(), comic.getAsignadoA() == null ? "" : comic.getAsignadoA()});
        }
        comicsServicio.escribirComicsCSV(rutaArchivo, datos);
    }

    public void guardarUsuariosEnCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
        datos.add(new String[]{"nombre","apellido","email","telefono"});
        for (Usuario usuario : usuarios.values()) {
            datos.add(new String[]{usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getTelefono()});
        }
        usuariosServicio.escribirUsuariosCSV(rutaArchivo, datos);
    }

    public void mostrarComics() {

        TreeSet<Comic> comicsOrdenados = new TreeSet<>(Comparator.comparing(Comic::getId));
        comicsOrdenados.addAll(comics);
        for (Comic comic : comicsOrdenados) {
            System.out.println(comic);
        }
    }

    public void mostrarUsuarios() {

        HashSet<String> emails = new HashSet<>();
        for (Usuario usuario : usuarios.values()) {
            if (!emails.contains(usuario.getEmail())) {
                System.out.println(usuario);
                emails.add(usuario.getEmail());
            }
        }
    }
}
