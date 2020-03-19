package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            for (int i  = 0; i < entradas.length; i++) {
                if (entradas[i] != null) {
                    indice = i;
                    iterador = entradas[i].iterator();
                    return;
                }
            }
            iterador = null;
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return (iterador != null);
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {

            if (iterador == null) {
                throw new IllegalArgumentException();
            }

            Entrada temp = iterador.next();
            boolean encontrado = true;

            if (!iterador.hasNext()) {
                for (int i = (indice + 1); i < entradas.length; i++) {
                    if (entradas[i] != null) {
                        indice = i;
                        iterador = entradas[i].iterator();
                        encontrado = false;
                        break;
                    }
                }
                if (encontrado) {
                    iterador = null;
                }

            }

            return temp;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return super.siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return super.siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    //private Lista<Entrada>[] entradas;
    public Lista<Entrada>[] entradas;
    /* Número de valores. */
    //private int elementos;
    public int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor;

        if (capacidad < this.MINIMA_CAPACIDAD) {
            capacidad = this.MINIMA_CAPACIDAD;
        }

        int potencia = 1;
        while (potencia < (2*capacidad)) {
            potencia *= 2;
        }

        capacidad = potencia;
        entradas = nuevoArreglo(capacidad);
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if (llave == null || valor == null) {
            throw new IllegalArgumentException();
        }

        int i = (dispersor.dispersa(llave) & entradas.length-1);
        if (entradas[i] == null) {
            Lista<Entrada> l = new Lista<>();
            l.agrega(new Entrada(llave, valor));
            entradas[i] = l;
            elementos ++;
        } else {
            boolean noRepetida = true;
            for (Entrada e: entradas[i]) {
                if (e.llave.equals(llave)) {
                    noRepetida = false;
                    e.valor = valor;
                }
            }

            if (noRepetida) {
                entradas[i].agrega(new Entrada(llave, valor));
                elementos ++;
            }
        }

        if (carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] nuevaEntradas = nuevoArreglo(entradas.length*2);
            for (int j = 0; j < entradas.length; j++) {
                if (entradas[j] != null) {

                    for (Entrada e: entradas[j]) {

                        int id = (dispersor.dispersa(e.llave) & nuevaEntradas.length-1);
                        if (nuevaEntradas[id] == null) {
                            Lista<Entrada> l = new Lista<>();
                            l.agrega(new Entrada(e.llave, e.valor));
                            nuevaEntradas[id] = l;
                        } else {
                            boolean noRepetida = true;
                            for (Entrada en : nuevaEntradas[id]) {
                                if (en.llave.equals(e.llave)) {
                                    en.valor = e.valor;
                                    noRepetida = false;
                                }
                            }
                            if (noRepetida) {
                                nuevaEntradas[id].agrega(new Entrada(e.llave, e.valor));
                            }
                        }
                    }
                }
            }
            entradas = nuevaEntradas;
        }
        
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if (llave == null) {
            throw new IllegalArgumentException();
        }

        int id = (dispersor.dispersa(llave) & entradas.length-1);
        if (entradas[id] == null) {
            throw new NoSuchElementException();
        } else {
            for (Entrada e: entradas[id]) {
                if (e.llave.equals((llave))) {
                    return e.valor;
                }
            }
            throw new NoSuchElementException();
        }
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
        if (llave == null) {
            return false;
        }
        int id = (dispersor.dispersa(llave) & entradas.length-1);
        if (entradas[id] == null) {
            return false;
        } else {
            for (Entrada e: entradas[id]) {
                if (e.llave.equals((llave))) {
                    return true;
                }
            }
            return false;
        }

    } 

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        if (llave == null) {
            throw new IllegalArgumentException();
        }
        int id = (dispersor.dispersa(llave) & entradas.length-1);
        if (entradas[id] == null) {
            throw new NoSuchElementException();
        } else {
            boolean llaveEncontrada = true;
            for (Entrada e : entradas[id]) {
                if (e.llave.equals(llave)) {
                    entradas[id].elimina(e);
                    llaveEncontrada = false;
                    if (entradas[id].esVacia()) {
                        entradas[id] = null;
                    }
                    elementos --;
                }
            }

            if (llaveEncontrada) {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        if (esVacia()) {
            return 0;
        }

        int suma = 0;
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                suma += entradas[i].getLongitud();
            }
        }
        return suma -1;
     }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        if (esVacia()) {
            return 0;
        }
        int colision = 0;
        int colisionMaxima = 0;
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                colision = entradas[i].getElementos() - 1;
                if (colision > colisionMaxima)
                    colisionMaxima = colision;
            }
        }
        return colisionMaxima;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return (double)elementos / (entradas.length);
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return this.elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return elementos == 0;
    }
        
    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        Lista<Entrada>[] nuevoArreglo = nuevoArreglo(entradas.length);
        entradas = nuevoArreglo;
        elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        if (esVacia()) {
            return "{}";
        }
        String s = "{ ";
        Iterador it = new Iterador();
        while (it.hasNext()) {
            Entrada e = it.siguiente();
            s += "\'" + e.llave + "\': " + "\'" + e.valor + "\', ";
        }
        return s + "}";
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        
        if (getElementos() != d.getElementos()) {
            return false;
        }

        for (Entrada e : getElements()) {
            if (!d.contiene(e.llave)) {
                return false;
            }
        }

        return true;
    }

    private Lista<Entrada> getElements() {
        Lista<Entrada> ll = new Lista<>();
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                for (Entrada e : entradas[i]) {
                    ll.agrega(e);
                }
            }
        }
        return ll;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
