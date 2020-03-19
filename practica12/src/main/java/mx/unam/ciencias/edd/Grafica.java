package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Diccionario<T,Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if(vertice.distancia == this.distancia)
                return 0;
            if(vertice.distancia > this.distancia)
                return -1;
            else 
                return 1;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return this.vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return this.vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return this.vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Diccionario<T,Vertice>();
        aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        if(contiene(elemento))
            throw new IllegalArgumentException();
        vertices.agrega(elemento ,new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        if(a.equals(b) || sonVecinos(a,b))
            throw new IllegalArgumentException();

        if(!vertices.contiene(a) || !vertices.contiene(b)) 
            throw new NoSuchElementException();

        va.vecinos.agrega(b, new Vecino(vb, 1.0));
        vb.vecinos.agrega(a, new Vecino(va, 1.0));
        aristas++;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        if(a.equals(b) || sonVecinos(a,b) || peso <= 0.0)
            throw new IllegalArgumentException();

        if(!vertices.contiene(a) || !vertices.contiene(b)) 
            throw new NoSuchElementException();

        va.vecinos.agrega(b, new Vecino(vb, peso));
        vb.vecinos.agrega(a, new Vecino(va, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        if(!sonVecinos(a,b) || a.equals(b))
            throw new IllegalArgumentException();

        Vecino v1 = null, v2 = null;

        for(Vecino e : va.vecinos){
            if(e.vecino.equals(vb))
                v1 = e;
        }
        for(Vecino e : vb.vecinos){
            if(e.vecino.equals(va))
                v2 = e;
        }

        va.vecinos.elimina(v1.get());
        vb.vecinos.elimina(v2.get());
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice e : vertices){
            if(e.get().equals(elemento))
                return true;
        }
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice v = (Vertice) vertice(elemento);

        for (Vertice e : vertices) {
            for (Vecino u : e.vecinos) {
                if (u.vecino.equals(v)) {
                    e.vecinos.elimina(u.get());
                    aristas--;
                }
            }

        }
        vertices.elimina(elemento);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        for(Vecino e : va.vecinos){
            if(e.vecino.get().equals(b))
                return true;
        }
        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        if(!sonVecinos(a,b))
            throw new IllegalArgumentException();

        for(Vecino e : va.vecinos){
            if(e.vecino.equals(vb))
                return e.peso;
        }
        return 1;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        if(!sonVecinos(a,b) || peso <= 0.0)
            throw new IllegalArgumentException();

        desconecta(a,b);
        conecta(a, b, peso);
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice v = vertices.get(elemento);
        if (v == null)
            throw new NoSuchElementException();
        return v;
        /* for(Vertice e : vertices){
            if(e.get().equals(elemento))
                return e;
        } */
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if(vertice == null || (vertice.getClass() != Vertice.class && 
            vertice.getClass() != Vecino.class))
            throw new IllegalArgumentException("Vértice inválido");
        
        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice) vertice;
            v.color = color;
        }

        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino) vertice;
            v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(esVacia())
            return true;
        Cola<Vertice> cola = new Cola<>();
        int i = 0;
        for(Vertice v: vertices) {
            v.color = Color.ROJO;
        }

        Iterator<Vertice> it = vertices.iterator();
        Vertice v = it.next();
        v.color = Color.NEGRO;
        cola.mete(v);
        while(!cola.esVacia()) {
            Vertice temp = cola.saca();
            i ++;
            for(Vecino u : temp.vecinos) {
                if(u.getColor() == Color.ROJO) {
                    u.vecino.color = Color.NEGRO;
                    cola.mete(u.vecino);
                }
            }
        }
        for(Vertice j : vertices) {
            j.color = Color.NINGUNO;
        }

        return (i == vertices.getElementos());
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice e : vertices)
            accion.actua(e);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = (Vertice) vertice(elemento);

        for(Vertice e : vertices){
            setColor(e, Color.ROJO);
        }

        setColor(v, Color.NEGRO);
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(v);

        while(!cola.esVacia()){
            Vertice aux = cola.saca();
            accion.actua(aux);
            for(Vecino e : aux.vecinos){
                if(e.vecino.getColor().equals(Color.ROJO)){
                    setColor(e.vecino, Color.NEGRO);
                    cola.mete(e.vecino);
                }
            }
        }

        paraCadaVertice(vertice -> setColor(vertice, Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = (Vertice) vertice(elemento);

        for(Vertice e : vertices){
            setColor(e, Color.ROJO);
        }

        setColor(v, Color.NEGRO);
        Pila<Vertice> pila = new Pila<Vertice>();
        pila.mete(v);

        while(!pila.esVacia()){
            Vertice aux = pila.saca();
            accion.actua(aux);
            for(Vecino e : aux.vecinos){
                if(e.vecino.getColor().equals(Color.ROJO)){
                    setColor(e.vecino, Color.NEGRO);
                    pila.mete(e.vecino);
                }
            }
        }

        paraCadaVertice(vertice -> setColor(vertice, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String cad = "{";
        for(Vertice e : vertices){
            cad += e.get() + ", ";
        }
        cad += "}, {";

        for(Vertice e : vertices){
            boolean ok = false;
            for(Vertice f : vertices){
                if(e.get().equals(f.get())){
                    ok = true;
                    continue;
                }
                if(ok && sonVecinos(e.get(),f.get())){
                    cad +="(" + e.get() + ", " + f.get() +"), ";
                }
            }
        }
        return cad + "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        
        if( getElementos() != grafica.getElementos() || getAristas() != grafica.getAristas())
            return false;

        for(Vertice e : vertices){
            if(!grafica.contiene(e.get()))
                return false;
        }

        for(Vertice e : vertices){
            for(Vecino f : e.vecinos){
                if(!grafica.sonVecinos(e.get(), f.vecino.get()))
                    return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        
        Vertice s = (Vertice) vertice(origen);
        Vertice t = (Vertice) vertice(destino);
        Lista<VerticeGrafica<T>> tr = new Lista<>();

        if(s.equals(t)){
            tr.agrega(t);
            return tr;
        }

        for(Vertice e : vertices)
            e.distancia = Double.MAX_VALUE;
        s.distancia = 0;

        Cola<Vertice> cola = new Cola<>();
        cola.mete(s);

        while(!cola.esVacia()){
            Vertice v = cola.saca();
            for(Vecino e : v.vecinos){
                if(e.vecino.distancia == Double.MAX_VALUE){
                    e.vecino.distancia = v.distancia + 1;
                    cola.mete(e.vecino);
                }
            }
        }

        if(t.distancia == Double.MAX_VALUE)
            return tr;

        tr.agrega(t);
        Vertice v = t;
        while(v != s){
            for(Vecino e : v.vecinos){
                if(e.vecino.distancia == v.distancia - 1){
                    tr.agrega(e.vecino);
                    v = e.vecino;
                    break;
                }
            }
        }    
        return tr.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        


        Vertice s = (Vertice) vertice(origen);
        Vertice t = (Vertice) vertice(destino);
        Lista<VerticeGrafica<T>> tr = new Lista<>();
        
        for(Vertice e : vertices)
            e.distancia = Double.MAX_VALUE;
        s.distancia = 0;

        MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<>(vertices, vertices.getElementos());

        while(!monticulo.esVacia()){
            Vertice u = monticulo.elimina();
            for(Vecino v : u.vecinos){
                if(v.vecino.distancia == Double.MAX_VALUE || u.distancia + v.peso < v.vecino.distancia){
                    v.vecino.distancia = u.distancia + v.peso;
                    monticulo.reordena(v.vecino);
                }
            }
        }

        if(t.distancia == Double.MAX_VALUE)
            return tr;

        Vertice u = t;
        tr.agrega(u);
        while (u != s) {
            for (Vecino v : u.vecinos) {
                if (u.distancia == v.vecino.distancia + v.peso) {
                    tr.agregaInicio(v.vecino);
                    u = v.vecino;
                }
            }
        }
        return tr;

    }


}
