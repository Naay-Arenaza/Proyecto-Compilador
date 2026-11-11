%{
import java.io.*;
import TrabajoPractico3.ArbolSintactico.*;
import java.util.*;
%}

/* =========================================================================
   NO USAMOS %union. Todo será manejado por ParserVal de forma nativa.
   ========================================================================= */

/* Declaración de tokens. BYACC/J asume que devuelven un ParserVal */
%token ID CTE CADENA
%token BEGIN END
%token USHORTINT DOUBLEF
%token FUNCTION RET CLASS IF ELSE END_IF POUT REPEAT WHILE
%token ASIGNACION /* Representa := */
%token IGUAL DISTINTO MAYORIGUAL MENORIGUAL
%token COMPTIME EXTENDS TODF
%nonassoc SIN_PUNTO_Y_COMA
%nonassoc BEGIN '('
%nonassoc ID ';'
%%
//////////////////////////////////////////// Gramatica

programa: 
    ID sentencias_declarativas BEGIN sentencias_ejecutables END {
        NodoPrograma raiz = new NodoPrograma($1.sval,(NodoAST) $2.obj,(NodoAST) $4.obj);
        raizAST = raiz;
        $$ = new ParserVal(raiz);
        System.out.println("Declaración de programa y AST generado");
    }
    | error sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Falta de nombre de programa");  $$ = new ParserVal(null);}
    | ID sentencias_declarativas sentencias_ejecutables END { yyerror("Falta la palabra reservada BEGIN"); $$ = new ParserVal(null);}
    | ID sentencias_declarativas BEGIN sentencias_ejecutables error { yyerror("Falta la palabra reservada END"); $$ = new ParserVal(null);}
    ;

//////////////////////////////////////////// SENTENCIAS DECLARATIVAS
 sentencias_declarativas: 
    sentencias_declarativas sentencia_declarativa{
        NodoBloque bloque = (NodoBloque) $1.obj;

        if ($2.obj != null) {
            bloque.agregarSentencia((NodoAST) $2.obj);
        }
        $$ = new ParserVal(bloque);}
    |
    {$$ = new ParserVal(new NodoBloque());}
    ;

sentencia_declarativa: 
    declaracion_variables 
    | declaracion_variables_comptime
    | declaracion_funcion 
    | declaracion_clase 
    | declaracion_objeto 
    ;

declaracion_variables: 
    tipo lista_variables ';' {
        System.out.println("Declaracion de VAR");
        declararListaVariables((List<String>) $2.obj, $1.sval, false);
    }
    | tipo lista_variables %prec SIN_PUNTO_Y_COMA { yyerror("Declaracion de variables: Falta ';' " );}
    ;

