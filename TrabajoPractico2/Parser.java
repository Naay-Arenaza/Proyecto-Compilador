package TrabajoPractico2;
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
//#line 19 "Parser.java"




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
   13,   13,   14,   14,   16,   16,   15,   15,   15,   15,
   18,   18,   18,   17,   17,   17,   17,   17,    8,    8,
    2,    2,   19,   19,   19,   19,   19,   19,   19,   19,
   19,   19,   20,   20,   20,   25,   25,   25,   27,   27,
   27,   27,   27,   28,   28,   26,   26,   26,   26,   26,
   26,   26,   29,   29,   29,   29,   29,   29,   29,   30,
   30,   30,   30,   30,   30,   32,   32,   32,   32,   31,
   31,   31,   31,   31,   31,   31,   34,   34,   34,   34,
   34,   34,   34,   35,   35,   35,   35,   35,   35,   33,
   33,   33,   33,   33,   37,   37,   37,   38,   38,   36,
   36,   36,   36,   39,   24,   24,   24,   24,   24,   24,
   24,   21,   21,   21,   21,   21,   21,   41,   42,   42,
   42,   42,   42,   42,   40,   40,   40,   40,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   23,   23,
};
final static short yylen[] = {                            2,
    5,    5,    4,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    2,
    1,   11,   11,   11,   11,   11,   11,   10,   11,    3,
    1,    2,    1,    1,    6,    6,    6,    5,    2,    1,
    1,    0,    2,    1,    1,    1,    3,    3,    2,    2,
    3,    1,    2,    9,    9,    9,    9,    8,    3,    2,
    2,    1,    2,    2,    2,    2,    2,    1,    1,    1,
    1,    1,    3,    3,    3,    1,    1,    3,    4,    3,
    4,    5,    5,    1,    1,    3,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    3,    3,    1,    1,
    1,    1,    5,    1,    1,    4,    4,    3,    2,    3,
    3,    3,    3,    3,    3,    1,    3,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    5,    5,
    7,    5,    4,    6,    3,    3,    0,    3,    1,    3,
    1,    2,    4,    1,    6,    6,    5,    5,    4,    6,
    6,    8,    6,    5,    5,    4,    6,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    3,    4,    4,
    3,    3,    3,    2,    3,    3,    2,    4,    3,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
    0,    0,    0,    0,    0,   62,    0,    0,    0,    0,
    0,    0,   77,   21,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   85,    0,    0,    0,    0,    0,    0,
    0,  101,    0,    0,    0,  102,    0,   99,  104,  105,
    0,    0,    0,    0,    0,    0,  165,    0,    3,   61,
   63,   64,   65,   66,   67,    0,    0,   20,   59,    0,
   84,    2,    0,    0,    0,   16,    0,    0,   12,    0,
   80,    0,   78,    4,    1,  179,    0,    0,    0,    0,
    0,    0,    0,    0,  109,    0,    0,  159,  160,  161,
  162,    0,    0,  163,  164,    0,    0,    0,    0,    0,
    0,  176,    0,  171,    0,  173,    0,    0,    0,    0,
    0,    0,    0,    0,   19,    0,   45,    0,    0,    0,
   41,   44,   46,    0,   15,   14,    0,    0,    0,   81,
    0,   79,  178,    0,    0,    0,   96,   97,    0,    0,
    0,  141,    0,    0,    0,  108,    0,    0,    0,    0,
    0,    0,    0,    0,   95,   93,   98,   94,    0,  156,
  169,  170,    0,  167,  168,  166,    0,    0,    0,    0,
   52,   50,    0,    0,    0,   39,   43,    0,    0,   33,
    0,    0,   31,    0,    0,    0,   82,   83,    0,  133,
    0,    0,    0,  142,    0,    0,  125,  126,    0,  127,
  128,    0,  123,    0,  107,  106,    0,  154,  155,    0,
    0,    0,    0,  148,   48,   53,   47,    0,    0,   36,
   37,   35,   32,    0,    6,    6,    6,    6,    6,    0,
  139,    0,  132,    0,  140,  130,    0,    0,    0,    0,
    0,  103,    0,    0,    0,    0,    0,    0,  157,    0,
  153,  150,  146,  151,  145,   51,    0,    0,   30,    0,
    0,    0,    0,    0,  136,    0,  135,  143,    0,    0,
    0,  120,  121,    0,    0,    0,    0,    0,  119,  117,
  122,  118,  134,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  138,    0,  131,  152,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  129,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   25,   23,   29,   24,   26,   27,   22,   56,   55,
   57,   54,
};
final static short yydgoto[] = {                          3,
    4,  128,   12,   13,   14,   15,   16,   17,   18,   35,
  202,  203,  139,  140,  141,  142,  143,  193,   26,   27,
   28,   29,   30,   31,   32,   55,   33,   46,   57,   58,
  219,   59,   60,  222,  223,  161,  210,  252,  162,   68,
   61,  116,
};
final static short yysindex[] = {                        73,
    0,    0,    0,  805,  788, -225,  359,    0,    0, -215,
  -59,    0,    0,    0,    0,    0,    0, -150,  -81,  359,
  -39,  -31,  -37,  689,  992,    0,  -35,   15,   51,  107,
  114,  -55,    0,    0,   10,  -72,  999, -108, -225,   12,
  172,   78,    0,    0,  -67,  100,  933,  116, -207,  320,
  448,    0,  -33, -207,   25,    0,  102,    0,    0,    0,
  350,  164,  174,  430,  980,  359,    0, -167,    0,    0,
    0,    0,    0,    0,    0, -179, -207,    0,    0,  -28,
    0,    0,   19,   19,   71,    0,  191,  -26,    0,  145,
    0,  -64,    0,    0,    0,    0,  596,  285,  285, -220,
 -220,  194,  217,   29,    0,  221,  471,    0,    0,    0,
    0,  342,  513,    0,    0, -207,  518,  575,  359,  945,
   54,    0,  297,    0,  646,    0,  324, 1010,  952,  334,
  -24,  320,   86,   86,    0,  -48,    0,  128,  129,   19,
    0,    0,    0, -233,    0,    0,  185,  185,  201,    0,
  -29,    0,    0,  165,  102,  102,    0,    0,  305,   86,
   31,    0,  586,  370,  712,    0,  737,  945,  146,  165,
  102,  165,  102,   86,    0,    0,    0,    0,  155,    0,
    0,    0, -207,    0,    0,    0, -207,  591,  395,  381,
    0,    0,   82,  406,  396,    0,    0,  402,  408,    0,
  208,   68,    0,  412,  431,  -19,    0,    0, -165,    0,
  305,  645,  305,    0,  752,  517,    0,    0,  771,    0,
    0,  243,    0,  224,    0,    0,  -68,    0,    0,  449,
  455,  842,  458,    0,    0,    0,    0,  232,  719,    0,
    0,    0,    0,  185,    0,    0,    0,    0,    0,  398,
    0,  -17,    0,  450,    0,    0,  658,  658, -118, -118,
  460,    0,  661,  675,  678,  686,  305,  143,    0,  945,
    0,    0,    0,    0,    0,    0,  461,  442,    0,  812,
  819,  829,  836,  778,    0,  245,    0,    0,  307,  243,
  243,    0,    0, -207,  307,  243,  307,  243,    0,    0,
    0,    0,    0,  305,  247,  254,  -77,  359,  359,  359,
  359,  359,  359,    0,  824,    0,    0,  359,  359,  359,
 1017, 1028, 1035, 1046, 1053,  964,    0, 1064, 1071,  971,
  464,  472,  473,  477,  479,  481,  485,  486,  487,  491,
  493,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -49,    0,
    0,    0,    0,    0,    0,    0,  848,  865,  882,  899,
  916,    0,    0,    0,  761,  -49,    0,    0,    0,    0,
    0,  707,  454,    0,    0,    0,    0,    0,    0,    0,
   76,    0,    0,    0,    0,    0,    3,    0,    0,    0,
    0,  478,    0,  495,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  292,  -45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  516,    0,  535,    0,    0,    0,    0,    0,
    0,  552,  569,  589,    0,    0,    0,    0,    0,   36,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  137,  250,    0,    0,  112,  169,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  316,
  375,  400,  425,  299,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  607,    0,
    0,    0,  110,  272,    0,    0,    0,    0,  727,    0,
   11,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  112,    0,  112,    0,    0, 1082,    0,    0,    0,    0,
    0,  907,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  625,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  112,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, 1065,
 1099,    0,    0,    0, 1116, 1133, 1150, 1203,    0,    0,
    0,    0,    0,  112,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  744,    0,    0,    0,
 -129,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    6,   -5,    0,  -43,    0,    0,    0,    0,  465,  584,
 -131,  314,  496,    0,  421,  422,    0,    0,   -4,    0,
    0,    0,    0,    0,    0,  -10,  -18,  534,   48,  321,
    0,  647,  664,  755,  777,  358, -109,    0, -126,  235,
  -20,    0,
};
final static int YYTABLESIZE=1342;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
   49,   37,   63,   56,   56,   77,  106,    5,   54,   45,
  192,   76,   64,  149,   47,  188,  204,  206,   45,   67,
   70,  249,  198,   71,  244,   91,  286,  199,  152,  207,
   56,   34,   70,  107,  214,   56,   51,   52,   97,  137,
  137,   38,   70,   92,   56,   92,   92,   92,   50,   51,
   52,   34,  125,   80,   34,   80,   67,   56,   56,   53,
  129,   92,   92,  208,   92,  133,  134,  112,   79,  113,
   86,  213,   53,   72,  212,  287,  132,   51,   52,   56,
   56,   56,   56,   56,  115,  255,  114,   56,  130,  211,
  250,  160,  251,   56,   56,  167,  137,   56,   56,   56,
   53,  253,   67,  256,  131,  174,   34,  278,  245,   73,
  189,  244,   56,   41,   80,   67,  100,  100,  100,  100,
  100,   80,  100,   70,   70,  238,   58,  288,  112,  146,
  113,   58,   58,   58,  100,  100,   89,  100,  216,  217,
  237,  214,   56,  117,  218,  155,  156,   83,  118,   58,
  160,   84,  137,  137,  137,  137,  137,  303,  137,  171,
  173,   53,  230,   67,   56,   74,  231,  233,   56,   56,
  137,  137,   75,  137,   96,   43,   44,   89,  319,   89,
   89,   89,  320,  304,   81,   44,  212,  269,   90,   81,
   44,  151,   93,   56,  316,   89,   89,   34,   89,  270,
  271,  160,    8,    9,  122,   56,  100,  190,  191,  144,
   42,  101,  144,  160,  124,   42,   48,   76,   50,   51,
   52,   62,  105,   76,   50,   51,   52,  144,  135,  148,
  147,   50,   51,   52,  159,   56,  248,  150,  218,  218,
  218,  218,   53,  160,  218,  218,  218,  218,   53,   56,
  280,  281,  282,  283,  284,   53,  163,  160,   92,   92,
   92,  166,   92,   92,  267,   67,   34,   92,   78,   92,
   92,   92,   92,   92,   92,   56,   92,   92,   92,   92,
    8,    9,   92,  315,  265,  164,   50,   51,   52,  266,
   91,   40,   91,   91,   91,  121,   40,  136,  108,  109,
  110,  111,  321,  322,  323,  324,  325,  326,   91,   91,
   53,   91,  328,  329,  330,   21,   70,   70,   70,   70,
   70,   70,  180,   70,   70,   70,  145,   78,    1,    2,
   21,  100,  100,  100,   78,  100,  100,  181,  236,  158,
  100,  169,  100,  100,  100,  100,  100,  100,  259,  100,
  100,  100,  100,  260,  179,  100,   88,  158,   88,   88,
   88,  100,   98,  183,   99,   49,  101,  137,  137,  137,
   49,  137,  137,  187,   88,   88,  137,   88,  137,  137,
  137,  137,  137,  137,  194,  137,  137,  137,  137,  195,
  120,  137,   89,   89,   89,  209,   89,   89,   50,   51,
   52,   89,  227,   89,   89,   89,   89,   89,   89,  224,
   89,   89,   89,   89,  228,   86,   89,   86,   86,   86,
  157,  158,   53,  229,  144,  144,  144,   87,   88,   50,
   51,   52,  123,   86,   86,  234,   86,  176,  178,  235,
   90,  200,   90,   90,   90,  239,    8,    9,  144,   50,
   51,   52,  246,   53,  240,  244,  205,  200,   90,   90,
  241,   90,    8,    9,  243,   87,  242,   87,   87,   87,
  126,  247,  112,   53,  113,   39,  165,   51,   52,   50,
   51,   52,  307,   87,   87,  244,   87,  102,  276,  272,
  285,  100,   98,  104,   99,  273,  101,   21,  275,  294,
   53,  306,  314,   53,  305,   91,   91,   91,  103,   91,
   91,  168,   21,  318,   91,  317,   91,   91,   91,   91,
   91,   91,  342,   91,   91,   91,   91,   21,   21,   91,
  343,  344,   21,   21,   21,  345,  177,  346,   45,  347,
  154,   51,   52,  348,  349,  350,   84,  138,  138,  351,
   21,  352,   42,  174,  158,  158,  102,  279,  158,  158,
  196,  197,  104,  158,   53,  158,  158,  158,  158,  158,
  158,   88,   88,   88,  175,   88,   88,  261,   92,  144,
   88,  268,   88,   88,   88,   88,   88,   88,    0,   88,
   88,   88,   88,  172,   40,   88,    0,  170,   51,   52,
    0,   42,    0,    0,  138,  119,   36,   45,    0,   66,
   75,  201,  201,  201,   21,   36,   22,    0,    0,   23,
   24,   53,   85,   21,    0,   22,    0,   73,   23,   24,
   86,   86,   86,    0,   86,   86,  153,    0,  112,   86,
  113,   86,   86,   86,   86,   86,   86,   74,   86,   86,
   86,   86,    0,    0,   86,   90,   90,   90,    0,   90,
   90,    0,    0,    0,   90,  149,   90,   90,   90,   90,
   90,   90,    0,   90,   90,   90,   90,    0,    0,   90,
   87,   87,   87,  147,   87,   87,  182,    0,  112,   87,
  113,   87,   87,   87,   87,   87,   87,    0,   87,   87,
   87,   87,    0,  201,   87,   50,   51,   52,  201,    0,
   21,    0,    0,   21,    0,   21,   21,    0,   21,   21,
   21,   42,    0,   21,   21,    0,  119,   36,    0,   53,
   66,   21,    0,  177,  177,   21,    0,   22,  177,    0,
   23,   24,  177,    0,  177,  177,  177,  177,  177,  177,
  174,  174,  225,  100,   98,  174,   99,    0,  101,  174,
    0,  174,  174,  174,  174,  174,  174,    0,  172,   51,
   52,  175,  175,  175,   51,   52,  175,  226,    0,  112,
  175,  113,  175,  175,  175,  175,  175,  175,    0,    0,
  172,  172,   53,  259,  257,  172,  258,   53,  260,  172,
    0,  172,  172,  172,  172,  172,  172,   75,   75,  220,
    0,  262,   75,  263,    0,  264,   75,    0,   75,   75,
   75,   75,   75,   75,   73,   73,  221,    0,    0,   73,
  177,   51,   52,   73,    0,   73,   73,   73,   73,   73,
   73,  215,  216,  217,   74,   74,  232,   51,   52,   74,
    0,    0,    0,   74,   53,   74,   74,   74,   74,   74,
   74,    0,  149,  149,  327,   53,  112,  149,  113,    0,
   53,  149,    0,  149,  149,  149,  149,  149,  149,    0,
  147,  147,  274,  100,   98,  147,   99,    0,  101,  147,
    0,  147,  147,  147,  147,  147,  147,    0,    0,    0,
  254,   51,   52,  220,  220,  220,  220,    0,    0,  220,
  220,  220,  220,  289,  216,  217,  295,  216,  217,    0,
  221,  221,  221,  221,   53,    0,  221,  221,  221,  221,
  297,  216,  217,  299,  216,  217,    0,   53,    0,    0,
   53,  301,  216,  217,   65,   36,    0,  116,   66,  116,
    0,  116,    0,   21,   53,   22,    0,   53,   23,   24,
    0,    0,   13,    0,    0,   53,   13,   13,   13,   13,
    0,   13,   13,   13,  277,  200,   13,   13,    0,    0,
    8,    9,   38,   38,   13,   13,   38,    0,   38,   38,
    0,   38,   38,   38,    0,    0,   38,   38,    0,   28,
   28,    0,    0,   28,   38,   28,   28,    0,   28,   28,
   28,  290,  291,   28,   28,    0,   60,  296,  298,    0,
   60,   28,   60,   60,    0,   60,   60,   60,    0,    0,
   60,   60,    0,  312,    6,  292,  293,  313,   60,    8,
    9,  300,  302,   10,   19,    0,    0,   20,    0,    8,
    9,    0,   21,   10,   22,   11,    0,   23,   24,    0,
    0,    6,    0,    0,    7,   11,    8,    9,    6,    0,
   10,  308,    0,    8,    9,    6,    0,   10,  309,    0,
    8,    9,   11,    0,   10,    6,    0,    0,  310,   11,
    8,    9,    6,    0,   10,  311,   11,    8,    9,    0,
    0,   10,    0,   68,   68,  113,   11,  113,   68,  113,
    0,    0,   68,   11,   68,   68,   68,   68,   68,   68,
   69,   69,  124,  124,  124,   69,  124,    0,  124,   69,
    0,   69,   69,   69,   69,   69,   69,   70,   70,  115,
    0,  115,   70,  115,    0,    0,   70,    0,   70,   70,
   70,   70,   70,   70,   71,   71,  112,    0,  112,   71,
  112,    0,    0,   71,    0,   71,   71,   71,   71,   71,
   71,   72,   72,  110,    0,  110,   72,  110,    0,    0,
   72,    0,   72,   72,   72,   72,   72,   72,   94,   36,
  114,    0,  114,   95,  114,    0,    0,   21,    0,   22,
  119,   36,   23,   24,   66,    0,    0,  185,   36,   21,
    0,   22,  186,    0,   23,   24,   21,    0,   22,  336,
   36,   23,   24,    0,  337,    0,  340,   36,   21,    0,
   22,  341,    0,   23,   24,   21,   36,   22,    0,    0,
   23,   24,    0,  111,   21,  111,   22,  111,   36,   23,
   24,  127,   69,    0,    0,   36,   21,    0,   22,   82,
    0,   23,   24,   21,    0,   22,   36,    0,   23,   24,
  184,    0,    0,   36,   21,    0,   22,  331,    0,   23,
   24,   21,    0,   22,   36,    0,   23,   24,  332,    0,
    0,   36,   21,    0,   22,  333,    0,   23,   24,   21,
    0,   22,   36,    0,   23,   24,  334,    0,    0,   36,
   21,    0,   22,  335,    0,   23,   24,   21,    0,   22,
   36,    0,   23,   24,  338,    0,    0,   36,   21,    0,
   22,  339,    0,   23,   24,   21,    0,   22,    0,    0,
   23,   24,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
   40,    7,   40,   22,   23,   61,   40,    2,   40,   91,
   59,   61,   23,   40,   20,   40,  148,  149,   91,   24,
   25,   41,  256,   59,   44,   93,   44,  261,   93,   59,
   49,  257,   37,   54,  161,   54,  257,  258,   49,   83,
   84,  257,   47,   41,   63,   43,   44,   45,  256,  257,
  258,   41,   63,   44,   44,   44,   61,   76,   77,  280,
   66,   59,   60,   93,   62,   76,   77,   43,   59,   45,
   59,   41,  280,   59,   44,   93,  256,  257,  258,   98,
   99,  100,  101,  102,   60,  212,   62,  106,  256,   59,
  256,  102,  258,  112,  113,  106,  140,  116,  117,  118,
  280,  211,  107,  213,  272,  116,  257,  239,   41,   59,
  131,   44,  131,  264,   44,  120,   41,   42,   43,   44,
   45,   44,   47,  128,  129,   44,  256,  254,   43,   59,
   45,  261,  262,  263,   59,   60,   59,   62,  257,  258,
   59,  268,  161,   42,  163,   98,   99,  256,   47,  279,
  161,  260,   41,   42,   43,   44,   45,  267,   47,  112,
  113,  280,  183,  168,  183,   59,  187,  188,  187,  188,
   59,   60,   59,   62,   59,  257,  258,   41,  256,   43,
   44,   45,  260,   41,  257,  258,   44,  256,  256,  257,
  258,  256,   93,  212,  304,   59,   60,  257,   62,  268,
  269,  212,  262,  263,   41,  224,   42,  256,  257,   41,
  256,   47,   44,  224,   41,  261,  256,  273,  256,  257,
  258,  259,  256,  273,  256,  257,  258,   59,  257,  256,
   40,  256,  257,  258,   41,  254,  256,   93,  257,  258,
  259,  260,  280,  254,  263,  264,  265,  266,  280,  268,
  245,  246,  247,  248,  249,  280,   40,  268,  256,  257,
  258,   41,  260,  261,   41,  270,  256,  265,  257,  267,
  268,  269,  270,  271,  272,  294,  274,  275,  276,  277,
  262,  263,  280,  294,   42,  257,  256,  257,  258,   47,
   41,  256,   43,   44,   45,   61,  261,  279,  274,  275,
  276,  277,  308,  309,  310,  311,  312,  313,   59,   60,
  280,   62,  318,  319,  320,   44,  321,  322,  323,  324,
  325,  326,  269,  328,  329,  330,  256,  257,  256,  257,
   59,  256,  257,  258,  257,  260,  261,   41,  257,   41,
  265,  107,  267,  268,  269,  270,  271,  272,   42,  274,
  275,  276,  277,   47,  120,  280,   41,   59,   43,   44,
   45,   42,   43,   40,   45,  256,   47,  256,  257,  258,
  261,  260,  261,   40,   59,   60,  265,   62,  267,  268,
  269,  270,  271,  272,  257,  274,  275,  276,  277,  261,
   41,  280,  256,  257,  258,   91,  260,  261,  256,  257,
  258,  265,  168,  267,  268,  269,  270,  271,  272,   40,
  274,  275,  276,  277,  269,   41,  280,   43,   44,   45,
  100,  101,  280,  269,  256,  257,  258,  256,  257,  256,
  257,  258,  259,   59,   60,   41,   62,  117,  118,   59,
   41,  257,   43,   44,   45,   40,  262,  263,  280,  256,
  257,  258,   41,  280,   59,   44,  256,  257,   59,   60,
   59,   62,  262,  263,  257,   41,   59,   43,   44,   45,
   41,   41,   43,  280,   45,   11,  256,  257,  258,  256,
  257,  258,   41,   59,   60,   44,   62,   40,  257,   41,
   93,   42,   43,   46,   45,   41,   47,   44,   41,   40,
  280,   41,  258,  280,  270,  256,  257,  258,   61,  260,
  261,   41,   59,  260,  265,  269,  267,  268,  269,  270,
  271,  272,   59,  274,  275,  276,  277,  256,  257,  280,
   59,   59,  261,  262,  263,   59,   59,   59,   91,   59,
  256,  257,  258,   59,   59,   59,   93,   83,   84,   59,
  279,   59,  261,   59,  256,  257,   40,  244,  260,  261,
  140,  140,   46,  265,  280,  267,  268,  269,  270,  271,
  272,  256,  257,  258,   59,  260,  261,   61,   45,   84,
  265,  224,  267,  268,  269,  270,  271,  272,   -1,  274,
  275,  276,  277,   59,   11,  280,   -1,  256,  257,  258,
   -1,   18,   -1,   -1,  140,  256,  257,   91,   -1,  260,
   59,  147,  148,  149,  265,  257,  267,   -1,   -1,  270,
  271,  280,   39,  265,   -1,  267,   -1,   59,  270,  271,
  256,  257,  258,   -1,  260,  261,   41,   -1,   43,  265,
   45,  267,  268,  269,  270,  271,  272,   59,  274,  275,
  276,  277,   -1,   -1,  280,  256,  257,  258,   -1,  260,
  261,   -1,   -1,   -1,  265,   59,  267,  268,  269,  270,
  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,  280,
  256,  257,  258,   59,  260,  261,   41,   -1,   43,  265,
   45,  267,  268,  269,  270,  271,  272,   -1,  274,  275,
  276,  277,   -1,  239,  280,  256,  257,  258,  244,   -1,
  257,   -1,   -1,  260,   -1,  262,  263,   -1,  265,  266,
  267,  138,   -1,  270,  271,   -1,  256,  257,   -1,  280,
  260,  278,   -1,  256,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   41,   42,   43,  261,   45,   -1,   47,  265,
   -1,  267,  268,  269,  270,  271,  272,   -1,  256,  257,
  258,  256,  257,  256,  257,  258,  261,   41,   -1,   43,
  265,   45,  267,  268,  269,  270,  271,  272,   -1,   -1,
  256,  257,  280,   42,   43,  261,   45,  280,   47,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,  163,
   -1,   41,  261,   43,   -1,   45,  265,   -1,  267,  268,
  269,  270,  271,  272,  256,  257,  163,   -1,   -1,  261,
  256,  257,  258,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,  258,  256,  257,  256,  257,  258,  261,
   -1,   -1,   -1,  265,  280,  267,  268,  269,  270,  271,
  272,   -1,  256,  257,   41,  280,   43,  261,   45,   -1,
  280,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
  256,  257,   41,   42,   43,  261,   45,   -1,   47,  265,
   -1,  267,  268,  269,  270,  271,  272,   -1,   -1,   -1,
  256,  257,  258,  257,  258,  259,  260,   -1,   -1,  263,
  264,  265,  266,  256,  257,  258,  256,  257,  258,   -1,
  257,  258,  259,  260,  280,   -1,  263,  264,  265,  266,
  256,  257,  258,  256,  257,  258,   -1,  280,   -1,   -1,
  280,  256,  257,  258,  256,  257,   -1,   41,  260,   43,
   -1,   45,   -1,  265,  280,  267,   -1,  280,  270,  271,
   -1,   -1,  256,   -1,   -1,  280,  260,  261,  262,  263,
   -1,  265,  266,  267,  256,  257,  270,  271,   -1,   -1,
  262,  263,  256,  257,  278,  279,  260,   -1,  262,  263,
   -1,  265,  266,  267,   -1,   -1,  270,  271,   -1,  256,
  257,   -1,   -1,  260,  278,  262,  263,   -1,  265,  266,
  267,  257,  258,  270,  271,   -1,  256,  263,  264,   -1,
  260,  278,  262,  263,   -1,  265,  266,  267,   -1,   -1,
  270,  271,   -1,  256,  257,  259,  260,  260,  278,  262,
  263,  265,  266,  266,  257,   -1,   -1,  260,   -1,  262,
  263,   -1,  265,  266,  267,  278,   -1,  270,  271,   -1,
   -1,  257,   -1,   -1,  260,  278,  262,  263,  257,   -1,
  266,  260,   -1,  262,  263,  257,   -1,  266,  260,   -1,
  262,  263,  278,   -1,  266,  257,   -1,   -1,  260,  278,
  262,  263,  257,   -1,  266,  260,  278,  262,  263,   -1,
   -1,  266,   -1,  256,  257,   41,  278,   43,  261,   45,
   -1,   -1,  265,  278,  267,  268,  269,  270,  271,  272,
  256,  257,   41,   42,   43,  261,   45,   -1,   47,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,   41,
   -1,   43,  261,   45,   -1,   -1,  265,   -1,  267,  268,
  269,  270,  271,  272,  256,  257,   41,   -1,   43,  261,
   45,   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,   41,   -1,   43,  261,   45,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,  256,  257,
   41,   -1,   43,  261,   45,   -1,   -1,  265,   -1,  267,
  256,  257,  270,  271,  260,   -1,   -1,  256,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,  256,
  257,  270,  271,   -1,  261,   -1,  256,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,  257,  267,   -1,   -1,
  270,  271,   -1,   41,  265,   43,  267,   45,  257,  270,
  271,  272,  261,   -1,   -1,  257,  265,   -1,  267,  261,
   -1,  270,  271,  265,   -1,  267,  257,   -1,  270,  271,
  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,
  271,  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,
   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,  265,
   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,
  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,
  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,   -1,  267,   -1,   -1,
  270,  271,
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
"lista_variables : lista_variables ID",
"lista_variables : ID",
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

