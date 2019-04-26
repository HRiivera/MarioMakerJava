package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;

public class FinishLineBar extends Item {
	int initX;
	int initY;
	String movement = "Up";

    public FinishLineBar(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.FinishLineBar);
        initX = x;
        initY= y;
    }

    @Override
    public void tick(){
    	if(this.y==initY) {
    		movement = "Up";
    	}
    	if(this.y==initY-228) {
    		movement = "Down";
    	}
    	if(movement.equals("Up")) {
    		this.y-=3;
    	}
    	if(movement.equals("Down")) {
    		this.y+=3;
    	}
    	
    	if(handler.getMario().getBounds().intersects(this.getBounds())) {
    		handler.getMario().touchFinish = true;
    		handler.getMario().timeCompleted = (double) (handler.getMario().tickCounter-120)/60;
    		this.x=1000000;
    		handler.getGame().getMusicHandler().playCheckPoint();
    	}
    	
    	
    	
    }


}
