package com.mojang.ld22.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SpriteSheet {
    public int width, height;
    public int[] pixels;

    public SpriteSheet() {
        try {
            InputStream resourceAsStream = SpriteSheet.class.getClassLoader().getResourceAsStream("res/icons.png");
            BufferedImage image = ImageIO.read(resourceAsStream);
            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = (pixels[i] & 0xff) / 64;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}