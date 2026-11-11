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
   10,    6,    6,    6,    6,    6,    6,    6,    6,   11,
   11,   12,   12,   12,    7,    7,    7,    7,   13,   13,
   13,   13,   14,   14,   16,   16,   16,   15,   15,   15,
   15,   18,   18,   18,   17,   17,   17,   17,   17,    8,
    8,    2,    2,   19,   19,   19,   19,   19,   19,   19,
   19,   19,   19,   20,   20,   20,   25,   25,   25,   27,
   27,   27,   27,   27,   28,   28,   26,   26,   26,   26,
   26,   26,   26,   29,   29,   29,   29,   29,   29,   29,
   30,   30,   30,   30,   30,   30,   32,   32,   32,   32,
   31,   31,   31,   31,   31,   31,   31,   34,   34,   34,
   34,   34,   34,   34,   35,   35,   35,   35,   35,   35,
   33,   33,   33,   33,   33,   37,   37,   37,   38,   38,
   36,   36,   36,   36,   39,   24,   24,   24,   24,   24,
   24,   24,   21,   21,   21,   21,   21,   21,   41,   42,
   42,   42,   42,   42,   42,   40,   40,   40,   40,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   23,   23,
};
final static short yylen[] = {                            2,
    5,    5,    4,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    1,
    2,   11,   11,   11,   11,   11,   11,   10,   11,    3,
    1,    2,    1,    1,    6,    6,    6,    5,    2,    1,
    1,    0,    2,    1,    1,    1,    1,    3,    3,    2,
    2,    3,    1,    2,    9,    9,    9,    9,    8,    3,
    2,    2,    1,    2,    2,    2,    2,    2,    1,    1,
    1,    1,    1,    3,    3,    3,    1,    1,    3,    4,
    3,    4,    5,    5,    1,    1,    3,    3,    3,    3,
    3,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    1,    5,    1,    1,    4,    4,    3,    2,
    3,    3,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    3,    3,    1,    1,    1,    1,    1,    1,    5,
    5,    7,    5,    4,    6,    3,    3,    0,    3,    1,
    3,    1,    2,    4,    1,    6,    6,    5,    5,    4,
    6,    6,    8,    6,    5,    5,    4,    6,    3,    1,
    1,    1,    1,    1,    1,    1,    3,    3,    3,    4,
    4,    3,    3,    3,    2,    3,    3,    2,    4,    3,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
    0,    0,    0,    0,    0,   63,    0,    0,    0,    0,
    0,    0,   78,   20,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   86,    0,    0,    0,    0,    0,    0,
    0,  102,    0,    0,    0,  103,    0,  100,  105,  106,
    0,    0,    0,    0,    0,    0,  166,    0,    3,   62,
   64,   65,   66,   67,   68,    0,    0,   21,   60,    0,
   85,    2,    0,    0,    0,   16,    0,    0,   12,    0,
   81,    0,   79,    4,    1,  180,    0,    0,    0,    0,
    0,    0,    0,    0,  110,    0,    0,  160,  161,  162,
  163,    0,    0,  164,  165,    0,    0,    0,    0,    0,
    0,  177,    0,  172,    0,  174,    0,    0,    0,    0,
    0,    0,    0,    0,   19,    0,   45,   47,    0,    0,
    0,   41,   44,   46,    0,   15,   14,    0,    0,    0,
   82,    0,   80,  179,    0,    0,    0,   97,   98,    0,
    0,    0,  142,    0,    0,    0,  109,    0,    0,    0,
    0,    0,    0,    0,    0,   96,   94,   99,   95,    0,
  157,  170,  171,    0,  168,  169,  167,    0,    0,    0,
    0,   53,   51,    0,    0,    0,   39,   43,    0,    0,
   33,    0,    0,   31,    0,    0,    0,   83,   84,    0,
  134,    0,    0,    0,  143,    0,    0,  126,  127,    0,
  128,  129,    0,  124,    0,  108,  107,    0,  155,  156,
    0,    0,    0,    0,  149,   49,   54,   48,    0,    0,
   36,   37,   35,   32,    0,    6,    6,    6,    6,    6,
    0,  140,    0,  133,    0,  141,  131,    0,    0,    0,
    0,    0,  104,    0,    0,    0,    0,    0,    0,  158,
    0,  154,  151,  147,  152,  146,   52,    0,    0,   30,
    0,    0,    0,    0,    0,  137,    0,  136,  144,    0,
    0,    0,  121,  122,    0,    0,    0,    0,    0,  120,
  118,  123,  119,  135,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  139,    0,  132,  153,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  130,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   25,   23,   29,   24,   26,   27,   22,   57,
   56,   58,   55,
};
final static short yydgoto[] = {                          3,
    4,  128,   12,   13,   14,   15,   16,   17,   18,   35,
  203,  204,  140,  141,  142,  143,  144,  194,   26,   27,
   28,   29,   30,   31,   32,   55,   33,   46,   57,   58,
  220,   59,   60,  223,  224,  162,  211,  253,  163,   68,
   61,  116,
};
final static short yysindex[] = {                       -66,
    0,    0,    0,  781,  764, -239, 1054,    0,    0, -149,
  -61,    0,    0,    0,    0,    0,    0, -192,  -69, 1054,
  -37,  -23,  -30,  909,  708,    0,   94,  121,  125,  149,
  217,  -15,    0,    0,  117,  -65,  975, -206, -239,  -21,
   36,  -19,    0,    0,  -52,  190,  916,  235,  201,  426,
   87,    0,  -26,  201,   89,    0,   68,    0,    0,    0,
  183,  259,   23,   96,  963, 1054,    0, -244,    0,    0,
    0,    0,    0,    0,    0,  262,  201,    0,    0,   44,
    0,    0, -172, -172,   18,    0,  270,  -24,    0,  236,
    0,  -38,    0,    0,    0,    0,  506,  277,  277,  -71,
  -71,  144,  299,   81,    0,  169,  283,    0,    0,    0,
    0,  323,  458,    0,    0,  201,  493,  504, 1054,  928,
   71,    0,  318,    0,  566,    0,  328,  982,  935,  335,
    4,  426,  180,  180,    0,   55,    0,    0,  140,  161,
 -172,    0,    0,    0, -224,    0,    0,   92,   92,  423,
    0,   38,    0,    0,   98,   68,   68,    0,    0,  332,
  180,  172,    0,  543,  391, 1058,    0,  622,  928,  168,
   98,   68,   98,   68,  180,    0,    0,    0,    0,  178,
    0,    0,    0,  201,    0,    0,    0,  201,  578,  400,
  392,    0,    0,   12,  415,  403,    0,    0,  411,  420,
    0,  229,  106,    0,  133,  459,  -17,    0,    0,  -44,
    0,  332,  597,  332,    0,  435,  471,    0,    0,  686,
    0,    0,  152,    0,  176,    0,    0, -167,    0,    0,
  464,  472, 1075,  480,    0,    0,    0,    0,  255,  456,
    0,    0,    0,    0,   92,    0,    0,    0,    0,    0,
  430,    0,   29,    0,  -36,    0,    0,  612,  612,  209,
  209,  486,    0,  617,  662,  665,  668,  332,    8,    0,
  928,    0,    0,    0,    0,    0,    0,  495,  287,    0,
  788,  795,  805,  812,  754,    0,  279,    0,    0,  231,
  152,  152,    0,    0,  201,  231,  152,  231,  152,    0,
    0,    0,    0,    0,  332,  272,  284, -181, 1054, 1054,
 1054, 1054, 1054, 1054,    0,  762,    0,    0, 1054, 1054,
 1054,  993, 1000, 1011, 1018, 1029,  947,    0, 1036, 1047,
  954,  497,  500,  508,  511,  512,  513,  531,  536,  537,
  539,  540,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   25,    0,
    0,    0,    0,    0,    0,    0,  824,  841,  858,  875,
  892,    0,    0,    0,  673,   25,    0,    0,    0,    0,
    0,  693,  298,    0,    0,    0,    0,    0,    0,    0,
   76,    0,    0,    0,    0,    0,  138,    0,    0,    0,
    0,  487,    0,  507,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  291,   74,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  524,    0,  541,    0,    0,    0,    0,    0,
    0,  559,  576,  595,    0,    0,    0,    0,    0,    0,
  111,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  227,  317,    0,    0,  113,
  258,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  376,  401,  431,  465,  345,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  615,
    0,    0,    0,  155,   34,    0,    0,    0,    0,  720,
    0,    7,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  113,    0,  113,    0,    0, 1092,    0,    0,    0,
    0,    0,  776,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  634,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  113,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  797,  816,    0,    0,    0,  853,  887, 1041, 1109,    0,
    0,    0,    0,    0,  113,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  737,    0,    0,
    0,  705,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    6,   -5,    0,  -54,    0,    0,    0,   21,  381,   24,
  -81,  300,  520,    0,  467,  478,    0,    0,   -4,    0,
    0,    0,    0,    0,    0,  -10,  -18,  575,   30,  669,
    0,  364,  650, 1051,  752,  402, -138,    0, -143,  -60,
  -20,    0,
};
final static int YYTABLESIZE=1325;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
  121,   37,   49,   56,   56,  100,   98,    5,   99,   63,
  101,  130,   64,  106,   47,  150,   54,   34,  215,   67,
   70,   45,   80,  250,   80,   45,  245,  131,  137,  137,
   56,  199,   70,  107,   40,   56,  200,   86,   97,   89,
   91,   42,   70,  189,   56,   77,  170,   34,  305,   83,
   34,  213,  125,   84,  153,  239,   67,   56,   56,  180,
  129,   80,   85,  124,   34,  133,  134,  205,  207,  256,
  238,   41,  287,  254,  320,  257,  147,   20,  321,   56,
   56,   56,   56,   56,    6,   77,  137,   56,  270,    8,
    9,  161,   20,   56,   56,  168,  208,   56,   56,   56,
  271,  272,   67,  138,  138,  175,  136,   38,  228,  117,
  190,  289,   56,  193,  118,   67,  101,  101,  101,  101,
  101,  288,  101,   70,   70,  215,  102,  156,  157,  304,
  209,  112,  104,  113,  101,  101,  126,  101,  112,  100,
  113,  172,  174,   56,  101,  219,  246,  103,  115,  245,
  114,  161,   71,  138,  138,  138,  138,  138,  279,  138,
   80,  138,   42,  231,   67,   56,  317,  232,  234,   56,
   56,  138,  138,  247,  138,   79,  245,   45,   93,   72,
   93,   93,   93,   73,  160,   51,   52,   43,   44,    1,
    2,   81,   44,  266,   56,   34,   93,   93,  267,   93,
    8,    9,  161,   90,   81,   44,   56,   74,   53,  167,
  306,  251,  214,  252,  161,  213,  268,  152,   48,   50,
   51,   52,  112,  120,  113,   50,   51,   52,   62,  105,
  212,  149,   50,   51,   52,   78,   56,   78,  249,  219,
  219,  219,  219,   53,  161,  219,  219,  219,  219,   53,
   56,  281,  282,  283,  284,  285,   53,   76,  161,   50,
   51,   52,   34,   50,   51,   52,   67,   90,  237,   90,
   90,   90,  260,  146,   78,   75,   56,  261,   50,   51,
   52,  123,   93,   53,  316,   90,   90,   53,   90,   20,
   20,   87,   88,   96,   20,   20,   20,   77,  145,  122,
  135,  145,   53,  322,  323,  324,  325,  326,  327,  148,
  191,  192,   20,  329,  330,  331,  145,   70,   70,   70,
   70,   70,   70,  169,   70,   70,   70,  308,  151,   42,
  245,  101,  101,  101,   42,  101,  101,  165,  164,  181,
  101,   20,  101,  101,  101,  101,  101,  101,  201,  101,
  101,  101,  101,    8,    9,  101,   20,   92,  182,   92,
   92,   92,  108,  109,  110,  111,   40,  184,  138,  138,
  138,   40,  138,  138,  188,   92,   92,  138,   92,  138,
  138,  138,  138,  138,  138,  159,  138,  138,  138,  138,
   85,   39,  138,   93,   93,   93,  195,   93,   93,   50,
   51,   52,   93,  159,   93,   93,   93,   93,   93,   93,
   50,   93,   93,   93,   93,   50,   89,   93,   89,   89,
   89,  196,  210,   53,  166,   51,   52,   50,   51,   52,
  225,   50,   51,   52,   89,   89,  229,   89,  119,   36,
  235,   87,   66,   87,   87,   87,  230,   21,   53,   22,
  236,   53,   23,   24,  240,   53,   50,   51,   52,   87,
   87,  241,   87,  139,  139,  217,  218,  100,   98,  242,
   99,   91,  101,   91,   91,   91,  260,  258,  243,  259,
   53,  261,   90,   90,   90,  244,   90,   90,   53,   91,
   91,   90,   91,   90,   90,   90,   90,   90,   90,  248,
   90,   90,   90,   90,  273,   88,   90,   88,   88,   88,
  102,  277,  274,  145,  145,  145,  104,  132,   51,   52,
  276,  139,  286,   88,   88,  295,   88,  221,  202,  202,
  202,  262,  155,   51,   52,  307,  315,  145,  119,   36,
  318,   53,   66,  319,  280,  178,  154,   21,  112,   22,
  113,   42,   23,   24,   20,  343,   53,   20,  344,   20,
   20,   45,   20,   20,   20,  175,  345,   20,   20,  346,
  347,  348,   92,   92,   92,   20,   92,   92,  171,   51,
   52,   92,  176,   92,   92,   92,   92,   92,   92,  349,
   92,   92,   92,   92,  350,  351,   92,  352,  353,  173,
  159,  159,   53,  145,  159,  159,  183,  197,  112,  159,
  113,  159,  159,  159,  159,  159,  159,   76,  198,   92,
  202,  221,  221,  221,  221,  202,  269,  221,  221,  221,
  221,   89,   89,   89,   74,   89,   89,    0,    0,    0,
   89,    0,   89,   89,   89,   89,   89,   89,    0,   89,
   89,   89,   89,   75,    0,   89,   87,   87,   87,    0,
   87,   87,  227,    0,  112,   87,  113,   87,   87,   87,
   87,   87,   87,  150,   87,   87,   87,   87,  206,  201,
   87,    0,    0,    0,    8,    9,   91,   91,   91,    0,
   91,   91,  148,    0,    0,   91,    0,   91,   91,   91,
   91,   91,   91,    0,   91,   91,   91,   91,    0,    0,
   91,  278,  201,  173,   51,   52,    0,    8,    9,    0,
   88,   88,   88,    0,   88,   88,  263,    0,  264,   88,
  265,   88,   88,   88,   88,   88,   88,   53,   88,   88,
   88,   88,  178,  178,   88,    0,    0,  178,  176,   51,
   52,  178,    0,  178,  178,  178,  178,  178,  178,  178,
   51,   52,  175,  175,    0,    0,    0,  175,  158,  159,
    0,  175,   53,  175,  175,  175,  175,  175,  175,  176,
  176,    0,    0,   53,  176,  177,  179,    0,  176,    0,
  176,  176,  176,  176,  176,  176,  173,  173,  216,  217,
  218,  173,  328,    0,  112,  173,  113,  173,  173,  173,
  173,  173,  173,  222,   76,   76,  117,    0,  117,   76,
  117,    0,   53,   76,    0,   76,   76,   76,   76,   76,
   76,   74,   74,  233,   51,   52,   74,  114,    0,  114,
   74,  114,   74,   74,   74,   74,   74,   74,    0,    0,
   75,   75,  255,   51,   52,   75,  116,   53,  116,   75,
  116,   75,   75,   75,   75,   75,   75,  290,  217,  218,
  150,  150,  296,  217,  218,  150,   53,    0,    0,  150,
    0,  150,  150,  150,  150,  150,  150,    0,    0,  148,
  148,   53,    0,  113,  148,  113,   53,  113,  148,    0,
  148,  148,  148,  148,  148,  148,    0,  222,  222,  222,
  222,    0,    0,  222,  222,  222,  222,  298,  217,  218,
  300,  217,  218,  302,  217,  218,    0,  111,   61,  111,
    0,  111,   61,   61,   61,   61,    0,   61,   61,   61,
    0,   53,   61,   61,   53,    0,    0,   53,   13,    0,
   61,   61,   13,   13,   13,   13,    0,   13,   13,   13,
   59,   59,   13,   13,   36,   59,   59,   59,   69,    0,
   13,   13,   21,    0,   22,   38,   38,   23,   24,   38,
    0,   38,   38,   59,   38,   38,   38,    0,    0,   38,
   38,    0,   28,   28,    0,    0,   28,   38,   28,   28,
    0,   28,   28,   28,    0,    0,   28,   28,    0,  313,
    6,  293,  294,  314,   28,    8,    9,  301,  303,   10,
   19,    0,    0,   20,    0,    8,    9,    0,   21,   10,
   22,   11,    0,   23,   24,    0,    0,    6,    0,    0,
    7,   11,    8,    9,    6,    0,   10,  309,    0,    8,
    9,    6,    0,   10,  310,    0,    8,    9,   11,    0,
   10,    6,    0,    0,  311,   11,    8,    9,    6,    0,
   10,  312,   11,    8,    9,    0,    0,   10,    0,   69,
   69,  115,   11,  115,   69,  115,    0,    0,   69,   11,
   69,   69,   69,   69,   69,   69,   70,   70,  226,  100,
   98,   70,   99,    0,  101,   70,    0,   70,   70,   70,
   70,   70,   70,   71,   71,  275,  100,   98,   71,   99,
    0,  101,   71,    0,   71,   71,   71,   71,   71,   71,
   72,   72,  125,  125,  125,   72,  125,    0,  125,   72,
    0,   72,   72,   72,   72,   72,   72,   73,   73,  112,
    0,  112,   73,  112,    0,    0,   73,    0,   73,   73,
   73,   73,   73,   73,   65,   36,    0,    0,   66,    0,
    0,   94,   36,   21,    0,   22,   95,    0,   23,   24,
   21,    0,   22,  119,   36,   23,   24,   66,    0,    0,
  186,   36,   21,    0,   22,  187,    0,   23,   24,   21,
    0,   22,  337,   36,   23,   24,    0,  338,    0,  341,
   36,   21,    0,   22,  342,    0,   23,   24,   21,   36,
   22,    0,    0,   23,   24,    0,    0,   21,    0,   22,
    0,   36,   23,   24,  127,   82,    0,    0,   36,   21,
    0,   22,  185,    0,   23,   24,   21,    0,   22,   36,
    0,   23,   24,  332,    0,    0,   36,   21,    0,   22,
  333,    0,   23,   24,   21,    0,   22,   36,    0,   23,
   24,  334,    0,    0,   36,   21,    0,   22,  335,    0,
   23,   24,   21,    0,   22,   36,    0,   23,   24,  336,
    0,    0,   36,   21,    0,   22,  339,    0,   23,   24,
   21,    0,   22,   36,    0,   23,   24,  340,  291,  292,
   36,   21,    0,   22,  297,  299,   23,   24,   21,    0,
   22,    0,    0,   23,   24,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
   61,    7,   40,   22,   23,   42,   43,    2,   45,   40,
   47,  256,   23,   40,   20,   40,   40,  257,  162,   24,
   25,   91,   44,   41,   44,   91,   44,  272,   83,   84,
   49,  256,   37,   54,   11,   54,  261,   59,   49,   59,
   93,   18,   47,   40,   63,   61,  107,   41,   41,  256,
   44,   44,   63,  260,   93,   44,   61,   76,   77,  120,
   66,   44,   39,   41,  257,   76,   77,  149,  150,  213,
   59,  264,   44,  212,  256,  214,   59,   44,  260,   98,
   99,  100,  101,  102,  257,   61,  141,  106,  256,  262,
  263,  102,   59,  112,  113,  106,   59,  116,  117,  118,
  268,  269,  107,   83,   84,  116,  279,  257,  169,   42,
  131,  255,  131,   59,   47,  120,   41,   42,   43,   44,
   45,   93,   47,  128,  129,  269,   40,   98,   99,  268,
   93,   43,   46,   45,   59,   60,   41,   62,   43,   42,
   45,  112,  113,  162,   47,  164,   41,   61,   60,   44,
   62,  162,   59,   41,   42,   43,   44,   45,  240,   47,
   44,  141,  139,  184,  169,  184,  305,  188,  189,  188,
  189,   59,   60,   41,   62,   59,   44,   91,   41,   59,
   43,   44,   45,   59,   41,  257,  258,  257,  258,  256,
  257,  257,  258,   42,  213,  257,   59,   60,   47,   62,
  262,  263,  213,  256,  257,  258,  225,   59,  280,   41,
  271,  256,   41,  258,  225,   44,   41,  256,  256,  256,
  257,  258,   43,   41,   45,  256,  257,  258,  259,  256,
   59,  256,  256,  257,  258,  257,  255,  257,  256,  258,
  259,  260,  261,  280,  255,  264,  265,  266,  267,  280,
  269,  246,  247,  248,  249,  250,  280,  273,  269,  256,
  257,  258,  256,  256,  257,  258,  271,   41,  257,   43,
   44,   45,   42,  256,  257,   59,  295,   47,  256,  257,
  258,  259,   93,  280,  295,   59,   60,  280,   62,  256,
  257,  256,  257,   59,  261,  262,  263,  273,   41,   41,
  257,   44,  280,  309,  310,  311,  312,  313,  314,   40,
  256,  257,  279,  319,  320,  321,   59,  322,  323,  324,
  325,  326,  327,   41,  329,  330,  331,   41,   93,  256,
   44,  256,  257,  258,  261,  260,  261,  257,   40,  269,
  265,   44,  267,  268,  269,  270,  271,  272,  257,  274,
  275,  276,  277,  262,  263,  280,   59,   41,   41,   43,
   44,   45,  274,  275,  276,  277,  256,   40,  256,  257,
  258,  261,  260,  261,   40,   59,   60,  265,   62,  267,
  268,  269,  270,  271,  272,   41,  274,  275,  276,  277,
   93,   11,  280,  256,  257,  258,  257,  260,  261,  256,
  257,  258,  265,   59,  267,  268,  269,  270,  271,  272,
  256,  274,  275,  276,  277,  261,   41,  280,   43,   44,
   45,  261,   91,  280,  256,  257,  258,  256,  257,  258,
   40,  256,  257,  258,   59,   60,  269,   62,  256,  257,
   41,   41,  260,   43,   44,   45,  269,  265,  280,  267,
   59,  280,  270,  271,   40,  280,  256,  257,  258,   59,
   60,   59,   62,   83,   84,  257,  258,   42,   43,   59,
   45,   41,   47,   43,   44,   45,   42,   43,   59,   45,
  280,   47,  256,  257,  258,  257,  260,  261,  280,   59,
   60,  265,   62,  267,  268,  269,  270,  271,  272,   41,
  274,  275,  276,  277,   41,   41,  280,   43,   44,   45,
   40,  257,   41,  256,  257,  258,   46,  256,  257,  258,
   41,  141,   93,   59,   60,   40,   62,  164,  148,  149,
  150,   61,  256,  257,  258,   41,  258,  280,  256,  257,
  269,  280,  260,  260,  245,   59,   41,  265,   43,  267,
   45,  261,  270,  271,  257,   59,  280,  260,   59,  262,
  263,   91,  265,  266,  267,   59,   59,  270,  271,   59,
   59,   59,  256,  257,  258,  278,  260,  261,  256,  257,
  258,  265,   59,  267,  268,  269,  270,  271,  272,   59,
  274,  275,  276,  277,   59,   59,  280,   59,   59,   59,
  256,  257,  280,   84,  260,  261,   41,  141,   43,  265,
   45,  267,  268,  269,  270,  271,  272,   59,  141,   45,
  240,  258,  259,  260,  261,  245,  225,  264,  265,  266,
  267,  256,  257,  258,   59,  260,  261,   -1,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,   59,   -1,  280,  256,  257,  258,   -1,
  260,  261,   41,   -1,   43,  265,   45,  267,  268,  269,
  270,  271,  272,   59,  274,  275,  276,  277,  256,  257,
  280,   -1,   -1,   -1,  262,  263,  256,  257,  258,   -1,
  260,  261,   59,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,  256,  257,  256,  257,  258,   -1,  262,  263,   -1,
  256,  257,  258,   -1,  260,  261,   41,   -1,   43,  265,
   45,  267,  268,  269,  270,  271,  272,  280,  274,  275,
  276,  277,  256,  257,  280,   -1,   -1,  261,  256,  257,
  258,  265,   -1,  267,  268,  269,  270,  271,  272,  256,
  257,  258,  256,  257,   -1,   -1,   -1,  261,  100,  101,
   -1,  265,  280,  267,  268,  269,  270,  271,  272,  256,
  257,   -1,   -1,  280,  261,  117,  118,   -1,  265,   -1,
  267,  268,  269,  270,  271,  272,  256,  257,  256,  257,
  258,  261,   41,   -1,   43,  265,   45,  267,  268,  269,
  270,  271,  272,  164,  256,  257,   41,   -1,   43,  261,
   45,   -1,  280,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,  256,  257,  258,  261,   41,   -1,   43,
  265,   45,  267,  268,  269,  270,  271,  272,   -1,   -1,
  256,  257,  256,  257,  258,  261,   41,  280,   43,  265,
   45,  267,  268,  269,  270,  271,  272,  256,  257,  258,
  256,  257,  256,  257,  258,  261,  280,   -1,   -1,  265,
   -1,  267,  268,  269,  270,  271,  272,   -1,   -1,  256,
  257,  280,   -1,   41,  261,   43,  280,   45,  265,   -1,
  267,  268,  269,  270,  271,  272,   -1,  258,  259,  260,
  261,   -1,   -1,  264,  265,  266,  267,  256,  257,  258,
  256,  257,  258,  256,  257,  258,   -1,   41,  256,   43,
   -1,   45,  260,  261,  262,  263,   -1,  265,  266,  267,
   -1,  280,  270,  271,  280,   -1,   -1,  280,  256,   -1,
  278,  279,  260,  261,  262,  263,   -1,  265,  266,  267,
  256,  257,  270,  271,  257,  261,  262,  263,  261,   -1,
  278,  279,  265,   -1,  267,  256,  257,  270,  271,  260,
   -1,  262,  263,  279,  265,  266,  267,   -1,   -1,  270,
  271,   -1,  256,  257,   -1,   -1,  260,  278,  262,  263,
   -1,  265,  266,  267,   -1,   -1,  270,  271,   -1,  256,
  257,  260,  261,  260,  278,  262,  263,  266,  267,  266,
  257,   -1,   -1,  260,   -1,  262,  263,   -1,  265,  266,
  267,  278,   -1,  270,  271,   -1,   -1,  257,   -1,   -1,
  260,  278,  262,  263,  257,   -1,  266,  260,   -1,  262,
  263,  257,   -1,  266,  260,   -1,  262,  263,  278,   -1,
  266,  257,   -1,   -1,  260,  278,  262,  263,  257,   -1,
  266,  260,  278,  262,  263,   -1,   -1,  266,   -1,  256,
  257,   41,  278,   43,  261,   45,   -1,   -1,  265,  278,
  267,  268,  269,  270,  271,  272,  256,  257,   41,   42,
   43,  261,   45,   -1,   47,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,   41,   42,   43,  261,   45,
   -1,   47,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   41,   42,   43,  261,   45,   -1,   47,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,   41,
   -1,   43,  261,   45,   -1,   -1,  265,   -1,  267,  268,
  269,  270,  271,  272,  256,  257,   -1,   -1,  260,   -1,
   -1,  256,  257,  265,   -1,  267,  261,   -1,  270,  271,
  265,   -1,  267,  256,  257,  270,  271,  260,   -1,   -1,
  256,  257,  265,   -1,  267,  261,   -1,  270,  271,  265,
   -1,  267,  256,  257,  270,  271,   -1,  261,   -1,  256,
  257,  265,   -1,  267,  261,   -1,  270,  271,  265,  257,
  267,   -1,   -1,  270,  271,   -1,   -1,  265,   -1,  267,
   -1,  257,  270,  271,  272,  261,   -1,   -1,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,  257,
   -1,  270,  271,  261,   -1,   -1,  257,  265,   -1,  267,
  261,   -1,  270,  271,  265,   -1,  267,  257,   -1,  270,
  271,  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,   -1,  267,  257,   -1,  270,  271,  261,
   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,
  265,   -1,  267,  257,   -1,  270,  271,  261,  258,  259,
  257,  265,   -1,  267,  264,  265,  270,  271,  265,   -1,
  267,   -1,   -1,  270,  271,
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
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID error parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales error sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas error sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables error ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END",
"declaracion_funcion : tipo FUNCTION ID '(' error ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametros_formales : parametro_formal",
"parametro_formal : tipo ID",
"parametro_formal : ID",
"parametro_formal : tipo",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase END ';'",
"declaracion_clase : CLASS ID error cuerpo_clase END ';'",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase error ';'",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase END",
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

