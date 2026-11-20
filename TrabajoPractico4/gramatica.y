%{
import java.io.*;
import TrabajoPractico4.ArbolSintactico.*;
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
    | {$$ = new ParserVal(new NodoBloque());}
    ;

sentencia_declarativa: 
    declaracion_variables 
    | declaracion_variables_comptime
    | declaracion_funcion 
    | declaracion_clase 
    | declaracion_objeto 
    ;

declaracion_variables: 
    tipo lista_variables ';' {declararListaVariables((List<String>) $2.obj, $1.sval, false);}
    | tipo lista_variables %prec SIN_PUNTO_Y_COMA { yyerror("Declaracion de variables: Falta ';' " );}
    ;

declaracion_variables_comptime:
    COMPTIME tipo lista_variables ';' { declararListaVariables((List<String>) $3.obj, $2.sval, true);}
    | COMPTIME tipo lista_variables error { yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
    | COMPTIME lista_variables ';' { yyerror("Falta tipo en las variables en COMPTIME");}
    ;

tipo: 
    USHORTINT { $$ = new ParserVal("USHORTINT");} 
    | DOUBLEF { $$ = new ParserVal("DOUBLEF");}
    ;

lista_variables: 
    lista_variables ',' ID {
        List<String> lista = (List<String>) $1.obj;
        lista.add($3.sval);
        $$ = $1;
    }
    | ID {  List<String> lista = new ArrayList<>();
            lista.add($1.sval);
            $$ = new ParserVal(lista);
    }
    | lista_variables ID { yyerror("Lista de variables: Falta la ','"); $$ = new ParserVal(null);}
    ;

declaracion_funcion: 
    encabezado_funcion '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' {
        String ambitoFuncion = getAmbitoActual();
        boolean valida = ($1.obj instanceof RegistroSimboloTp4);;
        gestorAmbitos.salirAmbito();  
        if (!valida) {
            gestorAmbitos.finalizarSupresion(); 
        }
        $$ = valida ? new ParserVal(new NodoFuncion($1.sval, ambitoFuncion, (NodoAST) $3.obj, (NodoAST) $5.obj, (NodoAST) $7.obj)) : new ParserVal(null);
    }

    | tipo FUNCTION ID error parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Falta parentesis de apertura en el encabezado de la función"); }
    | tipo FUNCTION ID '(' parametros_formales error sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Falta parentesis de cierre en el encabezado de la función"); }
    | tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'{ yyerror("Falta de nombre de funcion");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas error sentencias_ejecutables END ';' { yyerror("Falta la palabra reservada BEGIN de la funcion");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables error ';' { yyerror("Falta la palabra reservada END de la funcion");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Falta el ';'");}
    | tipo FUNCTION ID '(' error ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
    ;

encabezado_metodo:
    tipo ID %prec ID {
        RegistroSimboloTp4 metodo = gestorAmbitos.declararMetodo($2.sval, $1.sval, getLineaActual());
        if (metodo == null) {
            errorSemantico("Método ya declarado en el ámbito actual: " + $2.sval);
            gestorAmbitos.iniciarSupresion();
        }
        gestorAmbitos.entrarAmbitoFuncion($2.sval);
        ParserVal valor = new ParserVal($2.sval);
        valor.obj = metodo;
        $$ = valor;
    }
    ;

parametros_formales: 
    parametros_formales ',' parametro_formal {
        NodoBloque lista = (NodoBloque) $1.obj;
        if ($3.obj != null) {
            lista.agregarSentencia((NodoAST) $3.obj);
        }
        $$ = new ParserVal(lista);
    }
    | parametro_formal {
        NodoBloque lista = new NodoBloque();
        if ($1.obj != null) {
            lista.agregarSentencia((NodoAST) $1.obj);
        }
        $$ = new ParserVal(lista);
    }
    ;

encabezado_funcion:
    tipo FUNCTION ID %prec ID {
        RegistroSimboloTp4 funcion = gestorAmbitos.declararFuncion($3.sval, $1.sval, getLineaActual());
        if (funcion == null) {
            errorSemantico("Función ya declarada en el ámbito actual: " + $3.sval);
            gestorAmbitos.iniciarSupresion();
        }
        gestorAmbitos.entrarAmbitoFuncion($3.sval);
        ParserVal valor = new ParserVal($3.sval);
        valor.obj = funcion;    
        $$ = valor;
    }
    ;

parametro_formal: 
    tipo ID { 
        String nombre = $2.sval;
        String tipoParametro = $1.sval;
        registrarParametroFormal(nombre, tipoParametro);
        $$ = new ParserVal(new NodoHoja("PARAMETRO", tipoParametro + " " + nombre, getLineaActual()));
    }
    | ID { yyerror("Parametro Formal: Falta el 'tipo'"); $$ = new ParserVal(null);}
    | tipo { yyerror("Parametro Formal: Falta el 'ID''"); $$ = new ParserVal(null);}
    ;

declaracion_clase: 
    encabezado_clase BEGIN cuerpo_clase END ';' {
        boolean valida = ($1.obj instanceof RegistroSimboloTp4);
        String ambitoClase = getAmbitoActual();  
        if (!pilaClases.isEmpty()) {
            pilaClases.pop();
        }
        gestorAmbitos.salirAmbito();
        if (!valida) {
            gestorAmbitos.finalizarSupresion();
        }
        $$ = valida ? new ParserVal(new NodoClase($1.sval, ambitoClase, (NodoAST) $3.obj)) : new ParserVal(null);
    }
    | CLASS ID error cuerpo_clase END ';' { yyerror ("Falta el BEGIN de clase"); $$ = new ParserVal(null);}
    | CLASS ID BEGIN cuerpo_clase error ';' { yyerror("Falta el END de clase"); $$ = new ParserVal(null);}
    | CLASS ID BEGIN cuerpo_clase END { yyerror("Falta el ';' al final de la clase"); $$ = new ParserVal(null);}
    ;

encabezado_clase:
    CLASS ID %prec ID {
        RegistroSimboloTp4 clase = gestorAmbitos.declararClase($2.sval, getLineaActual());

        if (clase == null) {
            errorSemantico("Clase ya declarada en el ámbito actual: " + $2.sval);
            gestorAmbitos.iniciarSupresion();
        }
        pilaClases.push($2.sval);
        gestorAmbitos.entrarAmbitoClase($2.sval);
        ParserVal valor = new ParserVal($2.sval);
        valor.obj = clase;
        $$ = valor;
    }
    ;

cuerpo_clase:
    elementos_clase sentencia_extends { $$ = $1; }
    | elementos_clase { $$ = $1; }
    | sentencia_extends { $$ = new ParserVal(new NodoBloque()); }
    | { $$ = new ParserVal(new NodoBloque());}
    ;

elementos_clase:
    elementos_clase elemento_clase {
        NodoBloque bloque = (NodoBloque) $1.obj;
        if ($2.obj instanceof NodoAST) {
            bloque.agregarSentencia((NodoAST) $2.obj);
        }
        $$ = new ParserVal(bloque);
    }
    | elemento_clase {
        NodoBloque bloque = new NodoBloque();
        if ($1.obj instanceof NodoAST) {
            bloque.agregarSentencia((NodoAST) $1.obj);
        }
        $$ = new ParserVal(bloque);
    }
    ;

elemento_clase:
    declaracion_variables { $$ = new ParserVal(null);}
    | declaracion_metodo { $$ = $1;}
    | declaracion_objeto {  $$ = new ParserVal(null);}
    ;

sentencia_extends:
    EXTENDS lista_ids ';' {registrarClasesBase((List<String>) $2.obj);}
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
    | lista_ids ID { yyerror("Lista de IDs: Falta la ','");$$ = new ParserVal(null);}
    ;


declaracion_metodo:
    encabezado_metodo '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';' {
        String nombreMetodo = $1.sval;
        String ambitoMetodo = getAmbitoActual();
        boolean valida = ($1.obj instanceof RegistroSimboloTp4);
        gestorAmbitos.salirAmbito();
        if (!valida) {
            gestorAmbitos.finalizarSupresion();
        }
        $$ = valida ? new ParserVal( new NodoMetodo(nombreMetodo,ambitoMetodo,(NodoAST)$3.obj,(NodoAST)$6.obj)): new ParserVal(null);
    }
    | tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';' {yyerror("Falta el BEGIN en la declaracion del metodo");}
    | tipo ID '(' error ')' BEGIN sentencias_ejecutables END ';' {yyerror("Falta una ',' o tipo invalido en los parametros del metodo");}
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';' {yyerror("Falta el END en la declaracion del metodo");}
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END {yyerror("Falta el ';' en la declaracion del metodo");}
    ;


declaracion_objeto: 
    ID lista_variables ';' {
        List<String> nombres = (List<String>) $2.obj;
        String tipoClase = $1.sval;
        for (String nombre : nombres) {
            RegistroSimboloTp4 obj = gestorAmbitos.declararObjeto(nombre, tipoClase, getLineaActual());
            if (obj == null) {
                errorSemantico("No se pudo declarar el objeto '" + nombre + "': clase no visible o identificador duplicado");
            }
        }
        $$ = new ParserVal(null);
    }
    | ID lista_variables { yyerror("Declaracion Objeto: Falta el ';'"); $$ = new ParserVal(null);}
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
    | asignacion { yyerror("Asignacion: Falta el ';', o un operador o expresion");$$ = new ParserVal(null);}
    | seleccion { yyerror("Seleccion: Falta el ';'");$$ = new ParserVal(null);}
    | salida { yyerror("Salida: Falta el ';'");$$ = new ParserVal(null);}
    | retorno { yyerror("Retorno: Falta el ';'");$$ = new ParserVal(null);}
    | iterativa { yyerror("Iterativa: Falta el ';'");$$ = new ParserVal(null);}
    ;

asignacion: 
    lado_izquierdo ASIGNACION expresion {
        if (!expresionInvalida && $1.obj != null && $3.obj != null) {
            NodoAST izquierdo = (NodoAST) $1.obj;
            NodoAST derecho = (NodoAST) $3.obj;
            actualizarComptimeSiCorresponde(izquierdo, derecho);
            $$ = new ParserVal(new NodoAsignacion(izquierdo, derecho));
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
    | acceso_posicional { $$ = $1; }
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
    | acceso_posicional {$$ = $1;}
    | ID '=' '(' expresion_simple ')' {
        if ($4.obj != null) {
            $$ = new ParserVal(new NodoAsignacion(new NodoHoja("ID", $1.sval, getLineaActual()),(NodoAST)$4.obj));
        } else {
            $$ = new ParserVal(null);
        }
    }
    | to_double { $$ = $1; }
    | invocacion { $$ = $1; }
    | ID '=' '(' '(' expresion_simple ')' ')' { yyerror("Un solo parentesis"); $$ = new ParserVal(null); }
    | ID ASIGNACION '(' expresion ')'         { yyerror("Solo se utilizar ="); $$ = new ParserVal(null); }
    | ID ASIGNACION '(' '(' expresion ')' ')' { yyerror("Solo se utiliza = y un solo parentesis"); $$ = new ParserVal(null); }
    ;

to_double:
    TODF '(' expresion ')' {
        if ($3.obj != null) {
            $$ = new ParserVal(new NodoUnario("TODF", (NodoAST)$3.obj));
        } else {
            $$ = new ParserVal(null);
        }
    }
    |TODF '('error')' { yyerror("Solo se pueden expresiones"); $$ = new ParserVal(null);}
    |TODF '('')' { yyerror("Falta expresion"); $$ = new ParserVal(null);}
    |TODF error { yyerror("Falta parentesis"); $$ = new ParserVal(null);}
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
        $$ = new ParserVal(new NodoInvocacion($1.sval, (NodoListaParametros)$3.obj,(List<Integer>)$5.obj,getLineaActual(),tablaSimbolos.resolverFuncion($1.sval)));
    }
    | ID '.' ID '(' parametros_reales ')' lista_constantes_opcional {
        $$ = new ParserVal(new NodoInvocacionMetodo( new NodoHoja("ID", $1.sval, getLineaActual()), $3.sval, (NodoListaParametros)$5.obj, (List<Integer>)$7.obj, getLineaActual() ));
    }
    | ID '(' parametros_reales ';' lista_constantes_opcional { yyerror("Error en los parametros: Falta un ')' "); $$ = new ParserVal(null);}
    | ID '(' ')' lista_constantes_opcional { yyerror("Lista de parametros vacia."); $$ = new ParserVal(null);}
    | ID '.' ID '(' ')' lista_constantes_opcional { yyerror("Lista de parametros vacia."); expresionInvalida = true; $$ = new ParserVal(null);}
    ;

lista_constantes_opcional:
    '[' lista_constantes ']' {$$ = $2;}
    | '[' error ']' { yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); $$ = new ParserVal(null);}
    | { $$ = new ParserVal(null);}
    ;

lista_constantes:
    lista_constantes ',' CTE {
        List<Integer> orden = (List<Integer>)$1.obj;
        Integer posicion =  convertirPosicionParametro($3.sval);
        if (posicion == null) {
            errorSemantico( "El orden de parámetros solo admite constantes enteras positivas.");
        }
        orden.add(posicion);
        $$ = new ParserVal(orden);
    }
    | CTE {
        List<Integer> orden = new ArrayList<>();
        Integer posicion =  convertirPosicionParametro($1.sval);
        if (posicion == null) {
            errorSemantico( "El orden de parámetros solo admite constantes enteras positivas." );
        }
        orden.add(posicion);
        $$ = new ParserVal(orden);
    }
    ;

parametros_reales: 
    parametros_reales ',' parametro_real {
        NodoListaParametros lista = (NodoListaParametros)$1.obj;
        lista.agregarParametro((NodoAST)$3.obj);
        $$ = new ParserVal(lista);
    }
    | parametro_real {
        NodoListaParametros lista = new NodoListaParametros();
        lista.agregarParametro((NodoAST)$1.obj);
        $$ = new ParserVal(lista); 
    }
    | parametros_reales parametro_real {yyerror("Falta ',' entre argumentos"); $$ = new ParserVal(null);}
    | parametros_reales ',' error parametro_real {yyerror("Elemento no valido despues de ','"); $$ = new ParserVal(null);}
    ;

parametro_real: 
    expresion { $$ = $1; }
    ;

iterativa:
    REPEAT bloque_ejecutable WHILE '(' condicion ')' {
        NodoRepeat repeticion = new NodoRepeat((NodoAST)$2.obj,(NodoAST)$5.obj);
        invalidarComptimesModificadosEnControl(repeticion);
        $$ = new ParserVal(repeticion);
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
        NodoIF seleccion = new NodoIF((NodoAST)$3.obj,(NodoAST)$5.obj,(NodoAST)$7.obj);
        invalidarComptimesModificadosEnControl(seleccion);
        $$ = new ParserVal(seleccion);
    }
    | IF '(' condicion ')' bloque_ejecutable END_IF {
        NodoIF seleccion = new NodoIF((NodoAST)$3.obj,(NodoAST)$5.obj,null);
        invalidarComptimesModificadosEnControl(seleccion);
        $$ = new ParserVal(seleccion);
    }
    | IF '(' condicion bloque_ejecutable END_IF { yyerror("Falta de parentesis de cierre en seleccion"); $$ = new ParserVal(null);}
    | IF condicion ')' bloque_ejecutable END_IF { yyerror("Falta de parentesis de apertura en seleccion"); $$ = new ParserVal(null);} 
    | IF condicion bloque_ejecutable END_IF { yyerror("Falta de parentesis en seleccion"); $$ = new ParserVal(null);}
    | IF '('condicion ')' bloque_ejecutable error { yyerror("Falta de END_IF en seleccion"); $$ = new ParserVal(null);}
    ;

condicion: 
    expresion comparador expresion {
        NodoAST comp = (NodoAST) $2.obj;
        $$ = new ParserVal(new NodoBinario(comp.getEtiqueta(), (NodoAST)$1.obj, (NodoAST)$3.obj));
    }
    ;

comparador: 
    IGUAL {$$ = new ParserVal(new NodoHoja("==", "==", getLineaActual()));}
    | DISTINTO { $$ = new ParserVal(new NodoHoja("!=", "!=", getLineaActual()));}
    | MAYORIGUAL {$$ = new ParserVal(new NodoHoja(">=", ">=", getLineaActual()));}
    | MENORIGUAL {$$ = new ParserVal(new NodoHoja("<=", "<=", getLineaActual()));}
    | '>' {$$ = new ParserVal(new NodoHoja(">", ">", getLineaActual()));}
    | '<'  {$$ = new ParserVal(new NodoHoja("<", "<", getLineaActual()));}
    ;

 bloque_ejecutable:  
    sentencia_ejecutable {$$ = $1;}
    | BEGIN sentencias_ejecutables END {$$ = $2;}
    | error sentencias_ejecutables END {yyerror("Declaracion de bloque ejecutable: Falta el BEGIN "); $$ = new ParserVal(null);}
    | BEGIN sentencias_ejecutables error {yyerror("Declaracion de bloque ejecutable: Falta el END "); $$ = new ParserVal(null);}
    ;

salida: 
    POUT '(' CADENA ')' {$$ = new ParserVal(new NodoPrint(new NodoHoja("CADENA", $3.sval, getLineaActual())));} 
    | POUT '(' expresion ')' {$$ = new ParserVal(new NodoPrint((NodoAST)$3.obj));}
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
        String ambito = getAmbitoActual();
        String tipoRetorno = null;
        int separador = ambito.lastIndexOf(':');
        if (separador >= 0) {
            String nombre = ambito.substring(separador + 1);
            RegistroSimboloTp4 registro;
            if (!pilaClases.isEmpty()) {
                registro = tablaSimbolos.resolverMiembroClase(pilaClases.peek(),nombre);
            } else {
                registro = tablaSimbolos.resolverFuncion(nombre);
            }
            if (registro != null) {
                tipoRetorno = registro.getTipo();
            }
        }
        $$ = new ParserVal(new NodoReturn((NodoAST)$3.obj,tipoRetorno,getLineaActual()));
    }
    | RET error ';' {yyerror("Retorno debe tener una expresion y parentesis"); $$ = new ParserVal(null);}
    ;
