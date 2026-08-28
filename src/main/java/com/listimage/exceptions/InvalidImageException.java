package com.listimage.exceptions;

/**
 * Excepción lanzada cuando se intenta crear o utilizar una imagen inválida,
 * por ejemplo cuando su contenido {@code BufferedImage} es nulo.
 */
public class InvalidImageException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo del error.
     *
     * @param message mensaje que describe la causa del error
     */
    public InvalidImageException(String message) {
        super(message);
    }

    /**
     * Construye la excepción con un mensaje descriptivo y la causa original
     * que la originó.
     *
     * @param mensaje mensaje que describe la causa del error
     * @param causa excepción original que provocó este error
     */
    public InvalidImageException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}