// Generated from C:/Users/DFSS/Documents/NetBeansProjects/Analizador-Lexico-y-sintactico/Analizador-Lexico-y-sintactico/src/grammar/Lenguaje.g4 by ANTLR 4.13.2
package generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LenguajeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LenguajeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(LenguajeParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentencia(LenguajeParser.SentenciaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracion(LenguajeParser.DeclaracionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(LenguajeParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(LenguajeParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#imprimir}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImprimir(LenguajeParser.ImprimirContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#si}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSi(LenguajeParser.SiContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#mientras}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMientras(LenguajeParser.MientrasContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#bloque}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloque(LenguajeParser.BloqueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#sentenciaBloque}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresion(LenguajeParser.ExpresionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(LenguajeParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(LenguajeParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#igualdad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgualdad(LenguajeParser.IgualdadContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#comparacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparacion(LenguajeParser.ComparacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#suma}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuma(LenguajeParser.SumaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#mult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult(LenguajeParser.MultContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnario(LenguajeParser.UnarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#primario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimario(LenguajeParser.PrimarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link LenguajeParser#leer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeer(LenguajeParser.LeerContext ctx);
}