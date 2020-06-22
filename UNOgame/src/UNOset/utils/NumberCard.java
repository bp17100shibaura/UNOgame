package UNOset.utils;

public class NumberCard implements Card 
{
	
	private int num;
	private String color;
	private String name;

	NumberCard(int num,String color)
	{
		this.num = num;
		this.color = color;
		this.name = "Num." + color + "." + Integer.toString(num);
	}
	
	@Override
	public String getCardName() 
	{
		return name;
	}

	@Override
	public int getCardType() 
	{
		return 1;
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
	
	public int getCardNumber()
	{
		return num;
	}

}
