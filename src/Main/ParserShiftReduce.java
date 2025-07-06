package Main;


import java.util.*;

public class ParserShiftReduce {
    private Map<String, String> producoes; // lado direito → lado esquerdo
    private Stack<String> pilha;
    private Queue<Character> entrada;
    private StringBuilder log;
    
    public ParserShiftReduce() {
        this.producoes = new HashMap<>();
        this.pilha = new Stack<>();
        this.log = new StringBuilder();
        inicializarProducoesDefault();
    }
    	
    private void inicializarProducoesDefault() {
    
        
        // Para parênteses: S → (S) | SS | ε
         producoes.put("(S)", "S");
         producoes.put("()", "S");
         producoes.put("SS", "S");
        
        // Para a^n b^n: S → aSb | ab
         producoes.put("ab", "S");
         producoes.put("aSb", "S");
         
         producoes.put("c", "S");
         producoes.put("aSb", "S");
         
    }
    
    public boolean analisarCadeia(String cadeia) {
        // Inicializar
        entrada = new LinkedList<>();
        pilha.clear();
        log.setLength(0);
        
        // Converter string para queue
        for (char c : cadeia.toCharArray()) {
            entrada.offer(c);
        }
        
        log.append("=== ANÁLISE SHIFT-REDUCE ===\n");
        log.append("Cadeia: ").append(cadeia).append("\n");
        log.append("Produções disponíveis:\n");
        for (Map.Entry<String, String> p : producoes.entrySet()) {
            log.append("  ").append(p.getValue()).append(" → ").append(p.getKey()).append("\n");
        }
        log.append("\n");
        
        int passo = 1;
        
        // Loop principal
        while (!entrada.isEmpty() || podeReduzir()) {
            
            log.append("Passo ").append(passo++).append(":\n");
            log.append("  Pilha: ").append(pilha.isEmpty() ? "[]" : pilha.toString()).append("\n");
            log.append("  Entrada: ").append(entradaToString()).append("\n");
            
            // Tentar REDUZIR primeiro (prioridade)
            if (tentarReduzir()) {
                log.append("  Ação: REDUCE\n\n");
                continue;
            }
            
            // Se não conseguiu reduzir, fazer SHIFT
            if (!entrada.isEmpty()) {
                char simbolo = entrada.poll();
                pilha.push(String.valueOf(simbolo));
                log.append("  Ação: SHIFT (").append(simbolo).append(")\n\n");
            } else {
                // Travou - não tem mais entrada nem reduções
                break;
            }
        }
        
        // Verificar resultado
        boolean aceita = pilha.size() == 1 && pilha.peek().equals("S") && entrada.isEmpty();
        
        log.append("=== RESULTADO ===\n");
        if (aceita) {
            log.append("✅ CADEIA ACEITA!\n");
            log.append("Derivação encontrada: ").append(construirDerivacao()).append("\n");
        } else {
            log.append("❌ CADEIA REJEITADA!\n");
            log.append("Estado final - Pilha: ").append(pilha).append(", Entrada: ").append(entradaToString()).append("\n");
        }
        
        return aceita;
    }
    
    private boolean tentarReduzir() {
        // Tentar reduções de tamanhos diferentes (maior primeiro)
        for (int tamanho = Math.min(pilha.size(), 4); tamanho >= 1; tamanho--) {
            
            if (pilha.size() < tamanho) continue;
            
            // Construir string dos últimos elementos
            StringBuilder sequencia = new StringBuilder();
            for (int i = pilha.size() - tamanho; i < pilha.size(); i++) {
                sequencia.append(pilha.get(i));
            }
            
            String chave = sequencia.toString();
            
            // Verificar se existe produção
            if (producoes.containsKey(chave)) {
                // Remover elementos da pilha
                for (int i = 0; i < tamanho; i++) {
                    pilha.pop();
                }
                
                // Adicionar não-terminal
                String naoTerminal = producoes.get(chave);
                pilha.push(naoTerminal);
                
                log.append("  Aplicando: ").append(naoTerminal).append(" → ").append(chave).append("\n");
                return true;
            }
        }
        return false;
    }
    
    private boolean podeReduzir() {
        for (int tamanho = Math.min(pilha.size(), 4); tamanho >= 1; tamanho--) {
            if (pilha.size() < tamanho) continue;
            
            StringBuilder sequencia = new StringBuilder();
            for (int i = pilha.size() - tamanho; i < pilha.size(); i++) {
                sequencia.append(pilha.get(i));
            }
            
            if (producoes.containsKey(sequencia.toString())) {
                return true;
            }
        }
        return false;
    }
    
    private String entradaToString() {
        if (entrada.isEmpty()) return "[]";
        return entrada.toString();
    }
    
    private String construirDerivacao() {
        // Aqui você pode implementar a construção da árvore de derivação
        // Por simplicidade, apenas indicamos que chegou em S
        return "Cadeia → ... → S";
    }
    
    // Métodos para integrar com sua interface
    public void adicionarProducao(String ladoDireito, String ladoEsquerdo) {
        producoes.put(ladoDireito, ladoEsquerdo);
        log.append("Produção adicionada: ").append(ladoEsquerdo).append(" → ").append(ladoDireito).append("\n");
    }
    
    public void limparProducoes() {
        producoes.clear();
    }
    
    public String getLog() {
        return log.toString();
    }
    
    public String listarProducoes() {
        StringBuilder sb = new StringBuilder("Produções atuais:\n");
        for (Map.Entry<String, String> entry : producoes.entrySet()) {
            sb.append("  ").append(entry.getValue()).append(" → ").append(entry.getKey()).append("\n");
        }
        return sb.toString();
    }
    
    // Método para ser chamado pela sua interface principal
    public boolean processarCadeiaComReducao(String cadeia, StringBuilder logExterno) {
        boolean resultado = analisarCadeia(cadeia);
        logExterno.append(getLog());
        return resultado;
    }
}