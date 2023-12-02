package domain.util;

import java.util.InputMismatchException;
import java.util.Scanner;

import data.DadosCompilacao;
import domain.analise.AnaliseConteudoImpl;
import domain.analise.IAnaliseConteudo;

public class OperacoesMenuUtil {

	private static final IAnaliseConteudo analiseConteudo = new AnaliseConteudoImpl();
	private static DadosCompilacao dadosCompilacao;
	
	private OperacoesMenuUtil() {}
	
	public static String informarArquivoLeitura() {			
		String conteudoArq = LeitorArquivoUtil.lerArquivo("src/conteudo.txt");
		dadosCompilacao = new DadosCompilacao(conteudoArq);
		
		if(conteudoArq.isBlank()) {
			System.out.println("-------------------------------------------");
			System.out.println("  Nenhum conteúdo encontrado para análise  ");
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
		} while (opcao != 3);
		teclado.close();
	}

	private static void exibirMenuPrincipal() {
		System.out.println("-------------------Menu-------------------");
		System.out.println("1 - Análise Léxica ");
		System.out.println("2 - Análise Sintática ");
		System.out.println("3 - Análise Semântica ");
		System.out.println("4 - Análise Completa ");
		System.out.println("5 - Sair");
		System.out.println("------------------------------------------");
	}

	private static void analisaLexica() {
		analiseConteudo.analisaLexica(dadosCompilacao);
	}

	private static void analisaSintatica() {
		analiseConteudo.analisaSemantica(dadosCompilacao);
	}

	private static void analisaSemantica() {
		analiseConteudo.analisaSemantica(dadosCompilacao);
	}

	private static void analisaCompleta() {
		analiseConteudo.analisaCompleta(dadosCompilacao);
	}
	
}