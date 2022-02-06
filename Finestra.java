package Covid;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Finestra extends Frame implements KeyListener,WindowListener {
	
	// ** VARIABLES **
	boolean JAR=false;	//activar JAR=true per a exportar el projecte com a .jar
	
	Joc j;
	Image im;
	Graphics g;
	final int AMPLADA=1350, ALÇADA=550,	ALÇADA_EXTRA=140,DX=60;

	public static void main(String[] args) {
		new Finestra();
	}
	
	// ** CONSTRUCTOR **
	Finestra(){
		super("COVID INVADERS");
		setSize(AMPLADA,ALÇADA+ALÇADA_EXTRA);
		setVisible(true); 
		setExtendedState(MAXIMIZED_BOTH); 
		addKeyListener(this);
		addWindowListener(this);
		im=this.createImage(AMPLADA,ALÇADA+ALÇADA_EXTRA);
		g=im.getGraphics();
		j=new Joc(this);
		j.run();
	} 
	
	// ** FUNCIONS ** 
	public void update(Graphics g) { //millora la sincronia del pintat
		paint(g);
	}
	public void paint(Graphics g) {
		g.drawImage(im,0,0,null);
	}

	//KeyListener
	public void keyPressed(KeyEvent e) {
		j.instruccio1=e;
	}
	
	public void keyReleased(KeyEvent e) {
		j.instruccio2=e;
		j.instruccio1=null;
	}

	public void keyTyped(KeyEvent arg0) {}
	
	//WindowListener:
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	
	public void windowOpened(WindowEvent e) {}

	public void windowClosed(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}
}
