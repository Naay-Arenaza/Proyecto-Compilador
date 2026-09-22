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
   10,    6,    6,    6,    6,    6,    6,   11,   11,   12,
   12,   12,    7,    7,    7,    7,   13,   13,   13,   13,
   14,   14,   16,   16,   15,   15,   15,   15,   18,   18,
   17,   17,   17,   17,   17,    8,    8,    2,    2,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   20,
   20,   25,   25,   27,   28,   28,   26,   26,   26,   26,
   26,   26,   26,   29,   29,   29,   29,   29,   29,   29,
   30,   30,   30,   30,   30,   30,   31,   31,   31,   31,
   33,   33,   33,   34,   34,   32,   32,   35,   24,   24,
   24,   24,   24,   24,   21,   21,   21,   21,   21,   21,
   37,   38,   38,   38,   38,   38,   38,   36,   36,   36,
   36,   22,   22,   22,   23,
};
final static short yylen[] = {                            2,
    5,    5,    5,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    2,
    1,   11,   11,   11,   11,   10,   11,    3,    1,    2,
    1,    1,    6,    6,    6,    5,    2,    1,    1,    0,
    2,    1,    1,    1,    3,    3,    2,    2,    3,    1,
    9,    9,    9,    9,    8,    3,    2,    2,    1,    2,
    2,    2,    2,    2,    1,    1,    1,    1,    1,    3,
    3,    1,    1,    4,    1,    1,    3,    3,    3,    3,
    3,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    1,    5,    4,    1,    5,    7,    5,    7,
    3,    3,    0,    3,    1,    3,    1,    1,    6,    6,
    5,    5,    4,    5,    8,    6,    5,    5,    4,    6,
    3,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    4,    4,    3,    4,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
   21,    0,    0,    0,    0,    0,    0,    0,   59,    0,
    0,    0,    0,    0,    0,   73,    0,    0,    0,    0,
    0,    0,    0,   20,   56,    0,    0,    0,    0,    0,
   92,    0,    0,    0,   93,    0,   90,   96,    0,    0,
    0,    0,    0,  128,    0,    2,   58,   60,   61,   62,
   63,   64,    0,    0,    0,    0,    0,   16,    0,    0,
   12,    3,    4,    1,   19,   75,   76,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  122,  123,
  124,  125,    0,    0,  126,  127,    0,    0,    0,    0,
    0,    0,  134,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   43,    0,    0,    0,   39,   42,   44,    0,
   15,   14,    0,    0,   74,  135,    0,    0,    0,   87,
   88,    0,    0,    0,  107,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,   84,   89,   85,    0,
  119,  132,  133,  130,  131,  129,    0,    0,    0,    0,
    0,   50,   48,    0,    0,    0,   37,   41,    0,    0,
   31,    0,    0,   29,    0,    0,    0,    0,    0,    0,
    0,   95,    0,  117,  118,  114,    0,    0,  112,   46,
   45,    0,    0,   34,   35,   33,   30,    0,    6,    6,
    6,    0,   99,  106,   97,   94,    0,    0,  120,    0,
  116,  110,  109,   49,    0,    0,   28,    0,    0,    0,
    0,  105,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  102,    0,  101,  100,   98,  115,    0,    0,
    0,    0,    0,    0,    0,  104,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   23,   27,
   24,   25,   22,   53,   52,   54,   51,
};
final static short yydgoto[] = {                          3,
    4,   28,   12,   13,   14,   15,   16,   17,   18,   41,
  183,  184,  125,  126,  127,  128,  129,  174,   29,   30,
   31,   32,   33,   34,   35,   54,   36,   88,   56,   57,
   58,  144,  213,  233,  145,   65,   59,  107,
};
final static short yysindex[] = {                       -57,
    0,    0,    0,  328,  193, -242,  700,    0,    0, -230,
   77,    0,    0,    0,    0,    0,    0, -149,  700,  700,
    0,  -31,  -46,   19,  -40,   27,  582,  424,    0,   26,
   34,   59,   89,  100,  -22,    0,   13, -242,  -42,  144,
  -23,  637,  278,    0,    0,  -76,  200, -234,  430,  -20,
    0,  188, -234,  190,    0,  102,    0,    0,  255,  -34,
  700,  700,  199,    0, -160,    0,    0,    0,    0,    0,
    0,    0, -234, -234, -173, -173,  -43,    0,  201,  221,
    0,    0,    0,    0,    0,    0,    0,  137,  341, -214,
 -214, -220, -220, -175,  234,   33, -234,  271,    0,    0,
    0,    0, -170, -155,    0,    0, -234, -134, -130,  599,
   14,  257,    0,  363,  648,  611, -234,  265,  -37,  164,
  164,  -51,    0,   56,   66, -173,    0,    0,    0, -116,
    0,    0,   99,  -68,    0,    0,  109,  102,  102,    0,
    0,  739,  164,   24,    0, -234,  280,  552,  599,   95,
  109,  102,  109,  102,  164,    0,    0,    0,    0,  114,
    0,    0,    0,    0,    0,    0,  301, -234, -234,  352,
  356,    0,    0,  -19,  386,  370,    0,    0,  374,  392,
    0,  183,   63,    0,  413,   93,  372, -234,  372,  592,
 -125,    0,  -72,    0,    0,    0,  427,  435,    0,    0,
    0,  222,  231,    0,    0,    0,    0,   99,    0,    0,
    0, -252,    0,    0,    0,    0,  748,  125,    0,  599,
    0,    0,    0,    0,  448,  294,    0,  464,  471,  368,
  393,    0,  -35,  372,  372,  228,  230,   35,  700,  700,
  700,  700,    0,  240,    0,    0,    0,    0,  700,  700,
  700,  655,  666,  673,  618,    0,  684,  691,  630,  440,
  449,  450,  451,  455,  462,  465,  470,  473,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  454,   -5,    0,    0,    0,    0,    0,    0,  489,
  507,  531,  548,  565,    0,    0,    0,    0,    0,    0,
  -88,    0,    0,    0,    0,    0,    0,    0,    0,  -12,
    0,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  262,  -94,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  213,
  290,    0,    0,    0,    0,  -58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   54,   76,    0,
    0,    0,  316,    0,    0,    0,    0,    0,    0,    0,
   98,  120,  142,  167,  235,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  312,
    0,    0,    0,  -14,  -25,    0,    0,    0,    0,  385,
    0,  381,    0,    0,    0,    0,   10,    0,   10,    0,
    0,    0,    0,    0,    0,    0,    0,  346,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   10,   10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  445,    0,    0,    0, -109,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  107,  359,    0,  -64,    0,    0,    0,    0,  441,   73,
 -129,  322,  457,    0,  411,  414,    0,    0,  403,    0,
    0,    0,    0,    0,    0,  606,  579,    0,  357,   84,
    0,  353, -171,    0,  362,  -49,  -39,    0,
};
final static int YYTABLESIZE=971;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   46,   46,  169,  231,  186,  232,  113,  173,  244,  111,
  123,  123,   46,   98,   21,  132,   78,  215,   21,   94,
   46,   49,   50,   51,  202,   96,   37,   45,   91,   91,
   91,   91,   91,   21,   91,   81,   50,   51,   74,  201,
   95,  137,   50,   51,   47,   52,   91,   91,  150,   91,
  103,  103,  103,  103,  103,   72,  103,  245,   48,   52,
  160,  123,  246,  247,  189,   52,   60,  188,  103,  103,
   47,  103,   83,  226,   83,   83,   83,  167,   22,  170,
  142,   50,   51,   39,   68,  151,   50,   51,    8,    9,
   83,   83,   69,   83,   80,  118,   80,   80,   80,  193,
  153,   50,   51,  209,   52,  122,  208,   21,    5,   52,
   77,  119,   80,   80,   40,   80,   82,   70,   82,   82,
   82,  156,   50,   51,   52,  158,   50,   51,  197,  198,
  217,   50,   51,  211,   82,   82,  208,   82,   79,  179,
   79,   79,   79,  108,  180,   52,   55,   71,  109,   52,
   92,   55,   55,   55,   52,   93,   79,   79,   72,   79,
   77,   40,   77,   77,   77,  235,   40,   13,  188,   55,
  236,   13,   13,   13,   13,  140,  141,   13,   77,   77,
   85,   77,   81,  219,   81,   81,   81,  185,  181,   13,
   13,  157,  159,    8,    9,  220,  221,   38,    1,    2,
   81,   81,   38,   81,  171,  172,  103,   78,  104,   78,
   78,   78,  131,   44,   44,   49,   50,   51,   49,   50,
   51,   49,   50,   51,  112,   78,   78,   97,   78,  135,
   21,   21,  103,   44,  104,   21,   21,   21,  117,   52,
  133,   47,   52,   91,   91,   52,   47,   91,   91,  106,
   73,  105,   91,   21,   91,   91,   91,   91,   91,   91,
  134,   91,   91,   91,   91,  103,  103,   72,   75,  103,
  103,   70,   76,  146,  103,  121,  103,  103,  103,  103,
  103,  103,  161,  103,  103,  103,  103,   83,   83,  147,
  250,   83,   83,  121,  251,  110,   83,  162,   83,   83,
   83,   83,   83,   83,  168,   83,   83,   83,   83,   80,
   80,  149,  175,   80,   80,  228,  229,  230,   80,  191,
   80,   80,   80,   80,   80,   80,  176,   80,   80,   80,
   80,   82,   82,   21,  238,   82,   82,  208,    8,    9,
   82,  196,   82,   82,   82,   82,   82,   82,   71,   82,
   82,   82,   82,   79,   79,  181,  108,   79,   79,  108,
    8,    9,   79,  194,   79,   79,   79,   79,   79,   79,
  113,   79,   79,   79,   79,   77,   77,   42,   43,   77,
   77,  136,  195,  103,   77,  104,   77,   77,   77,   77,
   77,   77,  199,   77,   77,   77,   77,   81,   81,   79,
   80,   81,   81,  163,  111,  103,   81,  104,   81,   81,
   81,   81,   81,   81,  200,   81,   81,   81,   81,  115,
  116,   32,   78,   78,   32,  203,   78,   78,  204,   64,
   67,   78,  205,   78,   78,   78,   78,   78,   78,  207,
   78,   78,   78,   78,   67,   67,  138,  139,   19,    6,
  206,   38,   20,  210,    8,    9,   86,   87,   10,  152,
  154,   64,  212,   99,  100,  101,  102,  222,   70,   70,
   11,   92,   90,   70,   91,  223,   93,   70,  224,   70,
   70,   70,   70,   70,   70,  243,  225,  181,  237,  249,
  121,  121,    8,    9,  121,  121,  248,  256,  269,  121,
   64,  121,  121,  121,  121,  121,  121,  270,  271,  272,
   61,   23,   64,  273,   62,  124,  124,   67,   67,   24,
  274,   25,   40,  275,   26,   27,   61,   23,  276,  227,
   62,  277,  130,   83,   23,   24,  177,   25,   84,  178,
   26,   27,   24,  218,   25,   71,   71,   26,   27,  214,
   71,   64,    0,    0,   71,    0,   71,   71,   71,   71,
   71,   71,    0,    0,    0,    0,  124,  113,  113,    0,
    0,    0,  113,  182,  182,    0,  113,    0,  113,  113,
  113,  113,  113,  113,    6,    0,    0,    7,    0,    8,
    9,    0,  192,   10,  103,    0,  104,  252,  253,  254,
  255,  111,  111,   55,    0,   11,  111,  257,  258,  259,
  111,    0,  111,  111,  111,  111,  111,  111,    0,    0,
    0,    0,   64,  241,    6,    0,   55,  242,    0,    8,
    9,   55,  216,   10,  103,    0,  104,    0,   55,    0,
   36,   36,    0,  182,   36,   11,   36,   36,  182,    0,
   36,   55,   55,   89,   67,   67,   67,   67,    0,   67,
   67,   67,   36,    0,    0,  114,    0,    0,   55,   55,
   55,   55,   55,    0,    0,   55,    0,    0,  120,  121,
   23,   55,   55,    0,   66,   55,   55,   55,   24,    0,
   25,    0,    0,   26,   27,   55,    0,   55,    0,  143,
   26,   26,  148,    0,   26,    0,   26,   26,    0,   57,
   26,    0,  155,   57,    0,   57,   57,    0,    0,   57,
    6,    0,   26,  239,   55,    8,    9,    6,    0,   10,
  240,   57,    8,    9,    0,    0,   10,    0,    0,    0,
    0,   11,    0,    0,   65,   65,   55,   55,   11,   65,
    0,  190,    0,   65,    0,   65,   65,   65,   65,   65,
   65,    0,   66,   66,    0,    0,   55,   66,    0,   55,
    0,   66,    0,   66,   66,   66,   66,   66,   66,  187,
   92,   90,    0,   91,    0,   93,   67,   67,  234,   92,
   90,   67,   91,  143,   93,   67,  143,   67,   67,   67,
   67,   67,   67,   68,   68,    0,    0,    0,   68,    0,
    0,    0,   68,    0,   68,   68,   68,   68,   68,   68,
   69,   69,    0,    0,    0,   69,    0,    0,    0,   69,
    0,   69,   69,   69,   69,   69,   69,   61,   23,    0,
    0,   62,    0,    0,    0,    0,   24,    0,   25,    0,
    0,   26,   27,   63,   61,   23,    0,    0,   62,    0,
    0,    0,    0,   24,    0,   25,  165,   23,   26,   27,
    0,  166,    0,  263,   23,   24,    0,   25,  264,    0,
   26,   27,   24,    0,   25,  267,   23,   26,   27,    0,
  268,    0,    0,   23,   24,    0,   25,   82,    0,   26,
   27,   24,    0,   25,   23,    0,   26,   27,  164,    0,
    0,   23,   24,    0,   25,  260,    0,   26,   27,   24,
    0,   25,   23,    0,   26,   27,  261,    0,    0,   23,
   24,    0,   25,  262,    0,   26,   27,   24,    0,   25,
   23,    0,   26,   27,  265,    0,    0,   23,   24,    0,
   25,  266,    0,   26,   27,   24,   23,   25,    0,    0,
   26,   27,    0,    0,   24,    0,   25,    0,    0,   26,
   27,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   44,   44,   40,  256,  134,  258,   41,   59,   44,   59,
   75,   76,   44,   53,  257,   59,   59,  189,   44,   40,
   44,  256,  257,  258,   44,   46,  257,   59,   41,   42,
   43,   44,   45,   59,   47,   59,  257,  258,   61,   59,
   61,  256,  257,  258,   91,  280,   59,   60,   98,   62,
   41,   42,   43,   44,   45,   61,   47,   93,   40,  280,
  110,  126,  234,  235,   41,  280,   40,   44,   59,   60,
   91,   62,   41,  203,   43,   44,   45,  117,    6,  119,
  256,  257,  258,   11,   59,  256,  257,  258,  262,  263,
   59,   60,   59,   62,   41,  256,   43,   44,   45,  149,
  256,  257,  258,   41,  280,  279,   44,  257,    2,  280,
   38,  272,   59,   60,  264,   62,   41,   59,   43,   44,
   45,  256,  257,  258,  280,  256,  257,  258,  168,  169,
  256,  257,  258,   41,   59,   60,   44,   62,   41,  256,
   43,   44,   45,   42,  261,  280,  256,   59,   47,  280,
   42,  261,  262,  263,  280,   47,   59,   60,   59,   62,
   41,  256,   43,   44,   45,   41,  261,  256,   44,  279,
  220,  260,  261,  262,  263,   92,   93,  266,   59,   60,
  257,   62,   41,  256,   43,   44,   45,  256,  257,  278,
  279,  108,  109,  262,  263,  268,  269,  256,  256,  257,
   59,   60,  261,   62,  256,  257,   43,   41,   45,   43,
   44,   45,  256,  257,  257,  256,  257,  258,  256,  257,
  258,  256,  257,  258,  259,   59,   60,   40,   62,   93,
  256,  257,   43,  257,   45,  261,  262,  263,   40,  280,
   40,  256,  280,  256,  257,  280,  261,  260,  261,   60,
  273,   62,  265,  279,  267,  268,  269,  270,  271,  272,
   40,  274,  275,  276,  277,  256,  257,  273,  256,  260,
  261,   59,  260,   40,  265,   41,  267,  268,  269,  270,
  271,  272,  269,  274,  275,  276,  277,  256,  257,  257,
  256,  260,  261,   59,  260,   41,  265,   41,  267,  268,
  269,  270,  271,  272,   40,  274,  275,  276,  277,  256,
  257,   41,  257,  260,  261,  209,  210,  211,  265,   40,
  267,  268,  269,  270,  271,  272,  261,  274,  275,  276,
  277,  256,  257,  257,   41,  260,  261,   44,  262,  263,
  265,   41,  267,  268,  269,  270,  271,  272,   59,  274,
  275,  276,  277,  256,  257,  257,   41,  260,  261,   44,
  262,  263,  265,  269,  267,  268,  269,  270,  271,  272,
   59,  274,  275,  276,  277,  256,  257,   19,   20,  260,
  261,   41,  269,   43,  265,   45,  267,  268,  269,  270,
  271,  272,   41,  274,  275,  276,  277,  256,  257,  256,
  257,  260,  261,   41,   59,   43,  265,   45,  267,  268,
  269,  270,  271,  272,   59,  274,  275,  276,  277,   61,
   62,   41,  256,  257,   44,   40,  260,  261,   59,   27,
   28,  265,   59,  267,  268,  269,  270,  271,  272,  257,
  274,  275,  276,  277,   42,   43,   90,   91,  256,  257,
   59,   11,  260,   41,  262,  263,  257,  258,  266,  103,
  104,   59,   91,  274,  275,  276,  277,   41,  256,  257,
  278,   42,   43,  261,   45,   41,   47,  265,  257,  267,
  268,  269,  270,  271,  272,   93,  256,  257,   41,  260,
  256,  257,  262,  263,  260,  261,  269,  258,   59,  265,
   98,  267,  268,  269,  270,  271,  272,   59,   59,   59,
  256,  257,  110,   59,  260,   75,   76,  115,  116,  265,
   59,  267,  261,   59,  270,  271,  256,  257,   59,  208,
  260,   59,   76,  256,  257,  265,  126,  267,  261,  126,
  270,  271,  265,  191,  267,  256,  257,  270,  271,  188,
  261,  149,   -1,   -1,  265,   -1,  267,  268,  269,  270,
  271,  272,   -1,   -1,   -1,   -1,  126,  256,  257,   -1,
   -1,   -1,  261,  133,  134,   -1,  265,   -1,  267,  268,
  269,  270,  271,  272,  257,   -1,   -1,  260,   -1,  262,
  263,   -1,   41,  266,   43,   -1,   45,  239,  240,  241,
  242,  256,  257,   25,   -1,  278,  261,  249,  250,  251,
  265,   -1,  267,  268,  269,  270,  271,  272,   -1,   -1,
   -1,   -1,  220,  256,  257,   -1,   48,  260,   -1,  262,
  263,   53,   41,  266,   43,   -1,   45,   -1,   60,   -1,
  256,  257,   -1,  203,  260,  278,  262,  263,  208,   -1,
  266,   73,   74,   48,  252,  253,  254,  255,   -1,  257,
  258,  259,  278,   -1,   -1,   60,   -1,   -1,   90,   91,
   92,   93,   94,   -1,   -1,   97,   -1,   -1,   73,   74,
  257,  103,  104,   -1,  261,  107,  108,  109,  265,   -1,
  267,   -1,   -1,  270,  271,  117,   -1,  119,   -1,   94,
  256,  257,   97,   -1,  260,   -1,  262,  263,   -1,  256,
  266,   -1,  107,  260,   -1,  262,  263,   -1,   -1,  266,
  257,   -1,  278,  260,  146,  262,  263,  257,   -1,  266,
  260,  278,  262,  263,   -1,   -1,  266,   -1,   -1,   -1,
   -1,  278,   -1,   -1,  256,  257,  168,  169,  278,  261,
   -1,  146,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,   -1,  256,  257,   -1,   -1,  188,  261,   -1,  191,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,   41,
   42,   43,   -1,   45,   -1,   47,  256,  257,   41,   42,
   43,  261,   45,  188,   47,  265,  191,  267,  268,  269,
  270,  271,  272,  256,  257,   -1,   -1,   -1,  261,   -1,
   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   -1,   -1,   -1,  261,   -1,   -1,   -1,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,   -1,
   -1,  260,   -1,   -1,   -1,   -1,  265,   -1,  267,   -1,
   -1,  270,  271,  272,  256,  257,   -1,   -1,  260,   -1,
   -1,   -1,   -1,  265,   -1,  267,  256,  257,  270,  271,
   -1,  261,   -1,  256,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,   -1,  267,  256,  257,  270,  271,   -1,
  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,
  271,  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,
   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,  265,
   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,
  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,
  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,  257,  267,   -1,   -1,
  270,  271,   -1,   -1,  265,   -1,  267,   -1,   -1,  270,
  271,
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
"programa : ID sentencias_declarativas error sentencias_ejecutables END",
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
"lado_izquierdo : ID",
"lado_izquierdo : acceso_posicional",
"acceso_posicional : ID '[' indice ']'",
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
"factor : ID '=' '(' expresion ')'",
"factor : TODF '(' expresion ')'",
"factor : invocacion",
"invocacion : ID '(' parametros_reales ')' lista_constantes_opcional",
"invocacion : ID '.' ID '(' parametros_reales ')' lista_constantes_opcional",
"invocacion : ID '(' error ')' lista_constantes_opcional",
"invocacion : ID '.' ID '(' error ')' lista_constantes_opcional",
"lista_constantes_opcional : '[' lista_constantes ']'",
"lista_constantes_opcional : '[' error ']'",
"lista_constantes_opcional :",
"lista_constantes : lista_constantes ',' CTE",
"lista_constantes : CTE",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : parametro_real",
"parametro_real : expresion",
"iterativa : REPEAT bloque_ejecutable WHILE '(' condicion ')'",
"iterativa : REPEAT bloque_ejecutable error '(' condicion ')'",
"iterativa : REPEAT bloque_ejecutable WHILE '(' condicion",
"iterativa : REPEAT bloque_ejecutable WHILE condicion ')'",
"iterativa : REPEAT bloque_ejecutable WHILE condicion",
"iterativa : REPEAT WHILE '(' condicion ')'",
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
"retorno : RET '(' expresion ')'",
};

