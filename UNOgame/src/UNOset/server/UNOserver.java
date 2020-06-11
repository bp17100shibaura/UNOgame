package UNOset.server;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import GameSet.GameServer;
import UNOset.utils.*;

public class UNOserver
{

	public static void main(String[] args)
	{
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		
	    GameServer server = new GameServer();	
	    
		if(0 == server.playerSet(a,b))
		{
			System.out.println("Ú‘±ƒGƒ‰[");
			return;
		}

		Deck deck = new Deck();
		deck.deckMake();
		System.out.println(deck.getDeckNum());
		for(int i = 0;i < deck.getDeckNum();i++)
		{
			System.out.print((deck.draw1Card()).getCardName() + "  ");
			if(i % 3 == 0)
			{
				System.out.println();
			}
		}
		
		deck.deckMake();
		deck.shuffle();
		
		System.out.println(deck.getDeckNum());
		for(int i = 0;i < deck.getDeckNum();i++)
		{
			System.out.print((deck.draw1Card()).getCardName() + "  ");
			if(i % 3 == 0)
			{
				System.out.println();
			}
		}
		
		deck.deckMake();
		deck.shuffle();
		
		String temp = "";

		try
		{
		    BufferedReader readA = server.getReader(1);
		    PrintWriter writeA = server.getWriter(1);
		    BufferedReader readB = server.getReader(2);
		    PrintWriter writeB = server.getWriter(2);
		    
		    writeA.println("yourturnA");
		    writeA.println("draw 7card");
		    for(int i = 0;i < 7;i++)
		    {
		    	writeA.println((deck.draw1Card()).getCardName());
		    }
		    temp = readA.readLine();
		    System.out.println(temp);
		    writeA.println("turnendA");
		    
		    writeB.println("yourturnB");
		    writeB.println("draw 7card");
		    for(int i = 0;i < 7;i++)
		    {
		    	writeB.println((deck.draw1Card()).getCardName());
		    }
		    temp = readB.readLine();
		    System.out.println(temp);
		    writeB.println("turnendB");
		    
		    readA.close();
		    writeA.close();
		    readB.close();
		    writeB.close();
		   
		    
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		server.endGame();
		System.out.println("sEND");
	}

}
