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
            Vertice aux = raiz;

            while(aux != null){
                pila.mete(aux);
                aux = aux.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice e = pila.saca();
            Vertice aux = e.derecho;

            while(aux != null){
                pila.mete(aux);
                aux = aux.izquierdo;
            }
            return e.get();
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
        
        if(elemento == null)
            throw new IllegalArgumentException();

        if(raiz == null){
            raiz = nuevoVertice(elemento);
            elementos++;
            ultimoAgregado = raiz;
            return;
        }
        agrega(elemento, raiz);

    }
    /**
     * Función auxiliar a agrega.
     */
    private void agrega(T elemento, Vertice v){
        if(elemento.compareTo(v.elemento) <= 0){
            if(!v.hayIzquierdo()){
                v.izquierdo = nuevoVertice(elemento);
                v.izquierdo.padre = v;
                ultimoAgregado = v.izquierdo;
                elementos++;
                return;
            }
            agrega(elemento, v.izquierdo);
        }
        else{
            if(!v.hayDerecho()){
                v.derecho = nuevoVertice(elemento);
                v.derecho.padre = v;
                ultimoAgregado = v.derecho;
                elementos++;
                return;
            }
            agrega(elemento, v.derecho);
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = (Vertice) busca(elemento);
        if(v == null)
            return;
        elementos--;
        if(!v.hayDerecho() && !v.hayIzquierdo()){
           eliminaVertice(v);
           return;
        }
        //Tiene solo hijo derecho
        if(!v.hayIzquierdo() && v.hayDerecho()){
            eliminaVertice(v);
            return;
        }
        //Tiene solo hijo izquierdo
        if(v.hayIzquierdo() && !v.hayDerecho()){
            eliminaVertice(v);
            return;
        }
        //Tiene ambos hijos
        Vertice aux = intercambiaEliminable(v);
        v.elemento = aux.elemento;
        Vertice p;

        v = aux;
        p = v.padre;
        if(v.hayIzquierdo())
            v.izquierdo.padre = p;
        if(p.izquierdo == v)
            p.izquierdo = v.izquierdo;
        else
            p.derecho = v.izquierdo;
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
        Vertice aux = vertice.izquierdo;
        while(aux.hayDerecho())
            aux = aux.derecho;
        return aux;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice v = vertice;
        Vertice p = null;
        //Tiene solo hijo derecho
        if(!v.hayIzquierdo() && v.hayDerecho()){
            p = v.padre;
            Vertice u = v.derecho;
            if(p != null){
                if(p.izquierdo == v)
                    p.izquierdo = u;
                else 
                    p.derecho = u;    
            }
            else{
                raiz = u;
            }
            u.padre = p;
            return;
        }
        //Tiene solo hijo izquierdo
        if(v.hayIzquierdo() && !v.hayDerecho()){
            p = v.padre;
            Vertice u = v.izquierdo;
            if(p != null){
                if(p.izquierdo == v)
                    p.izquierdo = u;
                else 
                    p.derecho = u;    
            }
            else{
                raiz = u;
            }
            u.padre = p;
            return;
        }
        //No tiene hijos
        if(!v.hayDerecho() && !v.hayIzquierdo()){
            p = v.padre;
            if(p == null){
                raiz = null;
                return;
            }
            if(p.izquierdo == v)
                p.izquierdo = null;
            if(p.derecho == v)
                p.derecho = null;
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
        return busca(elemento, raiz);
    }
    /**
     * Función auxiliar busca
     */
    private Vertice busca(T elemento, Vertice v){
        if(v == null)
            return null;
        Vertice aux = busca(elemento, v.izquierdo);
        if(aux != null)
            return aux;
        if(elemento.equals(v.get()))
            return v;
        return busca(elemento, v.derecho);
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
        
        Vertice q = (Vertice)vertice;
        if(!q.hayIzquierdo()) return;

        Vertice s = q.padre;
        
        Vertice p = q.izquierdo;
        q.padre = p;
        Vertice aux = p.derecho;
        p.derecho = q;

        if(aux != null)
            aux.padre = q;
        q.izquierdo = aux;    

        p.padre = s;
        if(s == null){
            raiz = p;
        }
        else{
            if(s.izquierdo == q)
                s.izquierdo = p;
            else
                s.derecho = p;
        }

    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        
        Vertice q = (Vertice)vertice;
        if(!q.hayDerecho()) return;

        Vertice s = q.padre;
        
        Vertice p = q.derecho;
        q.padre = p;
        Vertice aux = p.izquierdo;
        p.izquierdo = q;

        if(aux != null)
            aux.padre = q;
        q.derecho = aux;    

        p.padre = s;
        if(s == null){
            raiz = p;
        }
        else{
            if(s.izquierdo == q)
                s.izquierdo = p;
            else
                s.derecho = p;
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
    /**
     * Método auxiliar
     */
    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if(v == null)
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
    /**
     * Método auxiliar
     */
    private void dfsInOrder(AccionVerticeArbolBinario<T> accion,Vertice v) {
        if(v == null)
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
    /**
     * Método auxiliar
     */
    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if(v == null)
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
