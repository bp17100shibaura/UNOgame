package UNOset.player;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import GameSet.GamePlayer;
import UNOset.utils.*;

public class CUIplayer
{
		
	public static void main(String[] args)
	{
		int num = Integer.parseInt(args[0]);
		
	    GamePlayer player = new GamePlayer();
	    if(0 == player.playerSet(num))
	    {
	    	System.out.println("接続エラー");
	    	return;
	    }
	    InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
	    BufferedReader read = player.getReader();
		PrintWriter write = player.getWriter();
		Reader server = new Reader(write,read);
		
		int roundNum = 3;
		int playerNum = 4;
		int pnum = 0;
		int roundcount = 0;
		CardList list = new CardList();
		Card card;
		
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
							   do
							   {
								   str = br.readLine();
							   }while(str.matches(".*y.*") || str.matches(".*n.*"));
							   server.write(str);
							   if(str.matches(".*y.*")) //ドロー系を重ねる
							   {
								   for(int i = 0;i < hand.getNum();i++)
								   {
									   card = hand.cardOut(i);
									   if(card.isDrawcard())
									   {
										   System.out.println(i + card.getCardName());
									   }
								   }
								   while(true)
								   {
									   System.out.println("select draw card (num)");
									   str = br.readLine();
									   int s = Integer.parseInt(str);
									   card = hand.cardOut(s);
									   if(card.isDrawcard())
									   {
										   break;
									   }
								   }
								   server.write(card.getCardName());
								   server.read();
								   hand.disCard(card.getCardName());
								   
								   if(card instanceof WildCard)
								   {
									   server.read();
									   System.out.println("b/g/r/y");
									   while(true)
									   {
										   String color = br.readLine();
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
						   while(true)
						   {
							   str = br.readLine();
							   if(str.matches(".*command.*"))
							   {
								   server.write(str);
								   server.read();
								   str = server.read();
								   card = list.makeCard(str);
								   hand.getCard(card);
							   }
							   else if(str.matches(".*card.*"))
							   {
								   String temp = discard.getTopName();
								   card = list.makeCard(temp);
								   if(hand.canDiscard(card))
								   {
									   server.write(str);
									   break;
								   }
								   System.out.println("haven't discard card");
							   }
							   System.out.println("card or command");
						   }
						   
						   System.out.println("which discard? (num)");
						   for(int i = 0;i < hand.getNum();i++)
						   {
							   card = hand.cardOut(i);
							   System.out.println(i + card.getCardName());
						   }
						   while(true)
						   {
							   str = br.readLine();
							   int cnum = Integer.parseInt(str);
							   if(cnum >= 0 && cnum < hand.getNum())
							   {
								   card = hand.cardOut(cnum);
								   if(discard.isDiscard(card))
								   {
									   str = card.getCardName();
									   server.write(str);
									   break;
								   }
							   }
							   System.out.println("retry");
						   }
						   server.read();
						   server.read();
						   card = hand.disCard(str);
						   
						   if(card instanceof WildCard)
						   {
							   server.read();
							   System.out.println("b/g/r/y");
							   while(true)
							   {
								   String color = br.readLine();
								   server.write(color);
								   str = server.read();
								   if(str.matches(".*OK.*"))
								   {
									   ((WildCard) card).changeColor(color);
									   break;
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
			br.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		player.gameEnd();
		System.out.println("pEND");
	}
}
