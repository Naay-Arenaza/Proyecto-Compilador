package TrabajoPractico3;
//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
import java.io.*;
import TrabajoPractico3.ArbolSintactico.*;
import java.util.*;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short BEGIN=260;
public final static short END=261;
public final static short USHORTINT=262;
public final static short DOUBLEF=263;
public final static short FUNCTION=264;
public final static short RET=265;
public final static short CLASS=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short END_IF=269;
public final static short POUT=270;
public final static short REPEAT=271;
public final static short WHILE=272;
public final static short ASIGNACION=273;
public final static short IGUAL=274;
public final static short DISTINTO=275;
public final static short MAYORIGUAL=276;
public final static short MENORIGUAL=277;
public final static short COMPTIME=278;
public final static short EXTENDS=279;
public final static short TODF=280;
public final static short SIN_PUNTO_Y_COMA=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    3,    3,    3,
    3,    4,    4,    5,    5,    5,    9,    9,   10,   10,
   10,    6,    6,    6,    6,    6,    6,    6,    6,   12,
   12,   11,   13,   13,   13,    7,    7,    7,    7,   14,
   15,   15,   15,   15,   16,   16,   18,   18,   18,   17,
   17,   17,   17,   20,   20,   20,   19,   19,   19,   19,
   19,    8,    8,    2,    2,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   22,   22,   22,   27,   27,
   27,   29,   29,   29,   29,   29,   30,   30,   28,   28,
   28,   28,   28,   28,   28,   31,   31,   31,   31,   31,
   31,   31,   32,   32,   32,   32,   32,   32,   34,   34,
   34,   34,   33,   33,   33,   33,   33,   33,   33,   36,
   36,   36,   36,   36,   36,   36,   37,   37,   37,   37,
   37,   37,   35,   35,   35,   35,   35,   39,   39,   39,
   40,   40,   38,   38,   38,   38,   41,   26,   26,   26,
   26,   26,   26,   26,   23,   23,   23,   23,   23,   23,
   43,   44,   44,   44,   44,   44,   44,   42,   42,   42,
   42,   24,   24,   24,   24,   24,   24,   24,   24,   24,
   25,   25,
};
final static short yylen[] = {                            2,
    5,    5,    4,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    1,
    2,    9,   11,   11,   11,   11,   11,   10,   11,    3,
    1,    3,    2,    1,    1,    5,    6,    6,    5,    2,
    2,    1,    1,    0,    2,    1,    1,    1,    1,    3,
    3,    2,    2,    3,    1,    2,    9,    9,    9,    9,
    8,    3,    2,    2,    1,    2,    2,    2,    2,    2,
    1,    1,    1,    1,    1,    3,    3,    3,    1,    1,
    3,    4,    3,    4,    5,    5,    1,    1,    3,    3,
    3,    3,    3,    3,    1,    3,    3,    3,    3,    3,
    3,    1,    1,    1,    1,    5,    1,    1,    4,    4,
    3,    2,    3,    3,    3,    3,    3,    3,    1,    3,
    3,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    5,    5,    7,    5,    4,    6,    3,    3,    0,
    3,    1,    3,    1,    2,    4,    1,    6,    6,    5,
    5,    4,    6,    6,    8,    6,    5,    5,    4,    6,
    3,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    4,    4,    3,    3,    3,    2,    3,    3,    2,
    4,    3,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   65,    0,    0,
    0,    0,    0,    0,   80,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   88,    0,    0,
    0,    0,    0,    0,    0,  104,    0,    0,    0,  105,
    0,  102,  107,  108,    0,    0,    0,    0,    0,    0,
  168,    0,    3,   64,   66,   67,   68,   69,   70,    0,
    0,   21,   62,    0,   87,    2,    0,    0,    0,   16,
    0,    0,   12,   34,    0,    0,   31,    0,   47,   49,
    0,    0,    0,   43,   46,   48,    0,   83,    0,   81,
    4,    1,  182,    0,    0,    0,    0,    0,    0,    0,
    0,  112,    0,    0,  162,  163,  164,  165,    0,    0,
  166,  167,    0,    0,    0,    0,    0,    0,  179,    0,
  174,    0,  176,    0,    0,    0,    0,    0,    0,    0,
    0,   19,    0,    0,   15,   14,    0,    0,    0,   33,
    0,    6,    0,   55,   53,    0,    0,    0,   41,   45,
   84,    0,   82,  181,    0,    0,    0,   99,  100,    0,
    0,    0,  144,    0,    0,    0,  111,    0,    0,    0,
    0,    0,    0,    0,    0,   98,   96,  101,   97,    0,
  159,  172,  173,    0,  170,  171,  169,    0,    0,    0,
    0,    0,   39,    0,    0,    0,    0,   30,    0,   51,
   56,   50,    0,    0,   36,   85,   86,    0,  136,    0,
    0,    0,  145,    0,    0,  128,  129,    0,  130,  131,
    0,  126,    0,  110,  109,    0,  157,  158,    0,    0,
    0,    0,  151,   37,   38,    6,    6,    6,    6,    6,
    0,   54,    0,    0,    0,  142,    0,  135,    0,  143,
  133,    0,    0,    0,    0,    0,  106,    0,    0,    0,
    0,    0,    0,  160,    0,  156,  153,  149,  154,  148,
    0,    0,    0,    0,    0,    0,    0,    0,  139,    0,
  138,  146,    0,    0,    0,  123,  124,    0,    0,    0,
    0,    0,  122,  120,  125,  121,  137,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  141,
    0,  134,  155,    0,    0,    0,    0,    0,    0,   22,
    0,    0,    0,  132,    0,    0,    0,    0,    0,    0,
   28,    0,    0,    0,    0,   25,   23,   29,   24,   26,
   27,   59,   58,   60,   57,
};
final static short yydgoto[] = {                          3,
    4,  145,   12,   13,   14,   15,   16,   17,   18,   37,
   19,   96,   97,   20,  102,  103,  104,  105,  106,  166,
   28,   29,   30,   31,   32,   33,   34,   59,   35,   50,
   61,   62,  238,   63,   64,  241,  242,  182,  229,  267,
  183,   72,   65,  133,
};
final static short yysindex[] = {                       -86,
    0,    0,    0,  747,  757, -228,  687,    0,    0, -219,
  -77,    0,    0,    0,    0,    0,    0, -138,   50, -180,
  -85,  687,  -20,  -33,  -39,  902,  968,    0,   44,   61,
   88,   96,  123,   24,    0,    0,   63,  -79,  975, -157,
 -228,   29,  -41,   32,  369,  216,    0,    0,  -60,   98,
  909,  136, -116,  758,  -12,    0,  -16, -116,  421,    0,
  -31,    0,    0,    0,  327,  158,   22,  439,  956,  687,
    0, -208,    0,    0,    0,    0,    0,    0,    0,  -91,
 -116,    0,    0,  -26,    0,    0,  216,  216,  -14,    0,
  166,   -1,    0,    0,   43,  102,    0,  -54,    0,    0,
   68,   71,  216,    0,    0,    0,  258,    0,  -80,    0,
    0,    0,    0,  633,  305,  305, -152, -152,   82,  315,
  113,    0,  180,  516,    0,    0,    0,    0,  380,  462,
    0,    0, -116,  594,  598,  687,  921,  112,    0,  347,
    0,  690,    0,  362,  986,  928,  363,  -30,  758,  405,
  405,    0,  161, -111,    0,    0,  369,  369,   80,    0,
  369,    0,  374,    0,    0,   56,  411,  395,    0,    0,
    0,  -51,    0,    0,  165,  -31,  -31,    0,    0,  368,
  405,  -19,    0,  609,  418,  775,    0,  715,  921,  196,
  165,  -31,  165,  -31,  405,    0,    0,    0,    0,  199,
    0,    0,    0, -116,    0,    0,    0, -116,  614,  436,
  431,  434,    0,  300,  367,  453,   17,    0,  774,    0,
    0,    0,  240,  178,    0,    0,    0,  269,    0,  368,
  650,  368,    0,  896,    1,    0,    0,  794,    0,    0,
  202,    0,  205,    0,    0,  -55,    0,    0,  468,  485,
 1051,  489,    0,    0,    0,    0,    0,    0,    0,    0,
  687,    0,  491,  398,  443,    0,  -18,    0,  187,    0,
    0,  654,  654,  120,  120,  512,    0,  668,  671,  675,
  679,  368,   26,    0,  921,    0,    0,    0,    0,    0,
  781,  791,  798,  805,  740,  993,  295, -221,    0,  301,
    0,    0,  267,  202,  202,    0,    0, -116,  267,  202,
  267,  202,    0,    0,    0,    0,    0,  368,  289,  687,
  687,  687,  687,  687,  687,  501,  687,  687,  687,    0,
  852,    0,    0, 1004, 1011, 1022, 1029, 1040,  940,    0,
 1047, 1058,  947,    0,  505,  507,  508,  510,  513,  517,
    0,  518,  521,  522,  529,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   28,    0,    0,    0,    0,    0,    0,    0,  817,  834,
  851,  868,  885,    0,    0,    0,  700,   28,    0,  329,
    0,    0,    0,  720,    0,  332,  308,    0,    0,    0,
    0,    0,    0,    0,   89,    0,    0,    0,    0,    0,
  149,    0,    0,    0,    0,  334,    0,  523,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  332,   92,    0,    0,
    0,  556,    0,    0,   40,    0,    0,    0,    0,    0,
    0,    0,  118,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  541,
    0,  558,    0,    0,    0,    0,    0,    0,  575,  592,
  612,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  143,   42,    0,    0,    0,
    0,    0,    0,    0,    0,  231,  353,    0,    0,  115,
   12,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  387,  412,  469,  494,  445,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  631,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  115,
    0,  115,    0,    0, 1068,    0,    0,    0,    0,    0,
  932,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  648,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  115,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, 1034, 1085,    0,    0,    0, 1102, 1179,
 1279, 1289,    0,    0,    0,    0,    0,  115,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  732,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    7,   -5,    0,  -10,    0,    0,    0,   65,  788,    3,
    0, -107,  446,    0,  220,    0,  509,  532,    0,    0,
   -8,    0,    0,    0,    0,    0,    0,    2,  -21,  559,
  404,  177,    0,  243,  268,  197,  270,  372, -164,    0,
 -159,  -50,   -4,    0,
};
final static int YYTABLESIZE=1334;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
   67,   39,   60,   60,  165,   49,   58,  226,    5,  209,
  134,   49,  173,   42,  138,  135,   51,   71,   74,   53,
   44,  232,  233,  123,  231,  300,   68,  119,   36,   84,
   74,   60,  108,  121,  328,   99,   60,   40,  329,  230,
  119,  227,   74,   89,  156,   60,  121,  147,  120,  214,
  215,  217,  147,  124,  114,  147,   71,  260,   60,   60,
  161,  276,  141,  148,  146,  268,  318,  271,  142,  231,
  147,  270,   84,  190,  301,   84,   99,   99,   49,   46,
   35,  150,  151,   35,   81,   20,  200,   90,   79,   45,
   93,   49,   99,   60,   60,   60,   60,   60,   87,  223,
   20,   60,   75,   44,   55,   56,   84,   60,   60,  302,
  100,   60,   60,   60,  222,   71,  264,  317,   36,   76,
  181,   83,  180,  233,  188,   43,   60,   57,   71,  103,
  103,  103,  103,  103,  195,  103,   74,   74,  246,   54,
   55,   56,  162,  210,  212,  161,   77,  103,  103,  213,
  103,  100,  100,  332,   78,  140,  140,  140,  140,  140,
   60,  140,  237,   57,  149,   55,   56,  100,  219,    1,
    2,   47,   48,  140,  140,  172,  140,   85,   48,   36,
   71,   79,   60,  181,    8,    9,   60,   60,   57,   95,
  110,   95,   95,   95,  113,  107,   85,   48,  139,  249,
  284,  163,  164,  250,  252,  157,  117,   95,   95,   60,
   95,  118,  285,  286,   91,   92,   54,   55,   56,   66,
  187,   60,   54,   55,   56,   54,   55,   56,  117,  115,
  152,  116,  181,  118,  319,   52,   54,   55,   56,  122,
   57,  155,   82,  280,  181,  282,   57,   60,  281,   57,
  237,  237,  237,  237,  158,  296,  237,  237,  237,  237,
   57,   60,  291,  292,  293,  294,  295,  147,  147,  147,
  181,   92,  259,   92,   92,   92,   71,   54,   55,   56,
  140,   54,   55,   56,  181,   82,   60,   74,   82,   92,
   92,  147,   92,  178,  179,   35,   80,   20,   20,  160,
   79,   57,   20,   20,   20,   57,  153,  154,  274,  331,
  197,  199,  221,  275,  334,  335,  336,  337,  338,  339,
   20,  341,  342,  343,  167,   74,   74,   74,   74,   74,
   74,  168,   74,   74,   74,  216,   94,   54,   55,   56,
  256,    8,    9,  161,  103,  103,  103,   44,  103,  103,
  171,   20,   44,  103,  184,  103,  103,  103,  103,  103,
  103,   57,  103,  103,  103,  103,   20,  137,  103,  185,
  140,  140,  140,   42,  140,  140,  235,  236,   42,  140,
  201,  140,  140,  140,  140,  140,  140,  202,  140,  140,
  140,  140,  180,   94,  140,   94,   94,   94,   52,   57,
   87,  204,  208,   52,   95,   95,   95,  257,   95,   95,
  161,   94,   94,   95,   94,   95,   95,   95,   95,   95,
   95,  211,   95,   95,   95,   95,  239,   91,   95,   91,
   91,   91,  220,  263,   94,  186,   55,   56,  298,    8,
    9,  161,   54,   55,   56,   91,   91,  129,   91,  130,
  224,  240,   89,  225,   89,   89,   89,  243,  228,   57,
   54,   55,   56,  129,  247,  130,   57,  248,  304,  305,
   89,   89,    6,   89,  310,  312,  253,    8,    9,  143,
  132,  129,  131,  130,   57,  161,   92,   92,   92,  254,
   92,   92,  255,  258,   98,   92,  262,   92,   92,   92,
   92,   92,   92,  161,   92,   92,   92,   92,  287,   93,
   92,   93,   93,   93,  239,  239,  239,  239,  176,  177,
  239,  239,  239,  239,  265,  288,  266,   93,   93,  290,
   93,  297,  192,  194,   90,  299,   90,   90,   90,  240,
  240,  240,  240,  306,  307,  240,  240,  240,  240,  314,
  316,  308,   90,   90,  327,   90,  189,  333,  330,  340,
  175,   55,   56,  356,   20,  357,  358,   20,  359,   20,
   20,  360,   20,   20,   20,  361,  362,   20,   20,  363,
  364,  177,  136,   38,   57,   20,   70,  365,   40,  180,
  180,   23,   44,   24,  180,   32,   25,   26,  180,  178,
  180,  180,  180,  180,  180,  180,  218,  109,   94,   94,
   94,  169,   94,   94,  283,    0,  175,   94,    0,   94,
   94,   94,   94,   94,   94,   94,   94,   94,   94,   94,
    8,    9,   94,   78,  170,  191,   55,   56,    0,    0,
    0,    0,   91,   91,   91,    0,   91,   91,    0,    0,
   76,   91,    0,   91,   91,   91,   91,   91,   91,   57,
   91,   91,   91,   91,    0,    0,   91,   89,   89,   89,
   77,   89,   89,  174,    0,  129,   89,  130,   89,   89,
   89,   89,   89,   89,    0,   89,   89,   89,   89,  152,
    0,   89,    0,    0,  125,  126,  127,  128,    0,    0,
  161,  161,    0,    0,  161,  161,  150,    0,    0,  161,
    0,  161,  161,  161,  161,  161,  161,  193,   55,   56,
    0,    0,    0,    0,   93,   93,   93,    0,   93,   93,
  203,    0,  129,   93,  130,   93,   93,   93,   93,   93,
   93,   57,   93,   93,   93,   93,    0,    0,   93,   90,
   90,   90,    0,   90,   90,  245,    0,  129,   90,  130,
   90,   90,   90,   90,   90,   90,    0,   90,   90,   90,
   90,  136,   38,   90,    0,   70,    0,    0,  177,  177,
   23,    0,   24,  177,    0,   25,   26,  177,    0,  177,
  177,  177,  177,  177,  177,    0,  178,  178,   41,  117,
  115,  178,  116,    0,  118,  178,    0,  178,  178,  178,
  178,  178,  178,  175,  175,  244,  117,  115,  175,  116,
    0,  118,  175,    0,  175,  175,  175,  175,  175,  175,
   78,   78,   95,  101,  277,   78,  278,    0,  279,   78,
    0,   78,   78,   78,   78,   78,   78,   76,   76,  196,
   55,   56,   76,  198,   55,   56,   76,    0,   76,   76,
   76,   76,   76,   76,  234,  235,  236,   77,   77,  251,
   55,   56,   77,   57,  101,  101,   77,   57,   77,   77,
   77,   77,   77,   77,    0,    0,  152,  152,   57,    0,
  101,  152,  344,   57,  129,  152,  130,  152,  152,  152,
  152,  152,  152,  150,  150,  269,   55,   56,  150,  303,
  235,  236,  150,    0,  150,  150,  150,  150,  150,  150,
    0,    0,    0,  309,  235,  236,  311,  235,  236,   57,
  313,  235,  236,   57,  315,  235,  236,  274,  272,    0,
  273,    0,  275,   38,   95,   95,   95,   57,   95,    0,
   57,   23,    0,   24,   57,   63,   25,   26,   57,   63,
   63,   63,   63,    0,   63,   63,   63,    0,    0,   63,
   63,    0,  119,    0,  119,   13,  119,   63,   63,   13,
   13,   13,   13,    0,   13,   13,   13,   61,   61,   13,
   13,    0,   61,   61,   61,  324,    6,   13,   13,  325,
    0,    8,    9,    6,    0,   10,    7,    0,    8,    9,
   61,   95,   10,   21,    0,    0,   22,   11,    8,    9,
    0,   23,   10,   24,   11,    0,   25,   26,    0,    0,
    6,    0,    0,  261,   11,    8,    9,    6,    0,   10,
  320,    0,    8,    9,    0,    0,   10,    6,    0,    0,
  321,   11,    8,    9,    6,    0,   10,  322,   11,    8,
    9,    6,    0,   10,  323,    0,    8,    9,   11,    0,
   10,    0,   71,   71,  116,   11,  116,   71,  116,    0,
    0,   71,   11,   71,   71,   71,   71,   71,   71,   72,
   72,  289,  117,  115,   72,  116,    0,  118,   72,    0,
   72,   72,   72,   72,   72,   72,   73,   73,  127,  127,
  127,   73,  127,    0,  127,   73,    0,   73,   73,   73,
   73,   73,   73,   74,   74,  118,    0,  118,   74,  118,
    0,    0,   74,    0,   74,   74,   74,   74,   74,   74,
   75,   75,  115,    0,  115,   75,  115,    0,    0,   75,
    0,   75,   75,   75,   75,   75,   75,   69,   38,    0,
    0,   70,    0,    0,  111,   38,   23,    0,   24,  112,
    0,   25,   26,   23,    0,   24,  136,   38,   25,   26,
   70,    0,    0,  206,   38,   23,    0,   24,  207,    0,
   25,   26,   23,    0,   24,  350,   38,   25,   26,    0,
  351,    0,  354,   38,   23,    0,   24,  355,    0,   25,
   26,   23,   38,   24,    0,    0,   25,   26,    0,  113,
   23,  113,   24,  113,   38,   25,   26,  144,   73,    0,
    0,   38,   23,    0,   24,   86,    0,   25,   26,   23,
    0,   24,   38,    0,   25,   26,  205,    0,    0,   38,
   23,    0,   24,  326,    0,   25,   26,   23,    0,   24,
   38,    0,   25,   26,  345,    0,    0,   38,   23,    0,
   24,  346,    0,   25,   26,   23,    0,   24,   38,    0,
   25,   26,  347,    0,    0,   38,   23,    0,   24,  348,
    0,   25,   26,   23,    0,   24,   38,    0,   25,   26,
  349,    0,    0,   38,   23,    0,   24,  352,    0,   25,
   26,   23,    0,   24,   38,    0,   25,   26,  353,  117,
    0,  117,   23,  117,   24,    0,    0,   25,   26,  114,
    0,  114,    0,  114,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
   40,    7,   24,   25,   59,   91,   40,   59,    2,   40,
   42,   91,   93,   11,   65,   47,   22,   26,   27,   40,
   18,   41,  182,   40,   44,   44,   25,   40,  257,   44,
   39,   53,   93,   46,  256,   46,   58,  257,  260,   59,
   40,   93,   51,   41,   59,   67,   46,  256,   61,  157,
  158,  159,   41,   58,   53,   44,   65,   41,   80,   81,
   44,   61,   41,  272,   70,  230,   41,  232,   67,   44,
   59,  231,   44,  124,   93,   44,   87,   88,   91,  260,
   41,   80,   81,   44,   61,   44,  137,   59,   61,   40,
   59,   91,  103,  115,  116,  117,  118,  119,  256,   44,
   59,  123,   59,  101,  257,  258,   44,  129,  130,  269,
   46,  133,  134,  135,   59,  124,  224,  282,  257,   59,
  119,   59,   41,  283,  123,  264,  148,  280,  137,   41,
   42,   43,   44,   45,  133,   47,  145,  146,  189,  256,
  257,  258,   41,  148,  256,   44,   59,   59,   60,  261,
   62,   87,   88,  318,   59,   41,   42,   43,   44,   45,
  182,   47,  184,  280,  256,  257,  258,  103,  162,  256,
  257,  257,  258,   59,   60,  256,   62,  257,  258,  257,
  189,   59,  204,  182,  262,  263,  208,  209,  280,   41,
   93,   43,   44,   45,   59,  256,  257,  258,   41,  204,
  256,  256,  257,  208,  209,   40,   42,   59,   60,  231,
   62,   47,  268,  269,  256,  257,  256,  257,  258,  259,
   41,  243,  256,  257,  258,  256,  257,  258,   42,   43,
  257,   45,  231,   47,  285,  256,  256,  257,  258,  256,
  280,  256,  257,   42,  243,   41,  280,  269,   47,  280,
  272,  273,  274,  275,  256,  261,  278,  279,  280,  281,
  280,  283,  256,  257,  258,  259,  260,  256,  257,  258,
  269,   41,  256,   43,   44,   45,  285,  256,  257,  258,
  259,  256,  257,  258,  283,  257,  308,  296,  257,   59,
   60,  280,   62,  117,  118,  256,  273,  256,  257,  257,
  273,  280,  261,  262,  263,  280,   87,   88,   42,  308,
  134,  135,  257,   47,  320,  321,  322,  323,  324,  325,
  279,  327,  328,  329,  257,  334,  335,  336,  337,  338,
  339,  261,  341,  342,  343,  256,  257,  256,  257,  258,
   41,  262,  263,   44,  256,  257,  258,  256,  260,  261,
   93,   44,  261,  265,   40,  267,  268,  269,  270,  271,
  272,  280,  274,  275,  276,  277,   59,   41,  280,  257,
  256,  257,  258,  256,  260,  261,  257,  258,  261,  265,
  269,  267,  268,  269,  270,  271,  272,   41,  274,  275,
  276,  277,   59,   41,  280,   43,   44,   45,  256,  280,
   93,   40,   40,  261,  256,  257,  258,   41,  260,  261,
   44,   59,   60,  265,   62,  267,  268,  269,  270,  271,
  272,  261,  274,  275,  276,  277,  184,   41,  280,   43,
   44,   45,   59,  256,  257,  256,  257,  258,   41,  262,
  263,   44,  256,  257,  258,   59,   60,   43,   62,   45,
   40,  184,   41,   59,   43,   44,   45,   40,   91,  280,
  256,  257,  258,   43,  269,   45,  280,  269,  272,  273,
   59,   60,  257,   62,  278,  279,   41,  262,  263,   41,
   60,   43,   62,   45,  280,   41,  256,  257,  258,   59,
  260,  261,   59,   41,  279,  265,  257,  267,  268,  269,
  270,  271,  272,   59,  274,  275,  276,  277,   41,   41,
  280,   43,   44,   45,  272,  273,  274,  275,  115,  116,
  278,  279,  280,  281,  256,   41,  258,   59,   60,   41,
   62,   41,  129,  130,   41,   93,   43,   44,   45,  272,
  273,  274,  275,  274,  275,  278,  279,  280,  281,  280,
  281,   40,   59,   60,  260,   62,   41,  269,  258,   59,
  256,  257,  258,   59,  257,   59,   59,  260,   59,  262,
  263,   59,  265,  266,  267,   59,   59,  270,  271,   59,
   59,   59,  256,  257,  280,  278,  260,   59,  260,  256,
  257,  265,  261,  267,  261,   40,  270,  271,  265,   59,
  267,  268,  269,  270,  271,  272,  161,   49,  256,  257,
  258,  103,  260,  261,  243,   -1,   59,  265,   -1,  267,
  268,  269,  270,  271,  272,  257,  274,  275,  276,  277,
  262,  263,  280,   59,  103,  256,  257,  258,   -1,   -1,
   -1,   -1,  256,  257,  258,   -1,  260,  261,   -1,   -1,
   59,  265,   -1,  267,  268,  269,  270,  271,  272,  280,
  274,  275,  276,  277,   -1,   -1,  280,  256,  257,  258,
   59,  260,  261,   41,   -1,   43,  265,   45,  267,  268,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,   59,
   -1,  280,   -1,   -1,  274,  275,  276,  277,   -1,   -1,
  256,  257,   -1,   -1,  260,  261,   59,   -1,   -1,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,  258,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
   41,   -1,   43,  265,   45,  267,  268,  269,  270,  271,
  272,  280,  274,  275,  276,  277,   -1,   -1,  280,  256,
  257,  258,   -1,  260,  261,   41,   -1,   43,  265,   45,
  267,  268,  269,  270,  271,  272,   -1,  274,  275,  276,
  277,  256,  257,  280,   -1,  260,   -1,   -1,  256,  257,
  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,
  268,  269,  270,  271,  272,   -1,  256,  257,   11,   42,
   43,  261,   45,   -1,   47,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,   41,   42,   43,  261,   45,
   -1,   47,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   45,   46,   41,  261,   43,   -1,   45,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,  256,
  257,  258,  261,  256,  257,  258,  265,   -1,  267,  268,
  269,  270,  271,  272,  256,  257,  258,  256,  257,  256,
  257,  258,  261,  280,   87,   88,  265,  280,  267,  268,
  269,  270,  271,  272,   -1,   -1,  256,  257,  280,   -1,
  103,  261,   41,  280,   43,  265,   45,  267,  268,  269,
  270,  271,  272,  256,  257,  256,  257,  258,  261,  256,
  257,  258,  265,   -1,  267,  268,  269,  270,  271,  272,
   -1,   -1,   -1,  256,  257,  258,  256,  257,  258,  280,
  256,  257,  258,  280,  256,  257,  258,   42,   43,   -1,
   45,   -1,   47,  257,  157,  158,  159,  280,  161,   -1,
  280,  265,   -1,  267,  280,  256,  270,  271,  280,  260,
  261,  262,  263,   -1,  265,  266,  267,   -1,   -1,  270,
  271,   -1,   41,   -1,   43,  256,   45,  278,  279,  260,
  261,  262,  263,   -1,  265,  266,  267,  256,  257,  270,
  271,   -1,  261,  262,  263,  256,  257,  278,  279,  260,
   -1,  262,  263,  257,   -1,  266,  260,   -1,  262,  263,
  279,  224,  266,  257,   -1,   -1,  260,  278,  262,  263,
   -1,  265,  266,  267,  278,   -1,  270,  271,   -1,   -1,
  257,   -1,   -1,  260,  278,  262,  263,  257,   -1,  266,
  260,   -1,  262,  263,   -1,   -1,  266,  257,   -1,   -1,
  260,  278,  262,  263,  257,   -1,  266,  260,  278,  262,
  263,  257,   -1,  266,  260,   -1,  262,  263,  278,   -1,
  266,   -1,  256,  257,   41,  278,   43,  261,   45,   -1,
   -1,  265,  278,  267,  268,  269,  270,  271,  272,  256,
  257,   41,   42,   43,  261,   45,   -1,   47,  265,   -1,
  267,  268,  269,  270,  271,  272,  256,  257,   41,   42,
   43,  261,   45,   -1,   47,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,   41,   -1,   43,  261,   45,
   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   41,   -1,   43,  261,   45,   -1,   -1,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,   -1,
   -1,  260,   -1,   -1,  256,  257,  265,   -1,  267,  261,
   -1,  270,  271,  265,   -1,  267,  256,  257,  270,  271,
  260,   -1,   -1,  256,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,   -1,  267,  256,  257,  270,  271,   -1,
  261,   -1,  256,  257,  265,   -1,  267,  261,   -1,  270,
  271,  265,  257,  267,   -1,   -1,  270,  271,   -1,   41,
  265,   43,  267,   45,  257,  270,  271,  272,  261,   -1,
   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,  265,
   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,
  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,
  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,   -1,  267,  257,   -1,
  270,  271,  261,   -1,   -1,  257,  265,   -1,  267,  261,
   -1,  270,  271,  265,   -1,  267,  257,   -1,  270,  271,
  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,
  271,  265,   -1,  267,  257,   -1,  270,  271,  261,   41,
   -1,   43,  265,   45,  267,   -1,   -1,  270,  271,   41,
   -1,   43,   -1,   45,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","BEGIN","END",
