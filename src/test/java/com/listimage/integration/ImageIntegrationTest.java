package com.listimage.integration;

import com.listimage.list.DoublyLinkedList;
import com.listimage.model.Image;
import com.listimage.service.ImageManager;
import com.listimage.ui.SwingView;
import com.listimage.util.ImageFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageIntegrationTest {

    @Test
    void fullFlowInsertsFiveImagesInOrder() {
        ImageFactory factory = new ImageFactory();
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();
        ImageManager manager = new ImageManager(list);

        List<Image> generated = factory.createTestImages();
        for (Image image : generated) {
            manager.add(image);
        }

        assertEquals(5, manager.count());

        List<Image> all = manager.getAll();
        assertEquals(5, all.size());
        for (int index = 0; index < all.size(); index++) {
            Image image = all.get(index);
            assertTrue(image.getId() > 0);
            assertEquals(all.get(0).getId() + index, image.getId());
            assertNotNull(image.getData());
            assertEquals(100, image.getData().getWidth());
            assertEquals(100, image.getData().getHeight());
        }
    }

    @Test
    void generatedTestImagesHaveDistinctBackgroundsAndUniqueIds() {
        ImageFactory factory = new ImageFactory();

        List<Image> images = factory.createTestImages();

        assertEquals(5, images.size());
        Set<Integer> backgroundPixels = images.stream()
                .map(image -> image.getData().getRGB(0, 0))
                .collect(Collectors.toSet());
        Set<Integer> ids = images.stream().map(Image::getId).collect(Collectors.toSet());
        assertEquals(5, backgroundPixels.size());
        assertEquals(5, ids.size());
        for (Image image : images) {
            assertTrue(image.getId() > 0);
            assertEquals(100, image.getData().getWidth());
            assertEquals(100, image.getData().getHeight());
        }
    }

    @Test
    void swingViewShowsImageWithIntegerIdAndPosition() {
        ImageFactory factory = new ImageFactory();
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();
        ImageManager manager = new ImageManager(list);
        List<Image> generated = factory.createTestImages();
        for (Image image : generated) {
            manager.add(image);
        }
        SwingView view = new SwingView(manager);

        for (int index = 0; index < generated.size(); index++) {
            setCurrentIndex(view, index);
            invokePrivate(view, "updateImage");

            String text = positionTextOf(view);
            assertTrue(text.contains(String.valueOf(generated.get(index).getId())));
            assertTrue(text.contains("Imagen " + (index + 1) + " de 5"));
            ImageIcon icon = imageIconOf(view);
            assertNotNull(icon);
            assertSame(generated.get(index).getData(), icon.getImage());
        }
    }

    private static void setCurrentIndex(SwingView view, int index) {
        try {
            Field field = SwingView.class.getDeclaredField("currentIndex");
            field.setAccessible(true);
            field.setInt(view, index);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("No se pudo asignar currentIndex", exception);
        }
    }

    private static String positionTextOf(SwingView view) {
        return labelOf(view, "positionLabel").getText();
    }

    private static ImageIcon imageIconOf(SwingView view) {
        return (ImageIcon) labelOf(view, "imageLabel").getIcon();
    }

    private static JLabel labelOf(SwingView view, String fieldName) {
        try {
            Field field = SwingView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (JLabel) field.get(view);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("No se pudo leer el campo " + fieldName, exception);
        }
    }

    private static void invokePrivate(SwingView view, String methodName) {
        try {
            Method method = SwingView.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(view);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new AssertionError("No se pudo invocar el método " + methodName, exception);
        }
    }
}
