package UNOset.player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import GameSet.GamePlayer;
import UNOset.utils.*;
import UNOset.player.tactics.*;

public class Monte2 
{

	public static void main(String[] args) 
	{
		//int num = Integer.parseInt(args[0]);
		int num = 2500;
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("接続エラー");
	    	return;
	    }
		
	    System.out.println("player 1");
	    
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		Reader server = new Reader(write,read);
		
		int gameNum = 5; //試合数
		
		try 
		{
			while(gameNum > 0)
			{
				System.out.println("DUEL "+ (6 - gameNum));
				int roundNum = 5;
				int playerNum = 4;
				int pnum = 0;
				int roundcount = 0;
				int[] hdata = new int[4];
				int base;
				CardList list = new CardList();
				Card card;
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
				
					/*ラウンドの準備*/
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
				
				
					/*ラウンド*/
					while(true)
					{
						str = server.read();
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
									server.write("hand");
									for(int i = 0;i < playerNum;i++)
									{
										str = server.sread();
										hdata[i] = Integer.parseInt(str);
									}
									str = server.sread();
									int dCount = Integer.parseInt(str);
									str = server.sread();
									base = Integer.parseInt(str);
							   
									DMontecorlo dmonte = new DMontecorlo(discard, hand, playerNum, pnum, base, dCount,hpre);
									dmonte.setHand(hdata);
									card = dmonte.cal();
									
									server.sread();
							      
									if(card.getCardType() != 0) //ドロー系を重ねる
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
											str = server.sread();
											server.write(color);
											((WildCard) out).changeColor(color);
										}
										
										discard.discard(out);
										server.sread();
										
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
										for(int i = 0;i < playerNum;i++)
										{
											str = server.sread();
											hdata[i] = Integer.parseInt(str);
										}
										server.write("card");
									  break;
									}
								}

								String col;
								while(true)
								{
									/*ここをモンテカルロで*/
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
										server.write(color);
										str = server.sread();
										if(str.matches(".*OK.*"))
										{
											((WildCard) card).changeColor(color);
											break;
										}
										else
										{
											for(int i = 0;i < hand.getNum();i++)
											{
												System.out.println(hand.cardOut(i));
											}
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
						else //相手のターン
						{
							int turn  = 0;
							String temp = str.substring(7, 8);
							//try 
							//{
								turn = Integer.parseInt(temp);
							/*}catch(NumberFormatException e)
							{
								System.out.println("EX1:eroor");
								System.out.println(str);
								str = server.read();
								temp = str.substring(7, 8);
								turn = Integer.parseInt(temp);
							}*/
							
							str = server.read();
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
				/*試合の処理*/
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
			
				gameNum = gameNum - 1;
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
