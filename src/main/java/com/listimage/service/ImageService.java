package com.listimage.service;

import com.listimage.model.Image;
import java.util.List;

/**
 * Contrato del servicio de gestión de imágenes.
 */
public interface ImageService {

    /**
     * Agrega una imagen a la lista.
     *
     * @param image la imagen a agregar; no debe ser nula
     * @throws IllegalArgumentException si image es nula
     */
    void add(Image image);

    /**
     * Retorna el número de imágenes registradas.
     *
     * @return conteo de imágenes
     */
    int count();

    /**
     * Retorna todas las imágenes en orden de inserción.
     *
     * @return lista ordenada de imágenes
     */
    List<Image> getAll();
}