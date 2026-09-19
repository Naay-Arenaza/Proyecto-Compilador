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
   10,    6,    6,    6,    6,    6,   11,   11,   11,   12,
   12,   12,    7,    7,    7,    7,   13,   13,   13,   13,
   14,   14,   16,   16,   15,   15,   15,   18,   18,   18,
   17,   17,   17,   17,    8,    8,    2,    2,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   20,   20,
   25,   25,   27,   28,   28,   26,   26,   26,   26,   26,
   26,   26,   29,   29,   29,   29,   29,   29,   30,   30,
   30,   30,   30,   30,   31,   31,   33,   33,   34,   34,
   34,   32,   32,   32,   35,   24,   24,   24,   24,   24,
   24,   21,   21,   21,   21,   21,   21,   37,   38,   38,
   38,   38,   36,   36,   36,   36,   22,   22,   22,   23,
};
final static short yylen[] = {                            2,
    5,    5,    5,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    2,
    1,   11,   11,   11,   11,   10,    3,    3,    1,    2,
    1,    1,    6,    6,    6,    5,    2,    1,    1,    0,
    2,    1,    1,    1,    3,    2,    2,    3,    2,    1,
    9,    9,    9,    8,    3,    2,    2,    1,    2,    2,
    2,    2,    2,    1,    1,    1,    1,    1,    3,    3,
    1,    1,    4,    1,    1,    3,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    3,    1,    1,    1,
    1,    5,    4,    1,    5,    7,    3,    0,    3,    2,
    1,    3,    3,    1,    1,    6,    6,    5,    5,    4,
    5,    8,    6,    5,    5,    4,    6,    3,    1,    1,
    1,    1,    1,    3,    3,    3,    4,    4,    3,    4,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
   21,    0,    0,    0,    0,    0,    0,    0,   58,    0,
    0,    0,    0,    0,    0,   72,    0,    0,    0,    0,
    0,    0,    0,   20,   55,    0,    0,    0,    0,    0,
   90,    0,    0,    0,   91,    0,   88,   94,    0,    0,
    0,    0,    0,  123,    0,    2,   57,   59,   60,   61,
   62,   63,    0,    0,    0,    0,    0,   16,    0,    0,
   12,    3,    4,    1,   19,   74,   75,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  119,  120,  121,
  122,    0,    0,    0,    0,    0,    0,    0,    0,  129,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   43,
    0,    0,    0,   39,   42,   44,    0,   15,   14,    0,
    0,   73,  130,    0,    0,    0,   86,    0,    0,  104,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   85,   83,   87,   84,    0,  116,  127,  128,  125,  126,
  124,    0,    0,    0,    0,   50,   47,    0,    0,    0,
   37,   41,    0,    0,   31,    0,    0,   29,    0,    0,
    0,    0,    0,    0,   93,    0,  114,  115,  111,    0,
    0,  109,   49,   45,    0,    0,   34,   35,   33,   30,
    0,    0,    6,    6,  103,  102,    0,   95,   92,    0,
  117,    0,  113,  107,  106,   48,    0,   28,   27,    0,
    0,  101,    0,    0,    0,    0,    0,    0,    0,  100,
    0,   97,   96,  112,    0,    0,    0,    0,    0,   99,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
   24,   25,   22,   52,   53,   51,
};
final static short yydgoto[] = {                          3,
    4,   28,   12,   13,   14,   15,   16,   17,  176,   41,
  177,  178,  122,  123,  124,  125,  126,  168,   29,   30,
   31,   32,   33,   34,   35,   54,   36,   88,   56,   57,
   58,  139,  208,  223,  140,   65,   59,  104,
};
final static short yysindex[] = {                       -98,
    0,    0,    0,  439,  234, -243,  -75,    0,    0, -237,
  -63,    0,    0,    0,    0,    0,    0, -247,  -75,  -75,
    0,    4,  -68,   24,  -40,   40,  548,  609,    0,  -15,
   46,   49,   57,   85,  -59,    0, -154, -243,   15,   61,
   48,  616,  560,    0,    0, -163,  121, -215,  404,   20,
    0,   93, -215,  184,    0,   62,    0,    0,  228,  -34,
  -75,  -75,  134,    0, -234,    0,    0,    0,    0,    0,
    0,    0, -215, -215, -251, -251,   56,    0,  137,  144,
    0,    0,    0,    0,    0,    0,    0,  100,  319, -170,
 -170, -168, -215,  151,  -55, -215,  249,    0,    0,    0,
    0, -134, -120, -215, -105, -102,  572,  -62,  172,    0,
  363,  627,  579, -215,  207,  -37,   27,   27,  -54,    0,
   19,   22, -251,    0,    0,    0, -116,    0,    0,   77,
   77,    0,    0,  256,   62,   62,    0,   27,  -28,    0,
 -215,  287,  388,  572,   66,  256,   62,  256,   62,   27,
    0,    0,    0,    0,   69,    0,    0,    0,    0,    0,
    0,  301, -215, -215,  308,    0,    0,   59,  317,  302,
    0,    0,  312,  323,    0,  127,  -26,    0,  -17, -215,
 -215,  295,  412, -215,    0, -142,    0,    0,    0,  352,
  360,    0,    0,    0,  148,   77,    0,    0,    0,    0,
   77,   77,    0,    0,    0,    0,  157,    0,    0,   -5,
    0,  572,    0,    0,    0,    0,   -4,    0,    0,  446,
  282,    0,  -43,  295,  153, -131,  -75,  -75,  -75,    0,
  182,    0,    0,    0,  -75,  -75,  634,  645,  591,    0,
  652,  598,  371,  389,  395,  397,  403,  405,  408,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  410,    1,    0,    0,    0,    0,    0,    0,  462,
  479,  496,  513,  531,    0,    0,    0,    0,    0,    0,
  -90,    0,    0,    0,    0,    0,    0,    0,    0,  -12,
    0,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  202,  -94,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  261,  297,    0,    0,
    0,    0,  -52,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   54,   76,    0,    5,    0,    0,
    0,    0,    0,    0,    0,   98,  120,  142,  167,  209,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  324,    0,    0,   35,  -25,    0,
    0,    0,    0,  385,    0,   17,    0,    0,    0,    0,
    0,   10,    0,    0,    0,    0,    0,    0,    0,    0,
  341,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   10,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  402,    0,    0,  189,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    2,    6,    0,  -67,    0,    0,    0,    0,   74,   90,
 -125,  219,  399,    0,  350,  359,    0,    0,  444,    0,
    0,    0,    0,    0,    0,  557,  530,    0,   78,   42,
    0,  299,  265,    0,  245,  -14,  -32,    0,
};
final static int YYTABLESIZE=923;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
  231,   74,  164,    5,  167,  179,  110,  120,  120,   21,
    8,    9,  182,   21,  203,  181,   40,  202,   21,   37,
   97,  115,   47,  204,   42,   43,  202,  119,   89,   89,
   89,   89,   89,   21,   89,  224,  226,  116,  181,  202,
   49,   50,   51,   68,  108,  105,   89,   46,  105,  232,
   98,   98,   98,   98,   98,  120,   98,   32,   46,   93,
   32,   71,   45,   48,   52,   95,  112,  113,   98,  102,
  217,  103,   82,   78,   82,   82,   82,   18,   18,   60,
   94,  162,  145,  165,   38,  134,   50,   51,   50,   51,
   82,   46,  155,   85,   79,   22,   79,   79,   79,   46,
   39,   75,  195,  105,   69,   76,   81,   70,  106,   52,
   47,   52,   79,  211,  129,   71,   81,  194,   81,   81,
   81,  146,   50,   51,  235,  212,  213,   77,  236,  186,
  190,  191,   96,  137,   81,  148,   50,   51,   78,  173,
   78,   78,   78,   72,  174,   52,  152,  154,  121,  121,
  151,   50,   51,  153,   50,   51,   78,    1,    2,   52,
   76,   40,   76,   76,   76,   13,   40,  135,  136,   13,
   13,   13,   13,  114,   52,   13,  130,   52,   76,  147,
  149,   23,   80,  131,   80,   80,   80,   13,   13,   24,
  141,   25,  132,   21,   26,   27,  121,  225,    8,    9,
   80,  142,  166,   38,  220,  221,  156,   77,   38,   77,
   77,   77,  157,   73,  230,   49,   50,   51,   49,   50,
   51,   49,   50,   51,  109,   77,  102,  180,  103,  201,
   21,   21,  237,  238,  239,   21,   21,   21,  201,   52,
  241,  242,   52,   89,   89,   52,  163,   89,   89,  118,
  180,  201,   89,   21,   89,   89,   89,   89,   89,   89,
  105,   89,   89,   89,   89,   98,   98,  118,  107,   98,
   98,   44,   32,   71,   98,  169,   98,   98,   98,   98,
   98,   98,  170,   98,   98,   98,   98,   82,   82,  144,
   46,   82,   82,   18,   18,   46,   82,   92,   82,   82,
   82,   82,   82,   82,   44,   82,   82,   82,   82,   79,
   79,  128,   44,   79,   79,  193,   79,   80,   79,   69,
   79,   79,   79,   79,   79,   79,  184,   79,   79,   79,
   79,   81,   81,  175,  187,   81,   81,  188,    8,    9,
   81,  189,   81,   81,   81,   81,   81,   81,  192,   81,
   81,   81,   81,   78,   78,   70,  196,   78,   78,  133,
  197,  102,   78,  103,   78,   78,   78,   78,   78,   78,
  198,   78,   78,   78,   78,   76,   76,   86,   87,   76,
   76,  199,  110,  200,   76,  207,   76,   76,   76,   76,
   76,   76,  214,   76,   76,   76,   76,   80,   80,  108,
  215,   80,   80,  158,  216,  102,   80,  103,   80,   80,
   80,   80,   80,   80,  222,   80,   80,   80,   80,  218,
  219,  234,   77,   77,  205,  206,   77,   77,  185,  250,
  102,   77,  103,   77,   77,   77,   77,   77,   77,  240,
   77,   77,   77,   77,   54,   92,   90,  251,   91,   54,
   54,   54,  209,  252,  102,  253,  103,   98,   99,  100,
  101,  254,   40,  255,  118,  118,  256,   54,  118,  118,
   64,   67,  171,  118,  127,  118,  118,  118,  118,  118,
  118,  172,  210,   61,   23,   67,   67,   62,  233,   19,
    6,    0,   24,   20,   25,    8,    9,   26,   27,   10,
    0,    0,   64,    0,   61,   23,    0,    0,   62,    0,
    0,   11,    0,   24,    0,   25,   69,   69,   26,   27,
    0,   69,    0,    0,    0,   69,    0,   69,   69,   69,
   69,   69,   69,    0,    0,    0,    0,  228,    6,    0,
   64,  229,    0,    8,    9,    0,    0,   10,    0,    0,
   64,    0,   70,   70,   55,   67,   67,   70,    0,   11,
    0,   70,    0,   70,   70,   70,   70,   70,   70,    0,
    0,    0,    0,    0,    0,    0,    0,   55,    0,  110,
  110,    0,   55,    0,  110,    0,    0,   64,  110,   55,
  110,  110,  110,  110,  110,  110,  108,  108,    0,    0,
    0,  108,   55,   55,   89,  108,    0,  108,  108,  108,
  108,  108,  108,    0,    0,    0,  111,    0,    0,   55,
   55,   55,   55,    0,    0,   55,    0,    0,    0,  117,
  118,   55,   55,   55,   55,   55,    0,    0,    0,    0,
   36,   36,    0,   55,   36,   55,   36,   36,    0,  138,
   36,    0,  143,    0,    0,   64,    0,   26,   26,    0,
  150,   26,   36,   26,   26,   56,    0,   26,    0,   56,
   55,   56,   56,    0,    0,   56,    0,    0,    0,   26,
   67,   67,   67,    0,   67,   67,    0,   56,    0,    0,
    0,    0,   55,   55,    0,    6,    0,  183,    7,    0,
    8,    9,    6,    0,   10,  227,    0,    8,    9,   55,
   55,   10,    0,   55,    0,    0,   11,   64,   64,    0,
    0,    0,   64,   11,    0,    0,   64,    0,   64,   64,
   64,   64,   64,   64,   65,   65,  138,  138,    0,   65,
  138,    0,    0,   65,    0,   65,   65,   65,   65,   65,
   65,   66,   66,    0,    0,    0,   66,    0,    0,    0,
   66,    0,   66,   66,   66,   66,   66,   66,   67,   67,
    0,    0,    0,   67,    0,    0,    0,   67,    0,   67,
   67,   67,   67,   67,   67,    0,   68,   68,    0,    0,
    0,   68,    0,    0,    0,   68,    0,   68,   68,   68,
   68,   68,   68,   61,   23,    0,    0,   62,    0,    0,
    0,    0,   24,    0,   25,   83,   23,   26,   27,   63,
   84,    0,    0,    0,   24,    0,   25,   61,   23,   26,
   27,   62,    0,    0,  160,   23,   24,    0,   25,  161,
    0,   26,   27,   24,    0,   25,  245,   23,   26,   27,
    0,  246,    0,  248,   23,   24,    0,   25,  249,    0,
   26,   27,   24,    0,   25,   23,    0,   26,   27,   66,
    0,    0,   23,   24,    0,   25,   82,    0,   26,   27,
   24,    0,   25,   23,    0,   26,   27,  159,    0,    0,
   23,   24,    0,   25,  243,    0,   26,   27,   24,    0,
   25,   23,    0,   26,   27,  244,    0,    0,   23,   24,
    0,   25,  247,    0,   26,   27,   24,    0,   25,    0,
    0,   26,   27,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   44,   61,   40,    2,   59,  131,   41,   75,   76,  257,
  262,  263,   41,  257,   41,   44,  264,   44,   44,  257,
   53,  256,   91,   41,   19,   20,   44,  279,   41,   42,
   43,   44,   45,   59,   47,   41,   41,  272,   44,   44,
  256,  257,  258,   59,   59,   41,   59,   44,   44,   93,
   41,   42,   43,   44,   45,  123,   47,   41,   44,   40,
   44,   61,   59,   40,  280,   46,   61,   62,   59,   43,
  196,   45,   41,   59,   43,   44,   45,    4,    5,   40,
   61,  114,   97,  116,   11,  256,  257,  258,  257,  258,
   59,   44,  107,  257,   41,    6,   43,   44,   45,   44,
   11,  256,   44,   42,   59,  260,   59,   59,   47,  280,
   91,  280,   59,  256,   59,   59,   41,   59,   43,   44,
   45,  256,  257,  258,  256,  268,  269,   38,  260,  144,
  163,  164,   40,   92,   59,  256,  257,  258,   41,  256,
   43,   44,   45,   59,  261,  280,  105,  106,   75,   76,
  256,  257,  258,  256,  257,  258,   59,  256,  257,  280,
   41,  256,   43,   44,   45,  256,  261,   90,   91,  260,
  261,  262,  263,   40,  280,  266,   40,  280,   59,  102,
  103,  257,   41,   40,   43,   44,   45,  278,  279,  265,
   40,  267,   93,  257,  270,  271,  123,  212,  262,  263,
   59,  257,  257,  256,  203,  204,  269,   41,  261,   43,
   44,   45,   41,  273,  258,  256,  257,  258,  256,  257,
  258,  256,  257,  258,  259,   59,   43,  256,   45,  256,
  256,  257,  227,  228,  229,  261,  262,  263,  256,  280,
  235,  236,  280,  256,  257,  280,   40,  260,  261,   41,
  256,  256,  265,  279,  267,  268,  269,  270,  271,  272,
  256,  274,  275,  276,  277,  256,  257,   59,   41,  260,
  261,  257,  256,  273,  265,  257,  267,  268,  269,  270,
  271,  272,  261,  274,  275,  276,  277,  256,  257,   41,
  256,  260,  261,  220,  221,  261,  265,   42,  267,  268,
  269,  270,  271,  272,  257,  274,  275,  276,  277,  256,
  257,  256,  257,  260,  261,  257,  256,  257,  265,   59,
  267,  268,  269,  270,  271,  272,   40,  274,  275,  276,
  277,  256,  257,  257,  269,  260,  261,  269,  262,  263,
  265,   41,  267,  268,  269,  270,  271,  272,   41,  274,
  275,  276,  277,  256,  257,   59,   40,  260,  261,   41,
   59,   43,  265,   45,  267,  268,  269,  270,  271,  272,
   59,  274,  275,  276,  277,  256,  257,  257,  258,  260,
  261,   59,   59,  257,  265,   91,  267,  268,  269,  270,
  271,  272,   41,  274,  275,  276,  277,  256,  257,   59,
   41,  260,  261,   41,  257,   43,  265,   45,  267,  268,
  269,  270,  271,  272,  258,  274,  275,  276,  277,  201,
  202,  269,  256,  257,  180,  181,  260,  261,   41,   59,
   43,  265,   45,  267,  268,  269,  270,  271,  272,  258,
  274,  275,  276,  277,  256,   42,   43,   59,   45,  261,
  262,  263,   41,   59,   43,   59,   45,  274,  275,  276,
  277,   59,  261,   59,  256,  257,   59,  279,  260,  261,
   27,   28,  123,  265,   76,  267,  268,  269,  270,  271,
  272,  123,  184,  256,  257,   42,   43,  260,  224,  256,
  257,   -1,  265,  260,  267,  262,  263,  270,  271,  266,
   -1,   -1,   59,   -1,  256,  257,   -1,   -1,  260,   -1,
   -1,  278,   -1,  265,   -1,  267,  256,  257,  270,  271,
   -1,  261,   -1,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,   -1,   -1,   -1,  256,  257,   -1,
   97,  260,   -1,  262,  263,   -1,   -1,  266,   -1,   -1,
  107,   -1,  256,  257,   25,  112,  113,  261,   -1,  278,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   48,   -1,  256,
  257,   -1,   53,   -1,  261,   -1,   -1,  144,  265,   60,
  267,  268,  269,  270,  271,  272,  256,  257,   -1,   -1,
   -1,  261,   73,   74,   48,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,   -1,   -1,   60,   -1,   -1,   90,
   91,   92,   93,   -1,   -1,   96,   -1,   -1,   -1,   73,
   74,  102,  103,  104,  105,  106,   -1,   -1,   -1,   -1,
  256,  257,   -1,  114,  260,  116,  262,  263,   -1,   93,
  266,   -1,   96,   -1,   -1,  212,   -1,  256,  257,   -1,
  104,  260,  278,  262,  263,  256,   -1,  266,   -1,  260,
  141,  262,  263,   -1,   -1,  266,   -1,   -1,   -1,  278,
  237,  238,  239,   -1,  241,  242,   -1,  278,   -1,   -1,
   -1,   -1,  163,  164,   -1,  257,   -1,  141,  260,   -1,
  262,  263,  257,   -1,  266,  260,   -1,  262,  263,  180,
  181,  266,   -1,  184,   -1,   -1,  278,  256,  257,   -1,
   -1,   -1,  261,  278,   -1,   -1,  265,   -1,  267,  268,
  269,  270,  271,  272,  256,  257,  180,  181,   -1,  261,
  184,   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,   -1,   -1,   -1,  261,   -1,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,  256,  257,
   -1,   -1,   -1,  261,   -1,   -1,   -1,  265,   -1,  267,
  268,  269,  270,  271,  272,   -1,  256,  257,   -1,   -1,
   -1,  261,   -1,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,   -1,   -1,  260,   -1,   -1,
   -1,   -1,  265,   -1,  267,  256,  257,  270,  271,  272,
  261,   -1,   -1,   -1,  265,   -1,  267,  256,  257,  270,
  271,  260,   -1,   -1,  256,  257,  265,   -1,  267,  261,
   -1,  270,  271,  265,   -1,  267,  256,  257,  270,  271,
   -1,  261,   -1,  256,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,   -1,  267,  257,   -1,  270,  271,  261,
   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,
  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,
  257,  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,
  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,   -1,
   -1,  270,  271,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,null,
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
"parametros_formales : parametros_formales ',' parametro_formal",
"parametros_formales : parametros_formales error parametro_formal",
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
"sentencia_extends : EXTENDS lista_ids",
"sentencia_extends : EXTENDS ';'",
"lista_ids : lista_ids ',' ID",
"lista_ids : lista_ids ID",
"lista_ids : ID",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';'",
"declaracion_metodo : tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';'",
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
"lista_constantes_opcional : '[' lista_constantes ']'",
"lista_constantes_opcional :",
"lista_constantes : lista_constantes ',' CTE",
"lista_constantes : lista_constantes CTE",
"lista_constantes : CTE",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : parametros_reales error parametro_real",
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
"bloque_ejecutable : sentencia_ejecutable",
"bloque_ejecutable : BEGIN sentencias_ejecutables END",
"bloque_ejecutable : error sentencias_ejecutables END",
"bloque_ejecutable : BEGIN sentencias_ejecutables error",
"salida : POUT '(' CADENA ')'",
"salida : POUT '(' expresion ')'",
"salida : POUT '(' ')'",
"retorno : RET '(' expresion ')'",
};

