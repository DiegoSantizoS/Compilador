grammar Lenguaje;

@parser::header{
    import java.util.*;
}

@parser::members{

    static class Simbolo {
        String nombre;
        String tipo;
        Object valor;
        boolean inicializada;

        Simbolo(String nombre, String tipo, Object valor, boolean inicializada) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.valor = valor;
            this.inicializada = inicializada;
        }
    }

    static class FuncionInfo {
        String nombre;
        String tipoRetorno;
        List<String> tiposParametros = new ArrayList<>();

        FuncionInfo(String nombre, String tipoRetorno) {
            this.nombre = nombre;
            this.tipoRetorno = tipoRetorno;
        }
    }

    static class ValorSemantico {
        String tipo;
        Object valor;

        ValorSemantico(String tipo, Object valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    Stack<Map<String, Simbolo>> pilaAmbitos = new Stack<>();
    Map<String, FuncionInfo> tablaFunciones = new HashMap<>();
    List<String> erroresSemanticos = new ArrayList<>();

    String funcionActual = null;
    String tipoRetornoActual = null;

    void iniciarAmbito() {
        pilaAmbitos.push(new HashMap<>());
    }

    void cerrarAmbito() {
        if (!pilaAmbitos.isEmpty()) {
            pilaAmbitos.pop();
        }
    }

    boolean existeEnAmbitoActual(String id) {
        if (pilaAmbitos.isEmpty()) return false;
        return pilaAmbitos.peek().containsKey(id);
    }

    Simbolo buscarSimbolo(String id) {
        for (int i = pilaAmbitos.size() - 1; i >= 0; i--) {
            Map<String, Simbolo> ambito = pilaAmbitos.get(i);
            if (ambito.containsKey(id)) {
                return ambito.get(id);
            }
        }
        return null;
    }

    void declararVariable(String id, String tipo, Object valor, boolean inicializada) {
        if (existeEnAmbitoActual(id)) {
            erroresSemanticos.add("Error semántico: la variable '" + id + "' ya fue declarada en este ámbito.");
            return;
        }
        pilaAmbitos.peek().put(id, new Simbolo(id, tipo, valor, inicializada));
    }

    void asignarVariable(String id, ValorSemantico valor) {
        Simbolo s = buscarSimbolo(id);
        if (s == null) {
            erroresSemanticos.add("Error semántico: la variable '" + id + "' no ha sido declarada.");
            return;
        }

        if (!tiposCompatiblesAsignacion(s.tipo, valor.tipo)) {
            erroresSemanticos.add("Error semántico: no se puede asignar un valor de tipo '" + valor.tipo
                    + "' a la variable '" + id + "' de tipo '" + s.tipo + "'.");
            return;
        }

        s.valor = convertirValor(valor, s.tipo);
        s.inicializada = true;
    }

    boolean tiposCompatiblesAsignacion(String destino, String origen) {
        if (destino.equals(origen)) return true;
        if (destino.equals("real") && origen.equals("entero")) return true;
        return false;
    }

    Object convertirValor(ValorSemantico valor, String tipoDestino) {
        if (valor == null) return null;
        if (tipoDestino.equals("real") && valor.tipo.equals("entero")) {
            return ((Number) valor.valor).doubleValue();
        }
        return valor.valor;
    }

    boolean esNumerico(String tipo) {
        return "entero".equals(tipo) || "real".equals(tipo);
    }

    boolean esBooleano(String tipo) {
        return "booleano".equals(tipo);
    }

    boolean esCadena(String tipo) {
        return "cadena".equals(tipo);
    }

    String tipoDominanteNumerico(String a, String b) {
        if ("real".equals(a) || "real".equals(b)) return "real";
        return "entero";
    }

    double aDouble(Object v) {
        if (v instanceof Integer) return ((Integer) v).doubleValue();
        if (v instanceof Double) return (Double) v;
        if (v instanceof Boolean) return ((Boolean) v) ? 1.0 : 0.0;
        return 0.0;
    }

    int aEntero(Object v) {
        if (v instanceof Integer) return (Integer) v;
        if (v instanceof Double) return ((Double) v).intValue();
        if (v instanceof Boolean) return ((Boolean) v) ? 1 : 0;
        return 0;
    }

    boolean aBooleano(Object v) {
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof Integer) return ((Integer) v) != 0;
        if (v instanceof Double) return ((Double) v) != 0.0;
        return false;
    }

    String quitarComillas(String s) {
        if (s == null || s.length() < 2) return s;
        return s.substring(1, s.length() - 1);
    }

    void declararFuncion(String nombre, String tipoRetorno, List<String> tiposParametros) {
        if (tablaFunciones.containsKey(nombre)) {
            erroresSemanticos.add("Error semántico: la función '" + nombre + "' ya fue declarada.");
            return;
        }
        FuncionInfo f = new FuncionInfo(nombre, tipoRetorno);
        f.tiposParametros.addAll(tiposParametros);
        tablaFunciones.put(nombre, f);
    }

    ValorSemantico validarLlamadaFuncion(String nombre, List<ValorSemantico> args) {
        if (!tablaFunciones.containsKey(nombre)) {
            erroresSemanticos.add("Error semántico: la función '" + nombre + "' no ha sido declarada.");
            return new ValorSemantico("error", null);
        }

        FuncionInfo f = tablaFunciones.get(nombre);

        if (f.tiposParametros.size() != args.size()) {
            erroresSemanticos.add("Error semántico: la función '" + nombre + "' esperaba "
                    + f.tiposParametros.size() + " parámetro(s), pero recibió " + args.size() + ".");
            return new ValorSemantico(f.tipoRetorno, null);
        }

        for (int i = 0; i < args.size(); i++) {
            String esperado = f.tiposParametros.get(i);
            String recibido = args.get(i).tipo;
            if (!tiposCompatiblesAsignacion(esperado, recibido)) {
                erroresSemanticos.add("Error semántico: parámetro " + (i + 1) + " de la función '" + nombre
                        + "' esperaba tipo '" + esperado + "', pero recibió '" + recibido + "'.");
            }
        }

        return new ValorSemantico(f.tipoRetorno, null);
    }

    List<String> obtenerErroresSemanticos() {
        return erroresSemanticos;
    }
}

