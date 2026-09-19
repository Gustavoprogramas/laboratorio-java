package src;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame janela =
                new JFrame(
                        "Steph e a Batata"
                );



        CardLayout cardLayout =
                new CardLayout();

        JPanel telas =
                new JPanel(
                        cardLayout
                );



        GamePanel jogo =
                new GamePanel();



        MenuFasesPanel menu =
        new MenuFasesPanel(
                jogo,

                faseSelecionada -> {

                    jogo.iniciarFase(
                            faseSelecionada
                    );

                    cardLayout.show(
                            telas,
                            "GAME"
                    );

                    jogo.requestFocusInWindow();
                }
        );



        telas.add(
                menu,
                "MENU"
        );

        telas.add(
                jogo,
                "GAME"
        );


        janela.add(
                telas
        );


        janela.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        janela.setResizable(false);

        janela.setSize(
                1280,
                720
        );

        janela.setLocationRelativeTo(
                null
        );


        janela.setVisible(
                true
        );


        cardLayout.show(
                telas,
                "MENU"
        );


        jogo.startGame();
    }
}