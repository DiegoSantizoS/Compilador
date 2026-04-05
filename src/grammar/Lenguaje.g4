grammar Lenguaje;
@parser::header{
    import java.util.Map;
    import java.util.HashMap;
}
@parser::members{
    Map<String, Double> symbolTable = new HashMap<String, Double>();
    }
/* =========================
   REGLAS DE PARSER
    ========================= */
    
    programa
    : sentencia* EOF
    ;
sentencia
    : declaracion FIN_SENT { System.out.println("Sentencia declaración"); }
    | asignacion  FIN_SENT { System.out.println("Sentencia asignación"); }
    | imprimir    FIN_SENT { System.out.println("Sentencia imprimir"); }
    | si
    | mientras
    | bloque
    ;

// Declaración: entero x;  entero x = 10;
declaracion
    : tipo ID (IGUAL e=expresion)?
{
    if ($e.ctx != null)
        symbolTable.put($ID.text, $e.value);
    else
        symbolTable.put($ID.text, 0.0);
    }
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
    { symbolTable.put($ID.text, $expresion.value); }
    ;

// imprimir expr;
imprimir
    : IMPRIMIR e=expresion
    { System.out.println($e.value); }
    ;

// si (cond) { ... } sino { ... }
si
    : SI PAR_IZQ expresion PAR_DER bloque
    { System.out.println("Estructura SI"); }
    (SINO bloque
    { System.out.println("Tiene SINO"); }
    )?
    ;

// mientras (cond) { ... }
mientras
    : MIENTRAS PAR_IZQ expresion PAR_DER bloque
    { System.out.println("Bucle MIENTRAS"); }
    ;

// { ... }  (permito sentencias con o sin ';' dentro del bloque)
bloque
    : LLAVE_IZQ
    { System.out.println("Inicio bloque"); }
    sentenciaBloque*
    LLAVE_DER
    { System.out.println("Fin bloque"); }
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
   EXPRESIONES
   De mayor a menor:
   1) () literales id leer
   2) unario: ! -
   3) * /
   4) + -
   5) comparaciones: < <= > >= == !=
   6) && 
   7) ||
========================= */


expresion returns [double value]
    : o=orExpr { $value = $o.value; }
    ;

orExpr returns [double value]
    : a1=andExpr { $value = $a1.value; }
    (O_LOGICO a2=andExpr { $value = ($value != 0 || $a2.value != 0) ? 1 : 0; })*
    ;

andExpr returns [double value]
    : an1=igualdad { $value = $an1.value; }
    (Y_LOGICO an2=igualdad 
    { $value = ($value != 0 && $an2.value != 0) ? 1 : 0; }
    )*
    ;

igualdad returns [double value]
    : c1=comparacion { $value = $c1.value; }
    (
    IGUAL_IGUAL c2=comparacion { $value = ($value == $c2.value) ? 1 : 0; }
    | DIFERENTE  c2=comparacion { $value = ($value != $c2.value) ? 1 : 0; }
    )*
    ;

comparacion returns [double value]
    : s1=suma { $value = $s1.value; }
    (
    MENOR s2=suma        { $value = ($value < $s2.value) ? 1 : 0; }
    | MENOR_IGUAL s2=suma { $value = ($value <= $s2.value) ? 1 : 0; }
    | MAYOR s2=suma       { $value = ($value > $s2.value) ? 1 : 0; }
    | MAYOR_IGUAL s2=suma { $value = ($value >= $s2.value) ? 1 : 0; }
    )*
    ;

suma returns [double value]
    : m1=mult { $value = $m1.value; }
    (MAS m2=mult { $value += $m2.value; }
    |MENOS m2=mult { $value -= $m2.value; }
    )*
    ;

mult returns [double value]
    : u1=unario { $value = $u1.value; }
    (POR u2=unario { $value *= $u2.value; }
    |DIV u2=unario { $value /= $u2.value; }
    )*
    ;

unario returns [double value]
    : MENOS u=unario { $value = -$u.value; }
    | NO u=unario    { $value = ($u.value == 0) ? 1 : 0; }
    | primario       { $value = $primario.value; }
    ;

primario returns [double value]
    : NUMERO { $value = Double.parseDouble($NUMERO.text); }
    | ID {
    if(symbolTable.containsKey($ID.text))
        $value = symbolTable.get($ID.text);
    else {
        System.out.println("Variable no definida: " + $ID.text);
        $value = 0;
    }
    }
    | PAR_IZQ e=expresion PAR_DER { $value = $e.value; }
    ;

leer
    : LEER PAR_IZQ PAR_DER
    ;


/* =========================
   REGLAS DE LEXER
========================= */

/* --- Palabras reservadas --- */
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