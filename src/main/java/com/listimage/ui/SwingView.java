package com.listimage.ui;

import com.listimage.model.Image;
import com.listimage.service.ImageService;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Capa de presentación que muestra una única ventana ({@code JFrame}) con la
 * imagen actual en la parte superior, el texto de posición y los botones
 * "Anterior", "Siguiente" y "Cerrar" en la parte inferior.
 */
public class SwingView {

    private static final String WINDOW_TITLE = "Navegación de Imágenes";

    private static final String NO_MORE_IMAGES_MESSAGE = "No hay más imágenes";

    private static final String NO_IMAGES_MESSAGE = "No hay imágenes registradas";

    private final ImageService service;

    private int currentIndex;

    private int totalImages;

    private final JLabel imageLabel;

    private final JLabel positionLabel;

    /**
     * Construye la vista con el servicio de gestión de imágenes.
     *
     * @param service servicio de gestión de imágenes
     */
    public SwingView(ImageService service) {
        this.service = service;
        this.currentIndex = 0;
        this.totalImages = service.count();
        this.imageLabel = new JLabel("", SwingConstants.CENTER);
        this.positionLabel = new JLabel("", SwingConstants.CENTER);
    }

    /**
     * Construye y muestra la ventana única con la imagen, el texto de posición
     * y los botones de navegación.
     */
    public void run() {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(imageLabel, BorderLayout.CENTER);
        frame.add(positionLabel, BorderLayout.NORTH);
        frame.add(buildButtonPanel(), BorderLayout.SOUTH);
        updateImage();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.add(createButton("Anterior", this::showPrevious));
        panel.add(createButton("Siguiente", this::showNext));
        panel.add(createButton("Cerrar", () -> System.exit(0)));
        return panel;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(event -> action.run());
        return button;
    }

    private void showNext() {
        if (totalImages == 0) {
            positionLabel.setText(NO_IMAGES_MESSAGE);
            return;
        }
        if (currentIndex < totalImages - 1) {
            currentIndex++;
            updateImage();
        } else {
            positionLabel.setText(NO_MORE_IMAGES_MESSAGE);
        }
    }

    private void showPrevious() {
        if (totalImages == 0) {
            positionLabel.setText(NO_IMAGES_MESSAGE);
            return;
        }
        if (currentIndex > 0) {
            currentIndex--;
            updateImage();
        } else {
            positionLabel.setText(NO_MORE_IMAGES_MESSAGE);
        }
    }

    private void updateImage() {
        try {
            List<Image> images = service.getAll();
            if (images.isEmpty()) {
                positionLabel.setText(NO_IMAGES_MESSAGE);
                imageLabel.setIcon(null);
                return;
            }
            Image image = images.get(currentIndex);
            imageLabel.setIcon(new ImageIcon(image.getData()));
            positionLabel.setText(buildMessage(image, currentIndex + 1, totalImages));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String buildMessage(Image image, int position, int total) {
        return String.format("Imagen %d de %d: %d", position, total, image.getId());
    }
}
