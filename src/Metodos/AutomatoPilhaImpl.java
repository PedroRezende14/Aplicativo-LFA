package Metodos;

import Interface.Estado;
import Interface.Transicao;
import Interface.AutomatoPilha;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class AutomatoPilhaImpl implements AutomatoPilha {
    private Set<Estado> estados;
    private Set<Character> alfabetoEntrada;
    private Set<Character> alfabetoPilha;
    private Estado estadoInicial;
    private Set<Estado> estadosFinais;
    private Set<Transicao> transicoes;

    public AutomatoPilhaImpl(Set<Estado> estados, Set<Character> alfabetoEntrada, Set<Character> alfabetoPilha, Estado estadoInicial, Set<Estado> estadosFinais) {
        this.estados = estados;
        this.alfabetoEntrada = alfabetoEntrada;
        this.alfabetoPilha = alfabetoPilha;
        this.estadoInicial = estadoInicial;
        this.estadosFinais = estadosFinais;
        this.transicoes = new HashSet<>();
    }

    @Override
    public void adicionarTransicao(Transicao transicao) {
        transicoes.add(transicao);
    }

    @Override
    public void adicionarEstado(Estado estado) {
        estados.add(estado);
    }

    @Override
    public void adicionarEstadoFinal(Estado estado) {
        estadosFinais.add(estado);
    }

    @Override
    public boolean processarCadeia(String cadeia, StringBuilder logger) {
        Stack<Character> pilha = new Stack<>();
        pilha.push('Z');
        Estado estadoAtual = estadoInicial;
        int pos = 0;

        logger.append(gerarRepresentacao(estadoAtual, pilha));

        while (pos <= cadeia.length()) {
            char simboloEntrada = pos < cadeia.length() ? cadeia.charAt(pos) : 'ε';
            boolean transicaoEncontrada = false;

            for (Transicao transicao : transicoes) {
                if (transicao.getEstadoOrigem().equals(estadoAtual) &&
                    (transicao.getSimboloEntrada() == simboloEntrada || transicao.getSimboloEntrada() == 'ε') &&
                    (pilha.isEmpty() || transicao.getSimboloPilha() == pilha.peek())) {
                    
                    estadoAtual = transicao.getEstadoDestino();
                    if (!pilha.isEmpty()) {
                        pilha.pop();
                    }
                    if (transicao.getSimbolosPilha().length > 0) {
                        for (int i = transicao.getSimbolosPilha().length - 1; i >= 0; i--) {
                            pilha.push(transicao.getSimbolosPilha()[i]);
                        }
                    }

                    logger.append(gerarRepresentacao(estadoAtual, pilha) + " - Transição: (" +
                            transicao.getEstadoOrigem().getNome() + ", " + simboloEntrada + ", " +
                            transicao.getSimboloPilha() + ") -> (" + estadoAtual.getNome() + ")\n");
                    
                    transicaoEncontrada = true;
                    if (transicao.getSimboloEntrada() != 'ε') pos++;
                    break;
                }
            }

            if (!transicaoEncontrada) {
                break;  
            }
        }

        boolean aceita = pos == cadeia.length() && estadosFinais.contains(estadoAtual) && (pilha.isEmpty() || pilha.peek() == 'Z');
        if (aceita) {
            logger.append(gerarRepresentacao(estadoAtual, pilha) + " - Cadeia foi aceita\n");
        } else {
            logger.append(gerarRepresentacao(estadoAtual, pilha) + " - Cadeia não foi aceita\n");
        }
        return aceita;
    }

    private String gerarRepresentacao(Estado estadoAtual, Stack<Character> pilha) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+------------------------+\n");
        sb.append("| Estado Atual: ").append(estadoAtual.getNome()).append(" |\n");
        sb.append("+------------------------+\n");
        sb.append("| Pilha:                 |\n");
        sb.append("+------------------------+\n");
        
        Stack<Character> pilhaInvertida = new Stack<>();
        pilhaInvertida.addAll(pilha);
        while (!pilhaInvertida.isEmpty()) {
            sb.append("| ").append(pilhaInvertida.pop()).append("                      |\n");
        }

        sb.append("+------------------------+\n");
        return sb.toString();
    }
    public void limparTransicoes() {
        this.transicoes.clear(); // ou similar, dependendo de como você armazena as transições
    }
}
