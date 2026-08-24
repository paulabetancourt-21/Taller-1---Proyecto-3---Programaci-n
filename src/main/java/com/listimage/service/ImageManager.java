package com.listimage.service;

import com.listimage.list.LinkedList;
import com.listimage.model.Image;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Servicio que orquesta las operaciones sobre la lista de imágenes, delegando la
 * estructura de datos en {@link LinkedList}.
 */
@RequiredArgsConstructor
public class ImageManager implements ImageService {

    private final LinkedList<Image> list;

    /**
     * Agrega una imagen al final de la lista.
     *
     * @param image la imagen a agregar; no debe ser nula
     * @throws IllegalArgumentException si image es nula
     */
    @Override
    public void add(Image image) {
        list.addLast(image);
    }

    /**
     * Retorna el número de imágenes registradas.
     *
     * @return conteo de imágenes
     */
    @Override
    public int count() {
        return list.size();
    }

    /**
     * Retorna todas las imágenes en orden de inserción.
     *
     * @return lista ordenada de imágenes
     */
    @Override
    public List<Image> getAll() {
        return list.getAll();
    }
}
