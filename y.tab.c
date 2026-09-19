#ifndef lint
static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";
#endif
#define YYBYACC 1
#line 2 "gramatica.y"
import java.io.*;
#line 8 "y.tab.c"
#define ID 257
#define CTE 258
#define CADENA 259
#define BEGIN 260
#define END 261
#define USHORTINT 262
#define DOUBLEF 263
#define FUNCTION 264
#define RET 265
#define CLASS 266
#define IF 267
#define ELSE 268
#define END_IF 269
#define POUT 270
#define REPEAT 271
#define WHILE 272
#define ASIGNACION 273
#define IGUAL 274
#define DISTINTO 275
#define MAYORIGUAL 276
#define MENORIGUAL 277
#define COMPTIME 278
#define EXTENDS 279
#define TODF 280
#define SIN_PUNTO_Y_COMA 281
#define YYERRCODE 256
short yylhs[] = {                                        -1,
    0,    0,    0,    0,    1,    1,    3,    3,    3,    3,
    3,    4,    4,    5,    5,    5,    9,    9,   10,   10,
   10,    6,    6,    6,    6,    6,   11,   11,   11,   12,
   12,   12,    7,    7,    7,    7,   13,   13,   13,   13,
   14,   14,   16,   16,   15,   15,   15,   18,   18,   18,
   17,   17,   17,   17,    8,    8,    2,    2,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   20,   20,
   25,   25,   27,   28,   28,   26,   26,   26,   26,   26,
   26,   26,   26,   29,   29,   29,   29,   29,   29,   29,
   30,   30,   30,   30,   30,   30,   31,   31,   33,   33,
   34,   34,   34,   32,   32,   32,   35,   24,   24,   24,
   24,   24,   24,   21,   21,   21,   21,   21,   21,   37,
   38,   38,   38,   38,   36,   36,   36,   36,   22,   22,
   22,   23,
};
short yylen[] = {                                         2,
    5,    5,    5,    5,    2,    0,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    1,    1,    3,    2,
    1,   11,   11,   11,   11,   10,    3,    3,    1,    2,
    1,    1,    6,    6,    6,    5,    2,    1,    1,    0,
    2,    1,    1,    1,    3,    2,    2,    3,    2,    1,
    9,    9,    9,    8,    3,    2,    2,    1,    2,    2,
    2,    2,    2,    1,    1,    1,    1,    1,    3,    3,
    1,    1,    4,    1,    1,    3,    3,    2,    3,    3,
    3,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    1,    5,    4,    1,    5,    7,    3,    0,
    3,    2,    1,    3,    2,    1,    1,    6,    6,    5,
    5,    4,    5,    8,    6,    5,    5,    4,    6,    3,
    1,    1,    1,    1,    1,    3,    3,    3,    4,    4,
    3,    4,
};
short yydefred[] = {                                      0,
    6,    6,    0,    0,    0,    0,    0,   17,   18,    0,
    0,    5,    7,    8,    9,   10,   11,    0,    0,    0,
   21,    0,    0,    0,    0,    0,    0,    0,   58,    0,
    0,    0,    0,    0,    0,   72,    0,    0,    0,    0,
    0,    0,    0,   20,   55,    0,    0,    0,    0,    0,
   92,    0,    0,    0,   93,    0,   90,   96,    0,    0,
    0,    0,    0,  125,    0,    2,   57,   59,   60,   61,
   62,   63,    0,    0,    0,    0,    0,   16,    0,    0,
   12,    3,    4,    1,   19,   74,   75,    0,    0,   78,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  121,
  122,  123,  124,    0,    0,    0,    0,    0,    0,    0,
    0,  131,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   43,    0,    0,    0,   39,   42,   44,    0,   15,
   14,    0,    0,   73,  132,    0,    0,    0,   87,   89,
    0,    0,  106,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   86,   84,   88,   85,    0,  118,  129,
  130,  127,  128,  126,    0,    0,    0,    0,   50,   47,
    0,    0,    0,   37,   41,    0,    0,   31,    0,    0,
   29,    0,    0,    0,  105,    0,    0,   95,    0,  116,
  117,  113,    0,    0,  111,   49,   45,    0,    0,   34,
   35,   33,   30,    0,    0,    6,    6,  104,    0,   97,
   94,    0,  119,    0,  115,  109,  108,   48,    0,   28,
   27,    0,    0,  103,    0,    0,    0,    0,    0,    0,
    0,  102,    0,   99,   98,  114,    0,    0,    0,    0,
    0,  101,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   23,   24,   25,   22,   52,   53,   51,
};
short yydgoto[] = {                                       3,
    4,   28,   12,   13,   14,   15,   16,   17,  179,   41,
  180,  181,  124,  125,  126,  127,  128,  171,   29,   30,
   31,   32,   33,   34,   35,   54,   36,   88,   56,   57,
   58,  142,  210,  225,  143,   65,   59,  106,
};
short yysindex[] = {                                   -217,
    0,    0,    0,  -72,  -79, -220,  261,    0,    0, -209,
  -50,    0,    0,    0,    0,    0,    0, -204,  261,  261,
    0,  -28,   26,   51,  -40,   79,  628,  522,    0,   82,
  104,  114,  117,  120,  -59,    0, -193, -220,  -21,  -56,
  -18,  666,  345,    0,    0, -123,  -27, -224,  492,   12,
    0,  149, -224,  159,    0,   17,    0,    0,  273,  209,
  261,  261,  168,    0, -243,    0,    0,    0,    0,    0,
    0,    0, -224, -224,  -97,  -97,   83,    0,  192,  216,
    0,    0,    0,    0,    0,    0,    0,   99,   42,    0,
 -168, -168, -148, -148, -224,  240,  -35, -224,  289,    0,
    0,    0,    0, -157, -150, -224, -142, -132,  204,   49,
  284,    0,   92,  677,  640, -224,  301,  -37,  -39,  -39,
  -52,    0,   86,  100,  -97,    0,    0,    0, -101,    0,
    0,   93,   93,    0,    0,  122,   17,   17,    0,    0,
  -39,  220,    0, -224,  324,  348,  204,   96,  122,   17,
  122,   17,  -39,    0,    0,    0,    0,  106,    0,    0,
    0,    0,    0,    0,  339, -224, -224,  340,    0,    0,
   -2,  328,  331,    0,    0,  341,  346,    0,  158,  -32,
    0,   10, -224,  315,    0,  373, -224,    0, -164,    0,
    0,    0,  384,  396,    0,    0,    0,  184,   93,    0,
    0,    0,    0,   93,   93,    0,    0,    0,  186,    0,
    0,  224,    0,  204,    0,    0,    0,    0,   25,    0,
    0,  497,  369,    0,  -43,  315,  176, -158,  261,  261,
  261,    0,  188,    0,    0,    0,  261,  261,  684,  695,
  647,    0,  702,  659,  404,  420,  426,  427,  431,  432,
  436,    0,    0,    0,    0,    0,    0,    0,
};
short yyrindex[] = {                                      0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  489,   33,    0,    0,    0,    0,    0,    0,  543,
  560,  577,  594,  611,    0,    0,    0,    0,    0,    0,
 -104,    0,    0,    0,    0,    0,    0,    0,    0,  -23,
    0,    0,    0,    0,    0,   27,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  194,  -58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  307,  327,
    0,    0,    0,    0,   44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   52,   77,    0,    0,
  245,    0,    0,    0,    0,    0,    0,    0,  102,  127,
  152,  182,  252,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  352,    0,    0,
   75,  231,    0,    0,    0,    0,  416,    0,   35,    0,
    0,    0,    0,    2,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  389,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    2,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  484,    0,    0,
 -112,    0,    0,    0,    0,    0,    0,    0,
};
short yygindex[] = {                                      0,
    3,  411,    0,  -48,    0,    0,    0,    0,    6,   24,
 -125,  111,  394,    0,  371,  372,    0,    0,  456,    0,
    0,    0,    0,    0,    0,  595,  614,    0,  561,   46,
    0,  318,  280,    0, -128,  -34,  -38,    0,
};
#define YYTABLESIZE 973
short yytable[] = {                                      53,
  233,   74,  167,  104,    5,  105,  170,  182,  206,   18,
   18,  205,  117,  185,   99,   46,   38,   91,   91,   91,
   91,   91,   46,   91,  110,   46,  122,  122,  118,   22,
   45,   49,   50,   51,   39,   91,   21,   78,    1,    2,
   81,  198,  100,  100,  100,  100,  100,   37,  100,  234,
  207,   95,   21,  205,  208,   52,  197,   97,  107,   40,
  100,   77,   75,  108,  148,  228,   76,   83,  205,   83,
   83,   83,   96,  219,  158,   32,  122,  165,   32,  168,
  123,  123,  135,  185,  104,   83,  105,  136,   50,   51,
   48,  213,   80,   71,   80,   80,   80,  237,  149,   50,
   51,  238,   47,  214,  215,  151,   50,   51,   50,   51,
   80,   52,  189,  154,   50,   51,   47,   82,   60,   82,
   82,   82,   52,  156,   50,   51,   46,  193,  194,   52,
  123,   52,  161,   85,  104,   82,  105,   52,  139,  140,
   68,  131,   79,   54,   79,   79,   79,   52,   54,   54,
   54,   13,  155,  157,  176,   13,   13,   13,   13,  177,
   79,   13,   69,   93,    8,    9,   54,   76,   94,   76,
   76,   76,   70,   13,   13,   71,   19,    6,   72,  227,
   20,  121,    8,    9,    6,   76,   10,    7,   98,    8,
    9,  134,   81,   10,   81,   81,   81,   40,   11,   79,
   80,  104,   40,  105,  169,   11,   21,  116,  222,  223,
   81,    8,    9,   73,  232,   49,   50,   51,   49,   50,
   51,  145,   77,  204,   77,   77,   77,   18,   18,   86,
   87,  132,   91,   91,   91,   44,   91,   91,   44,   52,
   77,   91,   52,   91,   91,   91,   91,   91,   91,  112,
   91,   91,   91,   91,  196,  133,   91,  100,  100,  100,
  184,  100,  100,  183,  226,  204,  100,  183,  100,  100,
  100,  100,  100,  100,   21,  100,  100,  100,  100,  144,
  204,  100,   83,   83,   83,  107,   83,   83,  107,   21,
   32,   83,  120,   83,   83,   83,   83,   83,   83,   38,
   83,   83,   83,   83,   38,   71,   83,   80,   80,   80,
  120,   80,   80,  109,  220,  221,   80,  159,   80,   80,
   80,   80,   80,   80,  160,   80,   80,   80,   80,  147,
   46,   80,   82,   82,   82,   46,   82,   82,  130,   44,
  166,   82,  172,   82,   82,   82,   82,   82,   82,  178,
   82,   82,   82,   82,    8,    9,   82,   79,   79,   79,
  173,   79,   79,  187,  190,   69,   79,  199,   79,   79,
   79,   79,   79,   79,  191,   79,   79,   79,   79,  192,
  195,   79,   76,   76,   76,   70,   76,   76,  188,  200,
  104,   76,  105,   76,   76,   76,   76,   76,   76,  201,
   76,   76,   76,   76,  202,  209,   76,   81,   81,   81,
  112,   81,   81,  211,  203,  104,   81,  105,   81,   81,
   81,   81,   81,   81,  216,   81,   81,   81,   81,   42,
   43,   81,  100,  101,  102,  103,  217,   77,   77,   77,
  218,   77,   77,  224,  236,  242,   77,  110,   77,   77,
   77,   77,   77,   77,   40,   77,   77,   77,   77,   61,
   23,   77,  252,   62,   49,   50,   51,  111,   24,  129,
   25,  114,  115,   26,   27,   49,   50,   51,  253,   49,
   50,   51,   64,   67,  254,  255,   21,   21,   52,  256,
  257,   21,   21,   21,  258,  174,  175,   67,   67,   52,
  107,  107,  107,   52,  212,  235,    0,  120,  120,   21,
    0,  120,  120,    0,   64,    0,  120,   23,  120,  120,
  120,  120,  120,  120,  107,   24,    0,   25,   61,   23,
   26,   27,   62,   93,   91,    0,   92,   24,   94,   25,
    0,    0,   26,   27,   61,   23,    0,    0,   62,    0,
   90,    0,    0,   24,   64,   25,    0,    0,   26,   27,
    0,    0,   69,   69,   64,    0,    0,   69,    0,   67,
   67,   69,    0,   69,   69,   69,   69,   69,   69,    0,
    0,    0,   70,   70,    0,    0,    0,   70,    0,    0,
    0,   70,    0,   70,   70,   70,   70,   70,   70,    0,
   83,   23,   64,    0,    0,   84,    0,  112,  112,   24,
    0,   25,  112,    0,   26,   27,  112,    0,  112,  112,
  112,  112,  112,  112,  230,    6,    0,    0,  231,    0,
    8,    9,    0,    0,   10,    0,    0,    0,   55,  239,
  240,  241,   89,    0,  110,  110,   11,  243,  244,  110,
    0,  137,  138,  110,  113,  110,  110,  110,  110,  110,
  110,   55,    0,    0,  150,  152,   55,  119,  120,   64,
    0,   36,   36,   55,    0,   36,    0,   36,   36,    0,
    0,   36,    0,    0,    0,    0,   55,   55,    0,  141,
    0,    0,  146,   36,   67,   67,   67,    0,   67,   67,
  153,    0,    0,    0,   55,   55,   55,   55,   55,    0,
    0,   55,    0,    0,    0,    0,    0,   55,   55,   55,
   55,   55,    0,    0,    0,    0,    0,    0,    0,   55,
    0,   55,    0,    0,    0,    0,  141,    0,  186,   26,
   26,    0,    0,   26,   56,   26,   26,    0,   56,   26,
   56,   56,    0,    6,   56,   55,  229,   55,    8,    9,
    0,   26,   10,    0,    0,    0,   56,    0,    0,    0,
    0,    0,    0,    0,   11,    0,    0,  141,   23,   55,
   55,  141,   66,    0,    0,    0,   24,    0,   25,    0,
    0,   26,   27,    0,    0,    0,   55,    0,   64,   64,
   55,    0,    0,   64,    0,    0,  141,   64,    0,   64,
   64,   64,   64,   64,   64,   65,   65,    0,    0,    0,
   65,    0,    0,    0,   65,   55,   65,   65,   65,   65,
   65,   65,   66,   66,    0,    0,    0,   66,    0,    0,
    0,   66,    0,   66,   66,   66,   66,   66,   66,   67,
   67,    0,    0,    0,   67,    0,    0,    0,   67,    0,
   67,   67,   67,   67,   67,   67,   68,   68,    0,    0,
    0,   68,    0,    0,    0,   68,    0,   68,   68,   68,
   68,   68,   68,   61,   23,    0,    0,   62,    0,    0,
    0,    0,   24,    0,   25,  163,   23,   26,   27,   63,
  164,    0,  247,   23,   24,    0,   25,  248,    0,   26,
   27,   24,    0,   25,  250,   23,   26,   27,    0,  251,
    0,    0,   23,   24,    0,   25,   82,    0,   26,   27,
   24,    0,   25,   23,    0,   26,   27,  162,    0,    0,
   23,   24,    0,   25,  245,    0,   26,   27,   24,    0,
   25,   23,    0,   26,   27,  246,    0,    0,   23,   24,
    0,   25,  249,    0,   26,   27,   24,    0,   25,    0,
    0,   26,   27,
};
short yycheck[] = {                                      40,
   44,   61,   40,   43,    2,   45,   59,  133,   41,    4,
    5,   44,  256,  142,   53,   44,   11,   41,   42,   43,
   44,   45,   44,   47,   59,   44,   75,   76,  272,    6,
   59,  256,  257,  258,   11,   59,  257,   59,  256,  257,
   59,   44,   41,   42,   43,   44,   45,  257,   47,   93,
   41,   40,  257,   44,  183,  280,   59,   46,   42,  264,
   59,   38,  256,   47,   99,   41,  260,   41,   44,   43,
   44,   45,   61,  199,  109,   41,  125,  116,   44,  118,
   75,   76,   41,  212,   43,   59,   45,  256,  257,  258,
   40,  256,   41,   61,   43,   44,   45,  256,  256,  257,
  258,  260,   91,  268,  269,  256,  257,  258,  257,  258,
   59,  280,  147,  256,  257,  258,   91,   41,   40,   43,
   44,   45,  280,  256,  257,  258,   44,  166,  167,  280,
  125,  280,   41,  257,   43,   59,   45,  280,   93,   94,
   59,   59,   41,  256,   43,   44,   45,  280,  261,  262,
  263,  256,  107,  108,  256,  260,  261,  262,  263,  261,
   59,  266,   59,   42,  262,  263,  279,   41,   47,   43,
   44,   45,   59,  278,  279,   59,  256,  257,   59,  214,
  260,  279,  262,  263,  257,   59,  266,  260,   40,  262,
  263,   93,   41,  266,   43,   44,   45,  256,  278,  256,
  257,   43,  261,   45,  257,  278,  257,   40,  206,  207,
   59,  262,  263,  273,  258,  256,  257,  258,  256,  257,
  258,  257,   41,  256,   43,   44,   45,  222,  223,  257,
  258,   40,  256,  257,  258,  257,  260,  261,  257,  280,
   59,  265,  280,  267,  268,  269,  270,  271,  272,   41,
  274,  275,  276,  277,  257,   40,  280,  256,  257,  258,
   41,  260,  261,   44,   41,  256,  265,   44,  267,  268,
  269,  270,  271,  272,   44,  274,  275,  276,  277,   40,
  256,  280,  256,  257,  258,   41,  260,  261,   44,   59,
  256,  265,   41,  267,  268,  269,  270,  271,  272,  256,
  274,  275,  276,  277,  261,  273,  280,  256,  257,  258,
   59,  260,  261,   41,  204,  205,  265,  269,  267,  268,
  269,  270,  271,  272,   41,  274,  275,  276,  277,   41,
  256,  280,  256,  257,  258,  261,  260,  261,  256,  257,
   40,  265,  257,  267,  268,  269,  270,  271,  272,  257,
  274,  275,  276,  277,  262,  263,  280,  256,  257,  258,
  261,  260,  261,   40,  269,   59,  265,   40,  267,  268,
  269,  270,  271,  272,  269,  274,  275,  276,  277,   41,
   41,  280,  256,  257,  258,   59,  260,  261,   41,   59,
   43,  265,   45,  267,  268,  269,  270,  271,  272,   59,
  274,  275,  276,  277,   59,   91,  280,  256,  257,  258,
   59,  260,  261,   41,  257,   43,  265,   45,  267,  268,
  269,  270,  271,  272,   41,  274,  275,  276,  277,   19,
   20,  280,  274,  275,  276,  277,   41,  256,  257,  258,
  257,  260,  261,  258,  269,  258,  265,   59,  267,  268,
  269,  270,  271,  272,  261,  274,  275,  276,  277,  256,
  257,  280,   59,  260,  256,  257,  258,  259,  265,   76,
  267,   61,   62,  270,  271,  256,  257,  258,   59,  256,
  257,  258,   27,   28,   59,   59,  256,  257,  280,   59,
   59,  261,  262,  263,   59,  125,  125,   42,   43,  280,
  256,  257,  258,  280,  187,  226,   -1,  256,  257,  279,
   -1,  260,  261,   -1,   59,   -1,  265,  257,  267,  268,
  269,  270,  271,  272,  280,  265,   -1,  267,  256,  257,
  270,  271,  260,   42,   43,   -1,   45,  265,   47,  267,
   -1,   -1,  270,  271,  256,  257,   -1,   -1,  260,   -1,
   59,   -1,   -1,  265,   99,  267,   -1,   -1,  270,  271,
   -1,   -1,  256,  257,  109,   -1,   -1,  261,   -1,  114,
  115,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
   -1,   -1,  256,  257,   -1,   -1,   -1,  261,   -1,   -1,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
  256,  257,  147,   -1,   -1,  261,   -1,  256,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,  268,
  269,  270,  271,  272,  256,  257,   -1,   -1,  260,   -1,
  262,  263,   -1,   -1,  266,   -1,   -1,   -1,   25,  229,
  230,  231,   48,   -1,  256,  257,  278,  237,  238,  261,
   -1,   91,   92,  265,   60,  267,  268,  269,  270,  271,
  272,   48,   -1,   -1,  104,  105,   53,   73,   74,  214,
   -1,  256,  257,   60,   -1,  260,   -1,  262,  263,   -1,
   -1,  266,   -1,   -1,   -1,   -1,   73,   74,   -1,   95,
   -1,   -1,   98,  278,  239,  240,  241,   -1,  243,  244,
  106,   -1,   -1,   -1,   91,   92,   93,   94,   95,   -1,
   -1,   98,   -1,   -1,   -1,   -1,   -1,  104,  105,  106,
  107,  108,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  116,
   -1,  118,   -1,   -1,   -1,   -1,  142,   -1,  144,  256,
  257,   -1,   -1,  260,  256,  262,  263,   -1,  260,  266,
  262,  263,   -1,  257,  266,  142,  260,  144,  262,  263,
   -1,  278,  266,   -1,   -1,   -1,  278,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  278,   -1,   -1,  183,  257,  166,
  167,  187,  261,   -1,   -1,   -1,  265,   -1,  267,   -1,
   -1,  270,  271,   -1,   -1,   -1,  183,   -1,  256,  257,
  187,   -1,   -1,  261,   -1,   -1,  212,  265,   -1,  267,
  268,  269,  270,  271,  272,  256,  257,   -1,   -1,   -1,
  261,   -1,   -1,   -1,  265,  212,  267,  268,  269,  270,
  271,  272,  256,  257,   -1,   -1,   -1,  261,   -1,   -1,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,  256,
  257,   -1,   -1,   -1,  261,   -1,   -1,   -1,  265,   -1,
  267,  268,  269,  270,  271,  272,  256,  257,   -1,   -1,
   -1,  261,   -1,   -1,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,  256,  257,   -1,   -1,  260,   -1,   -1,
   -1,   -1,  265,   -1,  267,  256,  257,  270,  271,  272,
  261,   -1,  256,  257,  265,   -1,  267,  261,   -1,  270,
  271,  265,   -1,  267,  256,  257,  270,  271,   -1,  261,
   -1,   -1,  257,  265,   -1,  267,  261,   -1,  270,  271,
  265,   -1,  267,  257,   -1,  270,  271,  261,   -1,   -1,
  257,  265,   -1,  267,  261,   -1,  270,  271,  265,   -1,
  267,  257,   -1,  270,  271,  261,   -1,   -1,  257,  265,
   -1,  267,  261,   -1,  270,  271,  265,   -1,  267,   -1,
   -1,  270,  271,
};
#define YYFINAL 3
#ifndef YYDEBUG
#define YYDEBUG 0
#endif
#define YYMAXTOKEN 281
#if YYDEBUG
char *yyname[] = {
"end-of-file",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,"'('","')'","'*'","'+'","','","'-'","'.'","'/'",0,0,0,0,0,0,0,0,0,0,
0,"';'",0,"'='",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"'['",
0,"']'",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,"ID","CTE","CADENA","BEGIN","END","USHORTINT","DOUBLEF",
"FUNCTION","RET","CLASS","IF","ELSE","END_IF","POUT","REPEAT","WHILE",
"ASIGNACION","IGUAL","DISTINTO","MAYORIGUAL","MENORIGUAL","COMPTIME","EXTENDS",
"TODF","SIN_PUNTO_Y_COMA",
};
char *yyrule[] = {
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
"expresion : error ';'",
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
"termino : error '/' factor",
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
"parametros_reales : parametros_reales parametro_real",
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
#endif
#ifndef YYSTYPE
typedef int YYSTYPE;
#endif
#define yyclearin (yychar=(-1))
#define yyerrok (yyerrflag=0)
#ifdef YYSTACKSIZE
#ifndef YYMAXDEPTH
#define YYMAXDEPTH YYSTACKSIZE
#endif
#else
#ifdef YYMAXDEPTH
#define YYSTACKSIZE YYMAXDEPTH
#else
#define YYSTACKSIZE 500
#define YYMAXDEPTH 500
#endif
#endif
int yydebug;
int yynerrs;
int yyerrflag;
int yychar;
short *yyssp;
YYSTYPE *yyvsp;
YYSTYPE yyval;
YYSTYPE yylval;
short yyss[YYSTACKSIZE];
YYSTYPE yyvs[YYSTACKSIZE];
#define yystacksize YYSTACKSIZE
#define YYABORT goto yyabort
#define YYACCEPT goto yyaccept
#define YYERROR goto yyerrlab
int
yyparse()
{
    register int yym, yyn, yystate;
#if YYDEBUG
    register char *yys;
    extern char *getenv();

    if (yys = getenv("YYDEBUG"))
    {
        yyn = *yys;
        if (yyn >= '0' && yyn <= '9')
            yydebug = yyn - '0';
    }
#endif

    yynerrs = 0;
    yyerrflag = 0;
    yychar = (-1);

    yyssp = yyss;
    yyvsp = yyvs;
    *yyssp = yystate = 0;

yyloop:
    if (yyn = yydefred[yystate]) goto yyreduce;
    if (yychar < 0)
    {
        if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, reading %d (%s)\n", yystate,
                    yychar, yys);
        }
#endif
    }
    if ((yyn = yysindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: state %d, shifting to state %d (%s)\n",
                    yystate, yytable[yyn],yyrule[yyn]);
#endif
        if (yyssp >= yyss + yystacksize - 1)
        {
            goto yyoverflow;
        }
        *++yyssp = yystate = yytable[yyn];
        *++yyvsp = yylval;
        yychar = (-1);
        if (yyerrflag > 0)  --yyerrflag;
        goto yyloop;
    }
    if ((yyn = yyrindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
        yyn = yytable[yyn];
        goto yyreduce;
    }
    if (yyerrflag) goto yyinrecovery;
#ifdef lint
    goto yynewerror;
#endif
yynewerror:
    yyerror("syntax error");
#ifdef lint
    goto yyerrlab;
#endif
yyerrlab:
    ++yynerrs;
yyinrecovery:
    if (yyerrflag < 3)
    {
        yyerrflag = 3;
        for (;;)
        {
            if ((yyn = yysindex[*yyssp]) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: state %d, error recovery shifting\
 to state %d\n", *yyssp, yytable[yyn]);
#endif
                if (yyssp >= yyss + yystacksize - 1)
                {
                    goto yyoverflow;
                }
                *++yyssp = yystate = yytable[yyn];
                *++yyvsp = yylval;
                goto yyloop;
            }
            else
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: error recovery discarding state %d\n",
                            *yyssp);
#endif
                if (yyssp <= yyss) goto yyabort;
                --yyssp;
                --yyvsp;
            }
        }
    }
    else
    {
        if (yychar == 0) goto yyabort;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, error recovery discards token %d (%s)\n",
                    yystate, yychar, yys);
        }
