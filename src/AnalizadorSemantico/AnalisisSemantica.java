package AnalizadorSemantico;

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import main_components.piccolo.TreeNodeModel;

public class AnalisisSemantica extends LenguajeBaseVisitor<Double> {

    private TreeNodeModel raizSemantica;
    private final Stack<TreeNodeModel> pilaNodos = new Stack<>();

    private static class Simbolo {
        String nombre;
        String tipo;
        Double valor;

        Simbolo(String nombre, String tipo, Double valor) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    private static class FuncionInfo {
        String nombre;
        String tipoRetorno;
        List<String> tiposParametros;

        FuncionInfo(String nombre, String tipoRetorno, List<String> tiposParametros) {
            this.nombre = nombre;
            this.tipoRetorno = tipoRetorno;
            this.tiposParametros = tiposParametros;
        }
    }

    private final Stack<Map<String, Simbolo>> pilaAmbitos = new Stack<>();
    private final Map<String, FuncionInfo> tablaFunciones = new HashMap<>();
    private final List<String> errores = new ArrayList<>();
    private final JTextPane terminal;

    private String funcionActual = null;
    private String tipoRetornoActual = null;

    public AnalisisSemantica(JTextPane terminal) {
        this.terminal = terminal;
        entrarAmbito();
    }

    public TreeNodeModel getArbolSemantico() {
        return raizSemantica;
    }

    public Map<String, Double> getTabla() {
        Map<String, Double> tablaPlana = new HashMap<>();
        for (Map<String, Simbolo> ambito : pilaAmbitos) {
            for (Simbolo s : ambito.values()) {
                tablaPlana.put(s.nombre, s.valor);
            }
        }
        return tablaPlana;
    }

    public Map<String, String> getTipos() {
        Map<String, String> tiposPlanos = new HashMap<>();
        for (Map<String, Simbolo> ambito : pilaAmbitos) {
            for (Simbolo s : ambito.values()) {
                tiposPlanos.put(s.nombre, s.tipo);
            }
        }
        return tiposPlanos;
    }

    public List<String> getErrores() {
        return errores;
    }

    private TreeNodeModel crearNodo(String texto) {
        TreeNodeModel nodo = new TreeNodeModel(texto);

        if (pilaNodos.isEmpty()) {
            raizSemantica = nodo;
        } else {
            pilaNodos.peek().addChild(nodo);
        }

        return nodo;
    }

    private TreeNodeModel crearNodoAnotado(String base, String anotacion) {
        if (anotacion == null || anotacion.isEmpty()) {
            return crearNodo(base);
        }
        return crearNodo(base + " [" + anotacion + "]");
    }

    private void entrarNodo(TreeNodeModel nodo) {
        pilaNodos.push(nodo);
    }

    private void salirNodo() {
        if (!pilaNodos.isEmpty()) {
            pilaNodos.pop();
        }
    }

    private void entrarAmbito() {
        pilaAmbitos.push(new HashMap<>());
    }

    private void salirAmbito() {
        if (!pilaAmbitos.isEmpty()) {
            pilaAmbitos.pop();
        }
    }

    private Map<String, Simbolo> ambitoActual() {
        return pilaAmbitos.peek();
    }

    private boolean existeEnAmbitoActual(String id) {
        return !pilaAmbitos.isEmpty() && ambitoActual().containsKey(id);
    }

    private Simbolo buscarSimbolo(String id) {
        for (int i = pilaAmbitos.size() - 1; i >= 0; i--) {
            Map<String, Simbolo> ambito = pilaAmbitos.get(i);
            if (ambito.containsKey(id)) {
                return ambito.get(id);
            }
        }
        return null;
    }

    private void declararVariable(org.antlr.v4.runtime.ParserRuleContext ctx, String id, String tipo, Double valor) {
        if (existeEnAmbitoActual(id)) {
            log(ctx, "Error semántico: variable ya declarada en este ámbito -> " + id);
            return;
        }
        ambitoActual().put(id, new Simbolo(id, tipo, valor));
    }
    private void registrarFuncion(LenguajeParser.FuncionContext ctx, String nombre, String tipoRetorno, LenguajeParser.ParametrosContext params) {
        if (tablaFunciones.containsKey(nombre)) {
            log(ctx, "Error semántico: función ya declarada -> " + nombre);
            return;
        }

        List<String> tiposParams = new ArrayList<>();
        if (params != null) {
            for (LenguajeParser.ParametroContext p : params.parametro()) {
                tiposParams.add(obtenerTipoDeclarado(p.tipo()));
            }
        }

        tablaFunciones.put(nombre, new FuncionInfo(nombre, tipoRetorno, tiposParams));
    }
    
