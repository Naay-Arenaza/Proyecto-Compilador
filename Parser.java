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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    3,    3,    3,
    3,    4,    4,    5,    5,    9,    9,   10,   10,   10,
    6,    6,    6,    6,    6,   11,   11,   11,   12,   12,
   12,    7,    7,    7,    7,   13,   13,   13,   13,   14,
   14,   16,   16,   15,   15,   18,   18,   18,   17,   17,
   17,   17,    8,    8,    2,    2,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   20,   25,   25,   27,
   28,   28,   26,   26,   26,   29,   29,   29,   30,   30,
   30,   30,   30,   30,   31,   31,   33,   33,   34,   34,
   34,   32,   32,   32,   35,   24,   21,   21,   37,   38,
   38,   38,   38,   36,   36,   36,   36,   22,   22,   23,
};
final static short yylen[] = {                            2,
    5,    5,    5,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    3,    1,    1,    3,    3,    1,
   11,   11,   10,   10,   10,    3,    3,    1,    2,    1,
    1,    6,    5,    6,    6,    2,    1,    1,    0,    2,
    1,    1,    1,    3,    2,    3,    2,    1,    9,    8,
    9,    9,    3,    2,    2,    1,    2,    1,    2,    1,
    2,    1,    2,    1,    2,    1,    3,    1,    1,    4,
    1,    1,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    5,    4,    1,    5,    7,    3,    0,    3,    3,
    1,    3,    3,    1,    1,    6,    8,    6,    3,    1,
    1,    1,    1,    1,    3,    3,    3,    4,    4,    4,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   16,   17,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
   20,    0,    0,    0,    0,    0,    0,    0,   56,    0,
    0,    0,    0,    0,    0,   69,    0,    0,    0,    0,
    0,    0,    0,   53,    0,    0,    0,    0,    0,    0,
    0,  104,    0,    2,   55,   57,   59,   61,   63,   65,
    0,    0,    0,    0,    0,    0,   12,    3,    4,    1,
   19,   18,   71,   72,    0,    0,   80,    0,    0,   81,
    0,   78,   84,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   42,    0,    0,    0,   38,   41,   43,    0,
   14,    0,    0,   70,    0,    0,    0,    0,  110,    0,
    0,    0,    0,  100,  101,  102,  103,    0,    0,  108,
  109,  106,  107,  105,    0,   48,    0,    0,    0,   36,
   40,    0,    0,   31,    0,    0,   28,    0,    0,    0,
   94,    0,    0,    0,    0,    0,   76,   77,    0,    0,
    0,   47,   44,    0,    0,   34,   35,   32,   29,    0,
    0,    6,    6,    0,    0,    0,    0,    0,   83,    0,
   98,   96,   46,    0,   27,   26,    0,    0,   93,   92,
    0,   85,   82,    0,    0,    0,    0,    0,    0,    0,
   91,    0,    0,   97,    0,    0,    0,    0,    0,    0,
    0,   87,   86,    0,    0,    0,    0,   24,   23,   90,
   89,    0,    0,    0,   22,   21,   51,   52,   49,
};
final static short yydgoto[] = {                          3,
    4,   28,   12,   13,   14,   15,   16,   17,  135,   22,
  136,  137,   95,   96,   97,   98,   99,  127,   29,   30,
   31,   32,   33,   34,   35,  139,   36,   75,   81,   82,
   83,  140,  182,  192,  141,   53,   85,  118,
};
final static short yysindex[] = {                       -55,
    0,    0,    0,  205, -147, -238,  104,    0,    0, -198,
  -57,    0,    0,    0,    0,    0,    0, -221,  104,  104,
    0,    5,  -28,   34,   62,   81,  303,  340,    0,   28,
   45,   65,   71,   74, -125,    0, -215, -238,  -44,   13,
  347,  310, -104,    0, -102,   53, -197, -197, -182,  104,
  104,    0, -127,    0,    0,    0,    0,    0,    0,    0,
 -197, -228, -228,   27,  122,  137,    0,    0,    0,    0,
    0,    0,    0,    0,   67,   -9,    0,  144,  120,    0,
  -26,    0,    0,   54,  125,  149,  246,  358,  322,  153,
   58,  -59,    0,  -16,  -18, -228,    0,    0,    0, -156,
    0, -116, -116,    0, -197,  207,   19, -197,    0, -197,
 -197, -197, -197,    0,    0,    0,    0, -197,  303,    0,
    0,    0,    0,    0, -197,    0,  -32,  258,  229,    0,
    0,  247,  254,    0,   75,  -34,    0,  -33,   58,  -24,
    0, -197,  296,  264,  -26,  -26,    0,    0,   58,  -50,
  312,    0,    0,  100, -116,    0,    0,    0,    0, -116,
 -116,    0,    0, -197, -197,  268,  294, -197,    0,  303,
    0,    0,    0,  -12,    0,    0,  212, -128,    0,    0,
  110,    0,    0,  -11,  101, -175,  104,  -82,  104,  365,
    0,  -39,  268,    0,  104,  104,  376,   93,  313,  115,
  119,    0,    0,  383,  329,  320,  321,    0,    0,    0,
    0,  323,  331,  335,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  171,   89,    0,    0,    0,    0,    0,    0,  -89,
  228,  248,  269,  286,    0,    0,    0,    0,    0,  126,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  134, -139,  188,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    3,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   77,    0,    0,    0,    0, -136,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -112,   64,    0,    0,
    0,    0,  -71,    0,   -2,    0,    0,    0,    9,    0,
    0,    0,    0,    0,   25,   47,    0,    0,  357,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -19,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   89,    0,    0,
    0,    0,  -19,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  154,    0,    0,    0,
    0,    0,    0, -183,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   11,  189,    0,   -7,    0,    0,    0,    0,  107,   20,
  -88,  102,  318,    0,  304,  305,    0,    0,  324,    0,
    0,    0,    0,    0,    0,   79,   46,    0,  156,  243,
    0,  231,  209,    0,  176, -105,  278,    0,
};
final static int YYTABLESIZE=654;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         79,
   79,   79,   79,   79,  201,   79,  162,  163,   46,  161,
  161,  154,    5,  150,  138,  112,  166,   79,   21,  165,
  113,   88,   88,   88,   88,   88,  153,   88,  186,  193,
  105,  161,  165,    8,    9,   21,  107,   40,   30,   88,
   62,   30,   39,   75,   63,   75,   75,   75,   45,   95,
   92,  106,   95,  202,   93,   93,   45,   64,   37,   76,
   77,   75,   46,   44,  185,   73,  174,   73,   73,   73,
   45,   67,   50,   47,   76,   77,   86,   50,   50,   50,
  195,   46,   78,   73,  196,  101,   56,   74,   93,   74,
   74,   74,   80,   80,   80,   50,  110,   78,  111,  132,
  110,   48,  111,   57,  133,   74,   80,   20,   19,    6,
   18,   18,   20,   40,    8,    9,   39,   38,   10,   37,
   49,   39,   20,   58,   37,   79,   84,   87,  188,   59,
   11,  189,   60,    8,    9,   67,   24,   10,   25,   91,
  134,   26,   27,   45,   90,    8,    9,   61,   45,   11,
   80,  208,   71,   80,   72,   80,   80,   80,   80,  104,
  109,  102,  110,   80,  111,  119,   58,   58,   94,   94,
   80,   58,  177,  178,   21,   58,  103,   58,   58,   58,
   58,   58,   58,  108,   33,   33,  144,   80,   33,  120,
   33,   33,  125,   33,   33,   33,  149,  126,   33,   33,
    1,    2,   94,   84,    8,    9,   33,   41,   42,   80,
   80,   65,   66,   80,   79,   79,  200,  170,  171,   79,
  167,  160,  160,   79,  152,   79,   79,   79,   79,   79,
   79,  164,   79,   79,   79,   79,   88,   88,   88,   89,
  128,   88,  129,  160,  164,   88,  142,   88,   88,   88,
   88,   88,   88,   30,   88,   88,   88,   88,   75,   75,
   43,  175,  176,   75,   95,  145,  146,   75,   43,   75,
   75,   75,   75,   75,   75,  143,   75,   75,   75,   75,
   73,   73,   43,   18,   18,   73,  121,  156,  110,   73,
  111,   73,   73,   73,   73,   73,   73,  155,   73,   73,
   73,   73,   74,   74,  169,  157,  110,   74,  111,   73,
   74,   74,  158,   74,   74,   74,   74,   74,   74,   20,
   74,   74,   74,   74,   20,   20,   20,  114,  115,  116,
  117,  159,   67,   67,  183,  168,  110,   67,  111,  179,
  180,   67,   20,   67,   67,   67,   67,   67,   67,   23,
   52,   55,  172,  207,  147,  148,  173,   24,  181,   25,
   23,   68,   26,   27,   55,   55,  190,  191,   24,  194,
   25,  209,  210,   26,   27,  197,  211,  198,  215,  216,
  100,  217,   13,  204,  205,   13,   13,   13,   13,  218,
   13,   13,   13,  219,   39,   13,   13,   99,  184,  130,
  131,  203,  151,   13,   13,    0,    0,    0,    0,   25,
   25,   55,   55,   25,    0,   25,   25,    0,   25,   25,
   25,    0,    0,   25,   25,    0,    0,   54,    0,    0,
   54,   25,   54,   54,    0,   54,   54,   54,    0,    0,
   54,   54,   52,    0,   15,    0,    0,   15,   54,   15,
   15,    0,   15,   15,   15,    0,    0,   15,   15,    0,
    0,    6,    0,    0,    7,   15,    8,    9,    6,    0,
   10,  187,    0,    8,    9,    0,    0,   10,    0,    0,
    0,    0,   11,   60,   60,    0,    0,    0,   60,   11,
    0,    0,   60,   52,   60,   60,   60,   60,   60,   60,
    0,    0,    0,   62,   62,    0,    0,    0,   62,    0,
    0,    0,   62,   55,   62,   62,   62,   62,   62,   62,
   55,   55,    0,    0,   64,   64,    0,   55,   55,   64,
    0,    0,    0,   64,    0,   64,   64,   64,   64,   64,
   64,   66,   66,    0,    0,    0,   66,    0,    0,    0,
   66,    0,   66,   66,   66,   66,   66,   66,   50,   23,
    0,    0,   51,    0,    0,   69,   23,   24,    0,   25,
   70,    0,   26,   27,   24,    0,   25,  123,   23,   26,
   27,    0,  124,    0,  213,   23,   24,    0,   25,  214,
    0,   26,   27,   24,    0,   25,   23,    0,   26,   27,
   54,    0,    0,   23,   24,    0,   25,   68,    0,   26,
   27,   24,    0,   25,   23,    0,   26,   27,  122,    0,
    0,   23,   24,    0,   25,  199,    0,   26,   27,   24,
    0,   25,   23,    0,   26,   27,  206,    0,    0,   23,
   24,    0,   25,  212,    0,   26,   27,   24,    0,   25,
    0,    0,   26,   27,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   44,   47,   41,   41,   91,   44,
   44,   44,    2,  119,  103,   42,   41,   59,  257,   44,
   47,   41,   42,   43,   44,   45,   59,   47,   41,   41,
   40,   44,   44,  262,  263,  257,   46,   18,   41,   59,
  256,   44,  264,   41,  260,   43,   44,   45,   44,   41,
  279,   61,   44,   93,   62,   63,   44,   38,  257,  257,
  258,   59,   91,   59,  170,   41,  155,   43,   44,   45,
   44,   59,  256,   40,  257,  258,  259,  261,  262,  263,
  256,   91,  280,   59,  260,   59,   59,   41,   96,   43,
   44,   45,   47,   48,   49,  279,   43,  280,   45,  256,
   43,   40,   45,   59,  261,   59,   61,   44,  256,  257,
    4,    5,  260,   94,  262,  263,  256,   11,  266,  256,
   40,  261,   59,   59,  261,   47,   48,   49,  257,   59,
  278,  260,   59,  262,  263,   59,  265,  266,  267,   61,
  257,  270,  271,  256,  272,  262,  263,  273,  261,  278,
  105,   59,  257,  108,  257,  110,  111,  112,  113,   93,
   41,   40,   43,  118,   45,   41,  256,  257,   62,   63,
  125,  261,  162,  163,  257,  265,   40,  267,  268,  269,
  270,  271,  272,   40,  256,  257,  108,  142,  260,   41,
  262,  263,   40,  265,  266,  267,  118,  257,  270,  271,
  256,  257,   96,  125,  262,  263,  278,   19,   20,  164,
  165,  256,  257,  168,  256,  257,  256,  268,  269,  261,
  142,  256,  256,  265,  257,  267,  268,  269,  270,  271,
  272,  256,  274,  275,  276,  277,  256,  257,   50,   51,
  257,  261,  261,  256,  256,  265,   40,  267,  268,  269,
  270,  271,  272,  256,  274,  275,  276,  277,  256,  257,
  256,  160,  161,  261,  256,  110,  111,  265,  256,  267,
  268,  269,  270,  271,  272,  257,  274,  275,  276,  277,
  256,  257,  256,  177,  178,  261,   41,   59,   43,  265,
   45,  267,  268,  269,  270,  271,  272,   40,  274,  275,
  276,  277,  256,  257,   41,   59,   43,  261,   45,  257,
  258,  265,   59,  267,  268,  269,  270,  271,  272,  256,
  274,  275,  276,  277,  261,  262,  263,  274,  275,  276,
  277,  257,  256,  257,   41,   40,   43,  261,   45,  164,
  165,  265,  279,  267,  268,  269,  270,  271,  272,  257,
   27,   28,   41,  261,  112,  113,  257,  265,   91,  267,
  257,  273,  270,  271,   41,   42,  178,  258,  265,  269,
  267,   59,  258,  270,  271,  187,  258,  189,   59,   59,
   63,   59,  257,  195,  196,  260,  261,  262,  263,   59,
  265,  266,  267,   59,  261,  270,  271,   41,  168,   96,
   96,  193,  125,  278,  279,   -1,   -1,   -1,   -1,  256,
  257,   88,   89,  260,   -1,  262,  263,   -1,  265,  266,
  267,   -1,   -1,  270,  271,   -1,   -1,  257,   -1,   -1,
  260,  278,  262,  263,   -1,  265,  266,  267,   -1,   -1,
  270,  271,  119,   -1,  257,   -1,   -1,  260,  278,  262,
  263,   -1,  265,  266,  267,   -1,   -1,  270,  271,   -1,
   -1,  257,   -1,   -1,  260,  278,  262,  263,  257,   -1,
  266,  260,   -1,  262,  263,   -1,   -1,  266,   -1,   -1,
   -1,   -1,  278,  256,  257,   -1,   -1,   -1,  261,  278,
   -1,   -1,  265,  170,  267,  268,  269,  270,  271,  272,
   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,  261,   -1,
   -1,   -1,  265,  190,  267,  268,  269,  270,  271,  272,
  197,  198,   -1,   -1,  256,  257,   -1,  204,  205,  261,
   -1,   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  256,  257,   -1,   -1,   -1,  261,   -1,   -1,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,  256,  257,
   -1,   -1,  260,   -1,   -1,  256,  257,  265,   -1,  267,
  261,   -1,  270,  271,  265,   -1,  267,  256,  257,  270,
  271,   -1,  261,   -1,  256,  257,  265,   -1,  267,  261,
   -1,  270,  271,  265,   -1,  267,  257,   -1,  270,  271,
  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,
  271,  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,
   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,  265,
   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,
  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,
   -1,   -1,  270,  271,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=280;
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
"COMPTIME","EXTENDS","TODF",
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
"declaracion_variables_comptime : COMPTIME tipo lista_variables",
"tipo : USHORTINT",
"tipo : DOUBLEF",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables error ID",
"lista_variables : ID",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION error '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas sentencias_ejecutables END ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables ';'",
"declaracion_funcion : tipo FUNCTION ID '(' parametros_formales ')' sentencias_declarativas BEGIN sentencias_ejecutables END",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametros_formales : parametros_formales error parametro_formal",
"parametros_formales : parametro_formal",
"parametro_formal : tipo ID",
"parametro_formal : tipo",
"parametro_formal : ID",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase END ';'",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase END",
"declaracion_clase : CLASS ID error cuerpo_clase END ';'",
"declaracion_clase : CLASS ID BEGIN cuerpo_clase error ';'",
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
"lista_ids : lista_ids ',' ID",
"lista_ids : lista_ids ID",
"lista_ids : ID",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END ';'",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables END",
"declaracion_metodo : tipo ID '(' parametros_formales ')' error sentencias_ejecutables END ';'",
"declaracion_metodo : tipo ID '(' parametros_formales ')' BEGIN sentencias_ejecutables error ';'",
"declaracion_objeto : ID lista_variables ';'",
"declaracion_objeto : ID lista_variables",
"sentencias_ejecutables : sentencias_ejecutables sentencia_ejecutable",
"sentencias_ejecutables : sentencia_ejecutable",
"sentencia_ejecutable : asignacion ';'",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : seleccion ';'",
"sentencia_ejecutable : seleccion",
"sentencia_ejecutable : salida ';'",
"sentencia_ejecutable : salida",
"sentencia_ejecutable : retorno ';'",
"sentencia_ejecutable : retorno",
"sentencia_ejecutable : iterativa ';'",
"sentencia_ejecutable : iterativa",
"asignacion : lado_izquierdo ASIGNACION expresion",
"lado_izquierdo : ID",
"lado_izquierdo : acceso_posicional",
"acceso_posicional : ID '[' indice ']'",
"indice : ID",
"indice : CTE",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
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
"lista_constantes : lista_constantes error CTE",
"lista_constantes : CTE",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : parametros_reales error parametro_real",
"parametros_reales : parametro_real",
"parametro_real : expresion",
"iterativa : REPEAT bloque_ejecutable WHILE '(' condicion ')'",
"seleccion : IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable END_IF",
"seleccion : IF '(' condicion ')' bloque_ejecutable END_IF",
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
"retorno : RET '(' expresion ')'",
};

