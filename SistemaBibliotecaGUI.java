package model;

import model.Excecoes.EmprestimoInvalido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class SistemaBibliotecaGUI extends JFrame {

    // Instância da "Fachada" do sistema
    private final Biblioteca biblioteca;

    // Componentes de navegação
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    public SistemaBibliotecaGUI() {
        // 1. Inicializa a biblioteca
        this.biblioteca = new Biblioteca();

        // CORREÇÃO ESSENCIAL: Carrega os dados assim que abre a janela!
        this.biblioteca.carregarDados();

        // Configuração da Janela
        setTitle("Sistema de Biblioteca - Trabalho Final POO");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // CORREÇÃO: Salvar ao clicar no "X" da janela
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarESair();
            }
        });

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

    // Método auxiliar para salvar e fechar com segurança
    private void salvarESair() {
        System.out.println("Salvando dados antes de fechar...");
        biblioteca.salvarDados();
        System.exit(0);
    }

    // --- MENU LATERAL ---
    private JPanel createMenuPanel() {
        JPanel menu = new JPanel(new GridLayout(6, 1, 10, 10));
        menu.setBackground(new Color(60, 63, 65));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnLivro = createMenuButton("Cadastrar Livro");
        JButton btnUsuario = createMenuButton("Cadastrar Usuário");
        JButton btnEmprestimo = createMenuButton("Empréstimo");
        JButton btnDevolucao = createMenuButton("Devolução");
        JButton btnListagem = createMenuButton("Relatórios");
        JButton btnSair = createMenuButton("Sair");

        // Ações de Navegação
        btnLivro.addActionListener(e -> cardLayout.show(mainPanel, "Livro"));
        btnUsuario.addActionListener(e -> cardLayout.show(mainPanel, "Usuario"));
        btnEmprestimo.addActionListener(e -> cardLayout.show(mainPanel, "Emprestimo"));
        btnDevolucao.addActionListener(e -> cardLayout.show(mainPanel, "Devolucao"));
        btnListagem.addActionListener(e -> cardLayout.show(mainPanel, "Listagem"));

        // CORREÇÃO: Botão Sair agora salva antes de fechar
        btnSair.addActionListener(e -> salvarESair());

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
                short ano = Short.parseShort(txtAno.getText());

                if(titulo.isEmpty() || autor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha título e autor!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                biblioteca.adicionarLivro(titulo, autor, genero, ano);
                biblioteca.salvarDados(); // Salva imediatamente para segurança

                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
                txtTitulo.setText(""); txtAutor.setText("");
                txtGenero.setText(""); txtAno.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return wrapInPanel(panel);
    }

    // --- TELA 2: CADASTRO DE USUÁRIO (AQUELE CORRIGIDO COM COMBOBOX) ---
    private JPanel createPanelCadastroUsuario() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Novo Usuário"));

        String[] tipos = {"Geral", "Aluno", "Professor"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();

        JTextField txtExtra1 = new JTextField(); // Matrícula ou SIAPE
        JTextField txtExtra2 = new JTextField(); // Curso

        JLabel lblExtra1 = new JLabel("Matrícula/SIAPE:");
        JLabel lblExtra2 = new JLabel("Curso:");

        JButton btnSalvar = new JButton("Salvar Usuário");

        // Estado inicial (Geral)
        txtExtra1.setEnabled(false);
        txtExtra2.setEnabled(false);
        lblExtra1.setText("---");
        lblExtra2.setText("---");

        cbTipo.addActionListener(e -> {
            String tipo = (String) cbTipo.getSelectedItem();
            txtExtra1.setText(""); txtExtra2.setText("");

            if ("Aluno".equals(tipo)) {
                txtExtra1.setEnabled(true); lblExtra1.setText("Matrícula:");
                txtExtra2.setEnabled(true); lblExtra2.setText("Curso:");
            } else if ("Professor".equals(tipo)) {
                txtExtra1.setEnabled(true); lblExtra1.setText("SIAPE:");
                txtExtra2.setEnabled(false); lblExtra2.setText("---");
            } else {
                txtExtra1.setEnabled(false); lblExtra1.setText("---");
                txtExtra2.setEnabled(false); lblExtra2.setText("---");
            }
        });

        panel.add(new JLabel("Tipo de Usuário:")); panel.add(cbTipo);
        panel.add(new JLabel("Nome Completo:")); panel.add(txtNome);
        panel.add(new JLabel("E-mail:")); panel.add(txtEmail);
        panel.add(lblExtra1); panel.add(txtExtra1);
        panel.add(lblExtra2); panel.add(txtExtra2);
        panel.add(new JLabel("")); panel.add(btnSalvar);

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
            biblioteca.salvarDados(); // Salva imediatamente
            JOptionPane.showMessageDialog(this, "Usuário (" + tipoSelecionado + ") cadastrado!");

            txtNome.setText(""); txtEmail.setText(""); cbTipo.setSelectedIndex(0);
        });

        return wrapInPanel(panel);
    }

    // --- TELA 3: EMPRÉSTIMO ---
    private JPanel createPanelEmprestimo() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Empréstimo"));

        JTextField txtIdUserEmp = new JTextField();
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
                if (u == null) throw new EmprestimoInvalido("Usuário não encontrado.");

                biblioteca.emprestarLivro(u, idL);
                biblioteca.salvarDados(); // Salva para persistir o vínculo
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

        JTextField txtIdUserDev = new JTextField();
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
                if (u == null) throw new EmprestimoInvalido("Usuário não encontrado.");

                biblioteca.devolverLivro(u, idL);
                biblioteca.salvarDados(); // Salva a atualização
                JOptionPane.showMessageDialog(this, "Livro devolvido com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        return wrapInPanel(panel);
    }

    // --- TELA 5: LISTAGEM (ATUALIZADA COM AS ABAS) ---
    private JPanel createPanelListagem() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Relatórios e Listagens"));

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. ABA LIVROS
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

        // 2. ABA USUÁRIOS
        String[] colUsers = {"ID", "Tipo", "Nome", "Email", "Extra"};
        DefaultTableModel modelUsers = new DefaultTableModel(colUsers, 0);
        JTable tableUsers = new JTable(modelUsers);
        JPanel pnlUsers = new JPanel(new BorderLayout());
        pnlUsers.add(new JScrollPane(tableUsers), BorderLayout.CENTER);

        JButton btnAtUsers = new JButton("Atualizar Usuários");
        btnAtUsers.addActionListener(e -> {
            modelUsers.setRowCount(0);
            for (Usuario u : biblioteca.getUsuarios()) {
                String tipo = "Geral";
                String extra = "-";
                if (u instanceof Aluno) {
                    tipo = "Aluno"; extra = ((Aluno) u).getMatricula();
                } else if (u instanceof Professor) {
                    tipo = "Professor"; extra = ((Professor) u).getSiape();
                }
                modelUsers.addRow(new Object[]{u.getId(), tipo, u.getNome(), u.getEmail(), extra});
            }
        });
        pnlUsers.add(btnAtUsers, BorderLayout.SOUTH);
        tabbedPane.addTab("Usuários", pnlUsers);

        // 3. ABA HISTÓRICO
        String[] colEmp = {"Livro", "Usuário", "Data Emp.", "Status"};
        DefaultTableModel modelEmp = new DefaultTableModel(colEmp, 0);
        JTable tableEmp = new JTable(modelEmp);
        JPanel pnlEmp = new JPanel(new BorderLayout());
        pnlEmp.add(new JScrollPane(tableEmp), BorderLayout.CENTER);

        JButton btnAtEmp = new JButton("Atualizar Empréstimos");
        btnAtEmp.addActionListener(e -> {
            modelEmp.setRowCount(0);
            for (Usuario u : biblioteca.getUsuarios()) {
                for (Emprestimo emp : u.getHistorico()) {
                    modelEmp.addRow(new Object[]{
                            emp.getLivro().getTitulo(),
                            u.getNome(),
                            emp.getDataEmprestimo(),
                            emp.isAtivo() ? "Em Aberto" : "Devolvido"
                    });
                }
            }
        });
        pnlEmp.add(btnAtEmp, BorderLayout.SOUTH);
        tabbedPane.addTab("Histórico", pnlEmp);

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel wrapInPanel(JPanel form) {
        JPanel wrapper = new JPanel(new FlowLayout());
        wrapper.add(form);
        return wrapper;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new SistemaBibliotecaGUI().setVisible(true);
        });
    }
}