package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
	int fin=(arreglo.length)-1;
	int ini=0;
	qS(arreglo,comparador,ini,fin);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }
    
    private static<T> void qS(T[] a,Comparator<T> comparador,int ini,int fin){
	if(fin<=ini)
	    return ;
	int i =ini+1;
	int j =fin;
	while(i<j)
	    if(comparador.compare(a[ini],a[i])<0 && comparador.compare(a[j],a[ini])<=0)
		intercambia(a,i++,j--);
	    else if(comparador.compare(a[ini],a[i])>=0)
		i++;
	    else
		j--;
	if(comparador.compare(a[i],a[ini])>0)
	    i--;
	intercambia(a,ini,i);
	qS(a,comparador,ini,(i-1));
	qS(a,comparador,(i+1),fin);
		
    }
	       
	

    private static <T> void intercambia(T[]arreglo,int i,int min){
	T aux=arreglo[i];
	arreglo[i]=arreglo[min];
	arreglo[min]=aux;
    }
    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
	for(int i=0;i<arreglo.length;i++){
	    int min=i;
	    for(int j=i+1;j<(arreglo.length);j++){
		if(comparador.compare(arreglo[j],arreglo[min])<0)
		    min=j;
	    }
	    intercambia(arreglo,i,min);
	}
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
	//if(arreglo.length==0)
	    //return -1;
	int fin = (arreglo.length-1);
	return bQ(arreglo,elemento,comparador,0,fin);
    }
    public static <T> int bQ(T[] a, T elemen, Comparator<T> comp,int ini,int fin){
	if(fin-ini==0)
	    return -1;
	if(comp.compare(a[ini],elemen)==0)
	    return ini;
	if(comp.compare(a[fin],elemen)==0)
	    return fin;
	int lon =((fin + ini)/2);
        if(comp.compare(a[lon],elemen)==0)
            return lon;
	if(comp.compare(a[lon],elemen)>0){
            return bQ(a,elemen,comp,ini,((fin+ini)/2));
        }else{
		return  bQ(a,elemen,comp,((fin+ini)/2)+1,fin);
	}
    }
    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
