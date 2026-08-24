package com.listimage;

import com.listimage.list.DoublyLinkedList;
import com.listimage.list.LinkedList;
import com.listimage.model.Image;
import com.listimage.service.ImageManager;
import com.listimage.ui.SwingView;
import com.listimage.util.ImageFactory;
import java.util.List;

/**
 * Punto de entrada de la aplicación. Conecta las capas de estructura de datos,
 * servicio y presentación mediante inyección de dependencias manual.
 */
public final class Main {

    private Main() {
    }

    /**
     * Inicia la aplicación generando las 5 imágenes de prueba, agregándolas al
     * servicio y lanzando la interfaz gráfica basada en JOptionPane.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ImageFactory factory = new ImageFactory();
        LinkedList<Image> list = new DoublyLinkedList<>();
        ImageManager service = new ImageManager(list);
        List<Image> images = factory.createTestImages();
        images.forEach(service::add);
        SwingView view = new SwingView(service);
        view.run();
    }
}