"USHORTINT","DOUBLEF","FUNCTION","RET","CLASS","IF","ELSE","END_IF","POUT",
"REPEAT","WHILE","ASIGNACION","IGUAL","DISTINTO","MAYORIGUAL","MENORIGUAL",
"COMPTIME","EXTENDS","TODF","SIN_PUNTO_Y_COMA",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID sentencias_declarativas BEGIN sentencias_ejecutables END",
"programa : error sentencias_declarativas BEGIN sentencias_ejecutables END",
"programa : ID sentencias_declarativas sentencias_ejecutables END",
"programa : ID sentencias_declarativas BEGIN sentencias_ejecutables error",
"sentencias_declarativas : sentencias_declarativas sentencia_declarativa",
"sentencias_declarativas :",
"sentencia_declarativa : declaracion_variables",
"sentencia_declarativa : declaracion_variables_comptime",
"sentencia_declarativa : declaracion_funcion",
"sentencia_declarativa : declaracion_clase",
"sentencia_declarativa : declaracion_objeto",
"declaracion_variables : tipo lista_variables ';'",
"declaracion_variables : tipo lista_variables",
"declaracion_variables_comptime : COMPTIME tipo lista_variables ';'",
"declaracion_variables_comptime : COMPTIME tipo lista_variables error",
"declaracion_variables_comptime : COMPTIME lista_variables ';'",
"tipo : USHORTINT",
"tipo : DOUBLEF",
"lista_variables : lista_variables ',' ID",
"lista_variables : ID",
"lista_variables : lista_variables ID",
"declaracion_funcion : encabezado_funcion '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID error parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales error sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas error sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables error ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END",
"declaracion_funcion : tipo FUNCTION ID '(' error ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametros_formales : parametro_formal",
"encabezado_funcion : tipo FUNCTION ID",
"parametro_formal : tipo ID",
"parametro_formal : ID",
"parametro_formal : tipo",
"declaracion_clase : encabezado_clase BEGIN cuerpo_clase END ';'",
"declaracion_clase : CLASS ID error cuerpo_clase END ';'",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase error ';'",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase END",
"encabezado_clase : CLASS ID",
"cuerpo_clase : elementos_clase sentencia_extends",
"cuerpo_clase : elementos_clase",
"cuerpo_clase : sentencia_extends",
"cuerpo_clase :",
"elementos_clase : elementos_clase elemento_clase",
"elementos_clase : elemento_clase",
"elemento_clase : declaracion_variables",
"elemento_clase : declaracion_metodo",
"elemento_clase : declaracion_objeto",
"sentencia_extends : EXTENDS lista_ids ';'",
"sentencia_extends : EXTENDS error ';'",
"sentencia_extends : EXTENDS lista_ids",
"sentencia_extends : EXTENDS ';'",
"lista_ids : lista_ids ',' ID",
"lista_ids : ID",
"lista_ids : lista_ids ID",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';'",
"declaracion_metodo : tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';'",
"declaracion_metodo : tipo ID '(' error ')' BEGIN sentencias_ejecutables END ';'",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';'",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END",
"declaracion_objeto : ID lista_variables ';'",
"declaracion_objeto : ID lista_variables",
"sentencias_ejecutables : sentencias_ejecutables sentencia_ejecutable",
"sentencias_ejecutables : sentencia_ejecutable",
"sentencia_ejecutable : asignacion ';'",
"sentencia_ejecutable : seleccion ';'",
"sentencia_ejecutable : salida ';'",
"sentencia_ejecutable : retorno ';'",
"sentencia_ejecutable : iterativa ';'",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : seleccion",
"sentencia_ejecutable : salida",
"sentencia_ejecutable : retorno",
"sentencia_ejecutable : iterativa",
"asignacion : lado_izquierdo ASIGNACION expresion",
"asignacion : lado_izquierdo '=' expresion",
"asignacion : lado_izquierdo ASIGNACION error",
"lado_izquierdo : ID",
"lado_izquierdo : acceso_posicional",
"lado_izquierdo : ID indice ']'",
"acceso_posicional : ID '[' indice ']'",
"acceso_posicional : ID '[' ']'",
"acceso_posicional : ID '[' error ']'",
"acceso_posicional : ID '[' indice error ';'",
"acceso_posicional : ID '[' indice error ']'",
"indice : ID",
"indice : CTE",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : expresion '+' error",
"expresion : error '+' termino",
"expresion : expresion '-' error",
"expresion : error '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : termino '*' error",
"termino : error '*' factor",
"termino : error '/' factor",
"termino : termino '/' error",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : acceso_posicional",
"factor : ID '=' '(' expresion_simple ')'",
"factor : to_double",
"factor : invocacion",
"to_double : TODF '(' expresion ')'",
"to_double : TODF '(' error ')'",
"to_double : TODF '(' ')'",
"to_double : TODF error",
"expresion_simple : expresion_simple '+' termino_simple",
"expresion_simple : expresion_simple '-' termino_simple",
"expresion_simple : expresion_simple '+' error",
"expresion_simple : error '+' termino_simple",
"expresion_simple : expresion_simple '-' error",
"expresion_simple : error '-' termino_simple",
"expresion_simple : termino_simple",
"termino_simple : termino_simple '*' factor_simple",
"termino_simple : termino_simple '/' factor_simple",
"termino_simple : termino_simple '*' error",
"termino_simple : error '*' factor_simple",
"termino_simple : error '/' factor_simple",
"termino_simple : termino_simple '/' error",
"termino_simple : factor_simple",
"factor_simple : ID",
"factor_simple : CTE",
"factor_simple : acceso_posicional",
"factor_simple : to_double",
"factor_simple : invocacion",
"factor_simple : ID '=' '(' expresion ')'",
"invocacion : ID '(' parametros_reales ')' lista_constantes_opcional",
"invocacion : ID '.' ID '(' parametros_reales ')' lista_constantes_opcional",
"invocacion : ID '(' parametros_reales ';' lista_constantes_opcional",
"invocacion : ID '(' ')' lista_constantes_opcional",
"invocacion : ID '.' ID '(' ')' lista_constantes_opcional",
"lista_constantes_opcional : '[' lista_constantes ']'",
"lista_constantes_opcional : '[' error ']'",
"lista_constantes_opcional :",
"lista_constantes : lista_constantes ',' CTE",
"lista_constantes : CTE",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : parametro_real",
"parametros_reales : parametros_reales parametro_real",
"parametros_reales : parametros_reales ',' error parametro_real",
"parametro_real : expresion",
"iterativa : REPEAT bloque_ejecutable WHILE '(' condicion ')'",
"iterativa : REPEAT bloque_ejecutable error '(' condicion ')'",
"iterativa : REPEAT bloque_ejecutable WHILE '(' condicion",
"iterativa : REPEAT bloque_ejecutable WHILE condicion ')'",
"iterativa : REPEAT bloque_ejecutable WHILE condicion",
"iterativa : REPEAT error WHILE '(' condicion ')'",
"iterativa : REPEAT bloque_ejecutable WHILE '(' error ')'",
"seleccion : IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable END_IF",
"seleccion : IF '(' condicion ')' bloque_ejecutable END_IF",
"seleccion : IF '(' condicion bloque_ejecutable END_IF",
"seleccion : IF condicion ')' bloque_ejecutable END_IF",
"seleccion : IF condicion bloque_ejecutable END_IF",
"seleccion : IF '(' condicion ')' bloque_ejecutable error",
"condicion : expresion comparador expresion",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : '>'",
"comparador : '<'",
"bloque_ejecutable : sentencia_ejecutable",
"bloque_ejecutable : BEGIN sentencias_ejecutables END",
"bloque_ejecutable : error sentencias_ejecutables END",
"bloque_ejecutable : BEGIN sentencias_ejecutables error",
"salida : POUT '(' CADENA ')'",
"salida : POUT '(' expresion ')'",
"salida : POUT '(' ')'",
"salida : POUT '(' expresion",
"salida : POUT expresion ')'",
"salida : POUT expresion",
"salida : POUT '(' CADENA",
"salida : POUT CADENA ')'",
"salida : POUT CADENA",
"retorno : RET '(' expresion ')'",
"retorno : RET error ';'",
};

