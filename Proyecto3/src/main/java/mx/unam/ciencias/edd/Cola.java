package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        // Aquí va su código.
	String cola="";
	if(cabeza==null)
	    return cola;
	Nodo aux=cabeza;
	while(aux!=null){
	    cola+=(aux.elemento + ",");
	    aux=aux.siguiente;
	}
	return cola;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
	if(elemento==null)
	    throw new IllegalArgumentException("Debes dar un elemento");
	Nodo nuevo = new Nodo(elemento);
	if(cabeza==null)
	    cabeza=rabo=nuevo;
	else{
	    rabo.siguiente=nuevo;
	    rabo=nuevo;
	}
	    
    }
}
