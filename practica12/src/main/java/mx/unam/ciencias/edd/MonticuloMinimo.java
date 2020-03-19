package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (indice < getElementos() && arbol[indice] != null);
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(hasNext())    
                return arbol[indice++];
            throw new NoSuchElementException();
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        
        arbol = nuevoArreglo(n);        
        int i = 0;
        for(T e : iterable){
            arbol[i] = e;
            arbol[i].setIndice(i); 
            i++;
        }
        elementos = n;

        for(int j = n/2; j >= 0; j--){
            acomodaAbajo(j);
        }
    }

    /**
     * Funcion auxiliar para saber si es mayor que algún hijo.
     */
    private boolean hijoEsMenor(int i){
        
        int a = 2*i + 1, b = 2*i + 2;
        if(b < getElementos() && arbol[b] != null && arbol[b].compareTo(arbol[i]) < 0)
            return true;
        if(a < getElementos() && arbol[a] != null && arbol[a].compareTo(arbol[i]) < 0)
            return true;
        return false;

    }     

    /**
     * Funcion auxiliar para intercambiar con hijo menor
     */
    private int hijoMenor(int i){

        int a = 2*i + 1, b = 2*i + 2;
        if(a >= getElementos())
            return -1;
        if(b >= getElementos())
            return a;

        return (arbol[a].compareTo(arbol[b]) < 0) ? a : b ;
        
    }

    /**
     * Acomoda hacia abajo;
     */
    private void acomodaAbajo(int i){
        
        if(i >= getElementos() || i < 0)
            return;

        if(arbol[i] == null)
            return;

        if(!hijoEsMenor(i))
            return;
        
        int p = hijoMenor(i);

        if(p == -1)
            return;

        T aux = arbol[i];

        arbol[i] = arbol[p];
        arbol[i].setIndice(i);

        arbol[p] = aux;
        arbol[p].setIndice(p);

        acomodaAbajo(p);
    }


    /**
     * Función auxiliar indica si el padre es mayor
     */
    private boolean padreEsMayor(int i){
        
        if(i == 0)
            return false;

        int j = (i-1)/2;
        return (arbol[j].compareTo(arbol[i]) > 0);
    }

    /**
     * Acomoda hacia arriba.
     */
    private void acomodaArriba(int i){

        if(i < 0)
            return;

        if(arbol[i] == null)
            return;

        if(!padreEsMayor(i))
            return;
        int j = (i-1)/2;

        T aux = arbol[i];

        arbol[i] = arbol[j];
        arbol[i].setIndice(i);

        arbol[j] = aux;
        arbol[j].setIndice(j);

        acomodaArriba(j);

    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {

        if(getElementos() == arbol.length){
            T[] _arbol = nuevoArreglo(arbol.length * 2);
            for (int i = 0; i < arbol.length; i++) {
                _arbol[i] = arbol[i];
            }
            arbol = _arbol;
        }

        arbol[getElementos()] = elemento;
        arbol[getElementos()].setIndice(getElementos());
        
        acomodaArriba(elemento.getIndice());

        elementos++;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(esVacia())
            throw new IllegalStateException();
        T aux = arbol[0];

        arbol[0] = arbol[getElementos()-1];
        arbol[0].setIndice(0);

        arbol[getElementos()-1] = null;
        elementos--;

        acomodaAbajo(0);
        
        aux.setIndice(-1);

        return aux;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        
        if(elemento == null)
            return;

        int i = elemento.getIndice();
        int u = getElementos()-1;
        if(i < 0 || i >= getElementos())
            return;

        T aux = arbol[i];

        arbol[i] = arbol[u];
        arbol[i].setIndice(i);
        arbol[u] = null;
        elementos--;

        reordena(arbol[i]);

        aux.setIndice(-1);

    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {

        int i = elemento.getIndice();
        if(i < 0 || i >= getElementos())
            return false;
        return true;

    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        return (getElementos() == 0);
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        int n = getElementos();
        for(int i = 0; i < n; i++)
            arbol[i] = null;
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(elemento == null)
            return;
        acomodaArriba(elemento.getIndice());
        acomodaAbajo(elemento.getIndice());
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i < 0 || i >= getElementos())
            throw new NoSuchElementException();
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {

        String cad = "";
        for (T  e : arbol) {
            cad += e.toString() + ", ";
        }
        return cad;

    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
         
        if(getElementos() != monticulo.getElementos())
            return false;
        
        for (int i = 0; i < getElementos(); i++) {
            if(!arbol[i].equals(monticulo.get(i)))
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        
        Lista<Adaptador<T>> l1 = new Lista<>();
        for (T e : coleccion){
            Adaptador<T> aux = new Adaptador<>(e);
            l1.agrega(aux);
        }

        MonticuloMinimo<Adaptador<T>> m = new MonticuloMinimo<>(l1);
       
        Lista<T> l2 = new Lista<>();
        while(!m.esVacia()){
            Adaptador<T> aux = m.elimina();
            l2.agrega(aux.elemento);
        }
        return l2;   

    }
}
