package javaDAM;

// import java.util.Arrays;
import java.util.Scanner;

public class Control_Test {

	/* private static int p_total = 30; */
	public static void main	(String[] args) {
    int p_total;

	Scanner preguntes = new Scanner (System.in);
	/*float p_correctes, p_incorrectes, p_enblanc, nota_final; */
	int p_correctes, p_incorrectes, p_enblanc;
	float nota_final;

	System.out.println("Entrar el nombre de preguntes:");
	p_total=preguntes.nextInt();

	do {
	System.out.println("Entrar el nombre de preguntes correctes:");
	p_correctes=preguntes.nextInt();

	System.out.println("Entrar el nombre de preguntes  incorrectes:");
    p_incorrectes=preguntes.nextInt();

	System.out.println("Entrar el nombre de preguntes no contestades:");
    p_enblanc=preguntes.nextInt();

    if ((p_correctes + p_incorrectes + p_enblanc) != p_total)
    	System.out.println("Error, torna a entrar els 3 valors, perquè no sumen " + p_total + ".");

	} while ((p_correctes + p_incorrectes + p_enblanc) != p_total);

    preguntes.close();
    // *nota_final=p_correctes - (p_incorrectes/2); **/
    nota_final=((float) p_correctes - (float) p_incorrectes)/((float) p_total/10);

	System.out.printf("La nota final és " + "%.2f%n" , nota_final );
	}
}