/* =========================
   PARSER
========================= */

programa
    : {
          iniciarAmbito();
      }
      elemento*
      EOF
      {
          cerrarAmbito();
      }
    ;

elemento
    : sentencia
    | funcion
    ;

sentencia
    : declaracion FIN_SENT
    | asignacion  FIN_SENT
    | imprimir    FIN_SENT
    | retornar    FIN_SENT
    | si
    | mientras
    | bloque
    | llamadaFuncion FIN_SENT
    ;

/* =========================
   DECLARACIONES
========================= */

declaracion
    : tipo ID (IGUAL e=expresion)?
      {
          if ($e.ctx != null) {
              if (!tiposCompatiblesAsignacion($tipo.text, $e.info.tipo)) {
                  erroresSemanticos.add("Error semántico: no se puede inicializar '" + $ID.text
                          + "' de tipo '" + $tipo.text + "' con una expresión de tipo '" + $e.info.tipo + "'.");
              }
              declararVariable($ID.text, $tipo.text, convertirValor($e.info, $tipo.text), true);
          } else {
              declararVariable($ID.text, $tipo.text, null, false);
          }
      }
    ;

tipo
    : ENTERO
    | REAL
    | CADENA
    | BOOLEANO
    | VACIO
    ;

/* =========================
   ASIGNACIÓN
========================= */

asignacion
    : ID IGUAL e=expresion
      {
          asignarVariable($ID.text, $e.info);
      }
    ;

/* =========================
   IMPRESIÓN / RETORNO
========================= */

imprimir
    : IMPRIMIR e=expresion
    ;

retornar
    : RETORNAR e=expresion?
      {
          if (tipoRetornoActual == null) {
              erroresSemanticos.add("Error semántico: 'retornar' usado fuera de una función.");
          } else {
              if ("vacio".equals(tipoRetornoActual)) {
                  if ($e.ctx != null) {
                      erroresSemanticos.add("Error semántico: la función '" + funcionActual + "' es de tipo 'vacio' y no debe retornar valor.");
                  }
              } else {
                  if ($e.ctx == null) {
                      erroresSemanticos.add("Error semántico: la función '" + funcionActual + "' debe retornar un valor de tipo '" + tipoRetornoActual + "'.");
                  } else if (!tiposCompatiblesAsignacion(tipoRetornoActual, $e.info.tipo)) {
                      erroresSemanticos.add("Error semántico: la función '" + funcionActual + "' debe retornar tipo '" + tipoRetornoActual
                              + "', pero retorna '" + $e.info.tipo + "'.");
                  }
              }
          }
      }
    ;

/* =========================
   CONTROL
========================= */

si
    : SI PAR_IZQ c=expresion PAR_DER b1=bloque (SINO b2=bloque)?
      {
          if (!"booleano".equals($c.info.tipo)) {
              erroresSemanticos.add("Error semántico: la condición de 'si' debe ser de tipo booleano.");
          }
      }
    ;

