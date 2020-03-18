package mx.unam.ciencias.edd.proyecto3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;

public class HTML {
    File file;
    FileOutputStream archivo;
    PrintStream p;
    int numeroArchivosHtml;
    String index;
    Lista<String> direcciones;
    Lista<Diccionario<String, Integer>> listaDiccionario;

    public HTML(){}

    public HTML(Lectura l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        this.index = l.salida;
        this.direcciones = l.direcciones;
        this.numeroArchivosHtml = l.direcciones.getElementos();
        this.listaDiccionario = l.archivos;
    }


    public String creaImg(String svg) {
        String emdeb = String.format("<img src=\"%s\" type=\"image/svg+xml\" width=\"800\" height=\"800\">\n", svg);
        return emdeb;
    }

    
    public String creaHipervinculo(String archivo, String titulo) {
        String hipervinculo = String.format("\n\t\t\t<a href=\"%s\">%s</a hrfe>\n", archivo, titulo);
        return hipervinculo;
    }

    
    public String creaDireccion(String direccion, String nombre) {
        int inicio = obtenNombre(nombre);
        String html = direccion + nombre.substring(++inicio, nombre.length()-4) + ".html";
        return html;
    }

    public void creaHtml(String direccion) throws FileNotFoundException {
        FileOutputStream archivo = new FileOutputStream(new File(direccion));
        PrintStream p = new PrintStream(archivo);

    }
    
    public void creaIndex() throws FileNotFoundException {
        file = new File(index);
        file.mkdirs();
        String file = index + "index.html";
        archivo = new FileOutputStream(file);
        p = new PrintStream(archivo);
        String s = "";
        p.println(index);
        
        for (String direccion : direcciones) {
            String directorio = creaDireccion(index, direccion);
            int inicio = obtenNombre(direccion);
            String nombre = direccion.substring(++inicio, direccion.length()-4);
            s += String.format("\t\t<p>%s</p>\n", creaHipervinculo(directorio, nombre));
            creaHtml(directorio);
        }
        System.out.println(creaEjemplo(s));
        p.println(creaEjemplo(s));
        p.close();
    }

    
    public int obtenNombre(String direccion) {
        int inicio = 0;
        for (int i = 0; i < direccion.length(); i++) {
            if (direccion.charAt(i) == '/') {
                inicio = i;
            }
        }
        return inicio;
    }


    public String creaEjemplo(String svg) {
        String inicio = "<html>\n";
        String iHead = "\t<head>\n";
        String fHead = "\t</head>\n";
        String iBody = "\t<body>\n";
        String s = "\t\t<p>Hola Mundo</p>\n";
        String fBody = "\t</body>\n";
        String fin = "</html>";
        return inicio + iHead + fHead + iBody + svg + fBody + fin;
    }
}