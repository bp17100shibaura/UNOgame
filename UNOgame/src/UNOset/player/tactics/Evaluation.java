package UNOset.player.tactics;
import UNOset.server.RoundData;

public class Evaluation 
{
	private int player; //1 2 3 4
	private int enemy; // 0 1 2 3
	
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
		switch(select)
		{
			case 1:
				ans = point(a,b);
				break;
			case 2:
				ans = rank(a,b);
				break;
			case 3:
				ans = differX(a,b);
				break;
		}
		
		return ans;
	}
	
	//1:単純に自分の点数で決める
	private boolean point(RoundData a, RoundData b) //Bが良かったらtrue
	{
		int pointA = a.score[player-1];
		int pointB = b.score[player-1];
		if(pointA < pointB)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//2:自分の順位で決める
	private boolean rank(RoundData a, RoundData b) //Bが良かったらtrue
	{
		int rankA = 0;
		int rankB = 0;
		for(int i = 0; i < 4; i++)
		{
			if(a.score[player-1] < a.score[i])
			{
				rankA++;
			}
			if(b.score[player-1] < b.score[i])
			{
				rankB++;
			}
		}
		
		if(rankB < rankA)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//プレイヤーXの点数で決める
	
	//3:プレイヤーXとの点数差で決める
	private boolean differX(RoundData a, RoundData b)
	{
		int diffA = a.score[player-1] - a.score[enemy];
		int diffB = a.score[player-1] - a.score[enemy];
		
		if(diffA < diffB)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
