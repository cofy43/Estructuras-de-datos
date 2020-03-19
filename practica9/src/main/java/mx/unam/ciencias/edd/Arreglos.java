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
        if (arreglo == null || arreglo.length <= 1)
            return;
        sort(arreglo, 0, arreglo.length-1, comparador);
    }

    private static<T> void
    sort(T[] arreglo, int inicio, int fin, Comparator<T> comparador) {
        if (inicio < fin) {
            int i = inicio, j = fin;
            T pivote = arreglo[(i + j) / 2];

            while (i <= j) {
                while (comparador.compare(arreglo[i], pivote) < 0) {
                    i++;
                }
                while (comparador.compare(pivote ,arreglo[j]) < 0) {
                    j--;
                }
                if (i <= j) {
                    T temp = arreglo[i];
                    arreglo[i] = arreglo[j];
                    arreglo[j] = temp;
                    i++;
                    j--;
                }
            }

            sort(arreglo, inicio, j, comparador);
            sort(arreglo, i, fin, comparador);
        }
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
        for (int i = 0; i < arreglo.length -1; i++) {
            int aux = i;
            for (int j = i + 1; j < arreglo.length; j++) {
                if (comparador.compare(arreglo[j], arreglo[aux]) < 0) {
                    aux = j;
                }
            }
            T temp = arreglo[aux];
            arreglo[aux] = arreglo[i];
            arreglo[i] = temp;
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
        int inicio = 0, fin = arreglo.length-1;
        return busquedaRecursiva(arreglo, elemento, inicio, fin, comparador);
    } 

    private static <T> int 
    busquedaRecursiva(T[] arreglo, T elemento, int inicio, int fin, Comparator<T> comparador) {
        if (inicio > fin) {
            return -1;
        }
        int m = (int) Math.floor((inicio + fin) / 2);
        T element = arreglo[m];
        if (comparador.compare(element, elemento) == 0) {
            return m;
        }else if (comparador.compare(elemento, element) < 0) {
            fin = m - 1;
            return busquedaRecursiva(arreglo, elemento, inicio, fin, comparador);
        }else {
            inicio = m + 1;
            return busquedaRecursiva(arreglo, elemento, inicio, fin, comparador);
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
