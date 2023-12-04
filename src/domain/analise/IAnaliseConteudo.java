package domain.analise;

import java.util.List;
import java.util.Map;

import data.DadosCompilacao;

public interface IAnaliseConteudo {

	Map<TipoLexama, List<String>> analisaLexica(DadosCompilacao dados);
	void analisaSintatica(DadosCompilacao dados);
	void analisaSemantica(DadosCompilacao dados);

}
