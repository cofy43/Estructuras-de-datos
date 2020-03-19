package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Conjunto}.
 */
public class TestConjunto {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El conjunto. */
    private Conjunto<Integer> conjunto;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestro conjunto, y un conjunto.
     */
    public TestConjunto() {
        random = new Random();
        total = 10 + random.nextInt(90);
        conjunto = new Conjunto<Integer>(total);
    }

    /**
     * Prueba unitaria para {@link Conjunto#Conjunto}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(conjunto.esVacia());
        Assert.assertTrue(conjunto.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link Conjunto#agrega}.
     */
    @Test public void testAgrega() {
        try {
            conjunto.agrega(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        int ini = random.nextInt(total);
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            conjunto.agrega(a[i]);
        }
        Assert.assertFalse(conjunto.esVacia());
        Assert.assertTrue(conjunto.getElementos() == total);
        for (int i = 0; i < total; i++)
            Assert.assertTrue(conjunto.contiene(a[i]));
        Assert.assertFalse(conjunto.contiene(ini-1));
        Assert.assertFalse(conjunto.contiene(ini+total));
    }

    /**
     * Prueba unitaria para {@link Conjunto#contiene}.
     */
    @Test public void testContiene() {
        int ini = random.nextInt(total);
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            Assert.assertFalse(conjunto.contiene(a[i]));
            conjunto.agrega(a[i]);
            Assert.assertTrue(conjunto.contiene(a[i]));
        }
        Assert.assertFalse(conjunto.contiene(null));
    }

    /**
     * Prueba unitaria para {@link Conjunto#elimina}.
     */
    @Test public void testElimina() {
        int ini = random.nextInt(total);
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            conjunto.agrega(a[i]);
        }
        for (int i = 0; i < total; i++) {
            Assert.assertTrue(conjunto.contiene(a[i]));
            conjunto.elimina(a[i]);
            Assert.assertFalse(conjunto.contiene(a[i]));
        }
        try {
            conjunto.elimina(null);
        } catch (IllegalArgumentException iae) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link Conjunto#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(conjunto.esVacia());
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++) {
            conjunto.agrega(ini + i);
            Assert.assertFalse(conjunto.esVacia());
        }
        for (Integer n : conjunto) {
            Assert.assertFalse(conjunto.esVacia());
            conjunto.elimina(n);
        }
        Assert.assertTrue(conjunto.esVacia());
    }

    /**
     * Prueba unitaria para {@link Conjunto#getElementos}.
     */
    @Test public void testGetElementos() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++) {
            conjunto.agrega(ini + i);
            Assert.assertTrue(conjunto.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Conjunto#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertTrue(conjunto.esVacia());
        Assert.assertTrue(conjunto.getElementos() == 0);
        for (int i = 0; i < total; i++)
            conjunto.agrega(i);
        Assert.assertFalse(conjunto.esVacia());
        Assert.assertTrue(conjunto.getElementos() == total);
        conjunto.limpia();
        Assert.assertTrue(conjunto.esVacia());
        Assert.assertTrue(conjunto.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link Conjunto#interseccion}.
     */
    @Test public void testInterseccion() {
        int ini = total + random.nextInt(total * 10);
        for (int i = 0; i < total; i++)
            conjunto.agrega(ini + i - total/2);
        total += (total % 2) == 1 ? 1 : 0;
        Conjunto<Integer> c2 = new Conjunto<Integer>();
        for (int i = 0; i < total; i++)
            c2.agrega(ini + i);
        Conjunto<Integer> interseccion = conjunto.interseccion(c2);
        Assert.assertTrue(interseccion.getElementos() == total/2);
        for (Integer n : interseccion) {
            Assert.assertTrue(conjunto.contiene(n));
            Assert.assertTrue(c2.contiene(n));
        }
        for (Integer n : conjunto) {
            if (c2.contiene(n))
                Assert.assertTrue(interseccion.contiene(n));
            else
                Assert.assertFalse(interseccion.contiene(n));
        }
        for (Integer n : c2) {
            if (conjunto.contiene(n))
                Assert.assertTrue(interseccion.contiene(n));
            else
                Assert.assertFalse(interseccion.contiene(n));
        }
    }

    /**
     * Prueba unitaria para {@link Conjunto#union}.
     */
    @Test public void testUnion() {
        int ini = total + random.nextInt(total * 10);
        for (int i = 0; i < total; i++)
            conjunto.agrega(ini + i);
        Conjunto<Integer> c2 = new Conjunto<Integer>();
        for (int i = total; i < total*2; i++)
            c2.agrega(ini + i);
        Conjunto<Integer> union = conjunto.union(c2);
        Assert.assertTrue(union.getElementos() == total*2);
        for (Integer n : union) {
            Assert.assertTrue(conjunto.contiene(n) || c2.contiene(n));
        }
        for (Integer n : conjunto)
            Assert.assertTrue(union.contiene(n));
        for (Integer n : c2)
            Assert.assertTrue(union.contiene(n));
    }

    /**
     * Prueba unitaria para {@link Conjunto#toString}.
     */
    @Test public void testToString() {
        int[] a = new int[total];
        int ini = random.nextInt(total);
        String s = "{ ";
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            conjunto.agrega(a[i]);
            s += a[i];
            Assert.assertTrue(conjunto.toString().equals(s + " }"));
            if ( i < total - 1)
                s += ", ";
        }
        s += " }";
        Assert.assertTrue(conjunto.toString().equals(s));
    }

    /**
     * Prueba unitaria para {@link Conjunto#equals}.
     */
    @Test public void testEquals() {
        Conjunto<Integer> c2 = new Conjunto<Integer>();
        int ini = random.nextInt(total);
        int[] a = new int[total];
        Assert.assertFalse(conjunto == c2);
        Assert.assertTrue(conjunto.equals(c2));
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            conjunto.agrega(a[i]);
            Assert.assertFalse(conjunto.equals(c2));
            c2.agrega(a[i]);
            Assert.assertTrue(conjunto.equals(c2));
        }
    }

    /**
     * Prueba unitaria para {@link Conjunto#iterator}.
     */
    @Test public void testIterator() {
        int ini = random.nextInt(total);
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            conjunto.agrega(ini + i);
            lista.agregaFinal(ini + i);
        }
        int c = 0;
        for (Integer n : conjunto) {
            Assert.assertTrue(lista.contiene(n));
            lista.elimina(n);
            c++;
        }
        Assert.assertTrue(c == total);
        Assert.assertTrue(lista.getLongitud() == 0);
        for (int i = 0; i < total; i++)
            conjunto.elimina(ini + i);
        c = 0;
        for (Integer s : conjunto)
            c++;
        Assert.assertTrue(c == 0);
    }
}
