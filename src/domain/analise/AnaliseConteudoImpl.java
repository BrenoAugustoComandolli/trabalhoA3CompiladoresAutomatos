package domain.analise;

import java.util.ArrayList;
import java.util.EnumMap;
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
	public void analisaSintatica(DadosCompilacao dados) {

	}

	@Override
	public void analisaSemantica(DadosCompilacao dados) {

	}

}
