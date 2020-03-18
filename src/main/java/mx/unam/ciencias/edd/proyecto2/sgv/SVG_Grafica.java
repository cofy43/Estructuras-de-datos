package mx.unam.ciencias.edd.proyecto2.sgv;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;

public class SVG_Grafica {

    private class Coordenadas {
        double x, y;
        int elemento;

        public Coordenadas(double x, double y, int elemento) {
            this.x = x;
            this.y = y;
            this.elemento = elemento;
        }
    }

    static final String INICIO = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ETIQUETA_G = "<g>\n";
    static final String CIERRA_ETIQUETA_G = "</g>\n";
    Lista<Coordenadas> coordenadas = new Lista<>();


    public void creaSVGArbolCompleto(Grafica<Integer> grafica) {
        String svg = INICIO;
        int radio = calculaRadio(grafica);
        double radioVertice = ((2*grafica.getElementos())* (2*radio))/Math.PI;
        double altura = (2*radio)+(2* radioVertice)+radio;
        svg += String.format("<svg height=\"%f\" width=\"%f\">\n", altura, altura);
        svg += ETIQUETA_G;

        dibujaVertices(radioVertice, grafica.getElementos(), altura/2, grafica);

        if (grafica.getAristas() >= 1) {
            svg += dibujaAristas(grafica);
        }

        svg += vertices(radio);
        svg += CIERRA_ETIQUETA_G;
        svg += "</svg>\n";
        System.out.println(svg);
        
    }

    public String vertices(int radio) {
        String s = "";
        for (Coordenadas c: this.coordenadas) {
            s +=  creaVertice(c.x, c.y, radio, String.valueOf(c.elemento));
        }
        return s;
    }

    public void dibujaVertices(double radio, double division, double altura, Grafica<Integer> grafica) {
        double angulo, x, y;
        int i = 0;
        for (Integer j: grafica) {
            if (grafica.getElementos() == 1) {
                this.coordenadas.agrega(new Coordenadas(altura, altura, j));
            } else {
                angulo = i++ * (360/division);
                x = radio * Math.cos(Math.toRadians(angulo));
                y = radio * Math.sin(Math.toRadians(angulo));
                this.coordenadas.agrega(new Coordenadas(x + altura, y + altura, j));
            }
        }
    }

    public String dibujaAristas(Grafica<Integer> grafica) {
        String s = "";
        for (int i = 0; i < this.coordenadas.getElementos(); i++) {
            Coordenadas a = this.coordenadas.get(i);
            for (int j = i; j < grafica.getElementos(); j++) {
                Coordenadas b = coordenadas.get(j);
                if (grafica.sonVecinos(a.elemento, b.elemento)) {
                    s += creaLinea(a.x, a.y, b.x, b.y);
                }
            }
        }
        return s;
    }

    public String creaVertice(double x, double y, double r, String elemento) {
        String texto, vertice;

        vertice = String.format("<circle cx=\"%f\" cy=\"%f\" r=\"%f\" fill=\"white\" stroke=\"black\" stroke-width=\"2\"/>\n", x, y, r);

        texto = String.format("<text x=\"%f\" y=\"%f\" fill=\"black\" font-famili=\"sans-serif\" font-size=\"20\" text-anchor=\"middle\">%s</text>\n", x, y+4, elemento);

        return vertice + texto;
    }

    public String creaLinea(double x1, double y1, double x2, double y2) {
        return String.format("<line stroke=\"black\" stroke-width=\"2\" x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\"/>\n", x1, y1, x2, y2);
    }

    public int calculaRadio(Iterable<?> iterable) {
        int maximo = 0, promedio = 25;
        for (Object e: iterable) {
            if (e.toString().length() > maximo) {
                maximo = e.toString().length();
            }
        }

        if (maximo >= 4)
            promedio = ((maximo - 3) * 4) + promedio;
        return promedio;
    }
}