//#line 528 "gramatica.y"
private AnalizadorLexicoTp3 lexer;
private TablaDeSimbolosTp3 tablaSimbolos;
private GestionAmbitoTp3 gestorAmbitos;
private boolean expresionInvalida = false;
private NodoAST raizAST;
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
//#line 851 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 26 "gramatica.y"
{
        NodoPrograma raiz = new NodoPrograma(val_peek(4).sval,(NodoAST) val_peek(3).obj,(NodoAST) val_peek(1).obj);
        raizAST = raiz;
        yyval = new ParserVal(raiz);
        System.out.println("Declaración de programa y AST generado");
    }
break;
case 2:
//#line 32 "gramatica.y"
{ yyerror("Falta de nombre de programa");  yyval = new ParserVal(null);}
break;
case 3:
//#line 33 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN"); yyval = new ParserVal(null);}
break;
case 4:
//#line 34 "gramatica.y"
{ yyerror("Falta la palabra reservada END"); yyval = new ParserVal(null);}
break;
case 5:
//#line 39 "gramatica.y"
{
        NodoBloque bloque = (NodoBloque) val_peek(1).obj;

        if (val_peek(0).obj != null) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);}
break;
case 6:
//#line 47 "gramatica.y"
{yyval = new ParserVal(new NodoBloque());}
break;
case 12:
//#line 59 "gramatica.y"
{
        System.out.println("Declaracion de VAR");
        declararListaVariables((List<String>) val_peek(1).obj, val_peek(2).sval, false);
    }
