package Main;

import Metodos.*;
import Interface.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Main {
    private static AutomatoPilha automato;
    private static ParserShiftReduce parser;
    static String cadeia;
    private static JTextArea logArea;
    private static JTabbedPane tabbedPane;
    
    // Cores do tema
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color DARK_GRAY = new Color(108, 117, 125);

    public static void main(String[] args) {
  
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();	
        }
        
        SwingUtilities.invokeLater(() -> {
            criarInterfaceGrafica();
            inicializarAutomato();
            inicializarParser();
        });
    }

    private static void inicializarAutomato() {
        Set<Estado> estados = new HashSet<>(Arrays.asList(
            new EstadoImpl("q0")
        ));

        Set<Character> alfabetoEntrada = new HashSet<>(Arrays.asList('a', 'b', 'c', '(', ')'));
        Set<Character> alfabetoPilha = new HashSet<>(Arrays.asList('Z', 'A', 'B'));
        Estado estadoInicial = new EstadoImpl("q0");
        Set<Estado> estadosFinais = new HashSet<>();

        automato = new AutomatoPilhaImpl(estados, alfabetoEntrada, alfabetoPilha, estadoInicial, estadosFinais);
        configurarAutomato(automato);
    }

    private static void inicializarParser() {
        parser = new ParserShiftReduce();
        if (logArea != null) {
            logArea.append("=== PARSER SHIFT-REDUCE INICIALIZADO ===\n");
            logArea.append("Parser pronto para receber produções.\n\n");
        }
    }

    private static void configurarAutomato(AutomatoPilha automato) {
        if (logArea != null) {
            logArea.append("=== AUTÔMATO DE PILHA INICIALIZADO ===\n");
            logArea.append("Autômato pronto para receber transições.\n\n");
        }
    }

    private static void criarInterfaceGrafica() {
        JFrame frame = new JFrame("Analisador Sintático - Autômato de Pilha & Parser Shift-Reduce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLayout(new BorderLayout());
        
        frame.setLocationRelativeTo(null);

        JPanel ribbonPanel = criarRibbonPanel();
        frame.add(ribbonPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Aba 1: Gramática e Parser
        JPanel gramaticaPanel = criarAbaGramatica();
        tabbedPane.addTab("📝 Gramática & Parser", gramaticaPanel);
        
        // Aba 2: Autômato de Pilha
        JPanel automatoPanel = criarAbaAutomato();
        tabbedPane.addTab("🔄 Autômato de Pilha", automatoPanel);
        
        // Aba 3: Análise de Cadeias
        JPanel analisePanel = criarAbaAnalise();
        tabbedPane.addTab("🔍 Análise de Cadeias", analisePanel);
        
        // Aba 4: Log e Resultados
        JPanel logPanel = criarAbaLog();
        tabbedPane.addTab("📊 Log & Resultados", logPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);

        // === BARRA DE STATUS ===
        JPanel statusBar = criarBarraStatus();
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.setVisible(true);
        
        // Mensagem inicial
        SwingUtilities.invokeLater(() -> {
            logArea.append("=== SISTEMA INICIALIZADO ===\n");
            logArea.append("🎯 Bem-vindo ao Analisador Sintático!\n");
            logArea.append("📋 Use as abas para navegar entre as funcionalidades\n");
            logArea.append("🔧 Configure primeiro a gramática ou o autômato\n");
            logArea.append("🚀 Digite uma cadeia na aba 'Análise' para começar\n\n");
        });
    }

    private static JPanel criarRibbonPanel() {
        JPanel ribbonPanel = new JPanel(new BorderLayout());
        ribbonPanel.setBackground(LIGHT_GRAY);
        ribbonPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, PRIMARY_COLOR),
            new EmptyBorder(8, 10, 8, 10)
        ));

        // Título
        JLabel titleLabel = new JLabel("Analisador Sintático v2.0");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);

        // Grupo de ações rápidas
        JPanel quickActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        quickActionsPanel.setOpaque(false);

        JButton novoProjetoBtn = criarBotaoRibbon("🆕 Novo", "Limpar tudo e começar novo projeto");
        JButton salvarBtn = criarBotaoRibbon("💾 Salvar", "Salvar configurações atuais");
        JButton ajudaBtn = criarBotaoRibbon("❓ Ajuda", "Exibir ajuda sobre o sistema");

        quickActionsPanel.add(novoProjetoBtn);
