package CodigoIntermedio;

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;

public class GeneradorTAC extends LenguajeBaseVisitor<String> {

    private int tempCount = 1;
    private int labelCount = 1;
    private final StringBuilder tacBuilder = new StringBuilder();

    private String newTemp() {
        return "t" + (tempCount++);
    }

    private String newLabel() {
        return "L" + (labelCount++);
    }

    public String getTAC() {
        return tacBuilder.toString();
    }

    public void reset() {
        tempCount = 1;
        labelCount = 1;
        tacBuilder.setLength(0);
    }

    @Override
    public String visitPrograma(LenguajeParser.ProgramaContext ctx) {
        for (LenguajeParser.ElementoContext e : ctx.elemento()) {
            visit(e);
        }
        return tacBuilder.toString();
    }

    @Override
    public String visitElemento(LenguajeParser.ElementoContext ctx) {
        if (ctx.sentencia() != null) {
            visit(ctx.sentencia());
        } else if (ctx.funcion() != null) {
            visit(ctx.funcion());
        }
        return null;
    }

    @Override
    public String visitSentencia(LenguajeParser.SentenciaContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public String visitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public String visitFuncion(LenguajeParser.FuncionContext ctx) {
        String nombre = ctx.ID().getText();

        tacBuilder.append("func ").append(nombre).append("\n");

        if (ctx.parametros() != null) {
            for (LenguajeParser.ParametroContext p : ctx.parametros().parametro()) {
                tacBuilder.append("param ").append(p.ID().getText()).append("\n");
            }
        }

        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }

        tacBuilder.append("end func ").append(nombre).append("\n");
        return null;
    }

    @Override
    public String visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String id = ctx.ID().getText();

        if (ctx.e != null) {
            String exprTemp = visit(ctx.e);
            tacBuilder.append(id).append(" = ").append(exprTemp).append("\n");
        }

