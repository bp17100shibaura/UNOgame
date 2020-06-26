package UNOset.utils;

public class CardList 
{
	public Card makeCard(String name)
	{
	    	int temp = 4;
	    	String type = name.substring(0, temp);
	    	String next = name.substring(temp);
	    	if(type.matches(".*Num.*"))
	    	{
	    		String color = String.valueOf(next.charAt(0));
	    		String numStr = next.substring(2);
	    		int num = Integer.parseInt(numStr);
	    		Card card = new NumberCard(num,color);
	    		return card;
	    	}
	    	else if(type.matches(".*Spe.*"))
	    	{
	    		String color = String.valueOf(next.charAt(0));
	    		String effect = next.substring(2);
	    		Card card = new SpecialCard(effect,color);
	    		return card;
	    	}
	    	else if(type.matches(".*Wil.*"))
	    	{
	    		String color = String.valueOf(next.charAt(0));
	    		String effect = next.substring(2);
	    		WildCard card = new WildCard(effect);
	    		card.changeColor(color);
	    		return card;
	    	}
	    	
	    	return null;
	}
	
}
