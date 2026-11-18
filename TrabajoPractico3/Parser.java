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
   10,    6,    6,    6,    6,    6,    6,    6,    6,   13,
   12,   12,   11,   14,   14,   14,    7,    7,    7,    7,
   15,   16,   16,   16,   16,   17,   17,   19,   19,   19,
   18,   18,   18,   18,   21,   21,   21,   20,   20,   20,
   20,   20,    8,    8,    2,    2,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   23,   23,   23,   28,
   28,   28,   30,   30,   30,   30,   30,   31,   31,   29,
   29,   29,   29,   29,   29,   29,   32,   32,   32,   32,
   32,   32,   32,   33,   33,   33,   33,   33,   33,   35,
   35,   35,   35,   34,   34,   34,   34,   34,   34,   34,
   37,   37,   37,   37,   37,   37,   37,   38,   38,   38,
   38,   38,   38,   36,   36,   36,   36,   36,   40,   40,
   40,   41,   41,   39,   39,   39,   39,   42,   27,   27,
   27,   27,   27,   27,   27,   24,   24,   24,   24,   24,
   24,   44,   45,   45,   45,   45,   45,   45,   43,   43,
   43,   43,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   26,   26,
};
final static short yylen[] = {                            2,
    5,    5,    4,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    1,
    2,    9,   11,   11,   11,   11,   11,   10,   11,    2,
    3,    1,    3,    2,    1,    1,    5,    6,    6,    5,
    2,    2,    1,    1,    0,    2,    1,    1,    1,    1,
    3,    3,    2,    2,    3,    1,    2,    8,    9,    9,
    9,    8,    3,    2,    2,    1,    2,    2,    2,    2,
    2,    1,    1,    1,    1,    1,    3,    3,    3,    1,
    1,    3,    4,    3,    4,    5,    5,    1,    1,    3,
    3,    3,    3,    3,    3,    1,    3,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    5,    1,    1,    4,
    4,    3,    2,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    1,    1,    1,    1,
    1,    1,    5,    5,    7,    5,    4,    6,    3,    3,
    0,    3,    1,    3,    1,    2,    4,    1,    6,    6,
    5,    5,    4,    6,    6,    8,    6,    5,    5,    4,
    6,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    3,    3,    4,    4,    3,    3,    3,    2,    3,    3,
    2,    4,    3,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,    0,    0,
    0,    0,    0,    0,   81,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   89,    0,    0,
    0,    0,    0,    0,    0,  105,    0,    0,    0,  106,
    0,  103,  108,  109,    0,    0,    0,    0,    0,    0,
  169,    0,    3,   65,   67,   68,   69,   70,   71,    0,
    0,   21,   63,    0,   88,    2,    0,    0,    0,   16,
    0,    0,   12,   35,    0,    0,   32,    0,   48,   50,
    0,    0,    0,    0,   44,   47,   49,    0,   84,    0,
   82,    4,    1,  183,    0,    0,    0,    0,    0,    0,
    0,    0,  113,    0,    0,  163,  164,  165,  166,    0,
    0,  167,  168,    0,    0,    0,    0,    0,    0,  180,
    0,  175,    0,  177,    0,    0,    0,    0,    0,    0,
    0,    0,   19,    0,    0,   15,   14,    0,    0,    0,
   34,    0,    6,    0,   56,   54,    0,    0,    0,    0,
   42,   46,   85,    0,   83,  182,    0,    0,    0,  100,
  101,    0,    0,    0,  145,    0,    0,    0,  112,    0,
    0,    0,    0,    0,    0,    0,    0,   99,   97,  102,
   98,    0,  160,  173,  174,    0,  171,  172,  170,    0,
    0,    0,    0,    0,   40,    0,    0,    0,    0,   31,
    0,   52,   57,   51,    0,    0,    0,   37,   86,   87,
    0,  137,    0,    0,    0,  146,    0,    0,  129,  130,
    0,  131,  132,    0,  127,    0,  111,  110,    0,  158,
  159,    0,    0,    0,    0,  152,   38,   39,    6,    6,
    6,    6,    6,    0,   55,    0,    0,    0,    0,  143,
    0,  136,    0,  144,  134,    0,    0,    0,    0,    0,
  107,    0,    0,    0,    0,    0,    0,  161,    0,  157,
  154,  150,  155,  149,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  140,    0,  139,  147,    0,    0,    0,
  124,  125,    0,    0,    0,    0,    0,  123,  121,  126,
  122,  138,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  142,    0,  135,  156,    0,
    0,    0,    0,    0,    0,   22,    0,    0,    0,    0,
  133,    0,    0,    0,    0,    0,    0,   28,    0,    0,
    0,   62,   58,   25,   23,   29,   24,   26,   27,   60,
   59,   61,
};
final static short yydgoto[] = {                          3,
    4,  146,   12,   13,   14,   15,   16,   17,   18,   37,
   19,   96,  102,   97,   20,  103,  104,  105,  106,  107,
  167,   28,   29,   30,   31,   32,   33,   34,   59,   35,
   50,   61,   62,  241,   63,   64,  244,  245,  184,  232,
  271,  185,   72,   65,  134,
};
final static short yysindex[] = {                      -236,
    0,    0,    0,  810,  793, -218, 1117,    0,    0, -183,
  262,    0,    0,    0,    0,    0,    0, -137,   72, -138,
  -76, 1117,  -35,  -34,  -39,  945, 1011,    0,   90,   96,
  109,  113,  127,  -30,    0,    0,   14,  -62, 1018,  -12,
 -218,  -10, -125,   -8,  352, -112,    0,    0,   91,  133,
  952,  189, -175,  117,  269,    0,  -24, -175,  313,    0,
   47,    0,    0,    0,  516,  213,   46,  591,  999, 1117,
    0, -208,    0,    0,    0,    0,    0,    0,    0, -132,
 -175,    0,    0,   20,    0,    0, -112, -112,  -18,    0,
  241,   35,    0,    0,   42,  -32,    0,   17,    0,    0,
   44,  260,   52, -112,    0,    0,    0,  226,    0,  -86,
    0,    0,    0,    0,  616,  228,  228, -151, -151,  165,
  294,   89,    0,  168,  534,    0,    0,    0,    0,  363,
  448,    0,    0, -175,  463,  483, 1117,  964,   84,    0,
  335,    0,  737,    0,  344, 1029,  971,  346,  -29,  117,
   43,   43,    0,  155, -109,    0,    0,  352,  352,  171,
    0,  352,    0,  361,    0,    0,   18,    0,  352,  372,
    0,    0,    0,  -40,    0,    0,  146,   47,   47,    0,
    0,  303,   43,   26,    0,  504,  402,  826,    0,  791,
  964,  166,  146,   47,  146,   47,   43,    0,    0,    0,
    0,  175,    0,    0,    0, -175,    0,    0,    0, -175,
  513,  415,  400,  401,    0,  125,  160,  425,  -16,    0,
  817,    0,    0,    0,  215,  190,  249,    0,    0,    0,
  -78,    0,  303,  558,  303,    0,  340,  397,    0,    0,
  794,    0,    0,  218,    0,  193,    0,    0, -200,    0,
    0,  437,  446,  842,  449,    0,    0,    0,    0,    0,
    0,    0,    0, 1117,    0,  456,  266,  238,  406,    0,
  -36,    0,  509,    0,    0,  561,  561, -140, -140,  473,
    0,  564,  567,  595,  606,  303,  161,    0,  964,    0,
    0,    0,    0,    0,  824,  834,  841,  848,  783, 1036,
  254, -156, 1117,    0,  258,    0,    0,  325,  218,  218,
    0,    0, -175,  325,  218,  325,  218,    0,    0,    0,
    0,    0,  303,  248, 1117, 1117, 1117, 1117, 1117, 1117,
  464, 1117, 1117, 1117, 1047,    0,  866,    0,    0, 1054,
 1065, 1072, 1083, 1090,  983,    0, 1101, 1108,  990,  467,
    0,  469,  476,  487,  500,  503,  507,    0,  508,  511,
  522,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -23,    0,    0,    0,    0,    0,    0,    0,  860,  877,
  894,  911,  928,    0,    0,    0,  743,  -23,    0,  267,
    0,    0,    0,  763,    0,  271,  298,    0,    0,    0,
    0,    0,    0,    0,   94,    0,    0,    0,    0,    0,
  235,    0,    0,    0,    0,  541,    0,  589,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  271,  149,    0,    0,
    0,  542,    0,    0,  -14,    0,    0,    0,    0,    0,
    0,    0,    0,  206,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  609,    0,  634,    0,    0,    0,    0,    0,    0,  656,
  673,  691,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  233,  -26,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  273,  336,    0,
    0,  132,   31,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  370,  395,  420,  477,  442,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  709,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  132,    0,  132,    0,    0,  851,    0,    0,
    0,    0,    0,  873,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  726,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  132,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  890,  908,
    0,    0,    0,  926,  943,  975, 1077,    0,    0,    0,
    0,    0,  132,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    8,   -5,    0,    4,    0,    0,    0,  112,  671,   60,
    0, -115,    0,  421,    0,  124,    0,  482,  491,    0,
    0,   -4,    0,    0,    0,    0,    0,    0,   -1,  -21,
  553,  468,   79,    0,  192,  295,  341,  445,  345, -193,
    0, -171,   19,   -3,    0,
};
final static int YYTABLESIZE=1388;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
   67,   39,   60,   60,   53,   58,  175,  305,  163,    5,
  211,  162,  236,   30,   49,  124,   51,   20,  229,    1,
    2,   71,   74,   68,  263,   84,   36,  162,   49,   36,
   81,   60,   20,   84,   74,   84,   60,   80,   36,  272,
  157,  275,  216,  217,  219,   60,   74,  148,   90,   99,
   93,  115,  230,  227,  125,  288,  306,   84,   60,   60,
   71,  225,  274,  149,  147,  143,  235,  289,  290,  234,
   42,  148,   83,   40,  148,  166,  224,   44,  151,  152,
   54,   55,   56,  139,  233,  130,  142,  131,  135,  148,
   99,   99,  322,  136,   60,   60,   60,   60,   60,  333,
   89,  307,   60,  334,   57,   55,   56,   99,   60,   60,
  267,   45,   60,   60,   60,  236,  238,  239,  183,   36,
   71,   46,  190,  150,   55,   56,   43,   60,   57,  338,
   91,   92,  197,   71,  104,  104,  104,  104,  104,   57,
  104,   74,   74,  192,    6,  212,  214,   57,   75,    8,
    9,  215,  104,  104,   76,  104,  202,  100,  118,  116,
   44,  117,   60,  119,  240,  259,   98,   77,  162,  174,
  221,   78,  141,  141,  141,  141,  141,  269,  141,  270,
   47,   48,  183,  109,   60,   79,   71,  118,   60,   60,
  141,  141,  119,  141,   85,   48,  180,  181,  100,  100,
  260,  323,  252,  162,  234,  182,  253,  255,  189,  249,
  154,  155,   60,  199,  201,  100,   54,   55,   56,   66,
   52,   54,   55,   56,   60,  111,   54,   55,   56,   20,
   20,  123,  183,  286,   20,   20,   20,  156,   82,  262,
   57,   36,   80,   87,  183,   57,   82,  114,   82,   80,
   57,   60,   20,  140,  240,  240,  240,  240,  300,  284,
  240,  240,  240,  240,  285,   60,  295,  296,  297,  298,
  299,  183,  164,  165,  223,   96,  153,   96,   96,   96,
  158,   54,   55,   56,   71,  183,  148,  148,  148,  268,
  159,   60,  162,   96,   96,   74,   96,  335,  161,  169,
  168,   54,   55,   56,  141,   57,  302,  324,  120,  162,
  148,  337,  170,   93,  122,   93,   93,   93,  173,  340,
  341,  342,  343,  344,  345,   57,  347,  348,  349,  121,
   74,   93,   93,  186,   93,   74,   74,   74,   74,   74,
   74,   20,   74,   74,   74,  187,  108,   85,   48,  104,
  104,  104,  203,  104,  104,  130,   20,  131,  104,   49,
  104,  104,  104,  104,  104,  104,  278,  104,  104,  104,
  104,  279,  133,  104,  132,  204,   95,  242,   95,   95,
   95,  278,  276,  206,  277,  210,  279,  141,  141,  141,
   88,  141,  141,  231,   95,   95,  141,   95,  141,  141,
  141,  141,  141,  141,   45,  141,  141,  141,  141,   45,
   92,  141,   92,   92,   92,  213,   54,   55,   56,  222,
   54,   55,   56,  188,   55,   56,  218,   94,   92,   92,
  228,   92,    8,    9,  250,   90,  120,   90,   90,   90,
   57,  246,  122,  251,   57,  266,   94,   57,   54,   55,
   56,    8,    9,   90,   90,  256,   90,  280,  257,  258,
   94,   43,   94,   94,   94,  261,   43,  242,  242,  242,
  242,  265,   57,  242,  242,  242,  242,  291,   94,   94,
  243,   94,  162,  177,   55,   56,  292,   49,   53,  294,
   96,   96,   96,   53,   96,   96,  301,  303,  304,   96,
  162,   96,   96,   96,   96,   96,   96,   57,   96,   96,
   96,   96,  313,  332,   96,  336,  339,   91,   36,   91,
   91,   91,  346,    8,    9,  363,   41,  364,   93,   93,
   93,   45,   93,   93,  365,   91,   91,   93,   91,   93,
   93,   93,   93,   93,   93,  366,   93,   93,   93,   93,
  118,  116,   93,  117,   20,  119,  138,   20,  367,   20,
   20,  368,   20,   20,   20,  369,  370,   20,   20,  371,
  243,  243,  243,  243,  191,   20,  243,  243,  243,  243,
  372,   33,  220,  178,  179,  171,  126,  127,  128,  129,
  287,   95,   95,   95,  172,   95,   95,  194,  196,  181,
   95,  110,   95,   95,   95,   95,   95,   95,   94,   95,
   95,   95,   95,    8,    9,   95,  309,  310,  193,   55,
   56,    0,  315,  317,    0,   92,   92,   92,    0,   92,
   92,  144,    0,  130,   92,  131,   92,   92,   92,   92,
   92,   92,   57,   92,   92,   92,   92,  178,    0,   92,
   90,   90,   90,    0,   90,   90,  176,    0,  130,   90,
  131,   90,   90,   90,   90,   90,   90,  179,   90,   90,
   90,   90,    0,    0,   90,   94,   94,   94,    0,   94,
   94,   41,    0,    0,   94,    0,   94,   94,   94,   94,
   94,   94,  176,   94,   94,   94,   94,  162,  162,   94,
    0,  162,  162,  195,   55,   56,  162,    0,  162,  162,
  162,  162,  162,  162,   79,   95,  101,    0,  198,   55,
   56,    0,  311,  312,    0,    0,    0,   57,  319,  321,
    0,   77,   91,   91,   91,    0,   91,   91,  200,   55,
   56,   91,   57,   91,   91,   91,   91,   91,   91,   78,
   91,   91,   91,   91,    0,    0,   91,  101,  101,  237,
  238,  239,   57,    0,   54,   55,   56,  153,  254,   55,
   56,  137,   38,    0,  101,   70,    0,  205,    0,  130,
   23,  131,   24,   57,  151,   25,   26,    0,   57,  137,
   38,    0,   57,   70,    0,    0,  181,  181,   23,    0,
   24,  181,    0,   25,   26,  181,    0,  181,  181,  181,
  181,  181,  181,  273,   55,   56,  308,  238,  239,  314,
  238,  239,  316,  238,  239,    0,    0,    0,   95,   95,
   95,  248,   95,  130,  281,  131,  282,   57,  283,   95,
   57,    0,    0,   57,  178,  178,   57,    0,    0,  178,
  318,  238,  239,  178,    0,  178,  178,  178,  178,  178,
  178,  320,  238,  239,  179,  179,  247,  118,  116,  179,
  117,    0,  119,  179,   57,  179,  179,  179,  179,  179,
  179,    0,  293,  118,  116,   57,  117,    0,  119,  176,
  176,  128,  128,  128,  176,  128,   95,  128,  176,    0,
  176,  176,  176,  176,  176,  176,  351,    0,  130,    0,
  131,   79,   79,  120,    0,  120,   79,  120,    0,    0,
   79,    0,   79,   79,   79,   79,   79,   79,   77,   77,
  117,    0,  117,   77,  117,    0,    0,   77,    0,   77,
   77,   77,   77,   77,   77,    0,   78,   78,  119,    0,
  119,   78,  119,    0,    0,   78,    0,   78,   78,   78,
   78,   78,   78,    0,  153,  153,  116,    0,  116,  153,
  116,    0,    0,  153,    0,  153,  153,  153,  153,  153,
  153,  151,  151,  114,    0,  114,  151,  114,    0,    0,
  151,    0,  151,  151,  151,  151,  151,  151,   64,    0,
    0,    0,   64,   64,   64,   64,    0,   64,   64,   64,
    0,    0,   64,   64,    0,  118,    0,  118,   13,  118,
   64,   64,   13,   13,   13,   13,    0,   13,   13,   13,
    0,    0,   13,   13,    0,    0,    0,    0,  329,    6,
   13,   13,  330,    0,    8,    9,    0,    0,   10,   21,
    0,    0,   22,    0,    8,    9,    0,   23,   10,   24,
   11,    0,   25,   26,    0,    0,    6,    0,    0,    7,
   11,    8,    9,    6,    0,   10,  264,    0,    8,    9,
    6,    0,   10,  325,    0,    8,    9,   11,    0,   10,
    6,    0,    0,  326,   11,    8,    9,    6,    0,   10,
  327,   11,    8,    9,    6,    0,   10,  328,    0,    8,
    9,   11,    0,   10,    0,   72,   72,  115,   11,  115,
   72,  115,    0,    0,   72,   11,   72,   72,   72,   72,
   72,   72,   73,   73,    0,    0,    0,   73,    0,    0,
    0,   73,    0,   73,   73,   73,   73,   73,   73,   74,
   74,    0,    0,    0,   74,    0,    0,    0,   74,    0,
   74,   74,   74,   74,   74,   74,   75,   75,    0,    0,
    0,   75,    0,    0,    0,   75,    0,   75,   75,   75,
   75,   75,   75,   76,   76,    0,    0,    0,   76,    0,
    0,    0,   76,    0,   76,   76,   76,   76,   76,   76,
   69,   38,    0,    0,   70,    0,    0,  112,   38,   23,
    0,   24,  113,    0,   25,   26,   23,    0,   24,  137,
   38,   25,   26,   70,    0,    0,  208,   38,   23,    0,
   24,  209,    0,   25,   26,   23,    0,   24,  357,   38,
   25,   26,    0,  358,    0,  361,   38,   23,    0,   24,
  362,    0,   25,   26,   23,   38,   24,    0,    0,   25,
   26,    0,    0,   23,    0,   24,    0,   38,   25,   26,
  145,   73,    0,    0,   38,   23,    0,   24,   86,    0,
   25,   26,   23,    0,   24,   38,    0,   25,   26,  207,
    0,    0,   38,   23,    0,   24,  331,    0,   25,   26,
   23,    0,   24,   38,    0,   25,   26,  350,    0,    0,
   38,   23,    0,   24,  352,    0,   25,   26,   23,    0,
   24,   38,    0,   25,   26,  353,    0,    0,   38,   23,
    0,   24,  354,    0,   25,   26,   23,    0,   24,   38,
    0,   25,   26,  355,    0,    0,   38,   23,    0,   24,
  356,    0,   25,   26,   23,    0,   24,   38,    0,   25,
   26,  359,    0,    0,   38,   23,    0,   24,  360,    0,
   25,   26,   23,   38,   24,    0,    0,   25,   26,    0,
    0,   23,    0,   24,    0,    0,   25,   26,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
   40,    7,   24,   25,   40,   40,   93,   44,   41,    2,
   40,   44,  184,   40,   91,   40,   22,   44,   59,  256,
  257,   26,   27,   25,   41,   44,   41,   44,   91,   44,
   61,   53,   59,   44,   39,   44,   58,   61,  257,  233,
   59,  235,  158,  159,  160,   67,   51,  256,   59,   46,
   59,   53,   93,  169,   58,  256,   93,   44,   80,   81,
   65,   44,  234,  272,   70,   67,   41,  268,  269,   44,
   11,   41,   59,  257,   44,   59,   59,   18,   80,   81,
  256,  257,  258,   65,   59,   43,   41,   45,   42,   59,
   87,   88,  286,   47,  116,  117,  118,  119,  120,  256,
   41,  273,  124,  260,  280,  257,  258,  104,  130,  131,
  226,   40,  134,  135,  136,  287,  257,  258,  120,  257,
  125,  260,  124,  256,  257,  258,  264,  149,  280,  323,
  256,  257,  134,  138,   41,   42,   43,   44,   45,  280,
   47,  146,  147,  125,  257,  149,  256,  280,   59,  262,
  263,  261,   59,   60,   59,   62,  138,   46,   42,   43,
  101,   45,  184,   47,  186,   41,  279,   59,   44,  256,
  163,   59,   41,   42,   43,   44,   45,  256,   47,  258,
  257,  258,  184,   93,  206,   59,  191,   42,  210,  211,
   59,   60,   47,   62,  257,  258,  118,  119,   87,   88,
   41,   41,  206,   44,   44,   41,  210,  211,   41,  191,
   87,   88,  234,  135,  136,  104,  256,  257,  258,  259,
  256,  256,  257,  258,  246,   93,  256,  257,  258,  256,
  257,  256,  234,   41,  261,  262,  263,  256,  257,  256,
  280,  256,  273,  256,  246,  280,  257,   59,  257,  273,
  280,  273,  279,   41,  276,  277,  278,  279,  264,   42,
  282,  283,  284,  285,   47,  287,  259,  260,  261,  262,
  263,  273,  256,  257,  257,   41,  257,   43,   44,   45,
   40,  256,  257,  258,  289,  287,  256,  257,  258,   41,
  256,  313,   44,   59,   60,  300,   62,  303,  257,   40,
  257,  256,  257,  258,  259,  280,   41,  289,   40,   44,
  280,  313,  261,   41,   46,   43,   44,   45,   93,  325,
  326,  327,  328,  329,  330,  280,  332,  333,  334,   61,
  335,   59,   60,   40,   62,  340,  341,  342,  343,  344,
  345,   44,  347,  348,  349,  257,  256,  257,  258,  256,
  257,  258,  269,  260,  261,   43,   59,   45,  265,   91,
  267,  268,  269,  270,  271,  272,   42,  274,  275,  276,
  277,   47,   60,  280,   62,   41,   41,  186,   43,   44,
   45,   42,   43,   40,   45,   40,   47,  256,  257,  258,
   93,  260,  261,   91,   59,   60,  265,   62,  267,  268,
  269,  270,  271,  272,  256,  274,  275,  276,  277,  261,
   41,  280,   43,   44,   45,  261,  256,  257,  258,   59,
  256,  257,  258,  256,  257,  258,  256,  257,   59,   60,
   59,   62,  262,  263,  269,   41,   40,   43,   44,   45,
  280,   40,   46,  269,  280,  256,  257,  280,  256,  257,
  258,  262,  263,   59,   60,   41,   62,   61,   59,   59,
   41,  256,   43,   44,   45,   41,  261,  276,  277,  278,
  279,  257,  280,  282,  283,  284,  285,   41,   59,   60,
  186,   62,   41,  256,  257,  258,   41,   91,  256,   41,
  256,  257,  258,  261,  260,  261,   41,  260,   93,  265,
   59,  267,  268,  269,  270,  271,  272,  280,  274,  275,
  276,  277,   40,  260,  280,  258,  269,   41,  257,   43,
   44,   45,   59,  262,  263,   59,  260,   59,  256,  257,
  258,  261,  260,  261,   59,   59,   60,  265,   62,  267,
  268,  269,  270,  271,  272,   59,  274,  275,  276,  277,
   42,   43,  280,   45,  257,   47,   41,  260,   59,  262,
  263,   59,  265,  266,  267,   59,   59,  270,  271,   59,
  276,  277,  278,  279,   41,  278,  282,  283,  284,  285,
   59,   40,  162,  116,  117,  104,  274,  275,  276,  277,
  246,  256,  257,  258,  104,  260,  261,  130,  131,   59,
  265,   49,  267,  268,  269,  270,  271,  272,  257,  274,
  275,  276,  277,  262,  263,  280,  276,  277,  256,  257,
  258,   -1,  282,  283,   -1,  256,  257,  258,   -1,  260,
  261,   41,   -1,   43,  265,   45,  267,  268,  269,  270,
  271,  272,  280,  274,  275,  276,  277,   59,   -1,  280,
  256,  257,  258,   -1,  260,  261,   41,   -1,   43,  265,
   45,  267,  268,  269,  270,  271,  272,   59,  274,  275,
  276,  277,   -1,   -1,  280,  256,  257,  258,   -1,  260,
  261,   11,   -1,   -1,  265,   -1,  267,  268,  269,  270,
  271,  272,   59,  274,  275,  276,  277,  256,  257,  280,
   -1,  260,  261,  256,  257,  258,  265,   -1,  267,  268,
  269,  270,  271,  272,   59,   45,   46,   -1,  256,  257,
  258,   -1,  278,  279,   -1,   -1,   -1,  280,  284,  285,
   -1,   59,  256,  257,  258,   -1,  260,  261,  256,  257,
  258,  265,  280,  267,  268,  269,  270,  271,  272,   59,
  274,  275,  276,  277,   -1,   -1,  280,   87,   88,  256,
  257,  258,  280,   -1,  256,  257,  258,   59,  256,  257,
  258,  256,  257,   -1,  104,  260,   -1,   41,   -1,   43,
  265,   45,  267,  280,   59,  270,  271,   -1,  280,  256,
  257,   -1,  280,  260,   -1,   -1,  256,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,  258,  256,  257,  258,  256,
  257,  258,  256,  257,  258,   -1,   -1,   -1,  158,  159,
  160,   41,  162,   43,   41,   45,   43,  280,   45,  169,
  280,   -1,   -1,  280,  256,  257,  280,   -1,   -1,  261,
  256,  257,  258,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,  258,  256,  257,   41,   42,   43,  261,
   45,   -1,   47,  265,  280,  267,  268,  269,  270,  271,
  272,   -1,   41,   42,   43,  280,   45,   -1,   47,  256,
  257,   41,   42,   43,  261,   45,  226,   47,  265,   -1,
  267,  268,  269,  270,  271,  272,   41,   -1,   43,   -1,
   45,  256,  257,   41,   -1,   43,  261,   45,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,  256,  257,
   41,   -1,   43,  261,   45,   -1,   -1,  265,   -1,  267,
  268,  269,  270,  271,  272,   -1,  256,  257,   41,   -1,
   43,  261,   45,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,  256,  257,   41,   -1,   43,  261,
   45,   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,   41,   -1,   43,  261,   45,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,  256,   -1,
   -1,   -1,  260,  261,  262,  263,   -1,  265,  266,  267,
   -1,   -1,  270,  271,   -1,   41,   -1,   43,  256,   45,
  278,  279,  260,  261,  262,  263,   -1,  265,  266,  267,
   -1,   -1,  270,  271,   -1,   -1,   -1,   -1,  256,  257,
  278,  279,  260,   -1,  262,  263,   -1,   -1,  266,  257,
   -1,   -1,  260,   -1,  262,  263,   -1,  265,  266,  267,
  278,   -1,  270,  271,   -1,   -1,  257,   -1,   -1,  260,
  278,  262,  263,  257,   -1,  266,  260,   -1,  262,  263,
  257,   -1,  266,  260,   -1,  262,  263,  278,   -1,  266,
  257,   -1,   -1,  260,  278,  262,  263,  257,   -1,  266,
  260,  278,  262,  263,  257,   -1,  266,  260,   -1,  262,
  263,  278,   -1,  266,   -1,  256,  257,   41,  278,   43,
  261,   45,   -1,   -1,  265,  278,  267,  268,  269,  270,
  271,  272,  256,  257,   -1,   -1,   -1,  261,   -1,   -1,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,  256,
  257,   -1,   -1,   -1,  261,   -1,   -1,   -1,  265,   -1,
  267,  268,  269,  270,  271,  272,  256,  257,   -1,   -1,
   -1,  261,   -1,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,   -1,   -1,   -1,  261,   -1,
   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   -1,   -1,  260,   -1,   -1,  256,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,  256,
  257,  270,  271,  260,   -1,   -1,  256,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,   -1,  267,  256,  257,
  270,  271,   -1,  261,   -1,  256,  257,  265,   -1,  267,
  261,   -1,  270,  271,  265,  257,  267,   -1,   -1,  270,
  271,   -1,   -1,  265,   -1,  267,   -1,  257,  270,  271,
  272,  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,   -1,  267,  257,   -1,  270,  271,  261,
   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,
  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,
  257,  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,
  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,  257,
   -1,  270,  271,  261,   -1,   -1,  257,  265,   -1,  267,
  261,   -1,  270,  271,  265,   -1,  267,  257,   -1,  270,
  271,  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,  257,  267,   -1,   -1,  270,  271,   -1,
   -1,  265,   -1,  267,   -1,   -1,  270,  271,
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
"encabezado_metodo : tipo ID",
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
"declaracion_metodo : encabezado_metodo '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';'",
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

