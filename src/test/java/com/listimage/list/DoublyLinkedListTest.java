package com.listimage.list;

import com.listimage.model.Image;
import com.listimage.node.Node;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoublyLinkedListTest {

    @Provide
    Arbitrary<Image> validImages() {
        return Arbitraries.integers().between(1, 50)
                .map(size -> new Image(new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)));
    }

    @Provide
    Arbitrary<List<Image>> imageLists() {
        return validImages().list().ofMaxSize(30);
    }

    // Feature: image-doubly-linked-list, Property 5: addLast structural invariant
    @Property(tries = 100)
    void addLastStructuralInvariant(@ForAll("imageLists") List<Image> initialImages,
                                    @ForAll("validImages") Image newImage) {
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();
        initialImages.forEach(list::addLast);
        int initialSize = list.size();

        list.addLast(newImage);

        assertEquals(initialSize + 1, list.size());
        assertSame(newImage, tailOf(list).getData());

        if (initialSize == 0) {
            assertSame(headOf(list), tailOf(list));
            assertNull(tailOf(list).getPrevious());
        } else {
            assertSame(tailOf(list).getPrevious().getData(), initialImages.get(initialSize - 1));
            assertSame(tailOf(list), tailOf(list).getPrevious().getNext());
        }
    }

    @Test
    void addLastWithNullImageThrowsIllegalArgumentException() {
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();

        assertThrows(IllegalArgumentException.class, () -> list.addLast(null));
    }

    @Test
    void emptyListReturnsSizeZero() {
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();

        assertEquals(0, list.size());
    }

    @Test
    void emptyListGetAllReturnsEmptyList() {
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();

        assertTrue(list.getAll().isEmpty());
    }

    @Test
    void twoConsecutiveInsertionsLinkPointersCorrectly() {
        Image firstImage = new Image(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        Image secondImage = new Image(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB));
        DoublyLinkedList<Image> list = new DoublyLinkedList<>();

        list.addLast(firstImage);
        list.addLast(secondImage);

        assertEquals(2, list.size());
        assertSame(firstImage, headOf(list).getData());
        assertSame(secondImage, tailOf(list).getData());
        assertSame(tailOf(list), headOf(list).getNext());
        assertSame(headOf(list), tailOf(list).getPrevious());
        assertNull(headOf(list).getPrevious());
        assertNull(tailOf(list).getNext());
        assertEquals(List.of(firstImage, secondImage), list.getAll());
    }

    @Test
    void genericListAcceptsNonImageData() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();

        list.addLast("uno");
        list.addLast("dos");

        assertEquals(2, list.size());
        assertEquals(List.of("uno", "dos"), list.getAll());
    }

    @SuppressWarnings("unchecked")
    private static Node<Image> headOf(DoublyLinkedList<Image> list) {
        return (Node<Image>) readField(list, "head");
    }

    @SuppressWarnings("unchecked")
    private static Node<Image> tailOf(DoublyLinkedList<Image> list) {
        return (Node<Image>) readField(list, "tail");
    }

    private static Object readField(DoublyLinkedList<?> list, String fieldName) {
        try {
            Field field = DoublyLinkedList.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(list);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("No se pudo acceder al campo " + fieldName, exception);
        }
    }
}
