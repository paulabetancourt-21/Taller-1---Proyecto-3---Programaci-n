package com.listimage.model;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Entidad de dominio que representa una imagen cargada completamente en memoria,
 * con su identificador entero consecutivo y su contenido como {@code BufferedImage}.
 */
@Getter
@ToString(exclude = "data")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @EqualsAndHashCode.Include
    private final int id;

    private final BufferedImage data;

    /**
     * Construye una imagen con el contenido indicado, asignando automáticamente
     * un identificador entero consecutivo a partir de 1.
     *
     * @param data contenido de la imagen cargado en memoria; no debe ser nulo
     * @throws IllegalArgumentException si data es nulo
     */
    public Image(BufferedImage data) {
        if (data == null) {
            throw new IllegalArgumentException("El contenido BufferedImage no puede ser nulo");
        }
        this.id = counter.incrementAndGet();
        this.data = data;
    }
}