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
            // Aquí va su código.
	    this.elemento=elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
            // Aquí va su código.
	    return this.padre!=null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            // Aquí va su código.
	    return this.izquierdo!=null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
            // Aquí va su código.
	    return this.derecho!=null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            // Aquí va su código.
	    if(this.padre==null)
		throw new NoSuchElementException("No hay padre");
	    return this.padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            // Aquí va su código.
	    if(this.izquierdo==null)
		throw new NoSuchElementException("No hay izquierdo");
            return this.izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            // Aquí va su código.
	    if(this.derecho==null)
                throw new NoSuchElementException("No hay derecho");
            return this.derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
	    if(this==null)
		return -1;
	    if(this.derecho==null && this.izquierdo==null)
		return 0;
	    if(this.derecho==null)
		return 1 + this.izquierdo.altura();
	    if(this.izquierdo==null)
		return 1 + this.derecho.altura();
	    int iz = 1 + this.izquierdo.altura();
	    int der = 1 + this.derecho.altura();
	    if(iz>der)
		return iz;
	    return der;
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            // Aquí va su código.
	    if(this.padre==null)
		return 0;
	    return 1+this.padre.profundidad();
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            // Aquí va su código.
	    return this.elemento;
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
            // Aquí va su código.
	    return eq(this,vertice);
	}
	private boolean eq(Vertice vertice, Vertice vert){
	    if(vertice == null && vert == null)
		return true;
	    if((vertice==null) || (vert==null))
		return false;
	    if(!vertice.elemento.equals(vert.elemento))
		return false;
	    boolean iz = eq(vertice.izquierdo,vert.izquierdo);
	    boolean der = eq(vertice.derecho,vert.derecho);
	    if(iz && der)
		return true;
	    return false;
	}
  

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            // Aquí va su código.
	    return this.elemento.toString();
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
        // Aquí va su código.
	if(coleccion == null)
	    return;
	for(T n : coleccion)
	    agrega(n);
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
        // Aquí va su código.
	if(raiz==null)
	    return -1;
	return this.raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	VerticeArbolBinario<T> elem = busca(elemento);
	return elem !=null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
	if(raiz==null)
	    return null;
	Cola<Vertice> cola = new Cola<Vertice>();
	cola.mete(raiz);
	while(!(cola.esVacia())){
	    Vertice ver = cola.saca();
	    if(ver.elemento.equals(elemento))
		return ver;
	    if(ver.hayIzquierdo())
		cola.mete(ver.izquierdo);
	    if(ver.hayDerecho())
		cola.mete(ver.derecho);
	}
	return null;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        // Aquí va su código.
	if(raiz==null)
	    throw new NoSuchElementException("El arbol es vacío");
	return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	if(raiz == null)
	    return true;
	return false;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
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
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        // Aquí va su código.
	if(raiz!=null && arbol.raiz!=null)
	    return raiz.equals(arbol.raiz);
	if(raiz==null && arbol.raiz == null)
	    return true;
	return false;
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        // Aquí va su código.
	if(raiz==null)
	    return "";
    
    /*private String toString(Vertice ver,String espacio){
	String arbol = ver.toString()+"\n";
	if(ver.hayIzquierdo() && ver.hayDerecho()){
	    arbol += espacio + "├─›" + toString(ver.izquierdo,espacio+"│   ");
	}else if(ver.hayIzquierdo()){
	    arbol += espacio + "└─›" + toString(ver.izquierdo,espacio+"│   ");
	}
	if(ver.hayDerecho())
	    arbol += espacio +  "└─»"+toString(ver.derecho,espacio+"   ");
	return arbol;
	    
    }*/
	int [] arreglo = new int[(this.raiz.altura()+1)];
        for(int i = 0; i<(this.raiz.altura()+1); i++)
            arreglo[i] = 0;
        return vertices(this.raiz,0,arreglo);
    }
    private String espacios(int l,int [] arreglo){
        String espacios = "";
        for(int i = 0; i<l-1 ;i++)
            if(arreglo[i] == 1)
                espacios += "│  ";
            else
                espacios += "   ";
        return espacios;
    }
    private  String vertices(Vertice v,int l, int [] arreglo){
        String s = v.toString()+"\n";
        arreglo[l] = 1;
        if(v.hayIzquierdo() && v.hayDerecho()){
            s += espacios(l+1,arreglo);
            s += "├─›";
            s += vertices(v.izquierdo,(l+1),arreglo);
            s += espacios(l+1,arreglo);
            s += "└─»";
            arreglo[l] = 0;
            s += vertices(v.derecho,(l+1),arreglo);
        }else if(v.hayIzquierdo()){
            s += espacios(l+1,arreglo);
            s += "└─›";
            arreglo[l] = 0;
            s += vertices(v.izquierdo,(l+1),arreglo);
        }else if(v.hayDerecho()){
            s += espacios(l+1,arreglo);
            s += "└─»";
            arreglo[l] = 0;
            s += vertices(v.derecho,(l+1),arreglo);
        }
        return s;
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
