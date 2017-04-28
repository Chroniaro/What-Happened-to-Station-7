package render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

public class GameRenderer extends JComponent
{
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
	}
}
