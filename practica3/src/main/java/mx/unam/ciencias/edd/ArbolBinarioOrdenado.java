package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            pila = new Pila<>();
            Vertice vertice = raiz;
            while (vertice.hayIzquierdo()) {
                pila.mete(vertice);
                vertice = vertice.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (!pila.esVacia());
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice vertice = pila.saca();
            Vertice aux = vertice;
            if (aux.hayDerecho()) {
                aux = aux.derecho;
                while (aux != null) {
                    pila.mete(aux);
                    aux = aux.izquierdo;
                }
            }
            return aux.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) throw new IllegalArgumentException();

        Vertice v = nuevoVertice(elemento);
        if (esVacia()) {
            ultimoAgregado = raiz = v;
            elementos = 1;
            return;
        } else {
            auxAgrega(raiz, v);
            return;
        }
    }

    private void auxAgrega(Vertice raiz, Vertice nuevo) {
        if (raiz.elemento.compareTo(nuevo.elemento) > 0) {
            if (!raiz.hayIzquierdo()) {
                raiz.izquierdo = nuevo;
                nuevo.padre = raiz;
                ultimoAgregado = nuevo;
                elementos ++;
                return;
            }
            auxAgrega(raiz.izquierdo, nuevo);
        } else {
            if (!raiz.hayDerecho()) {
                raiz.derecho = nuevo;
                nuevo.padre = raiz;
                ultimoAgregado  = nuevo;
                elementos ++;
                return;
            } 
            auxAgrega(raiz.derecho, nuevo);
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice vertice = vertice(busca(elemento));

        if (vertice == null)
            return;
        
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice v = vertice.izquierdo;
        while (v.hayDerecho()) {
            v = v.derecho;
        }
        return v;
    }


    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice v = vertice;
        Vertice padreDeV = null;
        if (!v.hayIzquierdo() && v.hayDerecho()) {
            padreDeV = v.padre;
            Vertice vd = v.derecho;
            if (v.hayPadre()) {
                if (padreDeV.izquierdo == v)
                    padreDeV.izquierdo = vd;
                else 
                    padreDeV.derecho  = vd;
            } else {
                raiz = vd;
            }
            vd.padre = padreDeV;
            return;
        }
        if (v.hayIzquierdo() && !v.hayDerecho()) {
            padreDeV = v.padre;
            Vertice vi = v.derecho;
            if (v.hayPadre()) {
                if (padreDeV.izquierdo == v)
                    padreDeV.izquierdo = vi;
                else 
                    padreDeV.derecho  = vi;
            } else {
                raiz = vi;
            }
            vi.padre = padreDeV;
            return;
        }
        if (!v.hayIzquierdo() && !v.hayDerecho()){
            padreDeV = v.padre;
            if (v.hayPadre()) {
                raiz = null;
                return;
            }
            if (padreDeV.izquierdo == v) {
                padreDeV.izquierdo = null;
            } else {
                padreDeV.derecho = null;
            }
            return;
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        if (elemento == null) {
            return null;
        } else {
            return buscar(raiz, elemento);
        }

    }

    private VerticeArbolBinario<T> buscar(Vertice v, T elemento){
        if (v == null) {
            return null;
        }
        if (v.elemento.compareTo(elemento) == 0) {
            return nuevoVertice(v.elemento);
        } else if (v.elemento.compareTo(elemento) > 0) {

            return buscar(v.izquierdo, elemento);
        } else {
            return buscar(v.derecho, elemento);
        }
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if (!vertice.hayIzquierdo()) {
            return;
        }
        Vertice v = vertice(vertice);
        Vertice padreDeV = null;
        if (v.hayPadre())
            padreDeV = v.padre;
        Vertice p = v.izquierdo;
        Vertice q = p.derecho;
        v.padre = p;
        if (q == null)
            q.padre = v;
        v.izquierdo = q;
        if (padreDeV == null)
            raiz = p;
        else {
            if (padreDeV.izquierdo == v)
                padreDeV.izquierdo = p;
            else {
                padreDeV.derecho = p;
            }
        }
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice v = vertice(vertice);
        if (!v.hayDerecho())
            return;
        Vertice padreDeV = null;
        if (v.hayPadre())
            padreDeV = v.padre;
        Vertice q = v.derecho;
        Vertice p = q.izquierdo;
        v.padre = q;
        q.izquierdo = v;
        if (p != null) 
            p.padre = v;
        v.derecho = p;
        q.padre = padreDeV;
        if (padreDeV == null)
            raiz = q;
        else {
            if (padreDeV.izquierdo == v)
                padreDeV.izquierdo = q;
            else 
                padreDeV.derecho = q;
        }
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(accion, raiz);
    }

    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion,
    Vertice v) {
        if (v == null)
            return;
        accion.actua(v);
        dfsPreOrder(accion, v.izquierdo);
        dfsPreOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(accion, raiz);
    }

    private void dfsInOrder(AccionVerticeArbolBinario<T> accion,
    Vertice v) {
        if (v == null)
            return;
        dfsInOrder(accion, v.izquierdo);
        accion.actua(v);
        dfsInOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(accion, raiz);
    }

    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion,
    Vertice v) {
        if (v == null)
            return;
        dfsPostOrder(accion, v.izquierdo);
        dfsPostOrder(accion, v.derecho);
        accion.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}

