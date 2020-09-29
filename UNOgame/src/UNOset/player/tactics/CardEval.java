package UNOset.player.tactics;
import UNOset.utils.*;

public class CardEval 
{
	String topCol;
	int topNum;
	boolean dcard;
	boolean wcard;
	
	public CardEval(String topCol,int topNum,boolean dcard,boolean wcard)
	{
		this.topCol = topCol;
		this.topNum = topNum;
		this.dcard = dcard;
		this.wcard = wcard;
	}
	
	public int eval(Card card,int cardNum)
	{
		int score = 0;
		
		if(!(topCol.equals(card.getCardColor())))
		{
			if(topNum < cardNum)
			{
				score += 10;
			}
		}
		
		if(card instanceof NumberCard)
		{
			int num = ((NumberCard) card).getCardNumber();
			score += num;
		}
		else if(card.getCardType() == 2)
		{
			score += 20;
			if(card.isDrawcard() && !dcard)
			{
				score = 0;
			}
		}
		else if(wcard)
		{
			score += 50;
		}
		
		return score;
	}
}
