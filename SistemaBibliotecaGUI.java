package model;

import model.Excecoes.EmprestimoInvalido;
//import model.Excecoes.LivroIndisponivel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

public class SistemaBibliotecaGUI extends JFrame {

    // Instância da "Fachada" do sistema
    private final Biblioteca biblioteca;

    // Componentes de navegação
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

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
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Novo Usuário"));

        // 1. Seletor de Tipo
        String[] tipos = {"Geral", "Aluno", "Professor"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();

        // 2. Campos "Extras"
        JTextField txtExtra1 = new JTextField(); // Matrícula ou SIAPE
        JTextField txtExtra2 = new JTextField(); // Curso

        JLabel lblExtra1 = new JLabel("Matrícula/SIAPE:");
        JLabel lblExtra2 = new JLabel("Curso:");

        JButton btnSalvar = new JButton("Salvar Usuário");

        // --- Lógica Visual (O Pulo do Gato) ---
        // Define o comportamento inicial (Geral selecionado -> tudo bloqueado)
        txtExtra1.setEnabled(false);
        txtExtra2.setEnabled(false);
        lblExtra1.setText("---");
        lblExtra2.setText("---");

        // Adiciona o evento de troca
        cbTipo.addActionListener(e -> {
            String tipo = (String) cbTipo.getSelectedItem();

            // Limpa os campos para evitar confusão visual
            txtExtra1.setText("");
            txtExtra2.setText("");

            if ("Aluno".equals(tipo)) {
                // Aluno: Tudo liberado
                txtExtra1.setEnabled(true);
                lblExtra1.setText("Matrícula:");

                txtExtra2.setEnabled(true);
                lblExtra2.setText("Curso:");

            } else if ("Professor".equals(tipo)) {
                // Professor: Só libera o primeiro campo (SIAPE)
                txtExtra1.setEnabled(true);
                lblExtra1.setText("SIAPE:");

                txtExtra2.setEnabled(false); // Bloqueia curso
                lblExtra2.setText("---");

            } else {
                // Geral: Bloqueia tudo
                txtExtra1.setEnabled(false);
                lblExtra1.setText("---");

                txtExtra2.setEnabled(false);
                lblExtra2.setText("---");
            }
        });

        // --- Adicionando ao Painel ---
        panel.add(new JLabel("Tipo de Usuário:")); panel.add(cbTipo);
        panel.add(new JLabel("Nome Completo:")); panel.add(txtNome);
        panel.add(new JLabel("E-mail:")); panel.add(txtEmail);
        panel.add(lblExtra1); panel.add(txtExtra1);
        panel.add(lblExtra2); panel.add(txtExtra2);
        panel.add(new JLabel("")); panel.add(btnSalvar);

        // --- Lógica de Salvar ---
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String tipoSelecionado = (String) cbTipo.getSelectedItem();

            if(nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = biblioteca.gerarIdUsuario();
            Usuario novoUsuario = null;

            if ("Aluno".equals(tipoSelecionado)) {
                novoUsuario = new Aluno(id, nome, email, java.time.LocalDate.now(), "", txtExtra1.getText(), txtExtra2.getText());
            } else if ("Professor".equals(tipoSelecionado)) {
                novoUsuario = new Professor(id, nome, email, java.time.LocalDate.now(), "", txtExtra1.getText());
            } else {
                novoUsuario = new Usuario(id, nome, email);
            }

            biblioteca.cadastrarUsuario(novoUsuario);
            biblioteca.salvarDados();
            JOptionPane.showMessageDialog(this, "Usuário (" + tipoSelecionado + ") cadastrado!");

            // Reseta o formulário para o estado padrão (Geral)
            txtNome.setText(""); txtEmail.setText("");
            cbTipo.setSelectedIndex(0);
        });

