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
        String cad = "";
        Nodo aux = cabeza;
        while(aux != null){
            cad += aux.elemento + "\n";
            aux = aux.siguiente;
        }
        return cad;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();

        Nodo aux = new Nodo(elemento);
        if(cabeza == null)
            rabo = aux;
        aux.siguiente = cabeza;
        cabeza = aux;
    }
}
