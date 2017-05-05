package com.whtss.assets;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;

public class Station7
{
	public static void main(String... args)
	{	
		Game game = new Game();
		
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
		
		window.setVisible(true);
		
		long t = System.currentTimeMillis();
		
		while(true)
		{
			long s = System.currentTimeMillis();
			game.update(s - t);
			t = s;
			render.repaint();
		}
	}
}
