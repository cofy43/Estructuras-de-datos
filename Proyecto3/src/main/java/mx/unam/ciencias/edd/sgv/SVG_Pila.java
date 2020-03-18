package mx.unam.ciencias.edd.sgv;

import mx.unam.ciencias.edd.MeteSaca;

public class SVG_Pila {
    
    static final String INICIO = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ETIQUETA_G = "<g>\n";
    static final String CIERRA_ETIQUETA_G = "</g>\n";

    public void creaSGVPila(MeteSaca<String> pila) {
        String svg = "", aux = "", inicio = "";
        int x1 = 10, x2 = 40, y = 10, heigt = 40, width = 60, stroke_width = 2, font_size = 21, i = 0; 
        int elementos = 0;
        while (!pila.esVacia()) {
            if (i != 0) {
                svg += dibujaRectangulo(x1, y, heigt, width, stroke_width);
                y = y + 25;
                aux = pila.saca();
                svg += dibujaTexto(x2, y, font_size, aux);
                y = y + 15;
                elementos ++;
            } else {
                i++;
            }
        }
        svg += "</svg>";

        int widt = (20 + elementos *40);
        inicio += INICIO;
        inicio += String.format("<svg height=\"%d\" width=\"80\">\n", widt);

        System.out.println(inicio + svg);
    }

    public String dibujaRectangulo(int x, int y, int heigt, int width, int stroke_width) {
        String concatenacion = "";
        concatenacion += ETIQUETA_G;
        String rectangulo = String.format("<rect x=\"%d\" y=\"%d\" height=\"%d\" width=\"%d\" fill=\"white\" stroke=\"black\" stroke-width=\"%d\"/>\n", x, y, heigt, width, stroke_width);
        return concatenacion + rectangulo;
    }

    public String dibujaTexto(int x, int y, int font_size, String texto) {
        //<text x="40" y="35" fill="black" font-family="sans-serif" font-size="16" text-anchor="middle">1</text>
        String concatenacion = String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-family=\"sans-serif\" font-size=\"%d\" text-anchor=\"middle\">",x ,y ,font_size);
        concatenacion += texto + "</text>\n" + CIERRA_ETIQUETA_G;
        return concatenacion ;
    }
}