%%
private AnalizadorLexicoTp4 lexer;
private TablaDeSimbolosTp4 tablaSimbolos;
private GestionAmbitoTp4 gestorAmbitos;
private boolean expresionInvalida = false;
private NodoAST raizAST;
private final Deque<String> pilaClases = new ArrayDeque<>();
private RecolectorErroresTp4 recolectorErrores;

public Parser(AnalizadorLexicoTp4 lexer){
    this.lexer = lexer;
    this.tablaSimbolos = lexer.getTablaSimbolos();
    NodoAST.setTablaSimbolos(this.tablaSimbolos);
    this.gestorAmbitos = new GestionAmbitoTp4(this.tablaSimbolos);
    this.recolectorErrores = lexer.getRecolectorErroresTp4();
    NodoAST.setRecolectorErrores(this.recolectorErrores);
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
        RegistroSimboloTp4 reg = gestorAmbitos.declararVariable(nombre, tipo, getLineaActual(), comptime);
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
    if (gestorAmbitos.estaSuprimido()) {
        return; 
    }
    recolectorErrores.agregarError("Semantico",getLineaActual(),mensaje);
}

private void yyerror(String mensaje){
    recolectorErrores.agregarError("Sintactico",getLineaActual(),mensaje);
}

private void actualizarComptimeSiCorresponde(NodoAST izquierdo, NodoAST derecho) {
    if (gestorAmbitos.estaSuprimido()) return;
    if (!(izquierdo instanceof NodoHoja)) return;

    NodoHoja hojaIzq = (NodoHoja) izquierdo;

    if (!"ID".equals(hojaIzq.getTipoNodo())) return;

    RegistroSimboloTp4 reg = tablaSimbolos.buscar(hojaIzq.getValor());

    if (reg == null || !reg.esComptime()) return;

    String resultado = evaluarValorComptime(derecho);

    if (resultado != null) {
        tablaSimbolos.actualizarValorComptime(hojaIzq.getValor(), resultado, getLineaActual());
    } else {
        reg.quitarAtributo("VALOR_COMPTIME");
    }
}

