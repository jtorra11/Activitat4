package Covid;

public class Bonus extends Mobil{
	
	// ** CONSTRUCTOR **
	Bonus(int x, int y, int amplada, int al�ada, int v, int num, Finestra f) {
		super(x,y,amplada,al�ada,v,f,"bonus"+Integer.toString(num)+".png");
		super.num=num;
	}
}


