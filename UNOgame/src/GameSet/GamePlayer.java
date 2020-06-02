package GameSet;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class GamePlayer {

	public static void main(String[] args) {
		
		try {
			    Socket sock = new Socket("localhost",10000);
			    
			    OutputStream out = sock.getOutputStream();
			    
			    String Data = "test";
			    
			    out.write(Data.getBytes("UTF-8"));
			    
			    System.out.println(Data + "ÇëóêM");
			    
			    out.close();
			    
			    sock.close();
			    
		}catch(IOException e) {
			e.printStackTrace();
		}

	}

}
