package UNOset.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import UNOset.player.tactics.CardEval;
import GameSet.GamePlayer;
import UNOset.utils.Card;
import UNOset.utils.CardList;
import UNOset.utils.DisCard;
import UNOset.utils.Hand;
import UNOset.utils.WildCard;

public class HeuristicEarly {

	public static void main(String[] args) {
		
		int num = 3000;
		try
		{
			num = Integer.parseInt(args[0]);
		}catch(NumberFormatException e)
		{
			num = 3000;
		}
		
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("�ڑ��G���[");
	    	return;
	    }
		
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		Reader server = new Reader(write,read);
		
		int gamenum = 5; //������

		try 
		{
			while(gamenum > 0)
			{
				System.out.println("DUEL " + (6 - gamenum));
				int roundNum = 5; //���E���h��
				int playerNum = 4; //�v���C���[��
				int roundcount = 0;
				int pnum = 0;
				CardList list = new CardList();
				Card card;
				Random random = new Random(257);
				String[] co = new String[4];
				co[0] = "r";
				co[1] = "g";
				co[2] = "b";
				co[3] = "y";
		

				/*4�����̏���*/
				String str = read.readLine();
				//System.out.println(str);
				str = read.readLine();
				pnum = Integer.parseInt(str);
				System.out.println("you are player " + pnum);
				System.out.println("game start!");
			
				while(true)
				{
					server.read();
					server.read();
				
					/*���E���h�̏���*/
					Hand hand = new Hand();
					DisCard discard = new DisCard();
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
										str = server.sread();
										card = list.makeCard(str);
										hand.getCard(card);
									}
								}
								else //�������I��
								{
									int r = 0; //�m��ŕς�����
									if(r == 0) //�h���[�n���d�˂�
									{
										server.write("y");
										while(true)
										{
											int s = random.nextInt(hand.getNum());
											card = hand.cardOut(s);
											if(card.isDrawcard() && discard.isDiscard(card))
											{
												break;
											}
										}
										server.write(card.getCardName());
										server.sread();
										hand.disCard(card.getCardName());
								   
										if(card instanceof WildCard)
										{
											server.sread();
											while(true)
											{
												int s = 0;
												int p = 0;
												for(int i = 0;i <= 3;i++)
												{
													//���C���h�̐F
													int temp = hand.colors(co[i]);
													if(s < temp)
													{
														s = temp;
														p = i;
													}
												}
												String color = co[p];
												server.write(color);
												str = server.sread();
												if(str.matches(".*OK.*"))
												{
													((WildCard) card).changeColor(color);
													break;
												}
											}
										}else
										{
											server.sread();
										}
										discard.discard(card);
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
										server.write("card");
										break;
									}
								}
						   
								while(true)
								{
									str = discard.getTopName();
									Card tcard = list.makeCard(str);
									String tCol = tcard.getCardColor();
									int tNum = hand.colors(tCol);
									Card nextCard = null;
									int nextEval = 0;
									boolean wcard = false;
									boolean dcard = false;
							   
									if(hand.drawnum() > 1)
									{
										dcard = true;
									}
									if(hand.getNum() <= 4)
									{
										wcard = true;
									}
								   
									CardEval eval = new CardEval(tCol,tNum,dcard,wcard);
							   
									for(int i = 0;i < hand.getNum();i++)
									{
										card = hand.cardOut(i);
										if(discard.isDiscard(card))
										{
											if(nextCard == null)
											{
												int tempNum = hand.colors(card.getCardColor());
												nextCard = card;
												nextEval = eval.eval(card,tempNum);
											}
											else
											{
												int tempNum = hand.colors(card.getCardColor());
												int temp = eval.eval(card,tempNum);
												if(nextEval < temp)
												{
													nextCard = card;
												}
											}
										}	
									}
							   
							   
									if(discard.isDiscard(nextCard))
									{
										str = nextCard.getCardName();
										server.write(str);
										String sr = server.sread();
										if(sr.equals("ok"))
										{
											break; 
										}
										//System.out.println(str);
									}
								}
								server.sread();
								card = hand.disCard(str);
						   
								if(card instanceof WildCard)
								{
									while(true)
									{
										int c = random.nextInt(4);
										String color = co[c];
										server.write(color);
										str = server.sread();
										if(str.matches(".*OK.*"))
										{
											((WildCard) card).changeColor(color);
											break;
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
							str = server.sread();
							if(str.matches(".*stack draw.*"))
							{
								server.sread();  
							}else
							{
								while(str.matches(".*one draw.*"))
								{
									str = server.sread();
								}
						   
								str = server.sread();
								card = list.makeCard(str);
								discard.discard(card);
							}
							server.read();
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
			
				gamenum = gamenum - 1;
		}
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
