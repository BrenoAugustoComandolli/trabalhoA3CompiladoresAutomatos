package domain.analise;

import java.util.List;
import java.util.Map;

import data.DadosCompilacao;

public interface IAnaliseConteudo {

	Map<TipoLexama, List<String>> analisaLexica(DadosCompilacao dados);
	List<String> analisaSintatica(Map<TipoLexama, List<String>> tokens);
	void analisaSemantica(DadosCompilacao dados);

}