#endif
        yychar = (-1);
        goto yyloop;
    }
yyreduce:
#if YYDEBUG
    if (yydebug)
        printf("yydebug: state %d, reducing by rule %d (%s)\n",
                yystate, yyn, yyrule[yyn]);
#endif
    yym = yylen[yyn];
    yyval = yyvsp[1-yym];
    switch (yyn)
    {
case 1:
#line 20 "gramatica.y"
{System.out.println("Declaracion de programa");}
break;
case 2:
#line 21 "gramatica.y"
{ yyerror("Falta de nombre de programa");}
break;
case 3:
#line 22 "gramatica.y"
{ yyerror("Falta la palabra reservada BEGIN");}
break;
case 4:
#line 23 "gramatica.y"
{ yyerror("Falta la palabra reservada END");}
break;
case 7:
#line 33 "gramatica.y"
{System.out.println("Declaracion de VAR");}
break;
case 8:
#line 34 "gramatica.y"
{System.out.println("Declaracion de comptime");}
break;
case 9:
#line 35 "gramatica.y"
{System.out.println("Declaracion de funcion");}
break;
case 10:
#line 36 "gramatica.y"
{System.out.println("Declaracion de clase");}
break;
case 11:
#line 37 "gramatica.y"
{System.out.println("Declaracion de objeto");}
break;
case 13:
#line 42 "gramatica.y"
{ yyerror("Declaracion de variables: Falta ';' " );}
break;
case 15:
#line 47 "gramatica.y"
{ yyerror("Declaracion de variables COMPTIME: Falta el ';'");}
break;
case 16:
#line 48 "gramatica.y"
{ yyerror("Falta tipo en las variables en COMPTIME");}
break;
case 20:
#line 57 "gramatica.y"
{ yyerror("Lista de variables: Falta la ','");}
break;
case 23:
#line 63 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta de nombre de programa");}
break;
case 24:
#line 64 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta la palabra reservada BEGIN");}
break;
case 25:
#line 65 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta la palabra reservada END");}
break;
case 26:
#line 66 "gramatica.y"
{ yyerror("Declaracion de funcion: Falta el ';'");}
break;
case 28:
#line 71 "gramatica.y"
{ yyerror("Parametros formales: Falta la ','");}
break;
case 31:
#line 77 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'tipo''");}
break;
case 32:
#line 78 "gramatica.y"
{ yyerror("Parametro Formal: Falta el 'ID''");}
break;
case 34:
#line 83 "gramatica.y"
{ yyerror ("Declaracion de clase: Falta el BEGIN");}
break;
case 35:
#line 84 "gramatica.y"
{ yyerror("Declaracion de clase: Falta el END");}
break;
case 36:
#line 85 "gramatica.y"
{ yyerror("Declaracion de clase: Falta el ';'");}
break;
case 45:
#line 106 "gramatica.y"
{System.out.println("Sentencia EXTENDS");}
break;
case 46:
#line 107 "gramatica.y"
{ yyerror("Sentencia EXTENDS: Falta el ';'");}
break;
case 47:
#line 108 "gramatica.y"
{ yyerror("Falta lista ids");}
break;
case 49:
#line 113 "gramatica.y"
{ yyerror("Lista ids: Falta la ','");}
break;
case 51:
#line 118 "gramatica.y"
{System.out.println("METODO");}
break;
case 52:
#line 119 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el BEGIN");}
break;
case 53:
#line 120 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el END");}
break;
case 54:
#line 121 "gramatica.y"
{ yyerror("Declaracion de metodo: Falta el ';'");}
break;
case 56:
#line 126 "gramatica.y"
{ yyerror("Declaracion Objeto: Falta el ';'");}
break;
case 59:
#line 136 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 60:
#line 137 "gramatica.y"
{System.out.println("Seleccion");}
break;
case 61:
#line 138 "gramatica.y"
{System.out.println("Salida");}
break;
case 62:
#line 139 "gramatica.y"
{System.out.println("Retorno");}
break;
case 63:
#line 140 "gramatica.y"
{System.out.println("Iterativa");}
break;
case 64:
#line 141 "gramatica.y"
{ yyerror("Asignacion: Falta el ';'");}
break;
case 65:
#line 142 "gramatica.y"
{ yyerror("Seleccion: Falta el ';'");}
break;
case 66:
#line 143 "gramatica.y"
{ yyerror("Salida: Falta el ';'");}
break;
case 67:
#line 144 "gramatica.y"
{ yyerror("Retorno: Falta el ';'");}
break;
case 68:
#line 145 "gramatica.y"
{ yyerror("Iterativa: Falta el ';'");}
break;
case 70:
#line 150 "gramatica.y"
{ yyerror("Uso de '=' en asignacion; debe utilizar ':='."); }
break;
case 78:
#line 169 "gramatica.y"
{yyerror("Falta operador")}
break;
case 79:
#line 170 "gramatica.y"
{yyerror("Falta operando")}
break;
case 80:
#line 171 "gramatica.y"
{yyerror("Falta operando")}
break;
case 81:
#line 172 "gramatica.y"
{yyerror("Falta operando")}
break;
case 82:
#line 173 "gramatica.y"
{yyerror("Falta operando")}
break;
case 86:
#line 180 "gramatica.y"
{yyerror("Falta operando")}
break;
case 87:
#line 181 "gramatica.y"
{yyerror("Falta operando")}
break;
case 88:
#line 182 "gramatica.y"
{yyerror("Falta operando")}
break;
case 89:
#line 183 "gramatica.y"
{yyerror("Falta operando")}
break;
case 93:
#line 190 "gramatica.y"
{System.out.println("Acceso posicional");}
break;
case 94:
#line 191 "gramatica.y"
{System.out.println("Asignacion EXP");}
break;
case 95:
#line 192 "gramatica.y"
{System.out.println("Conversion DF");}
break;
case 97:
#line 197 "gramatica.y"
{System.out.println("Metodo con orden opcional");}
break;
case 98:
#line 198 "gramatica.y"
{System.out.println("Objeto con orden opcional");}
break;
case 102:
#line 208 "gramatica.y"
{ yyerror("Lista constantes: Falta la ','");}
break;
case 105:
#line 214 "gramatica.y"
{ yyerror("Parametro Reales: Falta la ','");}
break;
case 109:
#line 224 "gramatica.y"
{ yyerror("Falta WHILE en condicion de iteracion");}
break;
case 110:
#line 225 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en condicion iterativa");}
break;
case 111:
#line 226 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en condicion iterativa");}
break;
case 112:
#line 227 "gramatica.y"
{ yyerror("Falta de parentesis en condicion iterativa");}
break;
case 113:
#line 228 "gramatica.y"
{ yyerror("Falta de bloque ejecutable en condicion de iteracion");}
break;
case 116:
#line 234 "gramatica.y"
{ yyerror("Falta de parentesis de cierre en seleccion"); }
break;
case 117:
#line 235 "gramatica.y"
{ yyerror("Falta de parentesis de apertura en seleccion");}
break;
case 118:
#line 236 "gramatica.y"
{ yyerror("Falta de parentesis en seleccion"); }
break;
case 119:
#line 237 "gramatica.y"
{ yyerror("Falta de END_IF en seleccion");}
break;
case 120:
#line 241 "gramatica.y"
{System.out.println("Condicion");}
break;
case 124:
#line 245 "gramatica.y"
{System.out.println("Comparador");}
break;
case 125:
#line 249 "gramatica.y"
{System.out.println("BloqueEJ1");}
break;
case 126:
#line 250 "gramatica.y"
{System.out.println("BloqueEJ2");}
break;
case 127:
#line 251 "gramatica.y"
{ yyerror("Declaracion de bloque ejecutable: Falta el BEGIN ");}
break;
case 128:
#line 252 "gramatica.y"
{ yyerror("Declaracion de bloque ejecutable: Falta el END ");}
break;
case 129:
#line 256 "gramatica.y"
{System.out.println("POUT cadena");}
break;
case 130:
#line 257 "gramatica.y"
{System.out.println("POUT expresion");}
break;
case 131:
#line 258 "gramatica.y"
{yyerror("Falta Argumento en POUT");}
break;
#line 994 "y.tab.c"
    }
    yyssp -= yym;
    yystate = *yyssp;
    yyvsp -= yym;
    yym = yylhs[yyn];
    if (yystate == 0 && yym == 0)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: after reduction, shifting from state 0 to\
 state %d\n", YYFINAL);
#endif
        yystate = YYFINAL;
        *++yyssp = YYFINAL;
        *++yyvsp = yyval;
        if (yychar < 0)
        {
            if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
            if (yydebug)
            {
                yys = 0;
                if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                if (!yys) yys = "illegal-symbol";
                printf("yydebug: state %d, reading %d (%s)\n",
                        YYFINAL, yychar, yys);
            }
#endif
        }
        if (yychar == 0) goto yyaccept;
        goto yyloop;
    }
    if ((yyn = yygindex[yym]) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn];
    else
        yystate = yydgoto[yym];
#if YYDEBUG
    if (yydebug)
        printf("yydebug: after reduction, shifting from state %d \
to state %d\n", *yyssp, yystate);
#endif
    if (yyssp >= yyss + yystacksize - 1)
    {
        goto yyoverflow;
    }
    *++yyssp = yystate;
    *++yyvsp = yyval;
    goto yyloop;
yyoverflow:
    yyerror("yacc stack overflow");
yyabort:
    return (1);
yyaccept:
    return (0);
}
