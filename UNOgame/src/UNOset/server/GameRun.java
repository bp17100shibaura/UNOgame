package UNOset.server;
import java.util.ArrayList;

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
		
		deck.deckMake();
		deck.shuffle();
		CardList list = new CardList();
		
		server.sendAllMessage("Game Start!");
		
		//初期手札配布
		server.sendAllMessage("start cards");
		for(int j = 0;j < 7;j++) 
		{
		    for(int i = 0;i < playerNum;i++)
		    {
			    Card temp = deck.draw1Card();
			    if(temp == null)
			    {
				    System.out.println("error A");
			    }
			    System.out.println(temp.getCardName());
			    hand[i].getCard(temp);
			    server.sendMessage(i+1,temp.getCardName());
			    
		    }
		}
		
		//初期カードの配置
		Card temp = deck.draw1Card();
		while(temp instanceof WildCard)
		{
			temp = deck.draw1Card();
		}
		disCard.discard(temp);
	    server.sendAllMessage("First Card");
	    server.sendAllMessage(temp.getCardName());
		
	    
		int turn = 1; //今のプレイヤ
		int drawcount = 0; //ドロー2系の溜まり方
		int turnbase = 1; //リバースで-1
		int skipcount = 0;
		
		
		//ターンベース
		while(true)
		{
			for(int i = 0;i < playerNum;i++)
			{
				if(i+1 != turn)
				{
					server.sendMessage(i+1,"player "+ turn+ "'s turn");
				}
			}
			
			/*ここにターンの処理*/
			String str = "";
			Card card;
			server.sendMessage(turn, "yourturn");
			server.sendMessage(turn, "Top Card");
			server.sendMessage(turn, disCard.getTopName());
			str = server.catchMessage(turn);
			
			if(drawcount > 0) //Dカードがたまってる場合
			{
				server.sendMessage(turn, "stack card");
				server.sendMessage(turn, "you draw "+ drawcount + " card");
				str = disCard.getTopName();
				card = list.makeCard(str);
				if(hand[turn-1].isdrawCard() && hand[turn-1].canDiscard(card)) //Dカードを持ってるか
				{
					server.sendMessage(turn, "play Dcard? y/n");
					String s = server.catchMessage(turn);
					if(s.equals("hand"))
					{
						int a = hand[0].getNum();
						int b = hand[1].getNum();
						//int c = hand[2].getNum();
						//int d = hand[3].getNum();
						server.sendMessage(turn,String.valueOf(a));
						server.sendMessage(turn,String.valueOf(b));
						//server.sendMessage(turn,String.valueOf(c));
						//server.sendMessage(turn,String.valueOf(d));
						
						a = drawcount;
						server.sendMessage(turn, String.valueOf(a));
						server.sendMessage(turn, String.valueOf(turnbase));
						server.sendMessage(turn, "play Dcard? y/n");
						s = server.catchMessage(turn);
					}
					if(s.matches(".*y.*")) //Dカードを使う
					{
						while(true)
						{
							str = server.catchMessage(turn);
							if(null != (card = list.makeCard(str)))
							{
								if(-1 != hand[turn-1].serchCard(str))
								{
									if(card.isDrawcard())
									{
										if(card instanceof SpecialCard)
										{
											drawcount += 2;
										}
										else if(card instanceof WildCard)
										{
											drawcount += 4;
										}
										server.sendMessage(turn, "ok");
										break;
									}
									else 
									{
										server.sendMessage(turn, "not drawcard");
									}
								}
								else
								{
									server.sendMessage(turn, "don't have");
								}
							}
							else 
							{
								server.sendMessage(turn, "error");
							}
						}
						card = hand[turn-1].disCard(str);
						
						if(card instanceof WildCard)
						{
							server.sendMessage(turn, "What's color");
							while(true)
							{
								String co = server.catchMessage(turn);
								if(co.matches(".*b.*") || co.matches(".*g.*") || co.matches(".*r.*") || co.matches(".*y.*"))
								{
									((WildCard) card).changeColor(co);
									break;
								}
								else
								{
									server.sendMessage(turn, "retry");
								}
							}
						}
						
						disCard.discard(card);
						server.sendMessage(turn, "OK");
						
						
					
						server.sendAllMessage("player"+ turn + " discard");
						server.sendAllMessage(card.getCardName());
					}
					else //Dカードを使わない
					{
						server.sendMessage(turn, "draw card");
						server.sendMessage(turn, Integer.toString(drawcount));
						Card c;
						for(int i = 0;i < drawcount;i++)
						{
							c = command(turn);
							server.sendMessage(turn, c.getCardName());
						}
						server.sendAllMessage("stack draw");
						server.sendAllMessage(Integer.toString(drawcount));
						drawcount = 0;
					}
				}
				else //Dカードを持ってない
				{
					server.sendMessage(turn, "draw card");
					server.sendMessage(turn, Integer.toString(drawcount));
					Card c;
					for(int i = 0;i < drawcount;i++)
					{
						c = command(turn);
						server.sendMessage(turn, c.getCardName());
					}
					server.sendAllMessage("stack draw");
					server.sendAllMessage(Integer.toString(drawcount));
					
					drawcount = 0;
				}
				
			}else //通常の場合
			{
				server.sendMessage(turn,"card or command");
				while(true)
				{
					str = server.catchMessage(turn);
					if(str.equals("command"))
					{
						System.out.println("command");
						card = command(turn);
						server.sendAllMessage("one draw");
						server.sendMessage(turn, card.getCardName());
					}
					else if(str.equals("card"))
					{
						break;
					}
					else if(str.equals("turnbase"))
					{
						server.sendMessage(turn, String.valueOf(turnbase));
					}
					else if(str.equals("hand"))
					{
						int a = hand[0].getNum();
						int b = hand[1].getNum();
						//int c = hand[2].getNum();
						//int d = hand[3].getNum();
						server.sendMessage(turn,String.valueOf(a));
						server.sendMessage(turn,String.valueOf(b));
						//server.sendMessage(turn,String.valueOf(c));
						//server.sendMessage(turn,String.valueOf(d));
					}
					else
					{
						 System.out.println("eroor");
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
						server.sendMessage(turn,"ok");
						break;
					}
				}
				card = hand[turn-1].disCard(str);
				if(card instanceof WildCard)
				{
					server.sendMessage(turn, "What's color");
					while(true)
					{
						String co = server.catchMessage(turn);
						if(co.matches(".*b.*") || co.matches(".*g.*") || co.matches(".*r.*") || co.matches(".*y.*"))
						{
							((WildCard) card).changeColor(co);
							break;
						}
						else
						{
							server.sendMessage(turn, "retry");
						}
						String ef = ((WildCard) card).getEffect();
						if(ef.matches(".*WD4.*"))
						{
							drawcount += 4;
						}
					}
				}
				disCard.discard(card);
				server.sendMessage(turn, "OK");
			
				server.sendAllMessage("player"+ turn + " discard");
				server.sendAllMessage(card.getCardName());
			
				/*ここにカードの効果処理*/
				if(card instanceof SpecialCard)
				{
					String ef = ((SpecialCard) card).getEffect();
					if(ef.equals("Skp"))
					{
						skipcount++;
					}
					else if(ef.equals("Rvs"))
					{
						turnbase = turnbase * -1;
					}
					else if(ef.equals("D2"))
					{
						drawcount += 2;
					}
				}
				
			
			}
			server.sendAllMessage("turn end");
			/*ここに勝利条件の処理*/
			if(hand[turn-1].getNum() == 0)
			{
				break;
			}
			server.sendAllMessage("next turn");
			/*ターン移行の処理*/
			turn = turncount(turn,turnbase,skipcount);
			skipcount = 0;
		}
		server.sendAllMessage("round end");
		calculate(result);
		
		return result;
	}
	
	private int turncount(int turn, int turnbase,int skipcount)
	{
		if(skipcount == 0) 
		{
			if(turn == 1)
			{
				turn = 2;
			}
			else
			{
				turn = 1;
			}
		}
			/*turn += turnbase;
		
		turn += turnbase * skipcount;
		if(turn == 0)
		{
			turn = playerNum;
		}
		else if(turn > playerNum)
		{
			turn = turn - playerNum;
		}
		else if(turn < 0)
		{
			turn = playerNum + turn;
		}*/
		
		/*if(turn == 1)
		{
			turn = 2;
		}
		else
		{
			turn = 1;
		}*/
		
		return turn;
	}
	
	private Card command(int turn)
	{
		Card card = deck.draw1Card();
		if(card == null)
		{
			ArrayList<Card> ndeck = disCard.openDiscard();
			deck.newDeck(ndeck);
			disCard.delete();
			card = deck.draw1Card();
		}
		hand[turn-1].getCard(card);
		return card;
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
				result.winner = i+1;
			}
		}
		result.score = score;
		
		return result;
	}
}
