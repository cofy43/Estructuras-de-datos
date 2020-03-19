package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
            return (padre != null);
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return (izquierdo != null);
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return (derecho != null);
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if (!hayPadre()) {
                throw new NoSuchElementException();
            }
            return padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if (!hayIzquierdo()) {
                throw new NoSuchElementException();
            }
            return izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if (!hayDerecho()) {
                throw new NoSuchElementException();
            }
            return derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            if (raiz == null) {
                return -1;
            }
            else if (!hayDerecho() && !hayIzquierdo()) {
                return 0;
            }
            else {
                if (hayIzquierdo()) {
                    if (!hayDerecho()) {
                        return 1 + (izquierdo.altura());
                    }
                    return 1 + maximo(izquierdo.altura(),derecho.altura());
                }else {
                    return 1 + (derecho.altura());
                }
            }
        }

        /**
         * Función auxiliar que devuelve el número mayor
         * @param int a entero a comparar
         * @param int b entero a comparar
         * @return int c entero mayor.
         */
        private int maximo(int a, int b){
            if (a <= b) {
                return b;
            }else {
                return a;
            }
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            if (padre == null) {
                return 0;
            }else {
                return (1 + padre.profundidad());
            }
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return elemento;    
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            if(!elemento.equals(vertice.elemento))
                return false;
            if((!hayIzquierdo() && vertice.hayIzquierdo()) ||
            (hayIzquierdo() && !vertice.hayIzquierdo()))
                return false;
            if((!hayDerecho() && vertice.hayDerecho()) || (hayDerecho() &&
            !vertice.hayDerecho()))
                return false;
            if((!hayIzquierdo() && !vertice.hayIzquierdo()) &&
            (!hayDerecho() && !vertice.hayDerecho()))
                return true;
            boolean vi = true;
            boolean vd = true;
            if(hayIzquierdo())
                vi = izquierdo.equals(vertice.izquierdo);
            if(hayDerecho())
                vd = derecho.equals(vertice.derecho);
            return (vi && vd);
        }
        /**

         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            String concatenacion = "";
            if (padre == null)
                return concatenacion;
            else 
                concatenacion += String.valueOf(elemento) +"\n";
            return concatenacion;
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for (T elemento : coleccion) {
            agrega(elemento);
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        if (raiz == null) {
            return -1;
        }else {
            return  raiz.altura();
        }
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return (busca(elemento) != null);
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        if (esVacia())
            return null;
        if (raiz.elemento.equals(elemento))
            return raiz;
        Vertice v = nuevoVertice(elemento);
        return busqueda(raiz, v);
        
    }

    private Vertice busqueda(Vertice v1, Vertice v2) {
        if (v1 == null) 
            return null;
        if (v1.elemento.equals(v2.elemento))
            return v1;
        Vertice temp = busqueda(v1.izquierdo, v2);
        if (temp != null)
            return temp;
        temp = busqueda(v1.derecho, v2);
        return temp;
        
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if (esVacia()) 
            throw new NoSuchElementException();
        return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return (raiz == null);        
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;

        if(raiz == null)
            return (arbol.raiz == null);
        return raiz.equals(arbol.raiz);
        
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if (esVacia())
            return "";
        Vertice aux = raiz;
        String concatenacion = aux.elemento + "\n";
        if (aux.hayIzquierdo()) {
            if (!aux.hayDerecho()) {
                concatenacion += representacion("", "└─›", aux.izquierdo);
            } else {
                concatenacion += representacion("", "├─›", aux.izquierdo);
                concatenacion += representacion("", "└─»", aux.derecho);
            }
        } else {
            if (aux.hayDerecho())
                concatenacion += representacion("", "└─»", aux.derecho);
        }
        return concatenacion;
    }

    private String representacion(String espacio, String direccion, Vertice v) {
        String cadena = espacio + direccion + v.toString() + "\n";
        if (v.hayIzquierdo()) {
            if (!v.hayDerecho()) {
                cadena += representacion((espacio + "|  "), "└─›", v.izquierdo);
            } else {
                cadena += representacion((espacio + "|  "), "├─›", v.izquierdo);
                cadena += representacion((espacio + "|  "), "└─»", v.derecho);
            }
        } else if (v.hayDerecho()) {
            cadena += representacion((espacio + "  "), "└─»", v.derecho);
        }
        return cadena;
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
