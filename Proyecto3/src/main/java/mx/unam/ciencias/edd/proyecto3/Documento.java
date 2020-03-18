package mx.unam.ciencias.edd.proyecto3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import mx.unam.ciencias.edd.Diccionario;

public class Documento {

    private static final String TAG1 = "<html>\n";
    private static final String TAG2 = "<head>\n";
    private static final String TAG3 = "<title>\n";
    private static final String TAG4 = "</title>\n";
    private static final String TAG5 = "<head>\n";
    private static final String TAG6 = "<body>\n";
    private static final String TAG7 = "</body>\n";
    private static final String TAG8 = "</html>\n";

    private Diccionario<String, Integer> diccionario;
    private String direccion;

    public Documento(Diccionario<String, Integer> diccionario, String direccion) {
        this.diccionario = diccionario;
        this.direccion = direccion;
    }

    public void creaHtml() throws FileNotFoundException {
        String document = direccion.substring(0, direccion.length()-4) + ".html";
        System.out.println(document);
        FileOutputStream file = new FileOutputStream(new File(document));
        PrintStream ps = new PrintStream(file);
        String conteo  = "";
        Iterator<String> it = this.diccionario.iteradorLlaves();
        while (it.hasNext()) {
            String aux = it.next();
            conteo += "<p>"+ "Palabra: "+ aux + "  #Apariciones: " + this.diccionario.get(aux) + "</p>\n";
        }
        String ouput  = creaCuerpoHtml(conteo);
        System.out.println(ouput);
        ps.println(ouput);
        ps.close();
    }

    private String creaCuerpoHtml(String conteo) {
        String cuerpo = TAG1;
        cuerpo += TAG2 + TAG2 + TAG4 + TAG5 + TAG6;
        cuerpo += conteo;
        cuerpo += TAG7 + TAG8;
        return cuerpo;
    }
}