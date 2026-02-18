// Generated from C:/Users/DFSS/Documents/NetBeansProjects/Analizador-Lexico-y-sintactico/Analizador-Lexico-y-sintactico/src/grammar/Lenguaje.g4 by ANTLR 4.13.2
package generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LenguajeParser}.
 */
public interface LenguajeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(LenguajeParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(LenguajeParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 */
	void enterSentencia(LenguajeParser.SentenciaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 */
	void exitSentencia(LenguajeParser.SentenciaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracion(LenguajeParser.DeclaracionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracion(LenguajeParser.DeclaracionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(LenguajeParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(LenguajeParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void enterAsignacion(LenguajeParser.AsignacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void exitAsignacion(LenguajeParser.AsignacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#imprimir}.
	 * @param ctx the parse tree
	 */
	void enterImprimir(LenguajeParser.ImprimirContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#imprimir}.
	 * @param ctx the parse tree
	 */
	void exitImprimir(LenguajeParser.ImprimirContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#si}.
	 * @param ctx the parse tree
	 */
	void enterSi(LenguajeParser.SiContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#si}.
	 * @param ctx the parse tree
	 */
	void exitSi(LenguajeParser.SiContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#mientras}.
	 * @param ctx the parse tree
	 */
	void enterMientras(LenguajeParser.MientrasContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#mientras}.
	 * @param ctx the parse tree
	 */
	void exitMientras(LenguajeParser.MientrasContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#bloque}.
	 * @param ctx the parse tree
	 */
	void enterBloque(LenguajeParser.BloqueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#bloque}.
	 * @param ctx the parse tree
	 */
	void exitBloque(LenguajeParser.BloqueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#sentenciaBloque}.
	 * @param ctx the parse tree
	 */
	void enterSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#sentenciaBloque}.
	 * @param ctx the parse tree
	 */
	void exitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterExpresion(LenguajeParser.ExpresionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitExpresion(LenguajeParser.ExpresionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(LenguajeParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(LenguajeParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(LenguajeParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(LenguajeParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#igualdad}.
	 * @param ctx the parse tree
	 */
	void enterIgualdad(LenguajeParser.IgualdadContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#igualdad}.
	 * @param ctx the parse tree
	 */
	void exitIgualdad(LenguajeParser.IgualdadContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#comparacion}.
	 * @param ctx the parse tree
	 */
	void enterComparacion(LenguajeParser.ComparacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#comparacion}.
	 * @param ctx the parse tree
	 */
	void exitComparacion(LenguajeParser.ComparacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#suma}.
	 * @param ctx the parse tree
	 */
	void enterSuma(LenguajeParser.SumaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#suma}.
	 * @param ctx the parse tree
	 */
	void exitSuma(LenguajeParser.SumaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#mult}.
	 * @param ctx the parse tree
	 */
	void enterMult(LenguajeParser.MultContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#mult}.
	 * @param ctx the parse tree
	 */
	void exitMult(LenguajeParser.MultContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#unario}.
	 * @param ctx the parse tree
	 */
	void enterUnario(LenguajeParser.UnarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#unario}.
	 * @param ctx the parse tree
	 */
	void exitUnario(LenguajeParser.UnarioContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#primario}.
	 * @param ctx the parse tree
	 */
	void enterPrimario(LenguajeParser.PrimarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#primario}.
	 * @param ctx the parse tree
	 */
	void exitPrimario(LenguajeParser.PrimarioContext ctx);
	/**
	 * Enter a parse tree produced by {@link LenguajeParser#leer}.
	 * @param ctx the parse tree
	 */
	void enterLeer(LenguajeParser.LeerContext ctx);
	/**
	 * Exit a parse tree produced by {@link LenguajeParser#leer}.
	 * @param ctx the parse tree
	 */
	void exitLeer(LenguajeParser.LeerContext ctx);
}