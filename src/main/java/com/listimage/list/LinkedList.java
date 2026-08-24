package com.listimage.list;

import java.util.List;

/**
 * Contrato para una lista doblemente enlazada genérica.
 *
 * @param <T> tipo del elemento almacenado en la lista
 */
public interface LinkedList<T> {

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param element el elemento a agregar; no debe ser nulo
     * @throws IllegalArgumentException si element es nulo
     */
    void addLast(T element);

    /**
     * Retorna el número de elementos en la lista.
     *
     * @return tamaño actual de la lista
     */
    int size();

    /**
     * Retorna todos los elementos en orden de inserción (head → tail).
     *
     * @return lista de elementos en orden de inserción
     */
    List<T> getAll();
}
