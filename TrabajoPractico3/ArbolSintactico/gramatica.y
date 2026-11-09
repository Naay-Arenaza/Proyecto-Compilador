%{
import java.io.*;
import Nodos.*;
%}

/* Declaración de la estructura de la pila (NodoAST para árboles) */
%union {
    public String sval;
    public NodoAST nodo;
}

/* Declaración de tokens con valor semántico (String) */
%token <sval> ID CTE CADENA
%token BEGIN END
%token USHORTINT DOUBLEF
%token FUNCTION RET CLASS IF ELSE END_IF POUT REPEAT WHILE
%token ASIGNACION /* Representa := */
%token IGUAL DISTINTO MAYORIGUAL MENORIGUAL
%token COMPTIME EXTENDS TODF

/* Precedencias */
%nonassoc SIN_PUNTO_Y_COMA
%nonassoc ID ';'

/* Mapeo de reglas no terminales a su tipo de NodoAST */
%type <nodo> programa sentencias_ejecutables sentencia_ejecutable parametros_reales parametro_real
%type <nodo> asignacion lado_izquierdo acceso_posicional indice expresion termino factor
%type <nodo> expresion_simple termino_simple factor_simple to_double invocacion
%type <nodo> seleccion iterativa condicion comparador salida retorno bloque_ejecutable

%%
//////////////////////////////////////////// Gramática

programa: 
    ID sentencias_declarativas BEGIN sentencias_ejecutables END {
        $$ = new NodoPrograma($1, $4);
        raizAST = $$;
        System.out.println("Declaración de programa y AST generado");
    }
    | error sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Falta de nombre de programa"); }
    | ID sentencias_declarativas sentencias_ejecutables END { yyerror("Falta la palabra reservada BEGIN"); }
    | ID sentencias_declarativas BEGIN sentencias_ejecutables error { yyerror("Falta la palabra reservada END"); }
    ;

//////////////////////////////////////////// SENTENCIAS DECLARATIVAS
sentencias_declarativas: 
    sentencias_declarativas sentencia_declarativa
    | 
    ;

sentencia_declarativa: 
    declaracion_variables 
    | declaracion_variables_comptime
    | declaracion_funcion 
    | declaracion_clase 
    | declaracion_objeto 
    ;

declaracion_variables: 
    tipo lista_variables ';' { System.out.println("Declaracion de VAR"); }
    | tipo lista_variables %prec SIN_PUNTO_Y_COMA { yyerror("Declaracion de variables: Falta ';' "); }
    ;

declaracion_variables_comptime:
    COMPTIME tipo lista_variables ';' { System.out.println("Declaracion de comptime"); }
    | COMPTIME tipo lista_variables error { yyerror("Declaracion de variables COMPTIME: Falta el ';'"); }
    | COMPTIME lista_variables ';' { yyerror("Falta tipo en las variables en COMPTIME"); }
    ;

tipo: 
    USHORTINT { System.out.println("Declaracion de tipo"); } 
    | DOUBLEF { System.out.println("Declaracion de tipo"); }
    ;

lista_variables: 
    lista_variables ',' ID 
    | lista_variables ID { yyerror("Lista de variables: Falta la ','"); }
    | ID
    ;

declaracion_funcion: 
    tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { System.out.println("Declaracion de funcion"); }
    | tipo FUNCTION ID error parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Falta parentesis de apertura en el encabezado de la función"); }
    | tipo FUNCTION ID '(' parametros_formales error sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Falta parentesis de cierre en el encabezado de la función"); }
    | tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'{ yyerror("Falta de nombre de funcion"); }
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas error sentencias_ejecutables END ';' { yyerror("Falta la palabra reservada BEGIN de la funcion"); }
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables error ';' { yyerror("Falta la palabra reservada END de la funcion"); }
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Falta el ';'"); }
    | tipo FUNCTION ID '(' error ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' { yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
    ;

parametros_formales: 
    parametros_formales ',' parametro_formal
    | parametro_formal
    ;

parametro_formal: 
    tipo ID
    | ID { yyerror("Parametro Formal: Falta el 'tipo'"); }
    | tipo { yyerror("Parametro Formal: Falta el 'ID'"); }
    ;

