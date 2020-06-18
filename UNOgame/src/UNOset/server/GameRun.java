package UNOset.server;
import UNOset.utils.*;

public class GameRun
{
	TCPserver server;
	private int playerNum;
	private Deck deck;
	private DisCard disCard;
	private Hand[] hand;
	
	GameRun(int playerNum,TCPserver server)
	{
		this.server = server;
		this.playerNum = playerNum;
		this.deck = new Deck();
		this.disCard = new DisCard();
		this.hand = new Hand[playerNum];
		for(int i = 0;i < playerNum;i++)
		{
			hand[i] = new Hand();
		}
	}
	
	RoundData match()
	{
		//試合結果の準備
		RoundData result = new RoundData();
		result.score = new int[playerNum];
		
		deck.deckMake();
		deck.shuffle();
		
		server.sendAllMessage("Game Start!");
		
		//初期手札配布
		for(int j = 0;j < 7;j++) 
		{
		    for(int i = 0;i < playerNum;i++)
		    {
			    server.sendMessage(i+1, "Start Cards");
			    Card temp = deck.draw1Card();
			    if(temp == null)
			    {
				    System.out.println("error A");
			    }
			    hand[i].getCard(temp);
			    server.sendMessage(i+1,temp.getCardName());
		    }
		}
		
		//初期カードの配置
		Card temp = deck.draw1Card();
		disCard.discard(temp);
	    server.sendAllMessage("First Card");
	    server.sendAllMessage(temp.getCardName());
		
	    
		int turn = 1; //今のプレイヤ
		int drawcount = 0; //ドロー2系の溜まり方
		int turnbase = 1; //リバースで-1
		
		//ターンベース
		while(true)
		{
			drawcount++;
			server.sendAllMessage("player"+ turn + "'s turn!");
			
			/*ここにターンの処理*/
			server.sendMessage(turn, "yourturn");
			server.catchMessage(turn);
			
			/*ここに勝利条件の処理*/
			
			
			
			/*ここはターン経過の処理*/
			turn += turnbase;
			if(turn == playerNum+1)
			{
				turn = 0;
			}
			else if(turn == 0)
			{
				turn = playerNum;
			}
			if(drawcount > 20)
			{
				break;
			}
		}
		
		return result;
	}
}
