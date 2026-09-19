package src;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Fase {

        int playerX;
        int playerY;

        int batataX;
        int batataY;

        int mundoLargura;
        int mundoAltura;

        ArrayList<Rectangle> plataformas;
        ArrayList<Rectangle> espinhos;
        ArrayList<Canhao> canhoes;


        public Fase(
                        int playerX,
                        int playerY,
                        int batataX,
                        int batataY) {

                this(
                                playerX,
                                playerY,
                                batataX,
                                batataY,
                                1280,
                                720);
        }


        public Fase(
                        int playerX,
                        int playerY,
                        int batataX,
                        int batataY,
                        int mundoLargura,
                        int mundoAltura) {

                this.playerX = playerX;
                this.playerY = playerY;

                this.batataX = batataX;
                this.batataY = batataY;

                this.mundoLargura = mundoLargura;
                this.mundoAltura = mundoAltura;

                plataformas = new ArrayList<>();
                espinhos = new ArrayList<>();
                canhoes = new ArrayList<>();
        }


        public void adicionarPlataforma(
                        int x,
                        int y,
                        int largura,
                        int altura) {

                plataformas.add(
                                new Rectangle(
                                                x,
                                                y,
                                                largura,
                                                altura));
        }


        public void adicionarCanhao(
                        int x,
                        int y,
                        int largura,
                        int altura,
                        int direcao) {

                canhoes.add(
                                new Canhao(
                                                x,
                                                y,
                                                largura,
                                                altura,
                                                direcao));
        }

        public void adicionarEspinho(
                        int x,
                        int y,
                        int largura,
                        int altura) {

                espinhos.add(
                                new Rectangle(
                                                x,
                                                y,
                                                largura,
                                                altura));
        }
}