package Game.Entities.Dynamic;


import Main.Handler;
import Resources.Images;
import UI.UIImageButton;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
//import java.net.http.HttpResponse.BodyHandler;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


import Game.Entities.Static.Apple;
import Game.GameStates.GameOverState;
import Game.GameStates.GameState;
import Game.GameStates.MenuState;
import Game.GameStates.PauseState;
import Game.GameStates.GameOverState;
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
	public int lenght;		//length variable
	public boolean justAte;
	private Handler handler;

	public int xCoord;
	public int yCoord;

	int speed = 5; //speed variable
	int step = 0; //For counter
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
		stepCounter(); //Making the step counter be a part of Tick, so it can count the steps of the snake
		if(moveCounter>=speed) { //speed
			step++;
			checkCollisionAndMove();
			moveCounter= 0 ; // from 0 to 5

			//Unable backtracking and changing the keys
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
			//setting the pause state
			State.setState(handler.getGame().pauseState);


		}

		//Implementation of key - increasing the tail by one
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) 
			handler.getWorld().body.addFirst(new Tail(xCoord, yCoord,handler));

		//Implementation of key - increasing the speed of the snake
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS))
			speed --;  

		//Implementation of key - reducing the speed of the snake
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS))	  
			speed++;
		//Implementation of key - reducing tail
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_M)){
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().appleLocation[xCoord][yCoord]=false;
			handler.getWorld().body.removeLast();	
		}
	}
	//Making the counter
	public void stepCounter() {


		if(step>100) { // it will do 100 step, then it will change to rotten
			Apple.setGood(false);	


		}
		else {
			Apple.setGood(true); //Good Apple

		}
	}
	//Going through walls implementation
	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				xCoord=handler.getWorld().GridWidthHeightPixelCount-1; //This is to make the snake appear in the opposite side "Right"
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){

				xCoord= 0;       //This is to make the snake appear in the opposite side "left"
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				yCoord=handler.getWorld().GridWidthHeightPixelCount-1; //This is to make the snake appear in the opposite side "down"

			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				yCoord = 0;  //This is to make the snake appear in the opposite side "down"

			}else{
				yCoord++;
			}
			break;
		}
		//When the Snakes eats
		handler.getWorld().playerLocation[xCoord][yCoord]=true;
		Tail tail= null;
		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			// This tells you that if the apple is false, it will get to another color
			if(Apple.isGood() == false ){ 
				shittyEat(); //This method allows to remove the tail
				step = 0;  //Resetting the counter
				score -= Math.sqrt((2*score +1)); // subtracting when a rotten apple gets eating
				lenght--; //Decrease the length whenever it eats a rotten apple

			}else
				Eat(); // Whenever the snake eat a good apple gain a tail
			step = 0; //Reset
			score += Math.sqrt((2*score +1)); //Summation whenever the snake eats a good apple

		}



		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));

			BufferedImage img;
			//Collision with itself
			for(int i =0;i< handler.getWorld().body.size();i++) {
				if(xCoord == handler.getWorld().body.get(i).x && yCoord == handler.getWorld().body.get(i).y) {

					State.setState(handler.getGame().gameOverState); //Calling the class to get the image



				}


			}

		}



	}

	// Render allows to display an object on the screeen
	public void render(Graphics g,Boolean[][] playeLocation){
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount ; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				//Implemented to change the color of the snake different from the apple
				if(playeLocation[i][j]){ // Playerlocation = "the snake"
					g.setColor(Color.green);
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
				//Making the color to yellow hen apple is bad
				else if(Apple.isGood() == false){
					if(handler.getWorld().appleLocation[i][j]){ //Apple
						g.setColor(Color.YELLOW);
						g.fillRect((i*handler.getWorld().GridPixelsize),
								(j*handler.getWorld().GridPixelsize),
								handler.getWorld().GridPixelsize,
								handler.getWorld().GridPixelsize);
					}
				}
				//The color for the good apple
				else if(handler.getWorld().appleLocation[i][j]){ //Apple
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
		g.setFont(new Font("Times new roman", Font.ITALIC, 20 ));
		g.drawString("Score: " + score, 5, 20 );
		//Length on the screen implementation
		g.setColor(Color.white);
		g.setFont(new Font(" Times new roman ", Font.ITALIC, 20 ));
		g.drawString(" Length: " + lenght, 320, 20);
		//steps on the screen implementation
		g.setColor(Color.white);
		g.setFont(new Font(" Times new roman ", Font.ITALIC, 20 ));
		g.drawString(" Steps: " + step, 680, 20);

	}

	public void Eat(){
		//For Length implemantation
		lenght++;
		//After the snake eats, "speed--" will make the snake go faster
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
	//Method to remove the tail after the snake has eaten a bad apple
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