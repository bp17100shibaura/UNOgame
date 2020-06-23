package UNOset.server;

public class UNOserver
{

	public static void main(String[] args)
	{
		int playerNum = 4;
		
		//ポートの設定
		//int a = Integer.parseInt(args[0]);
		//int b = Integer.parseInt(args[1]);
		
		int a = 555;
		int b = 666;
		int c = 777;
		int d = 888;
		
		//TCPserverとの接続
	    TCPserver server = new TCPserver(playerNum);
	    if(!server.connect(a, b, c ,d))
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
	    int roundNum = 3;
	    int roundCount = 0;
	    
	    int[][] scoreData = new int[playerNum][roundNum];
	    
	    for(int i = 0;i < playerNum;i++)
	    {
	    	server.sendMessage(i+1,"player");
	    	server.sendMessage(i+1, Integer.toString(i+1));
	    }
	    
	    //マッチの管理
		while(true)
		{
			
			//1ラウンド
			server.sendAllMessage("Round"+roundCount);
			GameRun round = new GameRun(playerNum,server);
			RoundData data = round.match();
			for(int i = 0;i < playerNum;i++)
			{
				server.sendMessage(i+1,"winner player"+ data.winner);
				server.sendMessage(i+1, "your score");
				server.sendMessage(i+1, "" + data.score[i]);
				scoreData[i][roundCount] = data.score[i];
			}
			
			roundCount++;
			if(roundCount == roundNum)
			{
				break;
			}
		}

		server.sendAllMessage("Match finish");
		server.sendAllMessage("final score");
		int topS = -1000;
		int topP = 0;
		for(int i= 0;i < playerNum;i++)
		{
			int temp = 0;
			for(int j = 0;j < roundNum;j++)
			{
				temp += scoreData[i][j];
			}
			server.sendAllMessage("player"+ (i+1) +"'s score");
			server.sendAllMessage(Integer.toString(temp));
			if(temp > topS)
			{
				topS = temp;
				topP = i + 1;
			}
		}
		server.sendAllMessage("winner player"+ topP);
		server.sendAllMessage("winner score "+ topS);
		
		server.sendAllMessage("GAME END");
		
		server.close();
		server.gameEnd();
		System.out.println("sEND");
	}

}
