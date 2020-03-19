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
        quickSort(arreglo, comparador, 0, arreglo.length - 1); 
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     * @param i índice inferior del rango a ordenar.
     * @param j índice superior del rengo a ordenar.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador, int i, int j) {
        if(i >= j)
            return;
        T piv = arreglo[i];
        int x = i;
        int y = j;

        while(x < y){
            while(comparador.compare(arreglo[y], piv) > 0)
                y--;

            while(x < y && comparador.compare(arreglo[x], piv) <= 0)
                x++;

            if(x < y){
                T t;
                t = arreglo[x];
                arreglo[x] = arreglo[y];
                arreglo[y] = t;
            }
        }
        T t;
        t = arreglo[i];
        arreglo[i] = arreglo[y];
        arreglo[y] = t;
        quickSort(arreglo, comparador, i, x-1);
        quickSort(arreglo, comparador, x+1, j);
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

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        int n = arreglo.length;
        for(int i = 0; i < n; i++){
            int p = i;
            for(int j = i+1; j < n; j++){
                if(comparador.compare(arreglo[j], arreglo[p]) < 0)
                    p = j;
            }
            T t = arreglo[i];
            arreglo[i] = arreglo[p];
            arreglo[p] = t;
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
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
        int ini = 0, fin = arreglo.length-1;
        quickSort(arreglo, comparador);

        while(ini <= fin){
            
            int m = (ini+fin)/2;
            if(comparador.compare(arreglo[m], elemento) == 0)
                return m;
            if(comparador.compare(arreglo[m], elemento) < 0)
                ini = m+1;
            if(comparador.compare(arreglo[m], elemento) > 0)
                fin = m-1;
        }

        return -1;
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
