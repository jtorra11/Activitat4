package Covid;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public abstract class Mobil {
	
	// ** VARIABLES **
	int x,y,amplada,al�ada; double v; String nomImatge;BufferedImage imatge = null;Finestra f;	//variables generals
	int vides=1,ampladaExplosio=300,al�adaExplosio=300,num,ampladaBala=12,al�adaBala=10,vBala=30,vBalaExplosiva=40;	//variables espec�fiques
	
	// ** CONSTRUCTOR **
	Mobil(int x,int y,int amplada,int al�ada,int v,Finestra f,String nomImatge){
		this.x=x;this.y=y;this.amplada=amplada;this.al�ada=al�ada;this.v=v;this.f=f;this.nomImatge=nomImatge;
		carregarImatge("C:\\Java\\PA\\recursos\\"+nomImatge);
	}
	
	// ** FUNCIONS **
	//  MOURE
	void moure(KeyEvent instruccio, int compt) {} 
	
	void moure() {
		x+=v;
	}
	
	// DISPARAR 
	void disparar(ArrayList<Mobil> bales, boolean balesExplosives) {};
	void disparar(KeyEvent instruccio,ArrayList<Mobil> bales) {}; 
	
	// EXPLOTAR
	void explotar(ArrayList<Mobil> explosions) {
		explosions.add(new Explosio(x-ampladaExplosio/2,y+al�ada/2-al�adaExplosio/2,ampladaExplosio,al�adaExplosio,0,f));
	}
	
	// XOCAR 
	boolean xocar(Mobil mobil) {
		if(x-mobil.amplada<=mobil.x && mobil.x<=x+amplada && y-mobil.al�ada<=mobil.y && mobil.y<=y+al�ada) return true;
		return false;
	}
	
	// CARREGAR IMATGE 
	void carregarImatge(String path) {
		try { if(f.JAR) {
		    	URL url=getClass().getResource("/recursos/"+nomImatge);
		    	imatge = ImageIO.read(url);
		    }	
		    else imatge = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
	}
	
	// PINTAR 
	void pintar(Graphics g) {
		g.drawImage(imatge,x,y,amplada,al�ada,null);
	}
}
