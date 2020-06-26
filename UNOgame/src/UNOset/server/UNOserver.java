package UNOset.server;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
public class UNOserver
{

	public static void main(String[] args)
	{
		int playerNum = 4;
		
		//�|�[�g�̐ݒ�
		//int a = Integer.parseInt(args[0]);
		//int b = Integer.parseInt(args[1]);
		
		int a = 555;
		int b = 666;
		int c = 777;
		int d = 888;
		
		//TCPserver�Ƃ̐ڑ�
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
	    
	    //������
	    int roundNum = 3;
	    int roundCount = 0;
	    
	    int[][] scoreData = new int[playerNum][roundNum];
	    
	    for(int i = 0;i < playerNum;i++)
	    {
	    	server.sendMessage(i+1,"player");
	    	server.sendMessage(i+1, Integer.toString(i+1));
	    }
	    
	    //�}�b�`�̊Ǘ�
		while(true)
		{
			
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
		try 
		{
			FileWriter fw = new FileWriter("C:\\Users\\ken\\Desktop\\test.csv", true);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print(",");
			pw.print("R1");
			pw.print(",");
			pw.print("R2");
			pw.print(",");
			pw.print("R3");
			pw.print(",");
			pw.print("LAST");
			pw.println();
			for(int i= 0;i < playerNum;i++)
			{
				pw.print("P"+ (i+1));
				pw.print(",");
				int temp = 0;
				for(int j = 0;j < roundNum;j++)
				{
					pw.print(scoreData[i][j]);
					pw.print(",");
					temp += scoreData[i][j];
				}
				server.sendAllMessage("player"+ (i+1) +"'s score");
				server.sendAllMessage(Integer.toString(temp));
				pw.print(temp);
				pw.println();
				if(temp > topS)
				{
					topS = temp;
					topP = i + 1;
				}
			}
			pw.close();
		}catch (IOException ex) 
		{
            ex.printStackTrace();
        }
		server.sendAllMessage("winner player"+ topP);
		server.sendAllMessage("winner score "+ topS);
		
		server.sendAllMessage("GAME END");
		
		server.close();
		server.gameEnd();
		System.out.println("sEND");
	}

}
