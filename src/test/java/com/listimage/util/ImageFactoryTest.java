package com.listimage.util;

import com.listimage.model.Image;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageFactoryTest {

    private final ImageFactory factory = new ImageFactory();

    @Test
    void createdTestImagesHave100x100Dimensions() {
        List<Image> images = factory.createTestImages();

        for (Image image : images) {
            assertEquals(100, image.getData().getWidth());
            assertEquals(100, image.getData().getHeight());
        }
    }

    @Test
    void createTestImagesReturnsExactlyFiveImages() {
        List<Image> images = factory.createTestImages();

        assertEquals(5, images.size());
    }

    @Test
    void createdTestImagesHaveUniqueConsecutiveIds() {
        List<Image> images = factory.createTestImages();
        Set<Integer> ids = new HashSet<>();

        for (Image image : images) {
            assertTrue(image.getId() > 0);
            ids.add(image.getId());
        }
        assertEquals(5, ids.size());
        for (int index = 0; index < images.size(); index++) {
            assertEquals(images.get(0).getId() + index, images.get(index).getId());
        }
    }

    @Test
    void createdTestImagesHaveNonNullBufferedImages() {
        List<Image> images = factory.createTestImages();

        for (Image image : images) {
            assertNotNull(image.getData());
            assertEquals(100, image.getData().getWidth());
            assertEquals(100, image.getData().getHeight());
        }
    }
}