        return null;
    }

    @Override
    public String visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();
        String exprTemp = visit(ctx.expresion());
        tacBuilder.append(id).append(" = ").append(exprTemp).append("\n");
        return null;
    }

    @Override
    public String visitImprimir(LenguajeParser.ImprimirContext ctx) {
        String exprTemp = visit(ctx.e);
        tacBuilder.append("imprimir ").append(exprTemp).append("\n");
        return null;
    }

    @Override
    public String visitRetornar(LenguajeParser.RetornarContext ctx) {
        if (ctx.e != null) {
            String exprTemp = visit(ctx.e);
            tacBuilder.append("retornar ").append(exprTemp).append("\n");
        } else {
            tacBuilder.append("retornar").append("\n");
        }
        return null;
    }

    @Override
    public String visitLlamadaFuncion(LenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();

        if (ctx.argumentos() == null || ctx.argumentos().expresion().isEmpty()) {
            String t = newTemp();
            tacBuilder.append(t).append(" = call ").append(nombre).append(", 0").append("\n");
            return t;
        }

        int n = ctx.argumentos().expresion().size();
        for (LenguajeParser.ExpresionContext e : ctx.argumentos().expresion()) {
            String argTemp = visit(e);
            tacBuilder.append("param ").append(argTemp).append("\n");
        }

        String t = newTemp();
        tacBuilder.append(t).append(" = call ").append(nombre).append(", ").append(n).append("\n");
        return t;
    }

    @Override
    public String visitSi(LenguajeParser.SiContext ctx) {
        String condTemp = visit(ctx.c);
        String labelFalse = newLabel();
        String labelEnd = newLabel();

        tacBuilder.append("ifFalso ")
                  .append(condTemp)
                  .append(" goto ")
                  .append(labelFalse)
                  .append("\n");

        visit(ctx.b1);

        if (ctx.b2 != null) {
            tacBuilder.append("goto ").append(labelEnd).append("\n");
        }

        tacBuilder.append(labelFalse).append(":\n");

        if (ctx.b2 != null) {
            visit(ctx.b2);
            tacBuilder.append(labelEnd).append(":\n");
        }

        return null;
    }

    @Override
    public String visitMientras(LenguajeParser.MientrasContext ctx) {
        String labelStart = newLabel();
        String labelEnd = newLabel();

        tacBuilder.append(labelStart).append(":\n");

        String condTemp = visit(ctx.c);
        tacBuilder.append("ifFalso ")
                  .append(condTemp)
                  .append(" goto ")
                  .append(labelEnd)
                  .append("\n");

        visit(ctx.b);

        tacBuilder.append("goto ").append(labelStart).append("\n");
        tacBuilder.append(labelEnd).append(":\n");

        return null;
    }

    @Override
    public String visitBloque(LenguajeParser.BloqueContext ctx) {
        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }
        return null;
    }

    @Override
    public String visitExpresion(LenguajeParser.ExpresionContext ctx) {
        return visit(ctx.o);
    }

    @Override
    public String visitOrExpr(LenguajeParser.OrExprContext ctx) {
        if (ctx.andExpr().size() == 1) {
            return visit(ctx.andExpr(0));
        }

        String tempLeft = visit(ctx.andExpr(0));

        for (int i = 1; i < ctx.andExpr().size(); i++) {
            String tempRight = visit(ctx.andExpr(i));
            String newT = newTemp();

            tacBuilder.append(newT)
                      .append(" = ")
                      .append(tempLeft)
                      .append(" || ")
                      .append(tempRight)
                      .append("\n");

            tempLeft = newT;
        }

        return tempLeft;
    }

    @Override
    public String visitAndExpr(LenguajeParser.AndExprContext ctx) {
        if (ctx.igualdad().size() == 1) {
            return visit(ctx.igualdad(0));
        }

        String tempLeft = visit(ctx.igualdad(0));

        for (int i = 1; i < ctx.igualdad().size(); i++) {
            String tempRight = visit(ctx.igualdad(i));
            String newT = newTemp();

            tacBuilder.append(newT)
                      .append(" = ")
                      .append(tempLeft)
                      .append(" && ")
                      .append(tempRight)
                      .append("\n");

            tempLeft = newT;
        }

        return tempLeft;
    }

    @Override
    public String visitIgualdad(LenguajeParser.IgualdadContext ctx) {
        if (ctx.comparacion().size() == 1) {
            return visit(ctx.comparacion(0));
        }

        String tempLeft = visit(ctx.comparacion(0));
        for (int i = 1; i < ctx.comparacion().size(); i++) {
            String tempRight = visit(ctx.comparacion(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();

            tacBuilder.append(newT)
                      .append(" = ")
                      .append(tempLeft)
                      .append(" ")
                      .append(op)
                      .append(" ")
                      .append(tempRight)
                      .append("\n");

            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitComparacion(LenguajeParser.ComparacionContext ctx) {
        if (ctx.suma().size() == 1) {
            return visit(ctx.suma(0));
        }

        String tempLeft = visit(ctx.suma(0));
        for (int i = 1; i < ctx.suma().size(); i++) {
            String tempRight = visit(ctx.suma(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();

            tacBuilder.append(newT)
                      .append(" = ")
                      .append(tempLeft)
                      .append(" ")
                      .append(op)
                      .append(" ")
                      .append(tempRight)
                      .append("\n");

            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitSuma(LenguajeParser.SumaContext ctx) {
        if (ctx.mult().size() == 1) {
            return visit(ctx.mult(0));
        }

        String tempLeft = visit(ctx.mult(0));
        for (int i = 1; i < ctx.mult().size(); i++) {
            String tempRight = visit(ctx.mult(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();

            tacBuilder.append(newT)
                      .append(" = ")
                      .append(tempLeft)
                      .append(" ")
                      .append(op)
                      .append(" ")
                      .append(tempRight)
                      .append("\n");

            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitMult(LenguajeParser.MultContext ctx) {
        if (ctx.unario().size() == 1) {
            return visit(ctx.unario(0));
        }

        String tempLeft = visit(ctx.unario(0));
        for (int i = 1; i < ctx.unario().size(); i++) {
            String tempRight = visit(ctx.unario(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();

            tacBuilder.append(newT)
                      .append(" = ")
                      .append(tempLeft)
                      .append(" ")
                      .append(op)
                      .append(" ")
                      .append(tempRight)
                      .append("\n");

            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return visit(ctx.primario());
        }

        String val = visit(ctx.u);
        String op = ctx.getChild(0).getText();
        String newT = newTemp();

        tacBuilder.append(newT)
                  .append(" = ")
                  .append(op)
                  .append(" ")
                  .append(val)
                  .append("\n");

        return newT;
    }

    @Override
    public String visitPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) return ctx.NUMERO().getText();
        if (ctx.ID() != null) return ctx.ID().getText();
        if (ctx.CADENA_LIT() != null) return ctx.CADENA_LIT().getText();
        if (ctx.VERDADERO() != null) return "verdadero";
        if (ctx.FALSO() != null) return "falso";
        if (ctx.leer() != null) return "leer()";
        if (ctx.e != null) return visit(ctx.e);
        if (ctx.llamadaFuncion() != null) return visit(ctx.llamadaFuncion());
        return "";
    }
}