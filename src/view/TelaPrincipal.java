package view;

import domain.util.OperacoesMenuUtil;

public class TelaPrincipal {
	
	public static void main(String[] args) {
		String conteudoArq = OperacoesMenuUtil.informarArquivoLeitura();
		
		if(!conteudoArq.isEmpty()) {
			OperacoesMenuUtil.carregarMenuOpcoes();
		}
	}
	
}