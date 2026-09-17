package janela;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;



interface WindowConfigurer {
    void configure(JFrame window);
}


class DefaultWindowConfigurer implements WindowConfigurer {

    private Font font = new Font("Arial", Font.PLAIN, 14);
	private JButton JButton;

    public void configure(JFrame window) {
        window.setFont(font);
    }

    public void setFont(Font font) {
        this.font = font;
    }
    public void estilizedButton(JButton button) {
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(
                        BevelBorder.RAISED,
                        Color.RED, Color.BLACK),
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED))); 
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setContentAreaFilled(false); 
        button.setOpaque(true);             
        button.setFocusPainted(false);      
        button.setForeground(Color.BLACK);
        
        button.setPreferredSize(new Dimension(200, 40)); 
        
        Color corNormal = Color.RED.darker();
        Color corHover = Color.RED.darker().darker(); 
        Color corClick = Color.RED.darker().darker().darker(); 
        
        
        button.setBackground(corNormal);
        
       
        button.getModel().addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                
                if (model.isPressed()) {
                    button.setBackground(corClick); 
                } else if (model.isRollover()) {
                    button.setBackground(corHover); 
                } else {
                    button.setBackground(corNormal); 
                }
            }
        });
    }
}

class Something extends JFrame {
 
	private Diario meuDiario;
    private DefaultTableModel modeloTabela;
    private JTable tabelaAlunos;
    public Something(Diario diario) {
        this.meuDiario = diario;
       
        setTitle("Minha Janela Swing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel painelbotoes = new JPanel();
        painelbotoes.setLayout(new GridLayout(10, 1, 2, 2));
        painelbotoes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        DefaultWindowConfigurer configurer = new DefaultWindowConfigurer();
       
        JPanel painelBtnRegistrar = new JPanel();
        JButton btnRegistrarAluno = new JButton("Registrar novo aluno");
        painelBtnRegistrar.add(btnRegistrarAluno);
        configurer.estilizedButton(btnRegistrarAluno);
        JPanel painelBtnEditarNota = new JPanel();
        JButton btnEditarNota = new JButton("Editar nota");
        painelBtnEditarNota.add(btnEditarNota);
        configurer.estilizedButton(btnEditarNota);
        JPanel painelBtnAtt = new JPanel();
        JButton btnAtt = new JButton("Atualizar");
        painelBtnAtt.add(btnAtt);
        configurer.estilizedButton(btnAtt);
        JPanel painelBtnHistograma = new JPanel();
        JButton btnHistograma = new JButton("Gerar Histograma");
        painelBtnHistograma.add(btnHistograma);
        configurer.estilizedButton(btnHistograma);

        painelbotoes.add(btnRegistrarAluno);
        painelbotoes.add(btnEditarNota);
        painelbotoes.add(btnAtt);
        painelbotoes.add(btnHistograma);
        add(painelbotoes, BorderLayout.WEST);


        int qtdeNotas = meuDiario.getQuantidadeNotas();
         String [] colunas = new String[qtdeNotas + 2];
         colunas[0] = "Aluno";

         for (int i = 0; i<qtdeNotas; i++){
            colunas[i+1] = "Nota " + (i+1);
         }
         colunas[qtdeNotas+1] = "Média";
         
         modeloTabela = new DefaultTableModel(colunas, 0) {
        	 @Override
        	 public boolean isCellEditable(int row, int column) {
        		 if (column > 0 && column <= qtdeNotas) {
        			 return true;
        		 }else return false;
        	 }
        	 @Override
             public void setValueAt(Object aValue, int row, int column) {
                 try {
                    
                     int novaNota = Integer.parseInt(aValue.toString());
                     
                    
                     String nomeAluno = getValueAt(row, 0).toString();
                     
                    meuDiario.modificarNota(nomeAluno, column, novaNota);
                     
                     super.setValueAt(novaNota, row, column);
                     
                     SwingUtilities.invokeLater(() -> atualizarTabela());

                 } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(null, "Por favor, digite apenas números inteiros.");
                 } catch (Exception ex) {
                     JOptionPane.showMessageDialog(null, "Erro ao salvar a nota: " + ex.getMessage());
                 }
             }
        };
        //modeloTabela = new DefaultTableModel(colunas, 0);

        tabelaAlunos = new JTable(modeloTabela);
      
        
      
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        
        
        add(scrollPane, BorderLayout.CENTER);


      
        btnRegistrarAluno.addActionListener(e -> {
            try {
                String nomeDigitado = JOptionPane.showInputDialog(this, "Digite o nome do novo aluno:");
                if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()) {
                    meuDiario.adicionarAluno(nomeDigitado);
                    JOptionPane.showMessageDialog(this, "Aluno registrado");
                    atualizarTabela();
                }
            } catch (Exception ex) {
              
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
            }
        
        });

