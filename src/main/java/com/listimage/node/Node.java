package com.listimage.node;

import lombok.Getter;
import lombok.Setter;

/**
 * Elemento individual de la lista doblemente enlazada. Encapsula un dato de
 * tipo genérico {@code T} y los punteros al nodo anterior y al nodo siguiente.
 *
 * @param <T> tipo del dato almacenado en el nodo
 */
@Getter
@Setter
public class Node<T> {

    private final T data;

    private Node<T> next;

    private Node<T> previous;

    /**
     * Construye un nodo con el dato indicado, inicializando los punteros
     * {@code next} y {@code previous} como {@code null}.
     *
     * @param data dato almacenado en el nodo; no debe ser nulo
     * @throws IllegalArgumentException si data es nulo
     */
    public Node(T data) {
        if (data == null) {
            throw new IllegalArgumentException("El dato del nodo no puede ser nulo");
        }
        this.data = data;
        this.next = null;
        this.previous = null;
    }
}