private void invalidarComptimesModificadosEnControl(NodoAST nodo) {
    if (nodo == null) {
        return;
    }
    java.util.List<RegistroSimboloTp4> modificados = new java.util.ArrayList<>();

    recolectarVariablesComptimeModificadas(nodo, modificados);

    for (RegistroSimboloTp4 registro : modificados) {
        registro.quitarAtributo("VALOR_COMPTIME");
    }
    invalidarReferenciasComptime(nodo, modificados);
}

private void recolectarVariablesComptimeModificadas( NodoAST nodo, java.util.List<RegistroSimboloTp4> modificados) {
    if (nodo == null) {
        return;
    }

    if (nodo instanceof NodoAsignacion) {
        NodoAsignacion asignacion = (NodoAsignacion) nodo;
        NodoAST variable = asignacion.getVariable();

        if (variable instanceof NodoHoja) {
            NodoHoja hoja = (NodoHoja) variable;
            RegistroSimboloTp4 registro = hoja.getSimboloVinculado();
            if (registro != null && registro.esComptime()) {
                boolean existe = false;
                for (RegistroSimboloTp4 anterior : modificados) {
                    if (anterior == registro) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    modificados.add(registro);
                }
            }
        }
        recolectarVariablesComptimeModificadas(asignacion.getExpresion(), modificados);
        return;
    }

    if (nodo instanceof NodoBloque) {
        for (NodoAST sentencia :((NodoBloque) nodo).getSentencias()) {
            recolectarVariablesComptimeModificadas(sentencia, modificados);
        }
        return;
    }

    if (nodo instanceof NodoIF) {
        NodoIF seleccion = (NodoIF) nodo;
        recolectarVariablesComptimeModificadas(seleccion.getCondicion(), modificados);
        recolectarVariablesComptimeModificadas(seleccion.getCuerpoThen(), modificados);
        recolectarVariablesComptimeModificadas(seleccion.getCuerpoElse(), modificados);
        return;
    }

    if (nodo instanceof NodoRepeat) {
        NodoRepeat repeticion = (NodoRepeat) nodo;
        recolectarVariablesComptimeModificadas(repeticion.getCuerpo(), modificados);
        recolectarVariablesComptimeModificadas(repeticion.getCondicion(), modificados);
        return;
    }

    if (nodo instanceof NodoBinario) {
        NodoBinario binario = (NodoBinario) nodo;
        recolectarVariablesComptimeModificadas(binario.getIzq(), modificados);
        recolectarVariablesComptimeModificadas(binario.getDer(), modificados);
        return;
    }

    if (nodo instanceof NodoUnario) {
        recolectarVariablesComptimeModificadas(((NodoUnario) nodo).getOperando(), modificados);
        return;
    }

    if (nodo instanceof NodoAccesoArreglo) {
        NodoAccesoArreglo acceso = (NodoAccesoArreglo) nodo;
        recolectarVariablesComptimeModificadas(acceso.getArreglo(), modificados);
        recolectarVariablesComptimeModificadas(acceso.getIndice(), modificados);
        return;
    }

    if (nodo instanceof NodoPrint) {
        recolectarVariablesComptimeModificadas(((NodoPrint) nodo).getContenido(), modificados);
        return;
    }

    if (nodo instanceof NodoReturn) {
        recolectarVariablesComptimeModificadas(((NodoReturn) nodo).getExpresionRetorno(), modificados);
        return;
    }

    if (nodo instanceof NodoListaParametros) {
        for (NodoAST parametro :((NodoListaParametros) nodo).getParametros()) {
            recolectarVariablesComptimeModificadas(parametro, modificados);
        }
        return;
    }

    if (nodo instanceof NodoInvocacion) {
        recolectarVariablesComptimeModificadas(((NodoInvocacion) nodo).getParametros(), modificados);
        return;
    }

    if (nodo instanceof NodoInvocacionMetodo) {
        recolectarVariablesComptimeModificadas(((NodoInvocacionMetodo) nodo).getObjeto(), modificados);
        recolectarVariablesComptimeModificadas(((NodoInvocacionMetodo) nodo).getParametros(), modificados);
    }
}

