package Interface;

public interface AutomatoPilha {
    boolean processarCadeia(String cadeia, StringBuilder logger);
    void adicionarTransicao(Transicao transicao);
    void adicionarEstado(Estado estado);
    void adicionarEstadoFinal(Estado estado);
	void limparTransicoes();
}