        return wrapInPanel(panel);
    }

    // --- TELA 3: EMPRÉSTIMO ---
    private JPanel createPanelEmprestimo() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Empréstimo"));

        JTextField txtIdUserEmp = new JTextField(); // Nome único para esta tela
        JTextField txtIdLivroEmp = new JTextField();
        JButton btnConfirmar = new JButton("Confirmar Empréstimo");

        panel.add(new JLabel("ID do Usuário:")); panel.add(txtIdUserEmp);
        panel.add(new JLabel("ID do Livro:")); panel.add(txtIdLivroEmp);
        panel.add(new JLabel("")); panel.add(btnConfirmar);

        btnConfirmar.addActionListener(e -> {
            try {
                int idU = Integer.parseInt(txtIdUserEmp.getText());
                int idL = Integer.parseInt(txtIdLivroEmp.getText());
                Usuario u = biblioteca.buscarUsuario(idU);
                if (u == null) throw new EmprestimoInvalido("Utilizador não encontrado.");

                biblioteca.emprestarLivro(u, idL);
                JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        return wrapInPanel(panel);
    }

    // --- TELA 4: DEVOLUÇÃO ---
    private JPanel createPanelDevolucao() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Devolução"));

        JTextField txtIdUserDev = new JTextField(); // Nome único para esta tela
        JTextField txtIdLivroDev = new JTextField();
        JButton btnDevolver = new JButton("Confirmar Devolução");

        panel.add(new JLabel("ID do Usuário:")); panel.add(txtIdUserDev);
        panel.add(new JLabel("ID do Livro:")); panel.add(txtIdLivroDev);
        panel.add(new JLabel("")); panel.add(btnDevolver);

        btnDevolver.addActionListener(e -> {
            try {
                int idU = Integer.parseInt(txtIdUserDev.getText());
                int idL = Integer.parseInt(txtIdLivroDev.getText());
                Usuario u = biblioteca.buscarUsuario(idU);
                if (u == null) throw new EmprestimoInvalido("Utilizador não encontrado.");

                biblioteca.devolverLivro(u, idL);
                JOptionPane.showMessageDialog(this, "Livro devolvido com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        return wrapInPanel(panel);
    }

    // --- TELA 5: LISTAGEM ---
    // Em model/SistemaBibliotecaGUI.java

    private JPanel createPanelListagem() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Relatórios e Listagens"));

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- ABA 1: LIVROS ---
        String[] colLivros = {"ID", "Título", "Autor", "Gênero", "Ano", "Disponível"};
        DefaultTableModel modelLivros = new DefaultTableModel(colLivros, 0);
        JTable tableLivros = new JTable(modelLivros);
        JPanel pnlLivros = new JPanel(new BorderLayout());
        pnlLivros.add(new JScrollPane(tableLivros), BorderLayout.CENTER);

        JButton btnAtLivros = new JButton("Atualizar Livros");
        btnAtLivros.addActionListener(e -> {
            modelLivros.setRowCount(0);
            for (Livro l : biblioteca.getLivros()) {
                modelLivros.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getGenero(), l.getAno(), l.getDisponibilidade() ? "Sim" : "Não"});
            }
        });
        pnlLivros.add(btnAtLivros, BorderLayout.SOUTH);
        tabbedPane.addTab("Livros", pnlLivros);

        // --- ABA 2: USUÁRIOS (Resolve sua pendência de listar usuários) ---
        String[] colUsers = {"ID", "Tipo", "Nome", "Email", "Extra (Mat/Siape)"};
        DefaultTableModel modelUsers = new DefaultTableModel(colUsers, 0);
        JTable tableUsers = new JTable(modelUsers);
        JPanel pnlUsers = new JPanel(new BorderLayout());
        pnlUsers.add(new JScrollPane(tableUsers), BorderLayout.CENTER);

        JButton btnAtUsers = new JButton("Atualizar Usuários");
        btnAtUsers.addActionListener(e -> {
            modelUsers.setRowCount(0);
            // Precisamos criar um método getUsuarios() na classe Biblioteca (veja passo 4 abaixo)
            for (Usuario u : biblioteca.getUsuarios()) {
                String tipo = "Geral";
                String extra = "-";

                if (u instanceof Aluno) {
                    tipo = "Aluno";
                    extra = ((Aluno) u).getMatricula();
                } else if (u instanceof Professor) {
                    tipo = "Professor";
                    extra = ((Professor) u).getSiape();
                }

                modelUsers.addRow(new Object[]{u.getId(), tipo, u.getNome(), u.getEmail(), extra});
            }
        });
        pnlUsers.add(btnAtUsers, BorderLayout.SOUTH);
        tabbedPane.addTab("Usuários", pnlUsers);

        // --- ABA 3: EMPRÉSTIMOS (Resolve a pendência de lista de empréstimos) ---
        String[] colEmp = {"Livro", "Usuário", "Data Emp.", "Status"};
        DefaultTableModel modelEmp = new DefaultTableModel(colEmp, 0);
        JTable tableEmp = new JTable(modelEmp);
        JPanel pnlEmp = new JPanel(new BorderLayout());
        pnlEmp.add(new JScrollPane(tableEmp), BorderLayout.CENTER);

        JButton btnAtEmp = new JButton("Atualizar Empréstimos");
        btnAtEmp.addActionListener(e -> {
            modelEmp.setRowCount(0);
            // Varre todos os usuários para pegar o histórico
            for (Usuario u : biblioteca.getUsuarios()) {
                for (Emprestimo emp : u.getHistorico()) {
                    modelEmp.addRow(new Object[]{
                            emp.getLivro().getTitulo(),
                            u.getNome(),
                            emp.getDataEmprestimo(),
                            emp.isAtivo() ? "Em Aberto" : "Devolvido" // Resolve lista de devolvidos
                    });
                }
            }
        });
        pnlEmp.add(btnAtEmp, BorderLayout.SOUTH);
        tabbedPane.addTab("Histórico", pnlEmp);

        panel.add(tabbedPane, BorderLayout.CENTER);
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