private void invalidarReferenciasComptime(NodoAST nodo,java.util.List<RegistroSimboloTp4> modificados) {
    if (nodo == null) {
        return;
    }

    if (nodo instanceof NodoHoja) {
        NodoHoja hoja = (NodoHoja) nodo;
        RegistroSimboloTp4 registro = hoja.getSimboloVinculado();
        if (registro != null) {
            for (RegistroSimboloTp4 modificado : modificados) {
                if (registro == modificado) {
                    hoja.invalidarValorComptime();
                    break;
                }
            }
        }
        return;
    }

    if (nodo instanceof NodoAsignacion) {
        NodoAsignacion asignacion = (NodoAsignacion) nodo;
        invalidarReferenciasComptime(asignacion.getVariable(), modificados);
        invalidarReferenciasComptime(asignacion.getExpresion(), modificados);
        return;
    }

    if (nodo instanceof NodoBloque) {
        for (NodoAST sentencia : ((NodoBloque) nodo).getSentencias()) {
            invalidarReferenciasComptime(sentencia, modificados);
        }
        return;
    }

    if (nodo instanceof NodoIF) {
        NodoIF seleccion = (NodoIF) nodo;
        invalidarReferenciasComptime(seleccion.getCondicion(), modificados);
        invalidarReferenciasComptime(seleccion.getCuerpoThen(), modificados);
        invalidarReferenciasComptime(seleccion.getCuerpoElse(), modificados);
        return;
    }

    if (nodo instanceof NodoRepeat) {
        NodoRepeat repeticion = (NodoRepeat) nodo;
        invalidarReferenciasComptime(repeticion.getCuerpo(), modificados);
        invalidarReferenciasComptime(repeticion.getCondicion(), modificados);
        return;
    }

    if (nodo instanceof NodoBinario) {
        NodoBinario binario = (NodoBinario) nodo;
        invalidarReferenciasComptime(binario.getIzq(), modificados);
        invalidarReferenciasComptime(binario.getDer(), modificados);
        return;
    }

    if (nodo instanceof NodoUnario) {
        invalidarReferenciasComptime(((NodoUnario) nodo).getOperando(), modificados);
        return;
    }

    if (nodo instanceof NodoAccesoArreglo) {
        NodoAccesoArreglo acceso = (NodoAccesoArreglo) nodo;

        invalidarReferenciasComptime(acceso.getArreglo(), modificados);
        invalidarReferenciasComptime(acceso.getIndice(), modificados);
        return;
    }

    if (nodo instanceof NodoPrint) {
        invalidarReferenciasComptime(((NodoPrint) nodo).getContenido(), modificados);
        return;
    }

    if (nodo instanceof NodoReturn) {
        invalidarReferenciasComptime(((NodoReturn) nodo).getExpresionRetorno(), modificados);
        return;
    }

    if (nodo instanceof NodoListaParametros) {
        for (NodoAST parametro :((NodoListaParametros) nodo).getParametros()) {
            invalidarReferenciasComptime(parametro, modificados);
        }
        return;
    }

    if (nodo instanceof NodoInvocacion) {
        invalidarReferenciasComptime(((NodoInvocacion) nodo).getParametros(), modificados);
        return;
    }

    if (nodo instanceof NodoInvocacionMetodo) {
        NodoInvocacionMetodo invocacion =(NodoInvocacionMetodo) nodo;
        invalidarReferenciasComptime(invocacion.getObjeto(), modificados);
        invalidarReferenciasComptime(invocacion.getParametros(), modificados);
    }
}

