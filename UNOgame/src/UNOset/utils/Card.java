package UNOset.utils;

public interface Card {

	String getCardName();
	
	int getCardType(); //1:number 2:special 3:wild
	
	String getCardColor();
}