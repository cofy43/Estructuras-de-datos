package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.sgv.*;

public class Lectura {

    private class Arista {
        int aritasUno, aritasDos;

        public Arista(int aristaUno, int aristaDos) {
            this.aritasUno = aristaUno;
            this.aritasDos = aristaDos;
        }

        public boolean equlas(Arista arista) {
            if (this.aritasUno == arista.aritasUno) {
                if (this.aritasDos == arista.aritasDos) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public String toString() {
            return String.format("%d , %d", this.aritasUno, this.aritasDos);
        }
    }

    Arista arista;
    FileReader fr;
    BufferedReader br;

    Lista<String> lista = new Lista<>();

    public String ignorarComentario(String cadena) {
        String tem = eliminarEspacios(cadena);
        if (tem.length() > 0) {
            String sub = tem.substring(0,1);
            if (!sub.equals("#")) {
                return cadena;
            }
        }
        return "";
    }

    public void lectura(String direccion) throws IOException {
        fr = new FileReader(direccion);
        br = new BufferedReader(fr);
        String cadena;
        while ((cadena = br.readLine()) != null) {
            if (!ignorarComentario(cadena).equals("")) {
                lista.agrega(cadena);
            }
        }
        fr.close();
        br.close();

        procesamiento(lista);
    }

    public String eliminarEspacios(String s) {
        String sinEspacios = s.replace(" ", "");
        String sinTabuladores = sinEspacios.replace("\t", "");
        return sinTabuladores;
    }

    public void uso() {
        System.out.println("Para poder utilizar el programa se debe respetar el siguiente formato:");
        System.out.println("Primero se indica la estructura a dibujar");
        System.out.println("Despues los elementos a agregar a la estructura");
    }

    public int sinComas(String s) {

        int j = 0;
        while (true) {
            if (s.charAt(j) == ',') {
                j++;
            } else {
                break;
            }
        }
        return j;
    }

    public int elementos(String s) {
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',')
                j++;
        }
        return ++j;
    }

    public void creaEstructura(Lista<String> lista, Coleccion<Integer> estruc) {
        String elementoLista, sinTabuladores, aux;
        while (!lista.esVacia()) {
            elementoLista = lista.getPrimero().replaceAll("\t", "");
            sinTabuladores = elementoLista.replaceAll(" ", ",");
            int i = sinComas(sinTabuladores);
            aux = sinTabuladores.substring(i, sinTabuladores.length());
            int j = elementos(aux);
            System.out.println("cadena: " + aux);
            for (int k = 0; k < j; k++) {
                int coma = aux.indexOf(",");
                if (coma == -1) {
                    estruc.agrega(Integer.valueOf(aux));
                    break;
                }
                String element = aux.substring(0, coma);
                estruc.agrega(Integer.valueOf(element));
                aux = aux.substring(coma+1, aux.length());
            }
            lista.eliminaPrimero();
        }
    }

    public void creaMeteSaca(Lista<String> lista, MeteSaca<String> est) {
        String elementoLista, sinTabuladores, aux;
        while (!lista.esVacia()) {
            elementoLista = lista.getPrimero().replaceAll("\t", "");
            sinTabuladores = elementoLista.replaceAll(" ", ",");
            int i = sinComas(sinTabuladores);
            aux = sinTabuladores.substring(i, sinTabuladores.length());
            int j = elementos(aux);
            for (int k = 0; k < j; k++) {
                int coma = aux.indexOf(",");
                if (coma == -1) {
                    est.mete(aux);
                    break;
                }
                String element = aux.substring(0, coma);
                est.mete(element);
                aux = aux.substring(coma+1, aux.length());
            }
            lista.eliminaPrimero();
        }
    }

    public ArbolRojinegro<Integer> creaRojiNegro(Lista<String> lista) {
        ArbolRojinegro<Integer> arbol = new ArbolRojinegro<>();
        String elementoLista, sinTabuladores, aux;
        while (!lista.esVacia()) {
            elementoLista = lista.getPrimero().replaceAll("\t", "");
            sinTabuladores = elementoLista.replaceAll(" ", ",");
            int i = sinComas(sinTabuladores);
            aux = sinTabuladores.substring(i, sinTabuladores.length());
            int j = elementos(aux);
            for (int k = 0; k < j; k++) {
                int coma = aux.indexOf(",");
                if (coma == -1) {
                    System.out.println("Elemento " + Integer.valueOf(aux));                
                    arbol.agrega(Integer.valueOf(aux));
                    System.out.println(arbol.toString());
                    break;
                }
                String element = aux.substring(0, coma);
                System.out.println("Elemento " + Integer.valueOf(element));                
                arbol.agrega(Integer.valueOf(element));
                System.out.println(arbol.toString());
                aux = aux.substring(coma+1, aux.length());
            }
            lista.eliminaPrimero();
        }
        return arbol;
    }

