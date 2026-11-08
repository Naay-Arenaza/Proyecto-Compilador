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
   19,   19,   20,   20,   25,   25,   25,   27,   27,   27,
   27,   27,   28,   28,   26,   26,   26,   26,   26,   26,
   26,   29,   29,   29,   29,   29,   29,   29,   30,   30,
   30,   30,   30,   30,   32,   32,   32,   32,   31,   31,
   31,   31,   31,   31,   31,   34,   34,   34,   34,   34,
   34,   34,   35,   35,   35,   35,   35,   35,   33,   33,
   33,   33,   33,   37,   37,   37,   38,   38,   36,   36,
   36,   36,   39,   24,   24,   24,   24,   24,   24,   24,
   21,   21,   21,   21,   21,   21,   41,   42,   42,   42,
   42,   42,   42,   40,   40,   40,   40,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   23,   23,
};
final static short yylen[] = {                            2,
    5,    5,    4,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    2,
    1,   11,   11,   11,   11,   11,   11,   10,   11,    3,
    1,    2,    1,    1,    6,    6,    6,    5,    2,    1,
    1,    0,    2,    1,    1,    1,    3,    3,    2,    2,
    3,    1,    2,    9,    9,    9,    9,    8,    3,    2,
    2,    1,    2,    2,    2,    2,    2,    1,    1,    1,
    1,    1,    3,    3,    1,    1,    3,    4,    3,    4,
    5,    5,    1,    1,    3,    3,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    1,    1,
    1,    5,    1,    1,    4,    4,    3,    2,    3,    3,
    3,    3,    3,    3,    1,    3,    3,    3,    3,    3,
    3,    1,    1,    1,    1,    1,    1,    5,    5,    7,
    5,    4,    6,    3,    3,    0,    3,    1,    3,    1,
    2,    4,    1,    6,    6,    5,    5,    4,    6,    6,
    8,    6,    5,    5,    4,    6,    3,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    3,    4,    4,    3,
    3,    3,    2,    3,    3,    2,    4,    3,
};
final static short yydefred[] = {                         0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
    0,    0,    0,    0,    0,   62,    0,    0,    0,    0,
    0,    0,   76,   21,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   84,    0,    0,    0,    0,    0,    0,
    0,  100,    0,    0,    0,  101,    0,   98,  103,  104,
    0,    0,    0,    0,    0,    0,  164,    0,    3,   61,
   63,   64,   65,   66,   67,    0,    0,   20,   59,    0,
   83,    2,    0,    0,    0,   16,    0,    0,   12,    0,
   79,    0,   77,    4,    1,  178,    0,    0,    0,    0,
    0,    0,    0,    0,  108,    0,    0,  158,  159,  160,
  161,    0,    0,  162,  163,    0,    0,    0,    0,    0,
    0,  175,    0,  170,    0,  172,    0,    0,    0,    0,
    0,    0,    0,   19,    0,   45,    0,    0,    0,   41,
   44,   46,    0,   15,   14,    0,    0,    0,   80,    0,
   78,  177,    0,    0,    0,   95,   96,    0,    0,    0,
  140,    0,    0,    0,  107,    0,    0,    0,    0,    0,
    0,    0,    0,   94,   92,   97,   93,    0,  155,  168,
  169,    0,  166,  167,  165,    0,    0,    0,    0,   52,
   50,    0,    0,    0,   39,   43,    0,    0,   33,    0,
    0,   31,    0,    0,    0,   81,   82,    0,  132,    0,
    0,    0,  141,    0,    0,  124,  125,    0,  126,  127,
    0,  122,    0,  106,  105,    0,  153,  154,    0,    0,
    0,    0,  147,   48,   53,   47,    0,    0,   36,   37,
   35,   32,    0,    6,    6,    6,    6,    6,    0,  138,
    0,  131,    0,  139,  129,    0,    0,    0,    0,    0,
  102,    0,    0,    0,    0,    0,    0,  156,    0,  152,
  149,  145,  150,  144,   51,    0,    0,   30,    0,    0,
    0,    0,    0,  135,    0,  134,  142,    0,    0,    0,
  119,  120,    0,    0,    0,    0,    0,  118,  116,  121,
  117,  133,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  137,    0,  130,  151,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  128,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   25,   23,   29,   24,   26,   27,   22,   56,   55,   57,
   54,
};
final static short yydgoto[] = {                          3,
    4,  128,   12,   13,   14,   15,   16,   17,   18,   35,
  201,  202,  138,  139,  140,  141,  142,  192,   26,   27,
   28,   29,   30,   31,   32,   55,   33,   46,   57,   58,
  218,   59,   60,  221,  222,  160,  209,  251,  161,   68,
   61,  116,
};
final static short yysindex[] = {                       118,
    0,    0,    0,  785,  768, -251, 1067,    0,    0, -225,
  -60,    0,    0,    0,    0,    0,    0, -227,  -69, 1067,
  -39,  -24,    2,  913,  979,    0,   14,   20,   34,   45,
   69,  -43,    0,    0,   63,  -67,  986,  -84, -251,  -21,
  133,   12,    0,    0,   53,  -58,  920,  125, -231,  437,
   24,    0,  -30, -231,  409,    0,   84,    0,    0,    0,
  374,   13,  -38,  297,  967, 1067,    0, -212,    0,    0,
    0,    0,    0,    0,    0, -231, -231,    0,    0, -114,
    0,    0, -211, -211,   42,    0,  135,  -29,    0,   72,
    0,  -64,    0,    0,    0,    0,  473,  -71,  -71, -168,
 -168,  104,  154,   23,    0,  142,  514,    0,    0,    0,
    0,  208,  263,    0,    0, -231,  266,  301, 1067,  932,
  -55,    0,  187,    0,  688,    0,  197,  997,  939,  231,
   10,  106,  106,    0,   38,    0,   39,  -13, -211,    0,
    0,    0,  -45,    0,    0,   30,   30,  282,    0,  -52,
    0,    0,  288,   84,   84,    0,    0,  190,  106,   28,
    0,  319,  275,  864,    0,  713,  932,   59,  288,   84,
  288,   84,  106,    0,    0,    0,    0,   70,    0,    0,
    0, -231,    0,    0,    0, -231,  329,  281,  298,    0,
    0,   19,  326,  304,    0,    0,  355,  359,    0,  172,
   61,    0,  171,  406,  -32,    0,    0,  200,    0,  190,
  332,  190,    0,  791,   68,    0,    0,  810,    0,    0,
  323,    0,  169,    0,    0, -108,    0,    0,  431,  432,
  895,  440,    0,    0,    0,    0,  193,  458,    0,    0,
    0,    0,   30,    0,    0,    0,    0,    0,  390,    0,
  -16,    0,  284,    0,    0,  439,  439, -148, -148,  451,
    0,  460,  541,  610,  614,  190,   21,    0,  932,    0,
    0,    0,    0,    0,    0,  461,  256,    0,  792,  799,
  809,  816,  386,    0,  249,    0,    0,  394,  323,  323,
    0,    0, -231,  394,  323,  394,  323,    0,    0,    0,
    0,    0,  190,  248,  265,  -78, 1067, 1067, 1067, 1067,
 1067, 1067,    0,  965,    0,    0, 1067, 1067, 1067, 1004,
 1015, 1022, 1033, 1040,  951,    0, 1051, 1058,  958,  469,
  475,  490,  494,  497,  501,  504,  506,  510,  515,  519,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -42,    0,
    0,    0,    0,    0,    0,    0,  828,  845,  862,  879,
  896,    0,    0,    0,  751,  -42,    0,    0,    0,    0,
    0,  697,  358,    0,    0,    0,    0,    0,    0,    0,
   76,    0,    0,    0,    0,    0,  136,    0,    0,    0,
    0,  521,    0,  539,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  271,  192,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  557,    0,  574,    0,    0,    0,    0,    0,
    0,  591,  608,    0,    0,    0,    0,    0,  206,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  163,  229,    0,    0,  111,  314,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  401,  433,
  467,  492,  336,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  628,    0,    0,
    0,  214,  305,    0,    0,    0,    0,  717,    0,  -27,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  111,
    0,  111,    0,    0, 1062,    0,    0,    0,    0,    0,
 1045,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  647,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  111,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, 1079, 1096,
    0,    0,    0, 1113, 1190, 1290, 1298,    0,    0,    0,
    0,    0,  111,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  734,    0,    0,    0, -122,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    6,   -5,    0,   -9,    0,    0,    0,    0,  116,   37,
 -101,  330,  495,    0,  443,  444,    0,    0,   -4,    0,
    0,    0,    0,    0,    0,  -10,  -18,  546,  703,  430,
    0,  670,  687,  625,  666,  372,  247,    0, -120,  348,
  -20,    0,
};
final static int YYTABLESIZE=1343;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
   49,   37,  124,   56,   56,   34,  206,    5,  248,  106,
  148,  243,   64,   34,   47,   54,   34,   77,   75,   67,
   70,   45,   80,   45,   50,   51,   52,  285,  151,   34,
   56,   38,   70,  107,   93,   56,   41,   86,   97,  213,
  207,   63,   70,  130,   56,  203,  205,   40,   53,  187,
    8,    9,  125,  122,   42,   80,   67,   56,   56,  131,
  129,  303,  237,  102,  211,  132,  133,  135,  212,  104,
   89,  211,   71,  136,  136,   85,  286,  236,   72,   56,
   56,   56,   56,   56,  103,   80,  210,   56,   51,   52,
  254,  159,   73,   56,   56,  166,  191,   56,   56,   56,
  145,  244,   67,   74,  243,  173,   80,  102,  215,  216,
  188,   53,   56,  104,   45,   67,   99,   99,   99,   99,
   99,   79,   99,   70,   70,  117,   39,   75,  260,  136,
  118,   53,  287,   58,   99,   99,  277,   99,   58,   58,
   58,   56,  134,  217,  158,   91,  213,  268,  112,  159,
  113,  136,  136,  136,  136,  136,   58,  136,   45,  269,
  270,  229,   67,   56,  149,  230,  232,   56,   56,  136,
  136,   83,  136,   42,  146,   84,   91,  318,   91,   91,
   91,  319,  165,   96,  153,   51,   52,   43,   44,   81,
   44,  150,   56,  162,   91,   91,   34,   91,  137,  137,
  159,    8,    9,   88,   56,   88,   88,   88,   53,  266,
  197,  245,  159,  179,  243,  198,   48,   50,   51,   52,
  123,   88,   88,  247,   88,  105,  147,  180,   34,   76,
   75,   50,   51,   52,   56,   78,  182,  217,  217,  217,
  217,   53,  159,  217,  217,  217,  217,  194,   56,  279,
  280,  281,  282,  283,  137,   53,  159,   50,   51,   52,
   62,  200,  200,  200,   67,   50,   51,   52,   78,   90,
  186,   90,   90,   90,   56,  235,   50,   51,   52,  163,
  208,   53,  314,   50,   51,   52,  199,   90,   90,   53,
   90,    8,    9,  189,  190,  193,  306,  144,   78,  243,
   53,  320,  321,  322,  323,  324,  325,   53,   90,   81,
   44,  327,  328,  329,  223,   70,   70,   70,   70,   70,
   70,  233,   70,   70,   70,  100,   98,  227,   99,  100,
  101,   99,   99,   99,  101,   99,   99,  126,  228,  112,
   99,  113,   99,   99,   99,   99,   99,   99,   21,   99,
   99,   99,   99,  200,  143,   99,  234,  143,  200,   50,
   51,   52,  239,   21,  264,  238,  136,  136,  136,  265,
  136,  136,  143,    1,    2,  136,  157,  136,  136,  136,
  136,  136,  136,   53,  136,  136,  136,  136,   87,   88,
  136,   91,   91,   91,  157,   91,   91,  164,   51,   52,
   91,   21,   91,   91,   91,   91,   91,   91,  121,   91,
   91,   91,   91,  240,  120,   91,   21,  241,   88,   88,
   88,   53,   88,   88,   50,   51,   52,   88,  242,   88,
   88,   88,   88,   88,   88,  258,   88,   88,   88,   88,
  259,   87,   88,   87,   87,   87,  246,   42,   53,  275,
   83,  112,   42,  113,  168,  249,  252,  250,  255,   87,
   87,   40,   87,  169,   51,   52,   40,  178,  115,   49,
  114,  271,  272,   85,   49,   85,   85,   85,  100,   98,
  274,   99,  284,  101,   90,   90,   90,   53,   90,   90,
  293,   85,   85,   90,   85,   90,   90,   90,   90,   90,
   90,  305,   90,   90,   90,   90,  313,   89,   90,   89,
   89,   89,  302,  152,  226,  112,  316,  113,  171,   51,
   52,  174,   51,   52,  317,   89,   89,  341,   89,  156,
  157,   42,   86,  342,   86,   86,   86,  204,  199,   50,
   51,   52,   53,    8,    9,   53,  175,  177,  343,  315,
   86,   86,  344,   86,  167,  345,  176,   51,   52,  346,
   21,   21,  347,   53,  348,   21,   21,   21,  349,  143,
  143,  143,  278,  350,  214,  215,  216,  351,  143,  176,
   53,  195,  196,   21,  231,   51,   52,  253,   51,   52,
   92,  157,  157,  143,  267,  157,  157,  173,   53,    0,
  157,    0,  157,  157,  157,  157,  157,  157,   53,    0,
    0,   53,    0,    0,   21,  174,  304,   21,    0,   21,
   21,    0,   21,   21,   21,    0,    0,   21,   21,  119,
   36,    0,  171,   66,    0,   21,    0,    0,   21,    0,
   22,  311,    6,   23,   24,  312,    0,    8,    9,   73,
    0,   10,    0,    0,    0,    0,   87,   87,   87,    0,
   87,   87,    0,   11,    0,   87,   74,   87,   87,   87,
   87,   87,   87,    0,   87,   87,   87,   87,    0,    0,
   87,    0,  108,  109,  110,  111,  148,    0,   85,   85,
   85,    0,   85,   85,  288,  215,  216,   85,    0,   85,
   85,   85,   85,   85,   85,  146,   85,   85,   85,   85,
    0,    0,   85,  276,  199,  294,  215,  216,   53,    8,
    9,    0,   89,   89,   89,    0,   89,   89,  181,    0,
  112,   89,  113,   89,   89,   89,   89,   89,   89,   53,
   89,   89,   89,   89,    0,    0,   89,   86,   86,   86,
    0,   86,   86,  225,    0,  112,   86,  113,   86,   86,
   86,   86,   86,   86,    0,   86,   86,   86,   86,  119,
   36,   86,    0,   66,    0,    0,  176,  176,   21,    0,
   22,  176,    0,   23,   24,  176,    0,  176,  176,  176,
  176,  176,  176,    0,  173,  173,  296,  215,  216,  173,
  154,  155,    0,  173,    0,  173,  173,  173,  173,  173,
  173,    0,  174,  174,  170,  172,    0,  174,    0,    0,
   53,  174,    0,  174,  174,  174,  174,  174,  174,  171,
  171,  219,  258,  256,  171,  257,    0,  259,  171,    0,
  171,  171,  171,  171,  171,  171,   73,   73,  220,    0,
  261,   73,  262,    0,  263,   73,    0,   73,   73,   73,
   73,   73,   73,   74,   74,  298,  215,  216,   74,  300,
  215,  216,   74,    0,   74,   74,   74,   74,   74,   74,
  289,  290,    0,  148,  148,    0,  295,  297,  148,   53,
    0,    0,  148,   53,  148,  148,  148,  148,  148,  148,
    0,    0,  146,  146,  224,  100,   98,  146,   99,    0,
  101,  146,    0,  146,  146,  146,  146,  146,  146,    0,
    0,    0,    0,  291,  292,  219,  219,  219,  219,  299,
  301,  219,  219,  219,  219,  273,  100,   98,    0,   99,
    0,  101,  220,  220,  220,  220,    0,    0,  220,  220,
  220,  220,   13,    0,    0,    0,   13,   13,   13,   13,
    0,   13,   13,   13,    0,    0,   13,   13,    0,    0,
    0,    0,   38,   38,   13,   13,   38,    0,   38,   38,
    0,   38,   38,   38,    0,    0,   38,   38,    0,   28,
   28,    0,    0,   28,   38,   28,   28,    0,   28,   28,
   28,    0,    0,   28,   28,  326,   60,  112,    0,  113,
   60,   28,   60,   60,    0,   60,   60,   60,    0,    0,
   60,   60,    0,    0,   19,    0,    0,   20,   60,    8,
    9,    0,   21,   10,   22,    0,    0,   23,   24,    0,
    0,    6,    0,    0,    7,   11,    8,    9,    6,    0,
   10,  307,    0,    8,    9,    6,    0,   10,  308,    0,
    8,    9,   11,    0,   10,    6,    0,    0,  309,   11,
    8,    9,    6,    0,   10,  310,   11,    8,    9,    0,
    0,   10,    0,   68,   68,  115,   11,  115,   68,  115,
    0,    0,   68,   11,   68,   68,   68,   68,   68,   68,
   69,   69,  123,  123,  123,   69,  123,    0,  123,   69,
    0,   69,   69,   69,   69,   69,   69,   70,   70,  112,
    0,  112,   70,  112,    0,    0,   70,    0,   70,   70,
   70,   70,   70,   70,   71,   71,  114,    0,  114,   71,
  114,    0,    0,   71,    0,   71,   71,   71,   71,   71,
   71,   72,   72,  111,    0,  111,   72,  111,    0,    0,
   72,    0,   72,   72,   72,   72,   72,   72,   65,   36,
    0,    0,   66,    0,    0,   94,   36,   21,    0,   22,
   95,    0,   23,   24,   21,    0,   22,  119,   36,   23,
   24,   66,    0,    0,  184,   36,   21,    0,   22,  185,
    0,   23,   24,   21,    0,   22,  335,   36,   23,   24,
    0,  336,    0,  339,   36,   21,    0,   22,  340,    0,
   23,   24,   21,   36,   22,    0,    0,   23,   24,    0,
  109,   21,  109,   22,  109,   36,   23,   24,  127,   69,
    0,    0,   36,   21,    0,   22,   82,    0,   23,   24,
   21,    0,   22,   36,    0,   23,   24,  183,    0,    0,
   36,   21,    0,   22,  330,    0,   23,   24,   21,    0,
   22,   36,    0,   23,   24,  331,    0,    0,   36,   21,
    0,   22,  332,    0,   23,   24,   21,    0,   22,   36,
    0,   23,   24,  333,    0,    0,   36,   21,    0,   22,
  334,    0,   23,   24,   21,    0,   22,   36,    0,   23,
   24,  337,    0,    0,   36,   21,    0,   22,  338,    0,
   23,   24,   21,   36,   22,    0,    0,   23,   24,    0,
  113,   21,  113,   22,  113,    0,   23,   24,  110,    0,
  110,    0,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
   40,    7,   41,   22,   23,  257,   59,    2,   41,   40,
   40,   44,   23,   41,   20,   40,   44,   61,   61,   24,
   25,   91,   44,   91,  256,  257,  258,   44,   93,  257,
   49,  257,   37,   54,   93,   54,  264,   59,   49,  160,
   93,   40,   47,  256,   63,  147,  148,   11,  280,   40,
  262,  263,   63,   41,   18,   44,   61,   76,   77,  272,
   66,   41,   44,   40,   44,   76,   77,  279,   41,   46,
   59,   44,   59,   83,   84,   39,   93,   59,   59,   98,
   99,  100,  101,  102,   61,   44,   59,  106,  257,  258,
  211,  102,   59,  112,  113,  106,   59,  116,  117,  118,
   59,   41,  107,   59,   44,  116,   44,   40,  257,  258,
  131,  280,  131,   46,   91,  120,   41,   42,   43,   44,
   45,   59,   47,  128,  129,   42,   11,   59,   61,  139,
   47,  280,  253,  256,   59,   60,  238,   62,  261,  262,
  263,  160,  257,  162,   41,   93,  267,  256,   43,  160,
   45,   41,   42,   43,   44,   45,  279,   47,   91,  268,
  269,  182,  167,  182,   93,  186,  187,  186,  187,   59,
   60,  256,   62,  137,   40,  260,   41,  256,   43,   44,
   45,  260,   41,   59,  256,  257,  258,  257,  258,  257,
  258,  256,  211,   40,   59,   60,  257,   62,   83,   84,
  211,  262,  263,   41,  223,   43,   44,   45,  280,   41,
  256,   41,  223,  269,   44,  261,  256,  256,  257,  258,
  259,   59,   60,  256,   62,  256,  256,   41,  256,  273,
  273,  256,  257,  258,  253,  257,   40,  256,  257,  258,
  259,  280,  253,  262,  263,  264,  265,  261,  267,  244,
  245,  246,  247,  248,  139,  280,  267,  256,  257,  258,
  259,  146,  147,  148,  269,  256,  257,  258,  257,   41,
   40,   43,   44,   45,  293,  257,  256,  257,  258,  257,
   91,  280,  293,  256,  257,  258,  257,   59,   60,  280,
   62,  262,  263,  256,  257,  257,   41,  256,  257,   44,
  280,  307,  308,  309,  310,  311,  312,  280,  256,  257,
  258,  317,  318,  319,   40,  320,  321,  322,  323,  324,
  325,   41,  327,  328,  329,   42,   43,  269,   45,   42,
   47,  256,  257,  258,   47,  260,  261,   41,  269,   43,
  265,   45,  267,  268,  269,  270,  271,  272,   44,  274,
  275,  276,  277,  238,   41,  280,   59,   44,  243,  256,
  257,  258,   59,   59,   42,   40,  256,  257,  258,   47,
  260,  261,   59,  256,  257,  265,   41,  267,  268,  269,
  270,  271,  272,  280,  274,  275,  276,  277,  256,  257,
  280,  256,  257,  258,   59,  260,  261,  256,  257,  258,
  265,   44,  267,  268,  269,  270,  271,  272,   61,  274,
  275,  276,  277,   59,   41,  280,   59,   59,  256,  257,
  258,  280,  260,  261,  256,  257,  258,  265,  257,  267,
  268,  269,  270,  271,  272,   42,  274,  275,  276,  277,
   47,   41,  280,   43,   44,   45,   41,  256,  280,  257,
   93,   43,  261,   45,  107,  256,  210,  258,  212,   59,
   60,  256,   62,  256,  257,  258,  261,  120,   60,  256,
   62,   41,   41,   41,  261,   43,   44,   45,   42,   43,
   41,   45,   93,   47,  256,  257,  258,  280,  260,  261,
   40,   59,   60,  265,   62,  267,  268,  269,  270,  271,
  272,   41,  274,  275,  276,  277,  258,   41,  280,   43,
   44,   45,  266,   41,  167,   43,  269,   45,  256,  257,
  258,  256,  257,  258,  260,   59,   60,   59,   62,  100,
  101,  261,   41,   59,   43,   44,   45,  256,  257,  256,
  257,  258,  280,  262,  263,  280,  117,  118,   59,  303,
   59,   60,   59,   62,   41,   59,  256,  257,  258,   59,
  256,  257,   59,  280,   59,  261,  262,  263,   59,  256,
  257,  258,  243,   59,  256,  257,  258,   59,   84,   59,
  280,  139,  139,  279,  256,  257,  258,  256,  257,  258,
   45,  256,  257,  280,  223,  260,  261,   59,  280,   -1,
  265,   -1,  267,  268,  269,  270,  271,  272,  280,   -1,
   -1,  280,   -1,   -1,  257,   59,  269,  260,   -1,  262,
  263,   -1,  265,  266,  267,   -1,   -1,  270,  271,  256,
  257,   -1,   59,  260,   -1,  278,   -1,   -1,  265,   -1,
  267,  256,  257,  270,  271,  260,   -1,  262,  263,   59,
   -1,  266,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
  260,  261,   -1,  278,   -1,  265,   59,  267,  268,  269,
  270,  271,  272,   -1,  274,  275,  276,  277,   -1,   -1,
  280,   -1,  274,  275,  276,  277,   59,   -1,  256,  257,
  258,   -1,  260,  261,  256,  257,  258,  265,   -1,  267,
  268,  269,  270,  271,  272,   59,  274,  275,  276,  277,
   -1,   -1,  280,  256,  257,  256,  257,  258,  280,  262,
  263,   -1,  256,  257,  258,   -1,  260,  261,   41,   -1,
   43,  265,   45,  267,  268,  269,  270,  271,  272,  280,
  274,  275,  276,  277,   -1,   -1,  280,  256,  257,  258,
   -1,  260,  261,   41,   -1,   43,  265,   45,  267,  268,
  269,  270,  271,  272,   -1,  274,  275,  276,  277,  256,
  257,  280,   -1,  260,   -1,   -1,  256,  257,  265,   -1,
  267,  261,   -1,  270,  271,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,  256,  257,  256,  257,  258,  261,
   98,   99,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,   -1,  256,  257,  112,  113,   -1,  261,   -1,   -1,
  280,  265,   -1,  267,  268,  269,  270,  271,  272,  256,
  257,  162,   42,   43,  261,   45,   -1,   47,  265,   -1,
  267,  268,  269,  270,  271,  272,  256,  257,  162,   -1,
   41,  261,   43,   -1,   45,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,  256,  257,  258,  261,  256,
  257,  258,  265,   -1,  267,  268,  269,  270,  271,  272,
  256,  257,   -1,  256,  257,   -1,  262,  263,  261,  280,
   -1,   -1,  265,  280,  267,  268,  269,  270,  271,  272,
   -1,   -1,  256,  257,   41,   42,   43,  261,   45,   -1,
   47,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
   -1,   -1,   -1,  258,  259,  256,  257,  258,  259,  264,
  265,  262,  263,  264,  265,   41,   42,   43,   -1,   45,
   -1,   47,  256,  257,  258,  259,   -1,   -1,  262,  263,
  264,  265,  256,   -1,   -1,   -1,  260,  261,  262,  263,
   -1,  265,  266,  267,   -1,   -1,  270,  271,   -1,   -1,
   -1,   -1,  256,  257,  278,  279,  260,   -1,  262,  263,
   -1,  265,  266,  267,   -1,   -1,  270,  271,   -1,  256,
  257,   -1,   -1,  260,  278,  262,  263,   -1,  265,  266,
  267,   -1,   -1,  270,  271,   41,  256,   43,   -1,   45,
  260,  278,  262,  263,   -1,  265,  266,  267,   -1,   -1,
  270,  271,   -1,   -1,  257,   -1,   -1,  260,  278,  262,
  263,   -1,  265,  266,  267,   -1,   -1,  270,  271,   -1,
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
   -1,   -1,  260,   -1,   -1,  256,  257,  265,   -1,  267,
  261,   -1,  270,  271,  265,   -1,  267,  256,  257,  270,
  271,  260,   -1,   -1,  256,  257,  265,   -1,  267,  261,
   -1,  270,  271,  265,   -1,  267,  256,  257,  270,  271,
   -1,  261,   -1,  256,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,  257,  267,   -1,   -1,  270,  271,   -1,
   41,  265,   43,  267,   45,  257,  270,  271,  272,  261,
   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,
  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,
  257,  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,
  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,  257,
   -1,  270,  271,  261,   -1,   -1,  257,  265,   -1,  267,
  261,   -1,  270,  271,  265,   -1,  267,  257,   -1,  270,
  271,  261,   -1,   -1,  257,  265,   -1,  267,  261,   -1,
  270,  271,  265,  257,  267,   -1,   -1,  270,  271,   -1,
   41,  265,   43,  267,   45,   -1,  270,  271,   41,   -1,
   43,   -1,   45,
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

