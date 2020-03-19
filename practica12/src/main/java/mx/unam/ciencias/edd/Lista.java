package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            
            if(elemento == null) 
                throw new IllegalArgumentException();

            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            if(siguiente != null)
                return true;
            return false;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            
            if(!hasNext())
                throw new NoSuchElementException();
            anterior = siguiente;
            siguiente = siguiente.siguiente; 
            return anterior.elemento; 
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            if(anterior != null)
                return true;
            return false;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(!hasPrevious())
                throw new NoSuchElementException();
            siguiente = anterior;
            anterior = anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            siguiente = cabeza;
            anterior = cabeza.anterior;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            if(rabo != null)
                siguiente = rabo.siguiente;
            anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return (cabeza == null); 
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        if(cabeza == null){
            cabeza = new Nodo(elemento);
            rabo = cabeza;
        }
        else{
            rabo.siguiente = new Nodo(elemento);
            rabo.siguiente.anterior = rabo;
            rabo = rabo.siguiente;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        if(!esVacia()){
            Nodo aux = new Nodo(elemento);
            aux.siguiente = cabeza;
            cabeza.anterior = aux;
            cabeza = aux;
        }
        else {
            cabeza = new Nodo(elemento);
            rabo = cabeza;
        }
        longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        if(i <= 0)
            agregaInicio(elemento);
        else if(i >= longitud){
            agregaFinal(elemento);
        }
        else {

            int j = 0;
            Nodo aux = cabeza;
            while(j < i-1){
                aux = aux.siguiente;
                j++;
            }
            Nodo nuevo = new Nodo(elemento);
            nuevo.siguiente = aux.siguiente;
            nuevo.anterior = aux;
            aux.siguiente = nuevo;
            nuevo.siguiente.anterior = nuevo;
            longitud++;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo aux = cabeza;
        while(aux != null && !aux.elemento.equals(elemento))
            aux = aux.siguiente;
        if(aux != null){
            if(aux == cabeza) 
                eliminaPrimero();
            else if(aux == rabo)
                eliminaUltimo();
            else {
                aux.anterior.siguiente = aux.siguiente;
                aux.siguiente.anterior = aux.anterior;
                longitud--;
            }
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(esVacia())
            throw new NoSuchElementException();
        Nodo aux = cabeza;
        if(cabeza == rabo){
            cabeza = null;
            rabo = null;
        }
        else {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }
        longitud--;
        return aux.elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(esVacia())
            throw new NoSuchElementException();
        Nodo aux = rabo;
        if(cabeza == rabo){
            cabeza = null;
            rabo = null;
        }
        else {
            rabo = rabo.anterior;
            rabo.siguiente = null;
        }
        longitud--;
        return aux.elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Nodo aux = cabeza;
        while(aux != null){
            if(aux.elemento.equals(elemento))
                return true;
            aux = aux.siguiente;
        }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> rev = new Lista<T>();
        Nodo aux = rabo;
        while(aux != null){
            rev.agrega(aux.elemento);
            aux = aux.anterior;
        }
        return rev;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> rev = new Lista<T>();
        Nodo aux = cabeza;
        while(aux != null){
            rev.agrega(aux.elemento);
            aux = aux.siguiente;
        }
        return rev;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(esVacia())
            throw new NoSuchElementException();
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if(esVacia())
            throw new NoSuchElementException();
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if(i < 0 || i >= longitud)
            throw new ExcepcionIndiceInvalido();
        int j = 0;
        Nodo aux = cabeza;
        while(j < i){
            aux = aux.siguiente;
            j++;
        }
        return aux.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo aux = cabeza;
        int i = 0;
        while(aux != null && !aux.elemento.equals(elemento)){
            aux = aux.siguiente;
            i++;
        }
        return (aux == null ? -1 : i);
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        String rep = "[";
        Nodo aux = cabeza;
        while(aux != null){
            if(aux != cabeza){
                rep += ", ";
            }
            rep += aux.elemento;
            aux = aux.siguiente;
        }
        rep += "]";
        return rep;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(lista.getLongitud() != longitud)
            return false;
        Nodo aux = cabeza;
        int i = 0;
        while(aux != null){
            if(!aux.elemento.equals(lista.get(i)))
                return false;
            aux = aux.siguiente;
            i++;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        
        if(getLongitud() <= 1)
            return copia();

        Lista<T> l = new Lista<T>();
        Lista<T> r = new Lista<T>();

        int m = getLongitud()/2;
        for(int i = 0; i < m; i++)
            l.agrega(get(i));
        for(int i = m; i < getLongitud(); i++)
            r.agrega(get(i)); 

        l = l.mergeSort(comparador);
        r = r.mergeSort(comparador);

        return mezcla(l, r, comparador);

    }

    /**
     * Regresa la mezcla ordenada de dos listas ordenadas.
     * @param l la lista izquierda a comparar.
     * @param r la lista derecha a comparar.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una lista que es la mezcla de ambas.
     */
    private Lista<T> mezcla(Lista<T> l, Lista<T> r, Comparator<T> comparador){

        Nodo auxl = l.cabeza;
        Nodo auxr = r.cabeza;
        Lista<T> res = new Lista<T>();

        while(auxl != null && auxr != null){
            if(comparador.compare(auxl.elemento, auxr.elemento) <= 0){
                res.agrega(auxl.elemento);
                auxl = auxl.siguiente;
            }
            else{
                res.agrega(auxr.elemento);
                auxr = auxr.siguiente;
            }
        }

        while(auxl != null){
            res.agrega(auxl.elemento);
                auxl = auxl.siguiente;
        }
        while(auxr != null){
                res.agrega(auxr.elemento);
                auxr = auxr.siguiente;
        }
        return res;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo aux = cabeza;
        while(aux != null){
            if(comparador.compare(elemento, aux.elemento) == 0)
                return true;
            aux = aux.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
