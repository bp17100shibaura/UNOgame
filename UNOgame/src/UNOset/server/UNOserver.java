package UNOset.server;

public class UNOserver
{

	public static void main(String[] args)
	{
		int playerNum = 2;
		
		//ポートの設定
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		
		//TCPserverとの接続
	    TCPserver server = new TCPserver(2);
	    if(!server.connect(a, b))
	    {
	    	System.out.println("connect miss");
	    	return;
	    }
	    if(!server.setServer())
	    {
	    	System.out.println("set miss");
	    	return;
	    }
	    
	    //試合数
	    int matchCount = 5;
	    
	    //マッチの管理
		while(true)
		{
			matchCount--;
			
			//1ラウンド
			GameRun round = new GameRun(playerNum,server);
			RoundData data = round.match();
			System.out.println(data.winner);
			
			
			if(matchCount == 0)
			{
				break;
			}
		}

		server.gameEnd();
		System.out.println("sEND");
	}

}
