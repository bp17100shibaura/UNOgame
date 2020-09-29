package UNOset.player.tactics;
import UNOset.utils.*;
import java.util.Random;
import java.util.ArrayList;
import UNOset.server.RoundData;

public class Montecorlo 
{
	Deck deck;
	int playerNum;
	Hand myhand;
	int num; //自分のプレイヤー番号
	int[] handNum;
	CardList list = new CardList();
	int turnbase;
	Card topcard;
	
	public Montecorlo(DisCard dis, Hand myhand,int plyerNum, int num, int turnbase)
	{
		this.myhand = new Hand();
		this.myhand.handin(myhand.handout());
		String str = dis.getTopName();
		System.out.println(str+ "1");
		CardList list = new CardList();
		Card c = list.makeCard(str);
		System.out.println(c.getCardName()+ "2");
		this.topcard = c;
		this.playerNum = plyerNum;
		this.num = num;
		this.handNum = new int[playerNum];
		this.turnbase = turnbase;
		ArrayList<Card> temp = myhand.handout();
		this.deck = new Deck();
		deck.deckMake();
		deck.outCard(temp);
		temp = dis.openDiscard();
		deck.outCard(temp);
		deck.shuffle();
	}
	
	public void makeHand(int[] data) //相手手札数から手札を作成
	{
		for(int i = 0;i < 2;i++)
		{
			handNum[i] = data[i];
		}
		handNum[this.num-1] = this.myhand.getNum();
	}
	
