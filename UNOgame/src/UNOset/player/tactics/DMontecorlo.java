package UNOset.player.tactics;
import UNOset.utils.*;
import java.util.Random;
import java.util.ArrayList;
import UNOset.server.RoundData;

public class DMontecorlo {
	Deck deck;
	int playerNum;
	Hand myhand;
	int num; //自分のプレイヤー番号
	int[] handNum;
	CardList list = new CardList();
	int turnbase;
	Card topcard;
	int dCount;
	HandPredict[] hpre;
	Evaluation evaluation;
	int tact = 1; //1:点数 2:順位 3:自分以外の一位との差
	int ct = 5;
	
	public DMontecorlo(DisCard dis, Hand myhand,int plyerNum, int num, int turnbase, int dCount, HandPredict[] hpre)
	{
		this.myhand = new Hand();
		this.myhand.handin(myhand.handout());
		String str = dis.getTopName();
		//System.out.println(str+ "1");
		CardList list = new CardList();
		Card c = list.makeCard(str);
		//System.out.println(c.getCardName()+ "2");
		this.topcard = c;
		this.playerNum = plyerNum;
		this.num = num;
		this.handNum = new int[playerNum];
		this.turnbase = turnbase;
		this.dCount = dCount;
		this.hpre = hpre;
		this.evaluation = new Evaluation(num);
		ArrayList<Card> temp = myhand.handout();
		this.deck = new Deck();
		deck.deckMake();
		deck.outCard(temp);
		temp = dis.openDiscard();
		deck.outCard(temp);
		deck.shuffle();
	}
	
	public void setHand(int[] data) //相手手札数から手札を作成
	{
		for(int i = 0;i < 4;i++)
		{
			handNum[i] = data[i];
		}
		handNum[this.num-1] = this.myhand.getNum();
	}
	
	public boolean eval(RoundData best,RoundData cor,int count) //どっちの手が優れているか調べる
	{
		boolean ans = evaluation.eval(tact, best, cor);
		return ans;
	}
	
	public void setTact(int tact,int enemy,int count)
	{
		this.ct = count;
		this.tact = tact;
		evaluation.setEnemy(enemy);
		//System.out.println(this.ct);
		//System.out.println(this.tact + " !! " +enemy);
	}

	public Card cal() //可能手を作成　run()でシュミ
	{
		int count = this.ct;
		String[] co = {"b","g","y","r"};
		Card bestcard  = null;
		RoundData best = new RoundData();
		best.score = new int[4];
		for(int i = 0;i < 4;i++)
		{
			best.score[i] = -9999;
		}
		best.winner = 9999;
		RoundData temp;
		for(int i = 0;i < this.myhand.getNum();i++)
		{
			Card card = this.myhand.cardOut(i);
			if(this.can(card))
			{
				if(card instanceof WildCard)
				{
					for(int j = 0;j < 4;j++)
					{
						((WildCard) card).changeColor(co[j]);
						RoundData tdata = new RoundData();
						tdata.score = new int[4];
						for(int k = 0;k < count;k++)
						{
							temp = run(card);
							tdata.score[0] += temp.score[0];
							tdata.score[1] += temp.score[1];
							tdata.score[2] += temp.score[2];
							tdata.score[3] += temp.score[3];
							tdata.winner += temp.winner;
							tdata.win += temp.win;
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
					tdata.score = new int[4];
					for(int j = 0;j < count;j++)
					{
						temp = run(card);
						tdata.score[0] += temp.score[0];
						tdata.score[1] += temp.score[1];
						tdata.score[2] += temp.score[2];
						tdata.score[3] += temp.score[3];
						tdata.winner += temp.winner;
						tdata.win += temp.win;
					}
					if(eval(best,tdata,count) || bestcard == null)
					{
						best = tdata;
						bestcard = card;
					}
				
				}
			}
		}
		
		Card pass = new Pass();
		RoundData tdata = new RoundData();
		tdata.score = new int[4];
		for(int j = 0;j < count;j++)
		{
			temp = run(pass);
			tdata.score[0] += temp.score[0];
			tdata.score[1] += temp.score[1];
			tdata.score[2] += temp.score[2];
			tdata.score[3] += temp.score[3];
			tdata.winner += temp.winner;
			tdata.win += temp.win;
		}
		if(eval(best,tdata,count) || bestcard == null)
		{
			best = tdata;
			bestcard = pass;
		}
		
		//System.out.println(bestcard.getCardName()+ "best");
		return bestcard;
	}
	
	public boolean can(Card card)
	{
		if(!card.isDrawcard())
		{
			return false;
		}
		if(topcard.getCardType() == 2)
		{
			return true;
		}
		if(card.getCardType() == 3)
		{
			return true;
		}
		return false;
	}
	
	public RoundData run(Card start)
	{
		int limit = 0;
		String[] co = {"b","g","y","r"};
		RoundData result;
		Deck tdeck = new Deck();
		tdeck.newDeck(deck.outdeck());
		DisCard tdis = new DisCard();
		String str = this.topcard.getCardName();
		tdis.discard(list.makeCard(str));
		Hand[] thand = new Hand[4];
		Random rand = new Random();
		for(int i = 0;i < playerNum;i++)
		{
			if((i+1) != this.num)
			{
				thand[i] = new Hand();
				//System.out.println(i);
				for(int j = 0;j < this.handNum[i];j++)
				{
					//int lt = 0;
					Card temp = tdeck.draw1Card();
					/*lt = 0;
					while(!hpre[i].cardCheck(temp))
					{
						tdeck.backCard(temp);
						temp = tdeck.draw1Card();
						lt++;
						if(lt > 10000)
						{
							//System.out.print("er?");
							break;
						}
					}*/
					
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
		int drawcount = this.dCount;
		if(start.getCardType() == 0)
		{
			if(dCount != 0)
			{
				for(int i = 0;i < dCount; i++)
				{
					thand[this.num-1].getCard(getCard(tdeck, tdis));
				}
				dCount = 0;
			}
		}
		else
		{
			tdis.discard(start);
			Card tt = list.makeCard(start.getCardName());
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
		}
		
		turn = turncount(turn,base,skipcount);
		while(true)
		{			
			limit ++;
			if(limit == 150)
			{
				result = new RoundData();
				result.score = new int[4];
				result.score[0] = 0;
				result.score[1] = 0;
				result.score[2] = 0;
				result.score[3] = 0;
				result.winner = 4;
				//System.out.println("limited out!!!");
				return result;
			}
			/*ここにターンの処理*/
			Card card;
			
			if(drawcount > 0) //Dカードがたまってる場合
			{
				str = tdis.getTopName();
				card = list.makeCard(str);
				if(thand[turn-1].dcanDiscard(card)) //Dカードを持ってるか
				{
					int r = rand.nextInt();
					if(r == 0) //Dカードを使う
					{
						while(true)
						{
							r = rand.nextInt(thand[turn-1].getNum());
							card = thand[turn-1].cardOut(r);
							 if(card.isDrawcard())
							 {
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
		int winner = 1;
		
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
			}
		}
		for(int i = 0;i < playerNum;i++)
		{
			if(score[num-1] < score[i])
			{
				winner++;
			}
		}
		RoundData result = new RoundData();
		
		result.score = score;
		result.winner = winner;
		if(winner == 1)
		{
			result.win = 1;
		}
		return result;
	}
	
	private int turncount(int turn,int turnbase,int skipcount)
	{

		turn += turnbase;
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
		}
		
		if(turn < 0)
		{
			turn = playerNum + turn;
		}
		
		return turn;
	}
	
}
