package com.whtss.assets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import com.whtss.assets.render.GameInfo;
import com.whtss.assets.render.GameRenderer;
import com.whtss.assets.render.SoundStuff;

public class Station7
{
	public final static boolean DEV = true;
	
	public static void main(String... args) throws Throwable
	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("What Happened to Station 7");
		window.setMinimumSize(new Dimension(400, 225));
		window.setLayout(new GridBagLayout());
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JFrame opening_text = new JFrame();
		opening_text.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		opening_text.setTitle("This Is What Happened to Station 7");
		JTextArea textField = new JTextArea(
				"The date is 123.5432.1241511    Station 7 is in full operation when suddenly one of the arachnids spot a rather large blue and yellow battle-cruiser barreling towards the aft section of the outer wheel. It suddenly plunges it self deep within the station. The flag suddenly comes within view… Its the swedes on their eternal quest to increase their GDP. They start to scatter but they are not quick enough to survive the army of Carl XVI Gustaf. The Dramatic entrance of the battle-cruiser had created a hull breach and the amount of air in the station was starting to drop; those blast doors can only hold so long….																																																		You Play as the aliens represented by the Purple hexes your objective is to get to the next floor of Staition 7 via the cyan ecape doors the sweds represented by the blue tiles are out to get you it would be in your best intrests to avoid them as they will kill you very quickly, if you notics your color shifting it indicats a change in helth and you will be best adviced to seek out the red health boxes but be carful as they will only heal you so much");
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		opening_text.setMinimumSize(new Dimension(400, 225));
		opening_text.add(textField, BorderLayout.PAGE_START);
		opening_text.setExtendedState(JFrame.MAXIMIZED_BOTH);
		opening_text.setAlwaysOnTop(true);

		JFrame closing_text = new JFrame();
		closing_text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		closing_text.setTitle("You Have Fell to the Swedes");
		JTextArea text = new JTextArea("You have failed to save your self…. The swedes trudge on obliterating the remaining inhabitants of the station. ");
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		closing_text.setMinimumSize(new Dimension(400, 225));
		closing_text.add(text, BorderLayout.PAGE_START);
		closing_text.setExtendedState(JFrame.MAXIMIZED_BOTH);

		SoundStuff soundStuff = new SoundStuff();
		soundStuff.dbol();

		Game game = new Game();

		GameRenderer render = new GameRenderer(game);
		render.addListeners(window);
		GridBagConstraints layoutGame = new GridBagConstraints();
		layoutGame.weightx = .9;
		layoutGame.weighty = .9;
		layoutGame.fill = GridBagConstraints.BOTH;
		layoutGame.anchor = GridBagConstraints.NORTH;
		window.add(render, layoutGame);

		GameInfo info = new GameInfo(game);
		info.addListeners();
		GridBagConstraints layoutInfo = new GridBagConstraints();
		layoutInfo.weightx = .1;
		layoutInfo.weighty = .1;
		layoutInfo.fill = GridBagConstraints.BOTH;
		layoutInfo.anchor = GridBagConstraints.NORTH;
		layoutInfo.gridy = 1;
		window.add(info, layoutInfo);

		game.init(render.new UIInterface(), info.new UIInterface());

		closing_text.setVisible(true);
		window.setVisible(true);
		opening_text.setVisible(true);
		Scanner reader = new Scanner(System.in);
		int n = reader.nextInt();
		if (n == 42)
		{
			System.out.print("adadfafd");
			SoundStuff cam = null;
			cam = new SoundStuff();
			cam.CCCP();
			reader.close();
		}
	}
}
