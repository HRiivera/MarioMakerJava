package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;

import Game.GameStates.State;

public class FinishLineBarP2 extends Item {
	int initX;
	int initY;
	String movement="Up";

	public FinishLineBarP2(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.FinishLineBar);
		initX = x;
		initY= y;
		direction = "Up";
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
    	
		if(State.getP2()) {
			if(handler.getFunkyKong().getBounds().intersects(this.getBounds())) {
				handler.getFunkyKong().touchFinish = true;
				handler.getFunkyKong().timeCompleted = (double) (handler.getFunkyKong().tickCounter-120)/60;
				this.x=1000000;
				handler.getGame().getMusicHandler().playCheckPoint();
			}
		}


	}


}
