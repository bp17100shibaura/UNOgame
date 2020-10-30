package UNOset.utils;

public class Pass implements Card 
{
	private int type = 0;
    private String color = "d";
    private String name = "pass";
    
	@Override
	public String getCardName() 
	{
		return name;
	}

	@Override
	public int getCardType() 
	{
		return type;
	}

	@Override
	public String getCardColor() 
	{
		return color;
	}

	@Override
	public boolean isDrawcard() 
	{
		return false;
	}

}
