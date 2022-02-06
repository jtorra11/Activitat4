package Covid;

public class Bonus extends Mobil{
	
	// ** CONSTRUCTOR **
	Bonus(int x, int y, int amplada, int alçada, int v, int num, Finestra f) {
		super(x,y,amplada,alçada,v,f,"bonus"+Integer.toString(num)+".png");
		super.num=num;
	}
}


