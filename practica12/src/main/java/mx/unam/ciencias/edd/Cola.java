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
        String cad = "";
        Nodo aux = cabeza;
        while(aux != null){
            cad += aux.elemento + ",";
            aux = aux.siguiente;
        }
        return cad;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        Nodo aux = new Nodo(elemento);
        if(cabeza == null){
            cabeza = aux;
            rabo = aux;
        }
        else{
            rabo.siguiente = aux;
            rabo = aux;
        }
    }
}
