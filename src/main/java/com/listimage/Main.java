package com.listimage;

import com.listimage.list.DoublyLinkedList;
import com.listimage.list.LinkedList;
import com.listimage.model.Image;
import com.listimage.service.ImageManager;
import com.listimage.ui.SwingView;
import com.listimage.util.ImageFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada de la aplicación. Conecta las capas de estructura de datos,
 * servicio y presentación mediante inyección de dependencias manual.
 */
public final class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    /**
     * Inicia la aplicación generando las 5 imágenes de prueba, agregándolas al
     * servicio y lanzando la interfaz gráfica basada en JOptionPane.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        log.info("Iniciando aplicación de navegación de imágenes");
        ImageFactory factory = new ImageFactory();
        LinkedList<Image> list = new DoublyLinkedList<>();

        ImageManager service = new ImageManager(list);
        List<Image> images = factory.createTestImages();
        images.forEach(service::add);
        
        log.info("Se cargaron 5 imágenes de prueba");
        SwingView view = new SwingView(service);
        view.run();
    }
}
