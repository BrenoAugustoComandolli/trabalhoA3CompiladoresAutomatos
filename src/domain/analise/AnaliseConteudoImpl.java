package domain.analise;

import java.util.ArrayList;
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
	public void analisaSemantica(DadosCompilacao dados) {

	}

}
