package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a operaciones
 * mete/saca/mira.
 */
public abstract class MeteSaca<T> {

    /**
     * Clase interna protegida para nodos.
     */
    protected class Nodo {
        /** El elemento del nodo. */
        public T elemento;
        /** El siguiente nodo. */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            // Aquí va su código.
	    this.elemento=elemento;
        }
    }

    /** La cabeza de la estructura. */
    protected Nodo cabeza;
    /** El rabo de la estructura. */
    protected Nodo rabo;
    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        // Aquí va su código.
	if(cabeza==null)
	    throw new NoSuchElementException("No hay elementos");
	Nodo guarda=new Nodo(cabeza.elemento);
	if(cabeza.siguiente==null){
	    cabeza=rabo=null;
	}else{
	    cabeza=cabeza.siguiente;
	    }
	return guarda.elemento;
    }
	
    /**
     * Nos permite ver el elemento en un extremo de la estructura, sin sacarlo
     * de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {
        // Aquí va su código.
	if(cabeza==null)
            throw new NoSuchElementException("No hay elementos");
	return cabeza.elemento;
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <tt>true</tt> si la estructura no tiene elementos,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
	return cabeza==null;
    }

    /**
     * Compara la estructura con un objeto.
     * @param object el objeto con el que queremos comparar la estructura.
     * @return <code>true</code> si el objeto recibido es una instancia de la
     *         misma clase que la estructura, y sus elementos son iguales en el
     *         mismo orden; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        @SuppressWarnings("unchecked") MeteSaca<T> m = (MeteSaca<T>)object;
        // Aquí va su código.
	if((cabeza==null && m.cabeza!=null)||(cabeza!=null&& m.cabeza==null))
	    return false;
	Nodo aux=cabeza;
	Nodo mn=m.cabeza;
	while(aux!=null && mn!=null){
	    if(!(aux.elemento.equals(mn.elemento)))
		return false;
	    if((aux.siguiente!=null && mn.siguiente==null) ||(aux.siguiente==null && mn.siguiente!=null))
	       return false;
	    aux=aux.siguiente;
	    mn=mn.siguiente;
	}
	return true;
    }
}