//#line 470 "gramatica.y"
private AnalizadorLexicoTp3 lexer;
private TablaDeSimbolosTp3 tablaSimbolos;
private GestionAmbitoTp3 gestorAmbitos;
private boolean expresionInvalida = false;
private NodoAST raizAST;

public Parser(AnalizadorLexicoTp3 lexer){
    this.lexer = lexer;
    this.tablaSimbolos = lexer.getTablaSimbolos();
    this.gestorAmbitos = new GestionAmbitoTp3(this.tablaSimbolos);
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
    System.err.println("Error semántico en la linea " + lexer.getLineaActual() + ": " + mensaje);
}

private void yyerror(String mensaje){
    System.err.println("Error sintáctico en la linea " + lexer.getLineaActual() + ": " + mensaje);
}

private void actualizarComptimeSiCorresponde(NodoAST izquierdo, NodoAST derecho) {
    if (!(izquierdo instanceof NodoHoja) || !(derecho instanceof NodoHoja)) {
        return;
    }

    NodoHoja hojaIzq = (NodoHoja) izquierdo;
    NodoHoja hojaDer = (NodoHoja) derecho;

    // Solo actualizamos asignaciones simples del tipo: ID := CTE
    if (!"ID".equals(hojaIzq.getTipoNodo())
            || !"CTE".equals(hojaDer.getTipoNodo())) {
        return;
    }

    RegistroSimboloTp3 reg = tablaSimbolos.buscar(hojaIzq.getValor());

    if (reg != null && reg.esComptime()) {
        tablaSimbolos.actualizarValorComptime(
            hojaIzq.getValor(),
            hojaDer.getValor(),
            lexer.getLineaActual()
        );
    }
}
//#line 837 "Parser.java"
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
        NodoPrograma raiz = new NodoPrograma(val_peek(4).sval, (NodoAST)val_peek(1).obj);
        raizAST = raiz;
        yyval = new ParserVal(raiz);
        System.out.println("Declaración de programa y AST generado");
    }
