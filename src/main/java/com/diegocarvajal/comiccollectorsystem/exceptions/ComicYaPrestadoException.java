package com.diegocarvajal.comiccollectorsystem.exceptions;

/**
 * Excepción personalizada que se lanza cuando se intenta prestar un cómic que ya está prestado.
 * Permite mostrar un mensaje claro al usuario sobre el error.
 */
public class ComicYaPrestadoException extends Exception {
    public ComicYaPrestadoException(String mensaje) {
        super(mensaje);
    }
}
