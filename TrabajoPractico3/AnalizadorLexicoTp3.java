package TrabajoPractico3;

public class AnalizadorLexicoTp3{

    private final RecolectorErroresTp3 recolectorErrores;
    public ParserVal yylval;// Valor semántico del token actual
    private LecturaCodigoTp3 lectorCodigo;
    private TablaDeSimbolosTp3 tablaSimbolos = new TablaDeSimbolosTp3();
    private TablaPalabrasReservadasTp3 tablaPR = new TablaPalabrasReservadasTp3(); 
    private StringBuilder lexema = new StringBuilder();
    private static final int F = -1; // Estado Final
    private static java.math.BigDecimal minPos = new java.math.BigDecimal("2.2250738585072014e-308");
    private static java.math.BigDecimal maxPos = new java.math.BigDecimal("1.7976931348623157e+308");
    private static java.math.BigDecimal minNeg = new java.math.BigDecimal("-1.7976931348623157e+308");
    private static java.math.BigDecimal maxNeg = new java.math.BigDecimal("-2.2250738585072014e-308");
    private static java.math.BigDecimal cero = java.math.BigDecimal.ZERO;
    private static int[][] matriz_estados = {
                //  l   L   d  "d"  +   -   *   /   :   =   >   <   !   (   )   ,   ;   _   $   u   S   {   }   .  BL   " [  ]
                {   1,  3,  4,  1,  F,  F,  F,  F, 12, 13, 13, 13, 13,  F,  F,  F,  F,  0,  0,  1,  1, 14,  0, 11,  0, 15, F, F}, // Estado 0
                {   1,  3,  2,  1,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  1,  F,  1,  1,  F,  F,  F,  F, F, F, F}, // Estado 1
                {   2,  F,  2,  2,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  2,  F,  2,  2,  F,  F,  F,  F, F, F, F}, // Estado 2
                {   3,  3,  F,  3,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  3,  F,  3,  3,  F,  F,  F,  F, F, F, F}, // Estado 3
                {   F,  F,  4,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  5,  F,  F,  F,  F, 16,  F, F, F, F}, // Estado 4
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  6,  F,  F,  F,  F,  F, F, F, F}, // Estado 5
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  7,  F,  F,  F,  F, F, F, F}, // Estado 6
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 7
                {   F,  F,  8,  9,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 8
                {   F,  F, 10,  F, 17, 17,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 9
                {   F,  F, 10,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 10
                {   F,  F,  8,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 11
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 12
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 13
                {  14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,  F, 14, 14, 14, 14, 14}, // Estado 14
                {  15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 15, 15},// Estado 15
                {   F,  F,  8,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 16
                {   F,  F, 10,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F} // Estado 17
        };
    private static int[][] matriz_acciones = { // Matriz de Acciones Semánticas 
            // l   L   d  "d"  +   -   *     /   :   =   >   <   !   (     )   ,    ;   _   $   u   s   {   }   .  BL   "   [   ]
            {  1,  1,  1,  1,  16, 16, 16,  16,  1,  1,  1,  1,  1,  16,  16,  16,  16,  0,  0,  1,  1,  1, 0, 1,  0,  1 , 16, 16}, // Fila 0
            {  2,  2,  2,  2,  3,  3,  3,   3,   3, 3, 3, 3, 3,  3,   3,  3,    3,   2, 3,  2,  2,  3, 3, 3,  3, 3 ,  3,  3}, // Fila 1
            {  2,  4,  2,  2,  4,  4,  4,   4,   4,  4,  4,  4,  4,   4,   4,   4,  4,  2,  4,  2,  2,  4,  4,  4,  4,  4 ,  4,  4}, // Fila 2
            {  2,  2,  5,  2,  5,  5,  5,   5,   5,  5,  5,  5,  5,   5,   5,   5,  5,  2,  5,  2,  2,  5,  5,  5,  5,  5 ,  5,  5}, // Fila 3
            {  0,  0,  2,  0,  0,  0,  0,   0,   0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  2,  0,  0,  0,  0,  2,  0,  0 ,  0,  0}, // Fila 4
            {  0,  0,  0,  0,  0,  0,  0,   0,   0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  2,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 5
            {  0,  0,  0,  0,  0,  0,  0,   0,   0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  2,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 6
            {  8,  8,  8,  8,  8,  8,  8,   8,   8,  8,  8,  8,  8,   8,   8,   8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8 ,  8,  8}, // Fila 7
            {  9,  9,  2,  2,  9,  9,  9,   9,   9,  9,  9,  9,  9,   9,   9,   9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9 ,  9,  9}, // Fila 8
            {  0,  0,  2,  0,  2,  2,  0,   0,   0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 9
            {  9,  9,  2,  9,  9,  9,  9,   9,   9,  9,  9,  9,  9,   9,   9,   9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9 ,  9,  9}, // Fila 10
            {  6,  6,  2,  6,  6,  6,  6,   6,   6,  6,  6,  6,  6,   6,   6,   6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6 ,  6,  6}, // Fila 11
            {  17, 17, 17, 17, 17, 17, 17,  17,  17, 7,  17, 17, 17,  17,  17,  17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17,  17}, // Fila 12
            {  6,  6,  6,  6,  6,  6,  6,   6,   6,  7,  6,  6,  6,   6,   6,   6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6 ,  6,  6}, // Fila 13
            {  2,  2,  2,  2,  2,  2,  2,    2,  2,  2,  2,  2,  2,   2,   2,   2,  2,  2,  2,  2,  2,  2, 11,  2,  11, 2 ,  2,  2}, // Fila 14
            {  2,  2,  2,  2,  2,  2,  2,    2,  2,  2,  2,  2,  2,   2,   2,   2,  2,  2,  2,  2,  2,  2,  2,  2,  2, 12 ,  2,  2}, // Fila 15
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 16
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}  // Fila 17
    };

    public AnalizadorLexicoTp3 (LecturaCodigoTp3 lectorCodigo) { 
            this.lectorCodigo = lectorCodigo;
            this.recolectorErrores = lectorCodigo.getRecolectorErrores();
        }
    
    public AnalizadorLexicoTp3(LecturaCodigoTp3 lectorCodigo, RecolectorErroresTp3 recolectorErrores) {
        if (lectorCodigo == null || recolectorErrores == null) {
            throw new IllegalArgumentException("El lector y el recolector son obligatorios.");
        }

        this.lectorCodigo = lectorCodigo;
        this.recolectorErrores = recolectorErrores;
    }

    public int obtenerColumna(char c) {
        if (c == 'd') return 3;   
        if (c == 'u') return 19; 
        if (c == 's') return 20; 

        if (Character.isLowerCase(c)) return 0; 
        if (Character.isUpperCase(c)) return 1; 
        if (Character.isDigit(c))     return 2; 

        switch (c) {
            case '+': return 4;
            case '-': return 5;
            case '*': return 6;
            case '/': return 7;
            case ':': return 8;
            case '=': return 9;
            case '>': return 10;
            case '<': return 11;
            case '!': return 12;
            case '(': return 13;
            case ')': return 14;
            case ',': return 15;
            case ';': return 16;
            case '_': return 17;
            case '$': return 18;
            case '{': return 21;
            case '}': return 22;
            case '.': return 23;

            case ' ':
            case '\t':
            case '\r':
            case '\n': return 24;

            case '"': return 25;
            case '[': return 26;
            case ']': return 27;

            default: 
                return -1; // Caracter invalido
            }
    }

    public int yylex() {
        this.yylval = null; // Reinicia el valor semántico del token actual
        int estadoActual = 0;
        lexema.setLength(0);
        int parserID = 0;

        while (parserID == 0) {
            char caracterLeido = lectorCodigo.leerSiguienteCaracter();
            
            if (caracterLeido == LecturaCodigoTp3.EOF) { // Fin de archivo - EOF
                
                if (estadoActual == 0) { // Fin de archivo
                    System.out.println("Final archivo - Linea " + lectorCodigo.getLineaActual());
                    lectorCodigo.cerrar();
                    return 0;
                }

                if (estadoActual == 15) { // Comentario multilinea sin cerrar y EOF
                    return ejecutarAccionSemantica(15, LecturaCodigoTp3.EOF);
                }
                
                int accionEOF = matriz_acciones[estadoActual][24]; // Token a medio formar y EOF
                parserID = ejecutarAccionSemantica(accionEOF, LecturaCodigoTp3.EOF);
                if (parserID == 0) {
                    estadoActual = 0;
                    continue;
                }
                return parserID;
            }

            int columna = obtenerColumna(caracterLeido);

            if (columna == 24 && estadoActual == 0) { // Ignorar espacios, tabulaciones y saltos de línea en reposo (Estado 0)
                lexema.setLength(0);
                continue;
            }

            if (columna == -1) { // Caracter invalido
                if (estadoActual == 15) { // Comentario multilinea y Caracter invalido
                    ejecutarAccionSemantica(2, caracterLeido); 
                    continue; 
                }
                else {
                    registrarError("Linea " + lectorCodigo.getLineaActual() + ": Caracter invalido:" + caracterLeido );
                    while (!(columna == 24)){
                        caracterLeido = lectorCodigo.leerSiguienteCaracter();
                        if (caracterLeido == LecturaCodigoTp3.EOF) {
                            System.out.println("Final archivo - Linea " + lectorCodigo.getLineaActual());
                            break;
                        }
                        columna = obtenerColumna(caracterLeido);
                    }
                    lexema.setLength(0);
                    estadoActual = 0;
                    continue;
                }
            }

            int accion = matriz_acciones[estadoActual][columna];
            int estadoSiguiente = matriz_estados[estadoActual][columna];

            parserID = ejecutarAccionSemantica(accion, caracterLeido);

            if (estadoSiguiente == F || (parserID == 0 && lexema.length() == 0)) { // Control de transición y recuperación
                estadoActual = 0;
            } 
            else {
                estadoActual = estadoSiguiente;
            }
        }

        return parserID;
    }

    private int ejecutarAccionSemantica(int accion, char c) {
        switch (accion) {

            case 0: // AS0 - Estructura mal formada
               boolean errorEnEstadoCero = (lexema.length() == 0); // Si lexema está vacío, ocurrió en reposo

                lexema.append(c);
                registrarError("Línea " + lectorCodigo.getLineaActual() + ": Estructura mal formada: " + lexema);

                if (!errorEnEstadoCero && c != LecturaCodigoTp3.EOF) { //Si el error no ocurrió en Estado 0
                    lectorCodigo.devolverCaracter();
                }

                lexema.setLength(0);
                return 0;   
            
            case 1: // AS1: Inicializa el buffer del string y añade el primer carácter leído.
                lexema.setLength(0); 
                lexema.append(c); 
                break;
            
            case 2: // AS2: Concatena el nuevo carácter al string
                lexema.append(c);
                break;

            case 3: // AS3: Estado Final -> Identificador o Palabra Reservada
                lectorCodigo.devolverCaracter();
                String textoAS3 = lexema.toString();
                lexema.setLength(0); 
                
                if (tablaPR.esPalabraReservada(textoAS3)) {
                    yylval = new ParserVal(textoAS3); 
                    return tablaPR.obtenerId(textoAS3);
                }

                if (textoAS3.length() > 22) { 
                    textoAS3 = textoAS3.substring(0, 22);
                    System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Warning - Identificador supera el límite permitido; se trunca a 22 caracteres: " + textoAS3);
                }

                yylval = new ParserVal(textoAS3);
                return Parser.ID; // Utilizabamos Token.ID

            case 4: // AS4: Estado Final -> Identificador. Al tener digitos no puede ser PR
                lectorCodigo.devolverCaracter();
                String textoAS4 = lexema.toString();
                lexema.setLength(0); 

                if (textoAS4.length() > 22) {
                    textoAS4 = textoAS4.substring(0, 22);
                    System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Warning - Identificador supera el límite permitido; se trunca a 22 caracteres: " + textoAS4);
                    // Esto es una advertencia.
                }

                yylval = new ParserVal(textoAS4);
                return Parser.ID; // Utilizabamos Token.ID

            case 5: // AS5: Palabra reservada. Al tener mayusculas no es Identificador
                lectorCodigo.devolverCaracter(); 
                String textoAS5 = lexema.toString();
                lexema.setLength(0); 

                if (tablaPR.esPalabraReservada(textoAS5)) {
                        yylval = new ParserVal(textoAS5);
                        return tablaPR.obtenerId(textoAS5);
                    }

                registrarError("Línea " + lectorCodigo.getLineaActual() + ": Identificador inválido con mayúsculas: " + textoAS5 );
                return 0; 

            case 6: // AS6: Estado Final -> Operador simple (+, -, >, <, =) y punto 
                lectorCodigo.devolverCaracter();
                char op = lexema.charAt(0); 
                lexema.setLength(0);            
                return (int) op; // Utilizabamos (int) op para devolver el código ASCII del operador simple

            case 7: // AS7: Estado Final -> Operador Compuesto (>=, <=, ==, !=, :=)
                lexema.append(c);                 
                String opCompuesto = lexema.toString();            

                if (opCompuesto.equals(">=")) 
                        return Parser.MAYORIGUAL;// Utilizabamos Token.MAYOR_IGUAL 
                if (opCompuesto.equals("<=")) 
                        return Parser.MENORIGUAL; // Utilizabamos Token.MAYOR_IGUAL 
                if (opCompuesto.equals("==")) 
                        return Parser.IGUAL; // Utilizabamos Token.MAYOR_IGUAL 
                if (opCompuesto.equals("!=")) 
                        return Parser.DISTINTO;  // Utilizabamos Token.MAYOR_IGUAL   
                if (opCompuesto.equals(":=")) 
                        return Parser.ASIGNACION; // Utilizabamos Token.MAYOR_IGUAL 
                  
                lexema.setLength(0); 
                return 0;

            case 8: // AS8: Estado Final -> USHORTINT
                if (c != LecturaCodigoTp3.EOF) {
                    lectorCodigo.devolverCaracter(); 
                }

                String lexUs = lexema.toString(); 
                lexema.setLength(0);

                String numero = lexUs.substring(0, lexUs.length() - 3);  // Quita el "$us"(longitud - 3)
                
                // Validación previa por longitud para evitar desbordamientos y excepciones
                if (numero.length() > 10) { 
                    registrarError("Línea " + lectorCodigo.getLineaActual() + ": Constante USHORTINT excesivamente larga o fuera de rango: " + lexUs);
                    return 0; 
                }

                long valorLargo = Long.parseLong(numero);
                
                if (valorLargo < 0 || valorLargo > 255) { 
                    registrarError("Línea " + lectorCodigo.getLineaActual() + ": Constante USHORTINT fuera de rango [0, 255]: " + valorLargo);
                    return 0; 
                }
                
                tablaSimbolos.agregarConstante(lexUs, "USHORTINT", lectorCodigo.getLineaActual());
                yylval = new ParserVal(lexUs);
                return Parser.CTE; // Utilizabamos Token.USHORTINT
            
            case 9: // AS9: Estado Final -> DOUBLEF
                if (c != LecturaCodigoTp3.EOF) {
                    lectorCodigo.devolverCaracter();
                }

                String lexDouble = lexema.toString();
                lexema.setLength(0);

                // Verificación de seguridad para exponentes excesivamente largos sin alterar la lógica de signos externa
                int indiceD = lexDouble.toLowerCase().indexOf('d');
                if (indiceD != -1) {
                    String exponenteStr = lexDouble.substring(indiceD + 1);
                    
                    // Si el exponente tras quitar un posible signo directo es demasiado largo
                    String expLimpio = exponenteStr;
                    if (expLimpio.startsWith("+") || expLimpio.startsWith("-")) {
                        expLimpio = expLimpio.substring(1);
                    }

                    if (expLimpio.length() > 5) { // Un exponente mayor a 5 dígitos (ej. e999999) desborda cualquier tipo estándar
                        registrarError("Línea " + lectorCodigo.getLineaActual() + ": Constante o exponente excesivamente largo para DOUBLEF: " + lexDouble);
                        return 0; // Error controlado, continúa el análisis
                    }
                }

                String expJava = lexDouble.replace('d', 'e').replace('D', 'e');

                try {
                    java.math.BigDecimal valor = new java.math.BigDecimal(expJava);

                    boolean valido = (valor.compareTo(cero) == 0) || 
                                    (valor.compareTo(minPos) > 0 && valor.compareTo(maxPos) < 0) || 
                                    (valor.compareTo(minNeg) > 0 && valor.compareTo(maxNeg) < 0);

                    if (!valido) {
                        registrarError("Línea " + lectorCodigo.getLineaActual() + ": Constante DOUBLEF fuera de rango: " + lexDouble);
                        return 0;
                    }

                    tablaSimbolos.agregarConstante(lexDouble, "DOUBLEF", lectorCodigo.getLineaActual());
                    yylval = new ParserVal(lexDouble);
                    return Parser.CTE; // Utilizabamos Token.DOUBLEF

                } catch (NumberFormatException e) {
                    registrarError("Línea " + lectorCodigo.getLineaActual() + ": Constante DOUBLEF mal formada o fuera de rango: " + lexDouble);
                    return 0;
                }

            case 11: // AS11: Estado Final -> Cadena de una línea {...} 
                if (c == '\n' || c == '\r') { // Error: si vino un salto de línea antes de cerrar
                    if (lexema.length() > 0) {
                        registrarError("Línea " + lectorCodigo.getLineaActual() + ": Cadena de una línea sin cerrar : " + lexema );
                    }
                    lexema.setLength(0);
                    return 0; 
                }

                if (c == '}') { // Cierre de cadena de una línea
                    lexema.append(c);
                    String textoCadena = lexema.toString();
                    lexema.setLength(0);
                    tablaSimbolos.agregarConstante(textoCadena, "CADENA", lectorCodigo.getLineaActual());
                    yylval = new ParserVal(textoCadena);
                    return Parser.CADENA;  // Utilizabamos Token.CADENA 
                }

                lexema.append(c);
                break;

            case 12: // AS12: Estado Final -> Comentario multilínea "..."
                lexema.setLength(0); 
                return 0;

            case 14: // AS14: Fin del archivo | Identificador o Palabra Reservada
                String textoAS14 = lexema.toString();
                lexema.setLength(0);

                if (tablaPR.esPalabraReservada(textoAS14)) {
                    yylval = new ParserVal(textoAS14);
                    return tablaPR.obtenerId(textoAS14);
                }

                if (textoAS14.length() > 22) {
                    textoAS14 = textoAS14.substring(0, 22);
                    System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Warning - Identificador supera el límite permitido; se trunca a 22 caracteres: " + textoAS14);
                }
                
                yylval = new ParserVal(textoAS14);
                return Parser.ID;  // Utilizabamos Token.ID
            
            
            case 15: // AS15: Comentario multilinea sin cerrar y EOF
                registrarError("Línea " + lectorCodigo.getLineaActual() + ": Comentario sin cerrar al alcanzar fin de archivo");
                
                lexema.setLength(0); 
                lectorCodigo.cerrar();
                return 0; // Retorna 0 para indicar fin de archivo o error
            
            case 16: // AS16: Token operador simple ('+', '-', '*', '/', '(', ')', ',', ';')
                lexema.setLength(0);   
                return (int) c; // Devuelve el código ASCII del operador simple
            
            case 17: // AS16: Caracter invalido
                char caracterError = lexema.charAt(0); 
                if (c != LecturaCodigoTp3.EOF) {
                    lectorCodigo.devolverCaracter();
                }
                registrarError("Línea " + lectorCodigo.getLineaActual() + ": Símbolo no permitido: " + caracterError);
                lexema.setLength(0);   
                return 0;
            } 

        return 0;
    }

    public TablaDeSimbolosTp3 getTablaSimbolos() {
        return tablaSimbolos;
    }

    public int getLineaActual() {
        return lectorCodigo.getLineaActual();
    }

    private void registrarError(String descripcion) {
        recolectorErrores.agregarError("Léxico",lectorCodigo.getLineaActual(),descripcion);
    }

    public RecolectorErroresTp3 getRecolectorErroresTp3 (){
        return this.recolectorErrores;
    }
}