                btnEditarNota.addActionListener(e -> {
            try {
                String nomeDigitado = JOptionPane.showInputDialog(this, "Digite o nome do aluno:");
                if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()) {
                    String numStr = JOptionPane.showInputDialog("Qual é a prova?");
                    int numNota = Integer.parseInt(numStr);
                    String valorStr = JOptionPane.showInputDialog("Digite o valor da nota:");
                    int valorNota = Integer.parseInt(valorStr);

                    meuDiario.modificarNota(nomeDigitado, numNota, valorNota);
                    JOptionPane.showMessageDialog(this, "Nota atualizada.");
                    atualizarTabela();
                }
            } catch (Exception ex) {
              
                JOptionPane.showMessageDialog(this, "Erro ao editar nota: " + ex.getMessage());
            }

            
        
        });

      
        btnAtt.addActionListener(e -> atualizarTabela());
        btnHistograma.addActionListener(e -> {
            try {
                String textoDoHistograma = meuDiario.gerarHistograma();
                JTextArea areatexto = new JTextArea(textoDoHistograma);
                areatexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
                areatexto.setEditable(false);
                areatexto.setBackground(null);
                JOptionPane.showMessageDialog(this, areatexto, "Histograma", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar histograma: " + ex.getMessage());
            }
        }

        );

       
        atualizarTabela();
    }

    
    public void atualizarTabela() {
       
        modeloTabela.setRowCount(0);
        int qtdeNotas = meuDiario.getQuantidadeNotas();
       
        
        Aluno[] lista = meuDiario.getListaDeAlunos(); 
        for (int i = 0; i < meuDiario.getQuantidadeCadastrados(); i++) {
        Aluno a = lista[i];
           
        Object[] linha =  new Object[qtdeNotas + 2];
        linha[0] = a.getNomeAluno();
        int [] notas = a.getNotasAluno();
        for (int j = 0; j < qtdeNotas; j++){
            linha[j+1] = notas[j];
        }
        linha[qtdeNotas + 1] = String.format("%.2f", a.calcularMedia());
        modeloTabela.addRow(linha);
        }
    } 
    
 
        /*btnRegistrarAluno.addActionListener(e -> {
            String nomeDigitado = JOptionPane.showInputDialog( this,
            "Digite o nome do novo aluno:",
            "Cadastro do aluno",
            JOptionPane.QUESTION_MESSAGE
        );

        if (nomeDigitado != null && !nomeDigitado.trim().isEmpty()){
            meuDiario.adicionarAluno(nomeDigitado);
            JOptionPane.showMessageDialog(
                this,
                "Aluno " + nomeDigitado + " cadastrado com sucesso.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            
            );
        }
           
   
        });*/
}
    



public class window {
    public static void main(String[] args) {
        String turma = JOptionPane.showInputDialog(
            null,
            "Qual o nome da turma?",
            "Lyceum2",
            JOptionPane.QUESTION_MESSAGE

        );

        String alunosStr = JOptionPane.showInputDialog(null, "Quantos alunos tem na sala?");
        int alunos = Integer.parseInt(alunosStr); 
        
        
        String notasStr = JOptionPane.showInputDialog(null, "Quantas notas por aluno precisa?");
        int notas = Integer.parseInt(notasStr);
      
        Diario meuDiario = new Diario(alunos + 50, notas);
        for (int i = 0; i< alunos; i++){
            String nomeDigitado = JOptionPane.showInputDialog(
                null,
                "Digite o nome do " + (i+1) + " aluno: "

            );
            meuDiario.adicionarAluno(nomeDigitado);
        }

        JOptionPane.showMessageDialog(null, "Turma " + turma + " Criada");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Something something = new Something(meuDiario);
                WindowConfigurer windowConfigurer = new DefaultWindowConfigurer();
                windowConfigurer.configure(something);
                something.setTitle("Lyceum 2.0");
                something.setVisible(true); 
            }
        });
    }
}
