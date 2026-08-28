package com.listimage.exceptions;

/**
 * Excepción lanzada cuando se proporciona un dato o elemento inválido,
 * por ejemplo cuando es nulo y se requiere un valor no nulo.
 */
public class InvalidDataException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo del error.
     *
     * @param message mensaje que describe la causa del error
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Construye la excepción con un mensaje descriptivo y la causa original
     * que la originó.
     *
     * @param mensaje mensaje que describe la causa del error
     * @param causa excepción original que provocó este error
     */
    public InvalidDataException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}