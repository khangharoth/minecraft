package com.mojang.ld22.item.resource;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.tile.Tile;

public class Resource {
    private static int baseValue = 4 * 32;
    public static Resource flower = new PlantableResource("Flower", 0 + baseValue, Color.get(-1, 10, 444, 330), Tile.flower, Tile.grass);
    public static Resource dirt = new PlantableResource("Dirt", 2 + baseValue, Color.get(-1, 100, 322, 432), Tile.dirt, Tile.hole, Tile.water, Tile.lava);
//    public static Resource sand = new PlantableResource("Sand", 2 + baseValue, Color.get(-1, 110, 440, 550), Tile.sand, Tile.grass, Tile.dirt);


    public final String name;
    public final int sprite;
    public final int color;

    public Resource(String name, int sprite, int color) {
        this.name = name;
        this.sprite = sprite;
        this.color = color;
    }
}