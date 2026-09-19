package src;

import java.awt.Rectangle;

public class Projetil {

    double x;
    double y;

    int largura;
    int altura;

    double xInicial;
    double yInicial;
    double velocidadeX;
    double velocidadeY;

    boolean ativo = true;

    public Projetil(
            double x,
            double y,
            
            int largura,
            int altura,
            double velocidadeX,
            double velocidadeY
    ) {

        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;

        this.largura = largura;
        this.altura = altura;

        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
    }


    public void atualizar() {

        x += velocidadeX;
        y += velocidadeY;
    }


    public Rectangle getHitbox() {

        return new Rectangle(
                (int) x,
                (int) y,
                largura,
                altura
        );
    }
}