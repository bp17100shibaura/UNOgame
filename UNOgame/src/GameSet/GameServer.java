package GameSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class GameServer
{
	
	private ServerSocket[] svSock = new ServerSocket[2]; //テスト用に2実際は4
    private Socket[] sock = new Socket[2]; //上と同じ
	

	public int playerSet(int a, int b)
	{
		try
		{   
			svSock[0] = new ServerSocket(a);
		    svSock[1] = new ServerSocket(b);
		    //svSock[2] = new ServerSocket(10003);
		    //svSock[3] = new ServerSocket(10004);
		    sock[0] = svSock[0].accept();
		    System.out.println("aOK");
		    sock[1] = svSock[1].accept();
		    System.out.println("bOK");
		    //sock[2] = svSock[2].accept();
		    //sock[3] = svSock[3].accept();
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
		    for(int i = 0;i < 2;i++)//テスト用に2
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
