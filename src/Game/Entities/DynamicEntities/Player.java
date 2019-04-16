package Game.Entities.DynamicEntities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Display.UI.UIPointer;
import Game.Entities.StaticEntities.BaseStaticEntity;
import Game.Entities.StaticEntities.BorderBlock;
import Game.Entities.StaticEntities.BoundBlock;
import Game.Entities.StaticEntities.CloudBlock;
import Game.GameStates.State;
import Main.Handler;
import Resources.Animation;

public class Player extends BaseDynamicEntity {

    protected double velX,velY;

    public String facing = "Left";
    public boolean moving = false;
    public Animation playerLeftAnimation,playerRightAnimation,playerIdleRightAnimation, playerIdleLeftAnimation,
    playerJumpRightAnimation, playerJumpLeftAnimation, playerRunRightAnimation, playerRunLeftAnimation;
    public boolean falling = true, jumping = false,isBig=true,running = false,changeDirrection=false;
    public double gravityAcc = 0.38;
    int changeDirectionCounter=0;
    
    public static int misc = 3;

    public Player(int x, int y, int width, int height, Handler handler, BufferedImage sprite,Animation PLA,Animation PRA,Animation PIRA,
            Animation PILA, Animation PJRA, Animation PJLA, Animation PRRA, Animation PRLA) {
        super(x, y, width, height, handler, sprite);

        playerLeftAnimation=PLA;
        playerRightAnimation=PRA;
        playerIdleRightAnimation=PIRA;
        playerIdleLeftAnimation=PILA;
        playerJumpRightAnimation=PJRA;
        playerJumpLeftAnimation=PJLA;
        playerRunRightAnimation=PRRA;
        playerRunLeftAnimation=PRLA;
    }

    @Override
    public void tick(){

        if (changeDirrection) {
            changeDirectionCounter++;
        }
        if(changeDirectionCounter>=10){
            changeDirrection=false;
            changeDirectionCounter=0;
        }

        checkBottomCollisions();
        checkMarioHorizontalCollision();
        checkTopCollisions();
        checkItemCollision();
        if (facing.equals("Left") && moving && !jumping && !falling) {
            playerLeftAnimation.tick();
            playerRunLeftAnimation.tick();
            playerJumpRightAnimation.end();
            playerJumpLeftAnimation.end();
        } else if (facing.equals("Right") && moving && !jumping  && !falling) {
            playerRightAnimation.tick();
            playerRunRightAnimation.tick();
            playerJumpRightAnimation.end();
            playerJumpLeftAnimation.end();
        }
        else if(facing.equals("Left") && !moving && !jumping  && !falling) {
            playerIdleLeftAnimation.tick();
            playerJumpRightAnimation.end();
            playerJumpLeftAnimation.end();
        }
        else if(facing.equals("Right") && !moving && !jumping  && !falling){
            playerIdleRightAnimation.tick();
            playerJumpRightAnimation.end();
            playerJumpLeftAnimation.end();
        }
        else if(jumping || falling) {
            playerJumpLeftAnimation.tick();
            playerJumpRightAnimation.tick();
        }
    }

    private void checkItemCollision() {

        for (BaseDynamicEntity entity : handler.getMap().getEnemiesOnMap()) {
            if (entity != null && getBounds().intersects(entity.getBounds()) && entity instanceof Item && !isBig) {
                isBig = true;
                this.y -= 8;
                this.height += 8;
                setDimension(new Dimension(width, this.height));
                ((Item) entity).used = true;
                entity.y = -100000;
            }
        }
    }


    public void checkBottomCollisions() {
        Player mario = this;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamicEntity> enemies =  handler.getMap().getEnemiesOnMap();
        
        Rectangle marioBottomBounds =getBottomBounds();

        if (!mario.jumping) {
            falling = true;
        }

        for (BaseStaticEntity brick : bricks) {
            Rectangle brickTopBounds = brick.getTopBounds();
            if (marioBottomBounds.intersects(brickTopBounds)) {
            	if(brick instanceof BorderBlock) {
            		State.setState(handler.getGame().deadState);
            	}
                mario.setY(brick.getY() - mario.getDimension().height + 1);
                falling = false;
                velY=0;
            }
        }

        for (BaseDynamicEntity enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (marioBottomBounds.intersects(enemyTopBounds) && !(enemy instanceof Item)) {
                if(!enemy.ded) {
                	State.setState(handler.getGame().deadState);
                    handler.getGame().getMusicHandler().playStomp();
                }
                enemy.kill();
                falling=false;
                velY=0;

            }
        }
    }

    public void checkTopCollisions() {
        Player mario = this;
        boolean marioDies = false;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseStaticEntity> borderBlocks =  handler.getMap().getBordersOnMap();

        Rectangle marioTopBounds = mario.getTopBounds();
        for (BaseStaticEntity brick : bricks) {
            Rectangle brickBottomBounds = brick.getBottomBounds();
            if (marioTopBounds.intersects(brickBottomBounds)) {
                velY=0;
                mario.setY(brick.getY() + brick.height);
            }
        }
        for (BaseStaticEntity borderBlock : borderBlocks) {
            Rectangle borderBounds = borderBlock.getBottomBounds();
            if (marioTopBounds.intersects(borderBounds)) {
                marioDies = true;
                break;
            }
        }
        if(marioDies) {
        	State.setState(handler.getGame().deadState);
        }
    }

    public void checkMarioHorizontalCollision(){
        Player mario = this;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamicEntity> enemies = handler.getMap().getEnemiesOnMap();
        ArrayList<BaseStaticEntity> borderBlocks =  handler.getMap().getBordersOnMap();


        boolean marioDies = false;
        boolean toRight = moving && facing.equals("Right");

        Rectangle marioBounds = toRight ? mario.getRightBounds() : mario.getLeftBounds();

        for (BaseStaticEntity brick : bricks) {


            Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
            if (marioBounds.intersects(brickBounds)) {
                velX=0;
                if(toRight)
                    mario.setX(brick.getX() - mario.getDimension().width);
                else
                    mario.setX(brick.getX() + brick.getDimension().width);
        	}
        }

        for(BaseDynamicEntity enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (marioBounds.intersects(enemyBounds)) {
                marioDies = true;
                break;
            }
        }
		for (BaseStaticEntity borderBlock : borderBlocks) {
			Rectangle borderBounds = !toRight ? borderBlock.getRightBounds() : borderBlock.getLeftBounds();
            if (marioBounds.intersects(borderBounds)) {
                marioDies = true;
            }
        }

        if(marioDies) {
//        	State.setState(handler.getGame().menuState);
        	UIPointer.kill = true;
            handler.getMap().reset();
            marioDies = false;
        }
    }

    public void jump() {
        if(!jumping && !falling){
            jumping=true;
            velY=10;
            handler.getGame().getMusicHandler().playJump();
        }
    }

    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }


}
