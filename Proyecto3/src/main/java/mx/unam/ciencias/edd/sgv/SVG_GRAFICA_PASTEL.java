package mx.unam.ciencias.edd.sgv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Graficable;

public class SVG_GRAFICA_PASTEL {

    static final String INICIO = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ETIQUETA_G = "<g>\n";
    static final String CIERRA_ETIQUETA_G = "</g>\n";
    private String file;

    public void creaGRaficaPastel(Lista<Graficable> lista, String direccion) throws FileNotFoundException {
        Random rand = new Random();
        file = direccion.substring(0, direccion.length()-5) + "_Grafica_Pastel.svg";
        FileOutputStream fos = new FileOutputStream(new File(file));
        PrintStream p = new PrintStream(fos);
        String svg = INICIO;
        svg += String.format("<svg height=\"%d\" width=\"%d\" viewBox=\"%s\" xmlns=\"http://www.w3.org/2000/svg\">\n", 1900, (lista.getElementos()*100)/2, "0 0 150 150");
        svg += "<circle r=\"10\" cx=\"10\" cy=\"10\" fill=\"white\" />";
        double area = Math.PI*(Math.pow(10, 2));
        double rotacion = 0;
        String tabla = "";
        int y = 105;
        System.out.println("Elementos " + lista.getElementos());

        for (Graficable gra : lista) {
            double seccion = (gra.porcentaje*100)/area;
            double aRotar = (seccion*2)/25;
            double aGrados = aRotar*(180/Math.PI) + rotacion;
            rotacion += aGrados;
            String result = generateRandomColor();
            svg += dibujaRebanada(50, 100, 100, result, gra.porcentaje, rotacion);
            //tabla += dibujaRectangulo(30, y, 3, 3, 0, result);
            String texto  = "palabra: " + gra.clave + " %: " +String.valueOf(gra.porcentaje);
            tabla += dibujaTexto(50, y, 5, texto) ;
            y += 5;
        }

        svg += tabla;
        svg += "</svg>\n";
        p.println(svg);
        p.close();
    }

    public String dibujaRectangulo(int x, int y, int heigt, int width, int stroke_width, String color) {
        String concatenacion = "";
        String rectangulo = String.format("<rect x=\"%d\" y=\"%d\" height=\"%d\" width=\"%d\" fill=\"%s\" stroke=\"black\" stroke-width=\"%d\"/>\n", x, y, heigt, width, color, stroke_width);
        return concatenacion + rectangulo;
    }

    public String dibujaTexto(int x, int y, int size, String texto) {
        String concatenacion = String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-family=\"sans-serif\" font-size=\"%d\" text-anchor=\"middle\">",x ,y, size, texto);
        concatenacion += texto + "</text>\n";
        return concatenacion ;
    }

    private static String generateRandomColor(){
        String[] letters = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String color = "#";
        for (int i = 0; i < 6; i++ ) {
            color += letters[(int) Math.round(Math.random() * 15)];
        }
        return color;
    }

    public String getDireccion() {
        return this.file;
    }

    public String dibujaRebanada(int radio, int x, int y, String stroke, double porcentaje, double rotacion) {
        double circunferencia = (2*Math.PI)*radio;
        double strikeDasharray = (porcentaje * circunferencia)/1000;
        String dasharray = String.valueOf(strikeDasharray) + " " + String.valueOf(circunferencia);
        return String.format("<circle r=\"%d\" cx=\"%d\" cy=\"%d\" fill=\"transparent\" stroke=\"%s\" stroke-width=\"%d\" stroke-dasharray=\"%s\" transform=\"rotate(%f, 50, 50)\" />\n", radio/2, x/2, y/2, stroke, radio, dasharray, rotacion);
    }
}