break;
case 2:
//#line 32 "gramatica.y"
{ yyerror("Falta de nombre de programa");}
break;
case 3:
//#line 33 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN");}
break;
case 4:
//#line 34 "gramatica.y"
{ yyerror("Falta la palabra reservada END");}
break;
case 12:
//#line 52 "gramatica.y"
{
        System.out.println("Declaracion de VAR");
        declararListaVariables((List<String>) val_peek(1).obj, val_peek(2).sval, false);
    }
break;
case 13:
//#line 56 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 14:
//#line 60 "gramatica.y"
{
        System.out.println("Declaracion de comptime");
        declararListaVariables((List<String>) val_peek(1).obj, val_peek(2).sval, true);
    }
break;
case 15:
//#line 64 "gramatica.y"
{ yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
break;
case 16:
//#line 65 "gramatica.y"
{ yyerror("Falta tipo en las variables en COMPTIME");}
break;
case 17:
//#line 69 "gramatica.y"
{ System.out.println("Declaracion de tipo");
                yyval = new ParserVal("USHORTINT");
    }
break;
case 18:
//#line 72 "gramatica.y"
{ System.out.println("Declaracion de tipo");
                yyval = new ParserVal("DOUBLEF");
    }
break;
case 19:
//#line 78 "gramatica.y"
{
        List<String> lista = (List<String>) val_peek(2).obj;
        lista.add(val_peek(0).sval);
        yyval = val_peek(2);
    }
break;
case 20:
//#line 83 "gramatica.y"
{
        List<String> lista = new ArrayList<>();
        lista.add(val_peek(0).sval);
        yyval = new ParserVal(lista);
    }
break;
case 21:
//#line 88 "gramatica.y"
{ 
        yyerror("Lista de variables: Falta la ','");}
break;
case 22:
//#line 93 "gramatica.y"
{System.out.println("Declaracion de funcion");}
break;
case 23:
//#line 94 "gramatica.y"
{ yyerror("Falta parentesis de apertura en el encabezado de la función"); }
break;
case 24:
//#line 95 "gramatica.y"
{ yyerror("Falta parentesis de cierre en el encabezado de la función"); }
break;
case 25:
//#line 96 "gramatica.y"
{ yyerror("Falta de nombre de funcion");}
break;
case 26:
//#line 97 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN de la funcion");}
break;
case 27:
//#line 98 "gramatica.y"
{ yyerror("Falta la palabra reservada END de la funcion");}
break;
case 28:
//#line 99 "gramatica.y"
{ yyerror("Falta el ';'");}
break;
case 29:
//#line 100 "gramatica.y"
{ yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
break;
case 30:
//#line 104 "gramatica.y"
{System.out.println("Declaracion de parametro_formales");}
break;
case 31:
//#line 105 "gramatica.y"
{System.out.println("Declaracion de parametro_formales");}
break;
case 32:
//#line 109 "gramatica.y"
{ System.out.println("Declaracion de parametro_formal"); }
break;
case 33:
//#line 110 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'tipo'");}
break;
case 34:
//#line 111 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'ID''");}
break;
case 35:
//#line 115 "gramatica.y"
{System.out.println("Declaracion de clase");}
break;
case 36:
//#line 116 "gramatica.y"
{ yyerror ("Falta el BEGIN de clase");}
break;
case 37:
//#line 117 "gramatica.y"
{ yyerror("Falta el END de clase");}
break;
case 38:
//#line 118 "gramatica.y"
{ yyerror("Falta el ';' al final de la clase");}
break;
case 48:
//#line 140 "gramatica.y"
{System.out.println("Sentencia EXTENDS");}
break;
case 49:
//#line 141 "gramatica.y"
{ yyerror("Sintaxis de lista de herencia invalida"); }
break;
case 50:
//#line 142 "gramatica.y"
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 51:
//#line 143 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 54:
//#line 149 "gramatica.y"
{ yyerror("Lista de IDs: Falta la ','");}
break;
case 55:
//#line 153 "gramatica.y"
{
        System.out.println("Declaracion de Metodo");
        RegistroSimboloTp3 metReg = gestorAmbitos.declararMetodo(
        val_peek(7).sval, val_peek(8).sval, lexer.getLineaActual()
    );
    if (metReg == null) {
        yyerror("Método ya declarado en el ámbito actual: " + val_peek(7).sval);
    }
}
break;
case 56:
//#line 162 "gramatica.y"
{ yyerror("Falta el BEGIN en la declaracion del metodo");}
break;
case 57:
//#line 163 "gramatica.y"
{ yyerror("Falta una ',' o tipo invalido en los parametros del metodo"); }
break;
case 58:
//#line 164 "gramatica.y"
{ yyerror("Falta el END en la declaracion del metodo");}
break;
case 59:
//#line 165 "gramatica.y"
{ yyerror("Falta el ';' en la declaracion del metodo");}
break;
case 60:
//#line 169 "gramatica.y"
{
        System.out.println("Declaracion de objeto");
        List<String> nombres = (List<String>) val_peek(1).obj;
        String tipoClase = val_peek(2).sval;

        for (String nombre : nombres) {
            RegistroSimboloTp3 obj = gestorAmbitos.declararObjeto(nombre, tipoClase, lexer.getLineaActual());
            if (obj == null) {
                yyerror("No se pudo declarar el objeto '" + nombre + "': clase no visible o identificador duplicado");
            }
        }
    }
break;
case 61:
//#line 181 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'");}
break;
case 62:
//#line 186 "gramatica.y"
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
case 63:
//#line 198 "gramatica.y"
{
        NodoBloque bloque = new NodoBloque();
        if (val_peek(0).obj != null) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);
    }
break;
case 64:
//#line 208 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 65:
//#line 209 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 66:
//#line 210 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 67:
//#line 211 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 68:
//#line 212 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 69:
//#line 213 "gramatica.y"
{ yyerror("Asignacion: Falta el ';', o un operador o expresion");}
break;
case 70:
//#line 214 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");}
break;
case 71:
//#line 215 "gramatica.y"
{ yyerror("Salida: Falta el ';'");}
break;
case 72:
//#line 216 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");}
break;
case 73:
//#line 217 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");}
break;
case 74:
//#line 221 "gramatica.y"
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
case 75:
//#line 235 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); yyval = new ParserVal(null); }
break;
case 76:
//#line 236 "gramatica.y"
{ yyerror("Falta expresion en la asignacion"); yyval = new ParserVal(null);}
break;
case 77:
//#line 240 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval)); }
break;
case 78:
//#line 241 "gramatica.y"
{
        yyval = val_peek(0); 
        System.out.println("Declaracion de acceso_posicional");}
break;
case 79:
//#line 244 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de apertura "); yyval = new ParserVal(null);}
break;
case 80:
//#line 248 "gramatica.y"
{ yyval = new ParserVal(new NodoAccesoArreglo(new NodoHoja("ID", val_peek(3).sval), (NodoAST)val_peek(1).obj)); }
break;
case 81:
//#line 249 "gramatica.y"
{ yyerror("Acceso posicional: Índice vacío"); yyval = new ParserVal(null);}
break;
case 82:
//#line 250 "gramatica.y"
{ yyerror("Acceso posicional: Índice inválido"); yyval = new ParserVal(null);}
break;
case 83:
//#line 251 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de cierre"); yyval = new ParserVal(null);}
break;
case 84:
//#line 252 "gramatica.y"
{ yyerror("Acceso posicional: Argumento invalido"); yyval = new ParserVal(null);}
break;
case 85:
//#line 256 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval)); }
break;
case 86:
//#line 257 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval)); }
break;
case 87:
//#line 261 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("+", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 88:
//#line 262 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("-", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 89:
//#line 263 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 90:
//#line 264 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 91:
//#line 265 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 92:
//#line 266 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 93:
//#line 267 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 94:
//#line 271 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("*", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 95:
//#line 272 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("/", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 96:
//#line 273 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 97:
//#line 274 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 98:
//#line 275 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 99:
//#line 276 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 100:
//#line 277 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 101:
//#line 281 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval)); }
break;
case 102:
//#line 282 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval)); }
break;
case 103:
//#line 283 "gramatica.y"
{
        yyval = val_peek(0);
        System.out.println("Acceso posicional");}
break;
case 104:
//#line 286 "gramatica.y"
{
        yyval = new ParserVal(new NodoAsignacion(new NodoHoja("ID", val_peek(4).sval), (NodoAST)val_peek(1).obj));
        System.out.println("Asignacion EXP");}
break;
case 105:
//#line 289 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 106:
//#line 290 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 107:
//#line 294 "gramatica.y"
{
        yyval = new ParserVal(new NodoUnario("TODF", (NodoAST)val_peek(1).obj));
        System.out.println("Conversion DF"); 
    }
break;
case 108:
//#line 298 "gramatica.y"
{ yyerror("Solo se pueden expresiones"); yyval = new ParserVal(null);}
break;
case 109:
//#line 299 "gramatica.y"
{ yyerror("Falta expresion"); yyval = new ParserVal(null);}
break;
case 110:
//#line 300 "gramatica.y"
{ yyerror("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 111:
//#line 304 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("+", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 112:
//#line 305 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("-", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 113:
//#line 306 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 114:
//#line 307 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 115:
//#line 308 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 116:
//#line 309 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 117:
//#line 310 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 118:
//#line 314 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("*", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 119:
//#line 315 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("/", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 120:
//#line 316 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 121:
//#line 317 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 122:
//#line 318 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 123:
//#line 319 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 124:
//#line 320 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 125:
//#line 324 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval)); }
break;
case 126:
//#line 325 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval)); }
break;
case 127:
//#line 326 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 128:
//#line 327 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 129:
//#line 328 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 130:
//#line 329 "gramatica.y"
{ yyerror("No se permite este anidamiento, solo 1 expresion"); yyval = new ParserVal(null);}
break;
case 131:
//#line 333 "gramatica.y"
{
                                            yyval = new ParserVal(new NodoInvocacion(val_peek(4).sval, (NodoAST)val_peek(2).obj));
                                            System.out.println("Metodo|funcion con orden opcional"); }
break;
case 132:
//#line 336 "gramatica.y"
{
                                            yyval = new ParserVal(new NodoInvocacionMetodo(val_peek(6).sval, val_peek(4).sval, (NodoAST)val_peek(2).obj));
                                            System.out.println("Objeto con orden opcional"); }
break;
case 133:
//#line 339 "gramatica.y"
{ yyerror("Error en los parametros: Falta un ')'"); yyval = new ParserVal(null);}
break;
case 134:
//#line 340 "gramatica.y"
{ yyerror("Lista de parametros vacia."); yyval = new ParserVal(null);}
break;
case 135:
//#line 341 "gramatica.y"
{ yyerror("Lista de parametros vacia."); expresionInvalida = true; yyval = new ParserVal(null);}
break;
case 137:
//#line 346 "gramatica.y"
{ yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); }
break;
case 141:
//#line 356 "gramatica.y"
{
        yyval = new ParserVal(new NodoBinario("PARAM", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj));
    }
break;
case 142:
//#line 359 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 143:
//#line 360 "gramatica.y"
{yyerror("Falta ',' entre argumentos"); yyval = new ParserVal(null);}
break;
case 144:
//#line 361 "gramatica.y"
{yyerror("Elemento no valido despues de ','"); yyval = new ParserVal(null);}
break;
case 145:
//#line 365 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 146:
//#line 369 "gramatica.y"
{
        yyval = new ParserVal(new NodoRepeat((NodoAST)val_peek(4).obj, (NodoAST)val_peek(1).obj));
        System.out.println("Declaracion Iterativa WHILE");
    }
break;
case 147:
//#line 373 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 148:
//#line 374 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 149:
//#line 375 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 150:
//#line 376 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 151:
//#line 377 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 152:
//#line 378 "gramatica.y"
{ yyerror("error en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 153:
//#line 382 "gramatica.y"
{
        System.out.println("Declaracion Seleccion");
        yyval = new ParserVal(new NodoIF((NodoAST)val_peek(5).obj, (NodoAST)val_peek(3).obj, (NodoAST)val_peek(1).obj));
    }
break;
case 154:
//#line 386 "gramatica.y"
{
        System.out.println("Declaracion Seleccion Simple");
        yyval = new ParserVal(new NodoIF((NodoAST)val_peek(3).obj, (NodoAST)val_peek(1).obj, null));
    }
break;
case 155:
//#line 390 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); yyval = new ParserVal(null);}
break;
case 156:
//#line 391 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion"); yyval = new ParserVal(null);}
break;
case 157:
//#line 392 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); yyval = new ParserVal(null);}
break;
case 158:
//#line 393 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion"); yyval = new ParserVal(null);}
break;
case 159:
//#line 397 "gramatica.y"
{
        System.out.println("Condicion");
        NodoAST comp = (NodoAST) val_peek(1).obj;
        yyval = new ParserVal(new NodoBinario(comp.getEtiqueta(), (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj));
    }
break;
case 160:
//#line 405 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("==", "=="));
        System.out.println("Comparador");
    }
break;
case 161:
//#line 409 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("!=", "!="));
        System.out.println("Comparador");
    }
break;
case 162:
//#line 413 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja(">=", ">="));
        System.out.println("Comparador");
    }
break;
case 163:
//#line 417 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("<=", "<="));
        System.out.println("Comparador");
    }
break;
case 164:
//#line 421 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja(">", ">"));
        System.out.println("Comparador");
    }
