package mx.unam.ciencias.edd.proyecto1;

import java.io.File;
import java.io.IOException;
import mx.unam.ciencias.edd.proyecto1.Lectura;

public class Proyecto1 {

    private Lectura lectura;

    public Proyecto1 () {
        lectura = new Lectura();
    }

    /* Imprime el uso del programa y lo termina. */
    private static void uso() {
        System.err.println("Uso: java -jar proyecto1.jar /ruta/cualquiera/ejemplo.txt");
        System.err.println("Para imprimir en orden inverso los renglones");
        System.err.println("Uso: java -jar proyecto1.jar -r /ruta/cualquiera/ejemplo.txt");
        System.err.println("Para rescribir en el archivo a ordenar");
        System.err.println("Uso: java -jar proyecto1.jar -o /ruta/cualquiera/ejemplo.txt");
        System.exit(1);
    }

    public static void bandera(String[] args) {
        for (String bandera : args) {
            if (bandera.equals("-r"))
                System.out.println("Imprime en orden inverso");
            if (bandera.equals("-o"))
                System.out.println("Sobre escribe el archivo");
        }
    }
}
