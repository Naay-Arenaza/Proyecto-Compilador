/*public class AnalizadorLexico1 {

    private LecturaCodigo lectorCodigo;
    private TablaDeSimbolos tablaSimbolos = new TablaDeSimbolos();
    private TablaPalabrasReservadas tablaPR = new TablaPalabrasReservadas(); 
    private StringBuilder lexema = new StringBuilder();
    private static final int F = -1; // Estado Final
    private static java.math.BigDecimal minPos = new java.math.BigDecimal("2.2250738585072014e-308");
    private static java.math.BigDecimal maxPos = new java.math.BigDecimal("1.7976931348623157e+308");
    private static java.math.BigDecimal minNeg = new java.math.BigDecimal("-1.7976931348623157e+308");
    private static java.math.BigDecimal maxNeg = new java.math.BigDecimal("-2.2250738585072014e-308");
    private static java.math.BigDecimal cero = java.math.BigDecimal.ZERO;
    private static int[][] matriz_estados = {
                //  l   L   d  "d"  +   -   *   /   :   =   >   <   !   (   )   ,   ;   _   $   u   S   {   }   .  BL   " [  ]
                {   1,  3,  4,  1, 12, 12,  F,  F, 13, 14, 14, 14, 14,  F,  F,  F,  F,  0,  0,  1,  1, 15,  0, 11,  0, 16, F, F}, // Estado 0
                {   1,  3,  2,  1,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  1,  F,  1,  1,  F,  F,  F,  F, F, F, F}, // Estado 1
                {   2,  F,  2,  2,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  2,  F,  2,  2,  F,  F,  F,  F, F, F, F}, // Estado 2
                {   3,  3,  F,  3,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  3,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 3
                {   F,  F,  4,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  5,  F,  F,  F,  F, 17,  F, F, F, F}, // Estado 4
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  6,  F,  F,  F,  F,  F, F, F, F}, // Estado 5
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  7,  F,  F,  F,  F, F, F, F}, // Estado 6
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 7
                {   F,  F,  8,  9,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 8
                {   F,  F, 10,  F, 19, 19,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 9
                {   F,  F, 10,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 10
                {   F,  F,  8,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 11
                {   F,  F, 18,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, 11,  F, F, F, F}, // Estado 12
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 13
                {   F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 14
                {  15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,  F, 15, 15, 15, 15, 15}, // Estado 15
                {  16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 0, 16, 16},// Estado 16
                {   F,  F,  8,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F}, // Estado 17
                {   F,  F, 18,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  11, F, F, F, F}, //Estado 18
                {   F,  F, 10,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F, F, F, F} // Estado 19
        };
     private static int[][] matriz_acciones = { // Matriz de Acciones Semánticas 
            // l   L   d  "d"  +   -   *     /   :   =   >   <   !   (     )   ,    ;   _   $   u   s   {   }   .  BL   "   [   ]
            {  1,  1,  1,  1,  1,  1,  16,  16,  1,  1,  1,  1,  1,  16,  16,  16,  1,  0,  0,  1,  1,  1,  0,  1,  0,  1 , 16, 16}, // Fila 0
            {  2,  2,  2,  2,  3,  3,   3,   3,  3,  3,  3,  3,  3,   3,   3,   3,  3,  2,  3,  2,  2,  3,  3,  3,  3,  3 ,  3,  3}, // Fila 1
            {  2,  4,  2,  2,  4,  4,   4,   4,  4,  4,  4,  4,  4,   4,   4,   4,  4,  2,  4,  2,  2,  4,  4,  4,  4,  4 ,  4,  4}, // Fila 2
            {  2,  2,  5,  2,  5,  5,  5,    5,  5,  5,  5,  5,  5,   5,   5,   5,  5,  2,  5,  2,  2,  5,  5,  5,  5,  5 ,  5,  5}, // Fila 3
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  2,  0,  0,  0,  0,  2,  0,  0 ,  0,  0}, // Fila 4
            {  0,  0,  0,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  2,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 5
            {  0,  0,  0,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  2,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 6
            {  8,  8,  8,  8,  8,  8,  8,    8,  8,  8,  8,  8,  8,   8,   8,   8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8 ,  8,  8}, // Fila 7
            {  9,  9,  2,  2,  9,  9,  9,    9,  9,  9,  9,  9,  9,   9,   9,   9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9 ,  9,  9}, // Fila 8
            {  0,  0,  2,  0,  2,  2,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 9
            {  9,  9,  2,  9,  9,  9,  9,    9,  9,  9,  9,  9,  9,   9,   9,   9,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9 ,  9,  9}, // Fila 10
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 11
            {  6,  6,  2,  6,  6,  6,  6,    6,  6,  6,  6,  6,  6,   6,   6,   6,  6,  6,  6,  6,  6,  6,  6,  2,  6,  6 ,  6,  6}, // Fila 12
            {  6,  6,  6,  6,  6,  6,  6,    6,  6,  7,  6,  6,  6,   6,   6,   6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6 ,  6,  6}, // Fila 13
            {  6,  6,  6,  6,  6,  6,  6,    6,  6,  7,  6,  6,  6,   6,   6,   6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6 ,  6,  6}, // Fila 14
            {  2,  2,  2,  2,  2,  2,  2,    2,  2,  2,  2,  2,  2,   2,   2,   2,  2,  2,  2,  2,  2,  2, 11,  2,  11, 2 ,  2,  2}, // Fila 15
            {  2,  2,  2,  2,  2,  2,  2,    2,  2,  2,  2,  2,  2,   2,   2,   2,  2,  2,  2,  2,  2,  2,  2,  2,  2, 12 ,  2,  2}, // Fila 16
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}, // Fila 17
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  2,  0,  0 ,  0,  0}, // Fila 18
            {  0,  0,  2,  0,  0,  0,  0,    0,  0,  0,  0,  0,  0,   0,   0,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 ,  0,  0}  // Fila 19
    };

    public AnalizadorLexico (LecturaCodigo lectorCodigo) { 
            this.lectorCodigo = lectorCodigo;
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

            default: return -1; // Caracter invalido
            }
    }

    public Token getToken() {
        int estadoActual = 0;
        lexema.setLength(0);
        Token token = null;

        while (token == null) {
            char caracterLeido = lectorCodigo.leerSiguienteCaracter();
            
            if (caracterLeido == LecturaCodigo.EOF) { // Fin de archivo - EOF
                
                if (estadoActual == 0) { // Fin de archivo
                    lectorCodigo.cerrar();
                    return new Token(0, "EOF");
                }

                if (estadoActual == 16) { // Comentario multilinea sin cerrar y EOF
                    return ejecutarAccionSemantica(15, LecturaCodigo.EOF);
                }
                
                int accionEOF = matriz_acciones[estadoActual][24]; // Token a medio formar y EOF
                return ejecutarAccionSemantica(accionEOF, LecturaCodigo.EOF);
            }

            int columna = obtenerColumna(caracterLeido);

            if (columna == 24 && estadoActual == 0) { // Ignorar espacios, tabulaciones y saltos de línea en reposo (Estado 0)
                lexema.setLength(0);
                continue;
            }

            if (columna == -1 && estadoActual == 16) { // Comentario multilinea y Caracter invalido
                ejecutarAccionSemantica(2, caracterLeido); 
                continue; 
            }

            if (columna == -1) { // Caracter invalido
                System.err.println("Linea " + lectorCodigo.getLineaActual() + ": Caracter invalido:" + caracterLeido );
                while (!(columna == 24)){
                    caracterLeido = lectorCodigo.leerSiguienteCaracter();
                    columna = obtenerColumna(caracterLeido);
                }
                lexema.setLength(0);
                estadoActual = 0;
                continue;
            }

            int accion = matriz_acciones[estadoActual][columna];
            int estadoSiguiente = matriz_estados[estadoActual][columna];

            token = ejecutarAccionSemantica(accion, caracterLeido);

            if (estadoSiguiente == F || (token == null && lexema.length() == 0)) { // Control de transición y recuperación
                estadoActual = 0;
            } 
            else {
                estadoActual = estadoSiguiente;
            }
        }

        return token;
    }

    private Token ejecutarAccionSemantica(int accion, char c) {
        switch (accion) {

            case 0: // AS0 - Estructura mal formada
                boolean errorEnEstadoCero = (lexema.length() == 0); // Si lexema está vacío, ocurrió en reposo

                lexema.append(c);
                System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Estructura mal formada: " + lexema);

                if (!errorEnEstadoCero && c != LecturaCodigo.EOF) { //Si el error ocurrió en Estado 0
                    lectorCodigo.devolverCaracter();
                }

                lexema.setLength(0);
                return null;   
            
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
                    return new Token(tablaPR.obtenerId(textoAS3), textoAS3);
                }

                if (textoAS3.length() > 22) { 
                    textoAS3 = textoAS3.substring(0, 22);
                    System.out.println("Línea " + lectorCodigo.getLineaActual() + ": Warning - Identificador supera el límite permitido; se trunca a 22 caracteres: " + textoAS3);
                }

                tablaSimbolos.agregarIdentificador(textoAS3, lectorCodigo.getLineaActual());
                return new Token(Token.ID, textoAS3);

            case 4: // AS4: Estado Final -> Identificador. Al tener digitos no puede ser PR
                lectorCodigo.devolverCaracter();
                String textoAS4 = lexema.toString();
                lexema.setLength(0); 

                if (textoAS4.length() > 22) {
                    textoAS4 = textoAS4.substring(0, 22);
                    System.out.println("Línea " + lectorCodigo.getLineaActual() + ": Warning - Identificador supera el límite permitido; se trunca a 22 caracteres: " + textoAS4);
                }

                tablaSimbolos.agregarIdentificador(textoAS4, lectorCodigo.getLineaActual());
                return new Token(Token.ID, textoAS4);

            case 5: // AS5: Palabra reservada. Al tener mayusculas no es Identificador
                lectorCodigo.devolverCaracter(); 
                String textoAS5 = lexema.toString();
                lexema.setLength(0); 

                if (tablaPR.esPalabraReservada(textoAS5)) {
                        return new Token(tablaPR.obtenerId(textoAS5), textoAS5);
                    }

                System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Identificador inválido con mayúsculas: " + textoAS5 );
                return null; 

            case 6: // AS6: Estado Final -> Operador simple (:, +, -, >, <, =)
                lectorCodigo.devolverCaracter();
                char opSimple = lexema.charAt(0); 
                lexema.setLength(0);            
                return new Token((int) opSimple); 

            case 7: // AS7: Estado Final -> Operador Compuesto (>=, <=, ==, !=, :=)
                lexema.append(c);                 
                String opCompuesto = lexema.toString();            

                if (opCompuesto.equals(">=")) 
                        return new Token(Token.MAYOR_IGUAL);
                if (opCompuesto.equals("<=")) 
                        return new Token(Token.MENOR_IGUAL); 
                if (opCompuesto.equals("==")) 
                        return new Token(Token.IGUAL_IGUAL); 
                if (opCompuesto.equals("!=")) 
                        return new Token(Token.DISTINTO);    
                if (opCompuesto.equals(":=")) 
                        return new Token(Token.ASIGNACION);
                  
                lexema.setLength(0); 
                return null;

            case 8: // AS8: Estado Final -> USHORTINT
                if (c != LecturaCodigo.EOF) {
                    lectorCodigo.devolverCaracter(); 
                }

                String lexUs = lexema.toString(); 
                lexema.setLength(0);

                String numero = lexUs.substring(0, lexUs.length() - 3);  // Quita el "$us"(longitud - 3)

                int valorInt = Integer.parseInt(numero);
                if (valorInt < 0 || valorInt > 255) { // se valida el rango [0, 255] 
                    System.err.println("Línea " + lectorCodigo.getLineaActual() +  ": Constante USHORTINT fuera de rango [0, 255]: " + valorInt);
                    return null; 
                }
                
                tablaSimbolos.agregarConstante(lexUs, "USHORTINT", lectorCodigo.getLineaActual());
                return new Token(Token.CTE, lexUs);
            
            case 9: // AS9: Estado Final -> DOUBLEF
                if (c != LecturaCodigo.EOF) {
                    lectorCodigo.devolverCaracter();
                }

                String lexDouble = lexema.toString();
                lexema.setLength(0);

                String expJava = lexDouble.replace('d', 'e').replace('D', 'e');

                java.math.BigDecimal valor = new java.math.BigDecimal(expJava);

                boolean valido = (valor.compareTo(cero) == 0) || (valor.compareTo(minPos) > 0 && valor.compareTo(maxPos) < 0) || (valor.compareTo(minNeg) > 0 && valor.compareTo(maxNeg) < 0);

                if (!valido) {
                    System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Constante DOUBLEF fuera de rango: " + lexDouble);
                    return null;
                }

                tablaSimbolos.agregarConstante(lexDouble, "DOUBLEF", lectorCodigo.getLineaActual());
                return new Token(Token.CTE, lexDouble);


            case 11: // AS11: Estado Final -> Cadena de una línea {...} 
                if (c == '\n' || c == '\r') { // Error: si vino un salto de línea antes de cerrar
                    if (lexema.length() > 0) {
                                System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Cadena de una línea sin cerrar : " + lexema );
                            }
                    lexema.setLength(0);
                    return null; 
                }

                if (c == '}') { // Cierre de cadena de una línea
                    lexema.append(c);
                    String textoCadena = lexema.toString();
                    lexema.setLength(0);

                    tablaSimbolos.agregarConstante(textoCadena, "CADENA", lectorCodigo.getLineaActual());
                    return new Token(Token.CADENA, textoCadena);
                }

                lexema.append(c);
                break;

            case 12: // AS12: Estado Final -> Comentario multilínea "..."
                lexema.setLength(0); 
                return null;

            case 14: // AS14: Fin del archivo | Identificador o Palabra Reservada
                String textoAS14 = lexema.toString();
                lexema.setLength(0);

                if (tablaPR.esPalabraReservada(textoAS14)) {
                    return new Token(tablaPR.obtenerId(textoAS14), textoAS14);
                }

                if (textoAS14.length() > 22) {
                    textoAS14 = textoAS14.substring(0, 22);
                    System.out.println("Línea " + lectorCodigo.getLineaActual() + ": Warning - Identificador supera el límite permitido; se trunca a 22 caracteres: " + textoAS14);
                }
                
                tablaSimbolos.agregarIdentificador(textoAS14, lectorCodigo.getLineaActual());
                return new Token(Token.ID, textoAS14);
            
            
            case 15: // AS15: Comentario multilinea sin cerrar y EOF
                System.err.println("Línea " + lectorCodigo.getLineaActual() + ": Comentario sin cerrar al alcanzar fin de archivo");
                
                lexema.setLength(0); 
                lectorCodigo.cerrar();
                return new Token(0, "EOF");
            
            case 16: // AS16: Token operador simple ('+', '-', '*', '/', '(', ')', ',', ';')
                lexema.setLength(0);   
                return new Token((int) c); 
            
            }   
        return null;
    }

    public TablaDeSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }
}
*/