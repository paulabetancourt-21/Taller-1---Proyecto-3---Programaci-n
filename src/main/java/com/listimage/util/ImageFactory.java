package com.listimage.util;

import com.listimage.model.Image;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente utilitario que genera objetos {@code BufferedImage} programáticamente
 * usando {@code java.awt.Graphics2D}, sin depender de archivos externos.
 */
public class ImageFactory {

    private static final int IMAGE_SIZE = 100;

    private static final int IMAGE_COUNT = 5;

    /**
     * Genera las 5 imágenes de prueba en orden, cada una con un color de fondo
     * derivado de su índice y el índice dibujado centrado en blanco.
     *
     * @return lista con las 5 imágenes de prueba generadas
     */
    public List<Image> createTestImages() {
        List<Image> images = new ArrayList<>();
        for (int index = 1; index <= IMAGE_COUNT; index++) {
            images.add(new Image(createSampleImage(IMAGE_SIZE, IMAGE_SIZE, index)));
        }
        return images;
    }

    /**
     * Genera un {@code BufferedImage} de las dimensiones indicadas con un color
     * de fondo derivado del índice y el índice dibujado centrado en blanco.
     *
     * @param width  ancho en píxeles de la imagen
     * @param height alto en píxeles de la imagen
     * @param index  índice de la imagen, usado para el color de fondo y el texto
     * @return la imagen generada
     */
    private BufferedImage createSampleImage(int width, int height, int index) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setColor(new Color((index * 37) % 256, (index * 59) % 256, (index * 83) % 256));
        g2d.fillRect(0, 0, width, height);

        String text = String.valueOf(index);
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);

        g2d.dispose();
        return img;
    }
}