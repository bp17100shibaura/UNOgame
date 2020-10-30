package UNOset.player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import GameSet.GamePlayer;
import UNOset.utils.*;
import UNOset.player.tactics.*;

public class Monte2 
{
	/*�����_�����R�s�[��������*/
	/*����̎�D���ƃ^�[���ڍs�󋵂̓T�[�o�[����Ƃ���*/
	public static void main(String[] args) 
	{
		//int num = Integer.parseInt(args[0]);
		int num = 2500;
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("�ڑ��G���[");
	    	return;
	    }
		
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		
		int roundNum = 3;
		int playerNum = 2;
		int pnum = 0;
		int roundcount = 0;
		int[] hdata = new int[2];
		int base;
		CardList list = new CardList();
		Card card;
		Reader server = new Reader(write,read);
		String[] co = new String[4];
		co[0] = "r";
		co[1] = "g";
		co[2] = "b";
		co[3] = "y";
		
		try
		{
			/*4�����̏���*/
			String str = read.readLine();
			System.out.println(str);
			str = read.readLine();
			pnum = Integer.parseInt(str);
			HandPredict hpre[] = new HandPredict[playerNum];
			for(int i = 0;i < playerNum;i++)
			{
				hpre[i] = new HandPredict(i+1);
			}
			System.out.println("you are player " + pnum);
			System.out.println("game start!");
			
			while(true)
			{
				server.read();
				server.read();
				
			   /*���E���h�̏���*/
				Hand hand = new Hand();
				DisCard discard = new DisCard();
				for(int i = 0;i < playerNum;i++)
				{
					hpre[i].reset();
				}
				server.read();
				for(int i = 0;i < 7;i++)
				{
					str = server.sread();
					card = list.makeCard(str);
					hand.getCard(card);
				}
				
				server.read();
				str = server.read();
				card = list.makeCard(str);
				discard.discard(card);
				
				
			   /*���E���h*/
				while(true)
				{
					str = server.sread();
					if(str.matches(".*yourturn.*"))//���^�[��
					{
					   server.sread();
					   server.sread();
					   server.write("ok");
					   
					   str = server.sread();
					   if(str.matches(".*stack card.*")) //�h���[2�n����
					   {
						   server.sread();
						   str = server.sread();
						   if(str.matches(".*draw card.*")) //�J�[�h������
						   {
							   str = server.sread();
							   int cnum = Integer.parseInt(str);
							   for(int i = 0;i < cnum;i++)
							   {
								   str = server.read();
								   card = list.makeCard(str);
								   hand.getCard(card);
							   }
						   }
						   else //�������I��
						   {
							   server.write("hand");
							   for(int i = 0;i < 2;i++) //�����p
							   {
								   str = server.read();
								   hdata[i] = Integer.parseInt(str);
							   }
							   str = server.sread();
							   int dCount = Integer.parseInt(str);
							   str = server.sread();
							   base = Integer.parseInt(str);
							   
							   DMontecorlo dmonte = new DMontecorlo(discard, hand, playerNum, pnum, base, dCount,hpre);
							   dmonte.setHand(hdata);
							   card = dmonte.cal();
							      
							   if(card.getCardType() != 0) //�h���[�n���d�˂�
							   {
								   server.write("y");
								   String col = "b";
								   if(card instanceof WildCard)
								   {
									   col = card.getCardColor();
									   ((WildCard) card).changeColor("w");
								   }
								   Card out = card;
								   server.write(out.getCardName());
								   server.sread();
								   hand.crean();
								   hand.disCard(out.getCardName());
								   
								   if(out instanceof WildCard)
								   {
									   String color = col;
									   server.write(color);
									   str = server.sread();
									   ((WildCard) out).changeColor(color);
								   }else
								   {
									   server.sread();
								   }
								   discard.discard(out);
								   server.sread();
							   }
							   else //�h���[�n���d�˂Ȃ�
							   {
								   server.write("n");
								   server.sread();
								   str = server.sread();
								   int cnum = Integer.parseInt(str);
								   for(int i = 0;i < cnum;i++)
								   {
									   str = server.sread();
									   card = list.makeCard(str);
									   hand.getCard(card);
								   }
							   }
						   }
					   }
					   else //�ʏ�
					   {
						   server.write("turnbase");
						   str = server.sread();
						   base = Integer.parseInt(str);
						   
						   while(true)
						   {
							   str = discard.getTopName();
							   card = list.makeCard(str);
							   if(!hand.canDiscard(card))
							   {
								   server.write("command");
								   server.sread();
								   str = server.sread();
								   card = list.makeCard(str);
								   hand.getCard(card);
							   }
							   else
							   {
								   server.write("hand");
								   for(int i = 0;i < 2;i++) //�����p
								   {
									   str = server.sread();
									   hdata[i] = Integer.parseInt(str);
								   }
								   server.write("card");
								   break;
							   }
						   }
						   
						   //System.out.println("which discard? (num)");
						   /*for(int i = 0;i < hand.getNum();i++)
						   {
							   card = hand.cardOut(i);
							   System.out.println(i + card.getCardName());
						   }*/
						   String col;
						   while(true)
						   {
							   /*�����������e�J������*/
							   Montecorlo2 monte2 = new Montecorlo2(discard, hand, playerNum, pnum, base,hpre);
							   monte2.setHand(hdata);
							   card = monte2.cal();
							   if(true)
							   {
								   col = card.getCardColor();
								   if(card instanceof WildCard)
								   {
									   ((WildCard) card).changeColor("w");
								   }
								   str = card.getCardName();
								   server.write(str);
								   
								   break;
							   }
						   }
						   server.sread();
						   server.sread();
						   hand.crean();
						   card = hand.disCard(str);
						   
						   if(card instanceof WildCard)
						   {
							   while(true)
							   {
								   String color = col;
								   //System.out.println(col);
								   server.write(color);
								   str = server.sread();
								   if(str.matches(".*OK.*"))
								   {
									   ((WildCard) card).changeColor(color);
									   break;
								   }
								   else
								   {
									   System.out.println("monte col error");
									   
								   }
							   }
						   }
						   
						   discard.discard(card);
					   }
					   server.sread();
					   server.sread();
					   server.sread();
					   
					   str = server.sread();
					   if(str.matches(".*round end.*"))
					   {
						   break;
					   }
					}
					else //����̃^�[��
					{
						String temp = str.substring(7, 8);
						int turn = Integer.parseInt(temp);
						
						str = server.sread();
						if(str.matches(".*stack draw.*"))
						{
							hpre[turn-1].drawcount();
							server.sread();
						}else
						{
							while(str.matches(".*one draw.*"))
							{	
								str = server.sread();
								hpre[turn-1].passCard(discard.getTopColor());
							}
						   
							str = server.sread();
							card = list.makeCard(str);
							discard.discard(card);
						}
						server.sread();
						str = server.sread();
						if(str.matches("round end"))
						{
							//System.out.println("round "+ (roundcount+1) + " end");
							break;
						}
					}
				}
				/*�������ʏ���*/
				server.read();
				server.read();
				server.read();
				
				roundcount++;
				if(roundcount == roundNum)
				{
					break;
				}
			}
			/*4�����̏���*/
			server.read();
			server.read();
			
			for(int i = 0; i < playerNum; i++)
			{
				server.read();
				server.read();
			}
			
			server.read();
			server.read();
			server.read();
			
			read.close();
			write.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		player.gameEnd();
		System.out.println("pEND");

	}

}