package com.listimage.ui;

import com.listimage.model.Image;
import com.listimage.service.ImageService;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SwingViewTest {

    @Provide
    Arbitrary<List<Image>> nonEmptyImageLists() {
        return arbitraryImages().list().ofMinSize(1).ofMaxSize(20);
    }

    @Provide
    Arbitrary<List<Image>> atLeastTwoImageLists() {
        return arbitraryImages().list().ofMinSize(2).ofMaxSize(20);
    }

    // Feature: image-doubly-linked-list, Property 8: visualization shows each image with its id and position
    @Property(tries = 100)
    void visualizationShowsEachImageWithIdAndPosition(@ForAll("nonEmptyImageLists") List<Image> images) {
        SwingView view = new SwingView(serviceWithImages(images));

        for (int index = 0; index < images.size(); index++) {
            setCurrentIndex(view, index);
            invokePrivate(view, "updateImage");

            String text = positionTextOf(view);
            assertTrue(text.contains(String.valueOf(images.get(index).getId())));
            assertTrue(text.contains("Imagen " + (index + 1) + " de " + images.size()));
            ImageIcon icon = imageIconOf(view);
            assertNotNull(icon);
            assertSame(images.get(index).getData(), icon.getImage());
        }
    }

    // Feature: image-doubly-linked-list, Property 9: next navigates each image once in order
    @Property(tries = 100)
    void nextVisitsEachImageInOrderWithoutRepeating(@ForAll("atLeastTwoImageLists") List<Image> images) {
        SwingView view = new SwingView(serviceWithImages(images));

        for (int step = 0; step < images.size() - 1; step++) {
            invokePrivate(view, "showNext");

            String text = positionTextOf(view);
            assertTrue(text.contains(String.valueOf(images.get(step + 1).getId())));
            assertTrue(text.contains("Imagen " + (step + 2) + " de " + images.size()));
        }

        invokePrivate(view, "showNext");
        assertEquals("No hay más imágenes", positionTextOf(view));
        assertEquals(images.size() - 1, currentIndexOf(view));
    }

    // Feature: image-doubly-linked-list, Property 10: previous navigates each image once in reverse
    @Property(tries = 100)
    void previousVisitsEachImageInReverseWithoutRepeating(@ForAll("atLeastTwoImageLists") List<Image> images) {
        SwingView view = new SwingView(serviceWithImages(images));
        setCurrentIndex(view, images.size() - 1);

        for (int step = 0; step < images.size() - 1; step++) {
            invokePrivate(view, "showPrevious");

            int index = images.size() - 2 - step;
            String text = positionTextOf(view);
            assertTrue(text.contains(String.valueOf(images.get(index).getId())));
            assertTrue(text.contains("Imagen " + (index + 1) + " de " + images.size()));
        }

        invokePrivate(view, "showPrevious");
        assertEquals("No hay más imágenes", positionTextOf(view));
        assertEquals(0, currentIndexOf(view));
    }

    @Test
    void nextAtLastImageShowsNoMoreImagesMessage() {
        SwingView view = new SwingView(serviceWithThreeImages());
        setCurrentIndex(view, 2);

        invokePrivate(view, "showNext");

        assertEquals("No hay más imágenes", positionTextOf(view));
        assertEquals(2, currentIndexOf(view));
    }

    @Test
    void previousAtFirstImageShowsNoMoreImagesMessage() {
        SwingView view = new SwingView(serviceWithThreeImages());

        invokePrivate(view, "showPrevious");

        assertEquals("No hay más imágenes", positionTextOf(view));
        assertEquals(0, currentIndexOf(view));
    }

    @Test
    void emptyListShowsNoImagesMessage() {
        ImageService service = mock(ImageService.class);
        when(service.count()).thenReturn(0);
        when(service.getAll()).thenReturn(List.of());
        SwingView view = new SwingView(service);

        invokePrivate(view, "updateImage");

        assertEquals("No hay imágenes registradas", positionTextOf(view));
    }

    @Test
    void serviceExceptionShowsErrorMessage() {
        ImageService service = mock(ImageService.class);
        when(service.count()).thenReturn(1);
        when(service.getAll()).thenThrow(new IllegalStateException("Error inesperado del servicio"));
        SwingView view = new SwingView(service);

        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            invokePrivate(view, "updateImage");

            mocked.verify(() -> JOptionPane.showMessageDialog(
                    isNull(), eq("Error inesperado del servicio"), eq("Error"), eq(JOptionPane.ERROR_MESSAGE)));
        }
    }

    private static Arbitrary<Image> arbitraryImages() {
        return Arbitraries.integers().between(1, 50)
                .map(size -> new Image(new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)));
    }

    private static ImageService serviceWithImages(List<Image> images) {
        ImageService service = mock(ImageService.class);
        when(service.count()).thenReturn(images.size());
        when(service.getAll()).thenReturn(images);
        return service;
    }

    private static ImageService serviceWithThreeImages() {
        List<Image> images = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            images.add(new Image(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)));
        }
        return serviceWithImages(images);
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

    private static int currentIndexOf(SwingView view) {
        try {
            Field field = SwingView.class.getDeclaredField("currentIndex");
            field.setAccessible(true);
            return field.getInt(view);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("No se pudo leer currentIndex", exception);
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
