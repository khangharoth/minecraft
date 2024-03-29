package com.mojang.ld22;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.gfx.SpriteSheet;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.screen.*;
import com.mojang.ld22.screen.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    public int gameTime = 0;
    public Player player;
    public Menu menu;
    private BufferedImage image = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private boolean running = false;
    private Screen screen;
    private InputHandler input = new InputHandler(this);
    private int[] colors = new int[256];
    private int tickCount = 0;
    private Level level;
    private int playerDeadTime;
    private int wonTimer = 0;
    private boolean hasWon = false;

    public static void main(String[] args) {
        Game game = new Game();
        game.setMinimumSize(Constants.GAME_DIM);
        game.setMaximumSize(Constants.GAME_DIM);
        game.setPreferredSize(Constants.GAME_DIM);

        JFrame frame = new JFrame("Minicraft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLocation(0, 0);
        frame.setVisible(true);

        game.start();
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        if (menu != null) menu.init(this, input);
    }

    private void start() {
        running = true;
        new Thread(this).start();
    }

    public void resetGame() {
        playerDeadTime = 0;
        wonTimer = 0;
        gameTime = 0;
        hasWon = false;

        level = new Level(0, null);
        player = new Player(this, input);
        player.findStartPos(level);

        level.add(player);
    }

    private void init() {
        int pp = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);
                    int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

                    int r1 = ((rr + mid) / 2) * 230 / 255 + 10;
                    int g1 = ((gg + mid) / 2) * 230 / 255 + 10;
                    int b1 = ((bb + mid) / 2) * 230 / 255 + 10;
                    colors[pp++] = r1 << 16 | g1 << 8 | b1;

                }
            }
        }

        SpriteSheet sheet = new SpriteSheet();
        screen = new Screen(Constants.WIDTH, Constants.HEIGHT, sheet);


        resetGame();
        setMenu(new TitleMenu());
    }

    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 60;
        long lastTimer1 = System.currentTimeMillis();

        init();

        while (running) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (unprocessed >= 1) {
                tick();
                unprocessed -= 1;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            render();

            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;
            }
        }
    }

    public void tick() {
        tickCount++;
        if (!hasFocus()) {
            input.releaseAll();
        } else {
            if (!player.removed && !hasWon) gameTime++;

            input.tick();
            if (menu != null) {
                menu.tick();
            } else {
                if (player.removed) {
                    playerDeadTime++;
                    if (playerDeadTime > 60) {
                        setMenu(new DeadMenu());
                    }
                }
                if (wonTimer > 0) {
                    if (--wonTimer == 0) {
                        setMenu(new WonMenu());
                    }
                }
                level.tick();
                Tile.tickCount++;
            }
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        int xScroll = player.x - screen.w / 2;
        int yScroll = player.y - (screen.h - 8) / 2;
        if (xScroll < 16) xScroll = 16;
        if (yScroll < 16) yScroll = 16;
        if (xScroll > level.w * 16 - screen.w - 16) xScroll = level.w * 16 - screen.w - 16;
        if (yScroll > level.h * 16 - screen.h - 16) yScroll = level.h * 16 - screen.h - 16;


        level.renderBackground(screen, xScroll, yScroll);
        level.renderSprites(screen, xScroll, yScroll);


        renderGui();

        if (!hasFocus()) renderFocusNagger();

        for (int y = 0; y < screen.h; y++) {
            for (int x = 0; x < screen.w; x++) {
                int cc = screen.pixels[x + y * screen.w];
                if (cc < 255) pixels[x + y * Constants.WIDTH] = colors[cc];
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());

        int ww = Constants.WIDTH * 3;
        int hh = Constants.HEIGHT * 3;
        int xo = (getWidth() - ww) / 2;
        int yo = (getHeight() - hh) / 2;
        g.drawImage(image, xo, yo, ww, hh, null);
        g.dispose();
        bs.show();
    }

    private void renderGui() {
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 20; x++) {
                screen.render(x * 8, screen.h - 16 + y * 8, 12 * 32, Color.get(000, 000, 000, 000), 0);
            }
        }

        for (int i = 0; i < 10; i++) {
            if (i < player.health)
                screen.render(i * 8, screen.h - 16, 12 * 32, Color.get(000, 200, 500, 533), 0);
            else
                screen.render(i * 8, screen.h - 16, 12 * 32, Color.get(000, 100, 000, 000), 0);

            if (player.staminaRechargeDelay > 0) {
                if (player.staminaRechargeDelay / 4 % 2 == 0)
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 555, 000, 000), 0);
                else
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
            } else {
                if (i < player.stamina)
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 220, 550, 553), 0);
                else
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
            }
        }
        if (player.activeItem != null) {
            player.activeItem.renderInventory(screen, 10 * 8, screen.h - 16);
        }

        if (menu != null) {
            menu.render(screen);
        }
    }

    private void renderFocusNagger() {
        String msg = "Click to focus!";
        int xx = (Constants.WIDTH - msg.length() * 8) / 2;
        int yy = (Constants.HEIGHT - 8) / 2;
        int w = msg.length();
        int h = 1;

        screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
        screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
        screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
        screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);
        for (int x = 0; x < w; x++) {
            screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
        }
        for (int y = 0; y < h; y++) {
            screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
        }

        if ((tickCount / 20) % 2 == 0) {
            Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
        } else {
            Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
        }
    }

    public void won() {
        wonTimer = 60 * 3;
        hasWon = true;
    }
}