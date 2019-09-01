package com.rashi;

import java.awt.*;

public class RashiGame extends Canvas implements Runnable{
    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            this.setVisible(true);
        }
    }

    public static void main(String[] args) {
        RashiGame game = new RashiGame();
        game.run();
    }
}