break;
case 13:
//#line 63 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 14:
//#line 67 "gramatica.y"
{
        System.out.println("Declaracion de comptime");
        declararListaVariables((List<String>) val_peek(1).obj, val_peek(2).sval, true);
    }
break;
case 15:
//#line 71 "gramatica.y"
{ yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
break;
case 16:
//#line 72 "gramatica.y"
{ yyerror("Falta tipo en las variables en COMPTIME");}
break;
case 17:
//#line 76 "gramatica.y"
{ System.out.println("Declaracion de tipo");
                yyval = new ParserVal("USHORTINT");
    }
break;
case 18:
//#line 79 "gramatica.y"
{ System.out.println("Declaracion de tipo");
                yyval = new ParserVal("DOUBLEF");
    }
break;
case 19:
//#line 85 "gramatica.y"
{
        List<String> lista = (List<String>) val_peek(2).obj;
        lista.add(val_peek(0).sval);
        yyval = val_peek(2);
    }
break;
case 20:
//#line 90 "gramatica.y"
{
        List<String> lista = new ArrayList<>();
        lista.add(val_peek(0).sval);
        yyval = new ParserVal(lista);
    }
break;
case 21:
//#line 95 "gramatica.y"
{ 
        yyerror("Lista de variables: Falta la ','");}
break;
case 22:
//#line 100 "gramatica.y"
{
        String nombreFuncion = val_peek(8).sval;

        /* Guardamos el ámbito antes de salir.*/
        String ambitoFuncion = getAmbitoActual();

        NodoFuncion nodoFn = new NodoFuncion(
            nombreFuncion,
            ambitoFuncion,
            (NodoAST) val_peek(6).obj,
            (NodoAST) val_peek(4).obj,
            (NodoAST) val_peek(2).obj
        );
        
        gestorAmbitos.salirAmbito();

        yyval = new ParserVal(nodoFn);

        System.out.println("Declaración de función");
    }
break;
case 23:
//#line 120 "gramatica.y"
{ yyerror("Falta parentesis de apertura en el encabezado de la función"); }
break;
case 24:
//#line 121 "gramatica.y"
{ yyerror("Falta parentesis de cierre en el encabezado de la función"); }
break;
case 25:
//#line 122 "gramatica.y"
{ yyerror("Falta de nombre de funcion");}
break;
case 26:
//#line 123 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN de la funcion");}
break;
case 27:
//#line 124 "gramatica.y"
{ yyerror("Falta la palabra reservada END de la funcion");}
break;
case 28:
//#line 125 "gramatica.y"
{ yyerror("Falta el ';'");}
break;
case 29:
//#line 126 "gramatica.y"
{ yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
break;
case 30:
//#line 130 "gramatica.y"
{System.out.println("Declaracion de parametro_formales");}
break;
case 31:
//#line 131 "gramatica.y"
{System.out.println("Declaracion de parametro_formales");}
break;
case 32:
//#line 135 "gramatica.y"
{
        RegistroSimboloTp3 funcion = gestorAmbitos.declararFuncion(val_peek(0).sval, val_peek(2).sval, lexer.getLineaActual() );
        if (funcion == null) {
            errorSemantico("Función ya declarada en el ámbito actual: " + val_peek(0).sval);
        }
        gestorAmbitos.entrarAmbitoFuncion(val_peek(0).sval);
        yyval = new ParserVal(val_peek(0).sval);
        }
break;
case 33:
//#line 146 "gramatica.y"
{ System.out.println("Declaracion de parametro_formal"); }
break;
case 34:
//#line 147 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'tipo'");}
break;
case 35:
//#line 148 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'ID''");}
break;
case 36:
//#line 152 "gramatica.y"
{
        System.out.println("Declaracion de clase");
        gestorAmbitos.salirAmbito();
        NodoClase nodoClase = new NodoClase(val_peek(3).sval, getAmbitoActual(), (NodoAST)val_peek(0).obj);
        yyval = new ParserVal(nodoClase);
    }
break;
case 37:
//#line 158 "gramatica.y"
{ yyerror ("Falta el BEGIN de clase");}
break;
case 38:
//#line 159 "gramatica.y"
{ yyerror("Falta el END de clase");}
break;
case 39:
//#line 160 "gramatica.y"
{ yyerror("Falta el ';' al final de la clase");}
break;
case 40:
//#line 164 "gramatica.y"
{
        RegistroSimboloTp3 clase = gestorAmbitos.declararClase(
            val_peek(0).sval, lexer.getLineaActual()
        );

        if (clase == null) {
            errorSemantico(
                "Clase ya declarada en el ámbito actual: " + val_peek(0).sval
            );
        }

        gestorAmbitos.entrarAmbitoClase(val_peek(0).sval);
    }
break;
case 50:
//#line 198 "gramatica.y"
{System.out.println("Sentencia EXTENDS");}
break;
case 51:
//#line 199 "gramatica.y"
{ yyerror("Sintaxis de lista de herencia invalida"); }
break;
case 52:
//#line 200 "gramatica.y"
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 53:
//#line 201 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 56:
//#line 207 "gramatica.y"
{ yyerror("Lista de IDs: Falta la ','");}
break;
case 57:
//#line 211 "gramatica.y"
{
        System.out.println("Declaracion de Metodo");
        RegistroSimboloTp3 metReg = gestorAmbitos.declararMetodo(val_peek(7).sval, val_peek(8).sval, lexer.getLineaActual());
    if (metReg == null) {
        errorSemantico("Método ya declarado en el ámbito actual: " + val_peek(7).sval);
        }
    NodoMetodo nodoMetodo = new NodoMetodo(val_peek(7).sval, getAmbitoActual(), (NodoAST)val_peek(5).obj, (NodoAST)val_peek(1).obj);
    yyval = new ParserVal(nodoMetodo);
    }
break;
case 58:
//#line 220 "gramatica.y"
{ yyerror("Falta el BEGIN en la declaracion del metodo");}
break;
case 59:
//#line 221 "gramatica.y"
{ yyerror("Falta una ',' o tipo invalido en los parametros del metodo"); }
break;
case 60:
//#line 222 "gramatica.y"
{ yyerror("Falta el END en la declaracion del metodo");}
break;
case 61:
//#line 223 "gramatica.y"
{ yyerror("Falta el ';' en la declaracion del metodo");}
break;
case 62:
//#line 227 "gramatica.y"
{
        System.out.println("Declaracion de objeto");
        List<String> nombres = (List<String>) val_peek(1).obj;
        String tipoClase = val_peek(2).sval;

        for (String nombre : nombres) {
            RegistroSimboloTp3 obj = gestorAmbitos.declararObjeto(nombre, tipoClase, lexer.getLineaActual());
            if (obj == null) {
                errorSemantico("No se pudo declarar el objeto '" + nombre + "': clase no visible o identificador duplicado");
            }
        }
    }
break;
case 63:
//#line 239 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'");}
break;
case 64:
//#line 244 "gramatica.y"
{
        NodoBloque bloque = null;
        if (val_peek(1).obj != null) {
            bloque = (NodoBloque) val_peek(1).obj;
        } else {
            bloque = new NodoBloque();
        }
        if (val_peek(0).obj != null) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);
    }
break;
case 65:
//#line 256 "gramatica.y"
{
        NodoBloque bloque = new NodoBloque();
        if (val_peek(0).obj != null) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);
    }
