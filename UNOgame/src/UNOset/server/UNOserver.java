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
		int gn = Integer.parseInt(args[2]);
		int outType = Integer.parseInt(args[0]);
		int fileNum = Integer.parseInt(args[1]);
		
		int a = 4001;
		int b = 4002;
		int c = 4003;
		int d = 4004;
		
		int singlePointData[] = new int[4];
		int singleRankData[] = new int[4];
		int singleWinData[] = new int[4];
		
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
	    
	    String file = "/home/tslab/bp17100/Exper";
	    if(fileNum == 1)
	    {
	    	file += "/src/";
	    }else if(fileNum == 2)
	    {
	    	file += "2/src/";
	    }
	    else if(fileNum == 3)
	    {
	    	file += "3/src/";
	    }
	    else if(fileNum == 4)
	    {
	    	file += "4/src/";
	    }
	    
	    int gameNum = gn;
	    try 
	    {	
	    	//FileWriter fw = new FileWriter("C:\\Users\\ken\\Desktop\\round.csv", true);
	    	//FileWriter fw2 = new FileWriter("C:\\Users\\ken\\Desktop\\single.csv", true);
	    	FileWriter fw = new FileWriter(file + "round.csv", true);
	    	PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
	    	
	    	FileWriter fw2 = new FileWriter(file + "single.csv", true);
	    	PrintWriter pw2 = new PrintWriter(new BufferedWriter(fw2));
	    	
	    	while(gameNum > 0)
	    	{
	    		//試合数
	    		int roundNum = 5;
	    		int roundCount = 0;
	    
	    		int[][] scoreData = new int[playerNum][roundNum];
	    		int[] totalScore = new int[playerNum];
	    
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
						totalScore[i] += data.score[i];
	    			}
	    			
	    			server.sendAllMessage("total score");
	    			server.sendAllMessage(Integer.toString(totalScore[0]));
	    			server.sendAllMessage(Integer.toString(totalScore[1]));
	    			server.sendAllMessage(Integer.toString(totalScore[2]));
	    			server.sendAllMessage(Integer.toString(totalScore[3]));
	    			
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
	    		
	    		int tRank[] = new int[4];
	    		for(int i = 0; i < roundNum;i++)
	    		{
	    			for(int k = 0;k < playerNum;k++)
	    			{
	    				int ct = 0;
	    				int tp = scoreData[k][i];
	    				for(int j = 0;j < playerNum;j++)
	    				{
	    					if(tp < scoreData[j][i])
	    					{
	    						ct++;
	    					}
	    				}
	    				tRank[k] = ct + 1; 
	    			}
	    			
	    			for(int l = 0;l < playerNum;l++)
		    		{
		    			singlePointData[l] += scoreData[l][i];
		    			singleRankData[l] += tRank[l];
		    			if(tRank[l] == 1)
		    			{
		    				singleWinData[l] ++;
		    			}
		    		}
	    		}
	    		
	    		
	    		int temp[] = new int[4];
	    		for(int i = 0;i < playerNum;i++)
	    		{
	    			temp [i] = 0;
	    			for(int j = 0;j < roundNum;j++)
	    			{
	    				temp[i] += scoreData[i][j];
	    			}
	    			server.sendAllMessage("player " + (i+1) + "'s score");
	    			server.sendAllMessage(Integer.toString(temp[i]));
	    			pw.print(temp[i]);
	    			pw.print(",");
	    			if(topS < temp[i])
	    			{
	    				topS = temp[i];
	    				topP = i+1;
 	    			}	
	    		}
	    		
	    		//順位の確定
	    		int rank[] = new int[4];
	    		rank[topP-1] = 1;
	    		for(int i = 0;i < playerNum ; i++)
	    		{
	    			int count = 0;
	    			for(int j = 0;j < playerNum ; j++)
	    			{
	    				if(temp[i] < temp[j])
	    				{
	    					count++;
	    				}
	    			}
	    			rank[i] = count+1;
	    			pw.print(rank[i]);
	    			pw.print(",");
	    		}
	    		
	    		pw.println();
	    		

	    		server.sendAllMessage("winner player"+ topP);
	    		server.sendAllMessage("winner score "+ topS);
		
	    		server.sendAllMessage("GAME END");
	    	
	    		gameNum = gameNum - 1;
	    		
	    		//System.out.println(" 1:"+temp[0] + " 2:"+ temp[1] + " 3:" + temp[2] + " 4:" +temp[3]);
	    	}
	    	
    		pw.println();
    		pw.close();
    		fw.close();
    		if(outType == 2)
    		{
    			for(int i = 0;i < playerNum;i++)
    			{
    				pw2.print(singlePointData[i]);
    				pw2.print(",");
    			}
    			pw2.println();
    			for(int i = 0;i < playerNum;i++)
    			{
    				pw2.print(singleRankData[i]);
    				pw2.print(",");
    			}
    			pw2.println();
    			for(int i = 0;i < playerNum;i++)
    			{
    				pw2.print(singleWinData[i]);
    				pw2.print(",");
    			}
    			pw2.println();
    		}
    		pw2.close();
    		fw2.close();
	    }catch (IOException ex) 
	    {
			ex.printStackTrace();
		}
	    server.close();
		server.gameEnd();
		System.out.println("sEND");
	}

}
