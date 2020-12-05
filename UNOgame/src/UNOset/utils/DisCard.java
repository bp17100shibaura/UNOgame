package UNOset.utils;

import java.util.ArrayList;

public class DisCard 
{
   private ArrayList<Card> cards = new ArrayList<Card>(); 
   
   public void discard(Card card)
   {
	   CardList l = new CardList();
	   Card c = l.makeCard(card.getCardName());
	   cards.add(c);
   }
   
   public ArrayList<Card> openDiscard()
   {
	   ArrayList<Card> openCard = new ArrayList<>(cards);
	   return openCard;
   }
   
   public void cardin(ArrayList<Card> in)
   {
	   this.cards = in;
   }
   
   public String getTopName()
   {
	   Card top = cards.get(cards.size()-1);
	   if(top == null)
	   {
		   top = cards.get(cards.size()-2);
	   }
	   return top.getCardName();
   }
   
   public String getTopColor()
   {
	   Card top = cards.get(cards.size()-1);
	   if(top == null)
	   {
		   top = cards.get(cards.size()-2);
	   }
	   return top.getCardColor();
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
   
   public void delete()
   {
	   Card top = cards.get(cards.size()-1);
	   if(top == null)
	   {
		   top = cards.get(cards.size()-2);
	   }
	   cards.clear();
	   cards.add(top);
   }
   
   public int getNum()
   {
	   return cards.size();   
   }
   
   public void fix(int num)
   {
	   for(int i = 0;i < num -1;i++)
	   {
		   cards.remove(i);
	   }
   }
   
   public void cardFix()
   {
	   int n = this.cards.size();
	   Card temp;
	   for(int i = 0;i < n;i++)
	   {
		   temp = cards.get(i);
		   if(temp == null)
		   {
			   cards.remove(i);
			   i--;
			   n--;
		   }
	   }
   }
}
