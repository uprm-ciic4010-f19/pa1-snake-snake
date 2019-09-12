package Game.Entities.Dynamic;


import Main.Handler;
import Resources.Images;
import UI.UIImageButton;


import java.awt.*;
import java.awt.event.KeyEvent;
//import java.net.http.HttpResponse.BodyHandler;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import Game.Entities.Static.Apple;
import Game.GameStates.GameState;
import Game.GameStates.MenuState;
import Game.GameStates.PauseState;
import Game.GameStates.State;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {
	private int score = 0 ; //score variable
	public int lenght;
	public boolean justAte;
	private Handler handler;

	public int xCoord;
	public int yCoord;

	int speed = 5; //speed variable
	public int moveCounter;

	public String direction;//is your first name one?

	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		lenght= 1;

	}

	public void tick(){   
		moveCounter++;
		if(moveCounter>=speed) { //speed
			checkCollisionAndMove();
			moveCounter= 0 ; // from 0 to 5

			//Unable backtracking
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_W ) ){

			if(direction !="Down" ) {
				direction = "Up" ;
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)){


			if(direction != "Up") {
				direction = "Down";
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)){


			if(direction != "Right") {
				direction = "Left";
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)){

			if(direction != "Left") {
				direction = "Right";
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){

			State.setState(handler.getGame().pauseState);


		}

		//implementacion de tecla N, para aumentar cola.
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) 
			handler.getWorld().body.addFirst(new Tail(xCoord, yCoord,handler));

		//implementacion de tecla + para aumentar velocidad
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS))
			speed --;  

		//implementacion de tecla - para reducir velocidad
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS))	  
			speed++;
		
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_M)){
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().body.removeLast();	
		}


	}

	//Going through walls implemantation
	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				xCoord=handler.getWorld().GridWidthHeightPixelCount-1;
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				//  kill();
				xCoord= 0;
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				yCoord=handler.getWorld().GridWidthHeightPixelCount-1;
				// kill();
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				yCoord = 0;
				// kill();
			}else{
				yCoord++;
			}
			break;
		}
		//When the Snakes eats
		handler.getWorld().playerLocation[xCoord][yCoord]=true;

		Tail tail= null;
		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			
			if(Apple.isGood() == false){
				shittyEat();
				score -= Math.sqrt((2*score +1));
				
			}else
				Eat();
				score += Math.sqrt((2*score +1));
		}
		// si choco 

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));

			//collition with itself
			for(int i =0;i< handler.getWorld().body.size();i++) {
				if(xCoord == handler.getWorld().body.get(i).x && yCoord == handler.getWorld().body.get(i).y) {

					JOptionPane.showMessageDialog(null, "Game Over");
					System.exit(0);

				}


			}

		}



	}

	public void render(Graphics g,Boolean[][] playeLocation){
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount ; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				if(playeLocation[i][j]){
					g.setColor(Color.green);
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
				//Para poder cambiar la manzana mala a otro color
				else if(Apple.isGood() == false){
					if(handler.getWorld().appleLocation[i][j]){
						g.setColor(Color.YELLOW);
						g.fillRect((i*handler.getWorld().GridPixelsize),
								(j*handler.getWorld().GridPixelsize),
								handler.getWorld().GridPixelsize,
								handler.getWorld().GridPixelsize);
					}
				}
				else if(handler.getWorld().appleLocation[i][j]){
					g.setColor(Color.red);
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);

				}

			}
		}

		//Score implementation
		g.setColor(Color.white);
		g.setFont(new Font("Times new roman", Font.PLAIN, 20 ));
		g.drawString("Score: " + score, 5, 20 );
		//lenght implementation
		g.setColor(Color.white);
		g.setFont(new Font(" Times new roman ", Font.PLAIN, 20 ));
		g.drawString(" Lenght: " + lenght, 5, 40);

	}

	public void Eat(){
		//For Lenght implemantation
		lenght++;
		//Can delete the speed does not affect anything
		speed--;

		Tail tail= null;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;
		switch (direction){
		case "Left":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail = new Tail(this.xCoord+1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail = new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail =new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
					}else{
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

					}
				}

			}
			break;
		case "Right":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=0){
					tail=new Tail(this.xCoord-1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail=new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail=new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}
				}

			}
			break;
		case "Up":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(this.xCoord,this.yCoord+1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					}
				}
			}else{
				if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		case "Down":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=0){
					tail=(new Tail(this.xCoord,this.yCoord-1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					} System.out.println("Tu biscochito mereeeee");
				}
			}else{
				if(handler.getWorld().body.getLast().y!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		}		
		handler.getWorld().body.addLast(tail);
		handler.getWorld().playerLocation[tail.x][tail.y] = true;		
	}
	
	public void shittyEat(){
		
		handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().body.removeLast();
		handler.getWorld().appleOnBoard = false;
	}

	public void kill(){
		lenght = 1;
		for (int i = 1; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 1; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j]=false;

			}
		}
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}