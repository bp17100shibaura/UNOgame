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
   
   public int getNum()
   {
	   return cards.size();   
   }
}