//#line 327 "gramatica.y"
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
//#line 783 "Parser.java"
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
    }
break;
case 74:
//#line 158 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
break;
case 76:
//#line 163 "gramatica.y"
{System.out.println("Declaracion de acceso_posicional");}
break;
case 77:
//#line 164 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de apertura "); }
break;
case 79:
//#line 169 "gramatica.y"
{ yyerror("Acceso posicional: Índice vacío"); }
break;
case 80:
//#line 170 "gramatica.y"
{ yyerror("Acceso posicional: Índice inválido"); }
break;
case 81:
//#line 171 "gramatica.y"
{ yyerror("Acceso posicional: Falta corchete de cierre"); }
break;
case 82:
//#line 172 "gramatica.y"
{ yyerror("Acceso posicional: Argumento invalido"); }
break;
case 87:
//#line 182 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 88:
//#line 183 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 89:
//#line 184 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 90:
//#line 185 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 94:
//#line 192 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 95:
//#line 193 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 96:
//#line 194 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 97:
//#line 195 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 101:
//#line 202 "gramatica.y"
{System.out.println("Acceso posicional");}
break;
case 102:
//#line 203 "gramatica.y"
{System.out.println("Asignacion EXP");}
break;
case 105:
//#line 209 "gramatica.y"
{System.out.println("Conversion DF");}
break;
case 106:
//#line 210 "gramatica.y"
{yyerror("Solo se pueden expresiones");}
break;
case 107:
//#line 211 "gramatica.y"
{yyerror("Falta expresion");}
break;
case 108:
//#line 212 "gramatica.y"
{yyerror("Falta parentesis");}
break;
case 111:
//#line 218 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 112:
//#line 219 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 113:
//#line 220 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 114:
//#line 221 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 118:
//#line 228 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 119:
//#line 229 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 120:
//#line 230 "gramatica.y"
{yyerror("Falta operando 1");}
break;
case 121:
//#line 231 "gramatica.y"
{yyerror("Falta operando 2");}
break;
case 128:
//#line 241 "gramatica.y"
{yyerror("No se permite este anidamiento, solo 1 expresion");}
break;
case 129:
//#line 245 "gramatica.y"
{System.out.println("Metodo|funcion con orden opcional");}
break;
case 130:
//#line 246 "gramatica.y"
{System.out.println("Objeto con orden opcional");}
break;
case 131:
//#line 247 "gramatica.y"
{ yyerror("Error en los parametros: Falta un ')'"); }
break;
case 132:
//#line 248 "gramatica.y"
{ yyerror("Lista de parametros vacia."); }
break;
case 133:
//#line 249 "gramatica.y"
{ 
        yyerror("Lista de parametros vacia."); expresionInvalida = true;
     }
