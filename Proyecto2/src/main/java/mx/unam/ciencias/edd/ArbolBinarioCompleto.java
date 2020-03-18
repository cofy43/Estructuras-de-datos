package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<ArbolBinario<T>.Vertice>();
            if(raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (!cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice aux = cola.saca();
            if(aux.izquierdo != null)
                cola.mete(aux.izquierdo);
            if(aux.derecho != null)
                cola.mete(aux.derecho);
            return aux.get();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

     /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */

    @Override public void agrega(T elemento) {
        if (elemento == null) throw new IllegalArgumentException();

        Vertice v = nuevoVertice(elemento);
        elementos ++;
        if (raiz == null) {
            raiz = v;
            return;
        } else {
            Cola<Vertice> cola = new Cola<>();
            cola.mete(raiz);
            Vertice aux = null;
            while (!cola.esVacia()) {
                aux = cola.saca();
                if (!aux.hayIzquierdo() || !aux.hayDerecho()) {
                    v.padre = aux;
                    if (!aux.hayIzquierdo())
                        aux.izquierdo = v;
                    else if (!aux.hayDerecho())
                        aux.derecho = v;        
                    break;
                }
                cola.mete(aux.izquierdo);
                cola.mete(aux.derecho);
            }
        }
        
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        //Si el elemento no se encuentra terminamos
        Vertice v = vertice(super.busca(elemento));
        if (v == null) {
            return;
        }
        //Si se encuentra decrementamos el contador de elementos
        elementos --;
        if (elementos == 0) {
            raiz = null;
            return;
        }
        Cola<ArbolBinario<T>.Vertice> cola = new Cola<>();
        cola.mete(raiz);
        Vertice u = null;
        while (!cola.esVacia()) {
            u = cola.saca();
            if (u.hayIzquierdo())
                cola.mete(u.izquierdo);
            if (u.hayDerecho())
                cola.mete(u.derecho);
        }
        v.elemento = u.elemento;
        Vertice padreDeU = u.padre;
        if (padreDeU.izquierdo == u) 
            padreDeU.izquierdo = null;
        else 
            padreDeU.derecho = null;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if (raiz == null) 
            return -1;
        int altura=(int)Math.floor(Math.log(elementos)/Math.log(2));
        return altura;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (raiz == null) {
            return;
        }
        Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
        while (cola.esVacia()) {
            Vertice v =cola.saca();
            bfs(accion);
            if (v.hayIzquierdo()) {
                cola.mete(v.izquierdo);
            }
            if (v.hayDerecho()) {
                cola.mete(v.derecho);
            }
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