//        quickActionsPanel.add(salvarBtn);
        quickActionsPanel.add(ajudaBtn);

        ribbonPanel.add(titleLabel, BorderLayout.WEST);
        ribbonPanel.add(quickActionsPanel, BorderLayout.EAST);

        // Event listeners para botões do ribbon
        novoProjetoBtn.addActionListener(e -> executarResetGeral());
        ajudaBtn.addActionListener(e -> mostrarAjuda());

        return ribbonPanel;
    }

    private static JButton criarBotaoRibbon(String texto, String tooltip) {
        JButton button = new JButton(texto);
        button.setToolTipText(tooltip);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(DARK_GRAY, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        button.setBackground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        });
        
        return button;
    }

    private static JPanel criarAbaGramatica() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Painel superior: Info sobre gramática
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(criarTitledBorder("ℹ️ Sobre Gramáticas Livres de Contexto"));
        
        JTextArea infoText = new JTextArea(
            "Uma gramática livre de contexto é definida por produções na forma A → α, onde:\n" +
            "• A é um símbolo não-terminal (variável)\n" +
            "• α é uma sequência de terminais e/ou não-terminais\n" +
            "• Use 'ε' para representar a cadeia vazia"
        );
        infoText.setEditable(false);
        infoText.setOpaque(false);
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(infoText, BorderLayout.CENTER);

        // Painel central: Entrada de produções
        JPanel entradaPanel = new JPanel(new GridBagLayout());
        entradaPanel.setBorder(criarTitledBorder("➕ Adicionar Produções"));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Lado esquerdo
        gbc.gridx = 0; gbc.gridy = 0;
        entradaPanel.add(new JLabel("Não-terminal:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField ladoEsquerdoField = new JTextField(5);
        ladoEsquerdoField.setFont(new Font("Courier New", Font.BOLD, 14));
        entradaPanel.add(ladoEsquerdoField, gbc);

        // Seta
        gbc.gridx = 2; gbc.gridy = 0;
        JLabel setaLabel = new JLabel("→");
        setaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        setaLabel.setForeground(PRIMARY_COLOR);
        entradaPanel.add(setaLabel, gbc);

        // Lado direito
        gbc.gridx = 3; gbc.gridy = 0;
        entradaPanel.add(new JLabel("Produção:"), gbc);
        
        gbc.gridx = 4; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField ladoDireitoField = new JTextField(15);
        ladoDireitoField.setFont(new Font("Courier New", Font.BOLD, 14));
        entradaPanel.add(ladoDireitoField, gbc);

        // Botões
        gbc.gridx = 5; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton adicionarProdButton = criarBotaoAcao("➕ Adicionar", SUCCESS_COLOR);
        entradaPanel.add(adicionarProdButton, gbc);

        // Painel inferior: Controles
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlesPanel.setBorder(criarTitledBorder("🛠️ Controles"));

        JButton mostrarProducoesBtn = criarBotaoAcao("📋 Mostrar Produções", PRIMARY_COLOR);
        JButton limparProducoesBtn = criarBotaoAcao("🗑️ Limpar Todas", DANGER_COLOR);
        JButton exemploBtn = criarBotaoAcao("💡 Carregar Exemplo", WARNING_COLOR);

        controlesPanel.add(mostrarProducoesBtn);
        controlesPanel.add(limparProducoesBtn);
//        controlesPanel.add(exemploBtn);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(entradaPanel, BorderLayout.CENTER);
        panel.add(controlesPanel, BorderLayout.SOUTH);

        // Event Listeners
        adicionarProdButton.addActionListener(e -> {
            String esquerdo = ladoEsquerdoField.getText().trim();
            String direito = ladoDireitoField.getText().trim();
            
            if (esquerdo.isEmpty() || direito.isEmpty()) {
                mostrarMensagem("Erro", "Campos não podem estar vazios!", DANGER_COLOR);
                return;
            }
            
            parser.adicionarProducao(direito, esquerdo);
            logArea.append("✅ Produção adicionada: " + esquerdo + " → " + direito + "\n\n");
            
            ladoEsquerdoField.setText("");
            ladoDireitoField.setText("");
            
      
            tabbedPane.setSelectedIndex(3);
        });

        mostrarProducoesBtn.addActionListener(e -> {
            logArea.append("=== PRODUÇÕES ATUAIS ===\n");
            logArea.append(parser.listarProducoes());
            logArea.append("\n");
            tabbedPane.setSelectedIndex(3);
        });

        limparProducoesBtn.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(panel,
                "Deseja remover todas as produções?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (resposta == JOptionPane.YES_OPTION) {
                parser.limparProducoes();
                logArea.append("🗑️ Todas as produções foram removidas.\n\n");
                tabbedPane.setSelectedIndex(3);
            }
        });

        exemploBtn.addActionListener(e -> {
            parser.limparProducoes();
            parser.adicionarProducao("E+T", "E");
            parser.adicionarProducao("T", "E");
            parser.adicionarProducao("T*F", "T");
            parser.adicionarProducao("F", "T");
            parser.adicionarProducao("(E)", "F");
            parser.adicionarProducao("id", "F");
            
            logArea.append("💡 Exemplo de gramática carregado:\n");
            logArea.append("E → E+T | T\n");
            logArea.append("T → T*F | F\n");
            logArea.append("F → (E) | id\n\n");
            tabbedPane.setSelectedIndex(3);
        });

        return panel;
    }

    private static JPanel criarAbaAutomato() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Painel superior: Info sobre autômatos
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(criarTitledBorder("ℹ️ Sobre Autômatos de Pilha"));
        
        JTextArea infoText = new JTextArea(
            "Um autômato de pilha é definido por transições na forma δ(q, a, X) = (p, α), onde:\n" +
            "• q é o estado atual, p é o próximo estado\n" +
            "• a é o símbolo lido da entrada (use 'ε' para transição vazia)\n" +
            "• X é o símbolo no topo da pilha, α são os símbolos que substituem X"
        );
        infoText.setEditable(false);
        infoText.setOpaque(false);
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(infoText, BorderLayout.CENTER);

        // Painel central: Configuração do autômato
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(criarTitledBorder("⚙️ Configuração do Autômato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Estados
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JPanel estadosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estadosPanel.add(new JLabel("Novo Estado:"));
        JTextField estadoField = new JTextField(8);
        estadosPanel.add(estadoField);
        JButton adicionarEstadoBtn = criarBotaoAcao("+ Estado", PRIMARY_COLOR);
        estadosPanel.add(adicionarEstadoBtn);
        estadosPanel.add(Box.createHorizontalStrut(20));
        estadosPanel.add(new JLabel("Estado Final:"));
        JTextField estadoFinalField = new JTextField(8);
        estadosPanel.add(estadoFinalField);
        JButton adicionarFinalBtn = criarBotaoAcao("+ Final", SUCCESS_COLOR);
        estadosPanel.add(adicionarFinalBtn);
        configPanel.add(estadosPanel, gbc);

        // Separador
        gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        configPanel.add(new JSeparator(), gbc);

        // Transições
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // Linha 1 de transição
        gbc.gridx = 0;
        configPanel.add(new JLabel("Estado Origem:"), gbc);
        gbc.gridx = 1;
        JTextField origemField = new JTextField(6);
        configPanel.add(origemField, gbc);
        
        gbc.gridx = 2;
        configPanel.add(new JLabel("Entrada:"), gbc);
        gbc.gridx = 3;
        JTextField entradaField = new JTextField(4);
        configPanel.add(entradaField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        configPanel.add(new JLabel("Topo Pilha:"), gbc);
        gbc.gridx = 1;
        JTextField pilhaField = new JTextField(6);
        configPanel.add(pilhaField, gbc);
        
        gbc.gridx = 2;
        configPanel.add(new JLabel("Estado Destino:"), gbc);
        gbc.gridx = 3;
        JTextField destinoField = new JTextField(6);
        configPanel.add(destinoField, gbc);

        // Linha 3 de transição
        gbc.gridy = 4;
        gbc.gridx = 0;
        configPanel.add(new JLabel("Novos Símbolos:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        JTextField novosSimbolosField = new JTextField(10);
        configPanel.add(novosSimbolosField, gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 1;
        JButton adicionarTransicaoBtn = criarBotaoAcao("+ Transição", SUCCESS_COLOR);
        configPanel.add(adicionarTransicaoBtn, gbc);

        // Painel inferior: Controles
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlesPanel.setBorder(criarTitledBorder("🛠️ Controles do Autômato"));

        JButton resetTransicoesBtn = criarBotaoAcao("🗑️ Reset Transições", WARNING_COLOR);
        JButton resetAutomatoBtn = criarBotaoAcao("🔄 Reset Autômato", DANGER_COLOR);
        JButton exemploAutomatoBtn = criarBotaoAcao("💡 Exemplo", PRIMARY_COLOR);

        controlesPanel.add(resetTransicoesBtn);
        controlesPanel.add(resetAutomatoBtn);
//        controlesPanel.add(exemploAutomatoBtn);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(configPanel, BorderLayout.CENTER);
        panel.add(controlesPanel, BorderLayout.SOUTH);

        // Event Listeners
        adicionarEstadoBtn.addActionListener(e -> {
            String nome = estadoField.getText().trim();
            if (nome.isEmpty()) {
                mostrarMensagem("Erro", "Nome do estado não pode estar vazio!", DANGER_COLOR);
                return;
            }
            
            Estado novoEstado = new EstadoImpl(nome);
            automato.adicionarEstado(novoEstado);
            logArea.append("✅ Estado adicionado: " + nome + "\n\n");
            estadoField.setText("");
            tabbedPane.setSelectedIndex(3);
        });

        adicionarFinalBtn.addActionListener(e -> {
            String nome = estadoFinalField.getText().trim();
            if (nome.isEmpty()) {
                mostrarMensagem("Erro", "Nome do estado final não pode estar vazio!", DANGER_COLOR);
                return;
            }
            
            Estado estadoFinal = new EstadoImpl(nome);
            automato.adicionarEstadoFinal(estadoFinal);
            logArea.append("✅ Estado final adicionado: " + nome + "\n\n");
            estadoFinalField.setText("");
            tabbedPane.setSelectedIndex(3);
        });

        adicionarTransicaoBtn.addActionListener(e -> {
            try {
                String origem = origemField.getText().trim();
                String entrada = entradaField.getText().trim();
                String pilha = pilhaField.getText().trim();
                String destino = destinoField.getText().trim();
                String novosSimbolos = novosSimbolosField.getText().trim();

                if (origem.isEmpty() || pilha.isEmpty() || destino.isEmpty()) {
                    mostrarMensagem("Erro", "Campos obrigatórios não preenchidos!", DANGER_COLOR);
                    return;
                }

                Estado estadoOrigem = new EstadoImpl(origem);
                char simboloEntrada = entrada.isEmpty() ? 'ε' : entrada.charAt(0);
                char simboloPilha = pilha.charAt(0);
                Estado estadoDestino = new EstadoImpl(destino);
                
                char[] arrayNovosSimbolos;
                if (novosSimbolos.isEmpty() || novosSimbolos.equals("ε")) {
                    arrayNovosSimbolos = new char[0];
                } else {
                    arrayNovosSimbolos = novosSimbolos.toCharArray();
                }

                Transicao transicao = new TransicaoImpl(estadoOrigem, simboloEntrada, simboloPilha, 
                                                       estadoDestino, arrayNovosSimbolos);
                automato.adicionarTransicao(transicao);

                logArea.append("✅ Transição adicionada: (" + origem + ", " + 
                               (simboloEntrada == 'ε' ? "ε" : simboloEntrada) + ", " + simboloPilha + 
                               ") → (" + destino + ", " + 
                               (arrayNovosSimbolos.length == 0 ? "ε" : new String(arrayNovosSimbolos)) + ")\n\n");

                // Limpar campos
                origemField.setText("");
                entradaField.setText("");
                pilhaField.setText("");
                destinoField.setText("");
                novosSimbolosField.setText("");
                
                tabbedPane.setSelectedIndex(3);
                
            } catch (Exception ex) {
                logArea.append("ERRO ao adicionar transição: " + ex.getMessage() + "\n\n");
                tabbedPane.setSelectedIndex(3);
            }
        });

        return panel;
    }

    private static JPanel criarAbaAnalise() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Painel superior: Entrada da cadeia
        JPanel entradaPanel = new JPanel(new GridBagLayout());
        entradaPanel.setBorder(criarTitledBorder("🔍 Entrada da Cadeia"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel cadeiaLabel = new JLabel("Cadeia a analisar:");
        cadeiaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        entradaPanel.add(cadeiaLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField cadeiaField = new JTextField(25);
        cadeiaField.setFont(new Font("Courier New", Font.BOLD, 16));
        cadeiaField.setBorder(new CompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2),
            new EmptyBorder(8, 12, 8, 12)
        ));
        entradaPanel.add(cadeiaField, gbc);

        // Painel central: Botões de análise
        JPanel botoesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        botoesPanel.setBorder(criarTitledBorder("⚡ Métodos de Análise"));

        // Botão Autômato
        JPanel automatoBtnPanel = new JPanel(new BorderLayout());
        JButton botaoAutomato = new JButton("<html><center>🔄<br><b>AUTÔMATO DE PILHA</b><br><small>Reconhecimento por transições</small></center></html>");
        botaoAutomato.setPreferredSize(new Dimension(200, 80));
        botaoAutomato.setBackground(PRIMARY_COLOR);
        botaoAutomato.setForeground(Color.WHITE);
        botaoAutomato.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botaoAutomato.setFocusPainted(false);
        botaoAutomato.setCursor(new Cursor(Cursor.HAND_CURSOR));
        automatoBtnPanel.add(botaoAutomato, BorderLayout.CENTER);

        // Botão Parser
        JPanel parserBtnPanel = new JPanel(new BorderLayout());
        JButton botaoParser = new JButton("<html><center>📝<br><b>PARSER SHIFT-REDUCE</b><br><small>Análise por reduções</small></center></html>");
        botaoParser.setPreferredSize(new Dimension(200, 80));
        botaoParser.setBackground(SUCCESS_COLOR);
        botaoParser.setForeground(Color.WHITE);
        botaoParser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botaoParser.setFocusPainted(false);
        botaoParser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parserBtnPanel.add(botaoParser, BorderLayout.CENTER);

        botoesPanel.add(automatoBtnPanel);
        botoesPanel.add(parserBtnPanel);

        // Painel inferior: Exemplos
        JPanel exemplosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        exemplosPanel.setBorder(criarTitledBorder("💡 Exemplos de Cadeias"));

        String[] exemplos = {"aaabbb", "((()))", "acb"};
        for (String exemplo : exemplos) {
            JButton exemploBtn = new JButton(exemplo);
            exemploBtn.setFont(new Font("Courier New", Font.BOLD, 11));
            exemploBtn.addActionListener(e -> cadeiaField.setText(exemplo));
            exemploBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            exemplosPanel.add(exemploBtn);
        }

        panel.add(entradaPanel, BorderLayout.NORTH);
        panel.add(botoesPanel, BorderLayout.CENTER);  
        panel.add(exemplosPanel, BorderLayout.SOUTH);

        // Event Listeners
        botaoAutomato.addActionListener(e -> {
            cadeia = cadeiaField.getText().trim();
            if (cadeia.isEmpty()) {
                mostrarMensagem("Erro", "Cadeia não pode estar vazia!", DANGER_COLOR);
                return;
            }
            
            logArea.append("=== PROCESSAMENTO COM AUTÔMATO DE PILHA ===\n");
            logArea.append("🔄 Analisando cadeia: \"" + cadeia + "\"\n");
            StringBuilder log = new StringBuilder();
            boolean aceita = automato.processarCadeia(cadeia, log);
            logArea.append(log.toString());
            logArea.append("Resultado: " + (aceita ? "✅ ACEITA" : "❌ REJEITADA") + "\n\n");
            tabbedPane.setSelectedIndex(3);
        });

        botaoParser.addActionListener(e -> {
            cadeia = cadeiaField.getText().trim();
            if (cadeia.isEmpty()) {
                mostrarMensagem("Erro", "Cadeia não pode estar vazia!", DANGER_COLOR);
                return;
            }
            
            logArea.append("=== PROCESSAMENTO COM PARSER SHIFT-REDUCE ===\n");
            logArea.append("📝 Analisando cadeia: \"" + cadeia + "\"\n");
            StringBuilder log = new StringBuilder();
            boolean aceita = parser.processarCadeiaComReducao(cadeia, log);
            logArea.append(log.toString());
            tabbedPane.setSelectedIndex(3);
        });

        return panel;
    }

    private static JPanel criarAbaLog() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Painel superior: Controles do log
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlesPanel.setBorder(criarTitledBorder("🛠️ Controles do Log"));

        JButton limparLogBtn = criarBotaoAcao("🗑️ Limpar Log", WARNING_COLOR);
        JButton salvarLogBtn = criarBotaoAcao("💾 Salvar Log", PRIMARY_COLOR);
        JButton exportarBtn = criarBotaoAcao("📤 Exportar", SECONDARY_COLOR);

        controlesPanel.add(limparLogBtn);
//        controlesPanel.add(salvarLogBtn);
//        controlesPanel.add(exportarBtn);

        // Área de log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        logArea.setBackground(new Color(43, 43, 43));
        logArea.setForeground(new Color(248, 248, 242));
        logArea.setCaretColor(Color.WHITE);
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(criarTitledBorder("📊 Log de Execução"));
        scrollPane.setPreferredSize(new Dimension(0, 400));

        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
//        statsPanel.setBorder(criarTitledBorder("📈 Estatísticas"));

        JLabel estadosLabel = new JLabel("Estados: 0", JLabel.CENTER);
        JLabel transicoesLabel = new JLabel("Transições: 0", JLabel.CENTER);
        JLabel producoesLabel = new JLabel("Produções: 0", JLabel.CENTER);
        JLabel analiseLabel = new JLabel("Análises: 0", JLabel.CENTER);

//        statsPanel.add(estadosLabel);
//        statsPanel.add(transicoesLabel);
//        statsPanel.add(producoesLabel);
//        statsPanel.add(analiseLabel);

        panel.add(controlesPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);

        // Event Listeners
        limparLogBtn.addActionListener(e -> {
            logArea.setText("");
            logArea.append("=== LOG LIMPO ===\n\n");
        });

        return panel;
    }

    private static JPanel criarBarraStatus() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, DARK_GRAY),
            new EmptyBorder(5, 10, 5, 10)
        ));
        statusBar.setBackground(LIGHT_GRAY);

        JLabel statusLabel = new JLabel("🟢 Sistema Pronto");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel versionLabel = new JLabel("v2.0 - Analisador Sintático");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(DARK_GRAY);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);

        return statusBar;
    }

    private static Border criarTitledBorder(String titulo) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            titulo,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        );
    }

    private static JButton criarBotaoAcao(String texto, Color cor) {
        JButton button = new JButton(texto);
        button.setBackground(cor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = cor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(cor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }

    private static void mostrarMensagem(String titulo, String mensagem, Color cor) {
        JOptionPane optionPane = new JOptionPane(mensagem, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(titulo);
        dialog.setVisible(true);
    }

    private static void executarResetGeral() {
        int resposta = JOptionPane.showConfirmDialog(null,
            "Deseja resetar completamente o sistema?\n(Autômato + Parser + Log)",
            "Confirmar Reset Geral",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (resposta == JOptionPane.YES_OPTION) {
            inicializarAutomato();
            inicializarParser();
            logArea.setText("");
            logArea.append("🔄 RESET GERAL CONCLUÍDO\n");
            logArea.append("Sistema reinicializado completamente.\n\n");
            tabbedPane.setSelectedIndex(0);
        }
    }

    private static void mostrarAjuda() {
        String ajuda = """
            📚 AJUDA - ANALISADOR SINTÁTICO
            
            🔹 ABA GRAMÁTICA & PARSER:
            • Defina produções da gramática livre de contexto
            • Formato: A → α (onde A é não-terminal, α é produção)
            • Use 'ε' para representar cadeia vazia
            
            🔹 ABA AUTÔMATO DE PILHA:
            • Configure estados e transições do autômato
            • Formato: δ(q,a,X) = (p,α)
            • Use 'ε' para transições vazias
            
            🔹 ABA ANÁLISE:
            • Digite a cadeia a ser analisada
            • Escolha entre Autômato de Pilha ou Parser Shift-Reduce
            • Visualize o resultado na aba Log
            
            🔹 ABA LOG:
            • Acompanhe todas as operações do sistema
            • Salve ou exporte os resultados
            • Visualize estatísticas de uso
            
            💡 DICAS:
            • Configure primeiro a gramática ou autômato
            • Use os exemplos fornecidos para testar
            • Acompanhe o log para debug
            
            
        		DESENVOLVIDO POR: PEDRO REZENDE, MAGDIEL PRESTES E VICTOR 	
            """;
        
        JTextArea textArea = new JTextArea(ajuda);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null, scrollPane, "Ajuda do Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
}