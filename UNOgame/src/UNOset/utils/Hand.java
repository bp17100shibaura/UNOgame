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
		this.cardFix();
		return hands.size();
	}
	
	public ArrayList<Card> handout()
	{
		ArrayList<Card> temp = new ArrayList<>(hands);
		return temp;
	}
	public void handin(ArrayList<Card> n)
	{
		this.hands = n;
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
		for(int i = 0;i < this.getNum() ;i++)
		{
			if(name.equals((hands.get(i)).getCardName()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public void crean()
	{
		for(int i =0;i < this.getNum();i++)
		{
			Card card = hands.get(i);
			if(card instanceof WildCard)
			{
				((WildCard) card).changeColor("w");
			}
		}
	}
	
	public boolean canDiscard(Card card)
	{
		Card h;
		boolean out = false;
		String color = card.getCardColor();
		for(int i = 0;i < this.getNum();i++)
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
	
	public boolean dcanDiscard(Card card)
	{
		Card h;
		boolean out = false;
		String color = card.getCardColor();
		for(int i = 0;i < this.getNum();i++)
		{
			h = hands.get(i);
			if(h.isDrawcard())
			{
				if(color == h.getCardColor())
				{
					return true;
				}
				else
				{
					if(h instanceof WildCard)
					{
						return true;
					}
				}
			}
		}
		
		return out;
	}
	
	public void cardFix()
	{
		int n = this.hands.size();
		Card temp;
		for(int i = 0;i < n;i++)
		{
			temp = hands.get(i);
			if(temp == null)
			{
				hands.remove(i);
				i--;
				n--;
			}
		}
	}
	
	
	public int result()
	{
		int result = 0;
		Card card;
		for(int i = 0;i < this.getNum();i++)
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
		for(int i = 0;i < this.getNum();i++)
		{
			Card card = hands.get(i);
			if(card.isDrawcard())
			{
				return true;
			}
		}
		return false;
	}
	
	public int drawnum()
	{
		int num = 0;
		for(int i = 0;i < this.getNum();i++)
		{
			Card card = hands.get(i);
			if(card.isDrawcard())
			{
				num++;
			}
		}
		return num;
	}
	
	public int colors(String co)
	{
		int num = 0;
		for(int i = 0;i < this.getNum();i++)
		{
			String temp = (hands.get(i)).getCardColor();
			if(temp.equals(co))
			{
				num++;
			}
		}
		return num;
	}
}
