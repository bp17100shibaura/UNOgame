package UNOset.player.tactics;

import UNOset.utils.*;
import java.util.Random;

public class HandPredict 
{
	int turn;
	
	float val[] = new float[5];
	
	int count[] = new int[5];
	
	int line = 1;
	
	Random rand = new Random();
	
	public HandPredict(int turn,int line)
	{
		this.turn = turn;
		this.line = line;
		this.val[0] = 1;
		this.val[1] = 1;
		this.val[2] = 1;
		this.val[3] = 1;
		this.val[4] = 1;
		this.count[0] = 0;
		this.count[1] = 0;
		this.count[2] = 0;
		this.count[3] = 0;
		this.count[4] = 0;
	}
	
	/*パスした時のトップから所持率0とカウントのリセット*/
	public void passCard(String s)
	{
		if(s.equals("r"))
		{
			this.val[0] = 0;
			for(int i = 0; i < 5;i++)
			{
				this.count[i]++;
			}
			this.count[0] = 0;
		}else if(s.equals("b"))
		{
			this.val[1] = 0;
			for(int i = 0; i < 5;i++)
			{
				this.count[i]++;
			}
			this.count[1] = 0;
		}else if(s.equals("g"))
		{
			this.val[2] = 0;
			for(int i = 0; i < 5;i++)
			{
				this.count[i]++;
			}
			this.count[2] = 0;
		}else if(s.equals("y"))
		{
			this.val[3] = 0;
			for(int i = 0; i < 5;i++)
			{
				this.count[i]++;
			}
			this.count[3] = 0;
		}else
		{
			this.val[4] = 0;
			for(int i = 0; i < 5;i++)
			{
				this.count[i]++;
			}
			this.count[4] = 0;
		}
		
		this.valChange();
	}
	
	/*ドロー毎のカウント*/
	public void drawcount()
	{
		for(int i = 0;i < 5;i++)
		{
			this.count[i] ++;
		}
	}
	
	/*カウントから所持率を調整(仮)*/
	void valChange()
	{
		for(int i = 0;i < 5;i++)
		{
			if(this.count[i] >= line)
			{
				this.count[i] = 0;
				this.val[i] = 1;
			}else if(this.count[i] != 0)
			{
				this.val[i] = this.count[i] / line;
			}
		}
	}
	
	/*予測の部分　数値は仮*/
	public boolean cardCheck(Card card)
	{
		this.valChange();
		String col = card.getCardColor();
		float t = (rand.nextInt(100) + 1) / 100;
		
		if(col.equals("r"))
		{
			if(val[0] >= t)
			{
				return true;
			}
			else
			{
				return false;
			}
		}else if(col.equals("b"))
		{
			if(val[1] >= t)
			{
				return true;
			}
			else
			{
				return false;
			}
		}else if(col.equals("g"))
		{
			if(val[2] >= t)
			{
				return true;
			}
			else
			{
				return false;
			}
		}else if(col.equals("y"))
		{
			if(val[3] >= t)
			{
				return true;
			}
			else
			{
				return false;
			}
		}else
		{
			if(val[4] >= t)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public void reset()
	{
		this.val[0] = 1;
		this.val[1] = 1;
		this.val[2] = 1;
		this.val[3] = 1;
		this.val[4] = 1;
		this.count[0] = 0;
		this.count[1] = 0;
		this.count[2] = 0;
		this.count[3] = 0;
		this.count[4] = 0;
	}
	
}
