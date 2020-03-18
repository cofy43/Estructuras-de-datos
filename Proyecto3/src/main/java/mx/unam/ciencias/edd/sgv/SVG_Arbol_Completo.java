package mx.unam.ciencias.edd.sgv;

import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Coleccion;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.ValorIndexable;
import mx.unam.ciencias.edd.VerticeArbolBinario;


/**
 * Clase encargada de generar el código svg de la estructura
 * árbol binario completo
 * @author Martin Felipe Espinal Cruces
 */
public class SVG_Arbol_Completo {
    static final String INICIO = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ETIQUETA_G = "<g>\n";
    static final String CIERRA_ETIQUETA_G = "</g>\n";

    /**
     * Método principal que se encarga de generar y regresar
     * la codiificación a traves de la entrada estandar
     * @param coleccion Coleccion con la que se generara la estructura 
     * de datos
     */
    public void creaSVGArbolCompleto(Coleccion<Integer> estructura, int indicador) {

        
        ArbolBinarioCompleto<Integer> arbol; 
        if (indicador == 1) {
            arbol = new ArbolBinarioCompleto<>();
            Lista<ValorIndexable<Integer>> le = new Lista<>();
            estructura.forEach(e -> le.agrega(new ValorIndexable<Integer>(e, Integer.parseInt(e.toString()))));
            MonticuloMinimo<ValorIndexable<Integer>> minHeap = new MonticuloMinimo<>(le);
            minHeap.forEach(e -> arbol.agrega(e.getElemento())); 
        } else {
            arbol = new ArbolBinarioCompleto<>(estructura);
        }
        String svg = INICIO;
        int profundidad = arbol.altura() == 0 ? 1 : arbol.altura();
        int radio = calculaRadio(arbol);
        int mitad = (int) (Math.pow(2, profundidad) * radio*2);
        int height = 100 + profundidad*100;
        svg += String.format("<svg height=\"%d\" width=\"%d\">\n", height, mitad);
        svg += ETIQUETA_G;

        if (!arbol.esVacia()) {
            svg += dibujaLineas(arbol.raiz(), (mitad/2)/2, mitad/2, 50);
            svg += dibujaVertices(arbol.raiz(), (mitad/2)/2, mitad/2, 50, radio);
        }

        svg += CIERRA_ETIQUETA_G;
        svg += "</svg>\n";
        System.out.println(svg);
        
    }

    /**
     * Método encargado de generar los vértices del árbol al recorrerlo
     * por medio de sus hijos calculando sus nuevas coordeadas
     * @param vertice Vétice del árbol por el cula se dibujará el vértice en svg
     * @param mitad Variable para generar las coordenadas de los vértices hijos
     * del vértice introducido
     * @param coordenada Variable para posicionar el vértice en svg.
     * @return v Representación en cadena del código en svg de cadena
     */
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

    /**
     * Método encargado de generar las aristas del árbol al recorrerlo
     * por medio de sus hijos calculando sus nuevas coordeadas
     * @param vertice Vétice del árbol por el cula se dibujará el vértice en svg
     * @param mitad Variable para generar las coordenadas de los vértices hijos
     * del vértice introducido
     * @param coordenada Variable para posicionar el vértice en svg.
     * @return v Representación en cadena del código en svg de cadena
     */
    public String dibujaLineas(VerticeArbolBinario vertice, double mitad, double coordenada,
    double y) {
        String s = "";

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

        return s;
    }

    /**
     * Método encargado de generar las etiquetas de svg correspoondiente de los vértices
     * @param x coodenada x de la etiqueta
     * @param y coodenada y de la etiqueta
     * @param r radio del vértice de la etiqueta
     * @param elemento representación en cadena del elemento
     * @param b representación en cadena del balance del vértice
     */
    public String creaVertice(double x, double y, double r, String elemento) {
        String texto, vertice;

        vertice = String.format("<circle cx=\"%f\" cy=\"%f\" r=\"%f\" fill=\"white\" stroke=\"black\" stroke-width=\"2\"/>\n", x, y, r);

        texto = String.format("<text x=\"%f\" y=\"%f\" fill=\"black\" font-famili=\"sans-serif\" font-size=\"20\" text-anchor=\"middle\">%s</text>\n", x, y+4, elemento);

        return vertice + texto;
    }

    /**
     * Método encargado de generar las etiquetas de svg correspoondiente de las aristas
     * @param x1 coodenada x de la etiqueta
     * @param x2 coodenada x de la etiqueta
     * @param y1 coodenada y de la etiqueta
     * @param y2 coodenada y de la etiqueta
     * @return Representación en cadena de la etiqueta
     */
    public String creaLinea(double x1, double y1, double x2, double y2) {
        return String.format("<line stroke=\"black\" stroke-width=\"2\" x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\"/>\n", x1, y1, x2, y2);
    }

    /**
     * Método encargado de calcular el radio del vértice svg en base
     * a la longitud mayor de los elementos
     * @return promedio Radio del vértice
     */
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