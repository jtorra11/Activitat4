package Covid;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Enemic3 extends Mobil{
	
	// ** VARIABLES **
	ArrayList <Image> imatges=new ArrayList<Image>();
	
	// ** CONSTRUCTOR **
	Enemic3(int x, int y,int amplada,int alçada, int v,Finestra f) {
		super(x,y,amplada,alçada,v,f,"");
		vides=4;		
		for(int i=1;i<5;i++)
			carregarImatge("enemic3_"+Integer.toString(i)+".png",imatges);
	}

	// ** FUNCIONS **
	// CARREGAR IMATGE  
	void carregarImatge(String nom,ArrayList <Image> imatges) {
		Image imatge=null;
		try {
			if(f.JAR) {
				URL url=getClass().getResource("/recursos/"+nom);
				imatge = ImageIO.read(url);
			}
			else imatge = ImageIO.read(new File("C:\\Java\\PA\\recursos\\"+nom));
		} catch (IOException e) {
		}
		imatges.add(imatge);
	}
	
	//	PINTAR 
	void pintar(Graphics g) {
		g.drawImage(imatges.get(vides-1),x,y,amplada,alçada,null);
	}
}
