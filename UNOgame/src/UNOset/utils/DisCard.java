package UNOset.utils;

import java.util.ArrayList;

public class DisCard 
{
   private ArrayList<Card> cards = new ArrayList<Card>(); 
   
   public void discard(Card card)
   {
	   cards.add(card);
   }
   
   public ArrayList<Card> openDiscard()
   {
	   ArrayList<Card> openCard = new ArrayList<>(cards);
	   return openCard;
   }
   
   public String getTopName()
   {
	   Card top = cards.get(cards.size()-1);
	   return top.getCardName();
   }
   
   public boolean isDiscard(Card card)
   {
	   Card top = cards.get(cards.size() -1);
	   if(top.getCardColor() == card.getCardColor())
	   {
		   return true;
	   }
	   else if(top.getCardName() == card.getCardName())
	   {
		   return true;
	   }
	   else if(card.getCardColor() == "w")
	   {
		   return true;
	   }
	   return false;
   }
   
   public int getNum()
   {
	   return cards.size();   
   }
}
