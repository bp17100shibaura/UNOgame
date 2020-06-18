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
	    int matchCount = 5;
	    
	    //�}�b�`�̊Ǘ�
		while(true)
		{
			matchCount--;
			
			//1���E���h
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
