package Covid;

public class Enemic4 extends Mobil{
	
	// ** VARIABLES **
	int ampladaBala=12,alçadaBala=10,vBala=-6;
	double v2;
	
	// ** CONSTRUCTOR **
	Enemic4(int x, int y,int amplada,int alçada, int v,int signeV2,Finestra f) {
		super(x,y,amplada,alçada,v,f,"enemic4.png");
		v2=signeV2;
	}
	
	// ** FUNCIONS **	
	//  MOURE
	void moure() {
		if(y+v2+amplada>f.ALÇADA) v2=-1; 
		if(y+v2<30) v2=1; 
		x+=v;
		y+=v2;
	}
}
	
