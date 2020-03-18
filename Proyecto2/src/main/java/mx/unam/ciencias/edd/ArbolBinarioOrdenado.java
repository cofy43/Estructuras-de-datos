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
            while (vertice != null) {
                pila.mete(vertice);
                vertice = vertice.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            if (pila.esVacia())
                return false;
            return true;
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice auxiliar = pila.saca();
            Vertice aux = auxiliar;
            if(aux.hayDerecho()){
                aux = aux.derecho;
                while(aux != null){
                    pila.mete(aux);
                    aux = aux.izquierdo;
                }
            }
            return auxiliar.elemento;
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

        if (raiz == null)
            raiz = ultimoAgregado = nuevoVertice(elemento);
        else
            agrega(raiz, elemento);
        elementos++;
    }

    private void agrega(Vertice vertice, T elemento) {
        if (elemento.compareTo(vertice.get()) < 0)
            if (!vertice.hayIzquierdo()) {
                Vertice verticeNuevo = nuevoVertice(elemento);
                verticeNuevo.padre = vertice;
                vertice.izquierdo = ultimoAgregado = verticeNuevo;
            } else
                agrega(vertice.izquierdo, elemento);
        else
            if (!vertice.hayDerecho()) {
                Vertice verticeNuevo = nuevoVertice(elemento);
                verticeNuevo.padre = vertice;
                vertice.derecho = ultimoAgregado = verticeNuevo;
            } else
                agrega(vertice.derecho, elemento);
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice vertice = (Vertice)busca(elemento);
        if(vertice == null)
            return;
        elementos--;
        if(!vertice.hayDerecho() && !vertice.hayIzquierdo()){
            eliminaVertice(vertice);
            return;
        }
        if(!vertice.hayIzquierdo() && vertice.hayDerecho()){
            eliminaVertice(vertice);
            return;
        }
        if(vertice.hayIzquierdo() && !vertice.hayDerecho()){
            eliminaVertice(vertice);
            return;
        }
        Vertice aux = intercambiaEliminable(vertice);
        vertice.elemento = aux.elemento;
        Vertice padre;
        vertice = aux;
        padre = vertice.padre;
        if(vertice.hayIzquierdo())
            vertice.izquierdo.padre = padre;
        if(padre.izquierdo == vertice)
            padre.izquierdo = vertice.izquierdo;
        else
            padre.derecho = vertice.izquierdo;

        System.out.println(toString());
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
        while(vertice.hayDerecho()){
            vertice = vertice.derecho;
        }
        return vertice;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice v) {
        Vertice vert = v;
        Vertice pv = null;
        if(!vert.hayIzquierdo() && vert.hayDerecho()){
          pv = vert.padre;
          Vertice d = vert.derecho;
          if(pv != null){
            if(pv.izquierdo == vert)
              pv.izquierdo = d;
            else
              pv.derecho = d;
          }else{
            raiz = d;
          }
          d.padre = pv;
          return;
        }
        if(vert.hayIzquierdo() && !vert.hayDerecho()){
          pv = vert.padre;
          Vertice i = vert.izquierdo;
          if(pv != null){
            if(pv.izquierdo == vert)
              pv.izquierdo = i;
            else
              pv.derecho = i;
          }else{
            raiz = i;
          }
          i.padre = pv;
          return;
        }
        if(!vert.hayIzquierdo() && !vert.hayDerecho()){
          pv = vert.padre;
          if(pv == null){
            System.out.println("Es hoja");
            raiz = ultimoAgregado = null;
            return;
          }
          if(pv.izquierdo == vert)
            pv.izquierdo = null;
          if(pv.derecho == vert)
            pv.derecho = null;
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
        Vertice v = (Vertice)vertice;
		if(!v.hayIzquierdo())
		    return;
		Vertice pv = v.padre;
		Vertice vi = v.izquierdo;
		Vertice vd = vi.derecho;
		v.padre = vi;
		vi.derecho = v;
		if(vd != null) {
		    vd.padre = v;
        }
		v.izquierdo = vd;
		vi.padre = pv;
		if(pv == null) {
		    raiz = vi;
        } else  {
            if(pv.izquierdo == v) {
                pv.izquierdo = vi;
            } else {
                pv.derecho = vi;
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

