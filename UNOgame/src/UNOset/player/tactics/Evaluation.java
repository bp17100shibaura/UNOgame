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
	
	//1:�P���Ɏ����̓_���Ō��߂�
	private boolean point(RoundData a, RoundData b) //B���ǂ�������true
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
	
	//2:�����̏��ʂŌ��߂�
	private boolean rank(RoundData a, RoundData b) //B���ǂ�������true
	{
		int rankA = a.winner;
		int rankB = b.winner;
		
		if(rankB < rankA)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//3:�v���C���[X�̓_��������
	private boolean downX(RoundData a, RoundData b)
	{
		int pointA = a.score[enemy-1];
		int pointB = b.score[enemy-1];
		
		if(pointB < pointA)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//4:�v���C���[X�̓_���グ��
	private boolean upX(RoundData a, RoundData b)
	{
		int pointA = a.score[enemy-1];
		int pointB = b.score[enemy-1];
		
		if(pointB > pointA)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//5�F�����@-�@�v���C���[X�@�̓_���ő�ɂ���
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