    private String ubicacion(org.antlr.v4.runtime.ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null) {
            return "[línea ?, columna ?]";
        }
        int linea = ctx.getStart().getLine();
        int columna = ctx.getStart().getCharPositionInLine();
        return "[línea " + linea + ", columna " + columna + "]";
    }

    private void log(org.antlr.v4.runtime.ParserRuleContext ctx, String mensaje) {
        log(ubicacion(ctx) + " " + mensaje);
    }

    private void log(String mensaje) {
        errores.add(mensaje);

        if (terminal != null) {
            StyledDocument doc = terminal.getStyledDocument();
            Style errorStyle = terminal.addStyle("ErrorStyle", null);
            StyleConstants.setForeground(errorStyle, Color.RED);

            try {
                doc.insertString(doc.getLength(), mensaje + "\n", errorStyle);
            } catch (Exception e) {
                System.out.println(mensaje);
            }
        } else {
            System.out.println(mensaje);
        }
    }

    public void logNormal(String mensaje) {
        if (terminal != null) {
            StyledDocument doc = terminal.getStyledDocument();
            Style normalStyle = terminal.addStyle("NormalStyle", null);
            StyleConstants.setForeground(normalStyle, Color.BLACK);

            try {
                doc.insertString(doc.getLength(), mensaje + "\n", normalStyle);
            } catch (Exception e) {
                System.out.println(mensaje);
            }
        } else {
            System.out.println(mensaje);
        }
    }

    private String obtenerTipoDeclarado(LenguajeParser.TipoContext ctx) {
        if (ctx.ENTERO() != null) return "entero";
        if (ctx.REAL() != null) return "real";
        if (ctx.CADENA() != null) return "cadena";
        if (ctx.BOOLEANO() != null) return "booleano";
        if (ctx.VACIO() != null) return "vacio";
        return "desconocido";
    }

    public String inferirTipo(LenguajeParser.ExpresionContext ctx) {
        if (ctx == null) return "desconocido";
        return inferirTipoOr(ctx.o);
    }

    private boolean esNumerico(String tipo) {
        return "entero".equals(tipo) || "real".equals(tipo);
    }

    private boolean esBooleano(String tipo) {
        return "booleano".equals(tipo);
    }

    private boolean esCadena(String tipo) {
        return "cadena".equals(tipo);
    }

    private boolean esTipoValido(String tipo) {
        return "entero".equals(tipo)
                || "real".equals(tipo)
                || "cadena".equals(tipo)
                || "booleano".equals(tipo)
                || "vacio".equals(tipo);
    }

    private boolean sonComparablesPorIgualdad(String a, String b) {
        if (!esTipoValido(a) || !esTipoValido(b)) return false;
        if (a.equals(b)) return true;
        return esNumerico(a) && esNumerico(b);
    }

    private String inferirTipoOr(LenguajeParser.OrExprContext ctx) {
        String tipo = inferirTipoAnd(ctx.andExpr(0));

        for (int i = 1; i < ctx.andExpr().size(); i++) {
            String der = inferirTipoAnd(ctx.andExpr(i));

            if (!esBooleano(tipo) || !esBooleano(der)) {
                return "incompatible";
            }

            tipo = "booleano";
        }

        return tipo;
    }

    private String inferirTipoAnd(LenguajeParser.AndExprContext ctx) {
        String tipo = inferirTipoIgualdad(ctx.igualdad(0));

        for (int i = 1; i < ctx.igualdad().size(); i++) {
            String der = inferirTipoIgualdad(ctx.igualdad(i));

            if (!esBooleano(tipo) || !esBooleano(der)) {
                return "incompatible";
            }

            tipo = "booleano";
        }

        return tipo;
    }

    private String inferirTipoIgualdad(LenguajeParser.IgualdadContext ctx) {
        String tipo = inferirTipoComparacion(ctx.comparacion(0));

        for (int i = 1; i < ctx.comparacion().size(); i++) {
            String der = inferirTipoComparacion(ctx.comparacion(i));

            if (!sonComparablesPorIgualdad(tipo, der)) {
                return "incompatible";
            }

            tipo = "booleano";
        }

        return tipo;
    }

