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
		
		deck.deckMake();
		deck.shuffle();
		CardList list = new CardList();
		
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
			String str = "";
			Card card;
			do 
			{
				server.sendMessage(turn, "yourturn");
				server.sendMessage(turn, "Top Card");
				server.sendMessage(turn, disCard.getTopName());
				str = server.catchMessage(turn);
			}while (str == "ok");
			
			server.sendMessage(turn,"card or command");
			str = server.catchMessage(turn);
			while(true)
			{
				if(str == "command")
				{
					command(turn);
				}
				else if(str != "card")
				{
					server.sendMessage(turn, "error");
				}
				else
				{
					break;
				}
			}
			
		    str = server.catchMessage(turn);
			while(true)
			{
				if(-1 == hand[turn-1].serchCard(str))
				{
					server.sendMessage(turn, "have'nt");
				}
				else if(!disCard.isDiscard(list.makeCard(str)))
				{
					server.sendMessage(turn, "can't discard");
				}
				else
				{
					break;
				}
			}
			card = hand[turn-1].disCard(str);
			disCard.discard(card);
			server.sendMessage(turn, "OK");
			
			server.sendAllMessage("player"+ turn + " discard");
			server.sendAllMessage(card.getCardName());
			
			/*�����ɃJ�[�h�̌��ʏ���*/
			
			
			/*�����ɏ��������̏���*/
			if(hand[turn-1].getNum() == 0)
			{
				break;
			}
			
			/*�^�[���ڍs�̏���*/
			turncount(turn,turnbase);
		}
		calculate(result);
		
		return result;
	}
	
	private int turncount(int turn, int turnbase)
	{
		turn += turnbase;
		if(turn == 0)
		{
			turn = playerNum;
		}
		else if(turnbase == 1)
		{
			if(turn > playerNum)
			{
				turn = turn - playerNum;
			}
		}
		else if(turnbase == -1)
		{
			if(turn < 0)
			{
				turn = playerNum + turn;
			}
		}
		
		return turn;
	}
	
	private void command(int turn)
	{
		System.out.println(turn);
	}
	
	private RoundData calculate(RoundData result)
	{
		int[] score = new int[playerNum];
		int temp = 0;
		
		for(int i = 0;i < playerNum;i++)
		{
			score[i] = - hand[i].result();
			temp += hand[i].result();
		}
		for(int i = 0;i < playerNum;i++)
		{
			if(score[i] == 0)
			{
				score[i] = temp;
			}
		}
		result.score = score;
		
		return result;
	}
}
