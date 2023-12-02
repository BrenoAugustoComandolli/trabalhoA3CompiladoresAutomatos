package domain.analise;

import data.DadosCompilacao;

public interface IAnaliseConteudo {

	void analisaLexica(DadosCompilacao dados);
	void analisaSintatica(DadosCompilacao dados);
	void analisaSemantica(DadosCompilacao dados);

	default void analisaCompleta(DadosCompilacao dados) {
		analisaLexica(dados);
		analisaSintatica(dados);
		analisaSemantica(dados);
	}

}
