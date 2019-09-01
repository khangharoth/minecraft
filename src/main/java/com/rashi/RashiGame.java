package com.rashi;

import javax.swing.*;
import java.awt.*;

public class RashiGame extends Canvas implements Runnable {
    private boolean running = true;
    private static final int HEIGHT = 300;
    private static final int WIDTH = 350;
    private static final int SCALE = 3;
    private int[] colors = new int[256];

    @Override
    public void run() {
        while (running) {
            this.setVisible(true);
        }
    }

    public static void main(String[] args) {
        RashiGame game = new RashiGame();
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame("Rashi Minicraft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    private void start() {
        running = true;
        new Thread(this).start();
    }
}