    public ArbolBinarioOrdenado<Integer> creaRojiOrdenado(Lista<String> lista) {
        ArbolBinarioOrdenado<Integer> arbol = new ArbolBinarioOrdenado<>();
        String elementoLista, sinTabuladores, aux;
        while (!lista.esVacia()) {
            elementoLista = lista.getPrimero().replaceAll("\t", "");
            sinTabuladores = elementoLista.replaceAll(" ", ",");
            int i = sinComas(sinTabuladores);
            aux = sinTabuladores.substring(i, sinTabuladores.length());
            int j = elementos(aux);
            for (int k = 0; k < j; k++) {
                int coma = aux.indexOf(",");
                if (coma == -1) {
                    System.out.println("Elemento " + Integer.valueOf(aux));
                    arbol.agrega(Integer.valueOf(aux));
                    System.out.println(arbol.toString());
                    break;
                }
                String element = aux.substring(0, coma);
                System.out.println("Elemento " + Integer.valueOf(element));                
                arbol.agrega(Integer.valueOf(element));
                System.out.println(arbol.toString());
                aux = aux.substring(coma+1, aux.length());
            }
            lista.eliminaPrimero();
        }
        return arbol;
    }

    public Grafica<Integer> creaGrafica(Lista<String> lista) {
        Grafica<Integer> grafica = new Grafica<>();
        int numeroElementos = 0, elementoUnoArista = 0;
        Lista<Arista> aristas = new Lista<>();
        String elementoLista, sinTabuladores, aux;
        while (!lista.esVacia()) {
            elementoLista = lista.getPrimero().replaceAll("\t", "");
            sinTabuladores = elementoLista.replaceAll(" ", ",");
            int i = sinComas(sinTabuladores);
            aux = sinTabuladores.substring(i, sinTabuladores.length());
            int j = elementos(aux);
            for (int k = 0; k < j; k++) {
                int coma = aux.indexOf(",");
                if (coma == -1) {
                    if (aux.equals("")) {
                        numeroElementos ++;
                        break;
                    }

                    if (!grafica.contiene(Integer.valueOf(aux))) {
                        grafica.agrega(Integer.valueOf(aux));
                        if (elementoUnoArista != 0) {
                            System.out.println("Elemento " + Integer.valueOf(aux));
                            System.out.println("Estado de la gráfica " + grafica.toString());
                            aristas.agrega(new Arista(elementoUnoArista, Integer.valueOf(aux)));
                            elementoUnoArista = 0;
                        } else {
                            elementoUnoArista = Integer.valueOf(aux);
                        }
                        numeroElementos++;
                        break;
                    } else {
                        if (elementoUnoArista != 0 ) {
                            aristas.agrega(new Arista(elementoUnoArista, Integer.valueOf(aux)));
                            numeroElementos++;
                            break;
                        } else {
                            elementoUnoArista = Integer.valueOf(aux);
                            break;
                        }
                    }
                }

                String element = aux.substring(0, coma);
                System.out.println("Elemento " + Integer.valueOf(element));
                System.out.println("Estado de la gráfica " + grafica.toString());
                if (!grafica.contiene(Integer.valueOf(element))) {
                    grafica.agrega(Integer.valueOf(element));
                    if (elementoUnoArista != 0) {
                        System.out.println("Arista Elemento uno " + Integer.valueOf(element));
                        aristas.agrega(new Arista(elementoUnoArista, Integer.valueOf(element)));
                        elementoUnoArista = 0;
                    } else {
                        elementoUnoArista = Integer.valueOf(element);
                    }
                    numeroElementos++;
                } else {
                    if (elementoUnoArista != 0) {
                        aristas.agrega(new Arista(elementoUnoArista, Integer.valueOf(element)));
                        elementoUnoArista = 0;
                    } else {
                        elementoUnoArista = Integer.valueOf(element);
                    }
                }
                 
                aux = aux.substring(coma+1, aux.length());
            }
            lista.eliminaPrimero();
        }
        if (numeroElementos % 2 == 0) {
            for (Arista a: aristas) {
                grafica.conecta(a.aritasUno, a.aritasDos);
            }
            return grafica;
        } else {
            grafica = null;
            return grafica;
        }
    }

    
    public boolean hayLazos(Arista a) {
        if (a.aritasUno == a.aritasDos) {
            return true;
        }
        return false;
    }

