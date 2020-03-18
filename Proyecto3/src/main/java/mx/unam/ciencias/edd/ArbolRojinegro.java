package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
	    super(elemento);
	    this.color = Color.ROJO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
	    String ver = "";
	    if(this.color==Color.NEGRO)
		ver += "N{";
	    else
		ver+="R{";
	    ver+=this.elemento.toString()+"}";
	    return ver;

        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
	    if(!this.elemento.equals(vertice.elemento) && !(this.color==vertice.color))
		return false;
	    return equals(this,vertice);
        }
	private boolean equals(VerticeRojinegro ver,VerticeRojinegro v){
	    if(ver==null && v==null)
		return true;
	    if((ver==null) || (v==null))
                return false;
	    if(!ver.elemento.equals(v.elemento) || ver.color!=v.color)
                return false;
	    VerticeRojinegro izquierdo = verticeRojinegro(ver.izquierdo);
	    VerticeRojinegro izquier = verticeRojinegro(v.izquierdo);
	    boolean iz = equals(izquierdo,izquier);
	    VerticeRojinegro derecho = verticeRojinegro(ver.derecho);
            VerticeRojinegro dere = verticeRojinegro(v.derecho);
	    boolean der = equals(derecho,dere);
	    if(iz && der)
		return true;
	    return false;
	}
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /* Convierte el vértice a VerticeRojinegro. */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	VerticeRojinegro ver = verticeRojinegro(vertice);
	return ver.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	VerticeRojinegro ver = verticeRojinegro(ultimoAgregado);
	ver.color = Color.ROJO;
	balancea(ver);
    }
    private void balancea(VerticeRojinegro ver){
	if(ver.padre==null){
	    ver.color = Color.NEGRO;
	    return;
	}
	VerticeRojinegro padre = verticeRojinegro(ver.padre);
	if(padre.color==Color.NEGRO)
	    return;
	VerticeRojinegro abuelo = verticeRojinegro(padre.padre);
	VerticeRojinegro tio = ver;
	if(tioRojo(padre,abuelo)){
	    abuelo.color = Color.ROJO;
	    padre.color = Color.NEGRO;
	    if(abuelo.izquierdo==padre)
		tio = verticeRojinegro(abuelo.derecho);
	    else
		tio = verticeRojinegro(abuelo.izquierdo);
	    tio.color = Color.NEGRO;
	    balancea(abuelo);
	    return;
	}
	if(tioNegro(padre,abuelo) && cruzados(ver,padre,abuelo)){
	    if(abuelo.izquierdo==padre)
		super.giraIzquierda(ver.padre);
	    else
		super.giraDerecha(ver.padre);
	    VerticeRojinegro aux = padre;
	    padre = ver;
	    ver = aux;
	}
	if(tioNegro(padre,abuelo)){
	    padre.color = Color.NEGRO;
	    abuelo.color = Color.ROJO;
	    if(abuelo.izquierdo==padre)
		super.giraDerecha(ver.padre.padre);
	    else
		super.giraIzquierda(ver.padre.padre);
	}
    }
    private boolean tioRojo(VerticeRojinegro padre,VerticeRojinegro abuelo){	
	if((abuelo.izquierdo==padre && abuelo.derecho==null) || (abuelo.derecho==padre && abuelo.izquierdo==null))
	    return false;
	VerticeRojinegro tio = padre;
	if(abuelo.izquierdo==padre)
	    tio = verticeRojinegro(abuelo.derecho);
	else
	    tio = verticeRojinegro(abuelo.izquierdo);
	return tio.color==Color.ROJO;
    }
    private boolean tioNegro(VerticeRojinegro padre,VerticeRojinegro abuelo){
	if((abuelo.izquierdo==padre && abuelo.derecho==null) || (abuelo.derecho==padre && abuelo.izquierdo==null))
            return true;
        VerticeRojinegro tio = padre;
	if(abuelo.izquierdo==padre)
	    tio = verticeRojinegro(abuelo.derecho);
	else
	    tio = verticeRojinegro(abuelo.izquierdo);
        return tio.color==Color.NEGRO;
    }
    private boolean cruzados(VerticeRojinegro ver, VerticeRojinegro padre,VerticeRojinegro abuelo){
	if(abuelo.izquierdo==padre && padre.derecho==ver)
	    return true;
	if(abuelo.derecho==padre && padre.izquierdo==ver)
	    return true;
	return false;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if(elemento==null)
            return;
        if(raiz==null)
            return;
        Vertice eliminar = super.vertice(busca(elemento));
        if(eliminar==null)
            return;
        elementos--;
        if(elementos==0){
            super.limpia();
            return;
        }
	if(eliminar.hayIzquierdo() && eliminar.hayDerecho()){
            Vertice max = super.intercambiaEliminable(eliminar);
	    if(!max.hayIzquierdo()){
		Vertice fan = nuevoVertice(null);
		max.izquierdo=fan;
		fan.padre=max;
		VerticeRojinegro fantasma = verticeRojinegro(max.izquierdo);
		fantasma.color = Color.NEGRO;
	    }
	    VerticeRojinegro eli = verticeRojinegro(max);
	    VerticeRojinegro izq = verticeRojinegro(max.izquierdo);
	    eliminaVertice(max);
	    if(izq.color == Color.ROJO){
		izq.color = Color.NEGRO;
		return;
	    }  
	    if(eli.color == Color.ROJO && izq.color == Color.NEGRO){
		if(izq.elemento == null)
		    eliminaVertice(izq);
		return;
	    }
	    if(eli.color ==Color.NEGRO && izq.color == Color.NEGRO)
		rebalanceo(izq);
	    if(izq.elemento == null)
		eliminaVertice(izq);
	    return;
	}
	if(eliminar.hayIzquierdo()){
	    VerticeRojinegro eli = verticeRojinegro(eliminar);
            VerticeRojinegro izq = verticeRojinegro(eliminar.izquierdo);
            eliminaVertice(eliminar);
            if(izq.color == Color.ROJO){
                izq.color = Color.NEGRO;
		return;
            }
            if(eli.color == Color.ROJO && izq.color == Color.NEGRO)
                return;
            if(eli.color ==Color.NEGRO && izq.color == Color.NEGRO)
		rebalanceo(izq);
	    return;
	}
	if(eliminar.hayDerecho()){
	    VerticeRojinegro eli = verticeRojinegro(eliminar);
            VerticeRojinegro der = verticeRojinegro(eliminar.derecho);
            eliminaVertice(eliminar);
            if(der.color == Color.ROJO){
                der.color = Color.NEGRO;
                return;
            }
            if(eli.color == Color.ROJO && der.color == Color.NEGRO)
                return;
            if(eli.color ==Color.NEGRO && der.color == Color.NEGRO)
                rebalanceo(der);
            return;
	}
	Vertice fan = nuevoVertice(null);
	eliminar.izquierdo=fan;
	fan.padre=eliminar;
	VerticeRojinegro fantasma = verticeRojinegro(fan);
	fantasma.color = Color.NEGRO;
	VerticeRojinegro eli = verticeRojinegro(eliminar);
	eliminaVertice(eliminar);
	if(eli.color == Color.ROJO && fantasma.color == Color.NEGRO){
	    eliminaVertice(fantasma);
	    return;
	}
	if(eli.color ==Color.NEGRO && fantasma.color == Color.NEGRO)
	    rebalanceo(fantasma);
	eliminaVertice(fantasma);
    }
    private void rebalanceo(VerticeRojinegro elimina){
	if(elimina.padre == null)
	    return;
	VerticeRojinegro padre = verticeRojinegro(elimina.padre);
	VerticeRojinegro hermano = verticeRojinegro(null);
	if(padre.izquierdo == elimina)
	    hermano = verticeRojinegro(padre.derecho);
	else
	    hermano = verticeRojinegro(padre.izquierdo);
	if(esRojo(hermano)){
	    padre.color = Color.ROJO;
	    hermano.color = Color.NEGRO;
	    if(esDerecho(elimina))
		super.giraDerecha(padre);
	    else
		super.giraIzquierda(padre);
	    if(padre.izquierdo == elimina)
		hermano = verticeRojinegro(padre.derecho);
	    else
		hermano = verticeRojinegro(padre.izquierdo);
	}
	VerticeRojinegro sobrinoi = verticeRojinegro(hermano.izquierdo);
	VerticeRojinegro sobrinod = verticeRojinegro(hermano.derecho);
	if(esNegro(padre)&&esNegro(hermano)&&esNegro(sobrinoi)&&esNegro(sobrinod)){
	    hermano.color = Color.ROJO;
	    rebalanceo(padre);
	    return;
	}
	if(esRojo(padre)&&esNegro(hermano)&&esNegro(sobrinoi)&&esNegro(sobrinod)){
	    hermano.color = Color.ROJO;
	    padre.color = Color.NEGRO;
	    return;
	}
	if((!esDerecho(elimina)&&esRojo(sobrinoi)&&esNegro(sobrinod)) ||(esDerecho(elimina)&&esNegro(sobrinoi)&&esRojo(sobrinod))){
	    hermano.color = Color.ROJO;
	    if(esRojo(sobrinoi))
		sobrinoi.color = Color.NEGRO;
	    else
		sobrinod.color = Color.NEGRO;
	    if(esDerecho(elimina))
		super.giraIzquierda(hermano);
	    else
		super.giraDerecha(hermano);
	    if(padre.izquierdo == elimina)
                hermano = verticeRojinegro(padre.derecho);
            else
                hermano = verticeRojinegro(padre.izquierdo);
	    sobrinoi = verticeRojinegro(hermano.izquierdo);
	    sobrinod = verticeRojinegro(hermano.derecho);
	} 
	if((!esDerecho(elimina)&&esRojo(sobrinod))||(esDerecho(elimina) && esRojo(sobrinoi))){
	    hermano.color = padre.color;
	    if(esRojo(padre))
		padre.color = Color.NEGRO;
	    if(esDerecho(elimina))
		sobrinoi.color = Color.NEGRO;
	    else
		sobrinod.color = Color.NEGRO;
	    if(esDerecho(elimina))
	       super.giraDerecha(padre);
	    else
		super.giraIzquierda(padre);
	    }
    }
    private boolean esDerecho(VerticeRojinegro v){
	Vertice padre = v.padre;
	Vertice ver = v;
	if(padre.derecho == ver)
	    return true;
	return false;
    }
    private boolean esNegro(VerticeRojinegro v){
	return v==null || v.color==Color.NEGRO;
    }
    private boolean esRojo(VerticeRojinegro v){
	return v!=null && v.color==Color.ROJO;
    }
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}