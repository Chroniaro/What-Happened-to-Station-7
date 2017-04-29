package com.whtss.assets;

import java.awt.Dimension;
import javax.swing.JFrame;
import com.whtss.assets.render.GameRenderer;

public class Station7
{
	public static void main(String... args)
	{
		HexPoint.iterateRectangle(HexPoint.XY(-2, -2), 3, 3, (HexPoint cell) ->
		{
			System.out.println(cell);
		});
		
		HexPoint.iterateHexagon(HexPoint.origin, 2, (HexPoint cell, int a, int b) ->
		{
			System.out.println(cell.abCoords() + " " + a + " " + b);
		});
		
//		Game game = new Game();
//		GameRenderer render = new GameRenderer(game);
//		
//		JFrame window = new JFrame();
//		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.setTitle("What Happened to Station 7");
//		window.add(render);
//		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		window.setVisible(true);
	}
}
