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
		this.name = "Spe." + color + "." + effect; 
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
	
	@Override
	public boolean isDrawcard()
	{
		if(this.effect == "D2")
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

}
