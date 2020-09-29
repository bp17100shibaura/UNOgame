package UNOset.utils;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	private ArrayList<Card> deck = new ArrayList<Card>();
	
	public void deckMake()
	{
		Card temp;
		String color[] = {"b","g","y","r"};
		String effect[] = {"D2","Rvs","Skp"};
		
		for(int i = 0; i < 4 ; i ++) //F
		{
			temp = new NumberCard(0,color[i]); //”Žš0
			deck.add(temp);
			
			for(int j = 1; j < 10; j++)//”Žš1~9
			{
				temp = new NumberCard(j,color[i]);
				deck.add(temp);
				temp = new NumberCard(j,color[i]);
				deck.add(temp);
			}
			
			for(int j = 0; j < 3; j ++)//“ÁŽê
			{
				temp = new SpecialCard(effect[j],color[i]);
				deck.add(temp);
				temp = new SpecialCard(effect[j],color[i]);
				deck.add(temp);
			}
		}
		
		for(int i = 0; i < 4 ;i++)//wild
		{
			temp = new WildCard("WD4");
			deck.add(temp);
			temp = new WildCard("Wild");
			deck.add(temp);
		}
		
	}
	
	public void newDeck(ArrayList<Card> ndeck)
	{
		deck.clear();
		deck = ndeck;
		this.shuffle();
	}
	
	public void outCard(ArrayList<Card> outcard)
	{
		for(int i = 0;i < outcard.size();i++)
		{
			Card card = outcard.get(i);
			if(card instanceof WildCard)
			{
				((WildCard) card).changeColor("w");
			}
			int num = this.serchCard(card.getCardName());
			if(num > 0)
			{
				deck.remove(num);
			}
		}
	}
	
	public int serchCard(String name)
	{
		for(int i = 0;i < deck.size() ;i++)
		{
			if(name.equals((deck.get(i)).getCardName()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public ArrayList<Card> outdeck()
	{
		ArrayList<Card> ndeck = new ArrayList<>(deck);
		return ndeck;
	}
	
	public void shuffle()
	{
		Collections.shuffle(deck);
	}
	
	public Card draw1Card()
	{
		if(deck.size() == 0)
		{
			return null;
		}
		else
		{
			int size = deck.size();
			return deck.remove(size-1);
		}
	}
	
	public int getDeckNum()
	{
		return deck.size();
	}
	
}