//#line 267 "gramatica.y"
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
//#line 632 "Parser.java"
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
case 7:
//#line 33 "gramatica.y"
{System.out.println("Declaracion de VAR");}
break;
case 8:
//#line 34 "gramatica.y"
{System.out.println("Declaracion de comptime");}
break;
case 9:
//#line 35 "gramatica.y"
{System.out.println("Declaracion de funcion");}
break;
case 10:
//#line 36 "gramatica.y"
{System.out.println("Declaracion de clase");}
break;
case 11:
//#line 37 "gramatica.y"
{System.out.println("Declaracion de objeto");}
break;
case 13:
//#line 42 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 15:
//#line 47 "gramatica.y"
{ yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
break;
case 16:
//#line 48 "gramatica.y"
{ yyerror("Falta tipo en las variables en COMPTIME");}
break;
case 20:
//#line 57 "gramatica.y"
{ yyerror("Lista de variables: Falta la ','");}
break;
case 23:
//#line 63 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta de nombre de funcion");}
break;
case 24:
//#line 64 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta la palabra reservada BEGIN");}
break;
case 25:
//#line 65 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta la palabra reservada END");}
break;
case 26:
//#line 66 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta el ';'");}
break;
case 27:
//#line 67 "gramatica.y"
{ yyerror("Error en parametros de funcion: Falta una ',' o tipo invalido"); }
break;
case 31:
//#line 77 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'tipo''");}
break;
case 32:
//#line 78 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'ID''");}
break;
case 34:
//#line 83 "gramatica.y"
{ yyerror ("Declaracion de clase: Falta el BEGIN");}
break;
case 35:
//#line 84 "gramatica.y"
{ yyerror("Declaracion de clase: Falta el END");}
break;
case 36:
//#line 85 "gramatica.y"
{ yyerror("Declaracion de clase: Falta el ';'");}
break;
case 45:
//#line 106 "gramatica.y"
{System.out.println("Sentencia EXTENDS");}
break;
case 46:
//#line 107 "gramatica.y"
{ yyerror("Error en la lista de IDs de EXTENDS: Falta una ','"); }
break;
case 47:
//#line 108 "gramatica.y"
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 48:
//#line 109 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 51:
//#line 118 "gramatica.y"
{System.out.println("METODO");}
break;
case 52:
//#line 119 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el BEGIN");}
break;
case 53:
//#line 120 "gramatica.y"
{ yyerror("Error en parametros de metodo: Falta una ',' o tipo invalido"); }
break;
case 54:
//#line 121 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el END");}
break;
case 55:
//#line 122 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el ';'");}
break;
case 57:
//#line 127 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'");}
break;
case 60:
//#line 137 "gramatica.y"
{System.out.println("Declaracion Asignacion");}
break;
case 61:
//#line 138 "gramatica.y"
{System.out.println("Declaracion Seleccion");}
break;
case 62:
//#line 139 "gramatica.y"
{System.out.println("Declaracion Salida");}
break;
case 63:
//#line 140 "gramatica.y"
{System.out.println("Declaracion Retorno");}
break;
case 64:
//#line 141 "gramatica.y"
{System.out.println("Declaracion Iterativa");}
break;
case 65:
//#line 142 "gramatica.y"
{ yyerror("Asignacion: Falta el ';' final de la asignacion o un operador");}
break;
case 66:
//#line 143 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");}
break;
case 67:
//#line 144 "gramatica.y"
{ yyerror("Salida: Falta el ';'");}
break;
case 68:
//#line 145 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");}
break;
case 69:
//#line 146 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");}
break;
case 71:
//#line 151 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
break;
case 79:
//#line 170 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 80:
//#line 171 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 81:
//#line 172 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 82:
//#line 173 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 86:
//#line 180 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 87:
//#line 181 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 88:
//#line 182 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 89:
//#line 183 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 93:
//#line 190 "gramatica.y"
{System.out.println("Acceso posicional");}
break;
case 94:
//#line 191 "gramatica.y"
{System.out.println("Asignacion EXP");}
break;
case 95:
//#line 192 "gramatica.y"
{System.out.println("Conversion DF");}
break;
case 97:
//#line 197 "gramatica.y"
{System.out.println("Metodo|funcion con orden opcional");}
break;
case 98:
//#line 198 "gramatica.y"
{System.out.println("Objeto con orden opcional");}
break;
case 99:
//#line 199 "gramatica.y"
{ yyerror("Error en los parametros: Falta una ',' o hay un elemento inválido"); }
break;
case 100:
//#line 200 "gramatica.y"
{ yyerror("Error en los parametros del objeto: Falta una ',' o hay un elemento inválido"); }
break;
case 102:
//#line 206 "gramatica.y"
{ yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); }
break;
case 110:
//#line 226 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion");}
break;
case 111:
//#line 227 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa");}
break;
case 112:
//#line 228 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa");}
break;
case 113:
//#line 229 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa");}
break;
case 114:
//#line 230 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion");}
break;
case 117:
//#line 236 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); }
break;
case 118:
//#line 237 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion");}
break;
case 119:
//#line 238 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); }
break;
case 120:
//#line 239 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion");}
break;
case 121:
//#line 243 "gramatica.y"
{System.out.println("Condicion");}
break;
case 127:
//#line 247 "gramatica.y"
{System.out.println("Comparador");}
break;
case 128:
//#line 251 "gramatica.y"
{System.out.println("BloqueEJ1");}
break;
case 129:
//#line 252 "gramatica.y"
{System.out.println("BloqueEJ2");}
break;
case 130:
//#line 253 "gramatica.y"
{ yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
break;
case 131:
//#line 254 "gramatica.y"
{ yyerror("Declaracion de bloque ejecutable: Falta el END ");}
break;
case 132:
//#line 258 "gramatica.y"
{System.out.println("POUT cadena");}
break;
case 133:
//#line 259 "gramatica.y"
{System.out.println("POUT expresion");}
break;
case 134:
//#line 260 "gramatica.y"
{yyerror("Falta Argumento en POUT");}
break;
//#line 1093 "Parser.java"
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
