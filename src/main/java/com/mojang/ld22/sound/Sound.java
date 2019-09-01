package com.mojang.ld22.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

	public static final Sound playerHurt = new Sound("res/playerhurt.wav");
	public static final Sound playerDeath = new Sound("res/death.wav");
	public static final Sound monsterHurt = new Sound("res/monsterhurt.wav");
	public static final Sound test = new Sound("res/test.wav");
	public static final Sound pickup = new Sound("res/pickup.wav");
	public static final Sound bossdeath = new Sound("res/bossdeath.wav");
	public static final Sound craft = new Sound("res/craft.wav");

	private AudioClip clip;

	private Sound(String name) {
		try {
			URL resource = Sound.class.getClassLoader().getResource(name);
			clip = Applet.newAudioClip(resource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}