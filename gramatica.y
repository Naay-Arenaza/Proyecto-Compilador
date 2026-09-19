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
    | declaracion_funcion {System.out.println("Declaracion de funcion");}
    | declaracion_clase {System.out.println("Declaracion de clase");}
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
    USHORTINT | DOUBLEF 
    ;

lista_variables: 
    lista_variables ',' ID
    | lista_variables ID { yyerror("Lista de variables: Falta la ','");}
    | ID
    ;

declaracion_funcion: 
    tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'
    | tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'{ yyerror("Declaracion de funcion: Falta de nombre de programa");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas error sentencias_ejecutables END ';' { yyerror("Declaracion de funcion: Falta la palabra reservada BEGIN");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables error ';' { yyerror("Declaracion de funcion: Falta la palabra reservada END");}
    | tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END { yyerror("Declaracion de funcion: Falta el ';'");}
    ;

parametros_formales: 
    parametros_formales ',' parametro_formal
    | parametros_formales error parametro_formal { yyerror("Parametros formales: Falta la ','");} 
    | parametro_formal
    ;

parametro_formal: 
    tipo ID 
    | ID { yyerror("Parametro Formal: Falta el 'tipo''");}
    | tipo { yyerror("Parametro Formal: Falta el 'ID''");}
    ;

declaracion_clase: 
    CLASS ID BEGIN cuerpo_clase END ';'
    | CLASS ID error cuerpo_clase END ';' { yyerror ("Declaracion de clase: Falta el BEGIN");}
    | CLASS ID BEGIN cuerpo_clase error ';' { yyerror("Declaracion de clase: Falta el END");}
    | CLASS ID BEGIN cuerpo_clase END { yyerror("Declaracion de clase: Falta el ';'");}
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
    | EXTENDS lista_ids { yyerror("Sentencia EXTENDS: Falta el ';'");}
    | EXTENDS ';' { yyerror("Falta lista ids");}
    ;

lista_ids:
    lista_ids ',' ID
    | lista_ids ID { yyerror("Lista ids: Falta la ','");}  
    | ID 
    ;

declaracion_metodo: 
    tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';' {System.out.println("METODO");}
    | tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';' { yyerror("Declaracion de metodo: Falta el BEGIN");}
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';' { yyerror("Declaracion de metodo: Falta el END");}
    | tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END { yyerror("Declaracion de metodo: Falta el ';'");}
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
    asignacion ';' {System.out.println("Asignacion");}
    | seleccion ';'{System.out.println("Seleccion");}
    | salida ';' {System.out.println("Salida");}
    | retorno ';' {System.out.println("Retorno");}
	| iterativa ';' {System.out.println("Iterativa");}
    | asignacion { yyerror("Asignacion: Falta el ';'");}
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
    | acceso_posicional 
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
    | expresion '+' error {yyerror("Falta operando");}
    | error '+' termino {yyerror("Falta operando");}
    | expresion '-' error {yyerror("Falta operando");}
    | error '-' termino {yyerror("Falta operando");}
    | termino
    ;

termino: 
    termino '*' factor
    | termino '/' factor
    | termino '*' error {yyerror("Falta operando");}
    | error '*' factor {yyerror("Falta operando");}
    | termino '/' error {yyerror("Falta operando");}
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
    ;

lista_constantes_opcional:
    '[' lista_constantes ']' 
    | 
    ;

lista_constantes:
    lista_constantes ',' CTE
    | lista_constantes CTE { yyerror("Lista constantes: Falta la ','");}  
    | CTE
    ;

parametros_reales: 
    parametros_reales ',' parametro_real
    | parametros_reales error parametro_real { yyerror("Parametro Reales: Falta la ,");}  
    | parametro_real
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
    IGUAL | DISTINTO | MAYORIGUAL | MENORIGUAL  {System.out.println("Comparador");}
    ;

 bloque_ejecutable:  
    sentencia_ejecutable {System.out.println("BloqueEJ1");}
    | BEGIN sentencias_ejecutables END {System.out.println("BloqueEJ2");}
    | error sentencias_ejecutables END { yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
    | BEGIN sentencias_ejecutables error { yyerror("Declaracion de bloque ejecutable: Falta el END ");}
    ;

salida: 
    POUT '(' CADENA ')' {System.out.println("POUT cadena");}
    | POUT '(' expresion ')' {System.out.println("POUT expresion");}
    | POUT'('')' {yyerror("Falta Argumento en POUT");}
    ;

retorno: 
    RET '(' expresion ')' 
    ;
%%
private AnalizadorLexico lexer;

public Parser(AnalizadorLexico lexer){
    this.lexer = lexer;
}
private int yylex(){
    return lexer.yylex();
}
private void yyerror(String mensaje){
    System.err.println("Error sintáctico: " + mensaje);
}