package Covid;

import java.util.ArrayList;


public class Enemic2 extends Mobil{
	// ** VARIABLES **
	int ampladaBala=12,alçadaBala=10,vBala=-10;
	
	// ** CONSTRUCTOR **
	Enemic2(int x, int y,int amplada,int alçada, int v,Finestra f) {
		super(x,y,amplada,alçada,v,f,"enemic2.png");
	}
	
	// ** FUNCIONS **
	// DISPARAR
	void disparar( ArrayList<Mobil> bales,boolean balesExplosives) {
		bales.add(new Bala(x-ampladaBala,y+alçada/2-alçadaBala/2,ampladaBala,alçadaBala,vBala,2,f));
	}
	
}