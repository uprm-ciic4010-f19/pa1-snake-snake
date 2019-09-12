package Game.GameStates;

import java.awt.Graphics;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

public class GameOverState extends State{

	private int count = 0;
    private UIManager uiManager;
    
	public GameOverState(Handler handler) {
		super(handler);
		
		uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(56, 50, 128, 64, Images.Restart, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
            
        }));

        uiManager.addObjects(new UIImageButton(330, 50, 128, 64, Images.Options, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
        }));

        uiManager.addObjects(new UIImageButton(620, 50, 128, 64, Images.Exit, () -> {
            handler.getMouseManager().setUimanager(null);
            System.exit(0);
        }));

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if( count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }


	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(Images.gameOver,0,0,800,800,null); // More for can fill the scren
        uiManager.Render(g);
	}

	
}