break;
case 165:
//#line 425 "gramatica.y"
{
        yyval = new ParserVal(new NodoHoja("<", "<"));
        System.out.println("Comparador");
    }
break;
case 166:
//#line 432 "gramatica.y"
{
        yyval = val_peek(0);
        System.out.println("BloqueEJ1");
    }
break;
case 167:
//#line 436 "gramatica.y"
{
        yyval = val_peek(1);
        System.out.println("BloqueEJ2");
    }
break;
case 168:
//#line 440 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el BEGIN "); yyval = new ParserVal(null);}
break;
case 169:
//#line 441 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el END "); yyval = new ParserVal(null);}
break;
case 170:
//#line 445 "gramatica.y"
{
        yyval = new ParserVal(new NodoPrint(new NodoHoja("CADENA", val_peek(1).sval)));
        System.out.println("POUT cadena");
    }
break;
case 171:
//#line 449 "gramatica.y"
{
        System.out.println("POUT expresion");
        yyval = new ParserVal(new NodoPrint((NodoAST)val_peek(1).obj));
    }
break;
case 172:
//#line 453 "gramatica.y"
{yyerror("Falta Argumento en POUT"); yyval = new ParserVal(null);}
break;
case 173:
//#line 454 "gramatica.y"
{yyerror ("Falta parentesis de cierre"); yyval = new ParserVal(null);}
break;
case 174:
//#line 455 "gramatica.y"
{yyerror ("Falta parentesis de apertura"); yyval = new ParserVal(null);}
break;
case 175:
//#line 456 "gramatica.y"
{yyerror ("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 176:
//#line 457 "gramatica.y"
{yyerror ("Falta parentesis de cierre"); yyval = new ParserVal(null);}
break;
case 177:
//#line 458 "gramatica.y"
{yyerror ("Falta parentesis de apertura"); yyval = new ParserVal(null);}
break;
case 178:
//#line 459 "gramatica.y"
{yyerror ("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 179:
//#line 463 "gramatica.y"
{
        yyval = new ParserVal(new NodoReturn((NodoAST)val_peek(1).obj));
        System.out.println("Declaracion Retorno");
    }
break;
case 180:
//#line 467 "gramatica.y"
{yyerror("Retorno debe tener una expresion y parentesis"); yyval = new ParserVal(null);}
break;
//#line 1752 "Parser.java"
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
