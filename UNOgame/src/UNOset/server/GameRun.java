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
		//�������ʂ̏���
		RoundData result = new RoundData();
		result.score = new int[playerNum];
		
		deck.deckMake();
		deck.shuffle();
		
		server.sendAllMessage("Game Start!");
		
		//������D�z�z
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
		
		//�����J�[�h�̔z�u
		Card temp = deck.draw1Card();
		disCard.discard(temp);
	    server.sendAllMessage("First Card");
	    server.sendAllMessage(temp.getCardName());
		
	    
		int turn = 1; //���̃v���C��
		int drawcount = 0; //�h���[2�n�̗��܂��
		int turnbase = 1; //���o�[�X��-1
		
		//�^�[���x�[�X
		while(true)
		{
			drawcount++;
			server.sendAllMessage("player"+ turn + "'s turn!");
			
			/*�����Ƀ^�[���̏���*/
			server.sendMessage(turn, "yourturn");
			server.catchMessage(turn);
			
			/*�����ɏ��������̏���*/
			
			
			
			/*�����̓^�[���o�߂̏���*/
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
