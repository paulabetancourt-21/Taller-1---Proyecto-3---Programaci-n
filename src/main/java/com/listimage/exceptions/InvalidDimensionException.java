package com.listimage.exceptions;

/**
 * Excepción lanzada cuando se intenta crear una imagen con dimensiones
 * (ancho o alto) inválidas, es decir, menores o iguales a cero.
 */
public class InvalidDimensionException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo del error.
     *
     * @param message mensaje que describe la causa del error
     */
    public InvalidDimensionException(String message) {
        super(message);
    }

    /**
     * Construye la excepción con un mensaje descriptivo y la causa original
     * que la originó.
     *
     * @param mensaje mensaje que describe la causa del error
     * @param causa excepción original que provocó este error
     */
    public InvalidDimensionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}