//#line 332 "gramatica.y"
private AnalizadorLexicoTp2 lexer;
private boolean expresionInvalida = false;
public Parser(AnalizadorLexicoTp2 lexer){
    this.lexer = lexer;
}
private int yylex(){
    return lexer.yylex();
}
private void yyerror(String mensaje){
    System.err.println("Error sintáctico en la linea " + lexer.getLineaActual() + ": " + mensaje);
}
//#line 784 "Parser.java"
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
//#line 20 "gramatica.y"
{System.out.println("Declaracion de programa");}
break;
case 2:
//#line 21 "gramatica.y"
{ yyerror("Falta de nombre de programa");}
break;
case 3:
//#line 22 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN");}
break;
case 4:
//#line 23 "gramatica.y"
{ yyerror("Falta la palabra reservada END");}
break;
case 12:
//#line 41 "gramatica.y"
{System.out.println("Declaracion de VAR");}
break;
case 13:
//#line 42 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 14:
//#line 46 "gramatica.y"
{System.out.println("Declaracion de comptime");}
break;
case 15:
//#line 47 "gramatica.y"
{ yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
break;
case 16:
//#line 48 "gramatica.y"
{ yyerror("Falta tipo en las variables en COMPTIME");}
break;
case 17:
//#line 52 "gramatica.y"
{System.out.println("Declaracion de tipo");}
break;
case 18:
//#line 52 "gramatica.y"
{System.out.println("Declaracion de tipo");}
break;
case 20:
//#line 57 "gramatica.y"
{ yyerror("Lista de variables: Falta la ','");}
break;
case 22:
//#line 62 "gramatica.y"
{System.out.println("Declaracion de funcion");}
break;
case 23:
//#line 63 "gramatica.y"
{ yyerror("Falta parentesis de apertura en el encabezado de la función"); }
break;
case 24:
//#line 64 "gramatica.y"
{ yyerror("Falta parentesis de cierre en el encabezado de la función"); }
break;
case 25:
//#line 65 "gramatica.y"
{ yyerror("Falta de nombre de funcion");}
break;
case 26:
//#line 66 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN de la funcion");}
break;
case 27:
//#line 67 "gramatica.y"
{ yyerror("Falta la palabra reservada END de la funcion");}
break;
case 28:
//#line 68 "gramatica.y"
{ yyerror("Falta el ';'");}
break;
case 29:
//#line 69 "gramatica.y"
{ yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
break;
case 30:
//#line 73 "gramatica.y"
{System.out.println("Declaracion de parametro_formales");}
break;
case 31:
//#line 74 "gramatica.y"
{System.out.println("Declaracion de parametro_formales");}
break;
case 32:
//#line 78 "gramatica.y"
{System.out.println("Declaracion de parametro_formal");}
break;
case 33:
//#line 79 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'tipo'");}
break;
case 34:
//#line 80 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'ID''");}
break;
case 35:
//#line 84 "gramatica.y"
{System.out.println("Declaracion de clase");}
break;
case 36:
//#line 85 "gramatica.y"
{ yyerror ("Falta el BEGIN de clase");}
break;
case 37:
//#line 86 "gramatica.y"
{ yyerror("Falta el END de clase");}
break;
case 38:
//#line 87 "gramatica.y"
{ yyerror("Falta el ';' al final de la clase");}
break;
case 47:
//#line 108 "gramatica.y"
{System.out.println("Sentencia EXTENDS");}
break;
case 48:
//#line 109 "gramatica.y"
{ yyerror("Sintaxis de lista de herencia invalida"); }
break;
case 49:
//#line 110 "gramatica.y"
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 50:
//#line 111 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 53:
//#line 117 "gramatica.y"
{ yyerror("Lista de IDs: Falta la ','");}
break;
case 54:
//#line 121 "gramatica.y"
{System.out.println("Declaracion de Metodo");}
break;
case 55:
//#line 122 "gramatica.y"
{ yyerror("Falta el BEGIN en la declaracion del metodo");}
break;
case 56:
//#line 123 "gramatica.y"
{ yyerror("Falta una ',' o tipo invalido en los parametros del metodo"); }
break;
case 57:
//#line 124 "gramatica.y"
{ yyerror("Falta el END en la declaracion del metodo");}
break;
case 58:
//#line 125 "gramatica.y"
{ yyerror("Falta el ';' en la declaracion del metodo");}
break;
case 59:
//#line 129 "gramatica.y"
{System.out.println("Declaracion de objeto");}
break;
case 60:
//#line 130 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'");}
break;
case 68:
//#line 145 "gramatica.y"
{ yyerror("Asignacion: Falta el ';', o un operador o expresion");}
break;
case 69:
//#line 146 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");}
break;
case 70:
//#line 147 "gramatica.y"
{ yyerror("Salida: Falta el ';'");}
break;
case 71:
//#line 148 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");}
break;
case 72:
//#line 149 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");}
break;
case 73:
//#line 153 "gramatica.y"
{
        if (expresionInvalida != true){
            System.out.println("Declaracion Asignacion");
        }
        else {
            expresionInvalida = true;
        }

    }
break;
case 74:
//#line 162 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
break;
case 75:
//#line 163 "gramatica.y"
{ yyerror("Falta expresion en la asignacion");}
break;
case 77:
//#line 168 "gramatica.y"
{System.out.println("Declaracion de acceso_posicional");}
break;
case 78:
//#line 169 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de apertura "); }
break;
case 80:
//#line 174 "gramatica.y"
{ yyerror("Acceso posicional: Índice vacío"); }
break;
case 81:
//#line 175 "gramatica.y"
{ yyerror("Acceso posicional: Índice inválido"); }
break;
case 82:
//#line 176 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de cierre"); }
break;
case 83:
//#line 177 "gramatica.y"
{ yyerror("Acceso posicional: Argumento invalido"); }
break;
case 88:
//#line 187 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 89:
//#line 188 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 90:
//#line 189 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 91:
//#line 190 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 95:
//#line 197 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 96:
//#line 198 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 97:
//#line 199 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 98:
//#line 200 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 102:
//#line 207 "gramatica.y"
{System.out.println("Acceso posicional");}
break;
case 103:
//#line 208 "gramatica.y"
{System.out.println("Asignacion EXP");}
break;
case 106:
//#line 214 "gramatica.y"
{System.out.println("Conversion DF");}
break;
case 107:
//#line 215 "gramatica.y"
{yyerror("Solo se pueden expresiones");}
break;
case 108:
//#line 216 "gramatica.y"
{yyerror("Falta expresion");}
break;
case 109:
//#line 217 "gramatica.y"
{yyerror("Falta parentesis");}
break;
case 112:
//#line 223 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 113:
//#line 224 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 114:
//#line 225 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 115:
//#line 226 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 119:
//#line 233 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 120:
//#line 234 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 121:
//#line 235 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 122:
//#line 236 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 129:
//#line 246 "gramatica.y"
{yyerror("No se permite este anidamiento, solo 1 expresion");}
break;
case 130:
//#line 250 "gramatica.y"
{System.out.println("Metodo|funcion con orden opcional");}
break;
case 131:
//#line 251 "gramatica.y"
{System.out.println("Objeto con orden opcional");}
break;
case 132:
//#line 252 "gramatica.y"
{ yyerror("Error en los parametros: Falta un ')'"); }
break;
case 133:
//#line 253 "gramatica.y"
{ yyerror("Lista de parametros vacia."); }
break;
case 134:
//#line 254 "gramatica.y"
{ 
        yyerror("Lista de parametros vacia."); expresionInvalida = true;
     }
break;
case 136:
//#line 261 "gramatica.y"
{ yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); }
break;
case 142:
//#line 273 "gramatica.y"
{yyerror("Falta ',' entre argumentos");}
break;
case 143:
//#line 274 "gramatica.y"
{yyerror("Elemento no valido despues de ','");}
break;
case 145:
//#line 282 "gramatica.y"
{System.out.println("Declaracion Iterativa WHILE");}
break;
case 146:
//#line 283 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion");}
break;
case 147:
//#line 284 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa");}
break;
case 148:
//#line 285 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa");}
break;
case 149:
//#line 286 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa");}
break;
case 150:
//#line 287 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion");}
break;
case 151:
//#line 288 "gramatica.y"
{ yyerror("error en condicion de iteracion"); }
break;
case 152:
//#line 292 "gramatica.y"
{System.out.println("Declaracion Seleccion");}
break;
case 153:
//#line 293 "gramatica.y"
{System.out.println("Declaracion Seleccion Simple");}
break;
case 154:
//#line 294 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); }
break;
case 155:
//#line 295 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion");}
break;
case 156:
//#line 296 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); }
break;
case 157:
//#line 297 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion");}
break;
case 158:
//#line 301 "gramatica.y"
{System.out.println("Condicion");}
break;
case 164:
//#line 305 "gramatica.y"
{System.out.println("Comparador");}
break;
case 165:
//#line 309 "gramatica.y"
{System.out.println("BloqueEJ1");}
break;
case 166:
//#line 310 "gramatica.y"
{System.out.println("BloqueEJ2");}
break;
case 167:
//#line 311 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
break;
case 168:
//#line 312 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el END ");}
break;
case 169:
//#line 316 "gramatica.y"
{System.out.println("POUT cadena");}
break;
case 170:
//#line 317 "gramatica.y"
{System.out.println("POUT expresion");}
break;
case 171:
//#line 318 "gramatica.y"
{yyerror("Falta Argumento en POUT");}
break;
case 172:
//#line 319 "gramatica.y"
{yyerror ("Falta parentesis de cierre");}
break;
case 173:
//#line 320 "gramatica.y"
{yyerror ("Falta parentesis de apertura");}
break;
case 174:
//#line 321 "gramatica.y"
{yyerror ("Falta parentesis");}
break;
case 175:
//#line 322 "gramatica.y"
{yyerror ("Falta parentesis de cierre");}
break;
case 176:
//#line 323 "gramatica.y"
{yyerror ("Falta parentesis de apertura");}
break;
case 177:
//#line 324 "gramatica.y"
{yyerror ("Falta parentesis");}
break;
case 178:
//#line 328 "gramatica.y"
{System.out.println("Declaracion Retorno");}
break;
case 179:
//#line 329 "gramatica.y"
{yyerror("Retorno debe tener una expresion y parentesis");}
break;
//#line 1407 "Parser.java"
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
