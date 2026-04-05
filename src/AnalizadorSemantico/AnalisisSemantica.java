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

public class AnalisisSemantica extends LenguajeBaseVisitor<Double> {

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

    public Map<String, Double> getTabla() {
        Map<String, Double> tablaPlana = new HashMap<>();
        if (!pilaAmbitos.isEmpty()) {
            for (Map<String, Simbolo> ambito : pilaAmbitos) {
                for (Simbolo s : ambito.values()) {
                    tablaPlana.put(s.nombre, s.valor);
                }
            }
        }
        return tablaPlana;
    }

    public Map<String, String> getTipos() {
        Map<String, String> tiposPlanos = new HashMap<>();
        if (!pilaAmbitos.isEmpty()) {
            for (Map<String, Simbolo> ambito : pilaAmbitos) {
                for (Simbolo s : ambito.values()) {
                    tiposPlanos.put(s.nombre, s.tipo);
                }
            }
        }
        return tiposPlanos;
    }

    public List<String> getErrores() {
        return errores;
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

    private void declararVariable(String id, String tipo, Double valor) {
        if (existeEnAmbitoActual(id)) {
            log("Error semántico: variable ya declarada en este ámbito -> " + id);
            return;
        }
        ambitoActual().put(id, new Simbolo(id, tipo, valor));
    }

    private void registrarFuncion(String nombre, String tipoRetorno, LenguajeParser.ParametrosContext params) {
        if (tablaFunciones.containsKey(nombre)) {
            log("Error semántico: función ya declarada -> " + nombre);
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
        return inferirTipoOr(ctx.o);
    }

    private String inferirTipoOr(LenguajeParser.OrExprContext ctx) {
        if (ctx.andExpr().size() > 1) return "booleano";
        return inferirTipoAnd(ctx.andExpr(0));
    }

    private String inferirTipoAnd(LenguajeParser.AndExprContext ctx) {
        if (ctx.igualdad().size() > 1) return "booleano";
        return inferirTipoIgualdad(ctx.igualdad(0));
    }

    private String inferirTipoIgualdad(LenguajeParser.IgualdadContext ctx) {
        if (ctx.comparacion().size() > 1) return "booleano";
        return inferirTipoComparacion(ctx.comparacion(0));
    }

    private String inferirTipoComparacion(LenguajeParser.ComparacionContext ctx) {
        if (ctx.suma().size() > 1) return "booleano";
        return inferirTipoSuma(ctx.suma(0));
    }

    private String inferirTipoSuma(LenguajeParser.SumaContext ctx) {
        String tipo = inferirTipoMult(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            String der = inferirTipoMult(ctx.mult(i));

            if (ctx.MAS(i - 1) != null) {
                if ("cadena".equals(tipo) && "cadena".equals(der)) {
                    tipo = "cadena";
                } else if ("booleano".equals(tipo) || "booleano".equals(der)
                        || "cadena".equals(tipo) || "cadena".equals(der)) {
                    return "incompatible";
                } else if ("real".equals(tipo) || "real".equals(der)) {
                    tipo = "real";
                } else {
                    tipo = "entero";
                }
            } else {
                if ("cadena".equals(tipo) || "cadena".equals(der)
                        || "booleano".equals(tipo) || "booleano".equals(der)) {
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

            if ("cadena".equals(tipo) || "cadena".equals(der)
                    || "booleano".equals(tipo) || "booleano".equals(der)) {
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

        if (ctx.NO() != null) {
            return "booleano";
        }

        return inferirTipoUnario(ctx.u);
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
        if (esperado.equals(recibido)) return true;
        if ("real".equals(esperado) && "entero".equals(recibido)) return true;
        return false;
    }

    @Override
    public Double visitPrograma(LenguajeParser.ProgramaContext ctx) {
        for (LenguajeParser.ElementoContext e : ctx.elemento()) {
            visit(e);
        }
        return 0.0;
    }

    @Override
    public Double visitElemento(LenguajeParser.ElementoContext ctx) {
        if (ctx.sentencia() != null) {
            return visit(ctx.sentencia());
        }
        if (ctx.funcion() != null) {
            return visit(ctx.funcion());
        }
        return 0.0;
    }

    @Override
    public Double visitFuncion(LenguajeParser.FuncionContext ctx) {
        String nombre = ctx.ID().getText();
        String tipoRetorno = obtenerTipoDeclarado(ctx.tipo());

        registrarFuncion(nombre, tipoRetorno, ctx.parametros());

        String funcionAnterior = funcionActual;
        String retornoAnterior = tipoRetornoActual;

        funcionActual = nombre;
        tipoRetornoActual = tipoRetorno;

        entrarAmbito();

        if (ctx.parametros() != null) {
            for (LenguajeParser.ParametroContext p : ctx.parametros().parametro()) {
                String tipo = obtenerTipoDeclarado(p.tipo());
                String id = p.ID().getText();
                declararVariable(id, tipo, 0.0);
            }
        }

        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }

        salirAmbito();

        funcionActual = funcionAnterior;
        tipoRetornoActual = retornoAnterior;

        return 0.0;
    }

    @Override
    public Double visitLlamadaFuncion(LenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();

        if (!tablaFunciones.containsKey(nombre)) {
            log("Error semántico: función no declarada -> " + nombre);
            return 0.0;
        }

        FuncionInfo f = tablaFunciones.get(nombre);

        List<LenguajeParser.ExpresionContext> args = new ArrayList<>();
        if (ctx.argumentos() != null) {
            args = ctx.argumentos().expresion();
        }

        if (args.size() != f.tiposParametros.size()) {
            log("Error semántico: cantidad incorrecta de parámetros en -> " + nombre);
            return 0.0;
        }

        for (int i = 0; i < args.size(); i++) {
            String recibido = inferirTipo(args.get(i));
            String esperado = f.tiposParametros.get(i);

            if (!tiposCompatibles(esperado, recibido)) {
                log("Error semántico: parámetro incompatible en función -> "
                        + nombre + " (esperado " + esperado + ", recibido " + recibido + ")");
            }

            visit(args.get(i));
        }

        return 0.0;
    }

    @Override
    public Double visitRetornar(LenguajeParser.RetornarContext ctx) {
        if (tipoRetornoActual == null) {
            log("Error semántico: retornar fuera de una función");
            return 0.0;
        }

        if ("vacio".equals(tipoRetornoActual)) {
            if (ctx.e != null) {
                log("Error semántico: función de tipo vacio no debe retornar valor -> " + funcionActual);
            }
            return 0.0;
        }

        if (ctx.e == null) {
            log("Error semántico: la función debe retornar un valor -> " + funcionActual);
            return 0.0;
        }

        String tipoRetornado = inferirTipo(ctx.e);
        if (!tiposCompatibles(tipoRetornoActual, tipoRetornado)) {
            log("Error semántico: retorno incompatible en función -> "
                    + funcionActual + " (" + tipoRetornoActual + " = " + tipoRetornado + ")");
        }

        return visit(ctx.e);
    }

    @Override
    public Double visitSentencia(LenguajeParser.SentenciaContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Double visitBloque(LenguajeParser.BloqueContext ctx) {
        entrarAmbito();
        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }
        salirAmbito();
        return 0.0;
    }

    @Override
    public Double visitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Double visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String id = ctx.ID().getText();
        String tipoDeclarado = obtenerTipoDeclarado(ctx.tipo());

        if (existeEnAmbitoActual(id)) {
            log("Error semántico: variable ya declarada -> " + id);
            return 0.0;
        }

        Double valor = 0.0;

        if (ctx.e != null) {
            String tipoExpresion = inferirTipo(ctx.e);

            if (!tiposCompatibles(tipoDeclarado, tipoExpresion)) {
                log("Error semántico: Asignación de tipos incompatibles -> "
                        + id + " (" + tipoDeclarado + " = " + tipoExpresion + ")");
                declararVariable(id, tipoDeclarado, 0.0);
                return 0.0;
            }

            valor = visit(ctx.e);
        }

        declararVariable(id, tipoDeclarado, valor);
        return valor;
    }

    @Override
    public Double visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();
        Simbolo s = buscarSimbolo(id);

        if (s == null) {
            log("Error semántico: variable no declarada -> " + id);
            return 0.0;
        }

        String tipoVariable = s.tipo;
        String tipoExpresion = inferirTipo(ctx.expresion());

        if (!tiposCompatibles(tipoVariable, tipoExpresion)) {
            log("Error semántico: Asignación de tipos incompatibles -> "
                    + id + " (" + tipoVariable + " = " + tipoExpresion + ")");
            return 0.0;
        }

        double valor = visit(ctx.expresion());
        s.valor = valor;
        return valor;
    }

    @Override
    public Double visitImprimir(LenguajeParser.ImprimirContext ctx) {
        return visit(ctx.e);
    }

    @Override
    public Double visitSi(LenguajeParser.SiContext ctx) {
        String tipoCond = inferirTipo(ctx.c);
        if (!"booleano".equals(tipoCond)) {
            log("Error semántico: la condición de 'si' debe ser booleana");
        }

        double condicion = visit(ctx.c);
        visit(ctx.b1);

        if (ctx.b2 != null) {
            visit(ctx.b2);
        }

        return condicion;
    }

    @Override
    public Double visitMientras(LenguajeParser.MientrasContext ctx) {
        String tipoCond = inferirTipo(ctx.c);
        if (!"booleano".equals(tipoCond)) {
            log("Error semántico: la condición de 'mientras' debe ser booleana");
        }

        double condicion = visit(ctx.c);
        visit(ctx.b);
        return condicion;
    }

    @Override
    public Double visitExpresion(LenguajeParser.ExpresionContext ctx) {
        return visit(ctx.o);
    }

    @Override
    public Double visitOrExpr(LenguajeParser.OrExprContext ctx) {
        double result = visit(ctx.andExpr(0));

        for (int i = 1; i < ctx.andExpr().size(); i++) {
            double right = visit(ctx.andExpr(i));
            result = (result != 0 || right != 0) ? 1.0 : 0.0;
        }

        return result;
    }

    @Override
    public Double visitAndExpr(LenguajeParser.AndExprContext ctx) {
        double result = visit(ctx.igualdad(0));

        for (int i = 1; i < ctx.igualdad().size(); i++) {
            double right = visit(ctx.igualdad(i));
            result = (result != 0 && right != 0) ? 1.0 : 0.0;
        }

        return result;
    }

    @Override
    public Double visitIgualdad(LenguajeParser.IgualdadContext ctx) {
        double result = visit(ctx.comparacion(0));

        for (int i = 1; i < ctx.comparacion().size(); i++) {
            double right = visit(ctx.comparacion(i));

            if (ctx.IGUAL_IGUAL(i - 1) != null) {
                result = (result == right) ? 1.0 : 0.0;
            } else {
                result = (result != right) ? 1.0 : 0.0;
            }
        }

        return result;
    }

    @Override
    public Double visitComparacion(LenguajeParser.ComparacionContext ctx) {
        double result = visit(ctx.suma(0));

        for (int i = 1; i < ctx.suma().size(); i++) {
            double right = visit(ctx.suma(i));

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

        return result;
    }

    @Override
    public Double visitSuma(LenguajeParser.SumaContext ctx) {
        double result = visit(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            if (ctx.MAS(i - 1) != null) {
                result += visit(ctx.mult(i));
            } else if (ctx.MENOS(i - 1) != null) {
                result -= visit(ctx.mult(i));
            }
        }

        return result;
    }

    @Override
    public Double visitMult(LenguajeParser.MultContext ctx) {
        double result = visit(ctx.unario(0));

        for (int i = 1; i < ctx.unario().size(); i++) {
            double right = visit(ctx.unario(i));

            if (ctx.POR(i - 1) != null) {
                result *= right;
            } else if (ctx.DIV(i - 1) != null) {
                if (right == 0) {
                    log("Error semántico: división entre cero");
                    return 0.0;
                }
                result /= right;
            }
        }

        return result;
    }

    @Override
    public Double visitUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return visit(ctx.primario());
        }

        double valor = visit(ctx.u);

        if (ctx.MENOS() != null) {
            return -valor;
        }

        if (ctx.NO() != null) {
            return (valor == 0) ? 1.0 : 0.0;
        }

        return valor;
    }

    @Override
    public Double visitPrimario(LenguajeParser.PrimarioContext ctx) {
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

        if (ctx.llamadaFuncion() != null) {
            return visit(ctx.llamadaFuncion());
        }

        if (ctx.leer() != null) {
            return 0.0;
        }

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            Simbolo s = buscarSimbolo(id);

            if (s == null) {
                log("Error semántico: variable no definida -> " + id);
                return 0.0;
            }

            return s.valor != null ? s.valor : 0.0;
        }

        if (ctx.e != null) {
            return visit(ctx.e);
        }

        return 0.0;
    }
}