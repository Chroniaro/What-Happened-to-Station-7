package com.whtss.assets;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import com.whtss.na_sweden;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import java.applet.*;
import java.net.*;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;

public class Station7
{
	public static void main(String... args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		Game game = new Game();
		try {
			File audioFile = new File("na_sweden.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.start();
			} catch (MalformedURLException murle) {
			System.out.println(murle);
			}
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("What Happened to Station 7");
		window.setMinimumSize(new Dimension(400, 225));
		window.setLayout(new GridBagLayout());
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		GameRenderer render = new GameRenderer(game);
		render.addListeners(window);
		GridBagConstraints layoutGame = new GridBagConstraints();
		layoutGame.weightx = .9;
		layoutGame.weighty = .9;
		layoutGame.fill = GridBagConstraints.BOTH;
		layoutGame.anchor = GridBagConstraints.NORTH;
		window.add(render, layoutGame);
		
		GameInfo info = new GameInfo(game);
		info.addListeners(window);
		GridBagConstraints layoutInfo = new GridBagConstraints();
		layoutInfo.weightx = .1;
		layoutInfo.weighty = .1;
		layoutInfo.fill = GridBagConstraints.BOTH;
		layoutInfo.anchor = GridBagConstraints.NORTH;
		layoutInfo.gridy = 1;
		window.add(info, layoutInfo);
		
		game.setNextTurnRunnable(render::repaint);
		
		window.setVisible(true);
		
//		long t = System.currentTimeMillis();
//		
//		while(true)
//		{
//			long s = System.currentTimeMillis();
//			game.update(s - t);
//			t = s;
//			render.repaint();
//		}
	}
}