break;
case 66:
//#line 266 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 67:
//#line 267 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 68:
//#line 268 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 69:
//#line 269 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 70:
//#line 270 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 71:
//#line 271 "gramatica.y"
{ yyerror("Asignacion: Falta el ';', o un operador o expresion");}
break;
case 72:
//#line 272 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");}
break;
case 73:
//#line 273 "gramatica.y"
{ yyerror("Salida: Falta el ';'");}
break;
case 74:
//#line 274 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");}
break;
case 75:
//#line 275 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");}
break;
case 76:
//#line 279 "gramatica.y"
{
        if (!expresionInvalida){
            NodoAST izquierdo = (NodoAST) val_peek(2).obj;
            NodoAST derecho = (NodoAST) val_peek(0).obj;

            actualizarComptimeSiCorresponde(izquierdo, derecho);

            yyval = new ParserVal(new NodoAsignacion(izquierdo, derecho));
            System.out.println("Declaracion Asignacion");
        } else {
            expresionInvalida = false;
            yyval = new ParserVal(null);
        }
    }
break;
case 77:
//#line 293 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); yyval = new ParserVal(null); }
break;
case 78:
//#line 294 "gramatica.y"
{ yyerror("Falta expresion en la asignacion"); yyval = new ParserVal(null);}
break;
case 79:
//#line 298 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 80:
//#line 299 "gramatica.y"
{
        yyval = val_peek(0); 
        System.out.println("Declaracion de acceso_posicional");}
