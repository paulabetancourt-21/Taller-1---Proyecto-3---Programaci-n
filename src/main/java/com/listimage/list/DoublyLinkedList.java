package com.listimage.list;

import com.listimage.node.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Lista doblemente enlazada genérica. Mantiene los punteros {@code head} y
 * {@code tail} y el tamaño de la lista para garantizar operaciones de inserción
 * al final y recorridos en orden de llegada.
 *
 * @param <T> tipo del elemento almacenado en la lista
 */
public class DoublyLinkedList<T> implements LinkedList<T> {

    private Node<T> head;

    private Node<T> tail;

    private int size;

    /**
     * Agrega un elemento como nuevo último elemento de la lista, actualizando el
     * puntero {@code tail} y enlazando el antiguo último nodo con el nuevo.
     *
     * @param element el elemento a agregar; no debe ser nulo
     * @throws IllegalArgumentException si element es nulo
     */
    @Override
    public void addLast(T element) {
        if (element == null) {
            throw new IllegalArgumentException("El elemento a insertar no puede ser nulo");
        }
        Node<T> newNode = new Node<>(element);
        if (size == 0) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
        }
        tail = newNode;
        size++;
    }

    /**
     * Retorna el número de elementos en la lista.
     *
     * @return tamaño actual de la lista
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Retorna todos los elementos en orden desde {@code head} hasta {@code tail}.
     *
     * @return lista de elementos en orden de inserción
     */
    @Override
    public List<T> getAll() {
        List<T> elements = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            elements.add(current.getData());
            current = current.getNext();
        }
        return elements;
    }
}
