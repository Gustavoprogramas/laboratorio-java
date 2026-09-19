package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MenuFasesPanel extends JPanel {

    interface SelecionarFaseListener {
        void selecionar(int fase);
    }

    private Clip somHover;
    private Clip somClick;  
    private GamePanel jogo;

    private SelecionarFaseListener listener;


    private JPanel painelFases;

    private JScrollPane scrollPane;



    private final Color MARROM_CLARO =
            new Color(181, 140, 103);

    private final Color BEGE =
            new Color(235, 215, 180);

    private final Color BEGE_HOVER =
            new Color(245, 229, 200);

    private final Color MARROM_TEXTO =
            new Color(90, 60, 40);

    private final Color BORDA =
            new Color(120, 85, 55);


    public MenuFasesPanel(
            GamePanel jogo,
            SelecionarFaseListener listener
    ) {

        this.jogo = jogo;

        this.listener = listener;


        setBackground(
                MARROM_CLARO
        );


        setLayout(
                new BorderLayout()
        );



        JLabel titulo =
                new JLabel(
                        "SELECIONE UMA FASE",
                        SwingConstants.CENTER
                );


        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        38
                )
        );


        titulo.setForeground(
                MARROM_TEXTO
        );


        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        35,
                        10,
                        25,
                        10
                )
        );


        add(
                titulo,
                BorderLayout.NORTH
        );



        painelFases =
                new JPanel();


        painelFases.setBackground(
                MARROM_CLARO
        );



        scrollPane =
                new JScrollPane(
                        painelFases
                );


        scrollPane.setBorder(null);

        scrollPane.setOpaque(false);

        scrollPane.getViewport()
                .setBackground(
                        MARROM_CLARO
                );


        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );


        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );


        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(20);


        add(
                scrollPane,
                BorderLayout.CENTER
        );



        addComponentListener(
                new ComponentAdapter() {

                    @Override
                    public void componentResized(
                            ComponentEvent e
                    ) {

                        reorganizar();
                    }
                }
        );

        somHover = carregarSom(
        "/assets/sons/hover.wav"
);

somClick = carregarSom(
        "/assets/sons/click.wav"
);


        atualizarFases();
    }

    private Clip carregarSom(String caminho) {

    try {

        AudioInputStream audio =
                AudioSystem.getAudioInputStream(
                        getClass().getResource(caminho)
                );

        Clip clip =
                AudioSystem.getClip();

        clip.open(audio);

        return clip;

    } catch (Exception e) {

        System.out.println(
                "Erro ao carregar som: "
                + caminho
        );

        e.printStackTrace();

        return null;
    }
}


private void tocarSom(Clip som) {

    if (som == null) {
        return;
    }

    som.stop();

    som.setFramePosition(0);

    som.start();
}


    
    public void atualizarFases() {

        painelFases.removeAll();


        int quantidade =
                jogo.getQuantidadeFases();



        for (
                int i = 0;
                i < quantidade;
                i++
        ) {

            final int indiceFase = i;


            JButton botao =
                    criarBotaoFase(
                            i + 1
                    );


            botao.addActionListener(
                    e -> {
                        tocarSom(somClick);
                        listener.selecionar(
                                indiceFase
                        );
                    }
            );


            painelFases.add(
                    botao
            );
        }


        reorganizar();


        painelFases.revalidate();

        painelFases.repaint();
    }



    private JButton criarBotaoFase(
            int numero
    ) {

        JButton botao =
                new JButton(
                        "FASE " + numero
                );


        botao.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );


        botao.setForeground(
                MARROM_TEXTO
        );


        botao.setBackground(
                BEGE
        );


        botao.setFocusPainted(
                false
        );


        botao.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        botao.setPreferredSize(
                new Dimension(
                        190,
                        120
                )
        );


        botao.setBorder(
                BorderFactory.createLineBorder(
                        BORDA,
                        3
                )
        );



        botao.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        botao.setBackground(
                                BEGE_HOVER
                        );
                        tocarSom(somHover);
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        botao.setBackground(
                                BEGE
                        );
                    }
                }
        );


        return botao;
    }



    private void reorganizar() {

        int larguraAtual =
                getWidth();


        if (larguraAtual <= 0) {

            larguraAtual = 1280;
        }


        int colunas;


        if (larguraAtual < 700) {

            colunas = 2;

        }

        else if (larguraAtual < 1000) {

            colunas = 3;

        }

        else if (larguraAtual < 1400) {

            colunas = 4;

        }

        else {

            colunas = 5;
        }


        painelFases.setLayout(
                new GridLayout(
                        0,
                        colunas,
                        25,
                        25
                )
        );


        painelFases.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        80,
                        60,
                        80
                )
        );


        painelFases.revalidate();

        painelFases.repaint();
    }
}