    private String inferirTipoComparacion(LenguajeParser.ComparacionContext ctx) {
        String tipo = inferirTipoSuma(ctx.suma(0));

        for (int i = 1; i < ctx.suma().size(); i++) {
            String der = inferirTipoSuma(ctx.suma(i));

            if (!esNumerico(tipo) || !esNumerico(der)) {
                return "incompatible";
            }

            tipo = "booleano";
        }

        return tipo;
    }

    private String inferirTipoSuma(LenguajeParser.SumaContext ctx) {
        String tipo = inferirTipoMult(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            String der = inferirTipoMult(ctx.mult(i));

            if (ctx.MAS(i - 1) != null) {
                if (esCadena(tipo) && esCadena(der)) {
                    tipo = "cadena";
                } else if (esBooleano(tipo) || esBooleano(der) || esCadena(tipo) || esCadena(der)) {
                    return "incompatible";
                } else if ("real".equals(tipo) || "real".equals(der)) {
                    tipo = "real";
                } else {
                    tipo = "entero";
                }
            } else {
                if (esCadena(tipo) || esCadena(der) || esBooleano(tipo) || esBooleano(der)) {
                    return "incompatible";
                }
                if ("real".equals(tipo) || "real".equals(der)) {
                    tipo = "real";
                } else {
                    tipo = "entero";
                }
            }
        }

        return tipo;
    }

    private String inferirTipoMult(LenguajeParser.MultContext ctx) {
        String tipo = inferirTipoUnario(ctx.unario(0));

        for (int i = 1; i < ctx.unario().size(); i++) {
            String der = inferirTipoUnario(ctx.unario(i));

            if (esCadena(tipo) || esCadena(der) || esBooleano(tipo) || esBooleano(der)) {
                return "incompatible";
            }

            if (ctx.DIV(i - 1) != null) {
                tipo = "real";
            } else if ("real".equals(tipo) || "real".equals(der)) {
                tipo = "real";
            } else {
                tipo = "entero";
            }
        }

        return tipo;
    }

