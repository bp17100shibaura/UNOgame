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
	
	public Card cardOut(int num) 
	{
		Card card = hands.get(num);
		return card;
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
			if(name.equals((hands.get(i)).getCardName()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean canDiscard(Card card)
	{
		Card h;
		boolean out = false;
		String color = card.getCardColor();
		for(int i = 0;i < hands.size();i++)
		{
			h = hands.get(i);
			if(color.equals(h.getCardColor()))
			{
				return true;
			}
			else if(h instanceof WildCard)
			{
				return true;
			}
			else if(h instanceof NumberCard)
			{
				if(card instanceof NumberCard)
				{
					int a = ((NumberCard) card).getCardNumber();
					int b = ((NumberCard) h).getCardNumber();
					if(a == b)
					{
						return true;
					}
				}
			}
			else if(h instanceof SpecialCard)
			{
				if(card instanceof SpecialCard)
				{
					String a = ((SpecialCard) h).getEffect();
					String b = ((SpecialCard) card).getEffect();
					if(a.equals(b))
					{
						return true;
					}
				}
			}
		}
		
		return out;
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
	
	public boolean isdrawCard()
	{
		for(int i = 0;i < hands.size();i++)
		{
			Card card = hands.get(i);
			if(card.isDrawcard())
			{
				return true;
			}
		}
		return false;
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
			if(temp.equals("r"))
			{
				r++;
			}
			else if(temp.equals("g"))
			{
				g++;
			}else if(temp.equals("b"))
			{
				b++;
			}else if(temp.equals("y"))
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
