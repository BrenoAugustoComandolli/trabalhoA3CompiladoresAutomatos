package domain.util;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import data.DadosCompilacao;
import domain.analise.AnaliseConteudoImpl;
import domain.analise.IAnaliseConteudo;
import domain.analise.TipoLexama;

public class OperacoesMenuUtil {

	private static final IAnaliseConteudo analiseConteudo = new AnaliseConteudoImpl();
	private static DadosCompilacao dadosCompilacao;
	
	private OperacoesMenuUtil() {}
	
	public static String informarArquivoLeitura() {			
		String conteudoArq = LeitorArquivoUtil.lerArquivo("src/conteudo.txt");
		dadosCompilacao = new DadosCompilacao(conteudoArq);
		
		if(conteudoArq.isBlank()) {
			System.out.println("-------------------------------------------");
			System.out.println("  Nenhum conteudo encontrado para analise  ");
			System.out.println("-------------------------------------------");
		}
		return conteudoArq;
	}
	
	public static void carregarMenuOpcoes() {
		var teclado = new Scanner(System.in);
		int opcao;
		
		do {
			exibirMenuPrincipal();
			try {				
				opcao = teclado.nextInt();
			} catch (InputMismatchException e) {
				opcao = 0;
			}
			
			switch (opcao) {
			case 1: analisaLexica(); break;
			case 2: analisaSintatica(); break;
			case 3: analisaSemantica(); break;
			case 4: analisaCompleta(); break;
			case 5: break;
			default:
				System.out.println("Opcao digitada invalida!");
				break;
			}
		} while (opcao != 5);
		teclado.close();
	}

	private static void exibirMenuPrincipal() {
		System.out.println("-------------------Menu-------------------");
		System.out.println("1 - Analise Lexica ");
		System.out.println("2 - Analise Sintatica ");
		System.out.println("3 - Analise Semantica ");
		System.out.println("4 - Analise Completa ");
		System.out.println("5 - Sair");
		System.out.println("------------------------------------------");
	}

	private static void analisaLexica() {
		Map<TipoLexama, List<String>> tokens = analiseConteudo.analisaLexica(dadosCompilacao);

		if(!tokens.isEmpty()) {			
			System.out.println("------------------------------------------");
			System.out.println("Tabela lexica:");
			System.out.println("------------------------------------------");
			for (Entry<TipoLexama, List<String>> umToken : tokens.entrySet()) {
				umToken.getValue().stream().forEach(umCaso -> {				
					System.out.print("[ Token: ");				
					System.out.print(umCaso + " - ");
					System.out.print("Lexema: ");
					System.out.print(umToken.getKey().name() + " - ");
					System.out.print("Descricao: ");
					System.out.print(umToken.getKey().getDescricao());
					System.out.print(" ]");
					System.out.println("");
				});
			}
			System.out.println("------------------------------------------");
		}
	}

	private static void analisaSintatica() {
		Map<TipoLexama, List<String>> tokens = analiseConteudo.analisaLexica(dadosCompilacao);
		List<String> logs = analiseConteudo.analisaSintatica(tokens);
		System.out.println("------------------------------------------");
		System.out.println("Analise sintatica:");
		System.out.println("------------------------------------------");
		logs.forEach(System.out::println);
		System.out.println("------------------------------------------");
	}

	private static void analisaSemantica() {
		Map<TipoLexama, List<String>> tokens = analiseConteudo.analisaLexica(dadosCompilacao);
		List<String> logs = analiseConteudo.analisaSemantica(dadosCompilacao, tokens);
		System.out.println("------------------------------------------");
		System.out.println("Analise semantica:");
		System.out.println("------------------------------------------");
		logs.forEach(System.out::println);
		System.out.println("------------------------------------------");
	}

	private static void analisaCompleta() {
		analisaLexica();
		analisaSintatica();
		analisaSemantica();
	}
	
}