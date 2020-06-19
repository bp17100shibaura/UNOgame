package UNOset.server;

public class UNOserver
{

	public static void main(String[] args)
	{
		int playerNum = 2;
		
		//�|�[�g�̐ݒ�
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		
		//TCPserver�Ƃ̐ڑ�
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
	    
	    //������
	    int roundNum = 1;
	    int roundCount = 0;
	    
	    int[][] scoreData = new int[playerNum][roundNum];
	    
	    //�}�b�`�̊Ǘ�
		while(true)
		{
			roundCount++;
			
			//1���E���h
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
			server.sendAllMessage("player"+ i +"'s score");
			server.sendAllMessage(Integer.toString(temp));
			if(temp > topS)
			{
				topS = temp;
				topP = i + 1;
			}
		}
		server.sendAllMessage("winner player"+ topP);
		server.sendAllMessage("winner score "+ topS);
		
		server.close();
		server.gameEnd();
		System.out.println("sEND");
	}

}
