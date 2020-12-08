package UNOset.player.tactics;
import UNOset.server.RoundData;

public class Evaluation 
{
	private int player; //1 2 3 4
	private int enemy; //1 2 3 4
	
	public Evaluation(int player)
	{
		this.player = player;
	}
	
	public void setEnemy(int num)
	{
		this.enemy = num;
	}
	
	public boolean eval(int select,RoundData a, RoundData b)
	{
		boolean ans = true;
		//System.out.print(enemy + "  "+ select + "   ");
		switch(select)
		{
			case 1:
				ans = point(a,b);
				break;
			case 2:
				ans = rank(a,b);
				break;
			case 3:
				ans = downX(a,b);
				break;
			case 4:
				ans = upX(a,b);
				break;
			case 5:
				ans = pointDiffX(a,b);
				break;
		}
		
		return ans;
	}
	
	//1:単純に自分の点数で決める
	private boolean point(RoundData a, RoundData b) //Bが良かったらtrue
	{
		int pointA = a.score[player-1];
		int pointB = b.score[player-1];
		//System.out.print(pointA + " " + pointB);
		if(pointA < pointB)
		{
			//System.out.println(" > t");
			return true;
		}
		else
		{
			//System.out.println(" > f");
			return false;
		}
	}
	
	//2:自分の順位で決める
	private boolean rank(RoundData a, RoundData b) //Bが良かったらtrue
	{
		int rankA = a.winner;
		int rankB = b.winner;
		//System.out.print(rankA + " " + rankB);
		
		if(rankA == 0)
		{
			//System.out.println(" > st");
			return true;
		}
		
		if(rankB < rankA)
		{
			//System.out.println(" > t");
			return true;
		}
		else
		{
			//System.out.println(" > f");
			return false;
		}
	}
	
	//3:プレイヤーXの点を下げる
	private boolean downX(RoundData a, RoundData b)
	{
		int pointA = a.score[enemy-1];
		int pointB = b.score[enemy-1];
		//System.out.print(pointA + " " + pointB);
		if(pointA == -9999)
		{
			//System.out.println(" > st");
			return true;
		}
		if(pointB < pointA)
		{
			//System.out.println(" > t");
			return true;
		}
		else
		{
			//System.out.println(" > f");
			return false;
		}
	}
	
	//4:プレイヤーXの点を上げる
	private boolean upX(RoundData a, RoundData b)
	{
		int pointA = a.score[enemy-1];
		int pointB = b.score[enemy-1];
		//System.out.print(pointA + " " + pointB);
		if(pointB > pointA)
		{
			//System.out.println(" > t");
			return true;
		}
		else
		{
			//System.out.println(" > f");
			return false;
		}
	}
	
	//5：自分　-　プレイヤーX　の点を最大にする
	private boolean pointDiffX(RoundData a, RoundData b)
	{
		int difPointA = a.score[player - 1] - a.score[enemy-1];
		int difPointB = a.score[player -1] - b.score[enemy-1];
		
		if(difPointB > difPointA)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