	public boolean eval(RoundData best,RoundData cor,int count) //どっちの手が優れているか調べる 作る
	{
		int a = best.score[num-1];
		int b = cor.score[num-1];
		if(a < b)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Card cal() //可能手を作成　run()でシュミ
	{
		int count = 400;
		String[] co = {"b","g","y","r"};
		Card bestcard  = null;
		RoundData best = new RoundData();
		best.score = new int[4];
		best.score[this.num-1] = -9999;
		for(int i = 0;i < myhand.getNum();i++)
		{
			Card card = myhand.cardOut(i);
			card = list.makeCard(card.getCardName());
			RoundData temp;
			if(this.can(card))
			{
				if(bestcard == null)
				{
					bestcard = card;
					if(bestcard instanceof WildCard)
					{
						((WildCard) bestcard).changeColor("r");
					}
				}
				if(card instanceof WildCard)
				{
					for(int j = 0;j < 4;j++)
					{
						((WildCard) card).changeColor(co[j]);
						System.out.println(card.getCardName());
						RoundData tdata = new RoundData();
						tdata.score = new int[2];
						for(int k = 0;k < count;k++)
						{
							//System.out.println(j);
							temp = run(card);
							tdata.score[0] += temp.score[0];
							tdata.score[1] += temp.score[1];
							//tdata.score[2] += temp.score[2];
							//tdata.score[3] += temp.score[3];
							//System.out.println("nnnn");
						}
						if(eval(best,tdata,count) || bestcard == null)
						{
							best = tdata;
							bestcard = card;
							
						}
					}
				}else
				{
					RoundData tdata = new RoundData();
					System.out.println(card.getCardName() + "d");
					tdata.score = new int[2];
					for(int j = 0;j < count;j++)
					{
						//System.out.println(j);
						temp = run(card);
						tdata.score[0] += temp.score[0];
						tdata.score[1] += temp.score[1];
						//tdata.score[2] += temp.score[2];
						//tdata.score[3] += temp.score[3];
						//System.out.println("nnnn");
					}
					if(eval(best,tdata,count) || bestcard == null)
					{
						best = tdata;
						bestcard = card;
					}
				
				}
			}
		}
		System.out.println(bestcard.getCardName()+ "best");
		return bestcard;
	}
	
	public boolean can(Card card)
	{
		if(topcard.getCardColor().equals(card.getCardColor()))
		{
			return true;
		}
		else if(card instanceof NumberCard)
		{
		    if(topcard instanceof NumberCard)
		    {
			    int a = ((NumberCard) card).getCardNumber();
			    int b = ((NumberCard) topcard).getCardNumber();
			    if(a == b)
			    {
				    return true;
			    }
		    }
		}
		else if(card instanceof SpecialCard)
		{
		    if(topcard instanceof SpecialCard)
		    {
			    String a = ((SpecialCard) topcard).getEffect();
			    String b = ((SpecialCard) card).getEffect();
			    if(a.equals(b))
			    {
				    return true;
			    } 
		   }
		}
		else if(card instanceof WildCard)
		{
			return true;
		}
		return false;
	}
	
	public RoundData run(Card start)//ランダムシュミレーション サーバーのコピーかな 300回で
	{
		int limit = 0;
		String[] co = {"b","g","y","r"};
		RoundData result;
		Deck tdeck = new Deck();
		tdeck.newDeck(deck.outdeck());
		DisCard tdis = new DisCard();
		String str = this.topcard.getCardName();
		tdis.discard(list.makeCard(str));
		Hand[] thand = new Hand[2];
		Random rand = new Random();
		for(int i = 0;i < playerNum;i++)
		{
			if((i+1) != this.num)
			{
				thand[i] = new Hand();
				//System.out.println(i);
				for(int j = 0;j < this.handNum[i];j++)
				{
					Card temp = tdeck.draw1Card();
					if(temp instanceof WildCard)
					{
						((WildCard) temp).changeColor("w");
					}
					//System.out.println(temp.getCardName());
					thand[i].getCard(temp);
					
				}
			}
			else
			{
				//System.out.println("my");
				thand[i] = new Hand();
				thand[i].handin(this.myhand.handout());
			}
		}
		
		int base = this.turnbase;
		int turn = this.num;
		int skipcount = 0;
		int drawcount = 0;
		tdis.discard(start);
		Card tt = list.makeCard(start.getCardName());
		//System.out.println(start.getCardName() + "s");
		if(tt instanceof WildCard)
		{
			((WildCard) tt).changeColor("w");
		}
		thand[this.num-1].disCard(tt.getCardName());
		if(tt instanceof SpecialCard)
		{
			String ef = ((SpecialCard) tt).getEffect();
			if(ef.equals("Skp"))
			{
				skipcount++;
			}
			else if(ef.equals("Rvs"))
			{
				base = base * -1;
			}
			else if(ef.equals("D2"))
			{
				drawcount += 2;
			}
		}
		else if(tt instanceof WildCard)
		{
			String ef = ((WildCard) tt).getEffect();
			if(ef.matches(".*WD4.*"))
			{
				drawcount += 4;
			}
		}
		
		turn = turncount(turn,base,skipcount);
		while(true)
		{			
			limit ++;
			if(limit == 800)
			{
				result = new RoundData();
				result.score = new int[2];
				result.score[0] = 0;
				result.score[1] = 0;
				//result.score[2] = 0;
				//result.score[3] = 0;
				System.out.println("limited out!!!");
				return result;
			}
			/*ここにターンの処理*/
			Card card;
			
			if(drawcount > 0) //Dカードがたまってる場合
			{
				str = tdis.getTopName();
				card = list.makeCard(str);
				if(thand[turn-1].isdrawCard() && thand[turn-1].dcanDiscard(card)) //Dカードを持ってるか
				{
					/*for(int i = 0;i < thand[turn-1].getNum();i++)
					{
						card = thand[turn -1].cardOut(i);
						//System.out.println(card.getCardName());
					}*/
					int r = rand.nextInt(2);
					if(r == 0) //Dカードを使う
					{
						while(true)
						{
							r = rand.nextInt(thand[turn-1].getNum());
							card = thand[turn-1].cardOut(r);
							//System.out.println(card.getCardName());
							 if(card.isDrawcard())
							 {
								 //System.out.println(card.getCardName());
								 if(tdis.isDiscard(card))
								 {
									 break;
								 }
							 }
						}
						str = card.getCardName();
						card = thand[turn-1].disCard(str);
						
						if(card instanceof WildCard)
						{
							r = rand.nextInt(4);
							((WildCard) card).changeColor(co[r]);
						}
						//System.out.println(card.getCardName() + "d");
						tdis.discard(card);
					}
					else //Dカードを使わない
					{
						for(int i = 0;i < drawcount;i++)
						{
							thand[turn-1].getCard(getCard(tdeck,tdis));
						}
						drawcount = 0;
					}
				}
				else //Dカードを持ってない
				{
					for(int i = 0;i < drawcount;i++)
					{
						thand[turn-1].getCard(getCard(tdeck,tdis));
					}
					drawcount = 0;
				}
				
			}else //通常の場合
			{
				while(true)
				{
					str = tdis.getTopName();
					card = list.makeCard(str);
					if(!thand[turn-1].canDiscard(card))
					{
						thand[turn-1].getCard(getCard(tdeck,tdis));
					}
					else
					{
						break;
					}
				}
				
				while(true)
				{
					int r = rand.nextInt(thand[turn-1].getNum());
					card = thand[turn-1].cardOut(r);
					if(tdis.isDiscard(card))
					{
						break;
					}
				}
				
				str = card.getCardName();
				//System.out.println(turn);
				//System.out.println(str);
				card = thand[turn-1].disCard(str);
				if(card instanceof WildCard)
				{
					if(card instanceof WildCard)
					{
						int r = rand.nextInt(4);
						((WildCard) card).changeColor(co[r]);
						String ef = ((WildCard) card).getEffect();
						if(ef.matches(".*WD4.*"))
						{
							drawcount += 4;
						}
					}
				}
				tdis.discard(card);
				//System.out.println(card.getCardName() + "n");
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
						base = base * -1;
					}
					else if(ef.equals("D2"))
					{
						drawcount += 2;
					}
				}
				
			}
			
			thand[turn-1].cardFix();
			tdis.cardFix();
			
			if(thand[turn-1].getNum() == 0)
			{
				break;
			}
			turn = turncount(turn,base,skipcount);
			skipcount = 0;
		}
		result = this.calculate(thand);
		return result;
	}
	
	private Card getCard(Deck tdeck,DisCard tdis)
	{
		Card card = tdeck.draw1Card();
		if(card == null)
		{
			//System.out.println("new deck");
			ArrayList<Card> ndeck = tdis.openDiscard();
			tdeck.newDeck(ndeck);
			tdis.delete();
			card = tdeck.draw1Card();
		}
		if(card instanceof WildCard)
		{
			((WildCard) card).changeColor("w");
		}
		return card;
	}
	
	private RoundData calculate(Hand[] thand)
	{
		int[] score = new int[playerNum];
		int temp = 0;
		int winner = 0;
		
		for(int i = 0;i < playerNum;i++)
		{
			score[i] = - thand[i].result();
			temp += thand[i].result();
		}
		for(int i = 0;i < playerNum;i++)
		{
			if(score[i] == 0)
			{
				score[i] = temp;
				winner = i+1;
			}
		}
		RoundData result = new RoundData();
		result.score = score;
		result.winner = winner;
		
		return result;
	}
	
	private int turncount(int turn, int turnbase,int skipcount)
	{
		if(skipcount == 0)
		{
			if(turn == 2)
			{
				turn = 1;
			}
			else 
			{
				turn = 2;
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
	
}
