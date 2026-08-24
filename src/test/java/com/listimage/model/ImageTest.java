package com.listimage.model;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageTest {

    @Provide
    Arbitrary<BufferedImage> validImages() {
        return Arbitraries.integers().between(1, 50)
                .map(size -> new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB));
    }

    // Feature: image-doubly-linked-list, Property 1: Image construction invariant
    @Property(tries = 100)
    void imageConstructionInvariant(@ForAll("validImages") BufferedImage data) {
        Image image = new Image(data);

        assertTrue(image.getId() > 0);
        assertNotNull(image.getData());
    }

    // Feature: image-doubly-linked-list, Property 2: equality of Image based on id
    @Property(tries = 100)
    void equalityBasedOnId(@ForAll("validImages") BufferedImage data) {
        Image first = new Image(data);
        Image sameId = withId(new Image(data), first.getId());
        Image differentId = new Image(data);

        assertEquals(first, sameId);
        assertEquals(first.hashCode(), sameId.hashCode());
        assertNotEquals(first, differentId);
    }

    // Feature: image-doubly-linked-list, Property 3: toString contains id and excludes binary data
    @Property(tries = 100)
    void toStringContainsIdAndExcludesData(@ForAll("validImages") BufferedImage data) {
        Image image = new Image(data);
        String toString = image.toString();

        assertTrue(toString.contains(String.valueOf(image.getId())));
        assertFalse(toString.contains("data="));
    }

    @Test
    void constructionWithNullDataThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Image(null));
    }

    @Test
    void constructionAssignsConsecutivePositiveIds() {
        BufferedImage data = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        Image first = new Image(data);
        Image second = new Image(data);

        assertTrue(first.getId() > 0);
        assertEquals(first.getId() + 1, second.getId());
        assertSame(data, first.getData());
    }

    @Test
    void toStringDoesNotIncludeDataField() {
        BufferedImage data = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Image image = new Image(data);

        String toString = image.toString();

        assertTrue(toString.contains(String.valueOf(image.getId())));
        assertFalse(toString.contains("data="));
        assertFalse(toString.contains(data.toString()));
    }

    private static Image withId(Image image, int id) {
        try {
            Field idField = Image.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.setInt(image, id);
            return image;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("No se pudo asignar el id a la imagen", exception);
        }
    }
}