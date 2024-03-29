package com.mojang.ld22.level;

import com.mojang.ld22.entity.*;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.levelgen.LevelGen;
import com.mojang.ld22.level.tile.Tile;

import java.util.*;

public class Level {
    public int w = 128;
    public int h = 128;
    public int grassColor = 141;
    public int dirtColor = 322;
    public int sandColor = 550;
    public int monsterDensity = 8;
    public Player player;
    private byte[] tiles;
    private byte[] data;
    private List<Entity>[] entitiesInTiles;
    private List<Entity> entities = new ArrayList<Entity>();
    private Comparator<Entity> spriteSorter = (e0, e1) -> {
        if (e1.y < e0.y) return +1;
        if (e1.y > e0.y) return -1;
        return 0;
    };
    private List<Entity> rowSprites = new ArrayList<Entity>();

    @SuppressWarnings("unchecked")
    public Level(int level, Level parentLevel) {
        if (level < 0) {
            dirtColor = 222;
        }
        byte[][] maps = LevelGen.createAndValidateTopMap(w, h);

        tiles = maps[0];
        data = maps[1];

        entitiesInTiles = new ArrayList[w * h];
        for (int i = 0; i < w * h; i++) {
            entitiesInTiles[i] = new ArrayList<Entity>();
        }

        if (level == 1) {
            AirWizard aw = new AirWizard();
            aw.x = w * 8;
            aw.y = h * 8;
            add(aw);
        }
    }

    public void renderBackground(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.w + 15) >> 4;
        int h = (screen.h + 15) >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                getTile(x, y).render(screen, this, x, y);
            }
        }
        screen.setOffset(0, 0);
    }

    public void renderSprites(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.w + 15) >> 4;
        int h = (screen.h + 15) >> 4;

        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                if (x < 0 || y < 0 || x >= this.w || y >= this.h) continue;
                rowSprites.addAll(entitiesInTiles[x + y * this.w]);
            }
            if (rowSprites.size() > 0) {
                sortAndRender(screen, rowSprites);
            }
            rowSprites.clear();
        }
        screen.setOffset(0, 0);
    }

    private void sortAndRender(Screen screen, List<Entity> list) {
        Collections.sort(list, spriteSorter);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).render(screen);
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.rock;
        return Tile.tiles[tiles[x + y * w]];
    }

    public void setTile(int x, int y, Tile t, int dataVal) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        tiles[x + y * w] = t.id;
        data[x + y * w] = (byte) dataVal;
    }

    public int getData(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return 0;
        return data[x + y * w] & 0xff;
    }

    public void setData(int x, int y, int val) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        data[x + y * w] = (byte) val;
    }

    public void add(Entity entity) {
        if (entity instanceof Player) {
            player = (Player) entity;
        }
        entity.removed = false;
        entities.add(entity);
        entity.init(this);

        insertEntity(entity.x >> 4, entity.y >> 4, entity);
    }

    private void insertEntity(int x, int y, Entity e) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        entitiesInTiles[x + y * w].add(e);
    }

    private void removeEntity(int x, int y, Entity e) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        entitiesInTiles[x + y * w].remove(e);
    }

    private void trySpawn() {
        Mob mob = new Slime(1);

        if (mob.findStartPos(this)) {
            this.add(mob);
        }
    }

    public void tick() {
        trySpawn();

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            int xto = e.x >> 4;
            int yto = e.y >> 4;

            e.tick();

            if (e.removed) {
                entities.remove(i--);
                removeEntity(xto, yto, e);
            } else {
                int xt = e.x >> 4;
                int yt = e.y >> 4;

                if (xto != xt || yto != yt) {
                    removeEntity(xto, yto, e);
                    insertEntity(xt, yt, e);
                }
            }
        }
    }

    public List<Entity> getEntities(int x0, int y0, int x1, int y1) {
        List<Entity> result = new ArrayList<Entity>();
        int xt0 = (x0 >> 4) - 1;
        int yt0 = (y0 >> 4) - 1;
        int xt1 = (x1 >> 4) + 1;
        int yt1 = (y1 >> 4) + 1;
        for (int y = yt0; y <= yt1; y++) {
            for (int x = xt0; x <= xt1; x++) {
                if (x < 0 || y < 0 || x >= w || y >= h) continue;
                List<Entity> entities = entitiesInTiles[x + y * this.w];
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    if (e.intersects(x0, y0, x1, y1)) result.add(e);
                }
            }
        }
        return result;
    }
}