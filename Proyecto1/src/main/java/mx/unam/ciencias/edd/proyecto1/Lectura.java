package mx.unam.ciencias.edd.proyecto1;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.util.Comparator;
import java.text.Normalizer;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto1.lexiSort;

public class Lectura {

    static File file;
    static FileWriter fw;
    static lexiSort sort = new lexiSort();

    public Lectura () {
    }

    public static void imprimeInverso(Lista<String> list) {
        Lista<String> l = list.reversa();
        for (String s : l) {
            System.out.println(s);
        }
    }

    public static String direccionDeArchivo(String [] args) {
        String direccion = "";

        for (String std : args) {
            if (std.length() > 2) {
                direccion = std;
            } 
        }
        return direccion;

    }

    public static void sobreEscribe(String direccion, Lista<String> lista) {
        try {
            file = new File(direccion);
            fw = new FileWriter(file);
            for (String s : lista) {
                fw.write(s + "\n");
            }
            fw.close();
        } catch (IOException io) {
            System.out.println("Error");
        }
    } 

    public static void bandera(String[] args) {
        for (String bandera : args) {
            if (bandera.length() == 2) {
                if (bandera.equals("-r")) {
                    System.out.println("Imprime en orden inverso \n");
                    String direccion = direccionDeArchivo(args);
                    Lista<String> list = sort.creaLista(args, direccion);
                    imprimeInverso(sort.merge(list));
                }
                if (bandera.equals("-o")) {
                    System.out.println("Sobre escribe el archivo \n");
                    String direccion = direccionDeArchivo(args);
                    Lista<String> list = sort.creaLista(args, direccion);
                    sobreEscribe(direccion, sort.merge(list));
                }
            }
            
        }
    }

    public static void main(String[] args) {
        bandera(args);
    } 
    
}