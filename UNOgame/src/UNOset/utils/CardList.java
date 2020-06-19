package UNOset.utils;

public class CardList 
{
	public Card makeCard(String name)
	{
	    	int temp = name.indexOf('.');
	    	String type = name.substring(0, temp);
	    	String next = name.substring(temp+1);
	    	if(type == "Num")
	    	{
	    		String color = String.valueOf(next.charAt(0));
	    		String numStr = next.substring(2);
	    		int num = Integer.parseInt(numStr);
	    		Card card = new NumberCard(num,color);
	    		return card;
	    	}
	    	else if(type == "Sp")
	    	{
	    		String color = String.valueOf(next.charAt(0));
	    		String effect = next.substring(2);
	    		Card card = new SpecialCard(effect,color);
	    		return card;
	    	}
	    	else if(type == "Wild")
	    	{
	    		String effect = next.substring(2);
	    		Card card = new WildCard(effect);
	    		return card;
	    	}
	    	
	    	return null;
	}
}
