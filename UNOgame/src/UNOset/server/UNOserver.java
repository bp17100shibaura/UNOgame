package UNOset.server;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;

import GameSet.GameServer;

public class UNOserver
{

	public static void main(String[] args)
	{
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		
	    GameServer server = new GameServer();	
	    
		if(0 == server.playerSet(a,b))
		{
			System.out.println("ê⁄ë±ÉGÉâÅ[");
			return;
		}

		String temp = "";

		try
		{
		    BufferedReader read = server.getReader(1);
		    PrintWriter write = server.getWriter(1);
		    write.println("yourturnA");
		    temp = read.readLine();
		    System.out.println(temp);
		    write.println("turnendA");
		    read.close();
		    write.close();
		    
		    read = server.getReader(2);
		    write = server.getWriter(2);
		    write.println("yourturnA");
		    temp = read.readLine();
		    System.out.println(temp);
		    write.println("turnendA");
		    read.close();
		    write.close();
		    
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		server.endGame();
		System.out.println("sEND");
	}

}
