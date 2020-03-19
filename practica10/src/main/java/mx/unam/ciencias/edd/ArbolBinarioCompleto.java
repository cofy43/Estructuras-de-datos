package mx.unam.ciencias.edd;
import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
	    cola = new Cola<Vertice>();
	    if(raiz==null)
		return;
	    cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return !(cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
	    Vertice sig = cola.mira();
	    if(sig.hayIzquierdo())
		cola.mete(sig.izquierdo);
	    if(sig.hayDerecho())
		cola.mete(sig.derecho);
	    return cola.saca().elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if(elemento==null)
	    throw new IllegalArgumentException("Debes dar un valor");
	elementos ++;
	Vertice ver = nuevoVertice(elemento);
	    if(raiz==null){
		raiz = ver;
		return;
	    }
	    Cola<Vertice> col = new Cola<Vertice>();
	    col.mete(raiz);
	    while(!(col.esVacia())){
		Vertice aux = col.saca();
		if(aux.izquierdo==null){
		    aux.izquierdo = ver;
		    ver.padre = aux;
		    break;
		}
		if(aux.derecho==null){
		    aux.derecho = ver;
		    ver.padre = aux;
		    break;
		}
		col.mete(aux.izquierdo);
		col.mete(aux.derecho);
	    }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if(elemento==null)
	    return;
	if(raiz==null)
	    return;
	Vertice eliminar = super.vertice(super.busca(elemento));
	if(eliminar==null)
	    return;
	elementos --;
	if(elementos==0){
	    super.limpia();
	    return;
	}
	Vertice ultimo = inter(eliminar);
	Vertice padre = ultimo.padre;
	if(padre.izquierdo.equals(ultimo))
	    padre.izquierdo = null;
	else
	    padre.derecho = null;
	ultimo = null;
	
    }
    private Vertice inter(Vertice elimin){
	Vertice ult = ult();
	T aux = elimin.elemento;
	elimin.elemento = ult.elemento;
	ult.elemento = aux;
	return ult;
	    
    }
    private Vertice ult(){
	Cola<Vertice> col = new Cola<Vertice>();
	col.mete(raiz);
	Vertice aux = raiz;
	while(!col.esVacia()){
	    aux = col.saca();
	    if(aux.hayIzquierdo())
		col.mete(aux.izquierdo);
	    if(aux.hayDerecho())
		col.mete(aux.derecho);
	}
	return aux;
    }
    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
	if(raiz==null)
	    return -1;
	return (int)( Math.floor(Math.log(elementos)/Math.log(2)));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	if(raiz==null)
	    return ;
	Cola<Vertice> col = new Cola<Vertice>();
	while(!col.esVacia()){
	    Vertice aux = col.saca();
	    accion.actua(aux);
	    if(aux.hayIzquierdo())
		col.mete(aux.izquierdo);
	    if(aux.hayDerecho())
		col.mete(aux.derecho);
	}
	    
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