declaracion_clase: 
    CLASS ID BEGIN cuerpo_clase END ';'
    | CLASS ID error cuerpo_clase END ';' { yyerror("Falta el BEGIN de clase"); }
    | CLASS ID BEGIN cuerpo_clase error ';' { yyerror("Falta el END de clase"); }
    | CLASS ID BEGIN cuerpo_clase END { yyerror("Falta el ';' al final de la clase"); }
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
    ;

sentencia_extends:
    EXTENDS lista_ids ';'
    | EXTENDS error ';' { yyerror("Sintaxis de lista de herencia invalida"); }
    | EXTENDS lista_ids { yyerror("Sentencia EXTENDS: Falta el ';'"); }
    | EXTENDS ';' { yyerror("Falta lista ids"); }
    ;

lista_ids:
    lista_ids ',' ID  
    | ID 
    | lista_ids ID { yyerror("Lista de IDs: Falta la ','"); }
    ;

declaracion_metodo: 
    tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';'
    | tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';' { yyerror("Falta el BEGIN en la declaracion del metodo"); }
    | tipo ID '(' error ')' BEGIN sentencias_ejecutables END ';' { yyerror("Falta una ',' o tipo invalido en los parametros del metodo"); }
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';' { yyerror("Falta el END en la declaracion del metodo"); }
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END { yyerror("Falta el ';' en la declaracion del metodo"); }
    ;

declaracion_objeto: 
    ID lista_variables ';'
    | ID lista_variables { yyerror("Declaracion Objeto: Falta el ';'"); }
    ;

////////////////////////////////////////////// Sentencias Ejecutables

sentencias_ejecutables: 
    sentencias_ejecutables sentencia_ejecutable {
        NodoBloque bloque = (NodoBloque) $1;
        bloque.agregarSentencia($2);
        $$ = bloque;
    }
    | sentencia_ejecutable {
        NodoBloque bloque = new NodoBloque();
        bloque.agregarSentencia($1);
        $$ = bloque;
    }
    ;

sentencia_ejecutable: 
    asignacion ';'      { $$ = $1; }
    | seleccion ';'     { $$ = $1; }
    | salida ';'        { $$ = $1; }
    | retorno ';'       { $$ = $1; }
    | iterativa ';'     { $$ = $1; }
    | asignacion        { yyerror("Asignacion: Falta el ';', o un operador o expresion"); }
    | seleccion         { yyerror("Seleccion: Falta el ';'"); }
    | salida            { yyerror("Salida: Falta el ';'"); }
    | retorno           { yyerror("Retorno: Falta el ';'"); }
    | iterativa         { yyerror("Iterativa: Falta el ';'"); }
    ;

