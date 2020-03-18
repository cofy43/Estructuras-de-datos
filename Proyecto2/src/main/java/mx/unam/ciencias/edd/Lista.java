package mx.unam.ciencias.edd;

import java.awt.List;
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
            this.anterior = null;
            this.siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (siguiente != null);
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return (anterior != null);
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }else {
                siguiente = anterior;
                anterior = siguiente.anterior;
                return siguiente.elemento;
            }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
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
        return (longitud == 0);
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
        agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if  (elemento == null) {
            throw new IllegalArgumentException();
        }else if (esVacia()) {
            Nodo aux = new Nodo(elemento);
            cabeza = rabo = aux;
            longitud ++;
        }else {
            Nodo aux = new Nodo(elemento);
            rabo.siguiente = aux;
            aux.anterior = rabo;
            rabo = aux;
            longitud ++;
        }
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if  (elemento == null) {
            throw new IllegalArgumentException();
        }else if (esVacia()) {
            Nodo aux = new Nodo(elemento);
            cabeza = rabo = aux;
            longitud ++;
        }else {
            Nodo aux = new Nodo(elemento);
            cabeza.anterior = aux;
            aux.siguiente = cabeza;
            cabeza = aux;
            longitud ++;
        }
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
        if (elemento == null) {
            throw new IllegalArgumentException();
        }else if (i <= 0) {
            agregaInicio(elemento);
        }else if (i >= longitud) {
            agregaFinal(elemento);
        }else {
            Nodo aux = cabeza;
            for (int j = 0; j < i ; j++) {
                aux = aux.siguiente;
            }
            Nodo aInsertar = new Nodo(elemento);
            aInsertar.anterior = aux.anterior;
            aux.anterior.siguiente = aInsertar;
            aInsertar.siguiente = aux;
            aux.anterior = aInsertar;
            longitud ++;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (buscaNodo(elemento) != null) {
            Nodo n = buscaNodo(elemento);
            if (n.anterior == null) {
                eliminaPrimero();
            }else if (n.siguiente == null) {
                eliminaUltimo();
            }else {
                n.anterior.siguiente = n.siguiente;
                n.siguiente.anterior = n.anterior;
                longitud --;
            }

        }
    }

    /**
     *Método auxiliar que dado un elemento, hace una busqueda en 
     *una lista y regresa el nodo con el elemento a buscar.
     *@param T elemento a buscar.
     *@return El nodo con el elemento a buscar.
     */
    private Nodo buscaNodo(T elemento) {
        Nodo n = new Nodo (elemento);
        Nodo aux = cabeza;
        while (aux != null) {
            if (aux.elemento.equals(n.elemento)) {
                return aux;
            }
            aux = aux.siguiente;
        }
        return aux;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (esVacia()) {
            throw new NoSuchElementException();
        }else if (cabeza == rabo) {
            T aux = cabeza.elemento;
            cabeza = rabo = null;
            longitud --;
            return aux;
        }else {
            T aux = cabeza.elemento;
            cabeza.siguiente.anterior = null;
            cabeza = cabeza.siguiente;
            longitud --;
            return aux;
        }
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (esVacia()){
            throw new NoSuchElementException();
        }else if(cabeza == rabo) {
            T aux = cabeza.elemento;
            cabeza = rabo = null;
            longitud --;
            return aux;
        }else {
            T aux = rabo.elemento;
            rabo.anterior.siguiente = null;
            rabo = rabo.anterior;
            longitud --;
            return aux;
        }
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (buscaNodo(elemento) != null) {
            return true;
        }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> lista =  new Lista<>();
        Nodo aux = cabeza;
        while (aux != null) {
            lista.agregaInicio(aux.elemento);
            aux = aux.siguiente;
        }
        return lista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista =  new Lista<>();
        Nodo aux = cabeza;
        while (aux != null) {
            lista.agregaFinal(aux.elemento);
            aux = aux.siguiente;
        }
        return lista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = rabo = null;
        longitud =0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (esVacia()) {
            throw new NoSuchElementException();
        }else {
            return cabeza.elemento;
        }
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (esVacia()) {
            throw new NoSuchElementException();
        }else {
            return rabo.elemento;
        }
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= longitud) {
            throw new ExcepcionIndiceInvalido();
        }
        Nodo aux = cabeza;
        for (int j = 0; j < i ; j++) {
            aux = aux.siguiente;
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
        int indice = 0;
        Nodo aux = cabeza;
        while (aux != null) {
            if (aux.elemento.equals(elemento)) {
                return indice;
            }
            aux = aux.siguiente;
            indice ++;
        }
        indice = -1;
        return indice;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (!esVacia()) {
            Nodo n = cabeza;
            String representación = "[";
            while (n != rabo) {
                representación += String.valueOf(n.elemento) + ", ";
                n = n.siguiente;
            }
            representación += String.valueOf(n.elemento);
            return representación + "]";
        }
        return "[]";
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

        if (longitud == lista.getLongitud()) {
            Nodo aux = cabeza;
            int i = 0;
            while (aux != null) {
                if (!aux.elemento.equals(lista.get(i)))
                    return false;
                aux = aux.siguiente;
                 i++;
            }
            return true;
        }
        return false;
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
        Lista<T> listaOrdenada =  copia();
        if (listaOrdenada.longitud <= 1) {
            return listaOrdenada;
        }
        Lista<T> l1 = new Lista<T>();
        Lista<T> l2 = new Lista<T>();
        int mitad = (int) Math.floor(listaOrdenada.getLongitud() / 2);
        for (int i = 0; i < mitad; i++)
            l1.agrega(listaOrdenada.get(i));
        for (int j = mitad; j < listaOrdenada.longitud; j++)
            l2.agrega(listaOrdenada.get(j));
        
        l1 = l1.mergeSort(comparador);
        l2 = l2.mergeSort(comparador);
        return merge(l1, l2, comparador);
    }

    private Lista<T> merge(Lista<T> l1, Lista<T> l2, Comparator<T> comparador) {
        Lista<T> list = new Lista<T>();
        Nodo i = l1.cabeza, j = l2.cabeza;
        while (i != null  && j != null) {
            if (comparador.compare(i.elemento, j.elemento) <= 0) {
                list.agrega(i.elemento);
                i = i.siguiente;
            }else {
                list.agrega(j.elemento);
                j = j.siguiente;
            }
        }
        if (i == null) {
            while (j != null) {
                list.agrega(j.elemento);
                j = j.siguiente;
            }
        }else {
            while (i != null) {
                list.agrega(i.elemento);
                i = i.siguiente;
            }
        }
        return list;
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
        return (lista.mergeSort((a, b) -> a.compareTo(b)));
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
        while (aux != null) {
            if(comparador.compare(aux.elemento, elemento) == 0)
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