private String evaluarValorComptime(NodoAST nodo) {
    if (nodo == null) return null;

    if (nodo instanceof NodoHoja) {
        NodoHoja hoja = (NodoHoja) nodo;
        if ("CTE".equals(hoja.getTipoNodo())) {
            return hoja.getValor();
        }
        if ("ID".equals(hoja.getTipoNodo())) {
            return tablaSimbolos.obtenerValorComptime(hoja.getValor());
        }
        return null;
    }

    if (nodo instanceof NodoBinario) {
        NodoBinario binario = (NodoBinario) nodo;

        String valorIzq = evaluarValorComptime(binario.getIzq());
        String valorDer = evaluarValorComptime(binario.getDer());

        if (valorIzq == null || valorDer == null) return null;

        if (!valorIzq.endsWith("$us") || !valorDer.endsWith("$us")) {
            return null;
        }

        try {
            long a = Long.parseLong(valorIzq.substring(0, valorIzq.length() - 3));
            long b = Long.parseLong(valorDer.substring(0, valorDer.length() - 3));
            long resultado;

            switch (binario.getEtiqueta()) {
                case "+":
                    resultado = Math.addExact(a, b);
                    break;
                case "-":
                    resultado = Math.subtractExact(a, b);
                    break;
                case "*":
                    resultado = Math.multiplyExact(a, b);
                    break;
                case "/":
                    if (b == 0) return null;
                    resultado = a / b;
                    break;
                default:
                    return null;
            }
            if (resultado < 0 || resultado > 255) {
                return null;
            }
            return resultado + "$us";
        } catch (ArithmeticException | NumberFormatException e) {
            return null;
        }
    }
    return null;
}

