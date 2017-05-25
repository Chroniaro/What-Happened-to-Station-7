package com.whtss.assets.render.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import com.whtss.assets.core.Damageable;
import com.whtss.assets.core.Entity;
import com.whtss.assets.hex.HexPoint;

public class ImageSprite extends ColorGradientSprite
{
	private final static HashMap<String, BufferedImage> loadedImages = new HashMap<>();
	
	BufferedImage sprite;
	
	public <DamageableEntity extends Entity & Damageable> ImageSprite(DamageableEntity e, String spriteName)
	{
		super(e, Color.GREEN, Color.RED);
		
		try
		{
			if(loadedImages.containsKey(spriteName))
				sprite = loadedImages.get(spriteName);
			else
				loadedImages.put(spriteName, sprite = ImageIO.read(getClass().getResourceAsStream(spriteName + ".png")));
		}
		catch (IOException excep)
		{
			excep.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g, int s)
	{
		super.draw(g, s);
		
		HexPoint location = e.getLocation();
		g.setClip(location.getBorder(s));
		int x = location.getVisualX(s), y = location.getVisualY(s);
		g.drawImage(sprite, x - s/2, y - s/2, s, s, null);
	}
}