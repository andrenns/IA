import java.io.File;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import lejos.hardware.Sound;

public class Main {
	
	private static int x = 0;//Dá nos a coluna que o robot se encontra
	private static int y = 0;//Dá nos a linha que o robot se encontra
	private static int xPro = 0;
	private static int yPro = 0;
	private static int dirD = 0;
	private static int dir=1; //dá nos a direção que o robot se encontra 
	private static SensorAngulo ang = new SensorAngulo();
	private static SensorUltraSom ultSom = new SensorUltraSom();
	private static SensorCor cor = new SensorCor();
	private static TouchSensor touch = new TouchSensor();
	private static int[][] percorrido = new int[6][6];
	private static int[][] tabuleiro = new int[6][6];
	private static RegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);
	private static RegulatedMotor motorC = new EV3LargeRegulatedMotor(MotorPort.C);
	private static RegulatedMotor motorD =  new EV3LargeRegulatedMotor(MotorPort.D);
	private static RegulatedMotor motorA =  new EV3LargeRegulatedMotor(MotorPort.A);
	private static boolean ver = false;
	private static final int CAMINHOIN = 2;
	private static final int CAMINHOINT = 3;
	private static final int CAMINHOFIN = 4;
	private static int a = 0;
	private static boolean aux = true;
	private static int [] cordZomb;
	private static int [] cordPeca;
	private static int DirZombieDet;
	private static int flagCam=0;
	private static int posZx=0;
	private static int posZy=0;
	private static int enctZ=0;
	private static int posPx=0;
	private static int posPy=0;
	private static int enctP=0;
	private static int xC=0;
	private static int yC=0;
	private static int conta=0;
	private static boolean mudaVarCam=false;
	private static boolean temZomb=false;
	private static int pecaAp = 1;
	private static boolean flag2=true;
	private static boolean flag3=true;
	private static int flagAnda=0;
	private static boolean flagApeca=false;
	private static boolean inverso= false;
	private static boolean sprintM=false;
	private static boolean flagLimitex=false;
	private static boolean flagLimitey=false;
	private static boolean flagAtaca=false;
	private static boolean flagAtacaT=false;
	
	
	
	public static void main(String[] args) 
	{	
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
			
		for(int i = 0; i < 5;i++)
		{
			preenche(1,i,CAMINHOIN);
		}
		preenche(2,4,CAMINHOIN);
		
		for(int i = 1; i < 5;i++)
		{
			preenche(3,i,CAMINHOINT);
		}
		for(int i = 1; i < 6;i++)
		{
			preenche(4,i,CAMINHOFIN);
		}
		
		preenche(5,5,CAMINHOFIN);
		

	
		
	/*	Delay.msDelay(10);
		reconhecimento();
	
		Delay.msDelay(10);*/
		
		while(aux)
		{
			
			turno();
			for(int i=0;i<6;i++)
			{
				for(int j=0;j<6;j++)
				{
					LCD.drawInt(percorrido[i][j], i, j);
				}
			}
			while(touch.isTouched() == false);
			LCD.clear();
		
			if(x == 5 && y ==5 && pecaAp>2)
			{
				aux = false;
			}
			else if (x == 5 && y ==5 && pecaAp<=2)
			{
				
				if(pecaAp==2)
				{
					baixo();
				}
				inverso=true;
				for(int i = 0; i < 5;i++)
				{
					preenche(1,i,CAMINHOIN);
				}
				preenche(2,4,CAMINHOIN);
				
				for(int i = 1; i < 5;i++)
				{
					preenche(3,i,CAMINHOINT);
				}
				for(int i = 1; i < 6;i++)
				{
					preenche(4,i,CAMINHOFIN);
				}
				
				preenche(5,5,CAMINHOFIN);
				
				for(int i=0; i<=5;i++) 
				{
					for(int j = 0; j<=5;j++)
					{
						tabuleiro[i][j]=0;
					}
				}
			}
			
			if(x==0 && y==0 && inverso)
			{
				inverso = false;
				for(int i = 0; i < 5;i++)
				{
					preenche(1,i,CAMINHOIN);
				}
				preenche(2,4,CAMINHOIN);
				
				for(int i = 1; i < 5;i++)
				{
					preenche(3,i,CAMINHOINT);
				}
				for(int i = 1; i < 6;i++)
				{
					preenche(4,i,CAMINHOFIN);
				}
				
				preenche(5,5,CAMINHOFIN);
				
				for(int i=0; i<=5;i++) 
				{
					for(int j = 0; j<=5;j++)
					{
						tabuleiro[i][j]=0;
					}
				}
				
			}
			
			
		}
		
	}
	
	//Preenche a matriz com o valor c, o valor do caminho 
	public static void preenche(int x, int y,int c) {
		percorrido[x][y]=c;
	}
	
	
	
	public static void dispara() 
	{
		
		
		motorD.setSpeed(1000);// 2 RPM
		motorD.forward();
		Delay.msDelay(7000);
		motorD.stop();

		
	}
	
	//quando é chamada tem como objectivo colocar o robo a se mover no caminho que foi definido que tem como objetivo chegar a moto
	public static void andaMota()
	{
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		Keys keys = ev3.getKeys();
	
		mudaVarCam=true;
		
		if(xPro>x)
		{
			dirD = 2;
			ajustaDir(dir,dirD);
			Delay.msDelay(10);
			andaFrente();
			Delay.msDelay(10);
			//reconhecimento();
			
			Delay.msDelay(10);
			
		}
		if(xPro<x)
		{
			dirD = 4;
			ajustaDir(dir,dirD);
			Delay.msDelay(10);
			andaFrente();
			Delay.msDelay(10);
			//reconhecimento();
			Delay.msDelay(10);
			
		
		}
		if(yPro>y)
		{
			dirD = 1;
			ajustaDir(dir,dirD);
			Delay.msDelay(10);
			andaFrente();
			Delay.msDelay(10);
			//reconhecimento();
			Delay.msDelay(10);
			
			
		}
		if(yPro<y)
		{
			dirD = 3;
			ajustaDir(dir,dirD);
			Delay.msDelay(10);
			andaFrente();
			Delay.msDelay(10);
			//reconhecimento();
			Delay.msDelay(10);
	
		}
			percorrido[x][y]=1;	
		
	}
	
	// Faz o robo se mover para a casa anterior consoante a sua direção
	public static void andaTras()
	{
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		motorB.synchronizeWith(new RegulatedMotor[] { motorC });
		
		motorB.startSynchronization();
		motorB.forward();
		motorC.forward();
		motorB.endSynchronization();
	
		Delay.msDelay(3800);
		
		motorB.startSynchronization();
		motorB.stop();
		motorC.stop();
		motorB.endSynchronization();
		
		switch(dir)
		{
		case 1: y--;
				break;
		case 2: x--;
				break;
		case 3: y++;
		break;
		case 4: x++;
		break;
				
		}	
		
	}
	
	// Faz o robo se mover para a casa em frente consoante a sua direção
	public static void andaFrente()
	{
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		motorB.synchronizeWith(new RegulatedMotor[] { motorC });
		
		/*motorB.setSpeed(720);
		motorC.setSpeed(720);*/
		
		motorB.startSynchronization();
		motorB.backward();
		motorC.backward();
		motorB.endSynchronization();
	
		Delay.msDelay(3800);
		
		motorB.startSynchronization();
		motorB.stop();
		motorC.stop();
		motorB.endSynchronization();
		
		if(mudaVarCam==true)
		{
			percorrido[x][y]=1;
			mudaVarCam=false;
		}
		
		switch(dir)
		{
		case 1: y++;
				break;
		case 2: x++;
				break;
		case 3: y--;
		break;
		case 4: x--;
		break;
				
		}	 
		
		
	}

	// Faz o robo rodar 90 graus consoante o bollean e ajusta o valor da dir
	public static void roda9(boolean a)
	{
		motorB.synchronizeWith(new RegulatedMotor[] { motorC });
		// true-direita false-esquerda
	
		if(a == true)
		{
			if(dir==1) 
			{
				dir=4;
			}
			else if(dir==2) 
			{
				dir=1;
			}
			else if(dir==3) 
			{
				dir=2;
			}
			else if(dir==4) 
			{
				dir=3;
			}
			motorB.forward();
            motorC.backward();
 
            Delay.msDelay(1550);
            motorB.startSynchronization();
            motorB.stop();
            motorC.stop();
		motorB.endSynchronization();
		}
		if(a == false)
		{
			if(dir==1) 
			{
				dir=2;
			}
			else if(dir==2) 
			{
				dir=3;
			}
			else if(dir==3) 
			{
				dir=4;
			}
			else if(dir==4) 
			{
				dir=1;
			}
			
			motorB.backward();
            motorC.forward();
 
            Delay.msDelay(1550);

            motorB.startSynchronization();
            motorB.stop();
            motorC.stop();
		motorB.endSynchronization();
		}
		
	}
	
	// verifica se tem zombies nas das diagonais superiores e inferiores
	public static void verificaT() {
		int dirC = dir;
		LCD.clear();
		
		if(x!=0)
		{
		ajustaDir(dir, 4);
		Delay.msDelay(120);
		verfZombie();
		}
		
		if(x!=5)
		{
		ajustaDir(dir, 2);
		Delay.msDelay(120);
		verfZombie();
		Delay.msDelay(120);
	
		}
		
		ajustaDir(dir, dirC);

	} 
	
	// verifica se tem zombies nas das diagonais esquerda e direita
	public static void verificaT1() {
		int dirC = dir;
		LCD.clear();
		
		if(y!=0)
		{
		ajustaDir(dir, 3);
		Delay.msDelay(120);
		verfZombie();
		}
		
		if(y!=5)
		{
		ajustaDir(dir, 1);
		Delay.msDelay(120);
		verfZombie();
		Delay.msDelay(120);
	
		}
		
		ajustaDir(dir, dirC);

	} 
	
	// Tem como objetivo ajustar a direção do robo para a direção desejada
	private static void ajustaDir(int dirAct, int dirDesej)
	{
		int a = 0;
		if(dirAct!=dirDesej)
		{
			a =dirAct - dirDesej;
			if(a==3 || a==-1)
			{
				while(dirAct!=dirDesej)
				{
					roda9(false);
					Delay.msDelay(10);
					if(dirAct == 4 && dirDesej == 1)
					{
						dirAct=dirDesej;
					}
					else
					{
						dirAct++;
					}
				}
			}
			else if(a==-3 || a == 1)
			{
				while(dirAct!=dirDesej)
				{
					roda9(true);
					Delay.msDelay(10);
					if(dirAct == 1 && dirDesej == 4)
					{
						dirAct=dirDesej;
					}
					else
					{
						dirAct--;
					}
				}
			}
			else 
			{
				if((dirAct==1 && dirDesej ==3)||(dirAct==2 && dirDesej==4))
				{
					while(dirAct!=dirDesej)
					{
						
						roda9(false);
						Delay.msDelay(10);
						dirAct++;
					}	
				}
				else
				{
					while(dirAct!=dirDesej)
					{
						roda9(true);
						Delay.msDelay(10);
						dirAct--;
					}
			}
			}
		}
	}
	
	
	// Função responsavel por fazer reconhecimento para detetar zombies, peças ou bala
	public static void reconhecimento()
	{
		
		/*if(pecaAp>2)
		{
			
			for(int i=0; i<=5;i++) 
			{
				for(int j = 0; j<=5;j++)
				{
					percorrido[i][j]=0;
				}
			}
			
			for(int i = x; i <= 5;i++)
			{
				preenche(i,y,CAMINHOIN);
			}
			
			for(int i = y; i <= 5;i++)
			{
				preenche(5,i,CAMINHOIN);
			}
		
		}
		
	for(int i =0; i<=5;i++)
		{
			for (int j=0; j<=5;j++)
			{
				if(tabuleiro[i][j]==5)
				{
					tabuleiro[i][j]=0;
				}	
			}
			
		}*/
		
		
		if(y-1>=0)
		{
			switch(dir)
			{
			case 1: 
			ajustaDir(1,3);
			Delay.msDelay(120);
			verfZombie();
			if (!temZomb)
			{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				//atualizaMatriz();
				verificaT();
				andaTras();
			}
			Delay.msDelay(10);
			
			
					break;
			case 2: 
				ajustaDir(2,3);
				/*roda9(false);*/

				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
					andaFrente();
					Delay.msDelay(120);
					verfZombie();
					verfPeca();
					//atualizaMatriz();
					verificaT();
					andaTras();
				}
				Delay.msDelay(10);
			
					break;
			case 3: 
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
					andaFrente();
					Delay.msDelay(120);
					verfZombie();
					verfPeca();
					//atualizaMatriz();
					verificaT();
					andaTras();
				}
				Delay.msDelay(10);
			break;
			case 4: 
				/*roda9(true);
				Delay.msDelay(10);*/
				ajustaDir(4,3);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				//atualizaMatriz();
				verificaT();
				andaTras();
				}
				Delay.msDelay(10);
			break;
					
			}	 
			
		}
		if(x-1>=0)
		{
			switch(dir)
			{
			case 1: 
			/*roda9(true);
			Delay.msDelay(10);*/
				ajustaDir(1,4);
				Delay.msDelay(120);
			verfZombie();
			if (!temZomb)
			{
			andaFrente();
			Delay.msDelay(120);
			verfZombie();
			verfPeca();
			verificaT1();
			
			//atualizaMatriz();
			
			andaTras();
			}
			Delay.msDelay(10);
					break;
			case 2: 
				/*roda9(false);
				Delay.msDelay(10);
				roda9(false);
				Delay.msDelay(10);*/
				ajustaDir(2,4);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				verificaT1();
				//atualizaMatriz();
			
			andaTras();
				}
				Delay.msDelay(10);
					break;
			case 3: 
				/*roda9(false);
				Delay.msDelay(10);*/
				ajustaDir(3,4);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				verificaT1();
				//atualizaMatriz();
				
				andaTras();
				}
				Delay.msDelay(10);
			break;
			case 4: 
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				//atualizaMatriz();
				verificaT1();
				andaTras();
				}
				Delay.msDelay(10);
			break;
					
			}
			
		}
		if(y+1<=5)
		{
			switch(dir)
			{
			case 1: 
			Delay.msDelay(120);
			verfZombie();
			if (!temZomb)
			{
			andaFrente();
			
			Delay.msDelay(120);
			verfZombie();
			verfPeca();
			//atualizaMatriz();
			verificaT();
			andaTras();
			}
			Delay.msDelay(10);
					break;
			case 2: 
				/*roda9(true);
				Delay.msDelay(10);*/
				ajustaDir(2,1);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
			andaFrente();
			Delay.msDelay(120);
			verfZombie();
			verfPeca();

			//atualizaMatriz();
			verificaT();
			andaTras();
				}
				Delay.msDelay(10);
					break;
			case 3: 
				/*roda9(true);
				Delay.msDelay(10);
				roda9(true);
				Delay.msDelay(10);*/
				ajustaDir(3,1);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();

				//atualizaMatriz();
				verificaT();
				andaTras();
				}
				Delay.msDelay(10);
			break;
			case 4: 
				/*roda9(false);
				Delay.msDelay(10);*/
				ajustaDir(4,1);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				//atualizaMatriz();
				verificaT();
				andaTras();
				}
				Delay.msDelay(10);
			break;
					
			}
			
		}

		if(x+1<=5)
		{
			switch(dir)
			{
			case 1: 
			/*roda9(false);
			Delay.msDelay(10);*/
				ajustaDir(1,2);
				Delay.msDelay(120);
			verfZombie();
			if (!temZomb)
			{
			andaFrente();
			//atualizaMatriz();
			Delay.msDelay(120);
			verfZombie();
			verfPeca();
			verificaT1();
			andaTras();
			}
			Delay.msDelay(10);
					break;
			case 2: 
				
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
			andaFrente();
			//atualizaMatriz();
			Delay.msDelay(120);
			verfZombie();
			verfPeca();
			verificaT1();
			andaTras();
				}
				Delay.msDelay(10);
					break;
			case 3: 
				/*roda9(true);
				Delay.msDelay(10);*/
				ajustaDir(3,2);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				//atualizaMatriz();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				verificaT1();
				andaTras();
				}
				Delay.msDelay(10);
			break;
			case 4: 
				/*roda9(true);
				Delay.msDelay(10);
				roda9(true);
				Delay.msDelay(10);*/
				ajustaDir(4,2);
				Delay.msDelay(120);
				verfZombie();
				if (!temZomb)
				{
				andaFrente();
				//atualizaMatriz();
				Delay.msDelay(120);
				verfZombie();
				verfPeca();
				verificaT1();
				andaTras();
				}
				Delay.msDelay(10);
			break;
					
			}	 
			
		}
		
		for(int i =0; i<=5;i++)
		{
			for (int j=0; j<=5;j++)
			{
				if(percorrido[i][j]==2)
				{
					if(x<i)
					{
						ajustaDir(dir,4);
					}
					if(y>j)
					{
						ajustaDir(dir,3);
					}
					if(x>i)
					{
						ajustaDir(dir,2);
					}
					
					if(y<j)
					{
						ajustaDir(dir,1);
					}
					
					i=6;
					j=6;
				}
				else if(percorrido[i][j]==3)
				{
					if(x<i)
					{
						ajustaDir(dir,3);
					}
					if(y<j)
					{
						ajustaDir(dir,4);
					}
					if(x>i)
					{
						ajustaDir(dir,1);
					}
					
					if(x>j)
					{
						ajustaDir(dir,2);
					}
					i=6;
					j=6;
				}
				else if(percorrido[i][j]==4)
				{
					if(x<i)
					{
						ajustaDir(dir,4);
					}
					if(y<j)
					{
						ajustaDir(dir,3);
					}
					if(x>i)
					{
						ajustaDir(dir,2);
					}
					
					if(x>j)
					{
						ajustaDir(dir,1);
					}
					i=6;
					j=6;
				}
			}
		}
		
		
		
		//Percorrer a matriz e guardar localização do zombie;
		for(int i =0; i<=5;i++)
		{
			for (int j=0; j<=5;j++)
			{
				if(tabuleiro[i][j]==5)
				{
					switch(dir)
					{
					case 1:
						if((x+1==i&&y+1==j)||(x-1==i&&y+1==j)||(y+1==j&&x==i)||(y+2==j&&x==i))
						{
							posZx=i;
							posZy=j;
							//tabuleiro[i][j]=1;
							enctZ=1;
							
						}
						break;
					case 2:
						if((x+1==i&&y==j)||(x+2==i&&y==j)||(x-1==i&&y+1==j)||(x+1==i&&y+1==j))
						{
							posZx=i;
							posZy=j;
							//tabuleiro[i][j]=1;
							enctZ=1;
							
						}
						break;
					case 3:
						if((x==i&&y-1==j)||(x==i&&y-2==j)||(x-1==i&&y-1==j)||(x+1==i&&y-1==j))
						{
							posZx=i;
							posZy=j;
							//tabuleiro[i][j]=1;
							enctZ=1;
						}
						break;
					case 4:
						if((x-1==i&&y==j)||(x-2==i&&y==j)||(y-1==j&&x-1==i)||(y+1==j&&x-1==i))
						{
							posZx=i;
							posZy=j;
							//tabuleiro[i][j]=1;
							enctZ=1;
						
							
						}
						break;
					}
				}
				if(tabuleiro[i][j]==9)
				{
					posPx=i;
					posPy=j;
					tabuleiro[i][j]=1;
					enctP=1;
				}
			}
		}
	
	}
	
	
	private static void atualizaMatriz()
	{
		if(percorrido[x][y] == 2)
		{
			
				xPro = x;
				yPro = y;
				
				//percorrido[x][y]=1;
				
			flagCam = 0;
		}
		else if(percorrido[x][y] == 3 && flagCam == 1)
			{
				xPro = x;
				yPro = y;
				//percorrido[x][y]=1;
			}

		
		
	}
	
	//Verifica Zombie
	public static void verfZombie()
	{
		Delay.msDelay(10);
		temZomb=false;

		
		if( (ultSom.getRange()<=0.34) )
		{
			
			if(y!=0)
			{
				
				Sound.beep();
				
				//se encontrar marca na matriz a posição
				switch(dir)
				{
				case 1:
					if(y!=5)
					{
					tabuleiro[x][y+1]=5;
					//percorrido[x][y+1]=1;
					DirZombieDet= dir;
					temZomb=true;
					}
					break;
				case 2:
					if(x!=5)
					{
					tabuleiro[x+1][y]=5;
					//percorrido[x+1][y]=1;
					DirZombieDet= dir;
					temZomb=true;
					}
					break;
				case 3:
					tabuleiro[x][y-1]=5;
					//percorrido[x][y-1]=1;
					DirZombieDet= dir;
					temZomb=true;
					break;
				case 4:
					if(x!=0)
					{
					tabuleiro[x-1][y]=5;
					//percorrido[x-1][y]=1;
					DirZombieDet= dir;
					temZomb=true;
					}
					break;
				}
			} else if(y==0)
				{
					
					Sound.beep();
					switch(dir)
					{
					case 1:
						tabuleiro[x][y+1]=5;
						//percorrido[x][y+1]=1;
						DirZombieDet= dir;
						temZomb=true;
						break;
					case 2:
						if(x!=5)
						{
						tabuleiro[x+1][y]=5;
						//percorrido[x+1][y]=1;
						DirZombieDet= dir;
						temZomb=true;
						}
						break;
					case 3:
						break;
					case 4:
						if(x!=0)
						{
						tabuleiro[x-1][y]=5;
						//percorrido[x-1][y]=1;
						DirZombieDet= dir;
						temZomb=true;
						}
						break;
					}
			
			}
		}
		
	}

	public static void atacarZombie()
	{
		// se ver estiver a true significa se apanhou a bola
		if(ver == true)
		{
			dispara();
			andaTras();
			ver=false;
		}
		
		else if(ver==false && (pecaAp == 2||pecaAp==3))
		{
			//caso este tenha a peça faz o ataque inverso
			baixo();
			cima();
			Delay.msDelay(10);
			andaTras();
		}
		else if(ver == false)
		{
			//caso este nao tenha peça faz a o ataque normal
			cima();
			baixo();
			Delay.msDelay(10);
			andaTras();
		}
	}
	
	
	//Define qual a ação que o robo irá fazer após efectuado o reconhecimento
	public static void turno()
	{
		flag2=true;
		flag3=true;
		flagAnda = 1;
		flagCam=1;
		reconhecimento();
		if(!inverso)
		{
		for(int i=0; i<=5;i++)
		{
			for(int j=0; j<=5; j++)
			{
				if(percorrido[i][j]==2)
				{
					xPro=i;
					yPro=j;
					i=6;
					j=6;
					flag2=false;
					flag3=false;
					
				}
			}
		}
		if(flag2) {
		for(int i=0; i<=5;i++)
		{
			for(int j=5; j>=0; j--)
			{
				if(percorrido[i][j]==3)
				{
					xPro=i;
					yPro=j;
					i=6;
					j=-1;
					flag3=false;
				}
			}
		}
		}
		if(flag3)
		{
		for(int i=0; i<=5;i++)
		{
			for(int j=0; j<=5; j++)
			{
				if(percorrido[i][j]==4)
				{
					xPro=i;
					yPro=j;
					i=6;
					j=6;
				}
			}
		}
		}
		}
		else
		{
			for(int i=0; i<=5;i++)
			{
				for(int j=5; j<=0; j--)
				{
					if(percorrido[i][j]==4)
					{
						xPro=i;
						yPro=j;
						i=6;
						j=6;
						flag2=false;
						flag3=false;
						
					}
				}
			}
			if(flag2) {
			for(int i=0; i<=5;i++)
			{
				for(int j=0; j<=5; j++)
				{
					if(percorrido[i][j]==3)
					{
						xPro=i;
						yPro=j;
						i=6;
						j=-1;
						flag3=false;
					}
				}
			}
			}
			if(flag3)
			{
			for(int i=0; i<=5;i++)
			{
				for(int j=5; j>=0; j--)
				{
					if(percorrido[i][j]==2)
					{
						xPro=i;
						yPro=j;
						i=6;
						j=6;
					}
				}
			}
			}
		}
		if(enctZ==1 || enctP==1)
		{
			a = acaoPosRec();
			if(a!=6)
			{
			
			/*LCD.clear();
			LCD.drawInt(a,1,1);
			while(touch.isTouched() == false);*/
			enctZ=0;
			enctP=0;
			if(a==2)
			{
				if(flagAtaca&&!flagAtacaT)
				{
					flagAnda=0;
					flagAtaca=false;
					andaFrente();
					atacarZombie();
				}
				else
				{
				andaFrente();
				atacarZombie();
				flagAnda=1;
				}
				a=0;
				xC=0;
				yC=0;
				
			}
			else if (a==1)
			{
				flagAnda=0;
			}
		}	
		}
		else if(a == 1)
		{
			while(y!=yC || x!=xC)
			{
				if(x<xC)
				{
					ajustaDir(dir,2);
					andaFrente();
				}
				if(x>xC)
				{
					ajustaDir(dir,4);
					andaFrente();
				}
				if(y<yC)
				{
					ajustaDir(dir,1);
					andaFrente();
				}
				if(y>yC)
				{
					ajustaDir(dir,3);
					andaFrente();
				}
		}
				a=0;
			 flagAnda = 0;
			 xC=0;
			 yC=0;
		} 
		
		 if (a!=6)
		 {
			if(flagAnda==1)
			{
				andaMota();
			}
		 }
		 
		 
		
	}
	
	
	// retorna um valor de 'a' para ser utilizado no turno para definir qual a ação a fazer 
	public static int acaoPosRec() 
	{
		flagAtaca=false;
		flagAtacaT=false;
		int a=0;
		int DirIni=dir;
		if(xC==0 && yC==0)
		{
			xC=x;
			yC=y;
		}
		
		conta=0;
		  
		
					switch(dir){
					case 1:
						if(x<5 && y<5) {
						if(tabuleiro[x+1][y+1]==5)
							{conta++;
							tabuleiro[x+1][y+1]=1;
							}}
						if(x>0 && y<5) {
						if(tabuleiro[x-1][y+1]==5)
							{conta++;
							tabuleiro[x-1][y+1]=1;}}
						if(y<4) {
						if(tabuleiro[x][y+2]==5)
							{conta++;
							tabuleiro[x][y+2]=1;}}
						if(y<5) {
						if(tabuleiro[x][y+1]==5)
							{
							conta++;
							flagAtaca=true;
							tabuleiro[x][y+1]=1;}}
						if(y>0) {
							if(tabuleiro[x][y-1]==5)
								{
								conta++;
								flagAtacaT=true;
								tabuleiro[x][y-1]=1;}}


						
					break;
					case 2:
						if(x<5 && y>0) {
						if(tabuleiro[x+1][y-1]==5)
							{conta++;
							tabuleiro[x+1][y-1]=1;}}
						if(x<5 && y<5) {
						if(tabuleiro[x+1][y+1]==5)
							{conta++;
							tabuleiro[x+1][y+1]=1;}}
						if(x<4) {
						if(tabuleiro[x+2][y]==5)
							{conta++;
							tabuleiro[x+2][y]=1;}}
						if(x<5) {
						if(tabuleiro[x+1][y]==5)
						{conta++;
						tabuleiro[x+1][y]=1;
						flagAtaca=true;}}
						if(x>0) {
							if(tabuleiro[x-1][y]==5)
							{conta++;
							tabuleiro[x-1][y]=1;
							flagAtacaT=true;}}
					
					break;
					case 3:
						if(x>0 && y>0) {
						if(tabuleiro[x-1][y-1]==5)
							{conta++;
							tabuleiro[x-1][y-1]=1;}}
						if(x<5 && y>0) {
						if(tabuleiro[x+1][y-1]==5)
							{conta++;
							tabuleiro[x+1][y-1]=1;}}
						if(y>1) {
						if(tabuleiro[x][y-2]==5)
							{conta++;
							tabuleiro[x][y-2]=1;}}
						if(y>0) {
						if(tabuleiro[x][y-1]==5)
						{conta++;
						tabuleiro[x][y-1]=1;
						flagAtaca=true;}}
						if(y<5) {
							if(tabuleiro[x][y+1]==5)
							{conta++;
							tabuleiro[x][y+1]=1;
							flagAtacaT=true;}}
					break;
					case 4:
						if(x>0 && y>0) {
						if(tabuleiro[x-1][y-1]==5)
							{conta++;
							tabuleiro[x-1][y-1]=1;}}
						if(x>0 && y<5) {
						if(tabuleiro[x-1][y+1]==5)
							{conta++;
							tabuleiro[x-1][y+1]=1;}}
						if(x>1) {
						if(tabuleiro[x-2][y]==5)
							{conta++;
							tabuleiro[x-2][y]=1;}}
						if(x>0) {
						if(tabuleiro[x-1][y]==5)
						{conta++;
						tabuleiro[x-1][y]=1;
						flagAtaca=true;}}
						if(x<5) {
							if(tabuleiro[x+1][y]==5)
							{conta++;
							tabuleiro[x+1][y]=1;
							flagAtacaT=true;}}
					break;
			}
				
		if(flagAtaca&&flagAtacaT)
		{
			a=2;
		}
		else if (conta>=2)
		{
			enctZ=0;
			a=6; // é seis pois no turno não existe nenhum if cujo o valor de a seja este, sendo assim este não se move. 
			if(flagAtaca)
			{
				a=2;
			}
		}
		else if(conta==1&&flagAtaca)
		{
			a=2;
		}
		else
		{
			flagAtaca=false;
			flagAtacaT=false;
		//anda até cordenada
		if(enctZ==1&&!((x==posZx && y==posZy-1)||(x==posZx && y==posZy+1)||(x==posZx+1 && y==posZy)||(x==posZx-1 && y==posZy))){
		while(y!=posZy || x !=posZx)
		{
			if (dir ==1 || dir ==3)
			{
				if( y < posZy)
				{
					ajustaDir(dir,1);
					andaFrente();
				}
				if(y > posZy)
				{
					ajustaDir(dir,3);
					andaFrente();
				}
				if(x < posZx)
				{
					ajustaDir(dir,2);
					andaFrente();
				}
				if( x > posZx)
				{
					ajustaDir(dir,4);
					andaFrente();
				}
			}
			else
			{
				if(x < posZx)
				{
					ajustaDir(dir,2);
					andaFrente();
				}
				if( x > posZx)
				{
					ajustaDir(dir,4);
					andaFrente();
				}	
				if( y < posZy)
				{
					ajustaDir(dir,1);
					andaFrente();
				}
				if(y > posZy)
				{
					ajustaDir(dir,3);
					andaFrente();
				}
				
			}
		}
		
		}
	
		//ajustaDir(dir,DirZombieDet);
		if(posZx==0 && posZy==0 && enctZ==0 && posPx==0 && posPy==0 && enctP==0) 
		{
			Sound.buzz();
			a= 0;
		}
		else if (enctZ==1)
		{
			if((x==posZx && y==posZy-1)||(x==posZx && y==posZy+1)||(x==posZx+1 && y==posZy)||(x==posZx-1 && y==posZy))
			{
				a=2;
				if(dir==1 || dir ==3)
				{
					if( y < posZy)
					{
						ajustaDir(dir,1);
						
					}
					if(y > posZy)
					{
						ajustaDir(dir,3);
					
					}
					if(x < posZx)
					{
						ajustaDir(dir,2);
					
					}
					if( x > posZx)
					{
						ajustaDir(dir,4);
					
					}
				}
				else
				{
					if(x < posZx)
					{
						ajustaDir(dir,2);
					
					}
					if( x > posZx)
					{
						ajustaDir(dir,4);
					
					}
					if( y < posZy)
					{
						ajustaDir(dir,1);
						
					}
					if(y > posZy)
					{
						ajustaDir(dir,3);
					
					}
				}
				andaFrente();
				atacarZombie();	
				
				
			}
			
			else
			{
				atacarZombie();
				a=1;
			}
		}
		
		
		if(enctZ==0 && enctP==1)
		{
			flagApeca=true;
			if(y!=0)
			{
			if(percorrido[x][y-1]==5)
			{
				ajustaDir(dir,3);
				andaFrente();
				atacarZombie();
				flagApeca=false;
			}
			}
			
			if(x!=5)
			{
			if(percorrido[x+1][y]==5)
			{
				ajustaDir(dir,2);
				andaFrente();
				atacarZombie();
				flagApeca=false;
			}
			}
			
			if(x!=0)
			{
			if (percorrido[x-1][y]==5)
			{
				ajustaDir(dir,4);
				andaFrente();
				atacarZombie();
				flagApeca=false;
			}
			}
			
			
			
			if(flagApeca)
			{	
				while(y!=posPy || x !=posPx)
				{
					if(y<posPy)
					{
						ajustaDir(dir,1);
						andaFrente();
						
					}
					if(y>posPy)
					{
						ajustaDir(dir,3);
						andaFrente();
					}
					if(x<posPx)
					{
						ajustaDir(dir,2);
						andaFrente();
					}
					if(x>posPx)
					{
						ajustaDir(dir,4);
						andaFrente();
					}
				
				}
				if(pecaAp == 2)
				{  
					
					andaTras();
					Delay.msDelay(10);
					baixo();
					
					andaFrente();
					Delay.msDelay(10);
				}
				cima();
				pecaAp++;
				posPy=0;
				posPx=0;
				flagAnda=0;
				a=1;
			
			}
		}
		}
		
		// ajustaDir(dir,DirIni);
	return a;
	}
	
	
	public static void verfPeca()
	{
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		Keys keys = ev3.getKeys();
		LCD.drawString("poe cor", 0, 0);

		LCD.clear();
		float[] sample = new float[cor.getColorSensor().sampleSize()];
		cor.getColorProvider().fetchSample(sample, 0);
		int colorID = (int)sample[0];
		
		switch(colorID)
		{
		case Color.BLUE:
			Sound.twoBeeps();
			Delay.msDelay(10);
			ver = true;
			//tabuleiro[x][y]=10;
			break;
		case Color.RED:
			
			Sound.beep();
			
			tabuleiro[x][y]=9;
			break;
		
		}
	
	}

	//Sobe a garra
	public static void cima()
	{
		motorA.rotateTo(500);
		Delay.msDelay(2000);
	
	}
	// Desce a garra
	public static void baixo()
	{
		motorA.rotateTo(-500);
		Delay.msDelay(2000);
	
	}

}
