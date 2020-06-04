package GameSet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class GamePlayer 
{

	private Socket sock;
	
	public int playerSet(int port)
	{
		
		try 
		{
	        sock = new Socket("localhost",port);
			    
		}catch(IOException e) 
		{
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public void gameEnd()
	{
		try
		{
		    sock.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public BufferedReader getReader()
	{
		BufferedReader reader = null;
		try
		{
		    reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return reader;
	}
	
	public PrintWriter getWriter()
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(sock.getOutputStream(),true);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return writer;
	}
}