declaracion_variables_comptime:
    COMPTIME tipo lista_variables ';' {
        System.out.println("Declaracion de comptime");
        declararListaVariables((List<String>) $3.obj, $2.sval, true);
    }
    | COMPTIME tipo lista_variables error { yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
    | COMPTIME lista_variables ';' { yyerror("Falta tipo en las variables en COMPTIME");}
    ;

tipo: 
    USHORTINT { System.out.println("Declaracion de tipo");
                $$ = new ParserVal("USHORTINT");
    } 
    | DOUBLEF { System.out.println("Declaracion de tipo");
                $$ = new ParserVal("DOUBLEF");
    }
    ;

lista_variables: 
    lista_variables ',' ID {
        List<String> lista = (List<String>) $1.obj;
        lista.add($3.sval);
        $$ = $1;
    }
    | ID {
        List<String> lista = new ArrayList<>();
        lista.add($1.sval);
        $$ = new ParserVal(lista);
    }
    | lista_variables ID { 
        yyerror("Lista de variables: Falta la ','");}
    ;

declaracion_funcion: 
    encabezado_funcion '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' {
        String nombreFuncion = $1.sval;

        // Guardamos el ámbito antes de salir.
        String ambitoFuncion = getAmbitoActual();

        NodoFuncion nodoFn = new NodoFuncion(
            nombreFuncion,
            ambitoFuncion,
            (NodoAST) $3.obj,
            (NodoAST) $5.obj,
            (NodoAST) $7.obj
        );
        
        gestorAmbitos.salirAmbito();

        $$ = new ParserVal(nodoFn);

        System.out.println("Declaración de función");
    }
    | tipo FUNCTION ID error parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Falta parentesis de apertura en el encabezado de la función"); }
    | tipo FUNCTION ID '(' parametros_formales error sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Falta parentesis de cierre en el encabezado de la función"); }
    | tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'{ yyerror("Falta de nombre de funcion");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas error sentencias_ejecutables END ';' { yyerror("Falta la palabra reservada BEGIN de la funcion");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables error ';' { yyerror("Falta la palabra reservada END de la funcion");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Falta el ';'");}
    | tipo FUNCTION ID '(' error ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
    ;

parametros_formales: 
    parametros_formales ',' parametro_formal {System.out.println("Declaracion de parametro_formales");}
    | parametro_formal {System.out.println("Declaracion de parametro_formales");}
    ;

encabezado_funcion:
    tipo FUNCTION ID %prec ID {
        RegistroSimboloTp3 funcion = gestorAmbitos.declararFuncion($3.sval, $1.sval, lexer.getLineaActual() );
        if (funcion == null) {
            errorSemantico("Función ya declarada en el ámbito actual: " + $3.sval);
        }
        gestorAmbitos.entrarAmbitoFuncion($3.sval);
        $$ = new ParserVal($3.sval);
        }
    ;

parametro_formal: 
    tipo ID { System.out.println("Declaracion de parametro_formal"); }
    | ID { yyerror("Parametro Formal: Falta el 'tipo'");}
    | tipo { yyerror("Parametro Formal: Falta el 'ID''");}
    ;

declaracion_clase: 
    encabezado_clase BEGIN cuerpo_clase END ';' {
        System.out.println("Declaracion de clase");
        if (!pilaClases.isEmpty()) {
            pilaClases.pop();
        }
        gestorAmbitos.salirAmbito();
        NodoClase nodoClase = new NodoClase($2.sval, getAmbitoActual(), (NodoAST)$5.obj);
        $$ = new ParserVal(nodoClase);
    }
    | CLASS ID error cuerpo_clase END ';' { yyerror ("Falta el BEGIN de clase");}
    | CLASS ID BEGIN cuerpo_clase error ';' { yyerror("Falta el END de clase");}
    | CLASS ID BEGIN cuerpo_clase END { yyerror("Falta el ';' al final de la clase");}
    ;

encabezado_clase:
    CLASS ID %prec ID {
        RegistroSimboloTp3 clase = gestorAmbitos.declararClase(
            $2.sval, lexer.getLineaActual()
        );

        if (clase == null) {
            errorSemantico(
                "Clase ya declarada en el ámbito actual: " + $2.sval
            );
        }
        pilaClases.push($2.sval);
        gestorAmbitos.entrarAmbitoClase($2.sval);
    }
    ;

cuerpo_clase:
    elementos_clase sentencia_extends
    | elementos_clase
    | sentencia_extends
    |
    ;

elementos_clase: 
    elementos_clase elemento_clase
    | elemento_clase
    ;

elemento_clase: 
    declaracion_variables
    | declaracion_metodo
    | declaracion_objeto
    ;

sentencia_extends:
    EXTENDS lista_ids ';' {
        System.out.println("Sentencia EXTENDS");
        registrarClasesBase((List<String>) $2.obj);
    }
    | EXTENDS error ';' { yyerror("Sintaxis de lista de herencia invalida"); }
    | EXTENDS lista_ids { yyerror("Sentencia EXTENDS: Falta el ';'");}
    | EXTENDS ';' { yyerror("Falta lista ids");}
    ;

lista_ids:
    lista_ids ',' ID  {
        List<String> lista = (List<String>) $1.obj;
        lista.add($3.sval);
        $$ = $1;
    }
    | ID {
        List<String> lista = new ArrayList<>();
        lista.add($1.sval);
        $$ = new ParserVal(lista);
    }
    | lista_ids ID { yyerror("Lista de IDs: Falta la ','");}
    ;

declaracion_metodo: 
    tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';' {
        System.out.println("Declaracion de Metodo");
        RegistroSimboloTp3 metReg = gestorAmbitos.declararMetodo($2.sval, $1.sval, lexer.getLineaActual());
    if (metReg == null) {
        errorSemantico("Método ya declarado en el ámbito actual: " + $2.sval);
        }
    NodoMetodo nodoMetodo = new NodoMetodo($2.sval, getAmbitoActual(), (NodoAST)$4.obj, (NodoAST)$8.obj);
    $$ = new ParserVal(nodoMetodo);
    }
    | tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';' { yyerror("Falta el BEGIN en la declaracion del metodo");}
    | tipo ID '(' error ')' BEGIN sentencias_ejecutables END ';' { yyerror("Falta una ',' o tipo invalido en los parametros del metodo"); }
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';' { yyerror("Falta el END en la declaracion del metodo");}
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END { yyerror("Falta el ';' en la declaracion del metodo");}
    ;

declaracion_objeto: 
    ID lista_variables ';' {
        System.out.println("Declaracion de objeto");
        List<String> nombres = (List<String>) $2.obj;
        String tipoClase = $1.sval;

        for (String nombre : nombres) {
            RegistroSimboloTp3 obj = gestorAmbitos.declararObjeto(nombre, tipoClase, lexer.getLineaActual());
            if (obj == null) {
                errorSemantico("No se pudo declarar el objeto '" + nombre + "': clase no visible o identificador duplicado");
            }
        }
    }
    | ID lista_variables { yyerror("Declaracion Objeto: Falta el ';'");}
    ;

////////////////////////////////////////////// Sentencias ejecutables
sentencias_ejecutables: 
    sentencias_ejecutables sentencia_ejecutable {
        NodoBloque bloque = null;
        if ($1.obj != null) {
            bloque = (NodoBloque) $1.obj;
        } else {
            bloque = new NodoBloque();
        }
        if ($2.obj != null) {
            bloque.agregarSentencia((NodoAST) $2.obj);
        }
        $$ = new ParserVal(bloque);
    }
    | sentencia_ejecutable {
        NodoBloque bloque = new NodoBloque();
        if ($1.obj != null) {
            bloque.agregarSentencia((NodoAST) $1.obj);
        }
        $$ = new ParserVal(bloque);
    }
    ;

sentencia_ejecutable: 
    asignacion ';' { $$ = $1; }
    | seleccion ';' { $$ = $1; }
    | salida ';' { $$ = $1; }
    | retorno ';' { $$ = $1; }
    | iterativa ';' { $$ = $1; }
    | asignacion { yyerror("Asignacion: Falta el ';', o un operador o expresion");}
    | seleccion { yyerror("Seleccion: Falta el ';'");}
    | salida { yyerror("Salida: Falta el ';'");}
    | retorno { yyerror("Retorno: Falta el ';'");}
    | iterativa { yyerror("Iterativa: Falta el ';'");}
    ;

asignacion: 
    lado_izquierdo ASIGNACION expresion {
        if (!expresionInvalida){
            NodoAST izquierdo = (NodoAST) $1.obj;
            NodoAST derecho = (NodoAST) $3.obj;

            actualizarComptimeSiCorresponde(izquierdo, derecho);

            $$ = new ParserVal(new NodoAsignacion(izquierdo, derecho));
            System.out.println("Declaracion Asignacion");
        } else {
            expresionInvalida = false;
            $$ = new ParserVal(null);
        }
    }
    | lado_izquierdo '=' expresion { yyerror("Uso de '=' en asignacion; debe utilizar ':='."); $$ = new ParserVal(null); }
    | lado_izquierdo ASIGNACION error { yyerror("Falta expresion en la asignacion"); $$ = new ParserVal(null);}
    ;

lado_izquierdo: 
    ID { $$ = new ParserVal(new NodoHoja("ID", $1.sval, getLineaActual())); }
    | acceso_posicional {
        $$ = $1; 
        System.out.println("Declaracion de acceso_posicional");}
    | ID   indice ']'            { yyerror("Acceso posicional: Falta corchete de apertura "); $$ = new ParserVal(null);}
    ;

acceso_posicional:
    ID '[' indice ']' { $$ = new ParserVal(new NodoAccesoArreglo(new NodoHoja("ID", $1.sval, getLineaActual()), (NodoAST)$3.obj)); }
    | ID '[' ']'                { yyerror("Acceso posicional: Índice vacío"); $$ = new ParserVal(null);}
    | ID '[' error ']'          { yyerror("Acceso posicional: Índice inválido"); $$ = new ParserVal(null);}
    | ID '[' indice  error ';'  { yyerror("Acceso posicional: Falta corchete de cierre"); $$ = new ParserVal(null);}
    | ID '[' indice  error  ']' { yyerror("Acceso posicional: Argumento invalido"); $$ = new ParserVal(null);}
    ;

indice:
    ID  { $$ = new ParserVal(new NodoHoja("ID", $1.sval, getLineaActual())); }
    | CTE { $$ = new ParserVal(new NodoHoja("CTE", $1.sval, getLineaActual())); }
    ;

expresion: 
    expresion '+' termino { $$ = new ParserVal(new NodoBinario("+", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | expresion '-' termino { $$ = new ParserVal(new NodoBinario("-", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | expresion '+' error {yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | error '+' termino {yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | expresion '-' error {yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | error '-' termino {yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | termino { $$ = $1; }
    ;

termino: 
    termino '*' factor { $$ = new ParserVal(new NodoBinario("*", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | termino '/' factor { $$ = new ParserVal(new NodoBinario("/", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | termino '*' error {yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | error '*' factor {yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | error '/' factor {yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | termino '/' error {yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | factor { $$ = $1; }
    ;

factor: 
    ID { $$ = new ParserVal(new NodoHoja("ID", $1.sval, getLineaActual())); }
    | CTE { $$ = new ParserVal(new NodoHoja("CTE", $1.sval, getLineaActual())); }
    | acceso_posicional {
        $$ = $1;
        System.out.println("Acceso posicional");}
    | ID '=' '(' expresion_simple ')' {
        $$ = new ParserVal(new NodoAsignacion(new NodoHoja("ID", $1.sval, getLineaActual()), (NodoAST)$4.obj));
        System.out.println("Asignacion EXP");}
    | to_double { $$ = $1; }
    | invocacion { $$ = $1; }
    ;

to_double:
    TODF '(' expresion ')' {
        $$ = new ParserVal(new NodoUnario("TODF", (NodoAST)$3.obj));
        System.out.println("Conversion DF"); 
    }
    |TODF '('error')'                       { yyerror("Solo se pueden expresiones"); $$ = new ParserVal(null);}
    |TODF '('')'                            { yyerror("Falta expresion"); $$ = new ParserVal(null);}
    |TODF error                             { yyerror("Falta parentesis"); $$ = new ParserVal(null);}
;

expresion_simple:
    expresion_simple '+' termino_simple     { $$ = new ParserVal(new NodoBinario("+", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | expresion_simple '-' termino_simple   { $$ = new ParserVal(new NodoBinario("-", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | expresion_simple '+' error            { yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | error '+' termino_simple              { yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | expresion_simple '-' error            { yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | error '-' termino_simple              { yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | termino_simple                        { $$ = $1; }
    ;

termino_simple:
    termino_simple '*' factor_simple        { $$ = new ParserVal(new NodoBinario("*", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | termino_simple '/' factor_simple      { $$ = new ParserVal(new NodoBinario("/", (NodoAST)$1.obj, (NodoAST)$3.obj)); }
    | termino_simple '*' error              { yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | error '*' factor_simple               { yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | error '/' factor_simple               { yyerror("Falta operando 1"); $$ = new ParserVal(null);}
    | termino_simple '/' error              { yyerror("Falta operando 2"); $$ = new ParserVal(null);}
    | factor_simple                         { $$ = $1; }
  ;

factor_simple:
    ID                                      { $$ = new ParserVal(new NodoHoja("ID", $1.sval, getLineaActual())); }
  | CTE                                     { $$ = new ParserVal(new NodoHoja("CTE", $1.sval, getLineaActual())); }
  | acceso_posicional                       { $$ = $1; }
  | to_double                               { $$ = $1; }
  | invocacion                              { $$ = $1; }
  | ID '=''('expresion')'                   { yyerror("No se permite este anidamiento, solo 1 expresion"); $$ = new ParserVal(null);}
;

invocacion: 
    ID '(' parametros_reales ')' lista_constantes_opcional {
                                            $$ = new ParserVal(new NodoInvocacion($1.sval, (NodoAST)$3.obj));
                                            System.out.println("Metodo|funcion con orden opcional"); }
    | ID '.' ID '(' parametros_reales ')' lista_constantes_opcional {
                                            $$ = new ParserVal(new NodoInvocacionMetodo($1.sval, $3.sval, (NodoAST)$5.obj));
                                            System.out.println("Objeto con orden opcional"); }
    | ID '(' parametros_reales ';' lista_constantes_opcional { yyerror("Error en los parametros: Falta un ')'"); $$ = new ParserVal(null);}
    | ID '(' ')' lista_constantes_opcional { yyerror("Lista de parametros vacia."); $$ = new ParserVal(null);}
    | ID '.' ID '(' ')' lista_constantes_opcional { yyerror("Lista de parametros vacia."); expresionInvalida = true; $$ = new ParserVal(null);}
    ;

lista_constantes_opcional:
    '[' lista_constantes ']'
    | '[' error ']' { yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); }
    | 
    ;

lista_constantes:
    lista_constantes ',' CTE
    | CTE
    ;

parametros_reales: 
    parametros_reales ',' parametro_real {
        $$ = new ParserVal(new NodoBinario("PARAM", (NodoAST)$1.obj, (NodoAST)$3.obj));
    }
    | parametro_real { $$ = $1; }
    | parametros_reales parametro_real {yyerror("Falta ',' entre argumentos"); $$ = new ParserVal(null);}
    | parametros_reales ',' error parametro_real {yyerror("Elemento no valido despues de ','"); $$ = new ParserVal(null);}
    ;

parametro_real: 
    expresion { $$ = $1; }
    ;

iterativa:
    REPEAT bloque_ejecutable WHILE '(' condicion ')' {
        $$ = new ParserVal(new NodoRepeat((NodoAST)$2.obj, (NodoAST)$5.obj));
        System.out.println("Declaracion Iterativa WHILE");
    }
    | REPEAT bloque_ejecutable error '(' condicion ')' { yyerror("Falta WHILE en condicion de iteracion"); $$ = new ParserVal(null);}
    | REPEAT bloque_ejecutable WHILE '(' condicion { yyerror("Falta de parentesis de cierre en condicion iterativa"); $$ = new ParserVal(null);}
    | REPEAT bloque_ejecutable WHILE condicion ')'{ yyerror("Falta de parentesis de apertura en condicion iterativa"); $$ = new ParserVal(null);}   
    | REPEAT bloque_ejecutable WHILE condicion { yyerror("Falta de parentesis en condicion iterativa"); $$ = new ParserVal(null);}
    | REPEAT error WHILE '(' condicion ')' { yyerror("Falta de bloque ejecutable en condicion de iteracion"); $$ = new ParserVal(null);}
    | REPEAT bloque_ejecutable WHILE '(' error ')' { yyerror("error en condicion de iteracion"); $$ = new ParserVal(null);}
    ;

seleccion: 
    IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable END_IF {
        System.out.println("Declaracion Seleccion");
        $$ = new ParserVal(new NodoIF((NodoAST)$3.obj, (NodoAST)$5.obj, (NodoAST)$7.obj));
    }
    | IF '(' condicion ')' bloque_ejecutable END_IF {
        System.out.println("Declaracion Seleccion Simple");
        $$ = new ParserVal(new NodoIF((NodoAST)$3.obj, (NodoAST)$5.obj, null));
    }
    | IF '(' condicion bloque_ejecutable END_IF { yyerror("Falta de parentesis de cierre en seleccion"); $$ = new ParserVal(null);}
    | IF condicion ')' bloque_ejecutable END_IF { yyerror("Falta de parentesis de apertura en seleccion"); $$ = new ParserVal(null);} 
    | IF condicion bloque_ejecutable END_IF { yyerror("Falta de parentesis en seleccion"); $$ = new ParserVal(null);}
    | IF '('condicion ')' bloque_ejecutable error { yyerror("Falta de END_IF en seleccion"); $$ = new ParserVal(null);}
    ;

condicion: 
    expresion comparador expresion {
        System.out.println("Condicion");
        NodoAST comp = (NodoAST) $2.obj;
        $$ = new ParserVal(new NodoBinario(comp.getEtiqueta(), (NodoAST)$1.obj, (NodoAST)$3.obj));
    }
    ;

comparador: 
    IGUAL {
        $$ = new ParserVal(new NodoHoja("==", "==", getLineaActual()));
        System.out.println("Comparador");
    }
    | DISTINTO {
        $$ = new ParserVal(new NodoHoja("!=", "!=", getLineaActual()));
        System.out.println("Comparador");
    }
    | MAYORIGUAL {
        $$ = new ParserVal(new NodoHoja(">=", ">=", getLineaActual()));
        System.out.println("Comparador");
    }
    | MENORIGUAL {
        $$ = new ParserVal(new NodoHoja("<=", "<=", getLineaActual()));
        System.out.println("Comparador");
    }
    | '>' {
        $$ = new ParserVal(new NodoHoja(">", ">", getLineaActual()));
        System.out.println("Comparador");
    }
    | '<'  {
        $$ = new ParserVal(new NodoHoja("<", "<", getLineaActual()));
        System.out.println("Comparador");
    }
    ;

 bloque_ejecutable:  
    sentencia_ejecutable {
        $$ = $1;
        System.out.println("BloqueEJ1");
    }
    | BEGIN sentencias_ejecutables END {
        $$ = $2;
        System.out.println("BloqueEJ2");
    }
    | error sentencias_ejecutables END {yyerror("Declaracion de bloque ejecutable: Falta el BEGIN "); $$ = new ParserVal(null);}
    | BEGIN sentencias_ejecutables error {yyerror("Declaracion de bloque ejecutable: Falta el END "); $$ = new ParserVal(null);}
    ;

salida: 
    POUT '(' CADENA ')' {
        $$ = new ParserVal(new NodoPrint(new NodoHoja("CADENA", $3.sval, getLineaActual())));
        System.out.println("POUT cadena");
    } 
    | POUT '(' expresion ')' {
        System.out.println("POUT expresion");
        $$ = new ParserVal(new NodoPrint((NodoAST)$3.obj));
    }
    | POUT'('')' {yyerror("Falta Argumento en POUT"); $$ = new ParserVal(null);}
    | POUT '('expresion {yyerror ("Falta parentesis de cierre"); $$ = new ParserVal(null);}
    | POUT expresion ')' {yyerror ("Falta parentesis de apertura"); $$ = new ParserVal(null);}
    | POUT expresion {yyerror ("Falta parentesis"); $$ = new ParserVal(null);}
    | POUT '('CADENA {yyerror ("Falta parentesis de cierre"); $$ = new ParserVal(null);}
    | POUT CADENA ')' {yyerror ("Falta parentesis de apertura"); $$ = new ParserVal(null);}
    | POUT CADENA {yyerror ("Falta parentesis"); $$ = new ParserVal(null);}
    ;

retorno: 
    RET '(' expresion ')' {
        $$ = new ParserVal(new NodoReturn((NodoAST)$3.obj));
        System.out.println("Declaracion Retorno");
    }
    | RET error ';' {yyerror("Retorno debe tener una expresion y parentesis"); $$ = new ParserVal(null);}
    ;
%%
private AnalizadorLexicoTp3 lexer;
private TablaDeSimbolosTp3 tablaSimbolos;
private GestionAmbitoTp3 gestorAmbitos;
private boolean expresionInvalida = false;
private NodoAST raizAST;
private final Deque<String> pilaClases = new ArrayDeque<>();
private RecolectorErroresTp3 recolectorErrores;

public Parser(AnalizadorLexicoTp3 lexer){
    this.lexer = lexer;
    this.tablaSimbolos = lexer.getTablaSimbolos();
    this.gestorAmbitos = new GestionAmbitoTp3(this.tablaSimbolos);
    this.recolectorErrores = lexer.getRecolectorErroresTp3();
}

public NodoAST getRaizAST() {
    return raizAST;
}

private int yylex(){
    int token = lexer.yylex();
    if (lexer.yylval != null) {
        yylval = lexer.yylval;
    }
    return token;
}

private void declararListaVariables(List<String> nombres, String tipo, boolean comptime) {
    for (String nombre : nombres) {
        RegistroSimboloTp3 reg = gestorAmbitos.declararVariable(nombre, tipo, lexer.getLineaActual(), comptime);
        if (reg == null) {
            errorSemantico("Ya declarado en el ámbito actual. Identificador: " + nombre);
        }
    }
}

private void registrarClasesBase(List<String> clasesBase) {
    if (clasesBase == null || pilaClases.isEmpty()) {
        return;
    }

    String clase = pilaClases.peek();

    for (String base : clasesBase) {
        if (!gestorAmbitos.agregarClaseBase(clase, base)) {
            errorSemantico("Clase base no existe: " + base);
        }
    }
}

private void errorSemantico(String mensaje) {
    recolectorErrores.agregarError("Semantico",lexer.getLineaActual(),mensaje);
}

private void yyerror(String mensaje){
    recolectorErrores.agregarError("Sintactico",lexer.getLineaActual(),mensaje);
}

private void actualizarComptimeSiCorresponde(NodoAST izquierdo, NodoAST derecho) {
    if (!(izquierdo instanceof NodoHoja) || !(derecho instanceof NodoHoja)) {
        return;
    }

    NodoHoja hojaIzq = (NodoHoja) izquierdo;
    NodoHoja hojaDer = (NodoHoja) derecho;

    // Solo actualizamos asignaciones simples del tipo: ID := CTE
    if (!"ID".equals(hojaIzq.getTipoNodo()) || !"CTE".equals(hojaDer.getTipoNodo())) {
        return;
    }

    RegistroSimboloTp3 reg = tablaSimbolos.buscar(hojaIzq.getValor());

    if (reg != null && reg.esComptime()) {
        tablaSimbolos.actualizarValorComptime(hojaIzq.getValor(),hojaDer.getValor(),lexer.getLineaActual());
    }
}

private int getLineaActual() {
    return lexer.getLineaActual();
}

private String getAmbitoActual() {
    return tablaSimbolos.getAmbitoActual();
}
