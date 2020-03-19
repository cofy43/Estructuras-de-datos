package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        int lon = llave.length;
        int r = 0, i = 0;
        while (lon >= 4) {
            r ^= ((llave[i]&0xff)    << 24)  | ((llave[i + 1]&0xff) << 16) |
                    ((llave[i + 2]&0xff) << 8)  | ((llave[i + 3]&0xff));
            i += 4;
            lon -= 4;
        }
        int t = 0;
        switch (lon) {
            case 3: t |= (llave[i + 2]&0xff) << 8;
            case 2: t |= (llave[i + 1]&0xff) << 16;
            case 1: t |= (llave[i]&0xff)     << 24;
        }
        if(lon != 0)
            r ^= t;
        return r;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a,b,c,n,lon = n = llave.length;
        a = b = 0x9e3779b9;
        c = 0xffffffff;
        int i = 0;
        while (lon >= 12){
            a += ((llave[i]&0xff)   + ((llave[i+1]&0xff) << 8) + ((llave[i+2]&0xff)  << 16) + ((llave[i+3]&0xff)  << 24));
            b += ((llave[i+4]&0xff) + ((llave[i+5]&0xff) << 8) + ((llave[i+6]&0xff)  << 16) + ((llave[i+7]&0xff)  << 24));
            c += ((llave[i+8]&0xff) + ((llave[i+9]&0xff) << 8) + ((llave[i+10]&0xff) << 16) + ((llave[i+11]&0xff) << 24));

            a -= b; a -= c; a ^= (c >>> 13);
            b -= c; b -= a; b ^= (a <<  8);
            c -= a; c -= b; c ^= (b >>> 13);
            a -= b; a -= c; a ^= (c >>> 12);
            b -= c; b -= a; b ^= (a <<  16);
            c -= a; c -= b; c ^= (b >>> 5);
            a -= b; a -= c; a ^= (c >>> 3);
            b -= c; b -= a; b ^= (a <<  10);
            c -= a; c -= b; c ^= (b >>> 15);
            
            i += 12;
            lon -=12;
        }
        c += n;
        switch (lon) {
            case 11: c += ((llave[i+10]&0xff) << 24);
            case 10: c += ((llave[i+9]&0xff)  << 16);
            case  9: c += ((llave[i+8]&0xff)  << 8);
        
            case  8: b += ((llave[i+7]&0xff)  << 24);
            case  7: b += ((llave[i+6]&0xff)  << 16);
            case  6: b += ((llave[i+5]&0xff)  << 8);
            case  5: b +=  (llave[i+4]&0xff);
                
            case  4: a += ((llave[i+3]&0xff)  << 24);
            case  3: a += ((llave[i+2]&0xff)  << 16);
            case  2: a += ((llave[i+1]&0xff)  << 8);
            case  1: a += (llave[i]&0xff);
        }

        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a <<  8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a <<  16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a <<  10);
        c -= a; c -= b; c ^= (b >>> 15);
        return c;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        for (int i = 0; i < llave.length; i++) {
            h += (h	<<	5)	+ (llave[i]&0xff);
        }
        return h;
    }
}
