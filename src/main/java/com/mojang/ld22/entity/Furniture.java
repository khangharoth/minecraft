package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Screen;

public class Furniture extends Entity {
	private int pushTime = 0;
	private int pushDir = -1;
    int col, sprite;
	public String name;
	private Player shouldTake;

    Furniture(String name) {
		this.name = name;
		xr = 3;
		yr = 3;
	}

	public void tick() {
		if (shouldTake != null) {
			shouldTake = null;
		}
		if (pushDir == 0) move(0, +1);
		if (pushDir == 1) move(0, -1);
		if (pushDir == 2) move(-1, 0);
		if (pushDir == 3) move(+1, 0);
		pushDir = -1;
		if (pushTime > 0) pushTime--;
	}

	public void render(Screen screen) {
		screen.render(x - 8, y - 8 - 4, sprite * 2 + 8 * 32, col, 0);
		screen.render(x - 0, y - 8 - 4, sprite * 2 + 8 * 32 + 1, col, 0);
		screen.render(x - 8, y - 0 - 4, sprite * 2 + 8 * 32 + 32, col, 0);
		screen.render(x - 0, y - 0 - 4, sprite * 2 + 8 * 32 + 33, col, 0);
	}

	public boolean blocks(Entity e) {
		return true;
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player && pushTime == 0) {
			pushDir = ((Player) entity).dir;
			pushTime = 10;
		}
	}

	public void take(Player player) {
		shouldTake = player;
	}
}