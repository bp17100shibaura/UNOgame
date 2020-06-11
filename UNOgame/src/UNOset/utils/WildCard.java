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
		this.name = "Wild." + color + "." + effect; 
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
	
	public String getEffect()
	{
		return effect;
	}
	
	public void changeColor(String next)
	{
		color = next;
	}

}
