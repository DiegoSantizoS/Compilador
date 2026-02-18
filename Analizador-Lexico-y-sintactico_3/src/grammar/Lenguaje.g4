grammar Lenguaje;

/* =========================
   PARSER RULES
========================= */

programa
    : sentencia* EOF
    ;

sentencia
    : declaracion FIN_SENT
    | asignacion  FIN_SENT
    | imprimir    FIN_SENT
    | si
    | mientras
    | bloque
    ;

// Declaración: entero x;  entero x = 10;
declaracion
    : tipo ID (IGUAL expresion)?
    ;

tipo
    : ENTERO
    | REAL
    | CADENA
    | BOOLEANO
    ;

// Asignación: x = expr;
asignacion
    : ID IGUAL expresion
    ;

// imprimir expr;
imprimir
    : IMPRIMIR expresion
    ;

// si (cond) { ... } sino { ... }
si
    : SI PAR_IZQ expresion PAR_DER bloque (SINO bloque)?
    ;

// mientras (cond) { ... }
mientras
    : MIENTRAS PAR_IZQ expresion PAR_DER bloque
    ;

// { ... }  (permito sentencias con o sin ';' dentro del bloque)
bloque
    : LLAVE_IZQ sentenciaBloque* LLAVE_DER
    ;

sentenciaBloque
    : declaracion FIN_SENT?
    | asignacion  FIN_SENT?
    | imprimir    FIN_SENT?
    | si
    | mientras
    | bloque
    ;


/* =========================
   EXPRESIONES (precedencia)
   De mayor a menor:
   1) () literales id leer
   2) unario: ! -
   3) * /
   4) + -
   5) comparaciones: < <= > >= == !=
   6) && 
   7) ||
========================= */

expresion
    : orExpr
    ;

orExpr
    : andExpr (O_LOGICO andExpr)*
    ;

andExpr
    : igualdad (Y_LOGICO igualdad)*
    ;

igualdad
    : comparacion ((IGUAL_IGUAL | DIFERENTE) comparacion)*
    ;

comparacion
    : suma ((MENOR | MENOR_IGUAL | MAYOR | MAYOR_IGUAL) suma)*
    ;

suma
    : mult ((MAS | MENOS) mult)*
    ;

mult
    : unario ((POR | DIV) unario)*
    ;

unario
    : (NO | MENOS) unario
    | primario
    ;

primario
    : PAR_IZQ expresion PAR_DER
    | NUMERO
    | CADENA_LIT
    | VERDADERO
    | FALSO
    | leer
    | ID
    ;

leer
    : LEER PAR_IZQ PAR_DER
    ;


/* =========================
   LEXER RULES
========================= */

/* --- Palabras reservadas (español) --- */
SI        : 'si';
SINO      : 'sino';
MIENTRAS  : 'mientras';
IMPRIMIR  : 'imprimir';
LEER      : 'leer';

ENTERO    : 'entero';
REAL      : 'real';
CADENA    : 'cadena';
BOOLEANO  : 'booleano';

VERDADERO : 'verdadero';
FALSO     : 'falso';

/* --- Delimitadores --- */
PAR_IZQ   : '(';
PAR_DER   : ')';
LLAVE_IZQ : '{';
LLAVE_DER : '}';
FIN_SENT  : ';';

/* --- Operadores --- */
IGUAL      : '=';
IGUAL_IGUAL: '==';
DIFERENTE  : '!=';
MENOR      : '<';
MENOR_IGUAL: '<=';
MAYOR      : '>';
MAYOR_IGUAL: '>=';

MAS        : '+';
MENOS      : '-';
POR        : '*';
DIV        : '/';

Y_LOGICO   : '&&';
O_LOGICO   : '||';
NO         : '!';

/* --- Identificadores y literales --- */
ID
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

NUMERO
    : [0-9]+ ('.' [0-9]+)?
    ;

// Soporta escapes comunes: \" \\ \n \t \r
CADENA_LIT
    : '"' ( ESC | ~["\\\r\n] )* '"'
    ;

fragment ESC
    : '\\' (["\\/bnrt] | 'u' HEX HEX HEX HEX)
    ;

fragment HEX
    : [0-9a-fA-F]
    ;

/* --- Espacios y comentarios --- */
WS
    : [ \t\r\n]+ -> skip
    ;

COMENTARIO_LINEA
    : '//' ~[\r\n]* -> skip
    ;

COMENTARIO_BLOQUE
    : '/*' .*? '*/' -> skip
    ;
