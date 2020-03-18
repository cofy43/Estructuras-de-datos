package mx.unam.ciencias.edd.proyecto2.sgv;

import mx.unam.ciencias.edd.MeteSaca;

public class SVG_Cola {
    static final String INICIO = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ETIQUETA_G = "<g>\n";
    static final String CIERRA_ETIQUETA_G = "</g>\n";

    String flecha = "<defs>\n";
    String cierra_flecha = "</defs>\n";
    String id = "<g id=\"flecha\">\n";

    public void creaSGVCola(MeteSaca<String> cola) {
        /* File file = new File("~/Documents/EDD/Proyecto2/SGV_LISTA.sgv");
        fw.write(svg);
        fw.close();
        FileWriter fw = new FileWriter(file); */
        String svg = "" ,aux = "", inicio = "";
        int x = 10, y1 = 10, y2 = 35, heigt = 40, width = 60, stroke_width = 2, font_size = 21, i = 0; 
        int elementos =0;
        svg += dibujaFlechas(0, 0);
        
        while (!cola.esVacia()) {
            if (i != 0) {
                svg += dibujaRectangulo(x, y1, heigt, width, stroke_width);
                x = x + 30;
                aux = cola.saca();
                svg += dibujaTexto(x, y2, font_size, aux);
                x = x + 40;
                svg += agregaFlechas(x, y2);
                x = x + 10;
                elementos ++;
            } else {
                i++;
            }
        }
        int widt = (10 + 80 * elementos - 10)-8;
        inicio += INICIO;
        inicio += String.format("<svg height=\"60\" width=\"%d\">\n", widt);

        System.out.println(inicio +svg);
    }

    public String dibujaRectangulo(int x, int y, int heigt, int width, int stroke_width) {
        String concatenacion = "";
        concatenacion += ETIQUETA_G;
        String rectangulo = String.format("<rect x=\"%d\" y=\"%d\" height=\"%d\" width=\"%d\" fill=\"white\" stroke=\"black\" stroke-width=\"%d\"/>\n", x, y, heigt, width, stroke_width);
        return concatenacion + rectangulo;
    }

    public String dibujaTexto(int x, int y, int font_size, String texto) {
        String concatenacion = String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-family=\"sans-serif\" font-size=\"%d\" text-anchor=\"middle\">",x ,y ,font_size);
        concatenacion += texto + "</text>\n" + CIERRA_ETIQUETA_G;
        return concatenacion ;
    }

    public String agregaFlechas(int x, int y) {
        String concatenacion = String.format("<use xlink:href =\"#flecha\" x=\"%d\" y=\"%d\" />", x, y);
        return concatenacion + "\n";
    }

    public String dibujaFlechas(int x, int y) {
        String concatenacion = flecha;
        concatenacion += id;
        String etiquetaText = String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-famili=\"sans-serif\" font-size=\"21\" text-anchor=\"middle\">→</text>\n", x , y);
        etiquetaText += CIERRA_ETIQUETA_G;
        return concatenacion + etiquetaText + cierra_flecha;
    }
}