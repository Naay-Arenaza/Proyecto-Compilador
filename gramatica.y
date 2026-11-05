%{
import java.io.*;
%}

/* Declaración de tokens */
%token ID CTE CADENA
%token BEGIN END
%token USHORTINT DOUBLEF
%token FUNCTION RET CLASS IF ELSE END_IF POUT REPEAT WHILE
%token ASIGNACION /* Representa := */
%token IGUAL DISTINTO MAYORIGUAL MENORIGUAL
%token COMPTIME EXTENDS TODF
%nonassoc SIN_PUNTO_Y_COMA
%nonassoc ID ';'

%%
//////////////////////////////////////////// Gramatica

programa: 
    ID sentencias_declarativas BEGIN sentencias_ejecutables END {System.out.println("Declaracion de programa");}
    | error sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Falta de nombre de programa");}
    | ID sentencias_declarativas error sentencias_ejecutables END { yyerror("Falta la palabra reservada BEGIN");}
    | ID sentencias_declarativas BEGIN sentencias_ejecutables error { yyerror("Falta la palabra reservada END");}
    ;

//////////////////////////////////////////// SENTENCIAS DECLARATIVAS
 sentencias_declarativas: 
    sentencias_declarativas sentencia_declarativa
    | 
    ;

sentencia_declarativa: 
    declaracion_variables {System.out.println("Declaracion de VAR");}
    | declaracion_variables_comptime {System.out.println("Declaracion de comptime");}
    | declaracion_funcion 
    | declaracion_clase 
    | declaracion_objeto {System.out.println("Declaracion de objeto");}
    ;

declaracion_variables: 
    tipo lista_variables ';'
    | tipo lista_variables %prec SIN_PUNTO_Y_COMA { yyerror("Declaracion de variables: Falta ';' " );}
    ;

