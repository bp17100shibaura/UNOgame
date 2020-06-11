package UNOset.utils;

import java.util.ArrayList;

public class DisCard 
{
   private ArrayList<Card> cards = new ArrayList<Card>(); 
   
   void discard(Card card)
   {
	   cards.add(card);
   }
   
   ArrayList<Card> openDiscard()
   {
	   ArrayList<Card> openCard = new ArrayList<>(cards);
	   return openCard;
   }
   
   String getTopName()
   {
	   Card top = cards.get(cards.size()-1);
	   return top.getCardName();
   }
   
   int getNum()
   {
	   return cards.size();   
   }
}