private int getLineaActual() {
    return lexer.getLineaActual();
}

private String getAmbitoActual() {
    return tablaSimbolos.getAmbitoActual();
}

private void registrarParametroFormal(String nombre, String tipo) {
    if (gestorAmbitos.estaSuprimido()) {
        return;
    }
    String ambitoActual = tablaSimbolos.getAmbitoActual();
    int separador = ambitoActual.lastIndexOf(':');
    if (separador < 0) {
        errorSemantico("Parámetro formal fuera de una función o método: " + nombre);
        return;
    }
    String nombreFuncion = ambitoActual.substring(separador + 1);
    String ambitoDeclaracion = ambitoActual.substring(0, separador);
    RegistroSimboloTp4 parametro = gestorAmbitos.declararParametro(nombre,tipo, getLineaActual());
    if (parametro == null) {
        errorSemantico("Parámetro formal ya declarado en el ámbito actual: " + nombre);
        return;
    }
    boolean agregado = tablaSimbolos.agregarParametroFormal(nombreFuncion,nombre,tipo, getLineaActual(),ambitoDeclaracion);
    if (!agregado) {
        errorSemantico("No se pudo registrar el parámetro en la firma de "+ nombreFuncion + ": " + nombre);
    }
}

private Integer convertirPosicionParametro(String lexema) {
    if (lexema == null) {
        return null;
    }
    int separadorTipo = lexema.indexOf('$');
    String numero = separadorTipo >= 0? lexema.substring(0, separadorTipo): lexema;
    try {
        int posicion = Integer.parseInt(numero);
        return posicion > 0 ? posicion : null;

    } catch (NumberFormatException e) {
        return null;
    }
}
