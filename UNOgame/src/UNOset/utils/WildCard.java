package UNOset.utils;

public class WildCard implements Card 
{

	private String color;
	private String effect;
	private String name;
	
	WildCard(String effect)
	{
		this.color = "w";
		this.effect = effect;
		this.name = "Wil." + color + "." + effect; 
	}
	
	@Override
	public String getCardName() 
	{
		return name;
	}

	@Override
	public int getCardType() 
	{
		return 3;
	}

	@Override
	public String getCardColor() 
	{
		return color;
	}
	
	@Override
	public boolean isDrawcard()
	{
		if(this.effect.matches(".*WD4.*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getEffect()
	{
		return effect;
	}
	
	private void reName()
	{
		this.name = "Wil." + color + "." + effect;
	}
	
	public void changeColor(String next)
	{
		color = next;
		reName();
	}

}
