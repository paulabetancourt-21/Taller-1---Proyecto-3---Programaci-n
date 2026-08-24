package com.listimage.node;

import com.listimage.model.Image;
import java.awt.image.BufferedImage;
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

class NodeTest {

    @Provide
    Arbitrary<Image> validImages() {
        return Arbitraries.integers().between(1, 50)
                .map(size -> new Image(new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)));
    }

    // Feature: image-doubly-linked-list, Property 4: pointer initialization of a new Node
    @Property(tries = 100)
    void newNodeHasNullPointers(@ForAll("validImages") Image image) {
        Node<Image> node = new Node<>(image);

        assertNull(node.getNext());
        assertNull(node.getPrevious());
    }

    @Test
    void newNodeWithNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Node<Image>(null));
    }

    @Test
    void gettersReturnValuesAssignedBySetters() {
        Image firstImage = new Image(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        Image secondImage = new Image(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB));
        Node<Image> first = new Node<>(firstImage);
        Node<Image> second = new Node<>(secondImage);

        first.setNext(second);
        second.setPrevious(first);

        assertSame(firstImage, first.getData());
        assertSame(secondImage, second.getData());
        assertSame(second, first.getNext());
        assertSame(first, second.getPrevious());
        assertNull(second.getNext());
        assertNull(first.getPrevious());
    }

    @Test
    void gettersReturnTheDataAndInitializedPointers() {
        Image image = new Image(new BufferedImage(15, 15, BufferedImage.TYPE_INT_RGB));
        Node<Image> node = new Node<>(image);

        assertSame(image, node.getData());
        assertNull(node.getNext());
        assertNull(node.getPrevious());
        assertEquals(image.getId(), node.getData().getId());
    }

    @Test
    void genericNodeAcceptsNonImageData() {
        Node<String> node = new Node<>("dato");

        assertSame("dato", node.getData());
        assertNull(node.getNext());
        assertNull(node.getPrevious());
    }
}
