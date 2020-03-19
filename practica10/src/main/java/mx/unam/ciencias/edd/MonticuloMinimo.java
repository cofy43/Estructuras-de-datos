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
        private int indice = 0;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (this.indice < elementos);
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (indice >= elementos)
                throw new NoSuchElementException();
            return arbol[indice++];
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
            this.indice = 0;
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
            return (adaptador.elemento.compareTo(this.elemento));
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos = 0;
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
        elementos = n;
        arbol = nuevoArreglo(elementos);
        int i = 0;
        for (T e : iterable) {
            arbol[i] = e;
            arbol[i].setIndice(i);
            ++i;
        }
        for (int j = (elementos - 1) / 2; j >= 0; j--) {
            acomodoHaciaAbajo(j);
        }
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (this.elementos == arbol.length) {
            T [] temp = nuevoArreglo(arbol.length*2);
            for (int i = 0; i < arbol.length; i++) {
                temp[i] = arbol[i];
            }
            this.arbol = temp;
        }

        arbol[elementos] = elemento;
        arbol[elementos].setIndice(elementos);
        acomodoHaciaArriba(elementos++);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if (elementos == 0)
            throw new IllegalStateException();
        
        T aux = arbol[0];

        intercambia(aux, arbol[--elementos]);

        arbol[elementos].setIndice(-1);
        arbol[elementos] = null;

        acomodoHaciaAbajo(0);

        return aux;
    }

    private void intercambia(T a, T b) {
        int i_a = a.getIndice();
        int i_b = b.getIndice();
        arbol[i_a] = b;
        arbol[i_b] = a;

        arbol[i_a].setIndice(i_a);
        arbol[i_b].setIndice(i_b);
    }

    private void acomodoHaciaArriba(int i) {
        int padre = (i - 1) / 2;
        int menor = i;

        if (padre >= 0 && arbol[padre].compareTo(arbol[i]) > 0)
            menor = padre;

        if (menor != i) {
            T aux = arbol[i];

            arbol[i] = arbol[padre];
            arbol[i].setIndice(i);

            arbol[padre] = aux;
            arbol[padre].setIndice(padre);

            acomodoHaciaArriba(menor);
        }
    }

    private void acomodoHaciaAbajo(int i) {
        int izq = i * 2 + 1;
        int der = i * 2 + 2;

        if (izq >= elementos && der >= elementos) {
            return;
        }

        int menor = getMenor(izq, der);
        menor = getMenor(i, menor);

        if (menor != i) {
            T aux = arbol[i];

            arbol[i] = arbol[menor];
            arbol[i].setIndice(i);

            arbol[menor] = aux;
            arbol[menor].setIndice(menor);

            acomodoHaciaAbajo(menor);
        }

    }

    private int getMenor(int a, int b) {
        if (b >= elementos) {
            System.out.println(a);
            return a;
        } else if (arbol[a].compareTo(arbol[b]) < 0) {
            return a;
        }else {
            return b;
        }
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= this.elementos || elemento == null)
            return;
            
        int i = elemento.getIndice();
        T a = arbol[i], b = arbol[--elementos];
        arbol[i] = b;
        arbol[i].setIndice(a.getIndice());
        arbol[elementos] = a;
        arbol[elementos].setIndice(b.getIndice());

        arbol[elementos].setIndice(-1);
        arbol[elementos] = null;
        reordena(arbol[i]);

    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= elementos) 
            return false;
        return true;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        return (elementos == 0);
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0;
        for (int i = 0; i < elementos; i++) {
            arbol[i] = null;
        }
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if (elemento == null) {
            return;
        }
        int i = elemento.getIndice();
        acomodoHaciaArriba(i);
        acomodoHaciaAbajo(i);
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
        if (i < 0 || i >= elementos)
            throw new NoSuchElementException();
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String s = "";

        for (int i = 0; i < getElementos(); i++) {
            s += String.valueOf(get(i).toString()) + ", ";
        }
        return s;
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
        if (this.elementos != monticulo.elementos)
            return false;
        for (int i = 0; i < getElementos(); i++) {
            if (arbol[i].compareTo(monticulo.get(i)) != 0)
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

        for (T t: coleccion) {
            l1.agrega(new Adaptador<T>(t));
        }

        Lista<T> l2 = new Lista<>();

        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<>(l1);

        int i = 0;
        while (!monticulo.esVacia()) {
            Adaptador<T> aux = monticulo.get(i);
            monticulo.elimina();
            l2.agrega(aux.elemento);
        }

        //Esto esta muy puerco
        return l2.reversa();
        
    }
}
