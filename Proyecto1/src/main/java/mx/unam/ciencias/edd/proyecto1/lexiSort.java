package mx.unam.ciencias.edd.proyecto1;

import java.util.Comparator;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import mx.unam.ciencias.edd.Lista;
import java.text.Normalizer;


public class lexiSort {

    static BufferedReader br;
    static FileReader fr;

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    public static Lista<String> creaLista(String[] args, String direccion) {
        Lista<String> lista = new Lista<>();
        for (String std : args) {
            if (std.length() > 2) {
                direccion = std;
                try {
                    fr = new FileReader(direccion);
                    br = new BufferedReader(fr);
                    String cadena;
                    while ((cadena = br.readLine()) != null) {
                        //System.out.println(cadena);
                            lista.agrega(cadena);
                    }
                    fr.close();
                    br.close();
                } catch (IOException io) {
                    System.err.println("Error al encontrar el archivo");
                }
            } 
        }
        return lista;
    }

    public static Comparator<String> comparador = new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            String filterA = ((cleanString(s1.replaceAll(" ", ""))).replaceAll("[^\\dA-Za-z ]", "")).toLowerCase();
            String filterB = ((cleanString(s2.replaceAll(" ", ""))).replaceAll("[^\\dA-Za-z ]", "")).toLowerCase();
            return filterA.compareTo(filterB);
        }
    };


    public static Lista<String> merge(Lista<String> lista) {
        return lista.mergeSort(comparador);
    }
    
}