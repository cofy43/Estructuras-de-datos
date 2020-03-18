package mx.unam.ciencias.edd.proyecto2.sgv;

import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.VerticeArbolBinario;

public class SVG_Arbol_Ordenado {
    static final String INICIO = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ETIQUETA_G = "<g>\n";
    static final String CIERRA_ETIQUETA_G = "</g>\n";

    public void creaSVGArbolCompleto(ArbolBinarioOrdenado<Integer> arbol) {
        String svg = INICIO;
        int profundidad = arbol.altura() == 0 ? 1 : arbol.altura();
        int radio = calculaRadio(arbol);
        int mitad = (int) (Math.pow(2, profundidad) * radio*2);
        int height = 100 + profundidad*100;
        svg += String.format("<svg height=\"%d\" width=\"%d\">\n", height, mitad);
        svg += ETIQUETA_G;

        if (!arbol.esVacia()) {
            svg += dibujaLineas(arbol.raiz(), (mitad/2)/2, mitad/2, 25);
            svg += dibujaVertices(arbol.raiz(), (mitad/2)/2, mitad/2, 25, radio);
        }

        svg += CIERRA_ETIQUETA_G;
        svg += "</svg>\n";
        System.out.println(svg);
        
    }

    public String dibujaVertices(VerticeArbolBinario vertice, double mitad, double coordenada,
    double y, double r) {
        String v = "";
        if (vertice != null) {
            v += creaVertice(coordenada, y, r, vertice.toString());
        }

        if (vertice.hayIzquierdo() && vertice.hayDerecho()) {
            v += dibujaVertices(vertice.izquierdo(), mitad/2, coordenada-mitad, y+100, r);
            v += dibujaVertices(vertice.derecho(), mitad/2, coordenada+mitad, y+100, r);
        } else if (vertice.hayIzquierdo()) {
            v += dibujaVertices(vertice.izquierdo(), mitad/2, coordenada-mitad, y+100, r);
        } else if (vertice.hayDerecho()) {
            v += dibujaVertices(vertice.derecho(), mitad/2, coordenada+mitad, y+100, r);
        }

        return v;
    }

    public String dibujaLineas(VerticeArbolBinario vertice, double mitad, double coordenada,
    double y) {
        String s = "";
        if (vertice != null) {
            if (vertice.hayIzquierdo() && vertice.hayDerecho()) {
                s += creaLinea(coordenada, y, coordenada-mitad, y+100);
                s += creaLinea(coordenada, y, coordenada+mitad, y+100);
                s += dibujaLineas(vertice.izquierdo(), mitad/2, coordenada-mitad, y+100);
                s += dibujaLineas(vertice.derecho(), mitad/2, coordenada+mitad, y+100);
            } else if (vertice.hayIzquierdo()) {
                s += creaLinea(coordenada, y, coordenada-mitad, y+100);
                s += dibujaLineas(vertice.izquierdo(), mitad/2, coordenada-mitad, y+100);
            } else if (vertice.hayDerecho()) {
                s += creaLinea(coordenada, y, coordenada+mitad, y+100);
                s += dibujaLineas(vertice.derecho(), mitad/2, coordenada+mitad, y+100);
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
        int maximo = 0, promedio = 20;
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