package com.diegocarvajal.comiccollectorsystem.exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un cómic en la tienda.
 * Permite mostrar un mensaje claro al usuario sobre el error.
 */
public class ComicNoEncontradoException extends Exception {
    public ComicNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
