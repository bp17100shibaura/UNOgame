package UNOset.player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import GameSet.GamePlayer;
import UNOset.utils.*;


public class RandomPlayer {

	public static void main(String[] args)
	{
		int num = Integer.parseInt(args[0]);
		
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("Ú‘±ƒGƒ‰[");
	    	return;
	    }
		
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		
		try
		{
			/*4‡‚Ì€”õ*/
			
			   /*‡‚Ì€”õ*/
			
			
			   /*‡*/
			
			   /*‡Œ‹‰Êˆ—*/
			
			/*4‡‚Ìˆ—*/
			
			read.close();
			write.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		player.gameEnd();
		System.out.println("pEND");

	}

}
