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
	   if(top.getCardColor().equals(card.getCardColor()))
	   {
		   return true;
	   }
	   else if(card instanceof NumberCard)
	   {
		   if(top instanceof NumberCard)
		   {
			   int a = ((NumberCard) card).getCardNumber();
			   int b = ((NumberCard) top).getCardNumber();
			   if(a == b)
			   {
				   return true;
			   }
		   }
	   }
	   else if(card instanceof SpecialCard)
	   {
		   if(top instanceof SpecialCard)
		   {
			   String a = ((SpecialCard) top).getEffect();
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
   
   public int getNum()
   {
	   return cards.size();   
   }
}
