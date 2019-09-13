package Game.Entities.Static;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;
private static boolean red = true;  //variable red, set the apple to true;
    public int xCoord;
    public int yCoord;

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }
//if red is true, then the method isGood() will set to true
    public static boolean isGood(){
    	return red;
    }
    //set the the apple to good
    public static void setGood(boolean isGood) {
    	
    	red = isGood;
    }
    

}