asignacion: 
    lado_izquierdo ASIGNACION expresion {
        if (!expresionInvalida) {
            $$ = new NodoAsignacion($1, $3);
        }
    }
    | lado_izquierdo '=' expresion { yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
    ;

lado_izquierdo: 
    ID                          { $$ = new NodoHoja("ID", $1); }
    | acceso_posicional         { $$ = $1; }
    | ID indice ']'            { yyerror("Acceso posicional: Falta corchete de apertura "); }
    ;

acceso_posicional:
    ID '[' indice ']'           { $$ = new NodoAccesoArreglo(new NodoHoja("ID", $1), $3); }
    | ID '[' ']'                { yyerror("Acceso posicional: Índice vacío"); }
    | ID '[' error ']'          { yyerror("Acceso posicional: Índice inválido"); }
    | ID '[' indice error ';'   { yyerror("Acceso posicional: Falta corchete de cierre"); }
    | ID '[' indice error ']'   { yyerror("Acceso posicional: Argumento invalido"); }
    ;

indice:
    ID                          { $$ = new NodoHoja("ID", $1); }
    | CTE                       { $$ = new NodoHoja("CTE", $1); }
    ;

expresion: 
    expresion '+' termino       { $$ = new NodoBinario("+", $1, $3); }
    | expresion '-' termino     { $$ = new NodoBinario("-", $1, $3); }
    | expresion '+' error       { yyerror("Falta operando 2"); }
    | error '+' termino         { yyerror("Falta operando 1"); }
    | expresion '-' error       { yyerror("Falta operando 2"); }
    | error '-' termino         { yyerror("Falta operando 1"); }
    | termino                   { $$ = $1; }
    ;

termino: 
    termino '*' factor          { $$ = new NodoBinario("*", $1, $3); }
    | termino '/' factor        { $$ = new NodoBinario("/", $1, $3); }
    | termino '*' error         { yyerror("Falta operando 2"); }
    | error '*' factor          { yyerror("Falta operando 1"); }
    | error '/' factor          { yyerror("Falta operando 1"); }
    | termino '/' error         { yyerror("Falta operando 2"); }
    | factor                    { $$ = $1; }
    ;

factor: 
    ID                          { $$ = new NodoHoja("ID", $1); }
    | CTE                       { $$ = new NodoHoja("CTE", $1); }
    | acceso_posicional         { $$ = $1; }
    | ID '=' '(' expresion_simple ')' { $$ = new NodoAsignacion(new NodoHoja("ID", $1), $4); }
    | to_double                 { $$ = $1; }
    | invocacion                { $$ = $1; }
    ;

to_double:
    TODF '(' expresion ')'      { $$ = new NodoUnario("TODF", $3); }
    | TODF '(' error ')'        { yyerror("Solo se pueden expresiones"); }
    | TODF '(' ')'              { yyerror("Falta expresion"); }
    | TODF error                { yyerror("Falta parentesis"); }
    ;

expresion_simple:
    expresion_simple '+' termino_simple     { $$ = new NodoBinario("+", $1, $3); }
    | expresion_simple '-' termino_simple   { $$ = new NodoBinario("-", $1, $3); }
    | expresion_simple '+' error            { yyerror("Falta operando 2"); }
    | error '+' termino_simple              { yyerror("Falta operando 1"); }
    | expresion_simple '-' error            { yyerror("Falta operando 2"); }
    | error '-' termino_simple              { yyerror("Falta operando 1"); }
    | termino_simple                        { $$ = $1; }
    ;

termino_simple:
    termino_simple '*' factor_simple        { $$ = new NodoBinario("*", $1, $3); }
    | termino_simple '/' factor_simple      { $$ = new NodoBinario("/", $1, $3); }
    | termino_simple '*' error              { yyerror("Falta operando 2"); }
    | error '*' factor_simple               { yyerror("Falta operando 1"); }
    | error '/' factor_simple               { yyerror("Falta operando 1"); }
    | termino_simple '/' error              { yyerror("Falta operando 2"); }
    | factor_simple                         { $$ = $1; }
    ;

factor_simple:
    ID                                      { $$ = new NodoHoja("ID", $1); }
    | CTE                                   { $$ = new NodoHoja("CTE", $1); }
    | acceso_posicional                     { $$ = $1; }
    | to_double                             { $$ = $1; }
    | invocacion                            { $$ = $1; }
    | ID '=' '(' expresion ')'              { yyerror("No se permite este anidamiento, solo 1 expresion"); }
    ;

invocacion: 
    ID '(' parametros_reales ')' lista_constantes_opcional                  { $$ = new NodoInvocacion($1, $3); }
    | ID '.' ID '(' parametros_reales ')' lista_constantes_opcional         { $$ = new NodoInvocacionMetodo($1, $3, $5); }
    | ID '(' parametros_reales ';' lista_constantes_opcional                { yyerror("Error en los parametros: Falta un ')'"); }
    | ID '(' ')' lista_constantes_opcional                                  { yyerror("Lista de parametros vacia."); }
    | ID '.' ID '(' ')' lista_constantes_opcional { 
        yyerror("Lista de parametros vacia."); 
        expresionInvalida = true;
    }
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
    parametros_reales ',' parametro_real
    | parametro_real
    | parametros_reales parametro_real         { yyerror("Falta ',' entre argumentos"); }
    | parametros_reales ',' error parametro_real { yyerror("Elemento no valido despues de ','"); }
    ;

parametro_real: 
    expresion
    ;

iterativa:
    REPEAT bloque_ejecutable WHILE '(' condicion ')' { $$ = new NodoRepeat($2, $5); }
    | REPEAT bloque_ejecutable error '(' condicion ')' { yyerror("Falta WHILE en condicion de iteracion"); }
    | REPEAT bloque_ejecutable WHILE '(' condicion { yyerror("Falta de parentesis de cierre en condicion iterativa"); }
    | REPEAT bloque_ejecutable WHILE condicion ')' { yyerror("Falta de parentesis de apertura en condicion iterativa"); }   
    | REPEAT bloque_ejecutable WHILE condicion { yyerror("Falta de parentesis en condicion iterativa"); }
    | REPEAT error WHILE '(' condicion ')' { yyerror("Falta de bloque ejecutable en condicion de iteracion"); }
    | REPEAT bloque_ejecutable WHILE '(' error ')' { yyerror("error en condicion de iteracion"); }
    ;

seleccion: 
    IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable END_IF { $$ = new NodoIf($3, $5, $7); }
    | IF '(' condicion ')' bloque_ejecutable END_IF                     { $$ = new NodoIf($3, $5, null); }
    | IF '(' condicion bloque_ejecutable END_IF                         { yyerror("Falta de parentesis de cierre en seleccion"); }
    | IF condicion ')' bloque_ejecutable END_IF                         { yyerror("Falta de parentesis de apertura en seleccion"); } 
    | IF condicion bloque_ejecutable END_IF                             { yyerror("Falta de parentesis en seleccion"); }
    | IF '(' condicion ')' bloque_ejecutable error                      { yyerror("Falta de END_IF en seleccion"); }
    ;

condicion: 
    expresion comparador expresion { $$ = new NodoBinario($2.getEtiqueta(), $1, $3); }
    ;

comparador: 
    IGUAL           { $$ = new NodoHoja("==", "=="); }
    | DISTINTO      { $$ = new NodoHoja("!=", "!="); }
    | MAYORIGUAL    { $$ = new NodoHoja(">=", ">="); }
    | MENORIGUAL    { $$ = new NodoHoja("<=", "<="); }
    | '>'           { $$ = new NodoHoja(">", ">"); }
    | '<'           { $$ = new NodoHoja("<", "<"); }
    ;

bloque_ejecutable:  
    sentencia_ejecutable                { $$ = $1; }
    | BEGIN sentencias_ejecutables END  { $$ = $2; }
    | error sentencias_ejecutables END  { yyerror("Declaracion de bloque ejecutable: Falta el BEGIN "); }
    | BEGIN sentencias_ejecutables error { yyerror("Declaracion de bloque ejecutable: Falta el END "); }
    ;

salida: 
    POUT '(' CADENA ')'        { $$ = new NodoPrint(new NodoHoja("CADENA", $3)); }
    | POUT '(' expresion ')'   { $$ = new NodoPrint($3); }
    | POUT '(' ')'             { yyerror("Falta Argumento en POUT"); }
    | POUT '(' expresion       { yyerror("Falta parentesis de cierre"); }
    | POUT expresion ')'       { yyerror("Falta parentesis de apertura"); }
    | POUT expresion           { yyerror("Falta parentesis"); }
    | POUT '(' CADENA          { yyerror("Falta parentesis de cierre"); }
    | POUT CADENA ')'          { yyerror("Falta parentesis de apertura"); }
    | POUT CADENA              { yyerror("Falta parentesis"); }
    ;

retorno: 
    RET '(' expresion ')'      { $$ = new NodoReturn($3); }
    | RET error ';'            { yyerror("Retorno debe tener una expresion y parentesis"); }
    ;
%%

private AnalizadorLexicoTp2 lexer;
private boolean expresionInvalida = false;
private NodoAST raizAST;

public Parser(AnalizadorLexicoTp2 lexer) {
    this.lexer = lexer;
}

public NodoAST getRaizAST() {
    return raizAST;
}

private int yylex() {
    int token = lexer.yylex();
    // Pasa la representación String del token a yylval para tokens con valor semántico
    if (lexer.getVal() != null) {
        yylval = new ParserVal(lexer.getVal());
    }
    return token;
}

private void yyerror(String mensaje) {
    System.err.println("Error sintáctico en la linea " + lexer.getLineaActual() + ": " + mensaje);
}