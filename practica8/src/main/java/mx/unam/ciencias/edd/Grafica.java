package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.Lista;

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
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            vecinos = new Lista<>();
            this.elemento = elemento;
            this.color = Color.NINGUNO;

        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return this.vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
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
            if (vertice.distancia == this.distancia) {
                return 0;
            } else if (vertice.distancia > this.distancia) {
                return 1;
            } else {
                return -1;
            }
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
            return vecino.vecinos();
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
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Lista<>();
        this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return this.vertices.getLongitud();
    }
    
        
    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (this.contiene(elemento) || elemento == null)
            throw new IllegalArgumentException();

        vertices.agrega(new Vertice(elemento));
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
        Vertice v1 = (Vertice) vertice(a);
        Vertice v2 = (Vertice) vertice(b);
        if (v1.equals(v2) || sonVecinos(a, b)) {
            throw new IllegalArgumentException();
        }

        if (!vertices.contiene(v1) || !vertices.contiene(v2)) {
            throw new NoSuchElementException();
        }

        v1.vecinos.agrega(new Vecino(v2, 1.0));
        v2.vecinos.agrega(new Vecino(v1, 1.0));
        aristas ++;
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
        Vertice v1 = (Vertice) vertice(a);
        Vertice v2 = (Vertice) vertice(b);
        if (v1.equals(v2) || sonVecinos(a, b) || peso < 0)
            throw new IllegalArgumentException();

        if (!vertices.contiene(v1) || !vertices.contiene(v2))
            throw new NoSuchElementException();

        v1.vecinos.agrega(new Vecino(v2, peso));
        v2.vecinos.agrega(new Vecino(v1, peso));
        aristas ++;
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
        Vertice v1 = (Vertice) vertice(a);
        Vertice v2 = (Vertice) vertice(b);

        if (!sonVecinos(a, b))
            throw new IllegalArgumentException();
        
        if (!sonVecinos(v1.elemento, v2.elemento))
            throw new NoSuchElementException();

        Vecino vecino1 = null, vecino2 = null;


        for (Vecino v: v1.vecinos) {
            if (v.vecino.equals(v2)) {
                vecino1 = v;
            }
        }

        for (Vecino v: v2.vecinos) {
            if (v.vecino.equals(v1)) {
                vecino2 = v;
            }
        }

        v1.vecinos.elimina(vecino1);
        v2.vecinos.elimina(vecino2);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice v : vertices) {
            if (v.elemento.equals(elemento))
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
        if (!contiene(elemento))
            throw new NoSuchElementException();

        Vertice vertice = (Vertice) vertice(elemento);
        for (Vertice vs : vertices) {
            for (Vecino vI : vs.vecinos) {
                if (vI.vecino.equals(vertice)) {
                    vs.vecinos.elimina(vI);
                    aristas--;
                }
            }

        }
        vertices.elimina(vertice);
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
        if (!this.contiene(a) || !this.contiene(b))
            throw new NoSuchElementException();
            
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        Vecino vecinoA = null, vecinoB = null;

        if (va.vecinos.getLongitud() > 0) {
            if (vb.vecinos.getLongitud() > 0) {

                for (Vecino v: va.vecinos) {
                    if (v.vecino.equals(vb)) {
                        vecinoA = v;
                    }
                }

                for (Vecino v: vb.vecinos) {
                    if (v.vecino.equals(va)) {
                        vecinoB = v;
                    }
                }

                return (vecinoA != null && vecinoB != null);

            } else {
                return false;
            }
        } else {
            return false;
        }
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
        if (!contiene(a) && !contiene(b)) {
            throw new NoSuchElementException();
        }

        if (!sonVecinos(a, b)) {
            throw new IllegalArgumentException();
        }

        Vertice v = (Vertice) vertice(a);
        Vertice vecinoV = (Vertice) vertice(b);


        for (Vecino v1: v.vecinos) {
            if(v1.vecino.equals(vecinoV)) {
                return v1.peso;
            }
        }
        return 1.0;
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
        if (!contiene(a) || !contiene(b)) {
            throw new NoSuchElementException();
        }

        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        Vecino vecinoA = buscaVecino(va, vb);

        if (!sonVecinos(a, b) || vecinoA.peso <= 0.0) {
            throw new IllegalArgumentException();
        }

        desconecta(a, b);
        conecta(a, b, peso);

    }

    private Vecino buscaVecino(Vertice vertice, Vertice vecino) {
        for (Vecino v: vertice.vecinos) {
            if (v.vecino.equals(vecino)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        for (Vertice v : vertices) {
            if (v.elemento.equals(elemento)) {
                return v;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || (vertice.getClass() != Vertice.class &&
                vertice.getClass() != Vecino.class)) {
            throw new IllegalArgumentException();
        }
                
        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice)vertice;
            v.color = color;
        }

        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino)vertice;
            v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        Cola<Vertice> cola = new Cola<>();
        int contador = 0;

        for (Vertice v: vertices) {
            v.color  = Color.ROJO;
        }

        Vertice v = vertices.getPrimero();
        v.color = Color.NEGRO;
        cola.mete(v);
        while (!cola.esVacia()) {
            Vertice u = cola.saca();
            contador ++;
            for (Vecino x: u.vecinos) {
                if (x.getColor() == Color.ROJO) {
                    x.vecino.color = Color.NEGRO;
                    cola.mete(x.vecino);
                }
            }
        }

        for (Vertice s: vertices) {
            v.color  = Color.NINGUNO;
        }

        return (contador == vertices.getLongitud());
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice v: vertices) {
            accion.actua(v);
        }
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
        if (!contiene(elemento))
            throw new NoSuchElementException();

        for (Vertice v: vertices) {
            v.color = Color.ROJO;
        }

        Vertice w = (Vertice) vertice(elemento);
        Cola<Vertice> q = new Cola<>();
        w.color = Color.NEGRO;
        q.mete(w);
        while (!q.esVacia()) {
            Vertice u = q.saca();
            accion.actua(u);
            for (Vecino v: u.vecinos) {
                if (v.getColor() == Color.ROJO) {
                    v.vecino.color = Color.NEGRO;
                    q.mete(v.vecino);
                }
            }
        }

        for (Vertice v: vertices) {
            v.color = Color.NINGUNO;
        }
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
        if (!contiene(elemento))
            throw new NoSuchElementException();
            
        for (Vertice v: vertices) {
            v.color = Color.ROJO;   
        }

        Pila<Vertice> s =  new Pila<>();
        Vertice w =  (Vertice)vertice(elemento);
        w.color = Color.NEGRO;
        s.mete(w);

        while (!s.esVacia()) {
            Vertice u = s.saca();
            accion.actua(u);
            for (Vecino v: u.vecinos) {
                if (v.vecino.color == Color.ROJO) {
                    v.vecino.color = Color.NEGRO;
                    s.mete(v.vecino);
                }
            }
        }   

        for (Vertice v: vertices) {
            v.color = Color.NINGUNO;
        }
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return (vertices.getLongitud() == 0);
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
        String ver = "{";
        String aristas = "{";

        Lista<T> aux = new Lista<>();
        for (Vertice v: vertices) {
            ver += v.elemento + ", ";
            aux.agrega(v.elemento);
            for (Vecino u: v.vecinos) {
                if (!aux.contiene(u.get()))
                    aristas += "(" + v.elemento + ", " + u.get() + "), ";
            }
        }

        ver += "}";
        aristas += "}";
        
        return ver + ", " + aristas;
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
        
        if (vertices.getLongitud() != grafica.vertices.getLongitud()) {
            return false;
        }
        if (aristas != grafica.aristas) {
            return false;
        }

        for (Vertice v: vertices) {
            v.color = Color.ROJO;
            if (!grafica.contiene(v.elemento))
                return false;
        }

        for (Vertice u: vertices) {
            for (Vecino x: u.vecinos) {
                if (x.getColor() == Color.ROJO) {
                    if (!grafica.sonVecinos(u.elemento, x.get()))
                        return false;
                }
            }
            u.color = Color.NEGRO;
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
        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();

        if (s.equals(t)) {
            trayectoria.agrega(s);
            return trayectoria;
        }
        //Double.MAX_VALUE

        for (Vertice v: vertices) {
            v.distancia = Double.MAX_VALUE;
        }
        s.distancia = 0;

        Cola<Vertice> cola = new Cola<>();
        cola.mete(s);

        while (!cola.esVacia()) {
            Vertice u = cola.saca();
            for (Vecino v: u.vecinos) {
                if (v.vecino.distancia == Double.MAX_VALUE) {
                    v.vecino.distancia = (u.distancia + 1);
                    cola.mete(v.vecino);
                }
            }
        }

        if (t.distancia == Double.MAX_VALUE) {
            return trayectoria;
        }

        trayectoria.agrega(t);
        
        while (t != s) {
            for (Vecino v: t.vecinos) {
                if (v.vecino.distancia == (t.distancia - 1)) {
                    trayectoria.agrega(v.vecino);
                    t = v.vecino;
                    break;
                }
            }
        }

        return trayectoria.reversa();
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

        if (s == null || t == null) {
            throw new NoSuchElementException();
        }

        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();    

        for (Vertice v: vertices) {
            v.distancia = Double.MAX_VALUE;
        }
        s.distancia = 0;

        int n = vertices.getElementos();
        double valor = ((n*(n-1))/2)-n;

        if (aristas > valor) {
            MonticuloArreglo<Vertice> monticulo = new MonticuloArreglo<>(vertices);

            while (!monticulo.esVacia()) {
                Vertice u = monticulo.elimina();
                for (Vecino v : u.vecinos) {
                    if (v.vecino.distancia > u.distancia + v.peso) {
                        v.vecino.distancia = u.distancia + v.peso;
                        monticulo.reordena(v.vecino);
                    }
                }
            }    
        } else {
            MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<>(vertices);
    
            while (!monticulo.esVacia()) {
                Vertice u = monticulo.elimina();
                for (Vecino v : u.vecinos) {
                    if (v.vecino.distancia > u.distancia + v.peso) {
                        v.vecino.distancia = u.distancia + v.peso;
                        monticulo.reordena(v.vecino);
                    }
                }
            }
        }


        if (t.distancia == Double.MAX_VALUE) {
            return trayectoria;
        }

        trayectoria.agrega(t);
        
        while (!t.elemento.equals(origen)) {
            for (Vecino v: t.vecinos) {
                if (t.distancia == v.vecino.distancia + v.peso) {
                    trayectoria.agrega(v.vecino);
                    t = v.vecino;
                    break;
                }
            }
        }

        return trayectoria.reversa();
    }
}
