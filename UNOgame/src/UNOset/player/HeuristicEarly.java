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
	    	System.out.println("接続エラー");
	    	return;
	    }
		
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		Reader server = new Reader(write,read);
		
		int gamenum = 5; //試合回数

		try 
		{
			while(gamenum > 0)
			{
				System.out.println("DUEL " + (6 - gamenum));
				int roundNum = 5; //ラウンド数
				int playerNum = 4; //プレイヤー数
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
		

				/*4試合の準備*/
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
				
					/*ラウンドの準備*/
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
					/*ラウンド*/
					while(true)
					{
						str = server.sread();
						if(str.matches(".*yourturn.*"))//自ターン
						{
							server.sread();
							server.sread();
							server.write("ok");
					   
							str = server.sread();
							if(str.matches(".*stack card.*")) //ドロー2系あり
							{
								server.sread();
								str = server.sread();
								if(str.matches(".*draw card.*")) //カードを引く
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
								else //引くか選ぶ
								{
									int r = 0; //確定で変えすよ
									if(r == 0) //ドロー系を重ねる
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
													//ワイルドの色
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
									else //ドロー系を重ねない
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
							else //通常
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
						else //相手のターン
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
					/*試合結果処理*/
					server.read();
					server.read();
					server.read();
				
					roundcount++;
					if(roundcount == roundNum)
					{
						break;
					}
				}
				/*4試合の処理*/
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
