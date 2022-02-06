package Covid;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Joc {
	
	// ** VARIABLES **
	int ESPERA_INICIAL=12*20, 
		DURADA_ONADA1=20*20, 
		DURADA_CALMA=5*20,
		INCREMENT_ONADA=20*20, 
		INFINIT=100000; 

	Graphics g;
	Finestra f;
	KeyEvent instruccio1, instruccio2;
	boolean musica=true,victoria=false;
	int partida=0,onadaMax=0,onada=0,compt,comptOnada,comptCalma,comptAux1,comptAux2,comptAux3,enemicsMorts,covidsPerInfeccio,bonus1,bonus2,bonus3; 
	ArrayList <Mobil> aliats,enemics1,enemics2,enemics3,enemics4,enemics5,bales,balesEnemigues,balesExplosives,explosions,bonus;
	Random r=new Random();
	Clip canço;
	
	// ** CONSTRUCTOR **
	Joc(Finestra f){
		this.f=f;
		this.g=f.g;
	}
	
	// ** FUNCIONS ** 
	//  RUN (funció principal)
	void run() { 
		importaFont(); 
		pantallaInicial();
		
		while(true) {
		inicialitzacio();
			while(aliats.get(0).vides>0 && covidsPerInfeccio>0) {			
				
				//accions que incorporen escoltes al teclat
				moviments();
				trets();
				efectesBonus();
				escoltaPausa();
				actualitzaMusica();
				saltaOnada();		//shortcut per a desenvolupadors (saltar onada amb la tecla ',')
				
				instruccio2=null;
			
				//altres accions
				generacions();
				recompte();
				xocs();
				repintar();
				pintaInstruccions(); 
				escoltaOmega();
				actualitzaOnada();
				
				f.repaint();
			
				try {Thread.sleep(30);}
				catch (InterruptedException e) {e.printStackTrace();}
				compt++;
			}
		
			try {Thread.sleep((int) 1000);}
			catch (InterruptedException e) {e.printStackTrace();}
		
			partida++;
			finalPartida(); 
		}	
	}
	
	//  IMPORTA FONT
	void importaFont() {
		Font font;
		try {
		    if(f.JAR) {
		    	InputStream is=getClass().getResourceAsStream("/recursos/font1.ttf");
		    	font= Font.createFont(Font.TRUETYPE_FONT, is);
		    }
		    
		    else font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Java\\PA\\recursos\\font1.ttf"));
		    
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		    
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
	}
	
	// PANTALLA INICIAL 
	void pantallaInicial() {
		BufferedImage imatge = null;
		
		try {
		    if(f.JAR) {
		    	URL url=getClass().getResource("/recursos/titol.png");
		    	imatge = ImageIO.read(url);
		    }
		    
		    else imatge = ImageIO.read(new File("C:\\Java\\PA\\recursos\\titol.png"));
		} catch (IOException e) {
		}
		
		g.setColor(new Color(100,15,20));
		g.fillRect(0,0,f.AMPLADA,f.ALÇADA+f.ALÇADA_EXTRA);
		g.drawImage(imatge,250,200,800,56,null);
		
		g.setFont(new Font("Game Played",Font.PLAIN,20));
		g.setColor(Color.WHITE);
		g.drawString("Any 2022. La Humanitat ha sigut arrasada per una pandèmia mundial : El Covid. ", 200,330);
		g.drawString("Els microxips s’han erigit com l’única esperança de supervivència dels humans. ", 200,370);
		g.drawString("Elimina els virus del teu hoste amb gel hidroalcohòlic abans que sigui infectat.  ", 200,410);
		
		g.setFont(new Font("Game Played",Font.PLAIN,30)); 
		g.setColor(Color.WHITE);
		g.drawString("Prem 'ENTER' per començar", 400,550);

		f.repaint();
		
		començaCanço(false); 
		
		while(true) {
			if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_ENTER) {
				instruccio2=null;
				break;
			}
			try{ Thread.sleep(50); } catch(Exception e){}
		} 
	}
	
	//	INICIALITZACIÓ
	void inicialitzacio() {
		compt=0;
		comptOnada=0;
		comptCalma=0;
		enemicsMorts=0;
		comptAux1=0;
		comptAux2=0;
		comptAux3=0;
		covidsPerInfeccio=3;
		bonus1=0;
		bonus2=0;
		bonus3=0;
		
		aliats=new ArrayList<Mobil>();
		aliats.add(new Aliat(50,50+3*f.DX,f));
		
		bales=new ArrayList<Mobil>();	
		balesEnemigues=new ArrayList<Mobil>();
		balesExplosives=new ArrayList<Mobil>();
		explosions=new ArrayList<Mobil>();
		enemics1= new ArrayList<Mobil>();	
		enemics2= new ArrayList<Mobil>();	
		enemics3= new ArrayList<Mobil>();	
		enemics4= new ArrayList<Mobil>();
		enemics5= new ArrayList<Mobil>();
		bonus= new ArrayList<Mobil>();	
	}
	
	//	MOVIMENTS
	void moviments() {
		moureAliats(aliats);
		moureLlista(bales);
		moureLlista(balesEnemigues);
		moureLlista(balesExplosives);
		moureLlista(enemics1);
		moureLlista(enemics2);
		moureLlista(enemics3);	
		moureLlista(enemics4);	
		moureLlista(enemics5);
		moureLlista(bonus);
	}
	void moureLlista(ArrayList<Mobil> llista) {
		for(int i=0;i<llista.size();i++)
			(llista.get(i)).moure();
	}
	void moureAliats(ArrayList<Mobil> aliats) {
		aliats.get(0).moure(instruccio1,compt);
		for(int i=1;i<aliats.size();i++)
			if(compt%10==0 && r.nextInt()%5==0) aliats.get(i).moure();
	}
	
	//	TRETS
	void trets() {
		tretsAliats(aliats); 	
		tretsEnemics(enemics2);
	}
	
	void tretsAliats(ArrayList<Mobil> aliats) {
		aliats.get(0).disparar(instruccio2,bales);
		if(compt%3==0)
		for(int i=1;i<aliats.size();i++) if(r.nextInt()%6==0) (aliats.get(i)).disparar(bales,false);
	}
	void tretsEnemics(ArrayList<Mobil> enemics) {
		if(compt%20==0)  
			for(int i=0;i<enemics2.size();i++) if(r.nextInt()%8==0) (enemics2.get(i)).disparar(balesEnemigues,false);
	} 

	//	EFECTES BONUS
	void efectesBonus() {
		if(bonus1>0 && instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_V) {
			amplia(aliats);
			bonus1--;
		}
		if(bonus2>0 && instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_C) {
			if(aliats.size()==1) {
				aliats.add(new Aliat(100,50,f));
				aliats.add(new Aliat(100,50+7*f.DX,f));
				}
			if(aliats.size()==2) {
				if(aliats.get(1).y>(f.ALÇADA/2)) aliats.add(new Aliat(100,50,f));
				else aliats.add(new Aliat(100,50+7*f.DX,f));
				}
			bonus2--;
			}
		if(bonus3>0 && instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_B) {
			for(int i=0;i<aliats.size();i++)
				aliats.get(i).disparar(balesExplosives,true);
			bonus3--;
		}
	}
	
	void amplia(ArrayList <Mobil> aliats) {
		for(int i=0;i<aliats.size();i++) {
			aliats.get(i).vides++;
			aliats.get(i).amplada+=8;
			aliats.get(i).alçada+=8;
			aliats.get(i).ampladaBala+=2;
			aliats.get(i).alçadaBala+=2;
			}
	 	}
	void redueix(Mobil aliat) { 
			aliat.vides--;
			if(aliat.vides>0) {
				aliat.amplada-=8;
				aliat.alçada-=8;
				aliat.ampladaBala-=2;
				aliat.alçadaBala-=2;	
			}
	}
		
	// ESCOLTA PAUSA
	void escoltaPausa() {
			if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_ENTER) {
					instruccio2=null;
					
					g.setColor(new Color(0,0,0,150));
					g.fillRect(0,0,f.AMPLADA,f.ALÇADA+f.ALÇADA_EXTRA);
						
					g.setFont(new Font("Game Played",Font.PLAIN,20));
					g.setColor(Color.WHITE);
					g.drawString("ENTER: Reprendre ", 625,350);
					f.repaint();
						
					while(true) {				
						if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_ENTER) break;
						try{ Thread.sleep(50); } catch(Exception e){}
					}
				} 
			}		
	
	// ACTUALITZA MUSICA	
	void actualitzaMusica() {
		if(musica) {
			if(canço.isActive()==false && onada<4) començaCanço(false); 
			if(canço.isActive()==false && onada>=4) començaCanço(true); 
			if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_M) {
				canço.stop();
				musica=false;
			}
		}
		else if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_M) {
			musica=true;
			if(onada>4) començaCanço(true);
			else començaCanço(false);
		}
	}

	void començaCanço(boolean estressant) {
		int t=0;
		if(estressant) t=1;
		int rand=r.nextInt()%5+5; 
		
		try { 
			AudioInputStream audio=null;
			
			if(f.JAR) {
				URL url=getClass().getResource("/recursos/so"+Integer.toString(t)+Integer.toString(rand)+".wav");
				audio = AudioSystem.getAudioInputStream(url);
			}
				
			else audio = AudioSystem.getAudioInputStream(new File("C:\\Java\\PA\\recursos\\so"+Integer.toString(t)+Integer.toString(rand)+".wav"));
				  
			canço=AudioSystem.getClip();
			canço.open(audio);
              
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
       	  	}	  
	    
		canço.start();
	}

	// SALTA ONADA		
	void saltaOnada() {
		if(instruccio2!=null  && instruccio2.getKeyCode()==KeyEvent.VK_COMMA) {
			onada++;
			if (onada>onadaMax)  onadaMax=onada;
		}
	}

	//  GENERACIONS
	void generacions() {	
		if(compt%25==0 && compt>ESPERA_INICIAL) { 	
		if(onada==8 || comptOnada<DURADA_ONADA1+onada*INCREMENT_ONADA) generacioEnemics();	
		generacioBonus();
		}
	}
	
	void generacioEnemics() {
		for(int i=0;i<8;i++) {
			if(r.nextInt()%probEnemic2()==0) enemics2.add(new Enemic2(f.AMPLADA+r.nextInt()%10,45+i*f.DX+r.nextInt()%10,40,40,-2,f));
			else if(r.nextInt()%probEnemic3()==0) enemics3.add(new Enemic3(f.AMPLADA+r.nextInt()%10,45+i*f.DX+r.nextInt()%10,40,40,-2,f));
			else if(r.nextInt()%probEnemic4()==0) enemics4.add(new Enemic4(f.AMPLADA+r.nextInt()%10,45+i*f.DX+r.nextInt()%10,40,40,-2,1-2*((r.nextInt())%2),f));
			else if(r.nextInt()%probEnemic1()==0) enemics1.add(new Enemic1(f.AMPLADA+r.nextInt()%10,45+i*f.DX+r.nextInt()%10,40,40,-3+r.nextInt()%2,f));
			else if(onada==7 && r.nextInt()%3==0 && comptOnada<20*20) enemics5.add(new Enemic5(f.AMPLADA+r.nextInt()%10,45+i*f.DX+r.nextInt()%10,40,40,-2,f));
		}
	}
		
	int probEnemic1() {
		int prob=INFINIT;	
		if(onada==1) prob=4;
		if(onada==2) prob=5;
		if(onada==3) prob=4;
		if(onada==4) prob=5;
		if(onada==5) prob=4;
		if(onada==6) prob=3;
		if(onada==7) prob=INFINIT;
		if(onada==8) prob=2;
		return prob;
	}
	int probEnemic2() {
		int prob=INFINIT;
		if(onada==1) prob=500;
		if(onada==2) prob=25;
		if(onada==3) prob=30;
		if(onada==4) prob=30;
		if(onada==5) prob=15;
		if(onada==6) prob=10;
		if(onada==7) prob=INFINIT;
		if(onada==8) prob=4;
		return prob;
	}
	int probEnemic3() {
		int prob=INFINIT;
		if(onada==3) prob=50;
		if(onada==4) prob=10;
		if(onada==5) prob=10;
		if(onada==6) prob=5;
		if(onada==7) prob=INFINIT;
		if(onada==8) prob=4;
		return prob;
	}
	int probEnemic4() {
		int prob=INFINIT;
		if(onada==5) prob=40;
		if(onada==6) prob=50;
		if(onada==7) prob=INFINIT;
		if(onada==8) prob=5;
		return prob;
	}
	
	void generacioBonus() {
		for(int i=0;i<8;i++) {
			if(onada>=2 && r.nextInt()%700==0) bonus.add(new Bonus(f.AMPLADA,65+i*f.DX,30,30,-15+r.nextInt()%5,1,f));  //Ones 5G
			else if(onada>=3 && r.nextInt()%550==0) bonus.add(new Bonus(f.AMPLADA,65+i*f.DX,30,30,-15+r.nextInt()%5,2,f)); //Pauta de vacunació
			else if(onada>=1 && r.nextInt()%150==0) bonus.add(new Bonus(f.AMPLADA,65+i*f.DX,20,30,-15+r.nextInt()%5,3,f)); //Càrrega de llexiu
		}
	}
	
	//  RECOMPTE
	void recompte() {
		recompteAliats(aliats); 
		recompteLlista(bales);
		recompteLlista(balesEnemigues);
		recompteLlista(balesExplosives);
		recompteLlista(bonus);
		recompteEnemics(enemics1);
		recompteEnemics(enemics2);
		recompteEnemics(enemics3);
		recompteEnemics(enemics4);
		recompteEnemics5(enemics5);
		
	}
	void recompteAliats(ArrayList<Mobil> aliats) {
		for(int i=0;i<aliats.size();i++) {
			if (aliats.get(i).vides<1) {
				aliats.remove(i);
				i--;
			}
		}
	}
	void recompteLlista(ArrayList<Mobil> llista) {
		for(int i=0;i<llista.size();i++) 
			if(llista.get(i).x>f.AMPLADA || llista.get(i).x+llista.get(i).amplada<0) {
				llista.remove(i);i--;
			}
	}
	void recompteEnemics(ArrayList<Mobil> enemics) {
		for(int i=0;i<enemics.size();i++) 
			if(enemics.get(i).x+enemics.get(i).amplada<0) {
				enemics.remove(i);	i--;
				covidsPerInfeccio--;
			}
	}
	void recompteEnemics5(ArrayList<Mobil> enemics5) {
		for(int i=0;i<enemics5.size();i++) 
			if(enemics5.get(i).x+enemics5.get(i).amplada<0) {
				victoria=true;
				finalPartida();
			}
	}
	
	//	XOCS
	void xocs() {
		explosions.clear();	
		
		xocsEnemicsBales(enemics1,bales);
		xocsEnemicsBales(enemics2,bales);
		xocsEnemicsBales(enemics3,bales);
		xocsEnemicsBales(enemics5,bales);

		xocsLlistaExplosivaLlista(enemics4,bales);
		xocsLlistaExplosivaLlista(balesExplosives,enemics1);
		xocsLlistaExplosivaLlista(balesExplosives,enemics2);
		xocsLlistaExplosivaLlista(balesExplosives,enemics3);
		xocsLlistaExplosivaLlista(balesExplosives,enemics4);
		xocsLlistaExplosivaLlista(balesExplosives,enemics5);
		
		xocsEnemicsExplosiusExplosions(enemics4,explosions);
		
		xocsEnemicsExplosions(enemics1,explosions);
		xocsEnemicsExplosions(enemics2,explosions);
		xocsEnemicsExplosions(enemics3,explosions);
		xocsEnemicsExplosions(enemics5,explosions);
		
		xocsLlistaPrincipal(enemics1,aliats); 
		xocsLlistaPrincipal(enemics2,aliats); 
		xocsLlistaPrincipal(enemics3,aliats);  
		xocsLlistaPrincipal(enemics4,aliats);  
		xocsLlistaPrincipal(explosions,aliats); 
		xocsLlistaPrincipal(balesEnemigues,aliats);
		
		xocsBonusAliats(bonus,aliats);
	}
	
	void xocsEnemicsBales(ArrayList<Mobil> enemics,ArrayList<Mobil> bales) {
		for(int i=0;i<enemics.size();i++) {
			for(int j=0;j<bales.size();j++) 
				if(enemics.get(i).xocar(bales.get(j))) {
					bales.remove(j); j--;
					enemics.get(i).vides--;
					if(enemics.get(i).vides==0) {
						enemics.remove(i); i--;enemicsMorts++;
					}
					break; 
				}
			}
	}
	void xocsLlistaExplosivaLlista(ArrayList<Mobil> llistaExplosiva,ArrayList<Mobil> llista) {
		for(int i=0;i<llistaExplosiva.size();i++) {
			for(int j=0;j<llista.size();j++) 
				if(llistaExplosiva.get(i).xocar(llista.get(j))) {
					llista.remove(j); j--;
					llistaExplosiva.get(i).vides--;
					(llistaExplosiva.get(i)).explotar(explosions);
					if(llistaExplosiva.get(i).vides==0) {
						llistaExplosiva.remove(i); i--;enemicsMorts++;
					}
					break;  
				}
			}
	}	
	void xocsEnemicsExplosiusExplosions(ArrayList<Mobil> enemics,ArrayList<Mobil> explosions) {
		for(int i=0;i<enemics.size();i++) {
			for(int j=0;j<explosions.size();j++) 
				if(enemics.get(i).xocar(explosions.get(j))) {
					enemics.get(i).vides--;
					(enemics.get(i)).explotar(explosions);
					if(enemics.get(i).vides==0) {
						enemics.remove(i); i--;enemicsMorts++;
					}
					break;  
				}
			}
	}
	void xocsEnemicsExplosions(ArrayList<Mobil> enemics,ArrayList<Mobil> bales) {
		for(int i=0;i<enemics.size();i++) {
			for(int j=0;j<bales.size();j++) 
				if(enemics.get(i).xocar(bales.get(j))) {
					enemics.get(i).vides--;
					if(enemics.get(i).vides==0) {
						enemics.remove(i); i--;enemicsMorts++;
					}
					break;  
				}
			}
	}	
	void xocsLlistaPrincipal(ArrayList<Mobil> llista,ArrayList<Mobil> aliats) {
		for(int j=0;j<aliats.size();j++)
			for(int i=0;i<llista.size();i++)
				if(llista.get(i).xocar(aliats.get(j))) {
					if(aliats.get(j).vides>1) {
						llista.remove(i);
						i--; 
					}
					redueix(aliats.get(j));
			}
	}	
	void xocsBonusAliats(ArrayList<Mobil> bonus,ArrayList<Mobil> aliats) {
		for(int i=0;i<aliats.size();i++) {
			for(int j=0;j<bonus.size();j++)
				if(bonus.get(j).xocar(aliats.get(i))) {
					if(bonus.get(j).num==1) bonus1++;
					if(bonus.get(j).num==2) bonus2++;
					if(bonus.get(j).num==3) bonus3++;
					bonus.remove(j);j--;
			}
		}
	}

	// REPINTAR
	void repintar() {
		g.setColor(new Color(100,15,20));
		g.fillRect(0,0,f.AMPLADA,f.ALÇADA);
		g.setColor(Color.BLACK);
		g.fillRect(0,f.ALÇADA,f.AMPLADA,f.ALÇADA_EXTRA);

		repintarLlista(aliats);
		repintarLlista(bales);
		repintarLlista(balesEnemigues);
		repintarLlista(balesExplosives);
		repintarLlista(enemics2);
		repintarLlista(enemics3);
		repintarLlista(enemics1);
		repintarLlista(enemics4);
		repintarLlista(enemics5);
		repintarLlista(explosions);
		repintarLlista(bonus);
	}		

	void repintarLlista(ArrayList<Mobil> llista) {
		for(int i=0;i<llista.size();i++)
			(llista.get(i)).pintar(g);
	}

	//	PINTA INSTRUCCIONS 
	void pintaInstruccions() {	
		g.setFont(new Font("Game Played",Font.PLAIN,20));
		g.setColor(Color.WHITE);
		
		if(compt>20 && compt<ESPERA_INICIAL-20) {
			g.drawString("Fletxes: amunt/avall",625,200);
			g.drawString("ESPAI: disparar",625,250);
			g.drawString("ENTER: Pausa ", 625,400);
			g.drawString("M: activar/desactivar música", 625,450);
		}
		
		if(compt>ESPERA_INICIAL){
			g.setFont(new Font("Game Played",Font.PLAIN,20)); 
			g.drawString("Virus eliminats: "+Integer.toString(enemicsMorts), 100,f.ALÇADA+f.ALÇADA_EXTRA/2);
			
			g.setFont(new Font("Game Played",Font.PLAIN,40));
			g.setColor(Color.RED);
			if(covidsPerInfeccio==0 && comptAux3<100) {
				g.drawString("Hoste infectat",460,250);
				comptAux3++;
			}
			else if(covidsPerInfeccio==1 && comptAux2<100) {
				g.drawString("1 virus per a infecció",460,250);	
				comptAux2++;
			}
			else if(covidsPerInfeccio==2 && comptAux1<100) {
				g.drawString("2 virus per a infecció",460,250);
				comptAux1++;
			}
			
			g.setFont(new Font("Game Played",Font.PLAIN,15));
			g.setColor(new Color(50,50,50));
			g.drawString("V: ones 5G", 900,f.ALÇADA+f.ALÇADA_EXTRA/2);
			g.drawString("C: pauta completa de vacunació", 590,f.ALÇADA+f.ALÇADA_EXTRA/2);
			g.drawString("B: càrrega de llexiu", 1040,f.ALÇADA+f.ALÇADA_EXTRA/2);
		
			g.setColor(Color.WHITE);
			if(bonus1>0) g.drawString("V: ones 5G ("+Integer.toString(bonus1)+")", 900,f.ALÇADA+f.ALÇADA_EXTRA/2);
			if(bonus2>0) g.drawString("C: pauta completa de vacunació ("+Integer.toString(bonus2)+")", 590,f.ALÇADA+f.ALÇADA_EXTRA/2);
			if(bonus3>0) g.drawString("B: càrrega de llexiu ("+Integer.toString(bonus3)+")", 1040,f.ALÇADA+f.ALÇADA_EXTRA/2);	
		}
	}

	// ESCOLTA OMEGA
	void escoltaOmega() {
			if(onada==7 && comptOnada==20*8) {
					g.setColor(new Color(0,0,0,150));
					g.fillRect(0,0,f.AMPLADA,f.ALÇADA+f.ALÇADA_EXTRA);
				
					g.setColor(Color.WHITE);
					g.drawString("<< Bona tarda, som la variant omega: la mutació definitiva. ", 360,250);
					g.drawString("Hem comprès els nostres errors i la futilitat de fer el mal.", 360,280);
					g.drawString("Si ens deixes passar començarà un futur de pau i harmonia", 360,310);
					g.drawString("en el qual covids i humans viuran des del respecte mutu >>", 360,340);
					
					g.setFont(new Font("Game Played",Font.PLAIN,15));
					g.drawString("ENTER: Hmm... D'acord", 530,400);

					f.repaint();
				
					while(true) { 		
						if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_ENTER) {
							instruccio2=null;
							break;
						}
						try{ Thread.sleep(50); } catch(Exception e){}
					}
				} 
			}
	
	//	ACTUALITZA ONADA
	void actualitzaOnada() {
		if(enemics1.size()==0 && enemics2.size()==0 && enemics3.size()==0 && enemics4.size()==0	&& comptOnada>DURADA_ONADA1+(onada-1)*INCREMENT_ONADA) {
			comptCalma++;
		}
		if(onada==7 && enemics5.size()==0 && comptOnada>5*20)
			comptCalma++;;
		
		if((onada==0 && comptOnada==ESPERA_INICIAL) || 
				(onada<8 && comptCalma>DURADA_CALMA)) { 
			comptOnada=0;
			comptCalma=0;
			onada++;
			if (onada>onadaMax)  onadaMax=onada;
		}
			
		else comptOnada++;
		
		if(onada<6 && compt>ESPERA_INICIAL+30 && comptOnada<ESPERA_INICIAL+50) {	
			g.setFont(new Font("Game Played",Font.PLAIN,50));
			g.setColor(Color.WHITE);
			g.drawString("Onada "+Integer.toString(onada), 600,300);	
		}
		
		if(onada==6 && comptOnada<100) {	
			g.setFont(new Font("Game Played",Font.PLAIN,50));
			g.setColor(Color.WHITE);
			g.drawString("Última onada", 500,300);	
		}
		
		if(onada==8 && comptOnada>50 && comptOnada<150) {	
			g.setFont(new Font("Game Played",Font.PLAIN,50));
			g.setColor(Color.WHITE);
			g.drawString("Onada infinita", 500,300);	
		}
	}
	
	// FINAL PARTIDA 
	void finalPartida() {			
			g.setColor(new Color(0,0,0,150));
			g.fillRect(0,0,f.AMPLADA,f.ALÇADA+f.ALÇADA_EXTRA);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Game Played",Font.PLAIN,20));
			g.drawString("Has eliminat "+Integer.toString(enemicsMorts)+" virus", 500,350);
			
			
			if(victoria) {
				g.setFont(new Font("Game Played",Font.PLAIN,30));
				g.drawString("Victòria !!", 500,250);
				g.setFont(new Font("Game Played",Font.PLAIN,20));
				g.drawString("La pandemia s'ha acabat", 500,310);
				
				f.repaint();	
			}
			
			else {
				g.setFont(new Font("Game Played",Font.PLAIN,30));
				if(covidsPerInfeccio==0) g.drawString("Hoste infectat ", 500,300);
				else g.drawString("Destruït", 500,300);
				
				g.setFont(new Font("Game Played",Font.PLAIN,15));
				g.drawString("Nombre: Tornar a la onada", 500,450);
				
				f.repaint();	
				ESPERA_INICIAL=0;
			}
				
			while(true) {	
				if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_1) {
					if(onadaMax<1) onada=onadaMax;
					else onada=1;
					instruccio2=null;
					break;
				}
				if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_2) {	
					if(onadaMax<2) onada=onadaMax;
					else onada=2;
					instruccio2=null;
					break;
				}
				if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_3) {
					if(onadaMax<3) onada=onadaMax;
					else onada=3;
					instruccio2=null;
					break;
				}
				if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_4) {
					if(onadaMax<4) onada=onadaMax;
					else onada=4;
					instruccio2=null;
					break;
				}
				if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_5) {
					if(onadaMax<5) onada=onadaMax;
					else onada=5;
					instruccio2=null;
					break;
				}
				if(instruccio2!=null && instruccio2.getKeyCode()==KeyEvent.VK_6) {
					if(onadaMax<6) onada=onadaMax;
					else onada=6;
					instruccio2=null;
					break;
				}
				try{ Thread.sleep(50); } catch(Exception e){}	
			}
		}

}
	

