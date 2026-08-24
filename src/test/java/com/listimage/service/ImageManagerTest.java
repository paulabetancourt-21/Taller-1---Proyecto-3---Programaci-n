package com.listimage.service;

import com.listimage.list.DoublyLinkedList;
import com.listimage.list.LinkedList;
import com.listimage.model.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageManagerTest {

    @Provide
    Arbitrary<Image> validImages() {
        return Arbitraries.integers().between(1, 50)
                .map(size -> new Image(new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)));
    }

    @Provide
    Arbitrary<List<Image>> imageLists() {
        return validImages().list().ofMinSize(1).ofMaxSize(30);
    }

    // Feature: image-doubly-linked-list, Property 6: getAll preserves insertion order
    @Property(tries = 100)
    void getAllPreservesInsertionOrder(@ForAll("imageLists") List<Image> images) {
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();
        ImageManager manager = new ImageManager(list);

        images.forEach(manager::add);

        assertEquals(images, manager.getAll());
    }

    @Test
    void addDelegatesToLinkedList() {
        @SuppressWarnings("unchecked")
        LinkedList<Image> list = mock(LinkedList.class);
        ImageManager manager = new ImageManager(list);
        Image image = new Image(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));

        manager.add(image);

        verify(list).addLast(image);
    }

    @Test
    void addWithNullImagePropagatesIllegalArgumentException() {
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();
        ImageManager manager = new ImageManager(list);

        assertThrows(IllegalArgumentException.class, () -> manager.add(null));
        assertEquals(0, manager.count());
    }

    @Test
    void countReturnsListSize() {
        @SuppressWarnings("unchecked")
        LinkedList<Image> list = mock(LinkedList.class);
        ImageManager manager = new ImageManager(list);
        when(list.size()).thenReturn(3);

        assertEquals(3, manager.count());
    }

    @Test
    void getAllReturnsImagesFromList() {
        @SuppressWarnings("unchecked")
        LinkedList<Image> list = mock(LinkedList.class);
        ImageManager manager = new ImageManager(list);
        Image image = new Image(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        when(list.getAll()).thenReturn(List.of(image));

        assertEquals(List.of(image), manager.getAll());
    }
}
