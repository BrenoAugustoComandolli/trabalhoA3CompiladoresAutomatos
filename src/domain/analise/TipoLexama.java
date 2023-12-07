package domain.analise;

public enum TipoLexama {

	IF("if", "Palavra reservada (if)", true, false), 
	ID("[a-zA-Z]+[0-9]*", "Identificador", false, false), 
	NUM("\\d+", "Numero inteiro", false, false), 
	REAL("\\d+\\.\\d+", "Numero real", false, false), 
	WHITE_SPACE("[ \\t]+", "Espaco em branco", false, false),
	CHAVE("[{}]", "Chave", false, false),
	PARENTESES("[()]", "Parenteses", false, false),
	OPERADOR_RELACIONAL("<|>|<=|>=|==|!=", "Operador relacional", false, false),
	OPERADOR_MATEMATICO("[\\+\\-\\*/%]", "Operador matematico", false, false),
	RECEBIMENTO(":=", "Recebimento", true, false),
	STRING("\\bString\\b", "String", true, true),
	INTEIRO("\\bint\\b", "Inteiro", true, true);
	
	private String padrao;
	private String descricao;
	private boolean palavraReservada;
	private boolean tipagem;

	TipoLexama(String padrao, String descricao, boolean palavraReservada, boolean tipagem){
		this.padrao = padrao;
		this.descricao = descricao;
		this.palavraReservada = palavraReservada;
		this.tipagem = tipagem;
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

	public boolean isTipagem() {
		return tipagem;
	}
	
}