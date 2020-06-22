package UNOset.player;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

public class Reader 
{
    private PrintWriter write;
    private BufferedReader read;
	
	Reader(PrintWriter write,BufferedReader read)
	{
		this.read = read;
		this.write = write;
	}
	
	String read()
	{
		String str = "";
		try
		{
			str = read.readLine();
			System.out.println(str);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return str;
	}
	
	String sread()
	{
		String str = "";
		try
		{
			str = read.readLine();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return str;
	}
	
	void write(String str)
	{
		write.println(str);
	}
}
