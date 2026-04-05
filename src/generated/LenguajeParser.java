// Generated from C:/Users/DFSS/Documents/NetBeansProjects/Compilador/src/grammar/Lenguaje.g4 by ANTLR 4.13.2
package generated;

    import java.util.Map;
    import java.util.HashMap;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class LenguajeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SI=1, SINO=2, MIENTRAS=3, IMPRIMIR=4, LEER=5, ENTERO=6, REAL=7, CADENA=8, 
		BOOLEANO=9, VERDADERO=10, FALSO=11, PAR_IZQ=12, PAR_DER=13, LLAVE_IZQ=14, 
		LLAVE_DER=15, FIN_SENT=16, IGUAL=17, IGUAL_IGUAL=18, DIFERENTE=19, MENOR=20, 
		MENOR_IGUAL=21, MAYOR=22, MAYOR_IGUAL=23, MAS=24, MENOS=25, POR=26, DIV=27, 
		Y_LOGICO=28, O_LOGICO=29, NO=30, ID=31, NUMERO=32, CADENA_LIT=33, WS=34, 
		COMENTARIO_LINEA=35, COMENTARIO_BLOQUE=36;
	public static final int
		RULE_programa = 0, RULE_sentencia = 1, RULE_declaracion = 2, RULE_tipo = 3, 
		RULE_asignacion = 4, RULE_imprimir = 5, RULE_si = 6, RULE_mientras = 7, 
		RULE_bloque = 8, RULE_sentenciaBloque = 9, RULE_expresion = 10, RULE_orExpr = 11, 
		RULE_andExpr = 12, RULE_igualdad = 13, RULE_comparacion = 14, RULE_suma = 15, 
		RULE_mult = 16, RULE_unario = 17, RULE_primario = 18, RULE_leer = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "sentencia", "declaracion", "tipo", "asignacion", "imprimir", 
			"si", "mientras", "bloque", "sentenciaBloque", "expresion", "orExpr", 
			"andExpr", "igualdad", "comparacion", "suma", "mult", "unario", "primario", 
			"leer"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'si'", "'sino'", "'mientras'", "'imprimir'", "'leer'", "'entero'", 
			"'real'", "'cadena'", "'booleano'", "'verdadero'", "'falso'", "'('", 
			"')'", "'{'", "'}'", "';'", "'='", "'=='", "'!='", "'<'", "'<='", "'>'", 
			"'>='", "'+'", "'-'", "'*'", "'/'", "'&&'", "'||'", "'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SI", "SINO", "MIENTRAS", "IMPRIMIR", "LEER", "ENTERO", "REAL", 
			"CADENA", "BOOLEANO", "VERDADERO", "FALSO", "PAR_IZQ", "PAR_DER", "LLAVE_IZQ", 
			"LLAVE_DER", "FIN_SENT", "IGUAL", "IGUAL_IGUAL", "DIFERENTE", "MENOR", 
			"MENOR_IGUAL", "MAYOR", "MAYOR_IGUAL", "MAS", "MENOS", "POR", "DIV", 
			"Y_LOGICO", "O_LOGICO", "NO", "ID", "NUMERO", "CADENA_LIT", "WS", "COMENTARIO_LINEA", 
			"COMENTARIO_BLOQUE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Lenguaje.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    Map<String, Double> symbolTable = new HashMap<String, Double>();
	    
	public LenguajeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramaContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LenguajeParser.EOF, 0); }
		public List<SentenciaContext> sentencia() {
			return getRuleContexts(SentenciaContext.class);
		}
		public SentenciaContext sentencia(int i) {
			return getRuleContext(SentenciaContext.class,i);
		}
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterPrograma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitPrograma(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitPrograma(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2147501018L) != 0)) {
				{
				{
				setState(40);
				sentencia();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SentenciaContext extends ParserRuleContext {
		public DeclaracionContext declaracion() {
			return getRuleContext(DeclaracionContext.class,0);
		}
		public TerminalNode FIN_SENT() { return getToken(LenguajeParser.FIN_SENT, 0); }
		public AsignacionContext asignacion() {
			return getRuleContext(AsignacionContext.class,0);
		}
		public ImprimirContext imprimir() {
			return getRuleContext(ImprimirContext.class,0);
		}
		public SiContext si() {
			return getRuleContext(SiContext.class,0);
		}
		public MientrasContext mientras() {
			return getRuleContext(MientrasContext.class,0);
		}
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public SentenciaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentencia; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterSentencia(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitSentencia(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitSentencia(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaContext sentencia() throws RecognitionException {
		SentenciaContext _localctx = new SentenciaContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sentencia);
		try {
			setState(63);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ENTERO:
			case REAL:
			case CADENA:
			case BOOLEANO:
				enterOuterAlt(_localctx, 1);
				{
				setState(48);
				declaracion();
				setState(49);
				match(FIN_SENT);
				 System.out.println("Sentencia declaración"); 
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(52);
				asignacion();
				setState(53);
				match(FIN_SENT);
				 System.out.println("Sentencia asignación"); 
				}
				break;
			case IMPRIMIR:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				imprimir();
				setState(57);
				match(FIN_SENT);
				 System.out.println("Sentencia imprimir"); 
				}
				break;
			case SI:
				enterOuterAlt(_localctx, 4);
				{
				setState(60);
				si();
				}
				break;
			case MIENTRAS:
				enterOuterAlt(_localctx, 5);
				{
				setState(61);
				mientras();
				}
				break;
			case LLAVE_IZQ:
				enterOuterAlt(_localctx, 6);
				{
				setState(62);
				bloque();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclaracionContext extends ParserRuleContext {
		public Token ID;
		public ExpresionContext e;
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
		public TerminalNode IGUAL() { return getToken(LenguajeParser.IGUAL, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public DeclaracionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterDeclaracion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitDeclaracion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitDeclaracion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracionContext declaracion() throws RecognitionException {
		DeclaracionContext _localctx = new DeclaracionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_declaracion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			tipo();
			setState(66);
			((DeclaracionContext)_localctx).ID = match(ID);
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IGUAL) {
				{
				setState(67);
				match(IGUAL);
				setState(68);
				((DeclaracionContext)_localctx).e = expresion();
				}
			}


			    if (((DeclaracionContext)_localctx).e != null)
			        symbolTable.put((((DeclaracionContext)_localctx).ID!=null?((DeclaracionContext)_localctx).ID.getText():null), ((DeclaracionContext)_localctx).e.value);
			    else
			        symbolTable.put((((DeclaracionContext)_localctx).ID!=null?((DeclaracionContext)_localctx).ID.getText():null), 0.0);
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TipoContext extends ParserRuleContext {
		public TerminalNode ENTERO() { return getToken(LenguajeParser.ENTERO, 0); }
		public TerminalNode REAL() { return getToken(LenguajeParser.REAL, 0); }
		public TerminalNode CADENA() { return getToken(LenguajeParser.CADENA, 0); }
		public TerminalNode BOOLEANO() { return getToken(LenguajeParser.BOOLEANO, 0); }
		public TipoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tipo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterTipo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitTipo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitTipo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TipoContext tipo() throws RecognitionException {
		TipoContext _localctx = new TipoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_tipo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 960L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsignacionContext extends ParserRuleContext {
		public Token ID;
		public ExpresionContext expresion;
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
		public TerminalNode IGUAL() { return getToken(LenguajeParser.IGUAL, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public AsignacionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asignacion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterAsignacion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitAsignacion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitAsignacion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsignacionContext asignacion() throws RecognitionException {
		AsignacionContext _localctx = new AsignacionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_asignacion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			((AsignacionContext)_localctx).ID = match(ID);
			setState(76);
			match(IGUAL);
			setState(77);
			((AsignacionContext)_localctx).expresion = expresion();
			 symbolTable.put((((AsignacionContext)_localctx).ID!=null?((AsignacionContext)_localctx).ID.getText():null), ((AsignacionContext)_localctx).expresion.value); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImprimirContext extends ParserRuleContext {
		public ExpresionContext e;
		public TerminalNode IMPRIMIR() { return getToken(LenguajeParser.IMPRIMIR, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public ImprimirContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imprimir; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterImprimir(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitImprimir(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitImprimir(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImprimirContext imprimir() throws RecognitionException {
		ImprimirContext _localctx = new ImprimirContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_imprimir);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(IMPRIMIR);
			setState(81);
			((ImprimirContext)_localctx).e = expresion();
			 System.out.println(((ImprimirContext)_localctx).e.value); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SiContext extends ParserRuleContext {
		public TerminalNode SI() { return getToken(LenguajeParser.SI, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public List<BloqueContext> bloque() {
			return getRuleContexts(BloqueContext.class);
		}
		public BloqueContext bloque(int i) {
			return getRuleContext(BloqueContext.class,i);
		}
		public TerminalNode SINO() { return getToken(LenguajeParser.SINO, 0); }
		public SiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_si; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterSi(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitSi(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitSi(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SiContext si() throws RecognitionException {
		SiContext _localctx = new SiContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_si);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(SI);
			setState(85);
			match(PAR_IZQ);
			setState(86);
			expresion();
			setState(87);
			match(PAR_DER);
			setState(88);
			bloque();
			 System.out.println("Estructura SI"); 
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SINO) {
				{
				setState(90);
				match(SINO);
				setState(91);
				bloque();
				 System.out.println("Tiene SINO"); 
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MientrasContext extends ParserRuleContext {
		public TerminalNode MIENTRAS() { return getToken(LenguajeParser.MIENTRAS, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public MientrasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mientras; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterMientras(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitMientras(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitMientras(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MientrasContext mientras() throws RecognitionException {
		MientrasContext _localctx = new MientrasContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_mientras);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(MIENTRAS);
			setState(97);
			match(PAR_IZQ);
			setState(98);
			expresion();
			setState(99);
			match(PAR_DER);
			setState(100);
			bloque();
			 System.out.println("Bucle MIENTRAS"); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BloqueContext extends ParserRuleContext {
		public TerminalNode LLAVE_IZQ() { return getToken(LenguajeParser.LLAVE_IZQ, 0); }
		public TerminalNode LLAVE_DER() { return getToken(LenguajeParser.LLAVE_DER, 0); }
		public List<SentenciaBloqueContext> sentenciaBloque() {
			return getRuleContexts(SentenciaBloqueContext.class);
		}
		public SentenciaBloqueContext sentenciaBloque(int i) {
			return getRuleContext(SentenciaBloqueContext.class,i);
		}
		public BloqueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bloque; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterBloque(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitBloque(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitBloque(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BloqueContext bloque() throws RecognitionException {
		BloqueContext _localctx = new BloqueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_bloque);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(LLAVE_IZQ);
			 System.out.println("Inicio bloque"); 
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2147501018L) != 0)) {
				{
				{
				setState(105);
				sentenciaBloque();
				}
				}
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(111);
			match(LLAVE_DER);
			 System.out.println("Fin bloque"); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SentenciaBloqueContext extends ParserRuleContext {
		public DeclaracionContext declaracion() {
			return getRuleContext(DeclaracionContext.class,0);
		}
		public TerminalNode FIN_SENT() { return getToken(LenguajeParser.FIN_SENT, 0); }
		public AsignacionContext asignacion() {
			return getRuleContext(AsignacionContext.class,0);
		}
		public ImprimirContext imprimir() {
			return getRuleContext(ImprimirContext.class,0);
		}
		public SiContext si() {
			return getRuleContext(SiContext.class,0);
		}
		public MientrasContext mientras() {
			return getRuleContext(MientrasContext.class,0);
		}
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public SentenciaBloqueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentenciaBloque; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterSentenciaBloque(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitSentenciaBloque(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitSentenciaBloque(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaBloqueContext sentenciaBloque() throws RecognitionException {
		SentenciaBloqueContext _localctx = new SentenciaBloqueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_sentenciaBloque);
		int _la;
		try {
			setState(129);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ENTERO:
			case REAL:
			case CADENA:
			case BOOLEANO:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				declaracion();
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(115);
					match(FIN_SENT);
					}
				}

				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				asignacion();
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(119);
					match(FIN_SENT);
					}
				}

				}
				break;
			case IMPRIMIR:
				enterOuterAlt(_localctx, 3);
				{
				setState(122);
				imprimir();
				setState(124);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(123);
					match(FIN_SENT);
					}
				}

				}
				break;
			case SI:
				enterOuterAlt(_localctx, 4);
				{
				setState(126);
				si();
				}
				break;
			case MIENTRAS:
				enterOuterAlt(_localctx, 5);
				{
				setState(127);
				mientras();
				}
				break;
			case LLAVE_IZQ:
				enterOuterAlt(_localctx, 6);
				{
				setState(128);
				bloque();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpresionContext extends ParserRuleContext {
		public double value;
		public OrExprContext o;
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
		}
		public ExpresionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expresion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterExpresion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitExpresion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitExpresion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpresionContext expresion() throws RecognitionException {
		ExpresionContext _localctx = new ExpresionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_expresion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			((ExpresionContext)_localctx).o = orExpr();
			 ((ExpresionContext)_localctx).value =  ((ExpresionContext)_localctx).o.value; 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrExprContext extends ParserRuleContext {
		public double value;
		public AndExprContext a1;
		public AndExprContext a2;
		public List<AndExprContext> andExpr() {
			return getRuleContexts(AndExprContext.class);
		}
		public AndExprContext andExpr(int i) {
			return getRuleContext(AndExprContext.class,i);
		}
		public List<TerminalNode> O_LOGICO() { return getTokens(LenguajeParser.O_LOGICO); }
		public TerminalNode O_LOGICO(int i) {
			return getToken(LenguajeParser.O_LOGICO, i);
		}
		public OrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_orExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			((OrExprContext)_localctx).a1 = andExpr();
			 ((OrExprContext)_localctx).value =  ((OrExprContext)_localctx).a1.value; 
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==O_LOGICO) {
				{
				{
				setState(136);
				match(O_LOGICO);
				setState(137);
				((OrExprContext)_localctx).a2 = andExpr();
				 ((OrExprContext)_localctx).value =  (_localctx.value != 0 || ((OrExprContext)_localctx).a2.value != 0) ? 1 : 0; 
				}
				}
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AndExprContext extends ParserRuleContext {
		public double value;
		public IgualdadContext an1;
		public IgualdadContext an2;
		public List<IgualdadContext> igualdad() {
			return getRuleContexts(IgualdadContext.class);
		}
		public IgualdadContext igualdad(int i) {
			return getRuleContext(IgualdadContext.class,i);
		}
		public List<TerminalNode> Y_LOGICO() { return getTokens(LenguajeParser.Y_LOGICO); }
		public TerminalNode Y_LOGICO(int i) {
			return getToken(LenguajeParser.Y_LOGICO, i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_andExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			((AndExprContext)_localctx).an1 = igualdad();
			 ((AndExprContext)_localctx).value =  ((AndExprContext)_localctx).an1.value; 
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Y_LOGICO) {
				{
				{
				setState(147);
				match(Y_LOGICO);
				setState(148);
				((AndExprContext)_localctx).an2 = igualdad();
				 ((AndExprContext)_localctx).value =  (_localctx.value != 0 && ((AndExprContext)_localctx).an2.value != 0) ? 1 : 0; 
				}
				}
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IgualdadContext extends ParserRuleContext {
		public double value;
		public ComparacionContext c1;
		public ComparacionContext c2;
		public List<ComparacionContext> comparacion() {
			return getRuleContexts(ComparacionContext.class);
		}
		public ComparacionContext comparacion(int i) {
			return getRuleContext(ComparacionContext.class,i);
		}
		public List<TerminalNode> IGUAL_IGUAL() { return getTokens(LenguajeParser.IGUAL_IGUAL); }
		public TerminalNode IGUAL_IGUAL(int i) {
			return getToken(LenguajeParser.IGUAL_IGUAL, i);
		}
		public List<TerminalNode> DIFERENTE() { return getTokens(LenguajeParser.DIFERENTE); }
		public TerminalNode DIFERENTE(int i) {
			return getToken(LenguajeParser.DIFERENTE, i);
		}
		public IgualdadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_igualdad; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterIgualdad(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitIgualdad(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitIgualdad(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IgualdadContext igualdad() throws RecognitionException {
		IgualdadContext _localctx = new IgualdadContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_igualdad);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			((IgualdadContext)_localctx).c1 = comparacion();
			 ((IgualdadContext)_localctx).value =  ((IgualdadContext)_localctx).c1.value; 
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IGUAL_IGUAL || _la==DIFERENTE) {
				{
				setState(166);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IGUAL_IGUAL:
					{
					setState(158);
					match(IGUAL_IGUAL);
					setState(159);
					((IgualdadContext)_localctx).c2 = comparacion();
					 ((IgualdadContext)_localctx).value =  (_localctx.value == ((IgualdadContext)_localctx).c2.value) ? 1 : 0; 
					}
					break;
				case DIFERENTE:
					{
					setState(162);
					match(DIFERENTE);
					setState(163);
					((IgualdadContext)_localctx).c2 = comparacion();
					 ((IgualdadContext)_localctx).value =  (_localctx.value != ((IgualdadContext)_localctx).c2.value) ? 1 : 0; 
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparacionContext extends ParserRuleContext {
		public double value;
		public SumaContext s1;
		public SumaContext s2;
		public List<SumaContext> suma() {
			return getRuleContexts(SumaContext.class);
		}
		public SumaContext suma(int i) {
			return getRuleContext(SumaContext.class,i);
		}
		public List<TerminalNode> MENOR() { return getTokens(LenguajeParser.MENOR); }
		public TerminalNode MENOR(int i) {
			return getToken(LenguajeParser.MENOR, i);
		}
		public List<TerminalNode> MENOR_IGUAL() { return getTokens(LenguajeParser.MENOR_IGUAL); }
		public TerminalNode MENOR_IGUAL(int i) {
			return getToken(LenguajeParser.MENOR_IGUAL, i);
		}
		public List<TerminalNode> MAYOR() { return getTokens(LenguajeParser.MAYOR); }
		public TerminalNode MAYOR(int i) {
			return getToken(LenguajeParser.MAYOR, i);
		}
		public List<TerminalNode> MAYOR_IGUAL() { return getTokens(LenguajeParser.MAYOR_IGUAL); }
		public TerminalNode MAYOR_IGUAL(int i) {
			return getToken(LenguajeParser.MAYOR_IGUAL, i);
		}
		public ComparacionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparacion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterComparacion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitComparacion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitComparacion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparacionContext comparacion() throws RecognitionException {
		ComparacionContext _localctx = new ComparacionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_comparacion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			((ComparacionContext)_localctx).s1 = suma();
			 ((ComparacionContext)_localctx).value =  ((ComparacionContext)_localctx).s1.value; 
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 15728640L) != 0)) {
				{
				setState(189);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case MENOR:
					{
					setState(173);
					match(MENOR);
					setState(174);
					((ComparacionContext)_localctx).s2 = suma();
					 ((ComparacionContext)_localctx).value =  (_localctx.value < ((ComparacionContext)_localctx).s2.value) ? 1 : 0; 
					}
					break;
				case MENOR_IGUAL:
					{
					setState(177);
					match(MENOR_IGUAL);
					setState(178);
					((ComparacionContext)_localctx).s2 = suma();
					 ((ComparacionContext)_localctx).value =  (_localctx.value <= ((ComparacionContext)_localctx).s2.value) ? 1 : 0; 
					}
					break;
				case MAYOR:
					{
					setState(181);
					match(MAYOR);
					setState(182);
					((ComparacionContext)_localctx).s2 = suma();
					 ((ComparacionContext)_localctx).value =  (_localctx.value > ((ComparacionContext)_localctx).s2.value) ? 1 : 0; 
					}
					break;
				case MAYOR_IGUAL:
					{
					setState(185);
					match(MAYOR_IGUAL);
					setState(186);
					((ComparacionContext)_localctx).s2 = suma();
					 ((ComparacionContext)_localctx).value =  (_localctx.value >= ((ComparacionContext)_localctx).s2.value) ? 1 : 0; 
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SumaContext extends ParserRuleContext {
		public double value;
		public MultContext m1;
		public MultContext m2;
		public List<MultContext> mult() {
			return getRuleContexts(MultContext.class);
		}
		public MultContext mult(int i) {
			return getRuleContext(MultContext.class,i);
		}
		public List<TerminalNode> MAS() { return getTokens(LenguajeParser.MAS); }
		public TerminalNode MAS(int i) {
			return getToken(LenguajeParser.MAS, i);
		}
		public List<TerminalNode> MENOS() { return getTokens(LenguajeParser.MENOS); }
		public TerminalNode MENOS(int i) {
			return getToken(LenguajeParser.MENOS, i);
		}
		public SumaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_suma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterSuma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitSuma(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitSuma(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SumaContext suma() throws RecognitionException {
		SumaContext _localctx = new SumaContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_suma);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			((SumaContext)_localctx).m1 = mult();
			 ((SumaContext)_localctx).value =  ((SumaContext)_localctx).m1.value; 
			setState(206);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MAS || _la==MENOS) {
				{
				setState(204);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case MAS:
					{
					setState(196);
					match(MAS);
					setState(197);
					((SumaContext)_localctx).m2 = mult();
					 _localctx.value += ((SumaContext)_localctx).m2.value; 
					}
					break;
				case MENOS:
					{
					setState(200);
					match(MENOS);
					setState(201);
					((SumaContext)_localctx).m2 = mult();
					 _localctx.value -= ((SumaContext)_localctx).m2.value; 
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(208);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultContext extends ParserRuleContext {
		public double value;
		public UnarioContext u1;
		public UnarioContext u2;
		public List<UnarioContext> unario() {
			return getRuleContexts(UnarioContext.class);
		}
		public UnarioContext unario(int i) {
			return getRuleContext(UnarioContext.class,i);
		}
		public List<TerminalNode> POR() { return getTokens(LenguajeParser.POR); }
		public TerminalNode POR(int i) {
			return getToken(LenguajeParser.POR, i);
		}
		public List<TerminalNode> DIV() { return getTokens(LenguajeParser.DIV); }
		public TerminalNode DIV(int i) {
			return getToken(LenguajeParser.DIV, i);
		}
		public MultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mult; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterMult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitMult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitMult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultContext mult() throws RecognitionException {
		MultContext _localctx = new MultContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_mult);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			((MultContext)_localctx).u1 = unario();
			 ((MultContext)_localctx).value =  ((MultContext)_localctx).u1.value; 
			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==POR || _la==DIV) {
				{
				setState(219);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case POR:
					{
					setState(211);
					match(POR);
					setState(212);
					((MultContext)_localctx).u2 = unario();
					 _localctx.value *= ((MultContext)_localctx).u2.value; 
					}
					break;
				case DIV:
					{
					setState(215);
					match(DIV);
					setState(216);
					((MultContext)_localctx).u2 = unario();
					 _localctx.value /= ((MultContext)_localctx).u2.value; 
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(223);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnarioContext extends ParserRuleContext {
		public double value;
		public UnarioContext u;
		public PrimarioContext primario;
		public TerminalNode MENOS() { return getToken(LenguajeParser.MENOS, 0); }
		public UnarioContext unario() {
			return getRuleContext(UnarioContext.class,0);
		}
		public TerminalNode NO() { return getToken(LenguajeParser.NO, 0); }
		public PrimarioContext primario() {
			return getRuleContext(PrimarioContext.class,0);
		}
		public UnarioContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unario; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterUnario(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitUnario(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitUnario(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnarioContext unario() throws RecognitionException {
		UnarioContext _localctx = new UnarioContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_unario);
		try {
			setState(235);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MENOS:
				enterOuterAlt(_localctx, 1);
				{
				setState(224);
				match(MENOS);
				setState(225);
				((UnarioContext)_localctx).u = unario();
				 ((UnarioContext)_localctx).value =  -((UnarioContext)_localctx).u.value; 
				}
				break;
			case NO:
				enterOuterAlt(_localctx, 2);
				{
				setState(228);
				match(NO);
				setState(229);
				((UnarioContext)_localctx).u = unario();
				 ((UnarioContext)_localctx).value =  (((UnarioContext)_localctx).u.value == 0) ? 1 : 0; 
				}
				break;
			case PAR_IZQ:
			case ID:
			case NUMERO:
				enterOuterAlt(_localctx, 3);
				{
				setState(232);
				((UnarioContext)_localctx).primario = primario();
				 ((UnarioContext)_localctx).value =  ((UnarioContext)_localctx).primario.value; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimarioContext extends ParserRuleContext {
		public double value;
		public Token NUMERO;
		public Token ID;
		public ExpresionContext e;
		public TerminalNode NUMERO() { return getToken(LenguajeParser.NUMERO, 0); }
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public PrimarioContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primario; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterPrimario(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitPrimario(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitPrimario(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimarioContext primario() throws RecognitionException {
		PrimarioContext _localctx = new PrimarioContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_primario);
		try {
			setState(246);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMERO:
				enterOuterAlt(_localctx, 1);
				{
				setState(237);
				((PrimarioContext)_localctx).NUMERO = match(NUMERO);
				 ((PrimarioContext)_localctx).value =  Double.parseDouble((((PrimarioContext)_localctx).NUMERO!=null?((PrimarioContext)_localctx).NUMERO.getText():null)); 
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				((PrimarioContext)_localctx).ID = match(ID);

				    if(symbolTable.containsKey((((PrimarioContext)_localctx).ID!=null?((PrimarioContext)_localctx).ID.getText():null)))
				        ((PrimarioContext)_localctx).value =  symbolTable.get((((PrimarioContext)_localctx).ID!=null?((PrimarioContext)_localctx).ID.getText():null));
				    else {
				        System.out.println("Variable no definida: " + (((PrimarioContext)_localctx).ID!=null?((PrimarioContext)_localctx).ID.getText():null));
				        ((PrimarioContext)_localctx).value =  0;
				    }
				    
				}
				break;
			case PAR_IZQ:
				enterOuterAlt(_localctx, 3);
				{
				setState(241);
				match(PAR_IZQ);
				setState(242);
				((PrimarioContext)_localctx).e = expresion();
				setState(243);
				match(PAR_DER);
				 ((PrimarioContext)_localctx).value =  ((PrimarioContext)_localctx).e.value; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LeerContext extends ParserRuleContext {
		public TerminalNode LEER() { return getToken(LenguajeParser.LEER, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public LeerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterLeer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitLeer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitLeer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeerContext leer() throws RecognitionException {
		LeerContext _localctx = new LeerContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_leer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(LEER);
			setState(249);
			match(PAR_IZQ);
			setState(250);
			match(PAR_DER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001$\u00fd\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0001\u0000\u0005\u0000*\b\u0000\n\u0000\f\u0000"+
		"-\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001@\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002F\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006_\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0005\bk\b\b\n\b\f\bn\t\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0003"+
		"\tu\b\t\u0001\t\u0001\t\u0003\ty\b\t\u0001\t\u0001\t\u0003\t}\b\t\u0001"+
		"\t\u0001\t\u0001\t\u0003\t\u0082\b\t\u0001\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b"+
		"\u008d\b\u000b\n\u000b\f\u000b\u0090\t\u000b\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0005\f\u0098\b\f\n\f\f\f\u009b\t\f\u0001\r\u0001\r"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u00a7\b\r\n\r\f\r\u00aa\t\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00be\b\u000e\n\u000e\f\u000e"+
		"\u00c1\t\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u00cd\b\u000f\n\u000f\f\u000f\u00d0\t\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0005\u0010\u00dc\b\u0010\n\u0010\f\u0010\u00df\t\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u00ec\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00f7\b\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0000\u0000"+
		"\u0014\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&\u0000\u0001\u0001\u0000\u0006\t\u0109\u0000+\u0001"+
		"\u0000\u0000\u0000\u0002?\u0001\u0000\u0000\u0000\u0004A\u0001\u0000\u0000"+
		"\u0000\u0006I\u0001\u0000\u0000\u0000\bK\u0001\u0000\u0000\u0000\nP\u0001"+
		"\u0000\u0000\u0000\fT\u0001\u0000\u0000\u0000\u000e`\u0001\u0000\u0000"+
		"\u0000\u0010g\u0001\u0000\u0000\u0000\u0012\u0081\u0001\u0000\u0000\u0000"+
		"\u0014\u0083\u0001\u0000\u0000\u0000\u0016\u0086\u0001\u0000\u0000\u0000"+
		"\u0018\u0091\u0001\u0000\u0000\u0000\u001a\u009c\u0001\u0000\u0000\u0000"+
		"\u001c\u00ab\u0001\u0000\u0000\u0000\u001e\u00c2\u0001\u0000\u0000\u0000"+
		" \u00d1\u0001\u0000\u0000\u0000\"\u00eb\u0001\u0000\u0000\u0000$\u00f6"+
		"\u0001\u0000\u0000\u0000&\u00f8\u0001\u0000\u0000\u0000(*\u0003\u0002"+
		"\u0001\u0000)(\u0001\u0000\u0000\u0000*-\u0001\u0000\u0000\u0000+)\u0001"+
		"\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000,.\u0001\u0000\u0000\u0000"+
		"-+\u0001\u0000\u0000\u0000./\u0005\u0000\u0000\u0001/\u0001\u0001\u0000"+
		"\u0000\u000001\u0003\u0004\u0002\u000012\u0005\u0010\u0000\u000023\u0006"+
		"\u0001\uffff\uffff\u00003@\u0001\u0000\u0000\u000045\u0003\b\u0004\u0000"+
		"56\u0005\u0010\u0000\u000067\u0006\u0001\uffff\uffff\u00007@\u0001\u0000"+
		"\u0000\u000089\u0003\n\u0005\u00009:\u0005\u0010\u0000\u0000:;\u0006\u0001"+
		"\uffff\uffff\u0000;@\u0001\u0000\u0000\u0000<@\u0003\f\u0006\u0000=@\u0003"+
		"\u000e\u0007\u0000>@\u0003\u0010\b\u0000?0\u0001\u0000\u0000\u0000?4\u0001"+
		"\u0000\u0000\u0000?8\u0001\u0000\u0000\u0000?<\u0001\u0000\u0000\u0000"+
		"?=\u0001\u0000\u0000\u0000?>\u0001\u0000\u0000\u0000@\u0003\u0001\u0000"+
		"\u0000\u0000AB\u0003\u0006\u0003\u0000BE\u0005\u001f\u0000\u0000CD\u0005"+
		"\u0011\u0000\u0000DF\u0003\u0014\n\u0000EC\u0001\u0000\u0000\u0000EF\u0001"+
		"\u0000\u0000\u0000FG\u0001\u0000\u0000\u0000GH\u0006\u0002\uffff\uffff"+
		"\u0000H\u0005\u0001\u0000\u0000\u0000IJ\u0007\u0000\u0000\u0000J\u0007"+
		"\u0001\u0000\u0000\u0000KL\u0005\u001f\u0000\u0000LM\u0005\u0011\u0000"+
		"\u0000MN\u0003\u0014\n\u0000NO\u0006\u0004\uffff\uffff\u0000O\t\u0001"+
		"\u0000\u0000\u0000PQ\u0005\u0004\u0000\u0000QR\u0003\u0014\n\u0000RS\u0006"+
		"\u0005\uffff\uffff\u0000S\u000b\u0001\u0000\u0000\u0000TU\u0005\u0001"+
		"\u0000\u0000UV\u0005\f\u0000\u0000VW\u0003\u0014\n\u0000WX\u0005\r\u0000"+
		"\u0000XY\u0003\u0010\b\u0000Y^\u0006\u0006\uffff\uffff\u0000Z[\u0005\u0002"+
		"\u0000\u0000[\\\u0003\u0010\b\u0000\\]\u0006\u0006\uffff\uffff\u0000]"+
		"_\u0001\u0000\u0000\u0000^Z\u0001\u0000\u0000\u0000^_\u0001\u0000\u0000"+
		"\u0000_\r\u0001\u0000\u0000\u0000`a\u0005\u0003\u0000\u0000ab\u0005\f"+
		"\u0000\u0000bc\u0003\u0014\n\u0000cd\u0005\r\u0000\u0000de\u0003\u0010"+
		"\b\u0000ef\u0006\u0007\uffff\uffff\u0000f\u000f\u0001\u0000\u0000\u0000"+
		"gh\u0005\u000e\u0000\u0000hl\u0006\b\uffff\uffff\u0000ik\u0003\u0012\t"+
		"\u0000ji\u0001\u0000\u0000\u0000kn\u0001\u0000\u0000\u0000lj\u0001\u0000"+
		"\u0000\u0000lm\u0001\u0000\u0000\u0000mo\u0001\u0000\u0000\u0000nl\u0001"+
		"\u0000\u0000\u0000op\u0005\u000f\u0000\u0000pq\u0006\b\uffff\uffff\u0000"+
		"q\u0011\u0001\u0000\u0000\u0000rt\u0003\u0004\u0002\u0000su\u0005\u0010"+
		"\u0000\u0000ts\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000u\u0082"+
		"\u0001\u0000\u0000\u0000vx\u0003\b\u0004\u0000wy\u0005\u0010\u0000\u0000"+
		"xw\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y\u0082\u0001\u0000"+
		"\u0000\u0000z|\u0003\n\u0005\u0000{}\u0005\u0010\u0000\u0000|{\u0001\u0000"+
		"\u0000\u0000|}\u0001\u0000\u0000\u0000}\u0082\u0001\u0000\u0000\u0000"+
		"~\u0082\u0003\f\u0006\u0000\u007f\u0082\u0003\u000e\u0007\u0000\u0080"+
		"\u0082\u0003\u0010\b\u0000\u0081r\u0001\u0000\u0000\u0000\u0081v\u0001"+
		"\u0000\u0000\u0000\u0081z\u0001\u0000\u0000\u0000\u0081~\u0001\u0000\u0000"+
		"\u0000\u0081\u007f\u0001\u0000\u0000\u0000\u0081\u0080\u0001\u0000\u0000"+
		"\u0000\u0082\u0013\u0001\u0000\u0000\u0000\u0083\u0084\u0003\u0016\u000b"+
		"\u0000\u0084\u0085\u0006\n\uffff\uffff\u0000\u0085\u0015\u0001\u0000\u0000"+
		"\u0000\u0086\u0087\u0003\u0018\f\u0000\u0087\u008e\u0006\u000b\uffff\uffff"+
		"\u0000\u0088\u0089\u0005\u001d\u0000\u0000\u0089\u008a\u0003\u0018\f\u0000"+
		"\u008a\u008b\u0006\u000b\uffff\uffff\u0000\u008b\u008d\u0001\u0000\u0000"+
		"\u0000\u008c\u0088\u0001\u0000\u0000\u0000\u008d\u0090\u0001\u0000\u0000"+
		"\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000"+
		"\u0000\u008f\u0017\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000"+
		"\u0000\u0091\u0092\u0003\u001a\r\u0000\u0092\u0099\u0006\f\uffff\uffff"+
		"\u0000\u0093\u0094\u0005\u001c\u0000\u0000\u0094\u0095\u0003\u001a\r\u0000"+
		"\u0095\u0096\u0006\f\uffff\uffff\u0000\u0096\u0098\u0001\u0000\u0000\u0000"+
		"\u0097\u0093\u0001\u0000\u0000\u0000\u0098\u009b\u0001\u0000\u0000\u0000"+
		"\u0099\u0097\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000"+
		"\u009a\u0019\u0001\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000"+
		"\u009c\u009d\u0003\u001c\u000e\u0000\u009d\u00a8\u0006\r\uffff\uffff\u0000"+
		"\u009e\u009f\u0005\u0012\u0000\u0000\u009f\u00a0\u0003\u001c\u000e\u0000"+
		"\u00a0\u00a1\u0006\r\uffff\uffff\u0000\u00a1\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a2\u00a3\u0005\u0013\u0000\u0000\u00a3\u00a4\u0003\u001c\u000e\u0000"+
		"\u00a4\u00a5\u0006\r\uffff\uffff\u0000\u00a5\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a6\u009e\u0001\u0000\u0000\u0000\u00a6\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a7\u00aa\u0001\u0000\u0000\u0000\u00a8\u00a6\u0001\u0000\u0000\u0000"+
		"\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u001b\u0001\u0000\u0000\u0000"+
		"\u00aa\u00a8\u0001\u0000\u0000\u0000\u00ab\u00ac\u0003\u001e\u000f\u0000"+
		"\u00ac\u00bf\u0006\u000e\uffff\uffff\u0000\u00ad\u00ae\u0005\u0014\u0000"+
		"\u0000\u00ae\u00af\u0003\u001e\u000f\u0000\u00af\u00b0\u0006\u000e\uffff"+
		"\uffff\u0000\u00b0\u00be\u0001\u0000\u0000\u0000\u00b1\u00b2\u0005\u0015"+
		"\u0000\u0000\u00b2\u00b3\u0003\u001e\u000f\u0000\u00b3\u00b4\u0006\u000e"+
		"\uffff\uffff\u0000\u00b4\u00be\u0001\u0000\u0000\u0000\u00b5\u00b6\u0005"+
		"\u0016\u0000\u0000\u00b6\u00b7\u0003\u001e\u000f\u0000\u00b7\u00b8\u0006"+
		"\u000e\uffff\uffff\u0000\u00b8\u00be\u0001\u0000\u0000\u0000\u00b9\u00ba"+
		"\u0005\u0017\u0000\u0000\u00ba\u00bb\u0003\u001e\u000f\u0000\u00bb\u00bc"+
		"\u0006\u000e\uffff\uffff\u0000\u00bc\u00be\u0001\u0000\u0000\u0000\u00bd"+
		"\u00ad\u0001\u0000\u0000\u0000\u00bd\u00b1\u0001\u0000\u0000\u0000\u00bd"+
		"\u00b5\u0001\u0000\u0000\u0000\u00bd\u00b9\u0001\u0000\u0000\u0000\u00be"+
		"\u00c1\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000\u00bf"+
		"\u00c0\u0001\u0000\u0000\u0000\u00c0\u001d\u0001\u0000\u0000\u0000\u00c1"+
		"\u00bf\u0001\u0000\u0000\u0000\u00c2\u00c3\u0003 \u0010\u0000\u00c3\u00ce"+
		"\u0006\u000f\uffff\uffff\u0000\u00c4\u00c5\u0005\u0018\u0000\u0000\u00c5"+
		"\u00c6\u0003 \u0010\u0000\u00c6\u00c7\u0006\u000f\uffff\uffff\u0000\u00c7"+
		"\u00cd\u0001\u0000\u0000\u0000\u00c8\u00c9\u0005\u0019\u0000\u0000\u00c9"+
		"\u00ca\u0003 \u0010\u0000\u00ca\u00cb\u0006\u000f\uffff\uffff\u0000\u00cb"+
		"\u00cd\u0001\u0000\u0000\u0000\u00cc\u00c4\u0001\u0000\u0000\u0000\u00cc"+
		"\u00c8\u0001\u0000\u0000\u0000\u00cd\u00d0\u0001\u0000\u0000\u0000\u00ce"+
		"\u00cc\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf"+
		"\u001f\u0001\u0000\u0000\u0000\u00d0\u00ce\u0001\u0000\u0000\u0000\u00d1"+
		"\u00d2\u0003\"\u0011\u0000\u00d2\u00dd\u0006\u0010\uffff\uffff\u0000\u00d3"+
		"\u00d4\u0005\u001a\u0000\u0000\u00d4\u00d5\u0003\"\u0011\u0000\u00d5\u00d6"+
		"\u0006\u0010\uffff\uffff\u0000\u00d6\u00dc\u0001\u0000\u0000\u0000\u00d7"+
		"\u00d8\u0005\u001b\u0000\u0000\u00d8\u00d9\u0003\"\u0011\u0000\u00d9\u00da"+
		"\u0006\u0010\uffff\uffff\u0000\u00da\u00dc\u0001\u0000\u0000\u0000\u00db"+
		"\u00d3\u0001\u0000\u0000\u0000\u00db\u00d7\u0001\u0000\u0000\u0000\u00dc"+
		"\u00df\u0001\u0000\u0000\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00dd"+
		"\u00de\u0001\u0000\u0000\u0000\u00de!\u0001\u0000\u0000\u0000\u00df\u00dd"+
		"\u0001\u0000\u0000\u0000\u00e0\u00e1\u0005\u0019\u0000\u0000\u00e1\u00e2"+
		"\u0003\"\u0011\u0000\u00e2\u00e3\u0006\u0011\uffff\uffff\u0000\u00e3\u00ec"+
		"\u0001\u0000\u0000\u0000\u00e4\u00e5\u0005\u001e\u0000\u0000\u00e5\u00e6"+
		"\u0003\"\u0011\u0000\u00e6\u00e7\u0006\u0011\uffff\uffff\u0000\u00e7\u00ec"+
		"\u0001\u0000\u0000\u0000\u00e8\u00e9\u0003$\u0012\u0000\u00e9\u00ea\u0006"+
		"\u0011\uffff\uffff\u0000\u00ea\u00ec\u0001\u0000\u0000\u0000\u00eb\u00e0"+
		"\u0001\u0000\u0000\u0000\u00eb\u00e4\u0001\u0000\u0000\u0000\u00eb\u00e8"+
		"\u0001\u0000\u0000\u0000\u00ec#\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005"+
		" \u0000\u0000\u00ee\u00f7\u0006\u0012\uffff\uffff\u0000\u00ef\u00f0\u0005"+
		"\u001f\u0000\u0000\u00f0\u00f7\u0006\u0012\uffff\uffff\u0000\u00f1\u00f2"+
		"\u0005\f\u0000\u0000\u00f2\u00f3\u0003\u0014\n\u0000\u00f3\u00f4\u0005"+
		"\r\u0000\u0000\u00f4\u00f5\u0006\u0012\uffff\uffff\u0000\u00f5\u00f7\u0001"+
		"\u0000\u0000\u0000\u00f6\u00ed\u0001\u0000\u0000\u0000\u00f6\u00ef\u0001"+
		"\u0000\u0000\u0000\u00f6\u00f1\u0001\u0000\u0000\u0000\u00f7%\u0001\u0000"+
		"\u0000\u0000\u00f8\u00f9\u0005\u0005\u0000\u0000\u00f9\u00fa\u0005\f\u0000"+
		"\u0000\u00fa\u00fb\u0005\r\u0000\u0000\u00fb\'\u0001\u0000\u0000\u0000"+
		"\u0015+?E^ltx|\u0081\u008e\u0099\u00a6\u00a8\u00bd\u00bf\u00cc\u00ce\u00db"+
		"\u00dd\u00eb\u00f6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}