//#line 263 "gramatica.y"
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
//#line 609 "Parser.java"
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
{ yyerror("Declaracion de funcion: Falta de nombre de programa");}
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
case 28:
//#line 71 "gramatica.y"
{ System.out.println("Parametros formales: Falta la ,");}
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
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 47:
//#line 108 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 49:
//#line 113 "gramatica.y"
{ yyerror("Lista ids: Falta la ','");}
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
{ yyerror("Declaracion de metodo: Falta el END");}
break;
case 54:
//#line 121 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el ';'");}
break;
case 56:
//#line 126 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'");}
break;
case 59:
//#line 136 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 60:
//#line 137 "gramatica.y"
{System.out.println("Seleccion");}
break;
case 61:
//#line 138 "gramatica.y"
{System.out.println("Salida");}
break;
case 62:
//#line 139 "gramatica.y"
{System.out.println("Retorno");}
break;
case 63:
//#line 140 "gramatica.y"
{System.out.println("Iterativa");}
break;
case 64:
//#line 141 "gramatica.y"
{ yyerror("Asignacion: Falta el ';'");}
break;
case 65:
//#line 142 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");}
break;
case 66:
//#line 143 "gramatica.y"
{ yyerror("Salida: Falta el ';'");}
break;
case 67:
//#line 144 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");}
break;
case 68:
//#line 145 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");}
break;
case 70:
//#line 150 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
break;
case 78:
//#line 169 "gramatica.y"
{yyerror("Falta operando");}
break;
case 79:
//#line 170 "gramatica.y"
{yyerror("Falta operando");}
break;
case 80:
//#line 171 "gramatica.y"
{yyerror("Falta operando");}
break;
case 81:
//#line 172 "gramatica.y"
{yyerror("Falta operando");}
break;
case 85:
//#line 179 "gramatica.y"
{yyerror("Falta operando");}
break;
case 86:
//#line 180 "gramatica.y"
{yyerror("Falta operando");}
break;
case 87:
//#line 181 "gramatica.y"
{yyerror("Falta operando");}
break;
case 91:
//#line 188 "gramatica.y"
{System.out.println("Acceso posicional");}
break;
case 92:
//#line 189 "gramatica.y"
{System.out.println("Asignacion EXP");}
break;
case 93:
//#line 190 "gramatica.y"
{System.out.println("Conversion DF");}
break;
case 95:
//#line 195 "gramatica.y"
{System.out.println("Metodo|funcion con orden opcional");}
break;
case 96:
//#line 196 "gramatica.y"
{System.out.println("Objeto con orden opcional");}
break;
case 100:
//#line 206 "gramatica.y"
{ yyerror("Lista constantes: Falta la ','");}
break;
case 103:
//#line 212 "gramatica.y"
{ yyerror("Parametro Reales: Falta la ,");}
break;
case 107:
//#line 222 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion");}
break;
case 108:
//#line 223 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa");}
break;
case 109:
//#line 224 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa");}
break;
case 110:
//#line 225 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa");}
break;
case 111:
//#line 226 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion");}
break;
case 114:
//#line 232 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); }
break;
case 115:
//#line 233 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion");}
break;
case 116:
//#line 234 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); }
break;
case 117:
//#line 235 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion");}
break;
case 118:
//#line 239 "gramatica.y"
{System.out.println("Condicion");}
break;
case 122:
//#line 243 "gramatica.y"
{System.out.println("Comparador");}
break;
case 123:
//#line 247 "gramatica.y"
{System.out.println("BloqueEJ1");}
break;
case 124:
//#line 248 "gramatica.y"
{System.out.println("BloqueEJ2");}
break;
case 125:
//#line 249 "gramatica.y"
{ yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
break;
case 126:
//#line 250 "gramatica.y"
{ yyerror("Declaracion de bloque ejecutable: Falta el END ");}
break;
case 127:
//#line 254 "gramatica.y"
{System.out.println("POUT cadena");}
break;
case 128:
//#line 255 "gramatica.y"
{System.out.println("POUT expresion");}
break;
case 129:
//#line 256 "gramatica.y"
{yyerror("Falta Argumento en POUT");}
break;
//#line 1058 "Parser.java"
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
