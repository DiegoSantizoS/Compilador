// Generated from C:/Users/DFSS/Documents/NetBeansProjects/Compilador/src/grammar/Lenguaje.g4 by ANTLR 4.13.2
package generated;

    import java.util.*;

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
		SI=1, SINO=2, MIENTRAS=3, IMPRIMIR=4, LEER=5, RETORNAR=6, VACIO=7, ENTERO=8, 
		REAL=9, CADENA=10, BOOLEANO=11, VERDADERO=12, FALSO=13, PAR_IZQ=14, PAR_DER=15, 
		LLAVE_IZQ=16, LLAVE_DER=17, FIN_SENT=18, COMA=19, IGUAL=20, IGUAL_IGUAL=21, 
		DIFERENTE=22, MENOR=23, MENOR_IGUAL=24, MAYOR=25, MAYOR_IGUAL=26, MAS=27, 
		MENOS=28, POR=29, DIV=30, Y_LOGICO=31, O_LOGICO=32, NO=33, ID=34, NUMERO=35, 
		CADENA_LIT=36, WS=37, COMENTARIO_LINEA=38, COMENTARIO_BLOQUE=39;
	public static final int
		RULE_programa = 0, RULE_elemento = 1, RULE_sentencia = 2, RULE_declaracion = 3, 
		RULE_tipo = 4, RULE_asignacion = 5, RULE_imprimir = 6, RULE_retornar = 7, 
		RULE_si = 8, RULE_mientras = 9, RULE_bloque = 10, RULE_sentenciaBloque = 11, 
		RULE_funcion = 12, RULE_parametros = 13, RULE_parametro = 14, RULE_llamadaFuncion = 15, 
		RULE_argumentos = 16, RULE_expresion = 17, RULE_orExpr = 18, RULE_andExpr = 19, 
		RULE_igualdad = 20, RULE_comparacion = 21, RULE_suma = 22, RULE_mult = 23, 
		RULE_unario = 24, RULE_primario = 25, RULE_leer = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "elemento", "sentencia", "declaracion", "tipo", "asignacion", 
			"imprimir", "retornar", "si", "mientras", "bloque", "sentenciaBloque", 
			"funcion", "parametros", "parametro", "llamadaFuncion", "argumentos", 
			"expresion", "orExpr", "andExpr", "igualdad", "comparacion", "suma", 
			"mult", "unario", "primario", "leer"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'si'", "'sino'", "'mientras'", "'imprimir'", "'leer'", "'retornar'", 
			"'vacio'", "'entero'", "'real'", "'cadena'", "'booleano'", "'verdadero'", 
			"'falso'", "'('", "')'", "'{'", "'}'", "';'", "','", "'='", "'=='", "'!='", 
			"'<'", "'<='", "'>'", "'>='", "'+'", "'-'", "'*'", "'/'", "'&&'", "'||'", 
			"'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SI", "SINO", "MIENTRAS", "IMPRIMIR", "LEER", "RETORNAR", "VACIO", 
			"ENTERO", "REAL", "CADENA", "BOOLEANO", "VERDADERO", "FALSO", "PAR_IZQ", 
			"PAR_DER", "LLAVE_IZQ", "LLAVE_DER", "FIN_SENT", "COMA", "IGUAL", "IGUAL_IGUAL", 
			"DIFERENTE", "MENOR", "MENOR_IGUAL", "MAYOR", "MAYOR_IGUAL", "MAS", "MENOS", 
			"POR", "DIV", "Y_LOGICO", "O_LOGICO", "NO", "ID", "NUMERO", "CADENA_LIT", 
			"WS", "COMENTARIO_LINEA", "COMENTARIO_BLOQUE"
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

	public LenguajeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramaContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LenguajeParser.EOF, 0); }
		public List<ElementoContext> elemento() {
			return getRuleContexts(ElementoContext.class);
		}
		public ElementoContext elemento(int i) {
			return getRuleContext(ElementoContext.class,i);
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

			          iniciarAmbito();
			      
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17179938778L) != 0)) {
				{
				{
				setState(55);
				elemento();
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(61);
			match(EOF);

			          cerrarAmbito();
			      
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
	public static class ElementoContext extends ParserRuleContext {
		public SentenciaContext sentencia() {
			return getRuleContext(SentenciaContext.class,0);
		}
		public FuncionContext funcion() {
			return getRuleContext(FuncionContext.class,0);
		}
		public ElementoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elemento; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterElemento(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitElemento(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitElemento(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementoContext elemento() throws RecognitionException {
		ElementoContext _localctx = new ElementoContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_elemento);
		try {
			setState(66);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				sentencia();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				funcion();
				}
				break;
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
		public RetornarContext retornar() {
			return getRuleContext(RetornarContext.class,0);
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
		public LlamadaFuncionContext llamadaFuncion() {
			return getRuleContext(LlamadaFuncionContext.class,0);
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
		enterRule(_localctx, 4, RULE_sentencia);
		try {
			setState(86);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				declaracion();
				setState(69);
				match(FIN_SENT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				asignacion();
				setState(72);
				match(FIN_SENT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(74);
				imprimir();
				setState(75);
				match(FIN_SENT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(77);
				retornar();
				setState(78);
				match(FIN_SENT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(80);
				si();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(81);
				mientras();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(82);
				bloque();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(83);
				llamadaFuncion();
				setState(84);
				match(FIN_SENT);
				}
				break;
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
		public TipoContext tipo;
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
		enterRule(_localctx, 6, RULE_declaracion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			((DeclaracionContext)_localctx).tipo = tipo();
			setState(89);
			((DeclaracionContext)_localctx).ID = match(ID);
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IGUAL) {
				{
				setState(90);
				match(IGUAL);
				setState(91);
				((DeclaracionContext)_localctx).e = expresion();
				}
			}


			          if (((DeclaracionContext)_localctx).e != null) {
			              if (!tiposCompatiblesAsignacion((((DeclaracionContext)_localctx).tipo!=null?_input.getText(((DeclaracionContext)_localctx).tipo.start,((DeclaracionContext)_localctx).tipo.stop):null), ((DeclaracionContext)_localctx).e.info.tipo)) {
			                  erroresSemanticos.add("Error semántico: no se puede inicializar '" + (((DeclaracionContext)_localctx).ID!=null?((DeclaracionContext)_localctx).ID.getText():null)
			                          + "' de tipo '" + (((DeclaracionContext)_localctx).tipo!=null?_input.getText(((DeclaracionContext)_localctx).tipo.start,((DeclaracionContext)_localctx).tipo.stop):null) + "' con una expresión de tipo '" + ((DeclaracionContext)_localctx).e.info.tipo + "'.");
			              }
			              declararVariable((((DeclaracionContext)_localctx).ID!=null?((DeclaracionContext)_localctx).ID.getText():null), (((DeclaracionContext)_localctx).tipo!=null?_input.getText(((DeclaracionContext)_localctx).tipo.start,((DeclaracionContext)_localctx).tipo.stop):null), convertirValor(((DeclaracionContext)_localctx).e.info, (((DeclaracionContext)_localctx).tipo!=null?_input.getText(((DeclaracionContext)_localctx).tipo.start,((DeclaracionContext)_localctx).tipo.stop):null)), true);
			          } else {
			              declararVariable((((DeclaracionContext)_localctx).ID!=null?((DeclaracionContext)_localctx).ID.getText():null), (((DeclaracionContext)_localctx).tipo!=null?_input.getText(((DeclaracionContext)_localctx).tipo.start,((DeclaracionContext)_localctx).tipo.stop):null), null, false);
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
	public static class TipoContext extends ParserRuleContext {
		public TerminalNode ENTERO() { return getToken(LenguajeParser.ENTERO, 0); }
		public TerminalNode REAL() { return getToken(LenguajeParser.REAL, 0); }
		public TerminalNode CADENA() { return getToken(LenguajeParser.CADENA, 0); }
		public TerminalNode BOOLEANO() { return getToken(LenguajeParser.BOOLEANO, 0); }
		public TerminalNode VACIO() { return getToken(LenguajeParser.VACIO, 0); }
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
		enterRule(_localctx, 8, RULE_tipo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3968L) != 0)) ) {
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
		public ExpresionContext e;
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
		enterRule(_localctx, 10, RULE_asignacion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			((AsignacionContext)_localctx).ID = match(ID);
			setState(99);
			match(IGUAL);
			setState(100);
			((AsignacionContext)_localctx).e = expresion();

			          asignarVariable((((AsignacionContext)_localctx).ID!=null?((AsignacionContext)_localctx).ID.getText():null), ((AsignacionContext)_localctx).e.info);
			      
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
		enterRule(_localctx, 12, RULE_imprimir);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(IMPRIMIR);
			setState(104);
			((ImprimirContext)_localctx).e = expresion();
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
	public static class RetornarContext extends ParserRuleContext {
		public ExpresionContext e;
		public TerminalNode RETORNAR() { return getToken(LenguajeParser.RETORNAR, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public RetornarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_retornar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterRetornar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitRetornar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitRetornar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RetornarContext retornar() throws RecognitionException {
		RetornarContext _localctx = new RetornarContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_retornar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(RETORNAR);
			setState(108);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(107);
				((RetornarContext)_localctx).e = expresion();
				}
				break;
			}

			          if (tipoRetornoActual == null) {
			              erroresSemanticos.add("Error semántico: 'retornar' usado fuera de una función.");
			          } else {
			              if ("vacio".equals(tipoRetornoActual)) {
			                  if (((RetornarContext)_localctx).e != null) {
			                      erroresSemanticos.add("Error semántico: la función '" + funcionActual + "' es de tipo 'vacio' y no debe retornar valor.");
			                  }
			              } else {
			                  if (((RetornarContext)_localctx).e == null) {
			                      erroresSemanticos.add("Error semántico: la función '" + funcionActual + "' debe retornar un valor de tipo '" + tipoRetornoActual + "'.");
			                  } else if (!tiposCompatiblesAsignacion(tipoRetornoActual, ((RetornarContext)_localctx).e.info.tipo)) {
			                      erroresSemanticos.add("Error semántico: la función '" + funcionActual + "' debe retornar tipo '" + tipoRetornoActual
			                              + "', pero retorna '" + ((RetornarContext)_localctx).e.info.tipo + "'.");
			                  }
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
	public static class SiContext extends ParserRuleContext {
		public ExpresionContext c;
		public BloqueContext b1;
		public BloqueContext b2;
		public TerminalNode SI() { return getToken(LenguajeParser.SI, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
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
		enterRule(_localctx, 16, RULE_si);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(SI);
			setState(113);
			match(PAR_IZQ);
			setState(114);
			((SiContext)_localctx).c = expresion();
			setState(115);
			match(PAR_DER);
			setState(116);
			((SiContext)_localctx).b1 = bloque();
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SINO) {
				{
				setState(117);
				match(SINO);
				setState(118);
				((SiContext)_localctx).b2 = bloque();
				}
			}


			          if (!"booleano".equals(((SiContext)_localctx).c.info.tipo)) {
			              erroresSemanticos.add("Error semántico: la condición de 'si' debe ser de tipo booleano.");
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
		public ExpresionContext c;
		public BloqueContext b;
		public TerminalNode MIENTRAS() { return getToken(LenguajeParser.MIENTRAS, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
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
		enterRule(_localctx, 18, RULE_mientras);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(MIENTRAS);
			setState(124);
			match(PAR_IZQ);
			setState(125);
			((MientrasContext)_localctx).c = expresion();
			setState(126);
			match(PAR_DER);
			setState(127);
			((MientrasContext)_localctx).b = bloque();

			          if (!"booleano".equals(((MientrasContext)_localctx).c.info.tipo)) {
			              erroresSemanticos.add("Error semántico: la condición de 'mientras' debe ser de tipo booleano.");
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
		enterRule(_localctx, 20, RULE_bloque);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(LLAVE_IZQ);

			          iniciarAmbito();
			      
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17179938778L) != 0)) {
				{
				{
				setState(132);
				sentenciaBloque();
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(138);
			match(LLAVE_DER);

			          cerrarAmbito();
			      
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
		public RetornarContext retornar() {
			return getRuleContext(RetornarContext.class,0);
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
		public LlamadaFuncionContext llamadaFuncion() {
			return getRuleContext(LlamadaFuncionContext.class,0);
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
		enterRule(_localctx, 22, RULE_sentenciaBloque);
		int _la;
		try {
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(141);
				declaracion();
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(142);
					match(FIN_SENT);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(145);
				asignacion();
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(146);
					match(FIN_SENT);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(149);
				imprimir();
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(150);
					match(FIN_SENT);
					}
				}

				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(153);
				retornar();
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(154);
					match(FIN_SENT);
					}
				}

				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(157);
				si();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(158);
				mientras();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(159);
				bloque();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(160);
				llamadaFuncion();
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIN_SENT) {
					{
					setState(161);
					match(FIN_SENT);
					}
				}

				}
				break;
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
	public static class FuncionContext extends ParserRuleContext {
		public TipoContext tipo;
		public Token ID;
		public ParametrosContext params;
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public TerminalNode LLAVE_IZQ() { return getToken(LenguajeParser.LLAVE_IZQ, 0); }
		public TerminalNode LLAVE_DER() { return getToken(LenguajeParser.LLAVE_DER, 0); }
		public List<SentenciaBloqueContext> sentenciaBloque() {
			return getRuleContexts(SentenciaBloqueContext.class);
		}
		public SentenciaBloqueContext sentenciaBloque(int i) {
			return getRuleContext(SentenciaBloqueContext.class,i);
		}
		public ParametrosContext parametros() {
			return getRuleContext(ParametrosContext.class,0);
		}
		public FuncionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterFuncion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitFuncion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitFuncion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncionContext funcion() throws RecognitionException {
		FuncionContext _localctx = new FuncionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_funcion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			((FuncionContext)_localctx).tipo = tipo();
			setState(167);
			((FuncionContext)_localctx).ID = match(ID);
			setState(168);
			match(PAR_IZQ);
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3968L) != 0)) {
				{
				setState(169);
				((FuncionContext)_localctx).params = parametros();
				}
			}

			setState(172);
			match(PAR_DER);

			          List<String> listaTipos = new ArrayList<>();
			          if (((FuncionContext)_localctx).params != null) listaTipos = ((FuncionContext)_localctx).params.listaTipos;
			          declararFuncion((((FuncionContext)_localctx).ID!=null?((FuncionContext)_localctx).ID.getText():null), (((FuncionContext)_localctx).tipo!=null?_input.getText(((FuncionContext)_localctx).tipo.start,((FuncionContext)_localctx).tipo.stop):null), listaTipos);

			          funcionActual = (((FuncionContext)_localctx).ID!=null?((FuncionContext)_localctx).ID.getText():null);
			          tipoRetornoActual = (((FuncionContext)_localctx).tipo!=null?_input.getText(((FuncionContext)_localctx).tipo.start,((FuncionContext)_localctx).tipo.stop):null);

			          iniciarAmbito();

			          if (((FuncionContext)_localctx).params != null) {
			              for (int i = 0; i < ((FuncionContext)_localctx).params.nombres.size(); i++) {
			                  declararVariable(((FuncionContext)_localctx).params.nombres.get(i), ((FuncionContext)_localctx).params.listaTipos.get(i), null, true);
			              }
			          }
			      
			setState(174);
			match(LLAVE_IZQ);
			setState(178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17179938778L) != 0)) {
				{
				{
				setState(175);
				sentenciaBloque();
				}
				}
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(181);
			match(LLAVE_DER);

			          cerrarAmbito();
			          funcionActual = null;
			          tipoRetornoActual = null;
			      
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
	public static class ParametrosContext extends ParserRuleContext {
		public List<String> listaTipos;
		public List<String> nombres;
		public ParametroContext p1;
		public ParametroContext p2;
		public List<ParametroContext> parametro() {
			return getRuleContexts(ParametroContext.class);
		}
		public ParametroContext parametro(int i) {
			return getRuleContext(ParametroContext.class,i);
		}
		public List<TerminalNode> COMA() { return getTokens(LenguajeParser.COMA); }
		public TerminalNode COMA(int i) {
			return getToken(LenguajeParser.COMA, i);
		}
		public ParametrosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametros; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterParametros(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitParametros(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitParametros(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametrosContext parametros() throws RecognitionException {
		ParametrosContext _localctx = new ParametrosContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_parametros);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			((ParametrosContext)_localctx).p1 = parametro();

			          ((ParametrosContext)_localctx).listaTipos =  new ArrayList<>();
			          ((ParametrosContext)_localctx).nombres =  new ArrayList<>();
			          _localctx.listaTipos.add(((ParametrosContext)_localctx).p1.tipoParam);
			          _localctx.nombres.add(((ParametrosContext)_localctx).p1.nombreParam);
			      
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(186);
				match(COMA);
				setState(187);
				((ParametrosContext)_localctx).p2 = parametro();

				              _localctx.listaTipos.add(((ParametrosContext)_localctx).p2.tipoParam);
				              _localctx.nombres.add(((ParametrosContext)_localctx).p2.nombreParam);
				          
				}
				}
				setState(194);
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
	public static class ParametroContext extends ParserRuleContext {
		public String tipoParam;
		public String nombreParam;
		public TipoContext t;
		public Token ID;
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public ParametroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametro; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterParametro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitParametro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitParametro(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametroContext parametro() throws RecognitionException {
		ParametroContext _localctx = new ParametroContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parametro);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			((ParametroContext)_localctx).t = tipo();
			setState(196);
			((ParametroContext)_localctx).ID = match(ID);

			          ((ParametroContext)_localctx).tipoParam =  (((ParametroContext)_localctx).t!=null?_input.getText(((ParametroContext)_localctx).t.start,((ParametroContext)_localctx).t.stop):null);
			          ((ParametroContext)_localctx).nombreParam =  (((ParametroContext)_localctx).ID!=null?((ParametroContext)_localctx).ID.getText():null);
			      
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
	public static class LlamadaFuncionContext extends ParserRuleContext {
		public ValorSemantico info;
		public Token ID;
		public ArgumentosContext a;
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public ArgumentosContext argumentos() {
			return getRuleContext(ArgumentosContext.class,0);
		}
		public LlamadaFuncionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_llamadaFuncion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterLlamadaFuncion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitLlamadaFuncion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitLlamadaFuncion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LlamadaFuncionContext llamadaFuncion() throws RecognitionException {
		LlamadaFuncionContext _localctx = new LlamadaFuncionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_llamadaFuncion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			((LlamadaFuncionContext)_localctx).ID = match(ID);
			setState(200);
			match(PAR_IZQ);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 129117483040L) != 0)) {
				{
				setState(201);
				((LlamadaFuncionContext)_localctx).a = argumentos();
				}
			}

			setState(204);
			match(PAR_DER);

			          List<ValorSemantico> listaArgs = new ArrayList<>();
			          if (((LlamadaFuncionContext)_localctx).a != null) listaArgs = ((LlamadaFuncionContext)_localctx).a.lista;
			          ((LlamadaFuncionContext)_localctx).info =  validarLlamadaFuncion((((LlamadaFuncionContext)_localctx).ID!=null?((LlamadaFuncionContext)_localctx).ID.getText():null), listaArgs);
			      
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
	public static class ArgumentosContext extends ParserRuleContext {
		public List<ValorSemantico> lista;
		public ExpresionContext e1;
		public ExpresionContext e2;
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public List<TerminalNode> COMA() { return getTokens(LenguajeParser.COMA); }
		public TerminalNode COMA(int i) {
			return getToken(LenguajeParser.COMA, i);
		}
		public ArgumentosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).enterArgumentos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LenguajeListener ) ((LenguajeListener)listener).exitArgumentos(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LenguajeVisitor ) return ((LenguajeVisitor<? extends T>)visitor).visitArgumentos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentosContext argumentos() throws RecognitionException {
		ArgumentosContext _localctx = new ArgumentosContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_argumentos);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			((ArgumentosContext)_localctx).e1 = expresion();

			          ((ArgumentosContext)_localctx).lista =  new ArrayList<>();
			          _localctx.lista.add(((ArgumentosContext)_localctx).e1.info);
			      
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(209);
				match(COMA);
				setState(210);
				((ArgumentosContext)_localctx).e2 = expresion();

				              _localctx.lista.add(((ArgumentosContext)_localctx).e2.info);
				          
				}
				}
				setState(217);
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
	public static class ExpresionContext extends ParserRuleContext {
		public ValorSemantico info;
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
		enterRule(_localctx, 34, RULE_expresion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			((ExpresionContext)_localctx).o = orExpr();
			 ((ExpresionContext)_localctx).info =  ((ExpresionContext)_localctx).o.info; 
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
		public ValorSemantico info;
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
		enterRule(_localctx, 36, RULE_orExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			((OrExprContext)_localctx).a1 = andExpr();

			          ((OrExprContext)_localctx).info =  ((OrExprContext)_localctx).a1.info;
			      
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==O_LOGICO) {
				{
				{
				setState(223);
				match(O_LOGICO);
				setState(224);
				((OrExprContext)_localctx).a2 = andExpr();

				              if (!esBooleano(_localctx.info.tipo) || !esBooleano(((OrExprContext)_localctx).a2.info.tipo)) {
				                  erroresSemanticos.add("Error semántico: operador '||' requiere operandos booleanos.");
				                  ((OrExprContext)_localctx).info =  new ValorSemantico("error", null);
				              } else {
				                  ((OrExprContext)_localctx).info =  new ValorSemantico("booleano",
				                          aBooleano(_localctx.info.valor) || aBooleano(((OrExprContext)_localctx).a2.info.valor));
				              }
				          
				}
				}
				setState(231);
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
		public ValorSemantico info;
		public IgualdadContext a1;
		public IgualdadContext a2;
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
		enterRule(_localctx, 38, RULE_andExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			((AndExprContext)_localctx).a1 = igualdad();

			          ((AndExprContext)_localctx).info =  ((AndExprContext)_localctx).a1.info;
			      
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Y_LOGICO) {
				{
				{
				setState(234);
				match(Y_LOGICO);
				setState(235);
				((AndExprContext)_localctx).a2 = igualdad();

				              if (!esBooleano(_localctx.info.tipo) || !esBooleano(((AndExprContext)_localctx).a2.info.tipo)) {
				                  erroresSemanticos.add("Error semántico: operador '&&' requiere operandos booleanos.");
				                  ((AndExprContext)_localctx).info =  new ValorSemantico("error", null);
				              } else {
				                  ((AndExprContext)_localctx).info =  new ValorSemantico("booleano",
				                          aBooleano(_localctx.info.valor) && aBooleano(((AndExprContext)_localctx).a2.info.valor));
				              }
				          
				}
				}
				setState(242);
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
		public ValorSemantico info;
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
		enterRule(_localctx, 40, RULE_igualdad);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			((IgualdadContext)_localctx).c1 = comparacion();

			          ((IgualdadContext)_localctx).info =  ((IgualdadContext)_localctx).c1.info;
			      
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IGUAL_IGUAL || _la==DIFERENTE) {
				{
				setState(253);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IGUAL_IGUAL:
					{
					setState(245);
					match(IGUAL_IGUAL);
					setState(246);
					((IgualdadContext)_localctx).c2 = comparacion();

					              if (_localctx.info.tipo.equals("error") || ((IgualdadContext)_localctx).c2.info.tipo.equals("error")) {
					                  ((IgualdadContext)_localctx).info =  new ValorSemantico("error", null);
					              } else if (_localctx.info.tipo.equals(((IgualdadContext)_localctx).c2.info.tipo)
					                      || (esNumerico(_localctx.info.tipo) && esNumerico(((IgualdadContext)_localctx).c2.info.tipo))) {
					                  boolean r;
					                  if (esCadena(_localctx.info.tipo) && esCadena(((IgualdadContext)_localctx).c2.info.tipo)) {
					                      r = String.valueOf(_localctx.info.valor).equals(String.valueOf(((IgualdadContext)_localctx).c2.info.valor));
					                  } else {
					                      r = aDouble(_localctx.info.valor) == aDouble(((IgualdadContext)_localctx).c2.info.valor);
					                  }
					                  ((IgualdadContext)_localctx).info =  new ValorSemantico("booleano", r);
					              } else {
					                  erroresSemanticos.add("Error semántico: operador '==' con tipos incompatibles '" + _localctx.info.tipo + "' y '" + ((IgualdadContext)_localctx).c2.info.tipo + "'.");
					                  ((IgualdadContext)_localctx).info =  new ValorSemantico("error", null);
					              }
					          
					}
					break;
				case DIFERENTE:
					{
					setState(249);
					match(DIFERENTE);
					setState(250);
					((IgualdadContext)_localctx).c2 = comparacion();

					              if (_localctx.info.tipo.equals("error") || ((IgualdadContext)_localctx).c2.info.tipo.equals("error")) {
					                  ((IgualdadContext)_localctx).info =  new ValorSemantico("error", null);
					              } else if (_localctx.info.tipo.equals(((IgualdadContext)_localctx).c2.info.tipo)
					                      || (esNumerico(_localctx.info.tipo) && esNumerico(((IgualdadContext)_localctx).c2.info.tipo))) {
					                  boolean r;
					                  if (esCadena(_localctx.info.tipo) && esCadena(((IgualdadContext)_localctx).c2.info.tipo)) {
					                      r = !String.valueOf(_localctx.info.valor).equals(String.valueOf(((IgualdadContext)_localctx).c2.info.valor));
					                  } else {
					                      r = aDouble(_localctx.info.valor) != aDouble(((IgualdadContext)_localctx).c2.info.valor);
					                  }
					                  ((IgualdadContext)_localctx).info =  new ValorSemantico("booleano", r);
					              } else {
					                  erroresSemanticos.add("Error semántico: operador '!=' con tipos incompatibles '" + _localctx.info.tipo + "' y '" + ((IgualdadContext)_localctx).c2.info.tipo + "'.");
					                  ((IgualdadContext)_localctx).info =  new ValorSemantico("error", null);
					              }
					          
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(257);
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
		public ValorSemantico info;
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
		enterRule(_localctx, 42, RULE_comparacion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			((ComparacionContext)_localctx).s1 = suma();

			          ((ComparacionContext)_localctx).info =  ((ComparacionContext)_localctx).s1.info;
			      
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 125829120L) != 0)) {
				{
				setState(276);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case MENOR:
					{
					setState(260);
					match(MENOR);
					setState(261);
					((ComparacionContext)_localctx).s2 = suma();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((ComparacionContext)_localctx).s2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '<' requiere operandos numéricos.");
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("booleano", aDouble(_localctx.info.valor) < aDouble(((ComparacionContext)_localctx).s2.info.valor));
					              }
					          
					}
					break;
				case MENOR_IGUAL:
					{
					setState(264);
					match(MENOR_IGUAL);
					setState(265);
					((ComparacionContext)_localctx).s2 = suma();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((ComparacionContext)_localctx).s2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '<=' requiere operandos numéricos.");
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("booleano", aDouble(_localctx.info.valor) <= aDouble(((ComparacionContext)_localctx).s2.info.valor));
					              }
					          
					}
					break;
				case MAYOR:
					{
					setState(268);
					match(MAYOR);
					setState(269);
					((ComparacionContext)_localctx).s2 = suma();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((ComparacionContext)_localctx).s2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '>' requiere operandos numéricos.");
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("booleano", aDouble(_localctx.info.valor) > aDouble(((ComparacionContext)_localctx).s2.info.valor));
					              }
					          
					}
					break;
				case MAYOR_IGUAL:
					{
					setState(272);
					match(MAYOR_IGUAL);
					setState(273);
					((ComparacionContext)_localctx).s2 = suma();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((ComparacionContext)_localctx).s2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '>=' requiere operandos numéricos.");
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  ((ComparacionContext)_localctx).info =  new ValorSemantico("booleano", aDouble(_localctx.info.valor) >= aDouble(((ComparacionContext)_localctx).s2.info.valor));
					              }
					          
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(280);
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
		public ValorSemantico info;
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
		enterRule(_localctx, 44, RULE_suma);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			((SumaContext)_localctx).m1 = mult();

			          ((SumaContext)_localctx).info =  ((SumaContext)_localctx).m1.info;
			      
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MAS || _la==MENOS) {
				{
				setState(291);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case MAS:
					{
					setState(283);
					match(MAS);
					setState(284);
					((SumaContext)_localctx).m2 = mult();

					              if (esCadena(_localctx.info.tipo) && esCadena(((SumaContext)_localctx).m2.info.tipo)) {
					                  ((SumaContext)_localctx).info =  new ValorSemantico("cadena", String.valueOf(_localctx.info.valor) + String.valueOf(((SumaContext)_localctx).m2.info.valor));
					              } else if (esNumerico(_localctx.info.tipo) && esNumerico(((SumaContext)_localctx).m2.info.tipo)) {
					                  String tipoRes = tipoDominanteNumerico(_localctx.info.tipo, ((SumaContext)_localctx).m2.info.tipo);
					                  if ("real".equals(tipoRes)) {
					                      ((SumaContext)_localctx).info =  new ValorSemantico("real", aDouble(_localctx.info.valor) + aDouble(((SumaContext)_localctx).m2.info.valor));
					                  } else {
					                      ((SumaContext)_localctx).info =  new ValorSemantico("entero", aEntero(_localctx.info.valor) + aEntero(((SumaContext)_localctx).m2.info.valor));
					                  }
					              } else {
					                  erroresSemanticos.add("Error semántico: operador '+' con tipos incompatibles '" + _localctx.info.tipo + "' y '" + ((SumaContext)_localctx).m2.info.tipo + "'.");
					                  ((SumaContext)_localctx).info =  new ValorSemantico("error", null);
					              }
					          
					}
					break;
				case MENOS:
					{
					setState(287);
					match(MENOS);
					setState(288);
					((SumaContext)_localctx).m2 = mult();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((SumaContext)_localctx).m2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '-' requiere operandos numéricos.");
					                  ((SumaContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  String tipoRes = tipoDominanteNumerico(_localctx.info.tipo, ((SumaContext)_localctx).m2.info.tipo);
					                  if ("real".equals(tipoRes)) {
					                      ((SumaContext)_localctx).info =  new ValorSemantico("real", aDouble(_localctx.info.valor) - aDouble(((SumaContext)_localctx).m2.info.valor));
					                  } else {
					                      ((SumaContext)_localctx).info =  new ValorSemantico("entero", aEntero(_localctx.info.valor) - aEntero(((SumaContext)_localctx).m2.info.valor));
					                  }
					              }
					          
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(295);
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
		public ValorSemantico info;
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
		enterRule(_localctx, 46, RULE_mult);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			((MultContext)_localctx).u1 = unario();

			          ((MultContext)_localctx).info =  ((MultContext)_localctx).u1.info;
			      
			setState(308);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==POR || _la==DIV) {
				{
				setState(306);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case POR:
					{
					setState(298);
					match(POR);
					setState(299);
					((MultContext)_localctx).u2 = unario();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((MultContext)_localctx).u2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '*' requiere operandos numéricos.");
					                  ((MultContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  String tipoRes = tipoDominanteNumerico(_localctx.info.tipo, ((MultContext)_localctx).u2.info.tipo);
					                  if ("real".equals(tipoRes)) {
					                      ((MultContext)_localctx).info =  new ValorSemantico("real", aDouble(_localctx.info.valor) * aDouble(((MultContext)_localctx).u2.info.valor));
					                  } else {
					                      ((MultContext)_localctx).info =  new ValorSemantico("entero", aEntero(_localctx.info.valor) * aEntero(((MultContext)_localctx).u2.info.valor));
					                  }
					              }
					          
					}
					break;
				case DIV:
					{
					setState(302);
					match(DIV);
					setState(303);
					((MultContext)_localctx).u2 = unario();

					              if (!esNumerico(_localctx.info.tipo) || !esNumerico(((MultContext)_localctx).u2.info.tipo)) {
					                  erroresSemanticos.add("Error semántico: operador '/' requiere operandos numéricos.");
					                  ((MultContext)_localctx).info =  new ValorSemantico("error", null);
					              } else if (aDouble(((MultContext)_localctx).u2.info.valor) == 0.0) {
					                  erroresSemanticos.add("Error semántico: división entre cero.");
					                  ((MultContext)_localctx).info =  new ValorSemantico("error", null);
					              } else {
					                  ((MultContext)_localctx).info =  new ValorSemantico("real", aDouble(_localctx.info.valor) / aDouble(((MultContext)_localctx).u2.info.valor));
					              }
					          
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(310);
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
		public ValorSemantico info;
		public UnarioContext u;
		public PrimarioContext p;
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
		enterRule(_localctx, 48, RULE_unario);
		try {
			setState(322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MENOS:
				enterOuterAlt(_localctx, 1);
				{
				setState(311);
				match(MENOS);
				setState(312);
				((UnarioContext)_localctx).u = unario();

				          if (!esNumerico(((UnarioContext)_localctx).u.info.tipo)) {
				              erroresSemanticos.add("Error semántico: operador unario '-' requiere operando numérico.");
				              ((UnarioContext)_localctx).info =  new ValorSemantico("error", null);
				          } else if ("real".equals(((UnarioContext)_localctx).u.info.tipo)) {
				              ((UnarioContext)_localctx).info =  new ValorSemantico("real", -aDouble(((UnarioContext)_localctx).u.info.valor));
				          } else {
				              ((UnarioContext)_localctx).info =  new ValorSemantico("entero", -aEntero(((UnarioContext)_localctx).u.info.valor));
				          }
				      
				}
				break;
			case NO:
				enterOuterAlt(_localctx, 2);
				{
				setState(315);
				match(NO);
				setState(316);
				((UnarioContext)_localctx).u = unario();

				          if (!esBooleano(((UnarioContext)_localctx).u.info.tipo)) {
				              erroresSemanticos.add("Error semántico: operador '!' requiere operando booleano.");
				              ((UnarioContext)_localctx).info =  new ValorSemantico("error", null);
				          } else {
				              ((UnarioContext)_localctx).info =  new ValorSemantico("booleano", !aBooleano(((UnarioContext)_localctx).u.info.valor));
				          }
				      
				}
				break;
			case LEER:
			case VERDADERO:
			case FALSO:
			case PAR_IZQ:
			case ID:
			case NUMERO:
			case CADENA_LIT:
				enterOuterAlt(_localctx, 3);
				{
				setState(319);
				((UnarioContext)_localctx).p = primario();

				          ((UnarioContext)_localctx).info =  ((UnarioContext)_localctx).p.info;
				      
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
		public ValorSemantico info;
		public ExpresionContext e;
		public Token NUMERO;
		public Token CADENA_LIT;
		public LeerContext leer;
		public LlamadaFuncionContext llamadaFuncion;
		public Token ID;
		public TerminalNode PAR_IZQ() { return getToken(LenguajeParser.PAR_IZQ, 0); }
		public TerminalNode PAR_DER() { return getToken(LenguajeParser.PAR_DER, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode NUMERO() { return getToken(LenguajeParser.NUMERO, 0); }
		public TerminalNode VERDADERO() { return getToken(LenguajeParser.VERDADERO, 0); }
		public TerminalNode FALSO() { return getToken(LenguajeParser.FALSO, 0); }
		public TerminalNode CADENA_LIT() { return getToken(LenguajeParser.CADENA_LIT, 0); }
		public LeerContext leer() {
			return getRuleContext(LeerContext.class,0);
		}
		public LlamadaFuncionContext llamadaFuncion() {
			return getRuleContext(LlamadaFuncionContext.class,0);
		}
		public TerminalNode ID() { return getToken(LenguajeParser.ID, 0); }
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
		enterRule(_localctx, 50, RULE_primario);
		try {
			setState(345);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(324);
				match(PAR_IZQ);
				setState(325);
				((PrimarioContext)_localctx).e = expresion();
				setState(326);
				match(PAR_DER);

				          ((PrimarioContext)_localctx).info =  ((PrimarioContext)_localctx).e.info;
				      
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(329);
				((PrimarioContext)_localctx).NUMERO = match(NUMERO);

				          if ((((PrimarioContext)_localctx).NUMERO!=null?((PrimarioContext)_localctx).NUMERO.getText():null).contains(".")) {
				              ((PrimarioContext)_localctx).info =  new ValorSemantico("real", Double.parseDouble((((PrimarioContext)_localctx).NUMERO!=null?((PrimarioContext)_localctx).NUMERO.getText():null)));
				          } else {
				              ((PrimarioContext)_localctx).info =  new ValorSemantico("entero", Integer.parseInt((((PrimarioContext)_localctx).NUMERO!=null?((PrimarioContext)_localctx).NUMERO.getText():null)));
				          }
				      
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(331);
				match(VERDADERO);

				          ((PrimarioContext)_localctx).info =  new ValorSemantico("booleano", true);
				      
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(333);
				match(FALSO);

				          ((PrimarioContext)_localctx).info =  new ValorSemantico("booleano", false);
				      
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(335);
				((PrimarioContext)_localctx).CADENA_LIT = match(CADENA_LIT);

				          ((PrimarioContext)_localctx).info =  new ValorSemantico("cadena", quitarComillas((((PrimarioContext)_localctx).CADENA_LIT!=null?((PrimarioContext)_localctx).CADENA_LIT.getText():null)));
				      
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(337);
				((PrimarioContext)_localctx).leer = leer();

				          ((PrimarioContext)_localctx).info =  ((PrimarioContext)_localctx).leer.info;
				      
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(340);
				((PrimarioContext)_localctx).llamadaFuncion = llamadaFuncion();

				          ((PrimarioContext)_localctx).info =  ((PrimarioContext)_localctx).llamadaFuncion.info;
				      
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(343);
				((PrimarioContext)_localctx).ID = match(ID);

				          Simbolo s = buscarSimbolo((((PrimarioContext)_localctx).ID!=null?((PrimarioContext)_localctx).ID.getText():null));
				          if (s == null) {
				              erroresSemanticos.add("Error semántico: la variable '" + (((PrimarioContext)_localctx).ID!=null?((PrimarioContext)_localctx).ID.getText():null) + "' no ha sido declarada.");
				              ((PrimarioContext)_localctx).info =  new ValorSemantico("error", null);
				          } else {
				              ((PrimarioContext)_localctx).info =  new ValorSemantico(s.tipo, s.valor);
				          }
				      
				}
				break;
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
		public ValorSemantico info;
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
		enterRule(_localctx, 52, RULE_leer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(LEER);
			setState(348);
			match(PAR_IZQ);
			setState(349);
			match(PAR_DER);

			          ((LeerContext)_localctx).info =  new ValorSemantico("entero", 0);
			      
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
		"\u0004\u0001\'\u0161\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0001\u0000\u0001\u0000"+
		"\u0005\u00009\b\u0000\n\u0000\f\u0000<\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u0001C\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		"W\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003"+
		"]\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0003\u0007m\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\bx\b\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005\n\u0086\b\n\n\n\f\n\u0089\t\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0003\u000b\u0090\b\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u0094\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u0098\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u009c\b"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003"+
		"\u000b\u00a3\b\u000b\u0003\u000b\u00a5\b\u000b\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0003\f\u00ab\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00b1"+
		"\b\f\n\f\f\f\u00b4\t\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\r\u0001\r\u0005\r\u00bf\b\r\n\r\f\r\u00c2\t\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u00cb\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u00d6\b\u0010\n\u0010\f\u0010\u00d9\t\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0005\u0012\u00e4\b\u0012\n\u0012\f\u0012\u00e7\t\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013"+
		"\u00ef\b\u0013\n\u0013\f\u0013\u00f2\t\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0005\u0014\u00fe\b\u0014\n\u0014\f\u0014\u0101\t\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0005\u0015\u0115\b\u0015\n\u0015\f\u0015\u0118\t\u0015\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u0124\b\u0016\n\u0016\f\u0016"+
		"\u0127\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017"+
		"\u0133\b\u0017\n\u0017\f\u0017\u0136\t\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0143\b\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u015a\b\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0000\u0000\u001b\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.024\u0000\u0001\u0001\u0000\u0007\u000b\u0178\u00006\u0001\u0000"+
		"\u0000\u0000\u0002B\u0001\u0000\u0000\u0000\u0004V\u0001\u0000\u0000\u0000"+
		"\u0006X\u0001\u0000\u0000\u0000\b`\u0001\u0000\u0000\u0000\nb\u0001\u0000"+
		"\u0000\u0000\fg\u0001\u0000\u0000\u0000\u000ej\u0001\u0000\u0000\u0000"+
		"\u0010p\u0001\u0000\u0000\u0000\u0012{\u0001\u0000\u0000\u0000\u0014\u0082"+
		"\u0001\u0000\u0000\u0000\u0016\u00a4\u0001\u0000\u0000\u0000\u0018\u00a6"+
		"\u0001\u0000\u0000\u0000\u001a\u00b8\u0001\u0000\u0000\u0000\u001c\u00c3"+
		"\u0001\u0000\u0000\u0000\u001e\u00c7\u0001\u0000\u0000\u0000 \u00cf\u0001"+
		"\u0000\u0000\u0000\"\u00da\u0001\u0000\u0000\u0000$\u00dd\u0001\u0000"+
		"\u0000\u0000&\u00e8\u0001\u0000\u0000\u0000(\u00f3\u0001\u0000\u0000\u0000"+
		"*\u0102\u0001\u0000\u0000\u0000,\u0119\u0001\u0000\u0000\u0000.\u0128"+
		"\u0001\u0000\u0000\u00000\u0142\u0001\u0000\u0000\u00002\u0159\u0001\u0000"+
		"\u0000\u00004\u015b\u0001\u0000\u0000\u00006:\u0006\u0000\uffff\uffff"+
		"\u000079\u0003\u0002\u0001\u000087\u0001\u0000\u0000\u00009<\u0001\u0000"+
		"\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;=\u0001"+
		"\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000=>\u0005\u0000\u0000\u0001"+
		">?\u0006\u0000\uffff\uffff\u0000?\u0001\u0001\u0000\u0000\u0000@C\u0003"+
		"\u0004\u0002\u0000AC\u0003\u0018\f\u0000B@\u0001\u0000\u0000\u0000BA\u0001"+
		"\u0000\u0000\u0000C\u0003\u0001\u0000\u0000\u0000DE\u0003\u0006\u0003"+
		"\u0000EF\u0005\u0012\u0000\u0000FW\u0001\u0000\u0000\u0000GH\u0003\n\u0005"+
		"\u0000HI\u0005\u0012\u0000\u0000IW\u0001\u0000\u0000\u0000JK\u0003\f\u0006"+
		"\u0000KL\u0005\u0012\u0000\u0000LW\u0001\u0000\u0000\u0000MN\u0003\u000e"+
		"\u0007\u0000NO\u0005\u0012\u0000\u0000OW\u0001\u0000\u0000\u0000PW\u0003"+
		"\u0010\b\u0000QW\u0003\u0012\t\u0000RW\u0003\u0014\n\u0000ST\u0003\u001e"+
		"\u000f\u0000TU\u0005\u0012\u0000\u0000UW\u0001\u0000\u0000\u0000VD\u0001"+
		"\u0000\u0000\u0000VG\u0001\u0000\u0000\u0000VJ\u0001\u0000\u0000\u0000"+
		"VM\u0001\u0000\u0000\u0000VP\u0001\u0000\u0000\u0000VQ\u0001\u0000\u0000"+
		"\u0000VR\u0001\u0000\u0000\u0000VS\u0001\u0000\u0000\u0000W\u0005\u0001"+
		"\u0000\u0000\u0000XY\u0003\b\u0004\u0000Y\\\u0005\"\u0000\u0000Z[\u0005"+
		"\u0014\u0000\u0000[]\u0003\"\u0011\u0000\\Z\u0001\u0000\u0000\u0000\\"+
		"]\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^_\u0006\u0003\uffff"+
		"\uffff\u0000_\u0007\u0001\u0000\u0000\u0000`a\u0007\u0000\u0000\u0000"+
		"a\t\u0001\u0000\u0000\u0000bc\u0005\"\u0000\u0000cd\u0005\u0014\u0000"+
		"\u0000de\u0003\"\u0011\u0000ef\u0006\u0005\uffff\uffff\u0000f\u000b\u0001"+
		"\u0000\u0000\u0000gh\u0005\u0004\u0000\u0000hi\u0003\"\u0011\u0000i\r"+
		"\u0001\u0000\u0000\u0000jl\u0005\u0006\u0000\u0000km\u0003\"\u0011\u0000"+
		"lk\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000"+
		"\u0000no\u0006\u0007\uffff\uffff\u0000o\u000f\u0001\u0000\u0000\u0000"+
		"pq\u0005\u0001\u0000\u0000qr\u0005\u000e\u0000\u0000rs\u0003\"\u0011\u0000"+
		"st\u0005\u000f\u0000\u0000tw\u0003\u0014\n\u0000uv\u0005\u0002\u0000\u0000"+
		"vx\u0003\u0014\n\u0000wu\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000"+
		"xy\u0001\u0000\u0000\u0000yz\u0006\b\uffff\uffff\u0000z\u0011\u0001\u0000"+
		"\u0000\u0000{|\u0005\u0003\u0000\u0000|}\u0005\u000e\u0000\u0000}~\u0003"+
		"\"\u0011\u0000~\u007f\u0005\u000f\u0000\u0000\u007f\u0080\u0003\u0014"+
		"\n\u0000\u0080\u0081\u0006\t\uffff\uffff\u0000\u0081\u0013\u0001\u0000"+
		"\u0000\u0000\u0082\u0083\u0005\u0010\u0000\u0000\u0083\u0087\u0006\n\uffff"+
		"\uffff\u0000\u0084\u0086\u0003\u0016\u000b\u0000\u0085\u0084\u0001\u0000"+
		"\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000"+
		"\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u008a\u0001\u0000"+
		"\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0005\u0011"+
		"\u0000\u0000\u008b\u008c\u0006\n\uffff\uffff\u0000\u008c\u0015\u0001\u0000"+
		"\u0000\u0000\u008d\u008f\u0003\u0006\u0003\u0000\u008e\u0090\u0005\u0012"+
		"\u0000\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000"+
		"\u0000\u0000\u0090\u00a5\u0001\u0000\u0000\u0000\u0091\u0093\u0003\n\u0005"+
		"\u0000\u0092\u0094\u0005\u0012\u0000\u0000\u0093\u0092\u0001\u0000\u0000"+
		"\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0094\u00a5\u0001\u0000\u0000"+
		"\u0000\u0095\u0097\u0003\f\u0006\u0000\u0096\u0098\u0005\u0012\u0000\u0000"+
		"\u0097\u0096\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000"+
		"\u0098\u00a5\u0001\u0000\u0000\u0000\u0099\u009b\u0003\u000e\u0007\u0000"+
		"\u009a\u009c\u0005\u0012\u0000\u0000\u009b\u009a\u0001\u0000\u0000\u0000"+
		"\u009b\u009c\u0001\u0000\u0000\u0000\u009c\u00a5\u0001\u0000\u0000\u0000"+
		"\u009d\u00a5\u0003\u0010\b\u0000\u009e\u00a5\u0003\u0012\t\u0000\u009f"+
		"\u00a5\u0003\u0014\n\u0000\u00a0\u00a2\u0003\u001e\u000f\u0000\u00a1\u00a3"+
		"\u0005\u0012\u0000\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000\u00a2\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a3\u00a5\u0001\u0000\u0000\u0000\u00a4\u008d"+
		"\u0001\u0000\u0000\u0000\u00a4\u0091\u0001\u0000\u0000\u0000\u00a4\u0095"+
		"\u0001\u0000\u0000\u0000\u00a4\u0099\u0001\u0000\u0000\u0000\u00a4\u009d"+
		"\u0001\u0000\u0000\u0000\u00a4\u009e\u0001\u0000\u0000\u0000\u00a4\u009f"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a0\u0001\u0000\u0000\u0000\u00a5\u0017"+
		"\u0001\u0000\u0000\u0000\u00a6\u00a7\u0003\b\u0004\u0000\u00a7\u00a8\u0005"+
		"\"\u0000\u0000\u00a8\u00aa\u0005\u000e\u0000\u0000\u00a9\u00ab\u0003\u001a"+
		"\r\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000"+
		"\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00ad\u0005\u000f\u0000"+
		"\u0000\u00ad\u00ae\u0006\f\uffff\uffff\u0000\u00ae\u00b2\u0005\u0010\u0000"+
		"\u0000\u00af\u00b1\u0003\u0016\u000b\u0000\u00b0\u00af\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b4\u0001\u0000\u0000\u0000\u00b2\u00b0\u0001\u0000\u0000"+
		"\u0000\u00b2\u00b3\u0001\u0000\u0000\u0000\u00b3\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b5\u00b6\u0005\u0011\u0000"+
		"\u0000\u00b6\u00b7\u0006\f\uffff\uffff\u0000\u00b7\u0019\u0001\u0000\u0000"+
		"\u0000\u00b8\u00b9\u0003\u001c\u000e\u0000\u00b9\u00c0\u0006\r\uffff\uffff"+
		"\u0000\u00ba\u00bb\u0005\u0013\u0000\u0000\u00bb\u00bc\u0003\u001c\u000e"+
		"\u0000\u00bc\u00bd\u0006\r\uffff\uffff\u0000\u00bd\u00bf\u0001\u0000\u0000"+
		"\u0000\u00be\u00ba\u0001\u0000\u0000\u0000\u00bf\u00c2\u0001\u0000\u0000"+
		"\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000\u0000"+
		"\u0000\u00c1\u001b\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000"+
		"\u0000\u00c3\u00c4\u0003\b\u0004\u0000\u00c4\u00c5\u0005\"\u0000\u0000"+
		"\u00c5\u00c6\u0006\u000e\uffff\uffff\u0000\u00c6\u001d\u0001\u0000\u0000"+
		"\u0000\u00c7\u00c8\u0005\"\u0000\u0000\u00c8\u00ca\u0005\u000e\u0000\u0000"+
		"\u00c9\u00cb\u0003 \u0010\u0000\u00ca\u00c9\u0001\u0000\u0000\u0000\u00ca"+
		"\u00cb\u0001\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc"+
		"\u00cd\u0005\u000f\u0000\u0000\u00cd\u00ce\u0006\u000f\uffff\uffff\u0000"+
		"\u00ce\u001f\u0001\u0000\u0000\u0000\u00cf\u00d0\u0003\"\u0011\u0000\u00d0"+
		"\u00d7\u0006\u0010\uffff\uffff\u0000\u00d1\u00d2\u0005\u0013\u0000\u0000"+
		"\u00d2\u00d3\u0003\"\u0011\u0000\u00d3\u00d4\u0006\u0010\uffff\uffff\u0000"+
		"\u00d4\u00d6\u0001\u0000\u0000\u0000\u00d5\u00d1\u0001\u0000\u0000\u0000"+
		"\u00d6\u00d9\u0001\u0000\u0000\u0000\u00d7\u00d5\u0001\u0000\u0000\u0000"+
		"\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8!\u0001\u0000\u0000\u0000\u00d9"+
		"\u00d7\u0001\u0000\u0000\u0000\u00da\u00db\u0003$\u0012\u0000\u00db\u00dc"+
		"\u0006\u0011\uffff\uffff\u0000\u00dc#\u0001\u0000\u0000\u0000\u00dd\u00de"+
		"\u0003&\u0013\u0000\u00de\u00e5\u0006\u0012\uffff\uffff\u0000\u00df\u00e0"+
		"\u0005 \u0000\u0000\u00e0\u00e1\u0003&\u0013\u0000\u00e1\u00e2\u0006\u0012"+
		"\uffff\uffff\u0000\u00e2\u00e4\u0001\u0000\u0000\u0000\u00e3\u00df\u0001"+
		"\u0000\u0000\u0000\u00e4\u00e7\u0001\u0000\u0000\u0000\u00e5\u00e3\u0001"+
		"\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6%\u0001\u0000"+
		"\u0000\u0000\u00e7\u00e5\u0001\u0000\u0000\u0000\u00e8\u00e9\u0003(\u0014"+
		"\u0000\u00e9\u00f0\u0006\u0013\uffff\uffff\u0000\u00ea\u00eb\u0005\u001f"+
		"\u0000\u0000\u00eb\u00ec\u0003(\u0014\u0000\u00ec\u00ed\u0006\u0013\uffff"+
		"\uffff\u0000\u00ed\u00ef\u0001\u0000\u0000\u0000\u00ee\u00ea\u0001\u0000"+
		"\u0000\u0000\u00ef\u00f2\u0001\u0000\u0000\u0000\u00f0\u00ee\u0001\u0000"+
		"\u0000\u0000\u00f0\u00f1\u0001\u0000\u0000\u0000\u00f1\'\u0001\u0000\u0000"+
		"\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f3\u00f4\u0003*\u0015\u0000"+
		"\u00f4\u00ff\u0006\u0014\uffff\uffff\u0000\u00f5\u00f6\u0005\u0015\u0000"+
		"\u0000\u00f6\u00f7\u0003*\u0015\u0000\u00f7\u00f8\u0006\u0014\uffff\uffff"+
		"\u0000\u00f8\u00fe\u0001\u0000\u0000\u0000\u00f9\u00fa\u0005\u0016\u0000"+
		"\u0000\u00fa\u00fb\u0003*\u0015\u0000\u00fb\u00fc\u0006\u0014\uffff\uffff"+
		"\u0000\u00fc\u00fe\u0001\u0000\u0000\u0000\u00fd\u00f5\u0001\u0000\u0000"+
		"\u0000\u00fd\u00f9\u0001\u0000\u0000\u0000\u00fe\u0101\u0001\u0000\u0000"+
		"\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000\u0000"+
		"\u0000\u0100)\u0001\u0000\u0000\u0000\u0101\u00ff\u0001\u0000\u0000\u0000"+
		"\u0102\u0103\u0003,\u0016\u0000\u0103\u0116\u0006\u0015\uffff\uffff\u0000"+
		"\u0104\u0105\u0005\u0017\u0000\u0000\u0105\u0106\u0003,\u0016\u0000\u0106"+
		"\u0107\u0006\u0015\uffff\uffff\u0000\u0107\u0115\u0001\u0000\u0000\u0000"+
		"\u0108\u0109\u0005\u0018\u0000\u0000\u0109\u010a\u0003,\u0016\u0000\u010a"+
		"\u010b\u0006\u0015\uffff\uffff\u0000\u010b\u0115\u0001\u0000\u0000\u0000"+
		"\u010c\u010d\u0005\u0019\u0000\u0000\u010d\u010e\u0003,\u0016\u0000\u010e"+
		"\u010f\u0006\u0015\uffff\uffff\u0000\u010f\u0115\u0001\u0000\u0000\u0000"+
		"\u0110\u0111\u0005\u001a\u0000\u0000\u0111\u0112\u0003,\u0016\u0000\u0112"+
		"\u0113\u0006\u0015\uffff\uffff\u0000\u0113\u0115\u0001\u0000\u0000\u0000"+
		"\u0114\u0104\u0001\u0000\u0000\u0000\u0114\u0108\u0001\u0000\u0000\u0000"+
		"\u0114\u010c\u0001\u0000\u0000\u0000\u0114\u0110\u0001\u0000\u0000\u0000"+
		"\u0115\u0118\u0001\u0000\u0000\u0000\u0116\u0114\u0001\u0000\u0000\u0000"+
		"\u0116\u0117\u0001\u0000\u0000\u0000\u0117+\u0001\u0000\u0000\u0000\u0118"+
		"\u0116\u0001\u0000\u0000\u0000\u0119\u011a\u0003.\u0017\u0000\u011a\u0125"+
		"\u0006\u0016\uffff\uffff\u0000\u011b\u011c\u0005\u001b\u0000\u0000\u011c"+
		"\u011d\u0003.\u0017\u0000\u011d\u011e\u0006\u0016\uffff\uffff\u0000\u011e"+
		"\u0124\u0001\u0000\u0000\u0000\u011f\u0120\u0005\u001c\u0000\u0000\u0120"+
		"\u0121\u0003.\u0017\u0000\u0121\u0122\u0006\u0016\uffff\uffff\u0000\u0122"+
		"\u0124\u0001\u0000\u0000\u0000\u0123\u011b\u0001\u0000\u0000\u0000\u0123"+
		"\u011f\u0001\u0000\u0000\u0000\u0124\u0127\u0001\u0000\u0000\u0000\u0125"+
		"\u0123\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126"+
		"-\u0001\u0000\u0000\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0128\u0129"+
		"\u00030\u0018\u0000\u0129\u0134\u0006\u0017\uffff\uffff\u0000\u012a\u012b"+
		"\u0005\u001d\u0000\u0000\u012b\u012c\u00030\u0018\u0000\u012c\u012d\u0006"+
		"\u0017\uffff\uffff\u0000\u012d\u0133\u0001\u0000\u0000\u0000\u012e\u012f"+
		"\u0005\u001e\u0000\u0000\u012f\u0130\u00030\u0018\u0000\u0130\u0131\u0006"+
		"\u0017\uffff\uffff\u0000\u0131\u0133\u0001\u0000\u0000\u0000\u0132\u012a"+
		"\u0001\u0000\u0000\u0000\u0132\u012e\u0001\u0000\u0000\u0000\u0133\u0136"+
		"\u0001\u0000\u0000\u0000\u0134\u0132\u0001\u0000\u0000\u0000\u0134\u0135"+
		"\u0001\u0000\u0000\u0000\u0135/\u0001\u0000\u0000\u0000\u0136\u0134\u0001"+
		"\u0000\u0000\u0000\u0137\u0138\u0005\u001c\u0000\u0000\u0138\u0139\u0003"+
		"0\u0018\u0000\u0139\u013a\u0006\u0018\uffff\uffff\u0000\u013a\u0143\u0001"+
		"\u0000\u0000\u0000\u013b\u013c\u0005!\u0000\u0000\u013c\u013d\u00030\u0018"+
		"\u0000\u013d\u013e\u0006\u0018\uffff\uffff\u0000\u013e\u0143\u0001\u0000"+
		"\u0000\u0000\u013f\u0140\u00032\u0019\u0000\u0140\u0141\u0006\u0018\uffff"+
		"\uffff\u0000\u0141\u0143\u0001\u0000\u0000\u0000\u0142\u0137\u0001\u0000"+
		"\u0000\u0000\u0142\u013b\u0001\u0000\u0000\u0000\u0142\u013f\u0001\u0000"+
		"\u0000\u0000\u01431\u0001\u0000\u0000\u0000\u0144\u0145\u0005\u000e\u0000"+
		"\u0000\u0145\u0146\u0003\"\u0011\u0000\u0146\u0147\u0005\u000f\u0000\u0000"+
		"\u0147\u0148\u0006\u0019\uffff\uffff\u0000\u0148\u015a\u0001\u0000\u0000"+
		"\u0000\u0149\u014a\u0005#\u0000\u0000\u014a\u015a\u0006\u0019\uffff\uffff"+
		"\u0000\u014b\u014c\u0005\f\u0000\u0000\u014c\u015a\u0006\u0019\uffff\uffff"+
		"\u0000\u014d\u014e\u0005\r\u0000\u0000\u014e\u015a\u0006\u0019\uffff\uffff"+
		"\u0000\u014f\u0150\u0005$\u0000\u0000\u0150\u015a\u0006\u0019\uffff\uffff"+
		"\u0000\u0151\u0152\u00034\u001a\u0000\u0152\u0153\u0006\u0019\uffff\uffff"+
		"\u0000\u0153\u015a\u0001\u0000\u0000\u0000\u0154\u0155\u0003\u001e\u000f"+
		"\u0000\u0155\u0156\u0006\u0019\uffff\uffff\u0000\u0156\u015a\u0001\u0000"+
		"\u0000\u0000\u0157\u0158\u0005\"\u0000\u0000\u0158\u015a\u0006\u0019\uffff"+
		"\uffff\u0000\u0159\u0144\u0001\u0000\u0000\u0000\u0159\u0149\u0001\u0000"+
		"\u0000\u0000\u0159\u014b\u0001\u0000\u0000\u0000\u0159\u014d\u0001\u0000"+
		"\u0000\u0000\u0159\u014f\u0001\u0000\u0000\u0000\u0159\u0151\u0001\u0000"+
		"\u0000\u0000\u0159\u0154\u0001\u0000\u0000\u0000\u0159\u0157\u0001\u0000"+
		"\u0000\u0000\u015a3\u0001\u0000\u0000\u0000\u015b\u015c\u0005\u0005\u0000"+
		"\u0000\u015c\u015d\u0005\u000e\u0000\u0000\u015d\u015e\u0005\u000f\u0000"+
		"\u0000\u015e\u015f\u0006\u001a\uffff\uffff\u0000\u015f5\u0001\u0000\u0000"+
		"\u0000\u001e:BV\\lw\u0087\u008f\u0093\u0097\u009b\u00a2\u00a4\u00aa\u00b2"+
		"\u00c0\u00ca\u00d7\u00e5\u00f0\u00fd\u00ff\u0114\u0116\u0123\u0125\u0132"+
		"\u0134\u0142\u0159";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}