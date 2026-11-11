
package TrabajoPractico3.ArbolSintactico;

import java.util.ArrayList;
import java.util.List;

public class ImpresorArbol {

    public static void imprimir(NodoAST raiz) {
        System.out.println();
        System.out.println("========== ÁRBOL SINTÁCTICO ==========");

        if (raiz == null) {
            System.out.println("No hay árbol sintáctico para mostrar.");
            return;
        }

        imprimirNodo(raiz, "", true);

        System.out.println("======================================");
    }

    private static void imprimirNodo(
            NodoAST nodo,
            String prefijo,
            boolean ultimo) {

        if (nodo == null) {
            return;
        }

        System.out.println(
            prefijo
            + (ultimo ? "└── " : "├── ")
            + obtenerNombre(nodo)
        );

        List<NodoAST> hijos = obtenerHijos(nodo);

        for (int i = 0; i < hijos.size(); i++) {
            boolean esUltimo = (i == hijos.size() - 1);

            imprimirNodo(
                hijos.get(i),
                prefijo + (ultimo ? "    " : "│   "),
                esUltimo
            );
        }
    }

    private static String obtenerNombre(NodoAST nodo) {

        if (nodo instanceof NodoHoja) {
            NodoHoja hoja = (NodoHoja) nodo;

            return hoja.getTipoNodo() + ": " + hoja.getValor();
        }

        if (nodo instanceof NodoPrograma) {
            NodoPrograma n = (NodoPrograma) nodo;
            return "PROGRAMA: " + n.getNombrePrograma();
        }

        if (nodo instanceof NodoInvocacion) {
            NodoInvocacion invocacion = (NodoInvocacion) nodo;

            return "INVOCACION: " + invocacion.getNombreFuncion();
        }

        if (nodo instanceof NodoInvocacionMetodo) {
            NodoInvocacionMetodo invocacion =
                (NodoInvocacionMetodo) nodo;

            return "INVOCACION MÉTODO: "
                + invocacion.getObjeto()
                + "."
                + invocacion.getMetodo();
        }

        if (nodo instanceof NodoFuncion) {
            NodoFuncion funcion = (NodoFuncion) nodo;

            return "FUNCIÓN: " + funcion.getNombre()
                + " [" + funcion.getAmbitoCompleto() + "]";
        }

        return nodo.getEtiqueta();
    }

    private static List<NodoAST> obtenerHijos(NodoAST nodo) {

        List<NodoAST> hijos = new ArrayList<>();

        if (nodo instanceof NodoPrograma) {
            NodoPrograma n = (NodoPrograma) nodo;

            hijos.add(n.getDeclaraciones());
            hijos.add(n.getBloqueEjecutable());

        } else if (nodo instanceof NodoBloque) {
            hijos.addAll(((NodoBloque) nodo).getSentencias());

        } else if (nodo instanceof NodoAsignacion) {
            NodoAsignacion n = (NodoAsignacion) nodo;
            hijos.add(n.getVariable());
            hijos.add(n.getExpresion());

        } else if (nodo instanceof NodoBinario) {
            NodoBinario n = (NodoBinario) nodo;
            hijos.add(n.getIzq());
            hijos.add(n.getDer());

        } else if (nodo instanceof NodoUnario) {
            hijos.add(((NodoUnario) nodo).getOperando());

        } else if (nodo instanceof NodoAccesoArreglo) {
            NodoAccesoArreglo n = (NodoAccesoArreglo) nodo;
            hijos.add(n.getArreglo());
            hijos.add(n.getIndice());

        } else if (nodo instanceof NodoIF) {
            NodoIF n = (NodoIF) nodo;
            hijos.add(n.getCondicion());
            hijos.add(n.getCuerpoThen());
            hijos.add(n.getCuerpoElse());

        } else if (nodo instanceof NodoRepeat) {
            NodoRepeat n = (NodoRepeat) nodo;
            hijos.add(n.getCuerpo());
            hijos.add(n.getCondicion());

        } else if (nodo instanceof NodoPrint) {
            hijos.add(((NodoPrint) nodo).getContenido());

        } else if (nodo instanceof NodoReturn) {
            hijos.add(((NodoReturn) nodo).getExpresionRetorno());

        } else if (nodo instanceof NodoInvocacion) {
            hijos.add(((NodoInvocacion) nodo).getParametros());

        } else if (nodo instanceof NodoInvocacionMetodo) {
            hijos.add(((NodoInvocacionMetodo) nodo).getParametros());

        }else if (nodo instanceof NodoFuncion) {
            NodoFuncion n = (NodoFuncion) nodo;

            hijos.add(n.getParametros());
            hijos.add(n.getCuerpoDeclarativo());
            hijos.add(n.getCuerpoEjecutable());
        }

        // Eliminamos hijos nulos.
        hijos.removeIf(hijo -> hijo == null);

        return hijos;
    }
}
