package UNOset.player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import GameSet.GamePlayer;
import UNOset.utils.*;


public class RandomPlayer {

	public static void main(String[] args)
	{
		int num = Integer.parseInt(args[0]);
		int gn = Integer.parseInt(args[1]);
		//int gn = 10;
		
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("接続エラー");
	    	return;
	    }
		
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		Reader server = new Reader(write,read);
		
		int gameNum = gn;
		
		try
		{
			while(gameNum > 0)
			{
				System.out.println("DUEL "+ ((gn+1) - gameNum));
				int roundNum = 5;
				int playerNum = 4;
				//int pnum = 0;
				int roundcount = 0;
				CardList list = new CardList();
				Card card;
				String[] co = new String[4];
				co[0] = "r";
				Random random = new Random();
				co[1] = "g";
				co[2] = "b";
				co[3] = "y";
		
				/*4試合の準備*/
				String str = read.readLine();
				//System.out.println(str);
				str = read.readLine();
				//pnum = Integer.parseInt(str);
				//System.out.println("you are player " + pnum);
				//System.out.println("game start!");
			
				while(true)
				{
					server.read();
					server.read();
				
					/*ラウンドの準備*/
					Hand hand = new Hand();
					DisCard discard = new DisCard();
					server.sread();
					for(int i = 0;i < 7;i++)
					{
						str = server.sread();
						card = list.makeCard(str);
						hand.getCard(card);
					}
				
					server.sread();
					str = server.sread();
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
									int r = random.nextInt(2);
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
												int s = random.nextInt(4);
												String color = co[s];
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
									int cnum = random.nextInt(hand.getNum());
									card = hand.cardOut(cnum);
									if(discard.isDiscard(card))
									{
										str = card.getCardName();
										server.write(str);
										break;
									}
								}
								server.sread();
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
			
					server.sread();
					server.sread();
					server.sread();
					server.sread();
					server.sread();
					
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
