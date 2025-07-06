package Metodos;

import java.util.Objects;
import Interface.Estado;

public class EstadoImpl implements Estado {
    private final String nome;

    public EstadoImpl(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EstadoImpl estado = (EstadoImpl) obj;
        return Objects.equals(nome, estado.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return nome;
    }
}
