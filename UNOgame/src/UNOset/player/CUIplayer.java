package UNOset.player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import GameSet.GamePlayer;

public class CUIplayer
{
		
	public static void main(String[] args)
	{
		int num = Integer.parseInt(args[0]);
		
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("ê⁄ë±ÉGÉâÅ[");
	    	return;
	    }
	    
	    String temp = "";
		
		try
		{
			BufferedReader read = player.getReader();
			PrintWriter write = player.getWriter();
			temp = read.readLine();
			System.out.println(temp);
			write.println("turn finish");
			temp= read.readLine();
			System.out.println(temp);
			
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
