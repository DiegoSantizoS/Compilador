package AnalizadorSemantico;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;

public class ArbolSemanticoPanel extends JPanel {

    private final JLabel titulo;

    public ArbolSemanticoPanel() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Árbol Semántico", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);
    }

    public void showTreeGui(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        ParseTree parseTree = parser.programa();

        AnalisisSemantica analizador = new AnalisisSemantica(null);
        analizador.visit(parseTree);

        SemanticNode semanticRoot = buildSemanticTree(parseTree, parser, analizador);

        removeAll();
        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);

        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), semanticRoot);
        JScrollPane scrollPane = new JScrollPane(viewer);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private SemanticNode buildSemanticTree(
            ParseTree node,
            LenguajeParser parser,
            AnalisisSemantica analizador
    ) {
        String label = getSemanticLabel(node, parser, analizador);
        SemanticNode semanticNode = new SemanticNode(label);

        for (int i = 0; i < node.getChildCount(); i++) {
            semanticNode.addChild(buildSemanticTree(node.getChild(i), parser, analizador));
        }

        return semanticNode;
    }

    private String getSemanticLabel(
            ParseTree node,
            LenguajeParser parser,
            AnalisisSemantica analizador
    ) {
        if (node instanceof TerminalNode terminal) {
            return terminal.getText();
        }

        if (!(node instanceof ParserRuleContext ruleNode)) {
            return node.getText();
        }

        String base = parser.getRuleNames()[ruleNode.getRuleIndex()];

        try {
            if (node instanceof LenguajeParser.ProgramaContext) {
                return "programa";
            }

            if (node instanceof LenguajeParser.ElementoContext) {
                return "elemento";
            }

            if (node instanceof LenguajeParser.FuncionContext ctx) {
                return "funcion -> " + ctx.ID().getText() + " : " + ctx.tipo().getText();
            }

            if (node instanceof LenguajeParser.ParametrosContext) {
                return "parametros";
            }

            if (node instanceof LenguajeParser.ParametroContext ctx) {
                return "parametro -> " + ctx.ID().getText() + " : " + ctx.tipo().getText();
            }

            if (node instanceof LenguajeParser.LlamadaFuncionContext ctx) {
                String tipo = analizador.inferirTipoLlamadaFuncion(ctx);
                return "llamadaFuncion -> " + ctx.ID().getText() + "() [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.ArgumentosContext) {
                return "argumentos";
            }

            if (node instanceof LenguajeParser.RetornarContext ctx) {
                if (ctx.e != null) {
                    String tipo = analizador.inferirTipo(ctx.e);
                    String valorVisual = obtenerValorVisual(ctx.e, analizador);
                    return "retornar -> " + valorVisual + " [" + tipo + "]";
                }
                return "retornar";
            }

            if (node instanceof LenguajeParser.SentenciaContext) {
                return "sentencia";
            }

            if (node instanceof LenguajeParser.SentenciaBloqueContext) {
                return "sentenciaBloque";
            }

            if (node instanceof LenguajeParser.BloqueContext) {
                return "bloque";
            }

            if (node instanceof LenguajeParser.DeclaracionContext ctx) {
                String id = ctx.ID().getText();
                String tipo = ctx.tipo().getText();

                if (ctx.e != null) {
                    String tipoExp = analizador.inferirTipo(ctx.e);
                    String valorVisual = obtenerValorVisual(ctx.e, analizador);
                    return "declaracion -> " + id + " : " + tipo + " = " + valorVisual + " [" + tipoExp + "]";
                }

                return "declaracion -> " + id + " : " + tipo;
            }

            if (node instanceof LenguajeParser.AsignacionContext ctx) {
                String id = ctx.ID().getText();
                String tipo = analizador.inferirTipo(ctx.expresion());
                String valorVisual = obtenerValorVisual(ctx.expresion(), analizador);
                return "asignacion -> " + id + " = " + valorVisual + " [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.ImprimirContext ctx) {
                String tipo = analizador.inferirTipo(ctx.e);
                String valorVisual = obtenerValorVisual(ctx.e, analizador);
                return "imprimir -> valor = " + valorVisual + " [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.SiContext ctx) {
                String valorVisual = obtenerValorVisual(ctx.c, analizador);
                return "si -> condicion = " + valorVisual;
            }

            if (node instanceof LenguajeParser.MientrasContext ctx) {
                String valorVisual = obtenerValorVisual(ctx.c, analizador);
                return "mientras -> condicion = " + valorVisual;
            }

            if (node instanceof LenguajeParser.ExpresionContext ctx) {
                String tipo = analizador.inferirTipo(ctx);
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "expresion -> valor = " + valorVisual + " [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.OrExprContext ctx) {
                String tipo = inferirTipoNodo(ctx, analizador);
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "orExpr -> valor = " + valorVisual + ("desconocido".equals(tipo) ? "" : " [" + inferirTipoNodo(ctx, analizador) + "]");
            }

            if (node instanceof LenguajeParser.AndExprContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "andExpr -> valor = " + valorVisual + " [" + inferirTipoNodo(ctx, analizador) + "]";
            }

            if (node instanceof LenguajeParser.IgualdadContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "igualdad -> valor = " + valorVisual + " [" + inferirTipoNodo(ctx, analizador) + "]";
            }

            if (node instanceof LenguajeParser.ComparacionContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "comparacion -> valor = " + valorVisual + " [" + inferirTipoNodo(ctx, analizador) + "]";
            }

            if (node instanceof LenguajeParser.SumaContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "suma -> valor = " + valorVisual + " [" + inferirTipoNodo(ctx, analizador) + "]";
            }

            if (node instanceof LenguajeParser.MultContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "mult -> valor = " + valorVisual + " [" + inferirTipoNodo(ctx, analizador) + "]";
            }

            if (node instanceof LenguajeParser.UnarioContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "unario -> valor = " + valorVisual + " [" + inferirTipoNodo(ctx, analizador) + "]";
            }

            if (node instanceof LenguajeParser.PrimarioContext ctx) {
                if (ctx.NUMERO() != null) {
                    String numero = ctx.NUMERO().getText();
                    String tipo = numero.contains(".") ? "real" : "entero";
                    return "primario -> numero = " + numero + " [" + tipo + "]";
                }

                if (ctx.VERDADERO() != null) {
                    return "primario -> booleano = verdadero";
                }

                if (ctx.FALSO() != null) {
                    return "primario -> booleano = falso";
                }

                if (ctx.CADENA_LIT() != null) {
                    return "primario -> cadena = " + ctx.CADENA_LIT().getText();
                }

                if (ctx.ID() != null) {
                    String id = ctx.ID().getText();
                    Map<String, Double> tabla = analizador.getTabla();
                    Map<String, String> tipos = analizador.getTipos();

                    if (tabla.containsKey(id)) {
                        String tipo = tipos.getOrDefault(id, "desconocido");

                        if ("booleano".equals(tipo)) {
                            return "primario -> id = " + id + ", valor = "
                                    + (tabla.get(id) != 0 ? "verdadero" : "falso")
                                    + " [" + tipo + "]";
                        }

                        if ("cadena".equals(tipo)) {
                            return "primario -> id = " + id + " [" + tipo + "]";
                        }

                        return "primario -> id = " + id
                                + ", valor = " + tabla.get(id)
                                + " [" + tipo + "]";
                    }

                    return "primario -> id = " + id + " [ERROR: no definida]";
                }

                if (ctx.e != null) {
                    String tipo = analizador.inferirTipo(ctx.e);
                    String valorVisual = obtenerValorVisual(ctx.e, analizador);
                    return "primario -> (expresion) = " + valorVisual + " [" + tipo + "]";
                }

                if (ctx.llamadaFuncion() != null) {
                    String tipo = analizador.inferirTipoLlamadaFuncion(ctx.llamadaFuncion());
                    return "primario -> llamada = " + ctx.llamadaFuncion().ID().getText() + "() [" + tipo + "]";
                }
            }

        } catch (Exception e) {
            return base + " [ERROR]";
        }

        return base;
    }

    private String obtenerValorVisual(ParseTree node, AnalisisSemantica analizador) {
        String tipo = inferirTipoNodo(node, analizador);

        if ("cadena".equals(tipo)) {
            return extraerTextoExpresion(node, analizador);
        }

        if ("booleano".equals(tipo)) {
            Double valor = evaluarNodo(node, analizador);
            return (valor != null && valor != 0) ? "verdadero" : "falso";
        }

        if ("entero".equals(tipo) || "real".equals(tipo)) {
            Double valor = evaluarNodo(node, analizador);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.PrimarioContext ctx) {
            if (ctx.CADENA_LIT() != null) return ctx.CADENA_LIT().getText();
            if (ctx.VERDADERO() != null) return "verdadero";
            if (ctx.FALSO() != null) return "falso";
            if (ctx.NUMERO() != null) return ctx.NUMERO().getText();

            if (ctx.ID() != null) {
                String id = ctx.ID().getText();
                String tipoId = analizador.getTipos().getOrDefault(id, "desconocido");

                if ("booleano".equals(tipoId) && analizador.getTabla().containsKey(id)) {
                    return analizador.getTabla().get(id) != 0 ? "verdadero" : "falso";
                }

                if ("cadena".equals(tipoId)) {
                    return id;
                }

                if (analizador.getTabla().containsKey(id)) {
                    return String.valueOf(analizador.getTabla().get(id));
                }

                return id;
            }

            if (ctx.e != null) {
                return obtenerValorVisual(ctx.e, analizador);
            }
        }

        return node.getText();
    }

    private String extraerTextoExpresion(ParseTree node, AnalisisSemantica analizador) {
       if (node instanceof LenguajeParser.PrimarioContext ctx) {
           if (ctx.CADENA_LIT() != null) {
               return ctx.CADENA_LIT().getText();
           }

           if (ctx.ID() != null) {
               return ctx.ID().getText();
           }

           if (ctx.e != null) {
               return extraerTextoExpresion(ctx.e, analizador);
           }

           if (ctx.llamadaFuncion() != null) {
               return ctx.llamadaFuncion().ID().getText() + "()";
           }
       }

       if (node instanceof LenguajeParser.SumaContext ctx) {
           if (inferirTipoNodo(ctx, analizador).equals("cadena") && ctx.mult().size() > 1) {
               StringBuilder sb = new StringBuilder();
               sb.append(extraerTextoExpresion(ctx.mult(0), analizador));

               for (int i = 1; i < ctx.mult().size(); i++) {
                   if (ctx.MAS(i - 1) != null) {
                       sb.append(" + ").append(extraerTextoExpresion(ctx.mult(i), analizador));
                   } else {
                       sb.append(" - ").append(extraerTextoExpresion(ctx.mult(i), analizador));
                   }
               }
               return sb.toString();
           }
       }

       for (int i = 0; i < node.getChildCount(); i++) {
           String texto = extraerTextoExpresion(node.getChild(i), analizador);
           if (texto != null && !texto.isBlank()) {
               return texto;
           }
       }

       return node.getText();
   }
    
    

    private static class SemanticNode implements Tree {

        private final String text;
        private final List<Tree> children = new ArrayList<>();
        private Tree parent;

        public SemanticNode(String text) {
            this.text = text;
        }

        public void addChild(SemanticNode child) {
            child.parent = this;
            children.add(child);
        }

        @Override
        public Tree getParent() {
            return parent;
        }

        @Override
        public Tree getChild(int i) {
            return children.get(i);
        }

        @Override
        public int getChildCount() {
            return children.size();
        }

        @Override
        public Object getPayload() {
            return text;
        }

        @Override
        public String toStringTree() {
            if (children.isEmpty()) {
                return text;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("(").append(text);

            for (Tree child : children) {
                sb.append(" ").append(child.toString());
            }

            sb.append(")");
            return sb.toString();
        }

        @Override
        public String toString() {
            return text;
        }
    }
    private Double evaluarNodo(ParseTree node, AnalisisSemantica analizador) {
            try {
                if (node instanceof LenguajeParser.ExpresionContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.OrExprContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.AndExprContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.IgualdadContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.ComparacionContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.SumaContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.MultContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.UnarioContext ctx) return analizador.visit(ctx);
                if (node instanceof LenguajeParser.PrimarioContext ctx) return analizador.visit(ctx);
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        private String inferirTipoNodo(ParseTree node, AnalisisSemantica analizador) {
            try {
                if (node instanceof LenguajeParser.ExpresionContext ctx) return analizador.inferirTipo(ctx);
                if (node instanceof LenguajeParser.OrExprContext ctx) return inferirTipoOr(ctx, analizador);
                if (node instanceof LenguajeParser.AndExprContext ctx) return inferirTipoAnd(ctx, analizador);
                if (node instanceof LenguajeParser.IgualdadContext ctx) return inferirTipoIgualdad(ctx, analizador);
                if (node instanceof LenguajeParser.ComparacionContext ctx) return inferirTipoComparacion(ctx, analizador);
                if (node instanceof LenguajeParser.SumaContext ctx) return inferirTipoSuma(ctx, analizador);
                if (node instanceof LenguajeParser.MultContext ctx) return inferirTipoMult(ctx, analizador);
                if (node instanceof LenguajeParser.UnarioContext ctx) return inferirTipoUnario(ctx, analizador);
                if (node instanceof LenguajeParser.PrimarioContext ctx) return inferirTipoPrimario(ctx, analizador);
            } catch (Exception e) {
                return "desconocido";
            }
            return "desconocido";
        }

        private String inferirTipoOr(LenguajeParser.OrExprContext ctx, AnalisisSemantica analizador) {
            if (ctx.andExpr().size() > 1) return "booleano";
            return inferirTipoAnd(ctx.andExpr(0), analizador);
        }

        private String inferirTipoAnd(LenguajeParser.AndExprContext ctx, AnalisisSemantica analizador) {
            if (ctx.igualdad().size() > 1) return "booleano";
            return inferirTipoIgualdad(ctx.igualdad(0), analizador);
        }

        private String inferirTipoIgualdad(LenguajeParser.IgualdadContext ctx, AnalisisSemantica analizador) {
            if (ctx.comparacion().size() > 1) return "booleano";
            return inferirTipoComparacion(ctx.comparacion(0), analizador);
        }

        private String inferirTipoComparacion(LenguajeParser.ComparacionContext ctx, AnalisisSemantica analizador) {
            if (ctx.suma().size() > 1) return "booleano";
            return inferirTipoSuma(ctx.suma(0), analizador);
        }

        private String inferirTipoSuma(LenguajeParser.SumaContext ctx, AnalisisSemantica analizador) {
            String tipo = inferirTipoMult(ctx.mult(0), analizador);

            for (int i = 1; i < ctx.mult().size(); i++) {
                String der = inferirTipoMult(ctx.mult(i), analizador);

                if (ctx.MAS(i - 1) != null) {
                    if ("cadena".equals(tipo) && "cadena".equals(der)) {
                        tipo = "cadena";
                    } else if ("real".equals(tipo) || "real".equals(der)) {
                        tipo = "real";
                    } else if ("entero".equals(tipo) && "entero".equals(der)) {
                        tipo = "entero";
                    } else {
                        tipo = "incompatible";
                    }
                } else {
                    if ("real".equals(tipo) || "real".equals(der)) {
                        tipo = "real";
                    } else if ("entero".equals(tipo) && "entero".equals(der)) {
                        tipo = "entero";
                    } else {
                        tipo = "incompatible";
                    }
                }
            }

            return tipo;
        }

        private String inferirTipoMult(LenguajeParser.MultContext ctx, AnalisisSemantica analizador) {
            String tipo = inferirTipoUnario(ctx.unario(0), analizador);

            for (int i = 1; i < ctx.unario().size(); i++) {
                String der = inferirTipoUnario(ctx.unario(i), analizador);

                if ("real".equals(tipo) || "real".equals(der)) {
                    tipo = "real";
                } else if ("entero".equals(tipo) && "entero".equals(der)) {
                    tipo = "entero";
                } else {
                    tipo = "incompatible";
                }
            }

            return tipo;
        }

        private String inferirTipoUnario(LenguajeParser.UnarioContext ctx, AnalisisSemantica analizador) {
            if (ctx.primario() != null) return inferirTipoPrimario(ctx.primario(), analizador);
            if (ctx.NO() != null) return "booleano";
            return inferirTipoUnario(ctx.u, analizador);
        }

        private String inferirTipoPrimario(LenguajeParser.PrimarioContext ctx, AnalisisSemantica analizador) {
            if (ctx.NUMERO() != null) {
                return ctx.NUMERO().getText().contains(".") ? "real" : "entero";
            }

            if (ctx.VERDADERO() != null || ctx.FALSO() != null) {
                return "booleano";
            }

            if (ctx.CADENA_LIT() != null) {
                return "cadena";
            }

            if (ctx.ID() != null) {
                return analizador.getTipos().getOrDefault(ctx.ID().getText(), "desconocido");
            }

            if (ctx.e != null) {
                return analizador.inferirTipo(ctx.e);
            }

            if (ctx.llamadaFuncion() != null) {
                return analizador.inferirTipoLlamadaFuncion(ctx.llamadaFuncion());
            }

            if (ctx.leer() != null) {
                return "entero";
            }

            return "desconocido";
        }
}