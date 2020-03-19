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
            this.altura = 0;
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
            return this.elemento + " " + this.altura + "/" + balance(this);
        }

        private boolean equals(VerticeAVL v1, VerticeAVL v2) {
            if (v1 == null && v2 == null) {
                return true;
            }
            if ((v1 == null && v2 != null) || (v1 != null && v2 == null) || !v1.elemento.equals(v2.elemento) || v1.altura != v2.altura) {
                return false;
            }
            return equals(verticeAVL(v1.izquierdo), verticeAVL(v2.izquierdo)) && equals(verticeAVL(v1.derecho), verticeAVL(v2.derecho));
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
            
            return equals(this, vertice);
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

    private int balance(VerticeAVL vertice) {
        return (vertice == null) ? 0 : this.getAltura(verticeAVL(vertice.izquierdo)) - this.getAltura(verticeAVL(vertice.derecho));
    }

    private int getAltura(VerticeArbolBinario<T> vertice) {
        return (vertice == null) ? -1 : verticeAVL(vertice).altura;
    }
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        VerticeAVL vertice;
        super.agrega(elemento);
        vertice = verticeAVL(super.ultimoAgregado);
        this.rebalanceo(vertice);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL eliminar = verticeAVL(super.busca(elemento)), aux;
        if (eliminar == null) {
            return;
        }
        if (eliminar.hayIzquierdo()) {
            aux = this.verticeAVL(maximoEnSubarbol(eliminar.izquierdo));
            eliminar.elemento = aux.elemento;
            eliminar = aux;
        }
        if (!eliminar.hayIzquierdo() && !eliminar.hayDerecho()) {
            this.eliminaHoja(eliminar);
        } else {
            this.subirUnicoHijo(eliminar);
        }
        this.rebalanceo(verticeAVL(eliminar.padre));
    }

    private Vertice maximoEnSubarbol(Vertice vertice) {
        while(vertice.hayDerecho()) {
            vertice = vertice.derecho;
        }
        return vertice;
    }


    private void subirUnicoHijo(Vertice padre) {
        if (!padre.hayIzquierdo()) {
            this.eliminaSinHijoIzquierdo(padre);
        } else {
            this.eliminaSinHijoDerecho(padre);
        }
    }

    private void eliminaSinHijoDerecho(Vertice eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = this.raiz.izquierdo;
            eliminar.izquierdo.padre = null;
        } else {
            eliminar.izquierdo.padre = eliminar.padre;
            if (this.esHijoIzquierdo(eliminar)) {
                eliminar.padre.izquierdo = eliminar.izquierdo;
            } else {
                eliminar.padre.derecho = eliminar.izquierdo;
            }
        }
        this.elementos--;
    }

    private void eliminaSinHijoIzquierdo(Vertice eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = this.raiz.derecho;
            eliminar.derecho.padre = null;
        } else {
            eliminar.derecho.padre = eliminar.padre;
            if (this.esHijoIzquierdo(eliminar)) {
                eliminar.padre.izquierdo = eliminar.derecho;
            } else {
                eliminar.padre.derecho = eliminar.derecho;
            }
        }
        this.elementos--;
    }

    private void eliminaHoja(Vertice eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = null;
            this.ultimoAgregado = null;
        } else if (this.esHijoIzquierdo(eliminar)) {
            eliminar.padre.izquierdo = null;
        } else {
            eliminar.padre.derecho = null;
        }
        this.elementos--;
    }

    private boolean esHijoDerecho(Vertice v) {
        if (!v.hayPadre()) {
            return false;
        }
        return v.padre.derecho == v;
    }

    private boolean esHijoIzquierdo(Vertice v) {
        if (!v.hayPadre()) {
            return false;
        }
        return v.padre.izquierdo == v;
    }

    private void rebalanceo(VerticeAVL vertice) {
        VerticeAVL d, i;
        if (vertice == null) {
            return;
        }
        this.alturaVertice(vertice);
        if (this.balance(vertice) == -2) {
            if (this.balance(verticeAVL(vertice.derecho)) == 1) {
                d = verticeAVL(vertice.derecho);
                super.giraDerecha(d);
                this.alturaVertice(d);
                this.alturaVertice(verticeAVL(d.padre));
            }
            super.giraIzquierda(vertice);
            this.alturaVertice(vertice);
        } else if (this.balance(vertice) == 2) {
            if (this.balance(verticeAVL(vertice.izquierdo)) == -1) {
                i = verticeAVL(vertice.izquierdo);
                super.giraIzquierda(i);
                this.alturaVertice(i);
                this.alturaVertice(verticeAVL(i.padre));
            }
            super.giraDerecha(vertice);
            this.alturaVertice(vertice);
        }
        this.rebalanceo(verticeAVL(vertice.padre));
    }

    private void alturaVertice(VerticeAVL vertice) {
        if (vertice == null) {
            return;
        }
        vertice.altura =  Math.max(this.getAltura(vertice.izquierdo), this.getAltura(vertice.derecho)) + 1;
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