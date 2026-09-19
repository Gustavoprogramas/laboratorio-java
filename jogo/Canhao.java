package src;

public class Canhao {

    int x;
    int y;

    int largura;
    int altura;


    int direcao;

    int contadorTiro = 0;

    public Canhao(
            int x,
            int y,
            int largura,
            int altura,
            int direcao
    ) {

        this.x = x;
        this.y = y;

        this.largura = largura;
        this.altura = altura;

        this.direcao = direcao;
    }
}