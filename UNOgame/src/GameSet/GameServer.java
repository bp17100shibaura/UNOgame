package GameSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class GameServer
{
	
	private ServerSocket[] svSock = new ServerSocket[4];
    private Socket[] sock = new Socket[4];
	

	public int playerSet(int a, int b,int c,int d)
	{
		try
		{   
			svSock[0] = new ServerSocket(a);
		    svSock[1] = new ServerSocket(b);
		    svSock[2] = new ServerSocket(c);
		    svSock[3] = new ServerSocket(d);
		    sock[0] = svSock[0].accept();
		    sock[1] = svSock[1].accept();
		    sock[2] = svSock[2].accept();
		    sock[3] = svSock[3].accept();
		}catch(IOException e)
		{
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
	
	public void endGame()
	{
		try
		{
		    for(int i = 0;i < 4;i++)
		    {
			    svSock[i].close();
		    }
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public BufferedReader getReader(int num)
	{
		BufferedReader reader = null;
		try
		{
		    reader = new BufferedReader(new InputStreamReader(sock[num-1].getInputStream()));
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return reader;
	}
	
	public PrintWriter getWriter(int num)
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(sock[num-1].getOutputStream(),true);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return writer;
	}
	

}
