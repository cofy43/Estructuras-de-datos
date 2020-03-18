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
            // Aquí va su código.
	    pila = new Pila<Vertice>();
	    Vertice aux = raiz;
	    while(aux!=null){
		pila.mete(aux);
		aux=aux.izquierdo;
	    }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
	    Vertice sig = pila.saca();
	    if(sig.hayDerecho()){
		Vertice aux = sig.derecho;
		while(aux!=null){
		    pila.mete(aux);
		    aux = aux.izquierdo;
		}
	    }
	    return sig.elemento;
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
        // Aquí va su código.
	if(elemento == null)
	    throw new IllegalArgumentException("Debes dar un elemento");
	elementos ++;
	Vertice nuevo = nuevoVertice(elemento);
	ultimoAgregado = nuevo;
	if(raiz== null)
	    raiz = nuevo;
	else
	    verAgrega(raiz,nuevo);
    }
    private void verAgrega(Vertice ver, Vertice nuevo){
	if(nuevo.elemento.compareTo(ver.elemento)<=0){
	    if(ver.izquierdo==null){
		ver.izquierdo = nuevo;
		nuevo.padre = ver;
	    }else
		verAgrega(ver.izquierdo,nuevo);
	}else
	    if(ver.derecho==null){
		ver.derecho = nuevo;
		nuevo.padre = ver;
	    }else
		verAgrega(ver.derecho,nuevo);
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if(elemento==null)
	    return;
	if(raiz==null)
	    return;
	Vertice eliminar = super.vertice(busca(elemento));
	if(eliminar==null)
	    return;
	elementos--;
	if(elementos==0){
	    super.limpia();
	    return;
	}
	if(eliminar.hayIzquierdo() && eliminar.hayDerecho()){
	    Vertice max = intercambiaEliminable(eliminar);
	    eliminaVertice(max);
	    return;
	}else{  
	    eliminaVertice(eliminar);
	}
    }
    private Vertice maximo(Vertice vertice){
	if(vertice.derecho==null)
	    return vertice;
	return maximo(vertice.derecho);
    }
    private void inter(Vertice eliminar,Vertice ultimo){
        T aux = eliminar.elemento;
        eliminar.elemento = ultimo.elemento;
        ultimo.elemento = aux;
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
        // Aquí va su código.
	Vertice maximo = maximo(vertice.izquierdo);
	inter(vertice,maximo);
	return maximo;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice ver) {
        // Aquí va su código.
	Vertice aux = raiz;
        if(ver.hayIzquierdo()){
            aux = ver.izquierdo;
	    aux.padre = ver.padre;
	    if(aux.padre == null)
		raiz = aux;
	    else{
		if(aux.padre.izquierdo==ver)
		    aux.padre.izquierdo = aux;
		else
		    aux.padre.derecho = aux;
		
	    }
	    ver = null;
	    return;
	}
	if(ver.hayDerecho()){
	    aux = ver.derecho;
	    aux.padre = ver.padre;
	    if(aux.padre == null)
		raiz = aux;
	    else{
		if(aux.padre.izquierdo==ver)
		    aux.padre.izquierdo= aux;
		else
		    aux.padre.derecho =aux;
		
	    }   
	    ver= null;
	    return;
	}
	if(ver.padre.derecho == ver)
	    ver.padre.derecho = null;
	else
	    ver.padre.izquierdo = null;
	ver = null;
    }
    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
	if(elemento==null)
	    return null;
	if(raiz==null)
	    return null;
	return busca(raiz,elemento);
    }
    public VerticeArbolBinario<T> busca(Vertice ver,T elemento) {
	if(ver.elemento.equals(elemento))
            return ver;
	if(elemento.compareTo(ver.elemento)<0)
	    if(!ver.hayIzquierdo())
		return null;
	    else
		return busca(ver.izquierdo,elemento);
	if(!ver.hayDerecho())
	    return null;
	return busca(ver.derecho,elemento);
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
        // Aquí va su código.
	return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	if(vertice==null || !vertice(vertice).hayIzquierdo())
	    return;
	Vertice ver = super.vertice(vertice);
	Vertice p = ver.izquierdo;
	if(ver==raiz ){
	    p.padre = null;
	    raiz = p;
	    
	}else{
	    Vertice padre = ver.padre;
	    p.padre = padre;
	    if(padre.izquierdo==ver)
		padre.izquierdo = p;
	    else
		padre.derecho = p;
	}
	ver.izquierdo = p.derecho;
	if(ver.hayIzquierdo())
	    ver.izquierdo.padre = ver; 
	p.derecho = ver;
	ver.padre = p;
	    
	  
    }


    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	if(vertice==null || !vertice(vertice).hayDerecho())
            return;
        Vertice ver = super.vertice(vertice);
        Vertice p = ver.derecho;
        if(ver==raiz ){
            p.padre = null;
            raiz = p;

        }else{
            Vertice padre = ver.padre;
            p.padre = padre;
            if(padre.izquierdo==ver)
                padre.izquierdo = p;
            else
                padre.derecho = p;
        }
	ver.derecho = p.izquierdo;
	if(ver.hayDerecho())
	    ver.derecho.padre = ver;
	p.izquierdo = ver;
	ver.padre = p;

    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	if(raiz==null)
	    return;
	pre(raiz,accion);
    }
    private void pre(Vertice v,AccionVerticeArbolBinario<T> accion){
	if(v==null)
	    return;
	accion.actua(v);
	pre(v.izquierdo,accion);
	pre(v.derecho,accion);
    }
    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	if(raiz==null)
	    return;
	in(raiz,accion);
	}
    private void in(Vertice v,AccionVerticeArbolBinario<T> accion){
	if(v==null)
            return;
	in(v.izquierdo,accion);
	accion.actua(v);
	in(v.derecho,accion);
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
    private void post(Vertice v,AccionVerticeArbolBinario<T> accion){
	if(v==null)
	    return;
	post(v.izquierdo,accion);
	post(v.derecho,accion);
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

