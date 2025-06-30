package com.grupo5.comiccollectorsystem;

import com.grupo5.comiccollectorsystem.controllers.MenuController;
import com.grupo5.comiccollectorsystem.models.TiendaDeComics;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        TiendaDeComics tiendaDeComics = new TiendaDeComics();
        Scanner scanner = new Scanner(System.in);
        MenuController menuController = new MenuController(tiendaDeComics, scanner);
        menuController.mostrarMenuPrincipal();
        scanner.close();
    }
}