//#line 573 "gramatica.y"
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
    this.raizAST.setTablaSimbolos(this.tablaSimbolos);
    this.gestorAmbitos = new GestionAmbitoTp3(this.tablaSimbolos);
    this.recolectorErrores = lexer.getRecolectorErroresTp3();
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
        RegistroSimboloTp3 reg = gestorAmbitos.declararVariable(nombre, tipo, getLineaActual(), comptime);
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
    recolectorErrores.agregarError("Semantico",lexer.getLineaActual(),mensaje);
}

private void yyerror(String mensaje){
    recolectorErrores.agregarError("Sintactico",lexer.getLineaActual(),mensaje);
}

private void actualizarComptimeSiCorresponde(NodoAST izquierdo, NodoAST derecho) {
    if (gestorAmbitos.estaSuprimido()) return;
    if (!(izquierdo instanceof NodoHoja)) return;

    NodoHoja hojaIzq = (NodoHoja) izquierdo;

    if (!"ID".equals(hojaIzq.getTipoNodo())) return;

    RegistroSimboloTp3 reg = tablaSimbolos.buscar(hojaIzq.getValor());

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
    java.util.List<RegistroSimboloTp3> modificados = new java.util.ArrayList<>();

    recolectarVariablesComptimeModificadas(nodo, modificados);

    for (RegistroSimboloTp3 registro : modificados) {
        registro.quitarAtributo("VALOR_COMPTIME");
    }
    invalidarReferenciasComptime(nodo, modificados);
}

