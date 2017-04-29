package com.whtss.assets;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import com.whtss.assets.render.GameRenderer;

public class Station7
{
	public static void main(String... args)
	{	
		Game game = new Game();
		GameRenderer render = new GameRenderer(game);
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("What Happened to Station 7");
		window.setMinimumSize(new Dimension(400, 225));
		window.add(render);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
