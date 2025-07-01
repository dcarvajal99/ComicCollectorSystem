package com.diegocarvajal.comiccollectorsystem;

import com.diegocarvajal.comiccollectorsystem.controllers.MenuController;
import com.diegocarvajal.comiccollectorsystem.models.TiendaDeComics;
import java.util.Scanner;

/**
 * Clase principal que inicia la aplicación y delega el control al menú principal.
 * Solo contiene el método main para mantener el código limpio y modular.
 */
public class App {

    public static void main(String[] args) {
        // Crea la tienda de cómics (modelo principal de datos)
        TiendaDeComics tiendaDeComics = new TiendaDeComics();
        // Crea el scanner para leer datos del usuario por consola
        Scanner scanner = new Scanner(System.in);
        // Crea el controlador del menú y le pasa la tienda y el scanner
        MenuController menuController = new MenuController(tiendaDeComics, scanner);
        // Inicia el menú principal de la aplicación
        menuController.mostrarMenuPrincipal();
        // Cierra el scanner al terminar
        scanner.close();
    }
}