break;
case 81:
//#line 302 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de apertura "); yyval = new ParserVal(null);}
break;
case 82:
//#line 306 "gramatica.y"
{ yyval = new ParserVal(new NodoAccesoArreglo(new NodoHoja("ID", val_peek(3).sval, getLineaActual()), (NodoAST)val_peek(1).obj)); }
break;
case 83:
//#line 307 "gramatica.y"
{ yyerror("Acceso posicional: Índice vacío"); yyval = new ParserVal(null);}
break;
case 84:
//#line 308 "gramatica.y"
{ yyerror("Acceso posicional: Índice inválido"); yyval = new ParserVal(null);}
break;
case 85:
//#line 309 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de cierre"); yyval = new ParserVal(null);}
break;
case 86:
//#line 310 "gramatica.y"
{ yyerror("Acceso posicional: Argumento invalido"); yyval = new ParserVal(null);}
break;
case 87:
//#line 314 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 88:
//#line 315 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval, getLineaActual())); }
break;
case 89:
//#line 319 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("+", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 90:
//#line 320 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("-", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 91:
//#line 321 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 92:
//#line 322 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 93:
//#line 323 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 94:
//#line 324 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 95:
//#line 325 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 96:
//#line 329 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("*", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 97:
//#line 330 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("/", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 98:
//#line 331 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 99:
//#line 332 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 100:
//#line 333 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 101:
//#line 334 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 102:
//#line 335 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 103:
//#line 339 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 104:
//#line 340 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval, getLineaActual())); }
break;
case 105:
//#line 341 "gramatica.y"
{
        yyval = val_peek(0);
        System.out.println("Acceso posicional");}
