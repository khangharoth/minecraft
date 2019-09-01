package com.mojang.ld22.level.levelgen;

import com.mojang.ld22.level.tile.Tile;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelGenTest {

    @Test
    public void shouldGenerateLevels() {
        int d = 0;
        while (true) {
            int w = 128;
            int h = 128;

            byte[] map = LevelGen.createAndValidateTopMap(w, h)[0];
//             byte[] map = LevelGen.createAndValidateUndergroundMap(w, h, (d++ % 3) + 1)[0];
//             byte[] map = LevelGen.createAndValidateSkyMap(w, h)[0];

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = x + y * w;

                    if (map[i] == Tile.water.id) pixels[i] = 0x000080;
                    if (map[i] == Tile.grass.id) pixels[i] = 0x208020;
                    if (map[i] == Tile.rock.id) pixels[i] = 0xa0a0a0;
                    if (map[i] == Tile.dirt.id) pixels[i] = 0x604040;
                    if (map[i] == Tile.sand.id) pixels[i] = 0xa0a040;
                    if (map[i] == Tile.tree.id) pixels[i] = 0x003000;
                    if (map[i] == Tile.lava.id) pixels[i] = 0xff2020;
                    if (map[i] == Tile.cloud.id) pixels[i] = 0xa0a0a0;
                    if (map[i] == Tile.stairsDown.id) pixels[i] = 0xffffff;
                    if (map[i] == Tile.stairsUp.id) pixels[i] = 0xffffff;
                    if (map[i] == Tile.cloudCactus.id) pixels[i] = 0xff00ff;
                }
            }
            img.setRGB(0, 0, w, h, pixels, 0, w);
            JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(w * 4, h * 4, Image.SCALE_AREA_AVERAGING)));
        }
    }
}
