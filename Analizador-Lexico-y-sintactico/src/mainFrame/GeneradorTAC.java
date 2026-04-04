package mainFrame; 

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;

public class GeneradorTAC extends LenguajeBaseVisitor<String> {
    private int tempCount = 1;
    private int labelCount = 1;
    private StringBuilder tacBuilder = new StringBuilder();

    private String newTemp() { return "t" + (tempCount++); }
    private String newLabel() { return "L" + (labelCount++); }
    public String getTAC() { return tacBuilder.toString(); }

    @Override
    public String visitPrograma(LenguajeParser.ProgramaContext ctx) {
        for (LenguajeParser.SentenciaContext sent : ctx.sentencia()) {
            visit(sent);
        }
        return tacBuilder.toString();
    }

    @Override
    public String visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        if (ctx.expresion() != null) {
            String id = ctx.ID().getText();
            String exprTemp = visit(ctx.expresion());
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
        String exprTemp = visit(ctx.expresion());
        tacBuilder.append("imprimir ").append(exprTemp).append("\n");
        return null;
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
            tacBuilder.append(newT).append(" = ").append(tempLeft).append(" ").append(op).append(" ").append(tempRight).append("\n");
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
            tacBuilder.append(newT).append(" = ").append(tempLeft).append(" ").append(op).append(" ").append(tempRight).append("\n");
            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) return ctx.NUMERO().getText();
        if (ctx.ID() != null) return ctx.ID().getText();
        if (ctx.CADENA_LIT() != null) return ctx.CADENA_LIT().getText();
        if (ctx.expresion() != null) return visit(ctx.expresion());
        return "";
    }
    
    @Override
    public String visitMientras(LenguajeParser.MientrasContext ctx) {
        String labelStart = newLabel(); 
        String labelEnd = newLabel();   

        tacBuilder.append(labelStart).append(":\n");
        
        String condTemp = visit(ctx.expresion());
        tacBuilder.append("ifFalso ").append(condTemp).append(" goto ").append(labelEnd).append("\n");

        visit(ctx.bloque());

        tacBuilder.append("goto ").append(labelStart).append("\n");
        
        tacBuilder.append(labelEnd).append(":\n");

        return null;
    }

    @Override
    public String visitSi(LenguajeParser.SiContext ctx) {
        String condTemp = visit(ctx.expresion());
        String labelFalse = newLabel(); 
        String labelEnd = newLabel();   

        tacBuilder.append("ifFalso ").append(condTemp).append(" goto ").append(labelFalse).append("\n");

        visit(ctx.bloque(0));
        
        if (ctx.SINO() != null) {
            tacBuilder.append("goto ").append(labelEnd).append("\n");
        }
        
        tacBuilder.append(labelFalse).append(":\n");

        if (ctx.SINO() != null) {
            visit(ctx.bloque(1));
            tacBuilder.append(labelEnd).append(":\n");
        }

        return null;
    }

    @Override
    public String visitIgualdad(LenguajeParser.IgualdadContext ctx) {
        if (ctx.comparacion().size() == 1) return visit(ctx.comparacion(0));
        String tempLeft = visit(ctx.comparacion(0));
        for (int i = 1; i < ctx.comparacion().size(); i++) {
            String tempRight = visit(ctx.comparacion(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();
            tacBuilder.append(newT).append(" = ").append(tempLeft).append(" ").append(op).append(" ").append(tempRight).append("\n");
            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitComparacion(LenguajeParser.ComparacionContext ctx) {
        if (ctx.suma().size() == 1) return visit(ctx.suma(0));
        String tempLeft = visit(ctx.suma(0));
        for (int i = 1; i < ctx.suma().size(); i++) {
            String tempRight = visit(ctx.suma(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();
            tacBuilder.append(newT).append(" = ").append(tempLeft).append(" ").append(op).append(" ").append(tempRight).append("\n");
            tempLeft = newT;
        }
        return tempLeft;
    }
    
    @Override
    public String visitOrExpr(LenguajeParser.OrExprContext ctx) {
        if (ctx.andExpr().size() == 1) return visit(ctx.andExpr(0));
        
        String tempLeft = visit(ctx.andExpr(0));
        for (int i = 1; i < ctx.andExpr().size(); i++) {
            String tempRight = visit(ctx.andExpr(i));
            String op = ctx.getChild(2 * i - 1).getText(); 
            String newT = newTemp();
            tacBuilder.append(newT).append(" = ").append(tempLeft).append(" ").append(op).append(" ").append(tempRight).append("\n");
            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitAndExpr(LenguajeParser.AndExprContext ctx) {
        if (ctx.igualdad().size() == 1) return visit(ctx.igualdad(0));
        
        String tempLeft = visit(ctx.igualdad(0));
        for (int i = 1; i < ctx.igualdad().size(); i++) {
            String tempRight = visit(ctx.igualdad(i));
            String op = ctx.getChild(2 * i - 1).getText();
            String newT = newTemp();
            tacBuilder.append(newT).append(" = ").append(tempLeft).append(" ").append(op).append(" ").append(tempRight).append("\n");
            tempLeft = newT;
        }
        return tempLeft;
    }

    @Override
    public String visitUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return visit(ctx.primario()); 
        }
        String op = ctx.getChild(0).getText(); 
        String tempExpr = visit(ctx.unario());
        String newT = newTemp();
        tacBuilder.append(newT).append(" = ").append(op).append(" ").append(tempExpr).append("\n");
        return newT;
    }
}

