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
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
           return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
        	int i = hayIzquierdo() ? izquierdo.altura() : -1;
        	int d = hayDerecho() ? derecho.altura() : -1;
        	return elemento.toString() + " " + altura + "/" + (i-d);  
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
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalanceo((VerticeAVL)ultimoAgregado);
    }

    /** Método de rebalanceo */
    private void rebalanceo(VerticeAVL v){
    	if(v == null)
    		return;
    	actualizaAltura(v);

    	VerticeAVL p = (VerticeAVL) v.izquierdo;
    	VerticeAVL q = (VerticeAVL) v.derecho;	
    	
    	if(balance(v) == -2){
    		if(balance(q) == 1){
    			super.giraDerecha(q);
    			actualizaAltura(q);
    			actualizaAltura((VerticeAVL) q.padre);
    			q = (VerticeAVL) v.derecho;
    		}
    		super.giraIzquierda(v);
    		actualizaAltura(v);
    		actualizaAltura((VerticeAVL) v.padre);

    	}
    	else if(balance(v) == 2){
    		if(balance(p) == -1){
    			super.giraIzquierda(p);
    			actualizaAltura(p);
    			actualizaAltura((VerticeAVL) p.padre);
    			p = (VerticeAVL) v.izquierdo;
    		}
    		super.giraDerecha(v);
    		actualizaAltura(v);
    		actualizaAltura((VerticeAVL) v.padre);

    	}

    	rebalanceo((VerticeAVL) v.padre);

    }

    private void actualizaAltura(VerticeAVL v){

    	VerticeAVL p = (VerticeAVL)v.izquierdo;
    	VerticeAVL q = (VerticeAVL)v.derecho;

		int i = v.hayIzquierdo() ? v.izquierdo.altura() : -1; 
    	int d = v.hayDerecho() ? v.derecho.altura() : -1; 
    	v.altura = 1 + Math.max(i,d);    	
    }

    /** Método para calcular balance */
    private int balance(VerticeAVL v){

    	VerticeAVL p = (VerticeAVL)v.izquierdo;
    	VerticeAVL q = (VerticeAVL)v.derecho;	

    	int i = v.hayIzquierdo() ? v.izquierdo.altura() : -1; 
    	int d = v.hayDerecho() ? v.derecho.altura() : -1;
    	return (i-d);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL v = (VerticeAVL) busca(elemento);

        if(v == null)
        	return;

        if(v.hayIzquierdo()){
        	VerticeAVL aux = v;
        	v = (VerticeAVL) super.intercambiaEliminable(v);
        	aux.elemento = v.elemento;
        }

        VerticeAVL aux = (VerticeAVL) v.padre;
        super.eliminaVertice(v);

        rebalanceo(aux);
        elementos--;

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
