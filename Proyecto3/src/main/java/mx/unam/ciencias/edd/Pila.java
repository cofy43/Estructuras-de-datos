package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        // Aquí va su código.
	String pila="";
	if(cabeza==null)
	    return pila;
	Nodo aux=cabeza;
	while(aux!=null){
	    pila+=(aux.elemento + "\n");
	    aux=aux.siguiente;
	}
	return pila;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
	if(elemento==null)
	    throw new IllegalArgumentException("Debes pasar un elemento");
	Nodo nuevo=new Nodo(elemento);
	nuevo.siguiente=cabeza;
	cabeza=nuevo;
    }
}
