package GameSet;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Arrays;

public class GameServer {

	public static void main(String[] args) {
		try {
			    ServerSocket svSock = new ServerSocket(10000);
			    
			    Socket sock = svSock.accept();
			    
			    byte[] data = new byte[1024];
			    
			    InputStream in = sock.getInputStream();
			    
			    int readSize = in.read(data);
			    
			    data = Arrays.copyOf(data, readSize);
			    
			    System.out.println("<" + new String(data,"UTF-8") + ">ÇéÛêM");
			    
			    in.close();
			    
			    svSock.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}

	}

}