//#line 242 "gramatica.y"
/* ---------------------------------------------------------
   CÓDIGO JAVA INYECTADO
   --------------------------------------------------------- */

private AnalizadorLexico lexer;

public Parser(AnalizadorLexico lexer) {
    this.lexer = lexer;
}

private int yylex() {
    return lexer.yylex(); 
}

private void yyerror(String mensaje) {
    System.err.println("Error sintáctico: " + mensaje);
}
//#line 524 "Parser.java"
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
//#line 18 "gramatica.y"
{System.out.println("Declaracion de programa");}
break;
case 2:
//#line 19 "gramatica.y"
{ yyerror("Falta de nombre de programa");}
break;
case 3:
//#line 20 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN");}
break;
case 4:
//#line 21 "gramatica.y"
{ yyerror("Falta la palabra reservada END");}
break;
case 7:
//#line 31 "gramatica.y"
{System.out.println("Declaracion de VAR");}
break;
case 8:
//#line 32 "gramatica.y"
{System.out.println("Declaracion de comptime");}
break;
case 9:
//#line 33 "gramatica.y"
{System.out.println("Declaracion de funcion");}
break;
case 10:
//#line 34 "gramatica.y"
{System.out.println("Declaracion de clase");}
break;
case 11:
//#line 35 "gramatica.y"
{System.out.println("Declaracion de objeto");}
break;
case 13:
//#line 40 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 15:
//#line 45 "gramatica.y"
{ yyerror("Declaracion COMPTIME de variables: Falta ';' " );}
break;
case 19:
//#line 54 "gramatica.y"
{ yyerror("Declaracion lista variables: Falta ',' " );}
break;
case 22:
//#line 60 "gramatica.y"
{yyerror("Falta de nombre de funcion");}
break;
case 23:
//#line 61 "gramatica.y"
{yyerror("Falta de BEGIN de funcion");}
break;
case 24:
//#line 62 "gramatica.y"
{yyerror("Falta de END de funcion");}
break;
case 25:
//#line 63 "gramatica.y"
{yyerror("Falta de ';' de funcion");}
break;
case 27:
//#line 68 "gramatica.y"
{ yyerror("Declaracion parametro formales: Falta ',' " );}
break;
case 30:
//#line 74 "gramatica.y"
{ yyerror("Falta Nombre de Parametro formal"); }
break;
case 31:
//#line 75 "gramatica.y"
{ yyerror("Falta Nombre de Parametro formal"); }
break;
case 33:
//#line 80 "gramatica.y"
{ yyerror("Declaracion clase: Falta ';' " );}
break;
case 34:
//#line 81 "gramatica.y"
{yyerror("Falta BEGIN en declaracion_clase");}
break;
case 35:
//#line 82 "gramatica.y"
{yyerror("Falta BEGIN en declaracion_clase");}
break;
case 44:
//#line 103 "gramatica.y"
{System.out.println("Sentencia EXTENDS");}
break;
case 45:
//#line 104 "gramatica.y"
{ yyerror("Declaracion EXTENDS: Falta ';' " );}
break;
case 47:
//#line 109 "gramatica.y"
{ yyerror("Declaracion lista ids: Falta ',' " );}
break;
case 49:
//#line 114 "gramatica.y"
{System.out.println("METODO");}
break;
case 50:
//#line 115 "gramatica.y"
{ yyerror("Declaracion metodo: Falta ';' " );}
break;
case 51:
//#line 116 "gramatica.y"
{yyerror("Falta BEGIN en declaracion_metodo");}
break;
case 52:
//#line 117 "gramatica.y"
{yyerror("Falta END en declaracion_metodo");}
break;
case 54:
//#line 122 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta ';' " );}
break;
case 57:
//#line 132 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 58:
//#line 133 "gramatica.y"
{ yyerror("Declaracion sentencia ejecutable: Falta ';' " );}
break;
case 59:
//#line 134 "gramatica.y"
{System.out.println("Seleccion");}
break;
case 60:
//#line 135 "gramatica.y"
{ yyerror("Declaracion sentencia ejecutable: Falta ';' " );}
break;
case 61:
//#line 136 "gramatica.y"
{System.out.println("Salida");}
break;
case 62:
//#line 137 "gramatica.y"
{ yyerror("Declaracion sentencia ejecutable: Falta ';' " );}
break;
case 63:
//#line 138 "gramatica.y"
{System.out.println("Retorno");}
break;
case 64:
//#line 139 "gramatica.y"
{ yyerror("Declaracion sentencia ejecutable: Falta ';' " );}
break;
case 65:
//#line 140 "gramatica.y"
{System.out.println("Iterativa");}
break;
case 66:
//#line 141 "gramatica.y"
{ yyerror("Declaracion sentencia ejecutable: Falta ';' " );}
break;
case 81:
//#line 177 "gramatica.y"
{System.out.println("Acceso posicional");}
break;
case 82:
//#line 178 "gramatica.y"
{System.out.println("Asignacion EXP");}
break;
case 83:
//#line 179 "gramatica.y"
{System.out.println("Conversion DF");}
break;
case 85:
//#line 184 "gramatica.y"
{System.out.println("Metodo con orden opcional");}
break;
case 86:
//#line 185 "gramatica.y"
{System.out.println("Objeto con orden opcional");}
break;
case 90:
//#line 195 "gramatica.y"
{ yyerror("Declaracion lista constantes: Falta ',' " );}
break;
case 93:
//#line 201 "gramatica.y"
{ yyerror("Declaracion lista constantes: Falta ',' " );}
break;
case 99:
//#line 218 "gramatica.y"
{System.out.println("Condicion");}
break;
case 103:
//#line 222 "gramatica.y"
{System.out.println("Comparador");}
break;
case 104:
//#line 226 "gramatica.y"
{System.out.println("BloqueEJ1");}
break;
case 105:
//#line 227 "gramatica.y"
{System.out.println("BloqueEJ2");}
break;
case 106:
//#line 228 "gramatica.y"
{yyerror("Falta BEGIN en bloque_ejecutable");}
break;
case 107:
//#line 229 "gramatica.y"
{yyerror("Falta END en bloque_ejecutable");}
break;
case 108:
//#line 233 "gramatica.y"
{System.out.println("POUT cadena");}
break;
case 109:
//#line 234 "gramatica.y"
{System.out.println("POUT expresion");}
break;
//#line 893 "Parser.java"
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
