package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SistemaBibliotecaGUI extends JFrame {

    // Instância da "Fachada" do sistema
    private Biblioteca biblioteca;

    // Componentes de navegação
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SistemaBibliotecaGUI() {
        // Inicializa a regra de negócio
        this.biblioteca = new Biblioteca();
        
        // Configuração da Janela
        setTitle("Sistema de Biblioteca - Trabalho Final POO");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menu Lateral
        add(createMenuPanel(), BorderLayout.WEST);

        // Painel Central (CardLayout)
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Adicionando as telas
        mainPanel.add(createPanelCadastroLivro(), "Livro");
        mainPanel.add(createPanelCadastroUsuario(), "Usuario");
        mainPanel.add(createPanelEmprestimo(), "Emprestimo");
        mainPanel.add(createPanelDevolucao(), "Devolucao");
        mainPanel.add(createPanelListagem(), "Listagem");

        add(mainPanel, BorderLayout.CENTER);
    }

    // --- MENU LATERAL ---
    private JPanel createMenuPanel() {
        JPanel menu = new JPanel(new GridLayout(6, 1, 10, 10));
        menu.setBackground(new Color(60, 63, 65)); // Cor escura estilo IDE
        menu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnLivro = createMenuButton("Cadastrar Livro");
        JButton btnUsuario = createMenuButton("Cadastrar Usuário");
        JButton btnEmprestimo = createMenuButton("Empréstimo");
        JButton btnDevolucao = createMenuButton("Devolução");
        JButton btnListagem = createMenuButton("Listagem");
        JButton btnSair = createMenuButton("Sair");

        // Ações de Navegação
        btnLivro.addActionListener(e -> cardLayout.show(mainPanel, "Livro"));
        btnUsuario.addActionListener(e -> cardLayout.show(mainPanel, "Usuario"));
        btnEmprestimo.addActionListener(e -> cardLayout.show(mainPanel, "Emprestimo"));
        btnDevolucao.addActionListener(e -> cardLayout.show(mainPanel, "Devolucao"));
        btnListagem.addActionListener(e -> cardLayout.show(mainPanel, "Listagem"));
        btnSair.addActionListener(e -> System.exit(0));

        menu.add(btnLivro);
        menu.add(btnUsuario);
        menu.add(btnEmprestimo);
        menu.add(btnDevolucao);
        menu.add(btnListagem);
        menu.add(btnSair);

        return menu;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        return btn;
    }

    // --- TELA 1: CADASTRO DE LIVRO ---
    private JPanel createPanelCadastroLivro() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Novo Livro"));

        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtGenero = new JTextField();
        JTextField txtAno = new JTextField();
        JButton btnSalvar = new JButton("Salvar Livro");

        panel.add(new JLabel("Título:")); panel.add(txtTitulo);
        panel.add(new JLabel("Autor:")); panel.add(txtAutor);
        panel.add(new JLabel("Gênero:")); panel.add(txtGenero);
        panel.add(new JLabel("Ano:")); panel.add(txtAno);
        panel.add(new JLabel("")); panel.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText();
                String autor = txtAutor.getText();
                String genero = txtGenero.getText();
                // O método da sua classe pede 'short'
                short ano = Short.parseShort(txtAno.getText());

                if(titulo.isEmpty() || autor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha título e autor!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Integração com Biblioteca.java
                biblioteca.adicionarLivro(titulo, autor, genero, ano);

                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
                // Limpar campos
                txtTitulo.setText(""); txtAutor.setText(""); 
                txtGenero.setText(""); txtAno.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return wrapInPanel(panel);
    }

    // --- TELA 2: CADASTRO DE USUÁRIO ---
    private JPanel createPanelCadastroUsuario() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Novo Usuário"));

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JButton btnSalvar = new JButton("Salvar Usuário");

        panel.add(new JLabel("Nome Completo:")); panel.add(txtNome);
        panel.add(new JLabel("E-mail:")); panel.add(txtEmail);
        panel.add(new JLabel("")); panel.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtEmail.getText();

            if(nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Integração: Gera ID e salva
            int id = biblioteca.gerarIdUsuario();
            Usuario u = new Usuario(id, nome, email);
            biblioteca.cadastrarUsuario(u);

            JOptionPane.showMessageDialog(this, "Usuário cadastrado! ID gerado: " + id);
            txtNome.setText(""); txtEmail.setText("");
        });

        return wrapInPanel(panel);
    }

    // --- TELA 3: EMPRÉSTIMO ---
    private JPanel createPanelEmprestimo() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Empréstimo"));

        JTextField txtIdUsuario = new JTextField();
        JTextField txtIdLivro = new JTextField();
        JButton btnConfirmar = new JButton("Confirmar Empréstimo");

        panel.add(new JLabel("ID do Usuário:")); panel.add(txtIdUsuario);
        panel.add(new JLabel("ID do Livro:")); panel.add(txtIdLivro);
        panel.add(new JLabel("")); panel.add(btnConfirmar);

        btnConfirmar.addActionListener(e -> {
            try {
                int idUser = Integer.parseInt(txtIdUsuario.getText());
                int idLivro = Integer.parseInt(txtIdLivro.getText());

                // 1. Busca o objeto Usuário
                Usuario u = biblioteca.buscarUsuario(idUser);
                if (u == null) {
                    JOptionPane.showMessageDialog(this, "Usuário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Tenta emprestar
                // O método emprestarLivro já verifica disponibilidade e retorna boolean
                boolean sucesso = biblioteca.emprestarLivro(u, idLivro);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Falha: Livro não encontrado ou indisponível.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return wrapInPanel(panel);
    }

    // --- TELA 4: DEVOLUÇÃO ---
    private JPanel createPanelDevolucao() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Devolução"));

        JTextField txtIdUsuario = new JTextField(); // Necessário para buscar o usuário
        JTextField txtIdLivro = new JTextField();
        JButton btnDevolver = new JButton("Confirmar Devolução");

        panel.add(new JLabel("ID do Usuário:")); panel.add(txtIdUsuario);
        panel.add(new JLabel("ID do Livro:")); panel.add(txtIdLivro);
        panel.add(new JLabel("")); panel.add(btnDevolver);

        btnDevolver.addActionListener(e -> {
            try {
                int idUser = Integer.parseInt(txtIdUsuario.getText());
                int idLivro = Integer.parseInt(txtIdLivro.getText());

                Usuario u = biblioteca.buscarUsuario(idUser);
                if (u == null) {
                    JOptionPane.showMessageDialog(this, "Usuário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean sucesso = biblioteca.devolverLivro(u, idLivro);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Livro devolvido com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Falha na devolução. Verifique os dados.", "Erro", JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return wrapInPanel(panel);
    }

    // --- TELA 5: LISTAGEM ---
    private JPanel createPanelListagem() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Acervo da Biblioteca"));

        // Modelo da tabela
        String[] colunas = {"ID", "Título", "Autor", "Gênero", "Ano", "Disponível"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desabilita edição direta na tabela
            }
        };
        
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 14));

        // Ação para carregar dados
        btnAtualizar.addActionListener(e -> {
            model.setRowCount(0); // Limpa tabela
            
            // Requer o método getLivros() na classe Biblioteca
            ArrayList<Livro> lista = biblioteca.getLivros(); 

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum livro cadastrado.");
                return;
            }

            for (Livro l : lista) {
                model.addRow(new Object[]{
                    l.getId(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getGenero(),
                    l.getAno(),
                    l.getDisponibilidade() ? "Sim" : "Não"
                });
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnAtualizar, BorderLayout.SOUTH);

        return panel;
    }

    // Método auxiliar para centralizar painéis
    private JPanel wrapInPanel(JPanel form) {
        JPanel wrapper = new JPanel(new FlowLayout());
        wrapper.add(form);
        return wrapper;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tenta usar o estilo visual do sistema operacional
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            
            new SistemaBibliotecaGUI().setVisible(true);
        });
    }
}
