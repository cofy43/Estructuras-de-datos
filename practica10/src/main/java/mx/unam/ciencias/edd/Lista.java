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
            // Aquí va su código.
	    this.elemento=elemento;
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
            // Aquí va su código.
	    this.anterior=null;
	    this.siguiente=cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return this.siguiente!=null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
	    if(hasNext()){
		    this.anterior=this.siguiente;
		    this.siguiente=this.siguiente.siguiente;
		    return this.anterior.elemento;
	    }else{
		throw new NoSuchElementException("No hay nodo siguiente");
	    }
        
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
	    return this.anterior!=null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
	    if(hasPrevious()){
		this.siguiente=this.anterior;
		this.anterior=this.anterior.anterior;
		return this.siguiente.elemento;
	    }else{
		throw new NoSuchElementException("No hay nodo anterior");
	    }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
	    this.anterior=null;
	    this.siguiente=cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
	    this.anterior=rabo;
	    this.siguiente=null;
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
        // Aquí va su código.
	return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	return rabo==null;
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
        // Aquí va su código.
	if(elemento==null)                                                                                                                    
	    throw new IllegalArgumentException("Debes de dar un elemento");
	longitud++;
	Nodo nuevo = new Nodo(elemento);                                                                                           
            if(rabo==null){
		cabeza=rabo=nuevo;
	    }else{
		rabo.siguiente=nuevo;
		nuevo.anterior=rabo;
		rabo=nuevo;
	    }
	
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
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
        // Aquí va su código.
	if(elemento==null)
	    throw new IllegalArgumentException("Debes de dar un elemento");
	longitud++;
	Nodo nuevo = new Nodo(elemento);
	if(rabo==null){
	    cabeza=rabo=nuevo;
	}else{
	    cabeza.anterior=nuevo;
	    nuevo.siguiente=cabeza;
	    cabeza=nuevo;
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
        // Aquí va su código.
	if(elemento==null)
	    throw new IllegalArgumentException("No hay elemento");
	if((longitud-1)<i){
	    agrega(elemento);
	    return;
	}else if(i<1){
	    agregaInicio(elemento);
	    return;
	}else if(0<i && i<=(longitud-1)){
	    longitud++;
	    Nodo nuevo = new Nodo(elemento);
	    Nodo n =buscaNodo(i);
	    Nodo a =n.anterior;
	    nuevo.anterior=a;
	    a.siguiente=nuevo;
	    nuevo.siguiente=n;
	    n.anterior=nuevo;
	    return;
	}
    }
    private Nodo buscaNodo(int i){
	int a=i;
	Nodo s=cabeza;
	for(int j=0; j<a;j++){
	    s=s.siguiente;
	}
	return s;
    }
    private Nodo buscaNodo(T elemento){
	Nodo n =cabeza;
	while(n!=null){
	    if(n.elemento.equals(elemento) || n.elemento==elemento){
		return n;
	    }
	    n=n.siguiente;
	}
	return n;
    }
    
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	Nodo n = buscaNodo(elemento);
	if(n==null){
	    return;
	}else if(cabeza==rabo){
	    longitud--;
	    cabeza=rabo=null;
	}else if(n==cabeza){
	    longitud--;
	    Nodo s=n.siguiente;
	    s.anterior=null;
	    cabeza=s;
	}else if(n==rabo){
	    longitud--;
	    Nodo a =n.anterior;
	    a.siguiente=null;
	    rabo=a;
	}else if(n!=cabeza && n!=rabo){
	    longitud--;
	    Nodo a=n.anterior;
	    Nodo s=n.siguiente;
	    a.siguiente=s;
	    s.anterior=a;
	}
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
	if(rabo==null)
	    throw new NoSuchElementException("La lista es vacía");
	Nodo salva=new Nodo(cabeza.elemento);
	if(cabeza==rabo){
	    longitud--;
	    cabeza=rabo=null;
	    return salva.elemento;
	}else if(cabeza!=rabo){
	    longitud--;
	    Nodo s =cabeza.siguiente;
	    s.anterior=null;
	    cabeza=s;
	    return salva.elemento;
	}
	return salva.elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
	if(rabo==null)                                                                                                                              
            throw new NoSuchElementException("La lista es vacía");
	Nodo salva=new Nodo(rabo.elemento);
	if(cabeza==rabo){
	    longitud--;    
	    cabeza=rabo=null;                                                                                                              
	    return salva.elemento;                                                                                                     
	}else if(cabeza!=rabo){
	    longitud--;                                                                                      
	    Nodo a =rabo.anterior;                                                                                                               
	    a.siguiente=null;                                                                                                                    
	    rabo=a;
	    return salva.elemento;
	}
	return salva.elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	Nodo n=buscaNodo(elemento);
	return n!=null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
	Lista<T> reversa = new Lista<T>();
	if(rabo==null){
	    return reversa;
	}else{
	    reversa.agregaInicio(rabo.elemento);
	    Nodo aux=rabo.anterior;
	    while(aux!=null){
		reversa.agrega(aux.elemento);
		aux=aux.anterior;
	    }
	    return reversa;
	}
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
	Lista<T> copia = new Lista<T>();
	if(rabo==null){
	    return copia;
	}else{
	    copia.agregaInicio(cabeza.elemento);
	    Nodo aux=cabeza.siguiente;
	    while(aux!=null){
		copia.agrega(aux.elemento);
		aux=aux.siguiente;
		
	    }
	    return copia;
	}
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
	cabeza=rabo=null;
	longitud=0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
	if(rabo==null)
	    throw new NoSuchElementException("La lista es vacía");
	    return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
	if(rabo==null)
	    throw new NoSuchElementException("La lista es vacía");
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
        // Aquí va su código.
	if(i<0 || i>=longitud)
	    throw new ExcepcionIndiceInvalido("No se tiene ese indice o quiere el primero o quiere el último");
	Nodo n=buscaNodo(i);
	return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
	int i = 0;
	Nodo aux=cabeza;
	while(aux!=null){
	    if(aux.elemento.equals(elemento)){
		return i;
	    }else{
		i++;
	    }
	    aux=aux.siguiente;
	}
	return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
	if(cabeza==null)
	    return "[]";
	String lista ="[";
	Nodo aux=cabeza;
	while(aux!=null){
	    if(aux.siguiente!=null){
		lista +=  (aux.elemento + ", ");
	    }else{
		lista+=(aux.elemento+"]");
	    }
	    aux=aux.siguiente;
	}
	return lista;
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
        // Aquí va su código.
	if(cabeza!=null && lista.cabeza!=null &&(!lista.cabeza.elemento.equals(cabeza.elemento) || !lista.rabo.elemento.equals(rabo.elemento))){
	    return false;
	}else if(longitud!=lista.longitud){
	    return false;
	}
	boolean mismo=true;
	Nodo aux=cabeza;
	Nodo list=lista.cabeza;
	while(aux!=null){
	    if(!list.elemento.equals(aux.elemento)){
		mismo=false;
	    }
	    aux=aux.siguiente;
	    list=list.siguiente;
	}
	return mismo;
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
        // Aquí va su código.
	Lista<T> lista=copia();
	return mS(comparador,longitud,lista);
    }
    private Lista<T> mS(Comparator<T> comp, int lon,Lista<T> lista){
	if(lon<2)
	    return lista;
	int med = lon/2;
	Lista<T> list1 = new Lista<T>();
	Lista<T> list2 = new Lista<T>();
	Nodo aux=lista.cabeza;
	for(int i = 0;i<med;i++){
	    list1.agrega(aux.elemento);
	    aux = aux.siguiente;
	}
	while(aux!=null){
	    list2.agrega(aux.elemento);
	    aux=aux.siguiente;
	}
	Lista<T> listiz = mS(comp,list1.longitud,list1);
	Lista<T> listder = mS(comp,list2.longitud,list2);
	Lista<T> r=mezcla(comp,listiz,listder);
	//System.out.println(".......................");
	//System.out.println(lista);
	//System.out.println(r);
	return r;
    }
    private Lista<T> mezcla(Comparator<T> comp,Lista<T> list1,Lista<T> list2){
	Lista<T> orden =new Lista<T>();
	Nodo aux = list1.cabeza;
	Nodo aux1 = list2.cabeza;
	while(aux!=null && aux1!=null)
	    if(comp.compare(aux.elemento,aux1.elemento)>0){
		orden.agrega(aux1.elemento);
		aux1 = aux1.siguiente;
	    }else{
		orden.agrega(aux.elemento);
		aux = aux.siguiente;
	    }
	
	    while(aux1!=null){
		orden.agrega(aux1.elemento);
		aux1 = aux1.siguiente;
	    }
	
	    while(aux!=null){
		orden.agrega(aux.elemento);
		aux= aux.siguiente;
	    }
	
	return orden;
	
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
        // Aquí va su código.
	return contiene(elemento);
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