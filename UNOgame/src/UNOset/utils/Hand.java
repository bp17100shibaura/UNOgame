package UNOset.utils;

import java.util.ArrayList;

public class Hand 
{
	private ArrayList<Card> hands = new ArrayList<Card>();
	
	public void getCard(Card card)
	{
		hands.add(card);
	}
	
	public int getNum()
	{
		return hands.size();
	}
	
	public Card disCard(String name)
	{
		int index = serchCard(name);
		if(index == -1)
		{
			return null;
		}
		
		Card temp = hands.remove(index);
		return temp;
	}
	
	public int serchCard(String name)
	{
		for(int i = 0;i < hands.size() ;i++)
		{
			if(name == (hands.get(i)).getCardName())
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public int result()
	{
		int result = 0;
		Card card;
		for(int i = 0;i < hands.size();i++)
		{
			card = hands.get(i);
			if(card instanceof NumberCard)
			{
				result += ((NumberCard) card).getCardNumber();
			}
			else if(card.getCardType() == 2)
			{
				result += 20;
			}
			else if(card.getCardType() == 3)
			{
				result += 50;
			}
		}
		return result;
	}
	
	public String colors()
	{
		int r = 0;
		int g = 0;
		int y = 0;
		int b = 0;
		int w = 0;
		for(int i = 0;i < hands.size();i++)
		{
			String temp = (hands.get(i)).getCardColor();
			if(temp == "r")
			{
				r++;
			}
			else if(temp == "g")
			{
				g++;
			}else if(temp == "b")
			{
				b++;
			}else if(temp == "y")
			{
				y++;
			}else
			{
				w++;
			}
		}
		return "R"+ r +"/G" +g +"/B" +b +"/Y"+ y + "/W" + w ;
	}
}
