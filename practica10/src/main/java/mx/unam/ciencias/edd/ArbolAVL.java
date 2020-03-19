package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
	    super(elemento);
	    altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
	    return super.altura();
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
	    return elemento.toString() + " " + Integer.toString(altura) + "/" + Integer.toString(balance(this));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            // Aquí va su código.
	    return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /* Convierte el vértice a VerticeAVL */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
	return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	VerticeAVL ver = verticeAVL(ultimoAgregado.padre);
	balancea(ver);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	Vertice eliminar = vertice(super.busca(elemento));
	if(eliminar==null)
	    return;
	elementos--;
	if(elementos==0){
	    super.limpia();
	    return;
	}
	VerticeAVL ver = verticeAVL(null);
	if(eliminar.hayIzquierdo() && eliminar.hayDerecho()){
            Vertice max = intercambiaEliminable(eliminar);
	    ver = verticeAVL(max.padre);
            eliminaVertice(max);
        }else{
	    ver =verticeAVL(eliminar.padre);
            eliminaVertice(eliminar);
        }
	if(ver==null)
	    return;
	balancea(ver);
    }
    private void balancea(VerticeAVL ver){
	if(ver == null)
	    return;
	ver.altura = altura(ver);
	if(balance(ver)==-2){
	    VerticeAVL der = verticeAVL(ver.derecho);
	    if(balance(der)==1){
		super.giraDerecha(der);
		der.altura = altura(der);
		ver.altura = altura(ver);
		der = verticeAVL(ver.derecho);
	    }
	    super.giraIzquierda(ver);
	    der.altura = altura(der);
	    ver.altura = altura(ver);
	}	
	if(balance(ver)==2){
	    VerticeAVL iz = verticeAVL(ver.izquierdo);
	    if(balance(iz)==-1){
		super.giraIzquierda(iz);
		iz.altura = altura(iz);
		ver.altura = altura(ver);
		iz = verticeAVL(ver.izquierdo);
	    }
	    super.giraDerecha(ver);
	    iz.altura = altura(iz);
	    ver.altura = altura(ver);
	}
	if(ver.padre==null)
	    return;
	balancea(verticeAVL(ver.padre));
	
    }
    private int altura(VerticeAVL ver){
	return ver.altura();
    }
    private int balance(VerticeAVL ver){
	if(ver.izquierdo==null && ver.derecho==null)
            return 0;
        VerticeAVL derecho = verticeAVL(ver.derecho);
	VerticeAVL izquierdo = verticeAVL(ver.izquierdo);
	if(izquierdo==null)
            return ((-1)-derecho.altura);
	if(derecho==null)
            return (izquierdo.altura+1);
	return (izquierdo.altura-derecho.altura);
    }
	   
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}