break;
case 135:
//#line 256 "gramatica.y"
{ yyerror("Error en la lista de constantes: Falta una ',' o hay un elemento inválido"); }
break;
case 141:
//#line 268 "gramatica.y"
{yyerror("Falta ',' entre argumentos");}
break;
case 142:
//#line 269 "gramatica.y"
{yyerror("Elemento no valido despues de ','");}
break;
case 144:
//#line 277 "gramatica.y"
{System.out.println("Declaracion Iterativa WHILE");}
break;
case 145:
//#line 278 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion");}
break;
case 146:
//#line 279 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa");}
break;
case 147:
//#line 280 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa");}
break;
case 148:
//#line 281 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa");}
break;
case 149:
//#line 282 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion");}
break;
case 150:
//#line 283 "gramatica.y"
{ yyerror("error en condicion de iteracion"); }
break;
case 151:
//#line 287 "gramatica.y"
{System.out.println("Declaracion Seleccion");}
break;
case 152:
//#line 288 "gramatica.y"
{System.out.println("Declaracion Seleccion Simple");}
break;
case 153:
//#line 289 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); }
break;
case 154:
//#line 290 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion");}
break;
case 155:
//#line 291 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); }
break;
case 156:
//#line 292 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion");}
break;
case 157:
//#line 296 "gramatica.y"
{System.out.println("Condicion");}
break;
case 163:
//#line 300 "gramatica.y"
{System.out.println("Comparador");}
break;
case 164:
//#line 304 "gramatica.y"
{System.out.println("BloqueEJ1");}
break;
case 165:
//#line 305 "gramatica.y"
{System.out.println("BloqueEJ2");}
break;
case 166:
//#line 306 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
break;
case 167:
//#line 307 "gramatica.y"
{yyerror("Declaracion de bloque ejecutable: Falta el END ");}
break;
case 168:
//#line 311 "gramatica.y"
{System.out.println("POUT cadena");}
break;
case 169:
//#line 312 "gramatica.y"
{System.out.println("POUT expresion");}
break;
case 170:
//#line 313 "gramatica.y"
{yyerror("Falta Argumento en POUT");}
break;
case 171:
//#line 314 "gramatica.y"
{yyerror ("Falta parentesis de cierre");}
break;
case 172:
//#line 315 "gramatica.y"
{yyerror ("Falta parentesis de apertura");}
break;
case 173:
//#line 316 "gramatica.y"
{yyerror ("Falta parentesis");}
break;
case 174:
//#line 317 "gramatica.y"
{yyerror ("Falta parentesis de cierre");}
break;
case 175:
//#line 318 "gramatica.y"
{yyerror ("Falta parentesis de apertura");}
break;
case 176:
//#line 319 "gramatica.y"
{yyerror ("Falta parentesis");}
break;
case 177:
//#line 323 "gramatica.y"
{System.out.println("Declaracion Retorno");}
break;
case 178:
//#line 324 "gramatica.y"
{yyerror("Retorno debe tener una expresion y parentesis");}
break;
//#line 1398 "Parser.java"
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
