package Covid;

import java.util.ArrayList;


public class Enemic2 extends Mobil{
	// ** VARIABLES **
	int ampladaBala=12,al�adaBala=10,vBala=-10;
	
	// ** CONSTRUCTOR **
	Enemic2(int x, int y,int amplada,int al�ada, int v,Finestra f) {
		super(x,y,amplada,al�ada,v,f,"enemic2.png");
	}
	
	// ** FUNCIONS **
	// DISPARAR
	void disparar( ArrayList<Mobil> bales,boolean balesExplosives) {
		bales.add(new Bala(x-ampladaBala,y+al�ada/2-al�adaBala/2,ampladaBala,al�adaBala,vBala,2,f));
	}
	
}