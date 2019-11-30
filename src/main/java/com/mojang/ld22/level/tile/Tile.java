package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.level.Level;

import java.util.Random;

public class Tile {
    public static int tickCount = 0;
    public static Tile[] tiles = new Tile[256];
    public static Tile grass = new GrassTile(0);


    public static Tile rock = new RockTile(1);
    public static Tile water = new WaterTile(2);
    public static Tile flower = new FlowerTile(3);
    public static Tile tree = new TreeTile(4);
    public static Tile dirt = new DirtTile(5);
    public static Tile sand = new SandTile(6);
    public static Tile cactus = new CactusTile(7);
    public static Tile hole = new HoleTile(8);

    public static Tile farmland = new FarmTile(11);
    public static Tile lava = new LavaTile(13);
    public static Tile stairsDown = new StairsTile(14, false);
    public final byte id;
    protected Random random = new Random();
    boolean connectsToGrass = false;
    boolean connectsToSand = false;
    boolean connectsToLava = false;
    boolean connectsToWater = false;

    public Tile(int id) {
        this.id = (byte) id;
        if (tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
        tiles[id] = this;
    }

    public void render(Screen screen, Level level, int x, int y) {
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return true;
    }

    public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
    }

    public void bumpedInto(Level level, int xt, int yt, Entity entity) {
    }

    public void steppedOn(Level level, int xt, int yt, Entity entity) {
    }

    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        return false;
    }


    public boolean connectsToLiquid() {
        return connectsToWater || connectsToLava;
    }
}