break;
case 106:
//#line 344 "gramatica.y"
{
        yyval = new ParserVal(new NodoAsignacion(new NodoHoja("ID", val_peek(4).sval, getLineaActual()), (NodoAST)val_peek(1).obj));
        System.out.println("Asignacion EXP");}
break;
case 107:
//#line 347 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 108:
//#line 348 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 109:
//#line 352 "gramatica.y"
{
        yyval = new ParserVal(new NodoUnario("TODF", (NodoAST)val_peek(1).obj));
        System.out.println("Conversion DF"); 
    }
break;
case 110:
//#line 356 "gramatica.y"
{ yyerror("Solo se pueden expresiones"); yyval = new ParserVal(null);}
break;
case 111:
//#line 357 "gramatica.y"
{ yyerror("Falta expresion"); yyval = new ParserVal(null);}
break;
case 112:
//#line 358 "gramatica.y"
{ yyerror("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 113:
//#line 362 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("+", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 114:
//#line 363 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("-", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 115:
//#line 364 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 116:
//#line 365 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 117:
//#line 366 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 118:
//#line 367 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 119:
//#line 368 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 120:
//#line 372 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("*", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 121:
//#line 373 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("/", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 122:
//#line 374 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 123:
//#line 375 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 124:
//#line 376 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 125:
//#line 377 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 126:
//#line 378 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 127:
//#line 382 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 128:
//#line 383 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval, getLineaActual())); }
break;
case 129:
//#line 384 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 130:
//#line 385 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 131:
//#line 386 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 132:
//#line 387 "gramatica.y"
{ yyerror("No se permite este anidamiento, solo 1 expresion"); yyval = new ParserVal(null);}
break;
case 133:
//#line 391 "gramatica.y"
{
                                            yyval = new ParserVal(new NodoInvocacion(val_peek(4).sval, (NodoAST)val_peek(2).obj));
                                            System.out.println("Metodo|funcion con orden opcional"); }
break;
case 134:
//#line 394 "gramatica.y"
{
                                            yyval = new ParserVal(new NodoInvocacionMetodo(val_peek(6).sval, val_peek(4).sval, (NodoAST)val_peek(2).obj));
                                            System.out.println("Objeto con orden opcional"); }
break;
case 135:
//#line 397 "gramatica.y"
{ yyerror("Error en los parametros: Falta un ')'"); yyval = new ParserVal(null);}
break;
case 136:
//#line 398 "gramatica.y"
{ yyerror("Lista de parametros vacia."); yyval = new ParserVal(null);}
break;
case 137:
//#line 399 "gramatica.y"
{ yyerror("Lista de parametros vacia."); expresionInvalida = true; yyval = new ParserVal(null);}
break;
case 139:
//#line 404 "gramatica.y"
{ yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); }
break;
case 143:
//#line 414 "gramatica.y"
{
        yyval = new ParserVal(new NodoBinario("PARAM", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj));
    }
break;
case 144:
//#line 417 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 145:
//#line 418 "gramatica.y"
{yyerror("Falta ',' entre argumentos"); yyval = new ParserVal(null);}
break;
case 146:
//#line 419 "gramatica.y"
{yyerror("Elemento no valido despues de ','"); yyval = new ParserVal(null);}
break;
case 147:
//#line 423 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 148:
//#line 427 "gramatica.y"
{
        yyval = new ParserVal(new NodoRepeat((NodoAST)val_peek(4).obj, (NodoAST)val_peek(1).obj));
        System.out.println("Declaracion Iterativa WHILE");
    }
break;
case 149:
//#line 431 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 150:
//#line 432 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 151:
//#line 433 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 152:
//#line 434 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 153:
//#line 435 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 154:
//#line 436 "gramatica.y"
{ yyerror("error en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 155:
//#line 440 "gramatica.y"
{
        System.out.println("Declaracion Seleccion");
        yyval = new ParserVal(new NodoIF((NodoAST)val_peek(5).obj, (NodoAST)val_peek(3).obj, (NodoAST)val_peek(1).obj));
    }
break;
case 156:
//#line 444 "gramatica.y"
{
        System.out.println("Declaracion Seleccion Simple");
        yyval = new ParserVal(new NodoIF((NodoAST)val_peek(3).obj, (NodoAST)val_peek(1).obj, null));
    }
break;
case 157:
//#line 448 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); yyval = new ParserVal(null);}
break;
case 158:
//#line 449 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion"); yyval = new ParserVal(null);}
break;
case 159:
//#line 450 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); yyval = new ParserVal(null);}
break;
case 160:
//#line 451 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion"); yyval = new ParserVal(null);}
break;
case 161:
//#line 455 "gramatica.y"
{
        System.out.println("Condicion");
        NodoAST comp = (NodoAST) val_peek(1).obj;
        yyval = new ParserVal(new NodoBinario(comp.getEtiqueta(), (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj));
    }
break;
case 162:
//#line 463 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("==", "==", getLineaActual()));
        System.out.println("Comparador");
    }
break;
case 163:
//#line 467 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("!=", "!=", getLineaActual()));
        System.out.println("Comparador");
    }
break;
case 164:
//#line 471 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja(">=", ">=", getLineaActual()));
        System.out.println("Comparador");
    }
break;
case 165:
//#line 475 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("<=", "<=", getLineaActual()));
        System.out.println("Comparador");
    }
