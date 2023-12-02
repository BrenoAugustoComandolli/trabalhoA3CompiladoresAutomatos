package domain.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivoUtil {

	private LeitorArquivoUtil() {}
	
	public static String lerArquivo(String caminho) {
		StringBuilder conteudoTexto = new StringBuilder();

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminho))) {
            String linha;

            while ((linha = leitor.readLine()) != null) {
            	conteudoTexto.append(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conteudoTexto.toString();
	}
	
}