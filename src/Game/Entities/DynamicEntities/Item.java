package Game.Entities.DynamicEntities;

import Main.Handler;

import java.awt.image.BufferedImage;

public class Item extends BaseDynamicEntity{
	public static int misc= 3;
    public boolean used=false;
    public boolean pickedup = false;

    public Item(int x, int y, int width, int height, Handler handler, BufferedImage sprite) {
        super(x, y, width, height, handler, sprite);
    }
}