    public boolean hayAristasRepetidas(Arista a, Lista<Arista> lista) {
        int longitud = lista.getLongitud();
        if (lista.contiene(a)) {
            System.out.println(lista.toString());
            lista.elimina(a);
        }
        System.out.println(lista.toString());
        lista.elimina(a);
        System.out.println(lista.toString());
        if (longitud == lista.getLongitud()-2) {
            return true;    
        } 
        return false;
    }

    public void procesamiento(Lista<String> lista) {
        String estructura = eliminarEspacios(lista.getPrimero());
        Coleccion<Integer> estruc;
        MeteSaca<String> est;
        int indicador = 0;
        lista.eliminaPrimero();
        switch(estructura) {
                case "MonticuloMinimo":
                estruc = new ArbolBinarioCompleto<>();
                creaEstructura(lista, estruc);
                SVG_Monticulo_Minimo dibujaMonticuloMinimo = new SVG_Monticulo_Minimo();
                dibujaMonticuloMinimo.creaSVGArbolCompleto(estruc, indicador);
                break;
            case "ArbolBinarioCompleto":
                if (indicador == 0) {
                    indicador = 2;
                }
                estruc = new ArbolBinarioCompleto<>();
                creaEstructura(lista, estruc);
                SVG_Arbol_Completo dibujaArbol = new SVG_Arbol_Completo();
                dibujaArbol.creaSVGArbolCompleto(estruc, indicador);
                break;
            case "ArbolBinarioOrdenado":
                ArbolBinarioOrdenado<Integer> arbolOdenado = creaRojiOrdenado(lista);
                SVG_Arbol_Ordenado dibujaOrdenado = new SVG_Arbol_Ordenado();
                dibujaOrdenado.creaSVGArbolCompleto(arbolOdenado);
                System.out.println(arbolOdenado.toString());
                break;
            case "ArbolRojinegro" :
                System.out.println(lista.toString());                
                estruc = new ArbolRojinegro<>();
                ArbolRojinegro<Integer> arbol= creaRojiNegro(lista);
                SVG_Arbol_RojiNegro dibujaRojiNegro = new SVG_Arbol_RojiNegro();
                dibujaRojiNegro.creaSVGArbolCompleto(arbol);
                break;
            case "ArbolAVL":
                estruc = new ArbolAVL<>();
                creaEstructura(lista, estruc);
                SVG_Arbol_AVL dibujaAVL = new SVG_Arbol_AVL();
                dibujaAVL.creaSVGArbolAVL((ArbolAVL<Integer>)estruc);
                System.out.println("Arbol estructura\n" + estruc.toString());
                break;
            case "Lista":
                estruc = new Lista<>();
                creaEstructura(lista, estruc);
                SVG_Lista dibujaLista = new SVG_Lista();
                dibujaLista.creaSGVLista(estruc);
                System.out.println(estruc.toString());
                break;
            case "Pila":
                est = new Pila<>();
                creaMeteSaca(lista, est);
                SVG_Pila dibujaPila = new SVG_Pila();
                dibujaPila.creaSGVPila(est);
                System.out.println(est.toString());
                break;
            case "Cola":
                est = new Cola<>();
                creaMeteSaca(lista, est);
                SVG_Cola dibujaCola = new SVG_Cola();
                dibujaCola.creaSGVCola(est);
                System.out.println(est.toString());
                break;
            case "Grafica":
                Grafica<Integer> grafica = creaGrafica(lista);
                if (grafica == null) {
                    System.out.println("Error de formato de texto, el número de elementos es impar");    
                    uso();
                    return;
                }
                SVG_Grafica dibujaGrafica = new SVG_Grafica();
                dibujaGrafica.creaSVGArbolCompleto(grafica);
                System.out.println(grafica.toString());
                break;
            case "MonticuloArreglo" :
                estruc = new Lista<>();
                creaEstructura(lista, estruc);
                SVG_Monticulo_Arreglo dibujaMonticuloArreglo = new SVG_Monticulo_Arreglo();
                dibujaMonticuloArreglo.creaSGVLista(estruc);
                System.out.println(estruc.toString());
                break;
            default:
                uso();
                return;    
        }
    }
}