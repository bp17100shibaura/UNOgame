package UNOset.server;
import GameSet.GameServer;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class TCPserver
{
	private GameServer server = null;
	private BufferedReader[] read;
	private PrintWriter[] write;
	private int playerNum;
	
	TCPserver(int playerNum)
	{
		this.playerNum = playerNum;
		this.server = new GameServer();
	}

	//playerと接続
	boolean connect(int a, int b, int c, int d)
	{
		if(0 == server.playerSet(a, b, c, d))
		{
			return false;
		}
		return true;
	}
	
	//サーバーと接続
	boolean setServer()
	{
		if(server == null)
		{
			return false;
		}
		
		read = new BufferedReader[playerNum];
		write = new PrintWriter[playerNum];
		
		for(int i = 0;i < playerNum;i++)
		{
			read[i] = server.getReader(i+1);
			write[i] = server.getWriter(i+1);
		}
		
		return true;
	}
	
	//終了
	void gameEnd()
	{
		server.endGame();
	}
	
	//送信
	void sendMessage(int player,String str)
	{
		write[player -1].println(str);
		System.out.println(str);
	}
	
	//全員に送信
	void sendAllMessage(String str)
	{
		for(int i = 0;i < playerNum;i++)
		{
			write[i].println(str);
		}
		System.out.println(str);
	}
	
	String catchMessage(int player)
	{
		String str = "";
		try 
		{
			str = read[player-1].readLine();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println(str);
		return str;
	}
	
	void close()
	{
		for(int i = 0;i < playerNum;i++)
		{
			try 
			{
				read[i].close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			write[i].close();
		}
	}
	
}
