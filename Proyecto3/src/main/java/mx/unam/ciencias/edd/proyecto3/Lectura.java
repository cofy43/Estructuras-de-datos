package mx.unam.ciencias.edd.proyecto3;

import java.io.IOException;
import java.text.Normalizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;

/**
 * Clase que se encarga del análisis de los argumentos reibidos desde la
 * terminal para realizar los comportamientos encontrados
 * 
 * @author Martin Felipe Espinal Cruces.
 */

public class Lectura {

    static File file;
    static FileWriter fw;
    static Lista<String> direcciones = new Lista<>();
    static Lista<Diccionario<String, Integer>> archivos = new Lista<>();
    static String salida = "";

    /**
     * Método que dado una dirección y una lista sobre escribe el archivo del
     * directorio indicado. En caso de no existir se crea en el directorio indicado.
     * 
     * @param String        direccion Ubucación del archivo donde se sobre
     *                      escribira, en caso de no existir, se creará en la ruta
     *                      indicada.
     * @param Lista<String> lista
     */
    public static void sobreEscribe(String direccion, Lista<String> lista) {
        try {
            file = new File(direccion);
            fw = new FileWriter(file);
            for (String s : lista) {
                fw.write(s + "\n");
            }
            fw.close();
        } catch (IOException io) {
            System.out.println("Error, direccion invalida");
            uso();
        }
    }

    /**
     * Método que se encarga de eliminar espacios, acentos y demas caracteres para
     * ordenar los renglones
     * 
     * @param texto Cadena a filtrar.
     * @return texto Cadena filtrada.
     */
    public static String cleanString(String texto) {
        String filter = Normalizer.normalize(texto.replaceAll("\\P{L}+", "").toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return filter;
    }

    /**
     * Método encargado de leer un archivo y almacenar las línas en una lista para
     * su posterior uso
     * 
     * @param args      Elementos de la terminal
     * @param direccion Ubicación del archivo
     * @return list Lista con las líneas obtenidas del archivo
     * @throws IOException
     */
    public static void creaDiccionarios(Lista<String> direcciones) throws IOException {
        for (int i = 0; i < direcciones.getElementos(); i ++) {
            Diccionario<String, Integer> diccionario = new Diccionario<>();
            FileReader fr = new FileReader(direcciones.get(i));
            BufferedReader br = new BufferedReader(fr);
            String cadena;
            while ((cadena = br.readLine()) != null) {
                String[] palabras = cadena.split("\\s");
                for (int j = 0; j < palabras.length; j++) {
                    String palabra = cleanString(palabras[j]);
                    if (palabra.length() > 0) {
                        if (diccionario.contiene(palabra)) {
                            diccionario.agrega(palabra, diccionario.get(palabra) + 1);
                        } else {
                            diccionario.agrega(palabra, 1);
                        }
                    } 
                }
            }
            fr.close();
            br.close();
            archivos.agrega(diccionario);
        }
    }

    /**
     * Método principal el cual se encarga de correr el
     * programa dependiendo de las banderas, direcciones, 
     * o entrada estandar que detecte.
     * @param String[] argumentos recibidos por el método
     * main.
     */
    public static void bandera(String[] args) throws IOException{
        boolean bandera2 = false;
        
        for (String bandera : args) {
            if (bandera.length() == 2) {
                if (bandera.equals("-o")) {
                    bandera2 = true;
                }
            }
            if (bandera.length() > 2) {
                if (bandera2 && salida.equals("")) {
                    salida = bandera;
                } else {
                    direcciones.agrega(bandera);
                }
            }
        }

        if (bandera2) {
            System.out.println("Salida: " + salida + "\n");
            int i = 1;
            for (String s : direcciones) {
                System.out.println("Direccion " + i + " " + s + "\n");
                i++;
            }

            creaDiccionarios(direcciones);

            for (int j = 0; j < archivos.getElementos(); j++) {
                System.out.println("Diccionario " + j + " " + archivos.get(j).toString() + "\n");
            }

        } else {
            uso();
        }
        
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
}