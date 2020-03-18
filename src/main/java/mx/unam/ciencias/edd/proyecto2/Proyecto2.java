package mx.unam.ciencias.edd.proyecto2;

import java.io.IOException;

public class Proyecto2 {

	static Lectura l = new Lectura();

	

	public static void main(String[] args) {
		for (String s : args) {
			try {
				l.lectura(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
