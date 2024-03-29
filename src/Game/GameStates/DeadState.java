package Game.GameStates;

import Display.UI.UIStringButton;
import Main.Handler;
import Resources.Images;
import Display.UI.UIManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class DeadState extends State {

    private UIManager uiManager;

    public DeadState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.addObjects(new UIStringButton(56, 223, 128, 64, "Title", () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
        }
        ,handler,Color.WHITE));


    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
            State.setState(handler.getGame().gameState);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.Pause,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
    }
    @Override
    public void renderP2(Graphics g) {
        g.drawImage(Images.Pause,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
    }
}