mientras
    : MIENTRAS PAR_IZQ c=expresion PAR_DER b=bloque
      {
          if (!"booleano".equals($c.info.tipo)) {
              erroresSemanticos.add("Error semántico: la condición de 'mientras' debe ser de tipo booleano.");
          }
      }
    ;

bloque
    : LLAVE_IZQ
      {
          iniciarAmbito();
      }
      sentenciaBloque*
      LLAVE_DER
      {
          cerrarAmbito();
      }
    ;

sentenciaBloque
    : declaracion FIN_SENT?
    | asignacion  FIN_SENT?
    | imprimir    FIN_SENT?
    | retornar    FIN_SENT?
    | si
    | mientras
    | bloque
    | llamadaFuncion FIN_SENT?
    ;

/* =========================
   FUNCIONES
========================= */

funcion
    : tipo ID
      PAR_IZQ params=parametros? PAR_DER
      {
          List<String> listaTipos = new ArrayList<>();
          if ($params.ctx != null) listaTipos = $params.listaTipos;
          declararFuncion($ID.text, $tipo.text, listaTipos);

          funcionActual = $ID.text;
          tipoRetornoActual = $tipo.text;

          iniciarAmbito();

          if ($params.ctx != null) {
              for (int i = 0; i < $params.nombres.size(); i++) {
                  declararVariable($params.nombres.get(i), $params.listaTipos.get(i), null, true);
              }
          }
      }
      LLAVE_IZQ sentenciaBloque* LLAVE_DER
      {
          cerrarAmbito();
          funcionActual = null;
          tipoRetornoActual = null;
      }
    ;

parametros returns [List<String> listaTipos, List<String> nombres]
    : p1=parametro
      {
          $listaTipos = new ArrayList<>();
          $nombres = new ArrayList<>();
          $listaTipos.add($p1.tipoParam);
          $nombres.add($p1.nombreParam);
      }
      (
          COMA p2=parametro
          {
              $listaTipos.add($p2.tipoParam);
              $nombres.add($p2.nombreParam);
          }
      )*
    ;

parametro returns [String tipoParam, String nombreParam]
    : t=tipo ID
      {
          $tipoParam = $t.text;
          $nombreParam = $ID.text;
      }
    ;

llamadaFuncion returns [ValorSemantico info]
    : ID PAR_IZQ a=argumentos? PAR_DER
      {
          List<ValorSemantico> listaArgs = new ArrayList<>();
          if ($a.ctx != null) listaArgs = $a.lista;
          $info = validarLlamadaFuncion($ID.text, listaArgs);
      }
    ;

argumentos returns [List<ValorSemantico> lista]
    : e1=expresion
      {
          $lista = new ArrayList<>();
          $lista.add($e1.info);
      }
      (
          COMA e2=expresion
          {
              $lista.add($e2.info);
          }
      )*
    ;

/* =========================
   EXPRESIONES
========================= */

expresion returns [ValorSemantico info]
    : o=orExpr { $info = $o.info; }
    ;

