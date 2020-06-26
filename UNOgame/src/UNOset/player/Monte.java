package UNOset.player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import GameSet.GamePlayer;
import UNOset.utils.*;
import UNOset.player.tactics.*;

public class Monte 
{
	/*ランダムをコピーしただけ*/
	/*相手の手札数とターン移行状況はサーバーからとれるよ*/
	public static void main(String[] args) 
	{
		//int num = Integer.parseInt(args[0]);
		int num = 555;
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("接続エラー");
	    	return;
	    }
		
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		
		int roundNum = 3;
		int playerNum = 4;
		int pnum = 0;
		int roundcount = 0;
		int[] hdata = new int[4];
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
			/*4試合の準備*/
			String str = read.readLine();
			System.out.println(str);
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
					str = server.read();
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
					   server.read();
					   server.read();
					   server.write("ok");
					   
					   str = server.read();
					   if(str.matches(".*stack card.*")) //ドロー2系あり
					   {
						   server.read();
						   str = server.read();
						   if(str.matches(".*draw card.*")) //カードを引く
						   {
							   str = server.read();
							   int cnum = Integer.parseInt(str);
							   for(int i = 0;i < cnum;i++)
							   {
								   str = server.read();
								   card = list.makeCard(str);
								   hand.getCard(card);
							   }
						   }
						   else //引くか選ぶ
						   {
							   /*ここは状況により分岐*/
							   int a = 0;
							   for(int i = 0;i < hand.getNum();i++)
							   {
								   card = hand.cardOut(i);
								   if(card.isDrawcard())
								   {
									   a++;
								   }
							   }
							   int r = 1;
							   if(a > 1)
							   {
								   r = 0;
							   }
							   if(r == 0) //ドロー系を重ねる
							   {
								   System.out.println("b");
								   server.write("y");
								   for(int i = 0;i < hand.getNum();i++)
								   {
									   card = hand.cardOut(i);
									   if(card.isDrawcard())
									   {
										   System.out.println(i + card.getCardName());
									   }
								   }
								   Card out = null;
								   for(int i = 0;i < hand.getNum();i++)
								   {
									   card = hand.cardOut(i);
									   if(card.isDrawcard())
									   {
										   if(out == null)
										   {
											   out = card;
										   }
										   else if(out instanceof WildCard)
										   {
											   out = card;
										   }
									   }
								   }
								   server.write(card.getCardName());
								   server.read();
								   hand.crean();
								   hand.disCard(card.getCardName());
								   
								   if(card instanceof WildCard)
								   {
									   server.read();
									   while(true)
									   {
										   int s = 1;
										   int t = 0;
										   /*手札で一番多い色に*/
										   for(int i = 0;i < hand.getNum();i++)
										   {
											   int d = hand.colors(co[i]);
											   if(d > t)
											   {
												   s = i;
											   }
										   }
										   String color = co[s];
										   server.write(color);
										   str = server.read();
										   if(str.matches(".*OK.*"))
										   {
											   ((WildCard) card).changeColor(color);
											   break;
										   }
									   }
								   }else
								   {
									   server.read();
								   }
								   discard.discard(card);
							   }
							   else //ドロー系を重ねない
							   {
								   server.write("n");
								   server.read();
								   str = server.read();
								   int cnum = Integer.parseInt(str);
								   for(int i = 0;i < cnum;i++)
								   {
									   str = server.read();
									   card = list.makeCard(str);
									   hand.getCard(card);
								   }
							   }
						   }
					   }
					   else //通常
					   {
						   server.write("turnbase");
						   str = server.read();
						   base = Integer.parseInt(str);
						   
						   while(true)
						   {
							   str = discard.getTopName();
							   card = list.makeCard(str);
							   if(!hand.canDiscard(card))
							   {
								   server.write("command");
								   server.read();
								   str = server.read();
								   card = list.makeCard(str);
								   hand.getCard(card);
							   }
							   else
							   {
								   server.write("hand");
								   for(int i = 0;i < 4;i++)
								   {
									   str = server.read();
									   hdata[i] = Integer.parseInt(str);
								   }
								   server.write("card");
								   break;
							   }
						   }
						   
						   System.out.println("which discard? (num)");
						   for(int i = 0;i < hand.getNum();i++)
						   {
							   card = hand.cardOut(i);
							   System.out.println(i + card.getCardName());
						   }
						   String col;
						   int dd = 0;
						   while(true)
						   {
							   /*ここをモンテカルロで*/
							   Montecorlo monte = new Montecorlo(discard, hand, playerNum, pnum, base);
							   monte.makeHand(hdata);
							   card = monte.cal();
							   if(dd == 0)
							   {
								   System.out.println(card.getCardName() + "d");
								   dd++;
							   }
							   if(/*discard.isDiscard(card)*/true)
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
							   /*else
							   {
								   //System.out.println("monte not back");
							   }*/
						   }
						   server.read();
						   server.read();
						   hand.crean();
						   card = hand.disCard(str);
						   
						   if(card instanceof WildCard)
						   {
							   while(true)
							   {
								   String color = col;
								   System.out.println(col);
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
					   server.read();
					   server.read();
					   server.read();
					   
					   str = server.read();
					   if(str.matches(".*round end.*"))
					   {
						   break;
					   }
					}
					else //相手のターン
					{
					   str = server.read();
					   if(str.matches(".*stack draw.*"))
					   {
						 server.read();  
					   }else
					   {
						   while(str.matches(".*one draw.*"))
						   {
							   str = server.read();
						   }
						   
						   str = server.read();
						   card = list.makeCard(str);
						   discard.discard(card);
					   }
					   server.read();
					   str = server.read();
					   if(str.matches("round end"))
					   {
						   System.out.println("round "+ (roundcount+1) + " end");
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
