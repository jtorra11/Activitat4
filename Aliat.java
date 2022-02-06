package Covid;

import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Aliat extends Mobil{
	
	// ** CONSTRUCTOR **
	Aliat(int x,int y,Finestra f) { 
		super(x,y,40,40,0,f,"nau.png");
	}
	
	// ** FUNCIONS **
	//	MOURE
	void moure(KeyEvent instruccio, int compt) {
		if(instruccio!=null) {
			if(compt%2==0 && instruccio.getKeyCode()==KeyEvent.VK_DOWN && y+alçada+f.DX<f.ALÇADA) y+=f.DX;
			if(compt%2==0 && instruccio.getKeyCode()==KeyEvent.VK_UP && (y-f.DX>0)) y-=f.DX;	
		}		
	}
	void moure() {
		if(y==50||y==(50+6*f.DX)) y+=f.DX; 
		else y-=f.DX;
	}
	
	//	DISPARAR
	void disparar(KeyEvent instruccio, ArrayList<Mobil> bales) { 
		if(instruccio!=null && instruccio.getKeyCode()==KeyEvent.VK_SPACE) 
			bales.add(new Bala(x+amplada,y+alçada/2-alçadaBala/2,ampladaBala,alçadaBala,vBala,1,f));
		}

	void disparar(ArrayList<Mobil> bales, boolean balesExplosives) {
		if(balesExplosives) bales.add(new Bala(x+amplada,y+alçada/2-alçadaBala/2,ampladaBala+2,alçadaBala+2,vBalaExplosiva,3,f));
		else bales.add(new Bala(x+amplada,y+alçada/2-alçadaBala/2,ampladaBala,alçadaBala,vBala,1,f));
	}
}
