package TablaDeSimbolos;

import generated.LenguajeBaseListener;
import generated.LenguajeParser;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;

public class GeneradorTabla extends LenguajeBaseListener {

    private final List<Simbolo> tablaDeSimbolos = new ArrayList<>();

    @Override
    public void enterDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();
        TerminalNode idNode = ctx.ID();

        if (idNode != null) {
            String nombreVar = idNode.getText();
            int linea = idNode.getSymbol().getLine();
            tablaDeSimbolos.add(new Simbolo(nombreVar, tipo, linea));
        }
    }

    @Override
    public void enterFuncion(LenguajeParser.FuncionContext ctx) {
        String tipo = "función " + ctx.tipo().getText();
        TerminalNode idNode = ctx.ID();

        if (idNode != null) {
            String nombreFuncion = idNode.getText();
            int linea = idNode.getSymbol().getLine();
            tablaDeSimbolos.add(new Simbolo(nombreFuncion, tipo, linea));
        }
    }

    @Override
    public void enterParametro(LenguajeParser.ParametroContext ctx) {
        String tipo = "parámetro " + ctx.tipo().getText();
        TerminalNode idNode = ctx.ID();

        if (idNode != null) {
            String nombreParametro = idNode.getText();
            int linea = idNode.getSymbol().getLine();
            tablaDeSimbolos.add(new Simbolo(nombreParametro, tipo, linea));
        }
    }

    public List<Simbolo> getTablaDeSimbolos() {
        return new ArrayList<>(tablaDeSimbolos);
    }

    public void clear() {
        tablaDeSimbolos.clear();
    }

    public void generarArchivo(String rutaArchivo) {
        try (FileWriter fileWriter = new FileWriter(rutaArchivo);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println("TABLA DE SIMBOLOS GENERADA");
            printWriter.println("==========================================");
            printWriter.println(String.format("%-15s | %-18s | %s", "NOMBRE", "TIPO", "LINEA"));
            printWriter.println("------------------------------------------");

            for (Simbolo s : tablaDeSimbolos) {
                printWriter.println(s.toString());
            }

            printWriter.println("==========================================");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}