orExpr returns [ValorSemantico info]
    : a1=andExpr
      {
          $info = $a1.info;
      }
      (
          O_LOGICO a2=andExpr
          {
              if (!esBooleano($info.tipo) || !esBooleano($a2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '||' requiere operandos booleanos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("booleano",
                          aBooleano($info.valor) || aBooleano($a2.info.valor));
              }
          }
      )*
    ;

andExpr returns [ValorSemantico info]
    : a1=igualdad
      {
          $info = $a1.info;
      }
      (
          Y_LOGICO a2=igualdad
          {
              if (!esBooleano($info.tipo) || !esBooleano($a2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '&&' requiere operandos booleanos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("booleano",
                          aBooleano($info.valor) && aBooleano($a2.info.valor));
              }
          }
      )*
    ;

igualdad returns [ValorSemantico info]
    : c1=comparacion
      {
          $info = $c1.info;
      }
      (
          IGUAL_IGUAL c2=comparacion
          {
              if ($info.tipo.equals("error") || $c2.info.tipo.equals("error")) {
                  $info = new ValorSemantico("error", null);
              } else if ($info.tipo.equals($c2.info.tipo)
                      || (esNumerico($info.tipo) && esNumerico($c2.info.tipo))) {
                  boolean r;
                  if (esCadena($info.tipo) && esCadena($c2.info.tipo)) {
                      r = String.valueOf($info.valor).equals(String.valueOf($c2.info.valor));
                  } else {
                      r = aDouble($info.valor) == aDouble($c2.info.valor);
                  }
                  $info = new ValorSemantico("booleano", r);
              } else {
                  erroresSemanticos.add("Error semántico: operador '==' con tipos incompatibles '" + $info.tipo + "' y '" + $c2.info.tipo + "'.");
                  $info = new ValorSemantico("error", null);
              }
          }
        | DIFERENTE c2=comparacion
          {
              if ($info.tipo.equals("error") || $c2.info.tipo.equals("error")) {
                  $info = new ValorSemantico("error", null);
              } else if ($info.tipo.equals($c2.info.tipo)
                      || (esNumerico($info.tipo) && esNumerico($c2.info.tipo))) {
                  boolean r;
                  if (esCadena($info.tipo) && esCadena($c2.info.tipo)) {
                      r = !String.valueOf($info.valor).equals(String.valueOf($c2.info.valor));
                  } else {
                      r = aDouble($info.valor) != aDouble($c2.info.valor);
                  }
                  $info = new ValorSemantico("booleano", r);
              } else {
                  erroresSemanticos.add("Error semántico: operador '!=' con tipos incompatibles '" + $info.tipo + "' y '" + $c2.info.tipo + "'.");
                  $info = new ValorSemantico("error", null);
              }
          }
      )*
    ;

comparacion returns [ValorSemantico info]
    : s1=suma
      {
          $info = $s1.info;
      }
      (
          MENOR s2=suma
          {
              if (!esNumerico($info.tipo) || !esNumerico($s2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '<' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("booleano", aDouble($info.valor) < aDouble($s2.info.valor));
              }
          }
        | MENOR_IGUAL s2=suma
          {
              if (!esNumerico($info.tipo) || !esNumerico($s2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '<=' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("booleano", aDouble($info.valor) <= aDouble($s2.info.valor));
              }
          }
        | MAYOR s2=suma
          {
              if (!esNumerico($info.tipo) || !esNumerico($s2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '>' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("booleano", aDouble($info.valor) > aDouble($s2.info.valor));
              }
          }
        | MAYOR_IGUAL s2=suma
          {
              if (!esNumerico($info.tipo) || !esNumerico($s2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '>=' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("booleano", aDouble($info.valor) >= aDouble($s2.info.valor));
              }
          }
      )*
    ;

suma returns [ValorSemantico info]
    : m1=mult
      {
          $info = $m1.info;
      }
      (
          MAS m2=mult
          {
              if (esCadena($info.tipo) && esCadena($m2.info.tipo)) {
                  $info = new ValorSemantico("cadena", String.valueOf($info.valor) + String.valueOf($m2.info.valor));
              } else if (esNumerico($info.tipo) && esNumerico($m2.info.tipo)) {
                  String tipoRes = tipoDominanteNumerico($info.tipo, $m2.info.tipo);
                  if ("real".equals(tipoRes)) {
                      $info = new ValorSemantico("real", aDouble($info.valor) + aDouble($m2.info.valor));
                  } else {
                      $info = new ValorSemantico("entero", aEntero($info.valor) + aEntero($m2.info.valor));
                  }
              } else {
                  erroresSemanticos.add("Error semántico: operador '+' con tipos incompatibles '" + $info.tipo + "' y '" + $m2.info.tipo + "'.");
                  $info = new ValorSemantico("error", null);
              }
          }
        | MENOS m2=mult
          {
              if (!esNumerico($info.tipo) || !esNumerico($m2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '-' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  String tipoRes = tipoDominanteNumerico($info.tipo, $m2.info.tipo);
                  if ("real".equals(tipoRes)) {
                      $info = new ValorSemantico("real", aDouble($info.valor) - aDouble($m2.info.valor));
                  } else {
                      $info = new ValorSemantico("entero", aEntero($info.valor) - aEntero($m2.info.valor));
                  }
              }
          }
      )*
    ;

mult returns [ValorSemantico info]
    : u1=unario
      {
          $info = $u1.info;
      }
      (
          POR u2=unario
          {
              if (!esNumerico($info.tipo) || !esNumerico($u2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '*' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else {
                  String tipoRes = tipoDominanteNumerico($info.tipo, $u2.info.tipo);
                  if ("real".equals(tipoRes)) {
                      $info = new ValorSemantico("real", aDouble($info.valor) * aDouble($u2.info.valor));
                  } else {
                      $info = new ValorSemantico("entero", aEntero($info.valor) * aEntero($u2.info.valor));
                  }
              }
          }
        | DIV u2=unario
          {
              if (!esNumerico($info.tipo) || !esNumerico($u2.info.tipo)) {
                  erroresSemanticos.add("Error semántico: operador '/' requiere operandos numéricos.");
                  $info = new ValorSemantico("error", null);
              } else if (aDouble($u2.info.valor) == 0.0) {
                  erroresSemanticos.add("Error semántico: división entre cero.");
                  $info = new ValorSemantico("error", null);
              } else {
                  $info = new ValorSemantico("real", aDouble($info.valor) / aDouble($u2.info.valor));
              }
          }
      )*
    ;

unario returns [ValorSemantico info]
    : MENOS u=unario
      {
          if (!esNumerico($u.info.tipo)) {
              erroresSemanticos.add("Error semántico: operador unario '-' requiere operando numérico.");
              $info = new ValorSemantico("error", null);
          } else if ("real".equals($u.info.tipo)) {
              $info = new ValorSemantico("real", -aDouble($u.info.valor));
          } else {
              $info = new ValorSemantico("entero", -aEntero($u.info.valor));
          }
      }
    | NO u=unario
      {
          if (!esBooleano($u.info.tipo)) {
              erroresSemanticos.add("Error semántico: operador '!' requiere operando booleano.");
              $info = new ValorSemantico("error", null);
          } else {
              $info = new ValorSemantico("booleano", !aBooleano($u.info.valor));
          }
      }
    | p=primario
      {
          $info = $p.info;
      }
    ;

primario returns [ValorSemantico info]
    : PAR_IZQ e=expresion PAR_DER
      {
          $info = $e.info;
      }
    | NUMERO
      {
          if ($NUMERO.text.contains(".")) {
              $info = new ValorSemantico("real", Double.parseDouble($NUMERO.text));
          } else {
              $info = new ValorSemantico("entero", Integer.parseInt($NUMERO.text));
          }
      }
    | VERDADERO
      {
          $info = new ValorSemantico("booleano", true);
      }
    | FALSO
      {
          $info = new ValorSemantico("booleano", false);
      }
    | CADENA_LIT
      {
          $info = new ValorSemantico("cadena", quitarComillas($CADENA_LIT.text));
      }
    | leer
      {
          $info = $leer.info;
      }
    | llamadaFuncion
      {
          $info = $llamadaFuncion.info;
      }
    | ID
      {
          Simbolo s = buscarSimbolo($ID.text);
          if (s == null) {
              erroresSemanticos.add("Error semántico: la variable '" + $ID.text + "' no ha sido declarada.");
              $info = new ValorSemantico("error", null);
          } else {
              $info = new ValorSemantico(s.tipo, s.valor);
          }
      }
    ;

leer returns [ValorSemantico info]
    : LEER PAR_IZQ PAR_DER
      {
          $info = new ValorSemantico("entero", 0);
      }
    ;

/* =========================
   LEXER
========================= */

SI         : 'si';
SINO       : 'sino';
MIENTRAS   : 'mientras';
IMPRIMIR   : 'imprimir';
LEER       : 'leer';
RETORNAR   : 'retornar';
VACIO      : 'vacio';

ENTERO     : 'entero';
REAL       : 'real';
CADENA     : 'cadena';
BOOLEANO   : 'booleano';

VERDADERO  : 'verdadero';
FALSO      : 'falso';

PAR_IZQ    : '(';
PAR_DER    : ')';
LLAVE_IZQ  : '{';
LLAVE_DER  : '}';
FIN_SENT   : ';';
COMA       : ',';

IGUAL        : '=';
IGUAL_IGUAL  : '==';
DIFERENTE    : '!=';
MENOR        : '<';
MENOR_IGUAL  : '<=';
MAYOR        : '>';
MAYOR_IGUAL  : '>=';

MAS          : '+';
MENOS        : '-';
POR          : '*';
DIV          : '/';

Y_LOGICO     : '&&';
O_LOGICO     : '||';
NO           : '!';

ID
    : [a-zA-Z_áéíóúÁÉÍÓÚñÑ][a-zA-Z_0-9áéíóúÁÉÍÓÚñÑ]*
    ;

NUMERO
    : [0-9]+ ('.' [0-9]+)?
    ;

CADENA_LIT
    : '"' ( ESC | ~["\\\r\n] )* '"'
    ;

fragment ESC
    : '\\' (["\\/bnrt] | 'u' HEX HEX HEX HEX)
    ;

fragment HEX
    : [0-9a-fA-F]
    ;
WS
    : [ \t\r\n]+ -> skip
    ;

COMENTARIO_LINEA
    : '//' ~[\r\n]* -> skip
    ;

COMENTARIO_BLOQUE
    : '/*' .*? '*/' -> skip
    ;