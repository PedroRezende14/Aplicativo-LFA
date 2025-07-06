package Metodos;

import Interface.*;

public class TransicaoImpl implements Transicao {
    private Estado estadoOrigem;
    private char simboloEntrada;
    private char simboloPilha;
    private Estado estadoDestino;
    private char[] simbolosPilha;

    public TransicaoImpl(Estado estadoOrigem, char simboloEntrada, char simboloPilha, Estado estadoDestino, char[] simbolosPilha) {
        this.estadoOrigem = estadoOrigem;
        this.simboloEntrada = simboloEntrada;
        this.simboloPilha = simboloPilha;
        this.estadoDestino = estadoDestino;
        this.simbolosPilha = simbolosPilha;
    }

    @Override
    public Estado getEstadoOrigem() {
        return estadoOrigem;
    }

    @Override
    public char getSimboloEntrada() {
        return simboloEntrada;
    }

    @Override
    public char getSimboloPilha() {
        return simboloPilha;
    }

    @Override
    public Estado getEstadoDestino() {
        return estadoDestino;
    }

    @Override
    public char[] getSimbolosPilha() {
        return simbolosPilha;
    }
}