break;
case 166:
//#line 479 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja(">", ">", getLineaActual()));
        System.out.println("Comparador");
    }
break;
case 167:
//#line 483 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("<", "<", getLineaActual()));
        System.out.println("Comparador");
    }
break;
case 168:
//#line 490 "gramatica.y"
{
        yyval = val_peek(0);
        System.out.println("BloqueEJ1");
    }
break;
case 169:
//#line 494 "gramatica.y"
{
        yyval = val_peek(1);
        System.out.println("BloqueEJ2");
    }
break;
case 170:
//#line 498 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el BEGIN "); yyval = new ParserVal(null);}
break;
case 171:
//#line 499 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el END "); yyval = new ParserVal(null);}
break;
case 172:
//#line 503 "gramatica.y"
{
        yyval = new ParserVal(new NodoPrint(new NodoHoja("CADENA", val_peek(1).sval, getLineaActual())));
        System.out.println("POUT cadena");
    }
break;
case 173:
//#line 507 "gramatica.y"
{
        System.out.println("POUT expresion");
        yyval = new ParserVal(new NodoPrint((NodoAST)val_peek(1).obj));
    }
break;
case 174:
//#line 511 "gramatica.y"
{yyerror("Falta Argumento en POUT"); yyval = new ParserVal(null);}
break;
case 175:
//#line 512 "gramatica.y"
{yyerror ("Falta parentesis de cierre"); yyval = new ParserVal(null);}
break;
case 176:
//#line 513 "gramatica.y"
{yyerror ("Falta parentesis de apertura"); yyval = new ParserVal(null);}
break;
case 177:
//#line 514 "gramatica.y"
{yyerror ("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 178:
//#line 515 "gramatica.y"
{yyerror ("Falta parentesis de cierre"); yyval = new ParserVal(null);}
break;
case 179:
//#line 516 "gramatica.y"
{yyerror ("Falta parentesis de apertura"); yyval = new ParserVal(null);}
break;
case 180:
//#line 517 "gramatica.y"
{yyerror ("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 181:
//#line 521 "gramatica.y"
{
        yyval = new ParserVal(new NodoReturn((NodoAST)val_peek(1).obj));
        System.out.println("Declaracion Retorno");
    }
break;
case 182:
//#line 525 "gramatica.y"
{yyerror("Retorno debe tener una expresion y parentesis"); yyval = new ParserVal(null);}
break;
//#line 1831 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