private void recolectarVariablesComptimeModificadas( NodoAST nodo, java.util.List<RegistroSimboloTp3> modificados) {
    if (nodo == null) {
        return;
    }

    if (nodo instanceof NodoAsignacion) {
        NodoAsignacion asignacion = (NodoAsignacion) nodo;
        NodoAST variable = asignacion.getVariable();

        if (variable instanceof NodoHoja) {
            NodoHoja hoja = (NodoHoja) variable;
            RegistroSimboloTp3 registro = hoja.getSimboloVinculado();
            if (registro != null && registro.esComptime()) {
                boolean existe = false;
                for (RegistroSimboloTp3 anterior : modificados) {
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

private void invalidarReferenciasComptime(NodoAST nodo,java.util.List<RegistroSimboloTp3> modificados) {
    if (nodo == null) {
        return;
    }

    if (nodo instanceof NodoHoja) {
        NodoHoja hoja = (NodoHoja) nodo;
        RegistroSimboloTp3 registro = hoja.getSimboloVinculado();
        if (registro != null) {
            for (RegistroSimboloTp3 modificado : modificados) {
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
    RegistroSimboloTp3 parametro = gestorAmbitos.declararParametro(nombre,tipo, getLineaActual());
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
//#line 1190 "Parser.java"
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
    }
break;
case 2:
//#line 31 "gramatica.y"
{ yyerror("Falta de nombre de programa");  yyval = new ParserVal(null);}
break;
case 3:
//#line 32 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN"); yyval = new ParserVal(null);}
break;
case 4:
//#line 33 "gramatica.y"
{ yyerror("Falta la palabra reservada END"); yyval = new ParserVal(null);}
break;
case 5:
//#line 38 "gramatica.y"
{
        NodoBloque bloque = (NodoBloque) val_peek(1).obj;
        if (val_peek(0).obj != null) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);}
break;
case 6:
//#line 44 "gramatica.y"
{yyval = new ParserVal(new NodoBloque());}
break;
case 12:
//#line 56 "gramatica.y"
{declararListaVariables((List<String>) val_peek(1).obj, val_peek(2).sval, false);}
break;
case 13:
//#line 57 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 14:
//#line 61 "gramatica.y"
{ declararListaVariables((List<String>) val_peek(1).obj, val_peek(2).sval, true);}
break;
case 15:
//#line 62 "gramatica.y"
{ yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
break;
case 16:
//#line 63 "gramatica.y"
{ yyerror("Falta tipo en las variables en COMPTIME");}
break;
case 17:
//#line 67 "gramatica.y"
{ yyval = new ParserVal("USHORTINT");}
break;
case 18:
//#line 68 "gramatica.y"
{ yyval = new ParserVal("DOUBLEF");}
break;
case 19:
//#line 72 "gramatica.y"
{
        List<String> lista = (List<String>) val_peek(2).obj;
        lista.add(val_peek(0).sval);
        yyval = val_peek(2);
    }
break;
case 20:
//#line 77 "gramatica.y"
{  List<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
    }
break;
case 21:
//#line 81 "gramatica.y"
{ yyerror("Lista de variables: Falta la ','"); yyval = new ParserVal(null);}
break;
case 22:
//#line 85 "gramatica.y"
{
        String ambitoFuncion = getAmbitoActual();
        boolean valida = (val_peek(8).obj instanceof RegistroSimboloTp3);;
        gestorAmbitos.salirAmbito();  
        if (!valida) {
            gestorAmbitos.finalizarSupresion(); 
        }
        yyval = valida ? new ParserVal(new NodoFuncion(val_peek(8).sval, ambitoFuncion, (NodoAST) val_peek(6).obj, (NodoAST) val_peek(4).obj, (NodoAST) val_peek(2).obj)) : new ParserVal(null);
    }
break;
case 23:
//#line 95 "gramatica.y"
{ yyerror("Falta parentesis de apertura en el encabezado de la función"); }
break;
case 24:
//#line 96 "gramatica.y"
{ yyerror("Falta parentesis de cierre en el encabezado de la función"); }
break;
case 25:
//#line 97 "gramatica.y"
{ yyerror("Falta de nombre de funcion");}
break;
case 26:
//#line 98 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN de la funcion");}
break;
case 27:
//#line 99 "gramatica.y"
{ yyerror("Falta la palabra reservada END de la funcion");}
break;
case 28:
//#line 100 "gramatica.y"
{ yyerror("Falta el ';'");}
break;
case 29:
//#line 101 "gramatica.y"
{ yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
break;
case 30:
//#line 105 "gramatica.y"
{
        RegistroSimboloTp3 metodo = gestorAmbitos.declararMetodo(val_peek(0).sval, val_peek(1).sval, getLineaActual());
        if (metodo == null) {
            errorSemantico("Método ya declarado en el ámbito actual: " + val_peek(0).sval);
            gestorAmbitos.iniciarSupresion();
        }
        gestorAmbitos.entrarAmbitoFuncion(val_peek(0).sval);
        ParserVal valor = new ParserVal(val_peek(0).sval);
        valor.obj = metodo;
        yyval = valor;
    }
break;
case 31:
//#line 119 "gramatica.y"
{
        NodoBloque lista = (NodoBloque) val_peek(2).obj;
        if (val_peek(0).obj != null) {
            lista.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(lista);
    }
break;
case 32:
//#line 126 "gramatica.y"
{
        NodoBloque lista = new NodoBloque();
        if (val_peek(0).obj != null) {
            lista.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(lista);
    }
break;
case 33:
//#line 136 "gramatica.y"
{
        RegistroSimboloTp3 funcion = gestorAmbitos.declararFuncion(val_peek(0).sval, val_peek(2).sval, lexer.getLineaActual());
        if (funcion == null) {
            errorSemantico("Función ya declarada en el ámbito actual: " + val_peek(0).sval);
            gestorAmbitos.iniciarSupresion();
        }
        gestorAmbitos.entrarAmbitoFuncion(val_peek(0).sval);
        ParserVal valor = new ParserVal(val_peek(0).sval);
        valor.obj = funcion;    
        yyval = valor;
    }
break;
case 34:
//#line 150 "gramatica.y"
{ 
        String nombre = val_peek(0).sval;
        String tipoParametro = val_peek(1).sval;
        registrarParametroFormal(nombre, tipoParametro);
        yyval = new ParserVal(new NodoHoja("PARAMETRO", tipoParametro + " " + nombre, getLineaActual()));
    }
break;
case 35:
//#line 156 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'tipo'"); yyval = new ParserVal(null);}
break;
case 36:
//#line 157 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'ID''"); yyval = new ParserVal(null);}
break;
case 37:
//#line 161 "gramatica.y"
{
        boolean valida = (val_peek(4).obj instanceof RegistroSimboloTp3);
        String ambitoClase = getAmbitoActual();  
        if (!pilaClases.isEmpty()) {
            pilaClases.pop();
        }
        gestorAmbitos.salirAmbito();
        if (!valida) {
            gestorAmbitos.finalizarSupresion();
        }
        yyval = valida ? new ParserVal(new NodoClase(val_peek(4).sval, ambitoClase, (NodoAST) val_peek(2).obj)) : new ParserVal(null);
    }
break;
case 38:
//#line 173 "gramatica.y"
{ yyerror ("Falta el BEGIN de clase"); yyval = new ParserVal(null);}
break;
case 39:
//#line 174 "gramatica.y"
{ yyerror("Falta el END de clase"); yyval = new ParserVal(null);}
break;
case 40:
//#line 175 "gramatica.y"
{ yyerror("Falta el ';' al final de la clase"); yyval = new ParserVal(null);}
break;
case 41:
//#line 179 "gramatica.y"
{
        RegistroSimboloTp3 clase = gestorAmbitos.declararClase(val_peek(0).sval, getLineaActual());

        if (clase == null) {
            errorSemantico("Clase ya declarada en el ámbito actual: " + val_peek(0).sval);
            gestorAmbitos.iniciarSupresion();
        }
        pilaClases.push(val_peek(0).sval);
        gestorAmbitos.entrarAmbitoClase(val_peek(0).sval);
        ParserVal valor = new ParserVal(val_peek(0).sval);
        valor.obj = clase;
        yyval = valor;
    }
break;
case 42:
//#line 195 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 43:
//#line 196 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 44:
//#line 197 "gramatica.y"
{ yyval = new ParserVal(new NodoBloque()); }
break;
case 45:
//#line 198 "gramatica.y"
{ yyval = new ParserVal(new NodoBloque());}
break;
case 46:
//#line 202 "gramatica.y"
{
        NodoBloque bloque = (NodoBloque) val_peek(1).obj;
        if (val_peek(0).obj instanceof NodoAST) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);
    }
break;
case 47:
//#line 209 "gramatica.y"
{
        NodoBloque bloque = new NodoBloque();
        if (val_peek(0).obj instanceof NodoAST) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);
    }
break;
case 48:
//#line 219 "gramatica.y"
{ yyval = new ParserVal(null);}
break;
case 49:
//#line 220 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 50:
//#line 221 "gramatica.y"
{  yyval = new ParserVal(null);}
break;
case 51:
//#line 225 "gramatica.y"
{registrarClasesBase((List<String>) val_peek(1).obj);}
break;
case 52:
//#line 226 "gramatica.y"
{ yyerror("Sintaxis de lista de herencia invalida"); }
break;
case 53:
//#line 227 "gramatica.y"
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 54:
//#line 228 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 55:
//#line 232 "gramatica.y"
{
        List<String> lista = (List<String>) val_peek(2).obj;
        lista.add(val_peek(0).sval);
        yyval = val_peek(2);
    }
break;
case 56:
//#line 237 "gramatica.y"
{
        List<String> lista = new ArrayList<>();
        lista.add(val_peek(0).sval);
        yyval = new ParserVal(lista);
    }
break;
case 57:
//#line 242 "gramatica.y"
{ yyerror("Lista de IDs: Falta la ','");yyval = new ParserVal(null);}
break;
case 58:
//#line 247 "gramatica.y"
{
        String nombreMetodo = val_peek(7).sval;
        String ambitoMetodo = getAmbitoActual();
        boolean valida = (val_peek(7).obj instanceof RegistroSimboloTp3);
        gestorAmbitos.salirAmbito();
        if (!valida) {
            gestorAmbitos.finalizarSupresion();
        }
        yyval = valida ? new ParserVal( new NodoMetodo(nombreMetodo,ambitoMetodo,(NodoAST)val_peek(5).obj,(NodoAST)val_peek(2).obj)): new ParserVal(null);
    }
break;
case 59:
//#line 257 "gramatica.y"
{yyerror("Falta el BEGIN en la declaracion del metodo");}
break;
case 60:
//#line 258 "gramatica.y"
{yyerror("Falta una ',' o tipo invalido en los parametros del metodo");}
break;
case 61:
//#line 259 "gramatica.y"
{yyerror("Falta el END en la declaracion del metodo");}
break;
case 62:
//#line 260 "gramatica.y"
{yyerror("Falta el ';' en la declaracion del metodo");}
break;
case 63:
//#line 265 "gramatica.y"
{
        List<String> nombres = (List<String>) val_peek(1).obj;
        String tipoClase = val_peek(2).sval;
        for (String nombre : nombres) {
            RegistroSimboloTp3 obj = gestorAmbitos.declararObjeto(nombre, tipoClase, getLineaActual());
            if (obj == null) {
                errorSemantico("No se pudo declarar el objeto '" + nombre + "': clase no visible o identificador duplicado");
            }
        }
        yyval = new ParserVal(null);
    }
break;
case 64:
//#line 276 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'"); yyval = new ParserVal(null);}
break;
case 65:
//#line 281 "gramatica.y"
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
case 66:
//#line 293 "gramatica.y"
{
        NodoBloque bloque = new NodoBloque();
        if (val_peek(0).obj != null) {
            bloque.agregarSentencia((NodoAST) val_peek(0).obj);
        }
        yyval = new ParserVal(bloque);
    }
break;
case 67:
//#line 303 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 68:
//#line 304 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 69:
//#line 305 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 70:
//#line 306 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 71:
//#line 307 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 72:
//#line 308 "gramatica.y"
{ yyerror("Asignacion: Falta el ';', o un operador o expresion");yyval = new ParserVal(null);}
break;
case 73:
//#line 309 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");yyval = new ParserVal(null);}
break;
case 74:
//#line 310 "gramatica.y"
{ yyerror("Salida: Falta el ';'");yyval = new ParserVal(null);}
break;
case 75:
//#line 311 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");yyval = new ParserVal(null);}
break;
case 76:
//#line 312 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");yyval = new ParserVal(null);}
break;
case 77:
//#line 316 "gramatica.y"
{
        if (!expresionInvalida && val_peek(2).obj != null && val_peek(0).obj != null) {
            NodoAST izquierdo = (NodoAST) val_peek(2).obj;
            NodoAST derecho = (NodoAST) val_peek(0).obj;
            actualizarComptimeSiCorresponde(izquierdo, derecho);
            yyval = new ParserVal(new NodoAsignacion(izquierdo, derecho));
        } else {
            expresionInvalida = false;
            yyval = new ParserVal(null);
        }
    }
break;
case 78:
//#line 327 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); yyval = new ParserVal(null); }
break;
case 79:
//#line 328 "gramatica.y"
{ yyerror("Falta expresion en la asignacion"); yyval = new ParserVal(null);}
break;
case 80:
//#line 332 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 81:
//#line 333 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 82:
//#line 334 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de apertura "); yyval = new ParserVal(null);}
break;
case 83:
//#line 338 "gramatica.y"
{ yyval = new ParserVal(new NodoAccesoArreglo(new NodoHoja("ID", val_peek(3).sval, getLineaActual()), (NodoAST)val_peek(1).obj)); }
break;
case 84:
//#line 339 "gramatica.y"
{ yyerror("Acceso posicional: Índice vacío"); yyval = new ParserVal(null);}
break;
case 85:
//#line 340 "gramatica.y"
{ yyerror("Acceso posicional: Índice inválido"); yyval = new ParserVal(null);}
break;
case 86:
//#line 341 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de cierre"); yyval = new ParserVal(null);}
break;
case 87:
//#line 342 "gramatica.y"
{ yyerror("Acceso posicional: Argumento invalido"); yyval = new ParserVal(null);}
break;
case 88:
//#line 346 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 89:
//#line 347 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval, getLineaActual())); }
break;
case 90:
//#line 351 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("+", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 91:
//#line 352 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("-", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 92:
//#line 353 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 93:
//#line 354 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 94:
//#line 355 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 95:
//#line 356 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 96:
//#line 357 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 97:
//#line 361 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("*", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 98:
//#line 362 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("/", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 99:
//#line 363 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 100:
//#line 364 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 101:
//#line 365 "gramatica.y"
{yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 102:
//#line 366 "gramatica.y"
{yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 103:
//#line 367 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 104:
//#line 371 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 105:
//#line 372 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval, getLineaActual())); }
break;
case 106:
//#line 373 "gramatica.y"
{yyval = val_peek(0);}
break;
case 107:
//#line 374 "gramatica.y"
{
        if (val_peek(1).obj != null) {
            yyval = new ParserVal(new NodoAsignacion(new NodoHoja("ID", val_peek(4).sval, getLineaActual()),(NodoAST)val_peek(1).obj));
        } else {
            yyval = new ParserVal(null);
        }
    }
break;
case 108:
//#line 381 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 109:
//#line 382 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 110:
//#line 386 "gramatica.y"
{
        if (val_peek(1).obj != null) {
            yyval = new ParserVal(new NodoUnario("TODF", (NodoAST)val_peek(1).obj));
        } else {
            yyval = new ParserVal(null);
        }
    }
break;
case 111:
//#line 393 "gramatica.y"
{ yyerror("Solo se pueden expresiones"); yyval = new ParserVal(null);}
break;
case 112:
//#line 394 "gramatica.y"
{ yyerror("Falta expresion"); yyval = new ParserVal(null);}
break;
case 113:
//#line 395 "gramatica.y"
{ yyerror("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 114:
//#line 399 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("+", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 115:
//#line 400 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("-", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 116:
//#line 401 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 117:
//#line 402 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 118:
//#line 403 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 119:
//#line 404 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 120:
//#line 405 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 121:
//#line 409 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("*", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 122:
//#line 410 "gramatica.y"
{ yyval = new ParserVal(new NodoBinario("/", (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj)); }
break;
case 123:
//#line 411 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 124:
//#line 412 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 125:
//#line 413 "gramatica.y"
{ yyerror("Falta operando 1"); yyval = new ParserVal(null);}
break;
case 126:
//#line 414 "gramatica.y"
{ yyerror("Falta operando 2"); yyval = new ParserVal(null);}
break;
case 127:
//#line 415 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 128:
//#line 419 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("ID", val_peek(0).sval, getLineaActual())); }
break;
case 129:
//#line 420 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("CTE", val_peek(0).sval, getLineaActual())); }
break;
case 130:
//#line 421 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 131:
//#line 422 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 132:
//#line 423 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 133:
//#line 424 "gramatica.y"
{ yyerror("No se permite este anidamiento, solo 1 expresion"); yyval = new ParserVal(null);}
break;
case 134:
//#line 428 "gramatica.y"
{
        yyval = new ParserVal(new NodoInvocacion(val_peek(4).sval, (NodoListaParametros)val_peek(2).obj,(List<Integer>)val_peek(0).obj,getLineaActual(),tablaSimbolos.resolverFuncion(val_peek(4).sval)));
    }
break;
case 135:
//#line 431 "gramatica.y"
{
        yyval = new ParserVal(new NodoInvocacionMetodo( new NodoHoja("ID", val_peek(6).sval, getLineaActual()), val_peek(4).sval, (NodoListaParametros)val_peek(2).obj, (List<Integer>)val_peek(0).obj, getLineaActual() ));
    }
break;
case 136:
//#line 434 "gramatica.y"
{ yyerror("Error en los parametros: Falta un ')' "); yyval = new ParserVal(null);}
break;
case 137:
//#line 435 "gramatica.y"
{ yyerror("Lista de parametros vacia."); yyval = new ParserVal(null);}
break;
case 138:
//#line 436 "gramatica.y"
{ yyerror("Lista de parametros vacia."); expresionInvalida = true; yyval = new ParserVal(null);}
break;
case 139:
//#line 440 "gramatica.y"
{yyval = val_peek(1);}
break;
case 140:
//#line 441 "gramatica.y"
{ yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); yyval = new ParserVal(null);}
break;
case 141:
//#line 442 "gramatica.y"
{ yyval = new ParserVal(null);}
break;
case 142:
//#line 446 "gramatica.y"
{
        List<Integer> orden = (List<Integer>)val_peek(2).obj;
        Integer posicion =  convertirPosicionParametro(val_peek(0).sval);
        if (posicion == null) {
            errorSemantico( "El orden de parámetros solo admite constantes enteras positivas.");
        }
        orden.add(posicion);
        yyval = new ParserVal(orden);
    }
break;
case 143:
//#line 455 "gramatica.y"
{
        List<Integer> orden = new ArrayList<>();
        Integer posicion =  convertirPosicionParametro(val_peek(0).sval);
        if (posicion == null) {
            errorSemantico( "El orden de parámetros solo admite constantes enteras positivas." );
        }
        orden.add(posicion);
        yyval = new ParserVal(orden);
    }
break;
case 144:
//#line 467 "gramatica.y"
{
        NodoListaParametros lista = (NodoListaParametros)val_peek(2).obj;
        lista.agregarParametro((NodoAST)val_peek(0).obj);
        yyval = new ParserVal(lista);
    }
break;
case 145:
//#line 472 "gramatica.y"
{
        NodoListaParametros lista = new NodoListaParametros();
        lista.agregarParametro((NodoAST)val_peek(0).obj);
        yyval = new ParserVal(lista); 
    }
break;
case 146:
//#line 477 "gramatica.y"
{yyerror("Falta ',' entre argumentos"); yyval = new ParserVal(null);}
break;
case 147:
//#line 478 "gramatica.y"
{yyerror("Elemento no valido despues de ','"); yyval = new ParserVal(null);}
break;
case 148:
//#line 482 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 149:
//#line 486 "gramatica.y"
{
        NodoRepeat repeticion = new NodoRepeat((NodoAST)val_peek(4).obj,(NodoAST)val_peek(1).obj);
        invalidarComptimesModificadosEnControl(repeticion);
        yyval = new ParserVal(repeticion);
    }
break;
case 150:
//#line 491 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 151:
//#line 492 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 152:
//#line 493 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 153:
//#line 494 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa"); yyval = new ParserVal(null);}
break;
case 154:
//#line 495 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 155:
//#line 496 "gramatica.y"
{ yyerror("error en condicion de iteracion"); yyval = new ParserVal(null);}
break;
case 156:
//#line 500 "gramatica.y"
{
        NodoIF seleccion = new NodoIF((NodoAST)val_peek(5).obj,(NodoAST)val_peek(3).obj,(NodoAST)val_peek(1).obj);
        invalidarComptimesModificadosEnControl(seleccion);
        yyval = new ParserVal(seleccion);
    }
break;
case 157:
//#line 505 "gramatica.y"
{
        NodoIF seleccion = new NodoIF((NodoAST)val_peek(3).obj,(NodoAST)val_peek(1).obj,null);
        invalidarComptimesModificadosEnControl(seleccion);
        yyval = new ParserVal(seleccion);
    }
break;
case 158:
//#line 510 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); yyval = new ParserVal(null);}
break;
case 159:
//#line 511 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion"); yyval = new ParserVal(null);}
break;
case 160:
//#line 512 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); yyval = new ParserVal(null);}
break;
case 161:
//#line 513 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion"); yyval = new ParserVal(null);}
break;
case 162:
//#line 517 "gramatica.y"
{
        NodoAST comp = (NodoAST) val_peek(1).obj;
        yyval = new ParserVal(new NodoBinario(comp.getEtiqueta(), (NodoAST)val_peek(2).obj, (NodoAST)val_peek(0).obj));
    }
break;
case 163:
//#line 524 "gramatica.y"
{yyval = new ParserVal(new NodoHoja("==", "==", getLineaActual()));}
break;
case 164:
//#line 525 "gramatica.y"
{ yyval = new ParserVal(new NodoHoja("!=", "!=", getLineaActual()));}
break;
case 165:
//#line 526 "gramatica.y"
{yyval = new ParserVal(new NodoHoja(">=", ">=", getLineaActual()));}
break;
case 166:
//#line 527 "gramatica.y"
{yyval = new ParserVal(new NodoHoja("<=", "<=", getLineaActual()));}
break;
case 167:
//#line 528 "gramatica.y"
{yyval = new ParserVal(new NodoHoja(">", ">", getLineaActual()));}
break;
case 168:
//#line 529 "gramatica.y"
{yyval = new ParserVal(new NodoHoja("<", "<", getLineaActual()));}
break;
case 169:
//#line 533 "gramatica.y"
{yyval = val_peek(0);}
break;
case 170:
//#line 534 "gramatica.y"
{yyval = val_peek(1);}
break;
case 171:
//#line 535 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el BEGIN "); yyval = new ParserVal(null);}
break;
case 172:
//#line 536 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el END "); yyval = new ParserVal(null);}
break;
case 173:
//#line 540 "gramatica.y"
{yyval = new ParserVal(new NodoPrint(new NodoHoja("CADENA", val_peek(1).sval, getLineaActual())));}
break;
case 174:
//#line 541 "gramatica.y"
{yyval = new ParserVal(new NodoPrint((NodoAST)val_peek(1).obj));}
break;
case 175:
//#line 542 "gramatica.y"
{yyerror("Falta Argumento en POUT"); yyval = new ParserVal(null);}
break;
case 176:
//#line 543 "gramatica.y"
{yyerror ("Falta parentesis de cierre"); yyval = new ParserVal(null);}
break;
case 177:
//#line 544 "gramatica.y"
{yyerror ("Falta parentesis de apertura"); yyval = new ParserVal(null);}
break;
case 178:
//#line 545 "gramatica.y"
{yyerror ("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 179:
//#line 546 "gramatica.y"
{yyerror ("Falta parentesis de cierre"); yyval = new ParserVal(null);}
break;
case 180:
//#line 547 "gramatica.y"
{yyerror ("Falta parentesis de apertura"); yyval = new ParserVal(null);}
break;
case 181:
//#line 548 "gramatica.y"
{yyerror ("Falta parentesis"); yyval = new ParserVal(null);}
break;
case 182:
//#line 552 "gramatica.y"
{
        String ambito = getAmbitoActual();
        String tipoRetorno = null;
        int separador = ambito.lastIndexOf(':');
        if (separador >= 0) {
            String nombre = ambito.substring(separador + 1);
            RegistroSimboloTp3 registro;
            if (!pilaClases.isEmpty()) {
                registro = tablaSimbolos.resolverMiembroClase(pilaClases.peek(),nombre);
            } else {
                registro = tablaSimbolos.resolverFuncion(nombre);
            }
            if (registro != null) {
                tipoRetorno = registro.getTipo();
            }
        }
        yyval = new ParserVal(new NodoReturn((NodoAST)val_peek(1).obj,tipoRetorno,getLineaActual()));
    }
break;
case 183:
//#line 570 "gramatica.y"
{yyerror("Retorno debe tener una expresion y parentesis"); yyval = new ParserVal(null);}
break;
//#line 2273 "Parser.java"
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
