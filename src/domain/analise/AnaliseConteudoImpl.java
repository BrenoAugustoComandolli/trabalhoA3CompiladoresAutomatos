package domain.analise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.DadosCompilacao;

public class AnaliseConteudoImpl implements IAnaliseConteudo {

	@Override
	public Map<TipoLexama, List<String>> analisaLexica(DadosCompilacao dados) {
		String codigoFonte = dados.getConteudoAnalise();

		Map<TipoLexama, List<String>> tokens = new EnumMap<>(TipoLexama.class);
		
		for (TipoLexama umTipo : TipoLexama.values()) {
			Pattern pattern = Pattern.compile(umTipo.getPadrao());
			Matcher matcher = pattern.matcher(codigoFonte);
			
			List<String> lCasosEncontrados = new ArrayList<>();
			while (matcher.find()) {
				if(umTipo.isPalavraReservada() || !isPalavraReservadaNoDicionarioLexico(matcher.group())) {					
					lCasosEncontrados.add(matcher.group());
				}
			}
			
			tokens.put(umTipo, lCasosEncontrados);
		}
		return tokens;
	}

	private boolean isPalavraReservadaNoDicionarioLexico(String token) {
		for (TipoLexama umLexama : TipoLexama.values()) {
			Pattern pattern = Pattern.compile(umLexama.getPadrao());
			Matcher matcher = pattern.matcher(token);
					
			while (matcher.find()) {
				if(umLexama.isPalavraReservada()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<String> analisaSintatica(Map<TipoLexama, List<String>> tokens) {
		List<String> logs = new ArrayList<>();
		boolean valido = true;
		
		Deque<Character> parenteses = new LinkedList<>();
	    for (String umToken : tokens.get(TipoLexama.PARENTESES)) {
		    valido = valido && verificaSeHaParenteseAbertoNaoFechado(logs, parenteses, umToken.charAt(0)); 
	    }
	    valido = valido && verificaSeHaParenteseFechadoSemAbertura(logs, parenteses);
	    
	    Deque<Character> chaves = new LinkedList<>();
	    for (String umToken : tokens.get(TipoLexama.CHAVE)) {
		    valido = valido && verificaSeHaChaveAbertaNaoFechada(logs, chaves, umToken.charAt(0));
	    }
	    valido = valido && verificaSeHaChaveFechadaNaoAberta(logs, chaves);
	    
	    if(valido) {	    	
	    	logs.add("Compilado com sucesso!");
	    }
	    return logs;
	}

	private boolean verificaSeHaChaveAbertaNaoFechada(List<String> logs, Deque<Character> chaves, char caracterAnalise) {
		if (caracterAnalise == SintaticoConstants.CHAVE_ABERTO) {
		    chaves.push(caracterAnalise);
		}
		if (caracterAnalise == SintaticoConstants.CHAVE_FECHADO) {
		    if (chaves.isEmpty()) {
		    	logs.add(">> Ha chaves fechadas sem abertura");
		        return false;
		    } else {
		        chaves.pop();
		    }
		}
		return true;
	}

	private boolean verificaSeHaParenteseAbertoNaoFechado(List<String> logs, Deque<Character> parenteses, char caracterAnalise) {
		if(caracterAnalise == SintaticoConstants.PARENTESE_ABERTO) {
		    parenteses.push(caracterAnalise);
		}
		if (caracterAnalise == SintaticoConstants.PARENTESE_FECHADO) {
		    if (parenteses.isEmpty()) {
		    	logs.add(">> Ha parenteses fechados sem abertura");
		    	return false;
		    } else {
		        parenteses.pop();
		    }
		}
		return true;
	}

	private boolean verificaSeHaChaveFechadaNaoAberta(List<String> logs, Deque<Character> chaves) {
		if (!chaves.isEmpty()) {
	    	logs.add(">> Ha chaves abertas sem fechamento");
	        return false;
	    }
		return true;
	}

	private boolean verificaSeHaParenteseFechadoSemAbertura(List<String> logs, Deque<Character> parenteses) {
		if (!parenteses.isEmpty()) {
	    	logs.add(">> Ha parenteses abertos sem fechamento");
	        return false;
	    }
		return true;
	}

	@Override
	public List<String> analisaSemantica(DadosCompilacao dados, Map<TipoLexama, List<String>> tokens) {	
		List<String> logs = new ArrayList<>();
		verificaSeIdentificadoresForamDecladados(dados.getConteudoAnalise(), tokens, logs);
		verificaSeOperacoesEntreNumerosParaVariaveisNumerericas(dados.getConteudoAnalise(), tokens, logs);
		
		if(logs.isEmpty()) {	    	
		   logs.add("Compilado com sucesso!");
		}
		return logs;
	}

	private void verificaSeIdentificadoresForamDecladados(String conteudoAnalise, Map<TipoLexama, List<String>> tokens, List<String> logs) {
		List<String> identificadores = tokens.get(TipoLexama.ID);
		
		if(identificadores != null) {			
			identificadores = removeItensDuplicados(identificadores);
			
			identificadores.forEach(umId -> {
				List<TipoLexama> enums = Arrays.asList(TipoLexama.values());
				List<TipoLexama> tipagens = filtrarTipagens(enums);
				boolean encontrou = false;
				
				for (TipoLexama umTipo : tipagens) {
					Pattern pattern = Pattern.compile(umTipo.getPadrao()+"\\s+("+umId+")\\s*:=\\s*(.+)");
					Matcher matcher = pattern.matcher(conteudoAnalise);	
					
					if(matcher.find()) {
						encontrou = true;
					}
				}
				if(!encontrou) {
					logs.add(">> Identificador "+umId+" nao foi declarado");
				}
			});
		}
	}

	private void verificaSeOperacoesEntreNumerosParaVariaveisNumerericas(String conteudoAnalise, Map<TipoLexama, List<String>> tokens, List<String> logs) {
		List<String> operadores = new ArrayList<>();
		operadores.addAll(tokens.get(TipoLexama.OPERADOR_MATEMATICO));
		operadores.addAll(tokens.get(TipoLexama.OPERADOR_RELACIONAL));
		
		if(!operadores.isEmpty()) {			
			operadores = removeItensDuplicados(operadores);
			
			operadores.forEach(umOperador -> {
				Pattern pattern = Pattern.compile("\\b(\\w*)\\s*" + umOperador + "\\s*(\\w*)\\b");
				Matcher matcher = pattern.matcher(conteudoAnalise);	
				
				while (matcher.find()) {
					String variaveisUsadas = matcher.group().replace(" "+umOperador+" ", ";");
					validaOperacaoNumericaRealizada(conteudoAnalise, tokens, logs, variaveisUsadas);
				}
			});
		}
	}

	private void validaOperacaoNumericaRealizada(String conteudoAnalise, Map<TipoLexama, List<String>> tokens,
			List<String> logs, String variaveisUsadas) {

		String[] arrayAvariaveis = variaveisUsadas.split(";");
		List<String> identificadores = tokens.get(TipoLexama.ID);

		for (String umaVariavel : arrayAvariaveis) {
			if(!umaVariavel.isBlank()) {
				if(identificadores.contains(umaVariavel)) {
					Pattern patternInteiro = Pattern.compile(TipoLexama.INTEIRO.getPadrao()+"\\s+("+umaVariavel+")\\s*:=\\s*(.+)");
					Matcher matcherInteiro = patternInteiro.matcher(conteudoAnalise);
					if(!matcherInteiro.find()) {
						logs.add(">> Operacao invalida: identificador "+umaVariavel+" nao e numerico, mas ta sendo usado em uma operacao numerica");
					}
				}else if (!isNumeric(umaVariavel.trim())) {
					logs.add(">> Operacao invalida: "+umaVariavel+" nao e um numerico, mas ta sendo usado em uma operacao numerica");
				}
			}
		}
	}
	
	private boolean isNumeric(String str) {
	    try {
	        Double.parseDouble(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	private List<TipoLexama> filtrarTipagens(List<TipoLexama> enums) {
	    return enums.stream().filter(TipoLexama::isTipagem).toList();
	}

	private List<String> removeItensDuplicados(List<String> lista) {
		return lista.stream().distinct().toList();
	}

}