    private String inferirTipoUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return inferirTipoPrimario(ctx.primario());
        }

        String tipoOperando = inferirTipoUnario(ctx.u);

        if (ctx.NO() != null) {
            return esBooleano(tipoOperando) ? "booleano" : "incompatible";
        }

        if (ctx.MENOS() != null) {
            return esNumerico(tipoOperando) ? tipoOperando : "incompatible";
        }

        return tipoOperando;
    }

    private String inferirTipoPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            return ctx.NUMERO().getText().contains(".") ? "real" : "entero";
        }

        if (ctx.VERDADERO() != null || ctx.FALSO() != null) {
            return "booleano";
        }

        if (ctx.CADENA_LIT() != null) {
            return "cadena";
        }

        if (ctx.llamadaFuncion() != null) {
            return inferirTipoLlamadaFuncion(ctx.llamadaFuncion());
        }

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            Simbolo s = buscarSimbolo(id);
            return s != null ? s.tipo : "desconocido";
        }

        if (ctx.e != null) {
            return inferirTipo(ctx.e);
        }

        if (ctx.leer() != null) {
            return "entero";
        }

        return "desconocido";
    }

    public String inferirTipoLlamadaFuncion(LenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();

        if (!tablaFunciones.containsKey(nombre)) {
            return "desconocido";
        }

        return tablaFunciones.get(nombre).tipoRetorno;
    }

    private boolean tiposCompatibles(String esperado, String recibido) {
        if (esperado == null || recibido == null) return false;
        if (esperado.equals(recibido)) return true;
        if ("real".equals(esperado) && "entero".equals(recibido)) return true;
        return false;
    }

    @Override
    public Double visitPrograma(LenguajeParser.ProgramaContext ctx) {
        raizSemantica = null;
        pilaNodos.clear();

        TreeNodeModel nodo = crearNodo("Programa");
        entrarNodo(nodo);

        for (LenguajeParser.ElementoContext e : ctx.elemento()) {
            visit(e);
        }

        salirNodo();
        return 0.0;
    }

    @Override
    public Double visitElemento(LenguajeParser.ElementoContext ctx) {
        TreeNodeModel nodo = crearNodo("Elemento");
        entrarNodo(nodo);

        Double result = visitChildren(ctx);

        salirNodo();
        return result;
    }

    @Override
    public Double visitFuncion(LenguajeParser.FuncionContext ctx) {
        String nombre = ctx.ID().getText();
        String tipoRetorno = obtenerTipoDeclarado(ctx.tipo());
        boolean duplicada = tablaFunciones.containsKey(nombre);

        TreeNodeModel nodo = crearNodoAnotado("Funcion(" + nombre + ")", duplicada ? "ERROR" : tipoRetorno);
        entrarNodo(nodo);

        if (ctx.parametros() != null) {
            TreeNodeModel paramsNode = crearNodo("Parametros");
            entrarNodo(paramsNode);
            for (LenguajeParser.ParametroContext p : ctx.parametros().parametro()) {
                String tipo = obtenerTipoDeclarado(p.tipo());
                crearNodoAnotado("ID(" + p.ID().getText() + ")", tipo);
            }
            salirNodo();
        }

        if (duplicada) {
            log(ctx,"Error semántico: función ya declarada -> " + nombre);
            salirNodo();
            return 0.0;
        }

        registrarFuncion(ctx, nombre, tipoRetorno, ctx.parametros());

        String funcionAnterior = funcionActual;
        String tipoRetornoAnterior = tipoRetornoActual;

        funcionActual = nombre;
        tipoRetornoActual = tipoRetorno;

        entrarAmbito();

        if (ctx.parametros() != null) {
            for (LenguajeParser.ParametroContext p : ctx.parametros().parametro()) {
                String nombreParam = p.ID().getText();
                String tipoParam = obtenerTipoDeclarado(p.tipo());

                if (existeEnAmbitoActual(nombreParam)) {
                    log(ctx, "Error semántico: parámetro duplicado -> " + nombreParam);
                } else {
                    declararVariable(ctx, nombreParam, tipoParam, 0.0);
                }
            }
        }

        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }

        salirAmbito();

        funcionActual = funcionAnterior;
        tipoRetornoActual = tipoRetornoAnterior;

        salirNodo();
        return 0.0;
    }

    @Override
    public Double visitLlamadaFuncion(LenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        boolean existe = tablaFunciones.containsKey(nombre);
        String anotacion = existe ? tablaFunciones.get(nombre).tipoRetorno : "ERROR";

        TreeNodeModel nodo = crearNodoAnotado("Llamada(" + nombre + ")", anotacion);
        entrarNodo(nodo);

        if (!existe) {
            log(ctx, "Error semántico: función no declarada -> " + nombre);
        }

        List<LenguajeParser.ExpresionContext> args = new ArrayList<>();
        if (ctx.argumentos() != null) {
            args = ctx.argumentos().expresion();
        }

        if (existe) {
            FuncionInfo f = tablaFunciones.get(nombre);

            if (args.size() != f.tiposParametros.size()) {
                log(ctx, "Error semántico: cantidad incorrecta de parámetros en -> " + nombre);
            }

            for (int i = 0; i < args.size(); i++) {
                String recibido = inferirTipo(args.get(i));
                String anotArg = recibido;

                if (i < f.tiposParametros.size()) {
                    String esperado = f.tiposParametros.get(i);
                    if (!tiposCompatibles(esperado, recibido)) {
                        anotArg = "ERROR";
                        log(ctx, "Error semántico: parámetro incompatible en función -> "
                                + nombre + " (esperado " + esperado + ", recibido " + recibido + ")");
                    }
                } else {
                    anotArg = "ERROR";
                }

                TreeNodeModel argNode = crearNodoAnotado("Argumento", anotArg);
                entrarNodo(argNode);
                visit(args.get(i));
                salirNodo();
            }
        } else {
            for (LenguajeParser.ExpresionContext e : args) {
                TreeNodeModel argNode = crearNodoAnotado("Argumento", inferirTipo(e));
                entrarNodo(argNode);
                visit(e);
                salirNodo();
            }
        }

        salirNodo();
        return 0.0;
    }

    @Override
    public Double visitRetornar(LenguajeParser.RetornarContext ctx) {
        String anotacion;

        if (tipoRetornoActual == null) {
            anotacion = "ERROR";
        } else if ("vacio".equals(tipoRetornoActual) && ctx.e != null) {
            anotacion = "ERROR";
        } else if (!"vacio".equals(tipoRetornoActual) && ctx.e == null) {
            anotacion = "ERROR";
        } else if (ctx.e != null) {
            String tipoRetornado = inferirTipo(ctx.e);
            anotacion = tiposCompatibles(tipoRetornoActual, tipoRetornado) ? tipoRetornado : "ERROR";
        } else {
            anotacion = tipoRetornoActual;
        }

        TreeNodeModel nodo = crearNodoAnotado("Retornar", anotacion);
        entrarNodo(nodo);

        if (tipoRetornoActual == null) {
            log(ctx, "Error semántico: retornar fuera de una función");
            salirNodo();
            return 0.0;
        }

        if ("vacio".equals(tipoRetornoActual)) {
            if (ctx.e != null) {
                log(ctx, "Error semántico: función de tipo vacio no debe retornar valor -> " + funcionActual);
                visit(ctx.e);
            }
            salirNodo();
            return 0.0;
        }

        if (ctx.e == null) {
            log(ctx, "Error semántico: la función debe retornar un valor -> " + funcionActual);
            salirNodo();
            return 0.0;
        }

        String tipoRetornado = inferirTipo(ctx.e);
        if (!tiposCompatibles(tipoRetornoActual, tipoRetornado)) {
            log(ctx, "Error semántico: retorno incompatible en función -> "
                    + funcionActual + " (" + tipoRetornoActual + " = " + tipoRetornado + ")");
        }

        Double valor = visit(ctx.e);

        salirNodo();
        return valor;
    }

    @Override
    public Double visitSentencia(LenguajeParser.SentenciaContext ctx) {
        TreeNodeModel nodo = crearNodo("Sentencia");
        entrarNodo(nodo);

        Double result = visitChildren(ctx);

        salirNodo();
        return result;
    }

    @Override
    public Double visitBloque(LenguajeParser.BloqueContext ctx) {
        TreeNodeModel nodo = crearNodo("Bloque");
        entrarNodo(nodo);

        entrarAmbito();
        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }
        salirAmbito();

        salirNodo();
        return 0.0;
    }

    @Override
    public Double visitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx) {
        TreeNodeModel nodo = crearNodo("SentenciaBloque");
        entrarNodo(nodo);

        Double result = visitChildren(ctx);

        salirNodo();
        return result;
    }

    @Override
    public Double visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String id = ctx.ID().getText();
        String tipoDeclarado = obtenerTipoDeclarado(ctx.tipo());

        boolean redeclarada = existeEnAmbitoActual(id);
        String anotacion = tipoDeclarado;

        if (redeclarada) {
            anotacion = "ERROR";
        } else if (ctx.e != null) {
            String tipoExpresion = inferirTipo(ctx.e);
            if (!tiposCompatibles(tipoDeclarado, tipoExpresion)) {
                anotacion = "ERROR";
            }
        }

        TreeNodeModel nodo = crearNodoAnotado("Declaracion", anotacion);
        entrarNodo(nodo);

        crearNodoAnotado("ID(" + id + ")", tipoDeclarado);

        if (redeclarada) {
            log(ctx, "Error semántico: variable ya declarada -> " + id);
            salirNodo();
            return 0.0;
        }

        Double valor = 0.0;

        if (ctx.e != null) {
            String tipoExpresion = inferirTipo(ctx.e);

            if (!tiposCompatibles(tipoDeclarado, tipoExpresion)) {
                log(ctx, "Error semántico: Asignación de tipos incompatibles -> "
                        + id + " (" + tipoDeclarado + " = " + tipoExpresion + ")");
                declararVariable(ctx, id, tipoDeclarado, 0.0);
                visit(ctx.e);
                salirNodo();
                return 0.0;
            }

            valor = visit(ctx.e);
        }

        declararVariable(ctx, id, tipoDeclarado, valor);

        salirNodo();
        return valor;
    }

    @Override
    public Double visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();
        Simbolo s = buscarSimbolo(id);

        String anotacion;
        if (s == null) {
            anotacion = "ERROR";
        } else {
            String tipoVariable = s.tipo;
            String tipoExpresion = inferirTipo(ctx.expresion());
            anotacion = tiposCompatibles(tipoVariable, tipoExpresion) ? tipoVariable : "ERROR";
        }

        TreeNodeModel nodo = crearNodoAnotado("Asignacion", anotacion);
        entrarNodo(nodo);

        if (s == null) {
            crearNodoAnotado("ID(" + id + ")", "no declarado");
            log(ctx, "Error semántico: variable no declarada -> " + id);
            if (ctx.expresion() != null) {
                visit(ctx.expresion());
            }
            salirNodo();
            return 0.0;
        }

        String tipoVariable = s.tipo;
        String tipoExpresion = inferirTipo(ctx.expresion());

        crearNodoAnotado("ID(" + id + ")", tipoVariable);

        if (!tiposCompatibles(tipoVariable, tipoExpresion)) {
            log(ctx, "Error semántico: Asignación de tipos incompatibles -> "
                    + id + " (" + tipoVariable + " = " + tipoExpresion + ")");
            visit(ctx.expresion());
            salirNodo();
            return 0.0;
        }

        double valor = visit(ctx.expresion());
        s.valor = valor;

        salirNodo();
        return valor;
    }

    @Override
    public Double visitImprimir(LenguajeParser.ImprimirContext ctx) {
        String tipo = inferirTipo(ctx.e);
        TreeNodeModel nodo = crearNodoAnotado("Imprimir", tipo);
        entrarNodo(nodo);

        Double valor = visit(ctx.e);

        salirNodo();
        return valor;
    }

    @Override
    public Double visitSi(LenguajeParser.SiContext ctx) {
        String tipoCond = inferirTipo(ctx.c);
        String anotacion = "booleano".equals(tipoCond) ? "booleano" : "ERROR";

        TreeNodeModel nodo = crearNodoAnotado("Si", anotacion);
        entrarNodo(nodo);

        TreeNodeModel condNode = crearNodoAnotado("Condicion", anotacion);
        entrarNodo(condNode);
        double condicion = visit(ctx.c);
        if (!"booleano".equals(tipoCond)) {
            log(ctx, "Error semántico: la condición de 'si' debe ser booleana");
        }
        salirNodo();

        TreeNodeModel thenNode = crearNodo("Then");
        entrarNodo(thenNode);
        visit(ctx.b1);
        salirNodo();

        if (ctx.b2 != null) {
            TreeNodeModel elseNode = crearNodo("Else");
            entrarNodo(elseNode);
            visit(ctx.b2);
            salirNodo();
        }

        salirNodo();
        return condicion;
    }

    @Override
    public Double visitMientras(LenguajeParser.MientrasContext ctx) {
        String tipoCond = inferirTipo(ctx.c);
        String anotacion = "booleano".equals(tipoCond) ? "booleano" : "ERROR";

        TreeNodeModel nodo = crearNodoAnotado("Mientras", anotacion);
        entrarNodo(nodo);

        TreeNodeModel condNode = crearNodoAnotado("Condicion", anotacion);
        entrarNodo(condNode);
        double condicion = visit(ctx.c);
        if (!"booleano".equals(tipoCond)) {
            log(ctx, "Error semántico: la condición de 'mientras' debe ser booleana");
        }
        salirNodo();

        visit(ctx.b);

        salirNodo();
        return condicion;
    }

    @Override
    public Double visitExpresion(LenguajeParser.ExpresionContext ctx) {
        String tipo = inferirTipo(ctx);
        TreeNodeModel nodo = crearNodoAnotado("Expresion", "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        Double result = visit(ctx.o);

        salirNodo();
        return result;
    }

    @Override
    public Double visitOrExpr(LenguajeParser.OrExprContext ctx) {
        String tipo = inferirTipoOr(ctx);
        TreeNodeModel nodo = crearNodoAnotado("OrExpr", "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        double result = visit(ctx.andExpr(0));

        for (int i = 1; i < ctx.andExpr().size(); i++) {
            double right = visit(ctx.andExpr(i));

            String izqTipo = inferirTipoAnd(ctx.andExpr(i - 1));
            String derTipo = inferirTipoAnd(ctx.andExpr(i));
            if (!esBooleano(izqTipo) || !esBooleano(derTipo)) {
                log(ctx, "Error semántico: el operador || requiere operandos booleanos");
            }

            result = (result != 0 || right != 0) ? 1.0 : 0.0;
        }

        salirNodo();
        return result;
    }

    @Override
    public Double visitAndExpr(LenguajeParser.AndExprContext ctx) {
        String tipo = inferirTipoAnd(ctx);
        TreeNodeModel nodo = crearNodoAnotado("AndExpr", "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        double result = visit(ctx.igualdad(0));

        for (int i = 1; i < ctx.igualdad().size(); i++) {
            double right = visit(ctx.igualdad(i));

            String izqTipo = inferirTipoIgualdad(ctx.igualdad(i - 1));
            String derTipo = inferirTipoIgualdad(ctx.igualdad(i));
            if (!esBooleano(izqTipo) || !esBooleano(derTipo)) {
                log(ctx, "Error semántico: el operador && requiere operandos booleanos");
            }

            result = (result != 0 && right != 0) ? 1.0 : 0.0;
        }

        salirNodo();
        return result;
    }

    @Override
    public Double visitIgualdad(LenguajeParser.IgualdadContext ctx) {
        String tipo = inferirTipoIgualdad(ctx);
        TreeNodeModel nodo = crearNodoAnotado("Igualdad", "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        double result = visit(ctx.comparacion(0));

        for (int i = 1; i < ctx.comparacion().size(); i++) {
            double right = visit(ctx.comparacion(i));

            String izqTipo = inferirTipoComparacion(ctx.comparacion(i - 1));
            String derTipo = inferirTipoComparacion(ctx.comparacion(i));

            if (!sonComparablesPorIgualdad(izqTipo, derTipo)) {
                if (ctx.IGUAL_IGUAL(i - 1) != null) {
                    log(ctx, "Error semántico: comparación == entre tipos incompatibles");
                } else {
                    log(ctx, "Error semántico: comparación != entre tipos incompatibles");
                }
            }

            if (ctx.IGUAL_IGUAL(i - 1) != null) {
                result = (result == right) ? 1.0 : 0.0;
            } else {
                result = (result != right) ? 1.0 : 0.0;
            }
        }

        salirNodo();
        return result;
    }

    @Override
    public Double visitComparacion(LenguajeParser.ComparacionContext ctx) {
        String tipo = inferirTipoComparacion(ctx);
        TreeNodeModel nodo = crearNodoAnotado("Comparacion", "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        double result = visit(ctx.suma(0));

        for (int i = 1; i < ctx.suma().size(); i++) {
            double right = visit(ctx.suma(i));

            String izqTipo = inferirTipoSuma(ctx.suma(i - 1));
            String derTipo = inferirTipoSuma(ctx.suma(i));

            if (!esNumerico(izqTipo) || !esNumerico(derTipo)) {
                if (ctx.MENOR(i - 1) != null) {
                    log(ctx, "Error semántico: el operador < requiere operandos numéricos");
                } else if (ctx.MENOR_IGUAL(i - 1) != null) {
                    log(ctx, "Error semántico: el operador <= requiere operandos numéricos");
                } else if (ctx.MAYOR(i - 1) != null) {
                    log(ctx, "Error semántico: el operador > requiere operandos numéricos");
                } else if (ctx.MAYOR_IGUAL(i - 1) != null) {
                    log(ctx, "Error semántico: el operador >= requiere operandos numéricos");
                }
            }

            if (ctx.MENOR(i - 1) != null) {
                result = (result < right) ? 1.0 : 0.0;
            } else if (ctx.MENOR_IGUAL(i - 1) != null) {
                result = (result <= right) ? 1.0 : 0.0;
            } else if (ctx.MAYOR(i - 1) != null) {
                result = (result > right) ? 1.0 : 0.0;
            } else if (ctx.MAYOR_IGUAL(i - 1) != null) {
                result = (result >= right) ? 1.0 : 0.0;
            }
        }

        salirNodo();
        return result;
    }

    @Override
    public Double visitSuma(LenguajeParser.SumaContext ctx) {
        String tipo = inferirTipoSuma(ctx);
        TreeNodeModel nodo = crearNodoAnotado("Suma", "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        double result = visit(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            if (ctx.MAS(i - 1) != null) {
                result += visit(ctx.mult(i));
            } else if (ctx.MENOS(i - 1) != null) {
                result -= visit(ctx.mult(i));
            }
        }

        salirNodo();
        return result;
    }
    
    private boolean esCeroLiteral(LenguajeParser.UnarioContext ctx) {
        if (ctx == null) return false;

        String texto = ctx.getText();
        if (texto == null) return false;

        texto = texto.trim();

        return texto.equals("0") || texto.equals("0.0") || texto.equals("0.00");
    }
    
    @Override
    public Double visitMult(LenguajeParser.MultContext ctx) {
        String tipo = inferirTipoMult(ctx);
        boolean errorDivisionCero = false;

        if (ctx.unario().size() > 1) {
            for (int i = 1; i < ctx.unario().size(); i++) {
                if (ctx.DIV(i - 1) != null && esCeroLiteral(ctx.unario(i))) {
                    errorDivisionCero = true;
                    break;
                }
            }
        }

        String anotacion = ("incompatible".equals(tipo) || errorDivisionCero) ? "ERROR" : tipo;
        TreeNodeModel nodo = crearNodoAnotado("Mult", anotacion);
        entrarNodo(nodo);

        double result = visit(ctx.unario(0));

        for (int i = 1; i < ctx.unario().size(); i++) {
            double right = visit(ctx.unario(i));

            if (ctx.POR(i - 1) != null) {
                result *= right;
            } else if (ctx.DIV(i - 1) != null) {
                if (esCeroLiteral(ctx.unario(i))) {
                    log(ctx.unario(i), "Error semántico: división entre cero");
                    salirNodo();
                    return 0.0;
                }
                result /= right;
            }
        }

        salirNodo();
        return result;
    }

    private double visitPreviewUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return previewPrimario(ctx.primario());
        }

        double valor = visitPreviewUnario(ctx.u);

        if (ctx.MENOS() != null) {
            return -valor;
        }

        if (ctx.NO() != null) {
            return (valor == 0) ? 1.0 : 0.0;
        }

        return valor;
    }

    private double previewPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            return Double.parseDouble(ctx.NUMERO().getText());
        }
        if (ctx.VERDADERO() != null) {
            return 1.0;
        }
        if (ctx.FALSO() != null) {
            return 0.0;
        }
        if (ctx.CADENA_LIT() != null) {
            return 0.0;
        }
        if (ctx.ID() != null) {
            Simbolo s = buscarSimbolo(ctx.ID().getText());
            return (s != null && s.valor != null) ? s.valor : 0.0;
        }
        if (ctx.e != null) {
            return 0.0;
        }
        if (ctx.leer() != null) {
            return 0.0;
        }
        return 0.0;
    }

    @Override
    public Double visitUnario(LenguajeParser.UnarioContext ctx) {
        String tipo = inferirTipoUnario(ctx);
        String nombre = "Unario";

        if (ctx.NO() != null) {
            nombre = "Unario(!)";
        } else if (ctx.MENOS() != null) {
            nombre = "Unario(-)";
        }

        TreeNodeModel nodo = crearNodoAnotado(nombre, "incompatible".equals(tipo) ? "ERROR" : tipo);
        entrarNodo(nodo);

        double valor;
        if (ctx.primario() != null) {
            valor = visit(ctx.primario());
        } else {
            String tipoOperando = inferirTipoUnario(ctx.u);
            valor = visit(ctx.u);

            if (ctx.MENOS() != null) {
                if (!esNumerico(tipoOperando)) {
                    log(ctx, "Error semántico: el operador - unario requiere un operando numérico");
                }
                valor = -valor;
            } else if (ctx.NO() != null) {
                if (!esBooleano(tipoOperando)) {
                    log(ctx, "Error semántico: el operador ! requiere un operando booleano");
                }
                valor = (valor == 0) ? 1.0 : 0.0;
            }
        }

        salirNodo();
        return valor;
    }

    @Override
    public Double visitPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            String tipo = ctx.NUMERO().getText().contains(".") ? "real" : "entero";
            crearNodoAnotado("Constante(" + ctx.NUMERO().getText() + ")", tipo);
            return Double.parseDouble(ctx.NUMERO().getText());
        }

        if (ctx.VERDADERO() != null) {
            crearNodoAnotado("Constante(verdadero)", "booleano");
            return 1.0;
        }

        if (ctx.FALSO() != null) {
            crearNodoAnotado("Constante(falso)", "booleano");
            return 0.0;
        }

        if (ctx.CADENA_LIT() != null) {
            crearNodoAnotado("Constante(" + ctx.CADENA_LIT().getText() + ")", "cadena");
            return 0.0;
        }

        if (ctx.llamadaFuncion() != null) {
            TreeNodeModel nodo = crearNodoAnotado("Primario", inferirTipoLlamadaFuncion(ctx.llamadaFuncion()));
            entrarNodo(nodo);
            Double result = visit(ctx.llamadaFuncion());
            salirNodo();
            return result;
        }

        if (ctx.leer() != null) {
            crearNodoAnotado("Leer()", "entero");
            return 0.0;
        }

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            Simbolo s = buscarSimbolo(id);

            if (s == null) {
                crearNodoAnotado("ID(" + id + ")", "no declarado");
                log(ctx, "Error semántico: variable no definida -> " + id);
                return 0.0;
            }

            crearNodoAnotado("ID(" + id + ")", s.tipo);
            return s.valor != null ? s.valor : 0.0;
        }

        if (ctx.e != null) {
            TreeNodeModel nodo = crearNodoAnotado("Primario", inferirTipo(ctx.e));
            entrarNodo(nodo);
            Double result = visit(ctx.e);
            salirNodo();
            return result;
        }

        crearNodoAnotado("Primario", "desconocido");
        return 0.0;
    }
}