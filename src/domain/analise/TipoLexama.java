package domain.analise;

public enum TipoLexama {

	IF("if", "Palavra reservada (if)", true), 
	ID("[a-zA-Z]+", "Identificador", false), 
	NUM("\\d+", "Numero inteiro", false), 
	REAL("\\d+\\.\\d+", "Numero real", false), 
	WHITE_SPACE("[ \\t]+", "Espaco em branco", false),
	CHAVE("[{}]", "Chave", false),
	PARENTESES("[()]", "Parenteses", false);
	
	private String padrao;
	private String descricao;
	private boolean palavraReservada;

	TipoLexama(String padrao, String descricao, boolean palavraReservada){
		this.padrao = padrao;
		this.descricao = descricao;
		this.palavraReservada = palavraReservada;
	}

	public String getPadrao() {
		return padrao;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean isPalavraReservada() {
		return palavraReservada;
	}

}