declaracion_variables_comptime:
    COMPTIME tipo lista_variables ';' 
    | COMPTIME tipo lista_variables error { yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
    | COMPTIME lista_variables ';' { yyerror("Falta tipo en las variables en COMPTIME");}
    ;

tipo: 
    USHORTINT | DOUBLEF {System.out.println("Declaracion de tipo");}
    ;

lista_variables: 
    lista_variables ',' ID 
    | lista_variables ID { yyerror("Lista de variables: Falta la ','");}
    | ID
    ;

declaracion_funcion: 
    tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';' {System.out.println("Declaracion de funcion");}
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

parametro_formal: 
    tipo ID {System.out.println("Declaracion de parametro_formal");}
    | ID { yyerror("Parametro Formal: Falta el 'tipo'");}
    | tipo { yyerror("Parametro Formal: Falta el 'ID''");}
    ;

declaracion_clase: 
    CLASS ID BEGIN cuerpo_clase END ';' {System.out.println("Declaracion de clase");}
    | CLASS ID error cuerpo_clase END ';' { yyerror ("Falta el BEGIN de clase");}
    | CLASS ID BEGIN cuerpo_clase error ';' { yyerror("Falta el END de clase");}
    | CLASS ID BEGIN cuerpo_clase END { yyerror("Falta el ';' al final de la clase");}
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
    EXTENDS lista_ids ';' {System.out.println("Sentencia EXTENDS");}
    | EXTENDS error ';' { yyerror("Error en la lista de IDs de EXTENDS: Falta una ','"); }
    | EXTENDS lista_ids { yyerror("Sentencia EXTENDS: Falta el ';'");}
    | EXTENDS ';' { yyerror("Falta lista ids");}
    ;

lista_ids:
    lista_ids ',' ID  
    | ID 
    ;

declaracion_metodo: 
    tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';' {System.out.println("Declaracion de Metodo");}
    | tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';' { yyerror("Falta el BEGIN en la declaracion del metodo");}
    | tipo ID '(' error ')' BEGIN sentencias_ejecutables END ';' { yyerror("Falta una ',' o tipo invalido en los parametros del metodo"); }
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';' { yyerror("Falta el END en la declaracion del metodo");}
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END { yyerror("Falta el ';' en la declaracion del metodo");}
    ;

declaracion_objeto: 
    ID lista_variables ';' 
    | ID lista_variables { yyerror("Declaracion Objeto: Falta el ';'");}
    ;

////////////////////////////////////////////// Sentencias ejecutables
sentencias_ejecutables: 
    sentencias_ejecutables sentencia_ejecutable
    | sentencia_ejecutable
    ;

sentencia_ejecutable: 
    asignacion ';' {
        if (expresionInvalida != false){
            System.out.println("Declaracion Asignacion");
        }
    }
    | seleccion ';'{System.out.println("Declaracion Seleccion");}
    | salida ';' {System.out.println("Declaracion Salida");}
    | retorno ';' {System.out.println("Declaracion Retorno");}
	| iterativa ';' {System.out.println("Declaracion Iterativa");}
    | asignacion { yyerror("Asignacion: Falta el ';', o un operador o expresion");}
    | seleccion { yyerror("Seleccion: Falta el ';'");}
    | salida { yyerror("Salida: Falta el ';'");}
    | retorno { yyerror("Retorno: Falta el ';'");}
    | iterativa { yyerror("Iterativa: Falta el ';'");}
    ;

asignacion: 
    lado_izquierdo ASIGNACION expresion
    | lado_izquierdo '=' expresion { yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
    ;

lado_izquierdo: 
    ID
    | acceso_posicional {System.out.println("Declaracion de acceso_posicional");}
    ;

acceso_posicional:
    ID '[' indice ']'
    ;

indice:
    ID | CTE 
    ;

expresion: 
    expresion '+' termino
    | expresion '-' termino
    | expresion '+' error {yyerror("Falta operando 2");}
    | error '+' termino {yyerror("Falta operando 1");}
    | expresion '-' error {yyerror("Falta operando 2");}
    | error '-' termino {yyerror("Falta operando 1");}
    | termino
    ;

termino: 
    termino '*' factor
    | termino '/' factor
    | termino '*' error {yyerror("Falta operando 2");}
    | error '*' factor {yyerror("Falta operando 1");}
    | error '/' factor {yyerror("Falta operando 1");}
    | termino '/' error {yyerror("Falta operando 2");}
    | factor
    ;

factor: 
    ID
    | CTE
    | acceso_posicional {System.out.println("Acceso posicional");}
    | ID '=' '(' expresion ')' {System.out.println("Asignacion EXP");}
    | TODF '(' expresion ')'  {System.out.println("Conversion DF");}
    | invocacion
    ;

invocacion: 
    ID '(' parametros_reales ')' lista_constantes_opcional {System.out.println("Metodo|funcion con orden opcional");}
    | ID '.' ID '(' parametros_reales ')' lista_constantes_opcional {System.out.println("Objeto con orden opcional");}
    | ID '(' parametros_reales ';' lista_constantes_opcional { yyerror("Error en los parametros: Falta un ')'"); }
    | ID '(' ')' lista_constantes_opcional { yyerror("Lista de parametros vacia."); }
    | ID '.' ID '(' ')' lista_constantes_opcional { 
        yyerror("Lista de parametros vacia."); expresionInvalida = true;
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
    | parametros_reales parametro_real {yyerror("Falta ',' entre argumentos");}
    | parametros_reales ',' error parametro_real {yyerror("Elemento no valido despues de ','");}
    ;

parametro_real: 
    expresion 
    ;

iterativa:
    REPEAT bloque_ejecutable WHILE '(' condicion ')'
    | REPEAT bloque_ejecutable error '(' condicion ')' { yyerror("Falta WHILE en condicion de iteracion");}
    | REPEAT bloque_ejecutable WHILE '(' condicion { yyerror("Falta de parentesis de cierre en condicion iterativa");}
    | REPEAT bloque_ejecutable WHILE condicion ')'{ yyerror("Falta de parentesis de apertura en condicion iterativa");}   
    | REPEAT bloque_ejecutable WHILE condicion { yyerror("Falta de parentesis en condicion iterativa");}
    | REPEAT WHILE '(' condicion ')' { yyerror("Falta de bloque ejecutable en condicion de iteracion");}
    ;

seleccion: 
    IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable END_IF 
    | IF '(' condicion ')' bloque_ejecutable END_IF
    | IF '(' condicion bloque_ejecutable END_IF { yyerror("Falta de parentesis de cierre en seleccion"); }
    | IF condicion ')' bloque_ejecutable END_IF { yyerror("Falta de parentesis de apertura en seleccion");} 
    | IF condicion bloque_ejecutable END_IF { yyerror("Falta de parentesis en seleccion"); }
    | IF '('condicion ')' bloque_ejecutable error { yyerror("Falta de END_IF en seleccion");}
    ;

condicion: 
    expresion comparador expresion {System.out.println("Condicion");}
    ;

comparador: 
    IGUAL | DISTINTO | MAYORIGUAL | MENORIGUAL | '>' | '<'  {System.out.println("Comparador");}
    ;

 bloque_ejecutable:  
    sentencia_ejecutable {System.out.println("BloqueEJ1");}
    | BEGIN sentencias_ejecutables END {System.out.println("BloqueEJ2");}
    | error sentencias_ejecutables END {yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
    | BEGIN sentencias_ejecutables error {yyerror("Declaracion de bloque ejecutable: Falta el END ");}
    ;

salida: 
    POUT '(' CADENA ')' {System.out.println("POUT cadena");}
    | POUT '(' expresion ')' {System.out.println("POUT expresion");}
    | POUT'('')' {yyerror("Falta Argumento en POUT");}
    ;

retorno: 
    RET '(' expresion ')'
    | RET error ';' {yyerror("Retorno debe tener una expresion y parentesis");}
    ;
%%
private AnalizadorLexico lexer;
private boolean expresionInvalida = false;
public Parser(AnalizadorLexico lexer){
    this.lexer = lexer;
}
private int yylex(){
    return lexer.yylex();
}
private void yyerror(String mensaje){
    System.err.println("Error sintáctico: " + mensaje);
}