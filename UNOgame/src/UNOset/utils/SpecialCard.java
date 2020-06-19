package UNOset.utils;

public class SpecialCard implements Card 
{
	
	private String color;
	private String effect;
	private String name;
	
	SpecialCard(String effect,String color)
	{
		this.color = color;
		this.effect = effect;
		this.name = "Sp." + color + "." + effect; 
	}

	@Override
	public String getCardName() 
	{
		return name;
	}

	@Override
	public int getCardType() 
	{
		return 2;
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

}
