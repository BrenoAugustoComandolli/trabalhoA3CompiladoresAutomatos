package domain.analise;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.DadosCompilacao;
import domain.analise.SintaticoConstants;

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
		Stack<Character> parenteses = new Stack<>();
		Stack<Character> chaves = new Stack<>();

		for (char caracterAnalise : dados.getConteudoAnalise().toCharArray()) 
		{
			if (caracterAnalise == SintaticoConstants.PARENTESE_ABERTO) 
			{
				parenteses.push(caracterAnalise);
		    } 
			else if (caracterAnalise == SintaticoConstants.PARENTESE_FECHADO) 
		    {
		    	if (parenteses.isEmpty()) 
		    	{
		    		System.out.println("Parêntese aberto não foi fechado");
		    		return;
		        } 
		    	else 
		          parenteses.pop();
		    }
			else if (caracterAnalise == SintaticoConstants.CHAVE_ABERTO)
			{
            chaves.push(caracterAnalise);
			} 
			else if (caracterAnalise == SintaticoConstants.CHAVE_FECHADO)
			{
				if (!chaves.isEmpty())
				{
					System.out.println("Chave aberta não foi fechada");
					return;
				}
				else 
					chaves.pop();
			}
		}

		if (!parenteses.isEmpty()) 
		{
			System.out.println("Parêntese aberto não foi fechado");
			return;
		}
		
		if (!chaves.isEmpty()) 
		{
			System.out.println("Chave aberta não foi fechada");
			return;
        }
		
		System.out.println("Compilado com sucesso!");
	}

	@Override
	public void analisaSemantica(DadosCompilacao dados) {

	}

}
