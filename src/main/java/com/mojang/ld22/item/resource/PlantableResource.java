package com.mojang.ld22.item.resource;

import java.util.Arrays;
import java.util.List;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

public class PlantableResource extends Resource {
	private List<Tile> sourceTiles;
	private Tile targetTile;

	public PlantableResource(String name, int sprite, int color, Tile targetTile, Tile... sourceTiles1) {
		super(name, sprite, color);
        this.sourceTiles = Arrays.asList(sourceTiles1);
		this.targetTile = targetTile;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		if (sourceTiles.contains(tile)) {
			level.setTile(xt, yt, targetTile, 0);
			return true;
		}
		return false;
	}
}
